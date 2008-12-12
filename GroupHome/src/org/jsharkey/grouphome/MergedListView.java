package org.jsharkey.grouphome;

import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;

public class MergedListView extends ExpandableListView {

	public static final String TAG = MergedListView.class.toString();

	public MergedListView(Context context) {
		super(context);
	}

	public MergedListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MergedListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	
	// try finding place where children views are laid out
	
	
	
    

//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//    	// force us to calculate full height
//		super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 3, MeasureSpec.AT_MOST));
//    }

}
