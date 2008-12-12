package org.jsharkey.grouphome;

import android.widget.GridView;
import android.widget.AdapterView;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class FullGridView extends GridView {
	
	public static final String TAG = FullGridView.class.toString();

    public FullGridView(Context context) {
        super(context);
    }

    public FullGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FullGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    	// force us to calculate full height
		super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 3, MeasureSpec.AT_MOST));
    }

}
