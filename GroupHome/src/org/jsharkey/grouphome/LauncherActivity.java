package org.jsharkey.grouphome;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ExpandableListActivity;
import android.app.ListActivity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class LauncherActivity extends ExpandableListActivity {
	
	public static final String TAG = LauncherActivity.class.toString();
	
	public static final String JSON_GROUP = "{'Apps: Communication': ['com.android.contacts','com.android.mms','com.android.browser','com.android.camera','com.android.music'],'Apps: Multimedia': [],'Apps: Travel': ['com.google.android.apps.maps'],'Apps: Tools': ['com.android.alarmclock','com.android.calculator2'],'Apps: Demo': ['com.example.android.apis','com.android.development']}";
	
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
		// menus:     -- normal home, open all/close all, settings, search
		
		JSONObject groupMap = new JSONObject();
		try {
			groupMap = new JSONObject(JSON_GROUP);
		} catch(Exception e) {
			Log.e(TAG, "Problem parsing incoming groups", e);
		}
		
		// final map used to store category mappings
		Map<String,List<EntryInfo>> entryMap = new HashMap<String,List<EntryInfo>>();
		
		PackageManager pm = (PackageManager)this.getPackageManager();
		
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
			
			CharSequence desc = info.activityInfo.applicationInfo.loadDescription(pm);
			
			Log.d(TAG, String.format("found groupName=%s for packageName=%s", groupName, packageName));
			Log.d(TAG, String.format("has desc=%s", desc));
			
			// ensure that we have a list for this category
			if(!entryMap.containsKey(groupName))
				entryMap.put(groupName, new LinkedList<EntryInfo>());
			
			entryMap.get(groupName).add(entry);
			entryMap.get(groupName).add(entry);
			entryMap.get(groupName).add(entry);
			
			// hold off creating actual intent until launched later
			
//			Intent launch = new Intent(Intent.ACTION_MAIN);
//			launch.addCategory(Intent.CATEGORY_LAUNCHER);
//			launch.setComponent(new ComponentName(info.activityInfo.applicationInfo.packageName, info.activityInfo.name));
//			launch.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
			
		}
		
		
		// now that app tree is built, pass along to adapter
		adapter = new GroupAdapter(entryMap);
		
		this.updateColumns(this.getResources().getConfiguration());
		this.setListAdapter(adapter);
		
		// allow focus inside of rows to select children
		this.getExpandableListView().setItemsCanFocus(true);

		
	}
	
	private GroupAdapter adapter = null;
	
	/**
	 * Force columns shown in adapter based on orientation.
	 */
	private void updateColumns(Configuration config) {
		adapter.setColumns((config.orientation == Configuration.ORIENTATION_PORTRAIT) ? 4 : 6);
	}
	
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		this.updateColumns(newConfig);
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


}