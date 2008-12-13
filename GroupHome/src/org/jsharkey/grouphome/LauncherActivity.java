/*
	Copyright (C) 2008 Jeffrey Sharkey, http://jsharkey.org/
	
	This program is free software: you can redistribute it and/or modify
	it under the terms of the GNU General Public License as published by
	the Free Software Foundation, either version 3 of the License, or
	(at your option) any later version.
	
	This program is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	GNU General Public License for more details.
	
	You should have received a copy of the GNU General Public License
	along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package org.jsharkey.grouphome;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ExpandableListActivity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class LauncherActivity extends ExpandableListActivity implements OnCreateContextMenuListener, OnClickListener {
	
	public static final String TAG = LauncherActivity.class.toString();
	
	public static final String JSON_GROUP = "{'Apps: Communication': ['com.android.contacts','com.android.mms','com.android.browser','com.android.im','com.android.email','com.google.android.gm'],'Apps: Multimedia': ['com.android.camera','com.android.music','com.amazon.mp3','com.google.android.youtube'],'Apps: Travel': ['com.google.android.apps.maps'],'Apps: Tools': ['com.android.alarmclock','com.android.calculator2','com.android.voicedialer','com.android.vending','com.android.settings'],'Apps: Productivity':['com.android.calendar','com.android.todo','com.android.notepad'],'Apps: Demo': ['com.example.android.apis','com.android.development']}";
	
	class EntryInfo {
		ResolveInfo resolveInfo;
		CharSequence title;
		Drawable icon;
	}
	
	public String GROUP_UNKNOWN = "Uncategorized";

	// try resolving group for given package name
	// we should try caching this information for later speed-up
	protected String resolveGroup(JSONObject groupMap, String packageName) {
		try {
			for(Iterator keys = groupMap.keys(); keys.hasNext(); ) {
				String groupName = (String)keys.next();
				JSONArray packages = groupMap.getJSONArray(groupName);
				for(int i = 0; i < packages.length(); i++) {
					if(packageName.equals(packages.getString(i)))
						return groupName;
				}
			}
		} catch(Exception e) {
			Log.e(TAG, "Problem while trying to resolve group", e);
		}
		return GROUP_UNKNOWN;
	}
	
	private LayoutInflater inflater;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.act_launch);
		
		this.inflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		// use our group adapter to help organize applications
		// offer various modes, including a decay rate
		// type to narrow down list
		
		// provide menu option to switch to other home app (through intent?)
		// or at least point to settings, or settings remove default intent
		
		// show as collapseable category list
		//   default expand/collapse behavior
		// remember open/closed status when coming back later
		
		// settings:  -- always open, always closed, remember last
		//			     check for category updates
		//				 install order, or alphabetical
		// menus:     -- normal home, search, open all/close all, settings
		
		// no window title, show user progress while scanning items
		// try latching onto new package events

		// allow focus inside of rows to select children
		getExpandableListView().setItemsCanFocus(true);

		new ProcessTask().execute();
		
	}
	

    private class ProcessTask extends UserTask<Void, Void, GroupAdapter> {
		public GroupAdapter doInBackground(Void... params) {

			JSONObject groupMap = new JSONObject();
			try {
				groupMap = new JSONObject(JSON_GROUP);
			} catch(Exception e) {
				Log.e(TAG, "Problem parsing incoming groups", e);
			}
			
			// final map used to store category mappings
			Map<String,List<EntryInfo>> entryMap = new HashMap<String,List<EntryInfo>>();
			
			PackageManager pm = (PackageManager)getPackageManager();
			
			// search for all launchable apps
			Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
			mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
			 
			List<ResolveInfo> apps = pm.queryIntentActivities(mainIntent, 0); 
			for(ResolveInfo info : apps) {
				
				EntryInfo entry = new EntryInfo();
				
				// load details about this app
				entry.resolveInfo = info;
				entry.icon = info.loadIcon(pm);
				entry.title = info.loadLabel(pm);
				if(entry.title == null)
					entry.title = info.activityInfo.name;

				// use package to help categorize this app
				// TODO: try using database caching here to speed-up resolution
				String packageName = info.activityInfo.packageName;
				String groupName = resolveGroup(groupMap, packageName);
				
				Log.d(TAG, String.format("found groupName=%s for packageName=%s", groupName, packageName));

//				CharSequence desc = info.activityInfo.applicationInfo.loadDescription(pm);
//				Log.d(TAG, String.format("has desc=%s", desc));
				
				// ensure that we have a list for this category
				if(!entryMap.containsKey(groupName))
					entryMap.put(groupName, new LinkedList<EntryInfo>());
				
				entryMap.get(groupName).add(entry);
				
			}
			
			// now that app tree is built, pass along to adapter
			return new GroupAdapter(entryMap);

		}

		@Override
		public void end(GroupAdapter result) {
			updateColumns(result, getResources().getConfiguration());
			setListAdapter(result);
			
			getExpandableListView().requestFocus();

		}

	}
	
	/**
	 * Force columns shown in adapter based on orientation.
	 */
	private void updateColumns(GroupAdapter adapter, Configuration config) {
		adapter.setColumns((config.orientation == Configuration.ORIENTATION_PORTRAIT) ? 4 : 6);
	}
	
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		this.updateColumns((GroupAdapter)this.getExpandableListAdapter(), newConfig);
	}
	
	/**
	 * Special adapter to help provide application lists using expandable
	 * categories. Specifically, it folds child items into grid-like columns
	 * based on setColumns(), which is a hack. While it correctly recycles rows
	 * when possible, this could be written much better.
	 */
	public class GroupAdapter extends BaseExpandableListAdapter {

		private Map<String,List<EntryInfo>> entryMap;
		private String[] groupNames;
		
		private int columns = -1;
		
		public GroupAdapter(Map<String,List<EntryInfo>> entryMap) {
			this.entryMap = entryMap;
			this.groupNames = entryMap.keySet().toArray(new String[] {});
			Arrays.sort(this.groupNames);
		}

		/**
		 * Force the number of columns to use when wrapping child elements.
		 * Inflated children should have static widths.
		 */
		public void setColumns(int columns) {
			this.columns = columns;
			this.notifyDataSetInvalidated();
		}
		
		public Object getGroup(int groupPosition) {
			return this.groupNames[groupPosition];
		}

		public int getGroupCount() {
			return this.groupNames.length;
		}

		public long getGroupId(int groupPosition) {
			return groupPosition;
		}

		public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
			if (convertView == null)
				convertView = inflater.inflate(R.layout.item_header, parent, false);

			String group = (String) this.getGroup(groupPosition);
			((TextView) convertView.findViewById(android.R.id.text1)).setText(group);

			return convertView;
		}

		public Object getChild(int groupPosition, int childPosition) {
			// no good value when a row is actually multiple children
			return null;
		}

		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		public int getChildrenCount(int groupPosition) {
			// wrap children items into rows using column count
			int actualCount = entryMap.get(groupNames[groupPosition]).size();
			return (actualCount + (columns - 1)) / columns;
		}

		public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
			
			if(convertView == null)
				convertView = inflater.inflate(R.layout.item_row, parent, false);

			final ViewGroup viewGroup = (ViewGroup)convertView;
			
			// TODO: try recycling any existing children instead of rebuilding
			viewGroup.removeAllViews();
			
			List<EntryInfo> actualChildren = entryMap.get(groupNames[groupPosition]);
			int start = childPosition * columns, end = (childPosition + 1) * columns;
			
			for(int i = start; i < end; i++) {
				final EntryInfo info = actualChildren.get(i);
				
				// inflate child elements if they dont already exist
				if(end > actualChildren.size()) end = actualChildren.size();
	
				final TextView textView = (TextView)inflater.inflate(R.layout.item_entry, parent, false);
				
				textView.setCompoundDrawablesWithIntrinsicBounds(null, info.icon, null, null);
				textView.setText(info.title);
				
				textView.setTag(info);
				textView.setOnClickListener(LauncherActivity.this);
				textView.setOnCreateContextMenuListener(LauncherActivity.this);
				
				viewGroup.addView(textView);
			}
			
			return convertView;
		}

		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}

		public boolean hasStableIds() {
			return true;
		}

	}
	
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
		if(!(v.getTag() instanceof EntryInfo)) return;
		EntryInfo info = (EntryInfo)v.getTag();
		
		String packageName = info.resolveInfo.activityInfo.applicationInfo.packageName;

		menu.setHeaderTitle(info.title);
		
		// TODO: we shouldnt rely on this entrance into the settings app
		Intent detailsIntent = new Intent();
		detailsIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
		detailsIntent.putExtra("com.android.settings.ApplicationPkgName", packageName);
		MenuItem details = menu.add("App details");
		details.setIntent(detailsIntent);
		
		Intent deleteIntent = new Intent(Intent.ACTION_DELETE);
		deleteIntent.setData(Uri.parse("package:" + packageName));
		MenuItem delete = menu.add("Uninstall");
		delete.setIntent(deleteIntent);
		
		MenuItem favorite = menu.add("Add to favorites");
		
	}

	public void onClick(View v) {
		if(!(v.getTag() instanceof EntryInfo)) return;
		EntryInfo info = (EntryInfo)v.getTag();
		
		// build actual intent for launching app
		Intent launch = new Intent(Intent.ACTION_MAIN);
		launch.addCategory(Intent.CATEGORY_LAUNCHER);
		launch.setComponent(new ComponentName(info.resolveInfo.activityInfo.applicationInfo.packageName, info.resolveInfo.activityInfo.name));
		launch.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
		this.startActivity(launch);
		
	}


}