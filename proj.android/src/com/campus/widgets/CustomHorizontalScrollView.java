package com.campus.widgets;

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
	private Context mContext;
	private int viewCounts = -1;
	private int currentIndex;

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
				ViewGroup view_wraper = (ViewGroup) getChildAt(0);
				int count = view_wraper.getChildCount();
				if (count != viewCounts)
					viewCounts = count;
				 if (event.getAction() == MotionEvent.ACTION_UP
						|| event.getAction() == MotionEvent.ACTION_CANCEL) {
					if (getScrollX() <= 0) {
						currentIndex = 0;
						return true;
					} else if (getScrollX() + v.getWidth() >= view_wraper
							.getWidth()) {
						currentIndex = viewCounts - 1;
						return true;
					} else {
						int position = getScrollX() + v.getMeasuredWidth() / 2;
						int min = 999;
						int index = 0;
						for (int i = 0; i < viewCounts; i++) {
							View view = view_wraper.getChildAt(i);
							int x = view.getLeft() + view.getWidth() / 2;
							System.out.println(x);
							if (Math.abs(x - position) < min) {
								min = Math.abs(x - position);
								index = i;
							}
						}
						View view = view_wraper.getChildAt(index);
						currentIndex = index;
						int scrollTo = view.getLeft() + view.getWidth() / 2
								- v.getMeasuredWidth() / 2;
						smoothScrollTo(scrollTo, 0);
						return true;
					}

				} else {
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
				ViewGroup view_wraper = (ViewGroup) getChildAt(0);
				int scrollTo;
				if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
						&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {

					if (currentIndex < (viewCounts - 2)) {
						currentIndex = currentIndex + 1;
						View view = view_wraper.getChildAt(currentIndex);
						scrollTo = view.getLeft() + view.getWidth() / 2
								- getWidth();
					} else {
						scrollTo = view_wraper.getWidth() - getWidth();
					}

					smoothScrollTo(scrollTo, 0);
					return true;
				}
				// left to right
				else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
						&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
					if (currentIndex > 2) {
						currentIndex = currentIndex - 1;
						View view = view_wraper.getChildAt(currentIndex);
						scrollTo = view.getLeft() + view.getWidth() / 2
								- getWidth();
					} else {
						scrollTo = 0;
					}
					smoothScrollTo(scrollTo, 0);
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
