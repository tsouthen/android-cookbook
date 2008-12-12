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
			
			Log.d(TAG, String.format("found groupName=%s for packageName=%s", groupName, packageName));
			
			// ensure that we have a list for this category
			if(!entryMap.containsKey(groupName))
				entryMap.put(groupName, new LinkedList<EntryInfo>());
			
			entryMap.get(groupName).add(entry);
			entryMap.get(groupName).add(entry);
			entryMap.get(groupName).add(entry);
			entryMap.get(groupName).add(entry);
			entryMap.get(groupName).add(entry);
			entryMap.get(groupName).add(entry);
			entryMap.get(groupName).add(entry);
			
			// hold off creating actual intent until launched later
			
//			Intent launch = new Intent(Intent.ACTION_MAIN);
//			launch.addCategory(Intent.CATEGORY_LAUNCHER);
//			launch.setComponent(new ComponentName(info.activityInfo.applicationInfo.packageName, info.activityInfo.name));
//			launch.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
			
		}
		
		// create expander for each group
//		ViewGroup list = (ViewGroup)this.findViewById(android.R.id.list);
//		
//		String[] groupList = entryMap.keySet().toArray(new String[] { });
//		Arrays.sort(groupList);
//		
//		for(String groupName : groupList) {
//			View expand = inflater.inflate(R.layout.item_expand, list, false);
//			
//			TextView title = (TextView)expand.findViewById(android.R.id.title);
//			title.setText(groupName);
//			
//			//ListAdapter adapter = new EntryAdapter(LauncherActivity.this, entryMap.get(groupName));
//			//((GridView)expand.findViewById(android.R.id.content)).setAdapter(adapter);
//			
//			// insert all children elements as rows
//			TableLayout content = (TableLayout)expand.findViewById(android.R.id.content);
//			TableRow currentRow = new TableRow(this);
//			currentRow.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
//			
//			for(EntryInfo entry : entryMap.get(groupName)) {
//				TextView textView = (TextView)inflater.inflate(R.layout.item_entry, currentRow, false);
//
//				textView.setCompoundDrawablesWithIntrinsicBounds(null, entry.icon, null, null);
//				textView.setText(entry.title);
//
//				currentRow.addView(textView);
//
//				// create new rows when needed
//				if(currentRow.getChildCount() == 4) {
//					content.addView(currentRow);
//					currentRow = new TableRow(this);
//					
//				}
//				
//			}
//			
//			if(currentRow.getChildCount() > 0)
//				content.addView(currentRow);
//			
//			
//			list.addView(expand);
//			
//			
//		}
//		
		
		// now that tree is built, pass along to adapter
		this.setListAdapter(new GroupAdapter(entryMap));
		this.getExpandableListView().setItemsCanFocus(true);

		
	}
	
	// helps map from category to gridview of apps
	// need to map from categories into applicationinfo
	// somehow need to pass along app name/category pairings
	// json from online with   "Finance": [""]
	public class GroupAdapter extends BaseExpandableListAdapter {

		private Map<String,List<EntryInfo>> entryMap;
		private String[] groupNames;
		
		public static final int COLS = 4;
		
		public GroupAdapter(Map<String,List<EntryInfo>> entryMap) {
			this.entryMap = entryMap;
			this.groupNames = entryMap.keySet().toArray(new String[] {});
			Arrays.sort(this.groupNames);
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
			return null; //entryMap.get(groupNames[groupPosition]).get(childPosition);
		}

		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		public int getChildrenCount(int groupPosition) {
			// round our reported row count to match number of columns
			int actualCount = entryMap.get(groupNames[groupPosition]).size();
			return (actualCount + (COLS - 1)) / COLS;
		}

		public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
			
			if(convertView == null)
				convertView = inflater.inflate(R.layout.item_row, parent, false);

			final ViewGroup viewGroup = (ViewGroup)convertView;
			viewGroup.removeAllViews();
			
			List<EntryInfo> actualChildren = entryMap.get(groupNames[groupPosition]);
			int start = childPosition * COLS, end = (childPosition + 1) * COLS;
			
			for(int i = start; i < end; i++) {
				final EntryInfo info = actualChildren.get(childPosition);
				
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