package com.campus;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.HorizontalScrollView;

public class CustomHorizontalScrollView extends HorizontalScrollView {

	public CustomHorizontalScrollView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init();
	}

	public CustomHorizontalScrollView(Context context, AttributeSet attributeSet) {
		super(context, attributeSet);
	}

	private void init() {
		setHorizontalScrollBarEnabled(false);
	}

}
