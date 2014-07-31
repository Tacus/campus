package com.campus.widgets;

import android.util.Log;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;

public abstract class AutoLoadScrollListener implements OnScrollListener {

	private String Tag = AutoLoadScrollListener.class.getSimpleName();
	private int lastItemIndex = -1;
	private boolean isLoading;

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		if (lastItemIndex == view.getAdapter().getCount()
				&& scrollState == OnScrollListener.SCROLL_STATE_IDLE
				&& !isLoading)
			loadData();
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		lastItemIndex = firstVisibleItem + visibleItemCount;
	}

	public boolean isLoading() {
		return isLoading;
	}

	public void setLoading(boolean isLoading) {
		this.isLoading = isLoading;
	}

	public abstract void loadData();

}
