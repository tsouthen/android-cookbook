package org.jsharkey.grouphome;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.LinearLayout;

/**
 * Layout class that provides a single-row of a grid-style view into an
 * {@link ExpandedListAdapter}. This helps us show something like a
 * {@link GridView} inside of a vertical {@link ListView}.
 * 
 * @author jsharkey
 */
public class ColumnView extends LinearLayout {
	
	public static final String TAG = ColumnView.class.toString();
	
	public ColumnView(Context context) {
		super(context);
	}

	public ColumnView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		
		// inform user how we are being laid out
		Log.d(TAG, MeasureSpec.toString(widthMeasureSpec));
		Log.d(TAG, MeasureSpec.toString(heightMeasureSpec));
		
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		
//        mTotalLength = 0;
//        int maxHeight = 0;
//        int alternativeMaxHeight = 0;
//        int weightedMaxHeight = 0;
//        boolean allFillParent = true;
//        float totalWeight = 0;
//
//        final int count = getVirtualChildCount();
//        
//        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
//        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
//
//        boolean matchHeight = false;
//
//        if (mMaxAscent == null || mMaxDescent == null) {
//            mMaxAscent = new int[VERTICAL_GRAVITY_COUNT];
//            mMaxDescent = new int[VERTICAL_GRAVITY_COUNT];
//        }
//
//        final int[] maxAscent = mMaxAscent;
//        final int[] maxDescent = mMaxDescent;
//
//        maxAscent[0] = maxAscent[1] = maxAscent[2] = maxAscent[3] = -1;
//        maxDescent[0] = maxDescent[1] = maxDescent[2] = maxDescent[3] = -1;
//
//        final boolean baselineAligned = mBaselineAligned;
//
//        // See how wide everyone is. Also remember max height.
//        for (int i = 0; i < count; ++i) {
//            final View child = getVirtualChildAt(i);
//
//            if (child == null) {
//                mTotalLength += measureNullChild(i);
//                continue;
//            }
//           
//            if (child.getVisibility() == GONE) {
//                i += getChildrenSkipCount(child, i);
//                continue;
//            }
//
//            final LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) child.getLayoutParams();
//
//            totalWeight += lp.weight;
//            
//            if (widthMode == MeasureSpec.EXACTLY && lp.width == 0 && lp.weight > 0) {
//                // Optimization: don't bother measuring children who are going to use
//                // leftover space. These views will get measured again down below if
//                // there is any leftover space.
//                mTotalLength += lp.leftMargin + lp.rightMargin;
//
//                // Baseline alignment requires to measure widgets to obtain the
//                // baseline offset (in particular for TextViews).
//                // The following defeats the optimization mentioned above.
//                // Allow the child to use as much space as it wants because we
//                // can shrink things later (and re-measure).
//                if (baselineAligned) {
//                    final int freeSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
//                    child.measure(freeSpec, freeSpec);
//                }
//            } else {
//                int oldWidth = Integer.MIN_VALUE;
//
//                if (lp.width == 0 && lp.weight > 0) {
//                    // widthMode is either UNSPECIFIED OR AT_MOST, and this child
//                    // wanted to stretch to fill available space. Translate that to
//                    // WRAP_CONTENT so that it does not end up with a width of 0
//                    oldWidth = lp.width;
//                    lp.width = LayoutParams.WRAP_CONTENT;
//                }
//
//                // Determine how big this child would like to be. If this or
//                // previous children have given a weight, then we allow it to
//                // use all available space (and we will shrink things later
//                // if needed).
//                measureChildBeforeLayout(child, i, widthMeasureSpec,
//                        totalWeight == 0 ? mTotalLength : 0,
//                        heightMeasureSpec, 0);
//
//                if (oldWidth != Integer.MIN_VALUE) {
//                    lp.width = oldWidth;
//                }
//               
//                mTotalLength += child.getMeasuredWidth() + lp.leftMargin +
//                        lp.rightMargin + getNextLocationOffset(child);
//            }
//
//            boolean matchHeightLocally = false;
//            if (heightMode != MeasureSpec.EXACTLY && lp.height == LayoutParams.FILL_PARENT) {
//                // The height of the linear layout will scale, and at least one
//                // child said it wanted to match our height. Set a flag indicating that
//                // we need to remeasure at least that view when we know our height.
//                matchHeight = true;
//                matchHeightLocally = true;
//            }
//
//            final int margin = lp.topMargin + lp.bottomMargin;
//            final int childHeight = child.getMeasuredHeight() + margin;
//
//            if (baselineAligned) {
//                final int childBaseline = child.getBaseline();
//                if (childBaseline != -1) {
//                    // Translates the child's vertical gravity into an index
//                    // in the range 0..VERTICAL_GRAVITY_COUNT
//                    final int gravity = (lp.gravity < 0 ? mGravity : lp.gravity)
//                            & Gravity.VERTICAL_GRAVITY_MASK;
//                    final int index = ((gravity >> Gravity.AXIS_Y_SHIFT)
//                            & ~Gravity.AXIS_SPECIFIED) >> 1;
//
//                    maxAscent[index] = Math.max(maxAscent[index], childBaseline);
//                    maxDescent[index] = Math.max(maxDescent[index], childHeight - childBaseline);
//                }
//            }
//
//            maxHeight = Math.max(maxHeight, childHeight);
//
//            allFillParent = allFillParent && lp.height == LayoutParams.FILL_PARENT;
//            if (lp.weight > 0) {
//                /*
//                 * Heights of weighted Views are bogus if we end up
//                 * remeasuring, so keep them separate.
//                 */
//                weightedMaxHeight = Math.max(weightedMaxHeight,
//                        matchHeightLocally ? margin : childHeight);
//            } else {
//                alternativeMaxHeight = Math.max(alternativeMaxHeight,
//                        matchHeightLocally ? margin : childHeight);
//            }
//
//            i += getChildrenSkipCount(child, i);
//        }
//
//        // Check mMaxAscent[INDEX_TOP] first because it maps to Gravity.TOP,
//        // the most common case
//        if (maxAscent[INDEX_TOP] != -1 ||
//                maxAscent[INDEX_CENTER_VERTICAL] != -1 ||
//                maxAscent[INDEX_BOTTOM] != -1 ||
//                maxAscent[INDEX_FILL] != -1) {
//            final int ascent = Math.max(maxAscent[INDEX_FILL],
//                    Math.max(maxAscent[INDEX_CENTER_VERTICAL],
//                    Math.max(maxAscent[INDEX_TOP], maxAscent[INDEX_BOTTOM])));
//            final int descent = Math.max(maxDescent[INDEX_FILL],
//                    Math.max(maxDescent[INDEX_CENTER_VERTICAL],
//                    Math.max(maxDescent[INDEX_TOP], maxDescent[INDEX_BOTTOM])));
//            maxHeight = Math.max(maxHeight, ascent + descent);
//        }
//
//        // Add in our padding
//        mTotalLength += mPaddingLeft + mPaddingRight;
//        
//        int widthSize = mTotalLength;
//        
//        // Check against our minimum width
//        widthSize = Math.max(widthSize, getSuggestedMinimumWidth());
//        
//        // Reconcile our calculated size with the widthMeasureSpec
//        widthSize = resolveSize(widthSize, widthMeasureSpec);
//        
//        // Either expand children with weight to take up available space or
//        // shrink them if they extend beyond our current bounds
//        int delta = widthSize - mTotalLength;
//        if (delta != 0 && totalWeight > 0.0f) {
//            float weightSum = mWeightSum > 0.0f ? mWeightSum : totalWeight;
//
//            maxAscent[0] = maxAscent[1] = maxAscent[2] = maxAscent[3] = -1;
//            maxDescent[0] = maxDescent[1] = maxDescent[2] = maxDescent[3] = -1;
//            maxHeight = -1;
//
//            mTotalLength = 0;
//
//            for (int i = 0; i < count; ++i) {
//                final View child = getVirtualChildAt(i);
//
//                if (child == null || child.getVisibility() == View.GONE) {
//                    continue;
//                }
//                
//                final LinearLayout.LayoutParams lp =
//                        (LinearLayout.LayoutParams) child.getLayoutParams();
//
//                float childExtra = lp.weight;
//                if (childExtra > 0) {
//                    // Child said it could absorb extra space -- give him his share
//                    int share = (int) (childExtra * delta / weightSum);
//                    weightSum -= childExtra;
//                    delta -= share;
//
//                    final int childHeightMeasureSpec = getChildMeasureSpec(
//                            heightMeasureSpec,
//                            mPaddingTop + mPaddingBottom + lp.topMargin + lp.bottomMargin,
//                            lp.height);
//
//                    // TODO: Use a field like lp.isMeasured to figure out if this
//                    // child has been previously measured
//                    if ((lp.width != 0) || (widthMode != MeasureSpec.EXACTLY)) {
//                        // child was measured once already above ... base new measurement
//                        // on stored values
//                        int childWidth = child.getMeasuredWidth() + share;
//                        if (childWidth < 0) {
//                            childWidth = 0;
//                        }
//
//                        child.measure(
//                            MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.EXACTLY),
//                            childHeightMeasureSpec);
//                    } else {
//                        // child was skipped in the loop above. Measure for this first time here
//                        child.measure(MeasureSpec.makeMeasureSpec(
//                                share > 0 ? share : 0, MeasureSpec.EXACTLY),
//                                childHeightMeasureSpec);
//                    }
//                }
//
//                mTotalLength += child.getMeasuredWidth() + lp.leftMargin +
//                        lp.rightMargin + getNextLocationOffset(child);
//
//                boolean matchHeightLocally = heightMode != MeasureSpec.EXACTLY &&
//                        lp.height == LayoutParams.FILL_PARENT;
//
//                final int margin = lp.topMargin + lp .bottomMargin;
//                int childHeight = child.getMeasuredHeight() + margin;
//                maxHeight = Math.max(maxHeight, childHeight);
//                alternativeMaxHeight = Math.max(alternativeMaxHeight,
//                        matchHeightLocally ? margin : childHeight);
//
//                allFillParent = allFillParent && lp.height == LayoutParams.FILL_PARENT;
//                alternativeMaxHeight = Math.max(alternativeMaxHeight,
//                        matchHeightLocally ? margin : childHeight);
//
//                if (baselineAligned) {
//                    final int childBaseline = child.getBaseline();
//                    if (childBaseline != -1) {
//                        // Translates the child's vertical gravity into an index in the range 0..2
//                        final int gravity = (lp.gravity < 0 ? mGravity : lp.gravity)
//                                & Gravity.VERTICAL_GRAVITY_MASK;
//                        final int index = ((gravity >> Gravity.AXIS_Y_SHIFT)
//                                & ~Gravity.AXIS_SPECIFIED) >> 1;
//
//                        maxAscent[index] = Math.max(maxAscent[index], childBaseline);
//                        maxDescent[index] = Math.max(maxDescent[index],
//                                childHeight - childBaseline);
//                    }
//                }
//            }
//
//            // Add in our padding
//            mTotalLength += mPaddingLeft + mPaddingRight;
//
//            // Check mMaxAscent[INDEX_TOP] first because it maps to Gravity.TOP,
//            // the most common case
//            if (maxAscent[INDEX_TOP] != -1 ||
//                    maxAscent[INDEX_CENTER_VERTICAL] != -1 ||
//                    maxAscent[INDEX_BOTTOM] != -1 ||
//                    maxAscent[INDEX_FILL] != -1) {
//                final int ascent = Math.max(maxAscent[INDEX_FILL],
//                        Math.max(maxAscent[INDEX_CENTER_VERTICAL],
//                        Math.max(maxAscent[INDEX_TOP], maxAscent[INDEX_BOTTOM])));
//                final int descent = Math.max(maxDescent[INDEX_FILL],
//                        Math.max(maxDescent[INDEX_CENTER_VERTICAL],
//                        Math.max(maxDescent[INDEX_TOP], maxDescent[INDEX_BOTTOM])));
//                maxHeight = Math.max(maxHeight, ascent + descent);
//            }
//        } else {
//            alternativeMaxHeight = Math.max(alternativeMaxHeight,
//                                            weightedMaxHeight);
//        }
//
//        if (!allFillParent && heightMode != MeasureSpec.EXACTLY) {
//            maxHeight = alternativeMaxHeight;
//        }
//        
//        maxHeight += mPaddingTop + mPaddingBottom;
//
//        // Check against our minimum height
//        maxHeight = Math.max(maxHeight, getSuggestedMinimumHeight());
//        
//        setMeasuredDimension(widthSize, resolveSize(maxHeight, heightMeasureSpec));
//
//        if (matchHeight) {
//            forceUniformHeight(count, widthMeasureSpec);
//        }
	}

	
	@Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
		
		super.onLayout(changed, l, t, r, b);
		
//        final int paddingTop = mPaddingTop;
//
//        int childTop = paddingTop;
//        int childLeft = mPaddingLeft;
//        
//        // Where bottom of child should go
//        final int height = mBottom - mTop;
//        int childBottom = height - mPaddingBottom; 
//        
//        // Space available for child
//        int childSpace = height - paddingTop - mPaddingBottom;
//
//        final int count = getVirtualChildCount();
//
//        final int majorGravity = mGravity & Gravity.HORIZONTAL_GRAVITY_MASK;
//        final int minorGravity = mGravity & Gravity.VERTICAL_GRAVITY_MASK;
//
//        final boolean baselineAligned = mBaselineAligned;
//
//        final int[] maxAscent = mMaxAscent;
//        final int[] maxDescent = mMaxDescent;
//
//        if (majorGravity != Gravity.LEFT) {
//            switch (majorGravity) {
//                case Gravity.RIGHT:
//                    childLeft = mRight - mLeft - mPaddingRight - mTotalLength;
//                    break;
//
//                case Gravity.CENTER_HORIZONTAL:
//                    childLeft += ((mRight - mLeft - mPaddingLeft - mPaddingRight) -
//                            mTotalLength) / 2;
//                    break;
//            }
//       }
//
//        for (int i = 0; i < count; i++) {
//            final View child = getVirtualChildAt(i);
//
//            if (child == null) {
//                childLeft += measureNullChild(i);
//            } else if (child.getVisibility() != GONE) {
//                final int childWidth = child.getMeasuredWidth();
//                final int childHeight = child.getMeasuredHeight();
//                int childBaseline = -1;
//
//                final LinearLayout.LayoutParams lp =
//                        (LinearLayout.LayoutParams) child.getLayoutParams();
//
//                if (baselineAligned && lp.height != LayoutParams.FILL_PARENT) {
//                    childBaseline = child.getBaseline();
//                }
//                
//                int gravity = lp.gravity;
//                if (gravity < 0) {
//                    gravity = minorGravity;
//                }
//                
//                switch (gravity & Gravity.VERTICAL_GRAVITY_MASK) {
//                    case Gravity.TOP:
//                        childTop = paddingTop + lp.topMargin;
//                        if (childBaseline != -1) {
//                            childTop += maxAscent[INDEX_TOP] - childBaseline;
//                        }
//                        break;
//
//                    case Gravity.CENTER_VERTICAL:
//                        // Removed support for baselign alignment when layout_gravity or
//                        // gravity == center_vertical. See bug #1038483.
//                        // Keep the code around if we need to re-enable this feature
//                        // if (childBaseline != -1) {
//                        //     // Align baselines vertically only if the child is smaller than us
//                        //     if (childSpace - childHeight > 0) {
//                        //         childTop = paddingTop + (childSpace / 2) - childBaseline;
//                        //     } else {
//                        //         childTop = paddingTop + (childSpace - childHeight) / 2;
//                        //     }
//                        // } else {
//                        childTop = paddingTop + ((childSpace - childHeight) / 2)
//                                + lp.topMargin - lp.bottomMargin;
//                        break;
//
//                    case Gravity.BOTTOM:
//                        childTop = childBottom - childHeight - lp.bottomMargin;
//                        if (childBaseline != -1) {
//                            int descent = child.getMeasuredHeight() - childBaseline;
//                            childTop -= (maxDescent[INDEX_BOTTOM] - descent);
//                        }
//                        break;
//                }
//
//                childLeft += lp.leftMargin;
//                setChildFrame(child, childLeft + getLocationOffset(child), childTop,
//                        childWidth, childHeight);
//                childLeft += childWidth + lp.rightMargin +
//                        getNextLocationOffset(child);
//
//                i += getChildrenSkipCount(child, i);
//            }
//        }
	}

}
