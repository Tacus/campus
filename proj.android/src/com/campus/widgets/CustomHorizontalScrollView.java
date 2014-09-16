package com.campus.widgets;

import java.util.ArrayList;

import com.campus.utils.CommonUtil;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

public class CustomHorizontalScrollView extends HorizontalScrollView {

	private static final int SWIPE_MIN_DISTANCE = 5;
	private static final int SWIPE_THRESHOLD_VELOCITY = 50;

	private GestureDetector mGestureDetector;
	private int mActiveFeature = 0;
	private Context mContext;
	private int mCurrentIndex = 0;

	public CustomHorizontalScrollView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public CustomHorizontalScrollView(Context context, AttributeSet attributeSet) {
		super(context, attributeSet);
		mContext = context;
		init();
	}

	private void init() {
		this.setSmoothScrollingEnabled(true);
		mGestureDetector = new GestureDetector(mContext,
				new MyGestureDetector());
		setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// If the user swipes
				ViewGroup lin = (ViewGroup) getChildAt(0);
				int count = lin.getChildCount();
				if (mGestureDetector.onTouchEvent(event)) {
					System.out.println("1111");
					return true;
				} else if (event.getAction() == MotionEvent.ACTION_UP
						|| event.getAction() == MotionEvent.ACTION_CANCEL) {
					System.out.println("222");
					int scrollX = getScrollX();
					int featureWidth = v.getMeasuredWidth();
					System.out.println("scrollx:" + featureWidth);
					mActiveFeature = ((scrollX + (featureWidth / 2)) / featureWidth);
					int scrollTo = mActiveFeature * featureWidth;
					smoothScrollTo(scrollTo, 0);
					return true;
				} else {
					System.out.println("333");
					return false;
				}
			}
		});
	}

	@Override
	public boolean performClick() {
		// TODO Auto-generated method stub
		System.out.println(1);
		return super.performClick();
	}

	class MyGestureDetector extends SimpleOnGestureListener {
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			try {
				// right to left
				System.out.println("fling:" + (e1.getX() - e2.getX()));
				if (Math.abs(e1.getX() - e2.getX()) > SWIPE_MIN_DISTANCE
						&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
					int featureWidth = getMeasuredWidth();
					// mActiveFeature = (mActiveFeature < (mItems.size() - 1)) ?
					// mActiveFeature + 1
					// : mItems.size() - 1;
					smoothScrollTo(mActiveFeature * featureWidth, 0);

					return true;
				}
				// left to right
				else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
						&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
					int featureWidth = getMeasuredWidth();
					mActiveFeature = (mActiveFeature > 0) ? mActiveFeature - 1
							: 0;
					smoothScrollTo(mActiveFeature * featureWidth, 0);
					return true;
				}
			} catch (Exception e) {
				Log.e("Fling", "There was an error processing the Fling event:"
						+ e.getMessage());
			}
			return false;
		}
	}
}
