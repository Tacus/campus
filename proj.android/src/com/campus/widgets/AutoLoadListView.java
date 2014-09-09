package com.campus.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.AbsListView;
import android.widget.ListView;

public class AutoLoadListView extends ListView {

	public AutoLoadListView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public AutoLoadListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public AutoLoadListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public void setAutoLoadListenner(AutoLoadScrollListener listenner) {
		this.setOnScrollListener(listenner);
	}

}
