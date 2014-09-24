package com.campus.widgets;

import android.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;

public class CustomHorizontalScrollView extends HorizontalScrollView implements
		GestureDetector.OnGestureListener {

	private static final int SWIPE_MIN_DISTANCE = 5;
	private static final int SWIPE_THRESHOLD_VELOCITY = 50;

	private GestureDetector mGestureDetector;
	private Context mContext;
	private int viewCounts = -1;
	private int currentIndex;
//	private FlingRunnable mFlingRunnable = new FlingRunnable();

//	private class FlingRunnable implements Runnable {
//		/**
//		 * Tracks the decay of a fling scroll
//		 */
//		private Scroller mScroller;
//
//		/**
//		 * X value reported by mScroller on the previous fling
//		 */
//		private int mLastFlingX;
//
//		public FlingRunnable() {
//			mScroller = new Scroller(getContext());
//		}
//
//		private void startCommon() {
//			// Remove any pending flings
//			removeCallbacks(this);
//		}
//
//		public void startUsingVelocity(int initialVelocity) {
//			if (initialVelocity == 0)
//				return;
//
//			startCommon();
//
//			int initialX = initialVelocity < 0 ? Integer.MAX_VALUE : 0;
//			mLastFlingX = initialX;
//			mScroller.fling(initialX, 0, initialVelocity, 0, 0,
//					Integer.MAX_VALUE, 0, Integer.MAX_VALUE);
//			post(this);
//		}
//
//		public void startUsingDistance(int distance) {
//			if (distance == 0)
//				return;
//
//			startCommon();
//
//			mLastFlingX = 0;
//			mScroller.startScroll(0, 0, -distance, 0, mAnimationDuration);
//			post(this);
//		}
//
//		public void stop(boolean scrollIntoSlots) {
//			removeCallbacks(this);
//			endFling(scrollIntoSlots);
//		}
//
//		private void endFling(boolean scrollIntoSlots) {
//			/*
//			 * Force the scroller's status to finished (without setting its
//			 * position to the end)
//			 */
//			mScroller.forceFinished(true);
//
//			if (scrollIntoSlots)
//				scrollIntoSlots();
//		}
//
//		@Override
//		public void run() {
//
//			if (mItemCount == 0) {
//				endFling(true);
//				return;
//			}
//
//			mShouldStopFling = false;
//
//			final Scroller scroller = mScroller;
//			boolean more = scroller.computeScrollOffset();
//			final int x = scroller.getCurrX();
//
//			// Flip sign to convert finger direction to list items direction
//			// (e.g. finger moving down means list is moving towards the top)
//			int delta = mLastFlingX - x;
//
//			// Pretend that each frame of a fling scroll is a touch scroll
//			if (delta > 0) {
//				// Moving towards the left. Use leftmost view as
//				// mDownTouchPosition
//				mDownTouchPosition = mIsRtl ? (mFirstPosition + getChildCount() - 1)
//						: mFirstPosition;
//
//				// Don't fling more than 1 screen
//				delta = Math.min(getWidth() - mPaddingLeft - mPaddingRight - 1,
//						delta);
//			} else {
//				// Moving towards the right. Use rightmost view as
//				// mDownTouchPosition
//				int offsetToLast = getChildCount() - 1;
//				mDownTouchPosition = mIsRtl ? mFirstPosition : (mFirstPosition
//						+ getChildCount() - 1);
//
//				// Don't fling more than 1 screen
//				delta = Math
//						.max(-(getWidth() - mPaddingRight - mPaddingLeft - 1),
//								delta);
//			}
//
//			trackMotionScroll(delta);
//
//			if (more && !mShouldStopFling) {
//				mLastFlingX = x;
//				post(this);
//			} else {
//				endFling(true);
//			}
//		}
//
//	}

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
		mGestureDetector = new GestureDetector(mContext, this);
	}

	@Override
	public boolean performClick() {
		// TODO Auto-generated method stub
		System.out.println(1);
		return super.performClick();
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		boolean retValue = mGestureDetector.onTouchEvent(ev);
		int action = ev.getAction();
		if (action == MotionEvent.ACTION_UP) {
			// Helper method for lifted finger
			onUp();
		} else if (action == MotionEvent.ACTION_CANCEL) {
			onCancel();
		}

		return retValue;
	}

	private boolean onUp() {
		ViewGroup view_wraper = (ViewGroup) getChildAt(0);
		int count = view_wraper.getChildCount();
		if (count != viewCounts)
			viewCounts = count;
		if (getScrollX() <= 0) {
			currentIndex = 0;
			return true;
		} else if (getScrollX() + this.getWidth() >= view_wraper.getWidth()) {
			currentIndex = viewCounts - 1;
			return true;
		} else {
			int position = getScrollX() + this.getMeasuredWidth() / 2;
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
					- this.getMeasuredWidth() / 2;
			smoothScrollTo(scrollTo, 0);
			return true;
		}
	}

	private boolean onCancel() {
		return onUp();
	}

	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		return false;
	}
}
