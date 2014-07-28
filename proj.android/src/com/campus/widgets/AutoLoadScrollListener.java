package com.campus.widgets;

import android.util.Log;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;

public abstract class AutoLoadScrollListener implements OnScrollListener {

	private String Tag = AutoLoadScrollListener.class.getSimpleName();
	private int lastItemIndex = -1;

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		Log.e(Tag, "onScrollStateChange:" + scrollState);
		Log.e(Tag, "view adapter count:" + view.getAdapter().getCount());
		if (lastItemIndex == view.getAdapter().getCount() + 1)
			loadData();
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		Log.e(Tag, "onScrll--firstviewItem:" + firstVisibleItem
				+ "visibleItemCount:" + visibleItemCount + "totalItemCount:"
				+ totalItemCount + "-item totalCount:"
				+ view.getAdapter().getCount());
		lastItemIndex = firstVisibleItem + visibleItemCount;
	}

	public abstract void loadData();

}
