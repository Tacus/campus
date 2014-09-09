package com.campus.publish;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;

import com.campus.R;

public class SelectFromGalleryActivity extends CenterAlignTitleActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_publish_select_from_gallery);
		initActionBar();
	}

	void initActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.set
	}
}
