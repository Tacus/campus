package com.campus.publish;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;

import com.campus.R;
import com.campus.widgets.CenterAlignTitleActivity;

public class SelectFromGalleryActivity extends CenterAlignTitleActivity
		implements OnClickListener, OnItemClickListener {
	private GridView gridView;
	private Button btnComplete;
	private GridViewAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_publish_select_from_gallery);
		initView();
		initActionBar();
	}

	void initView() {
		gridView = (GridView) findViewById(R.id.gridview);
		adapter = new GridViewAdapter(this);
		// adapter
		gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(this);
		btnComplete = (Button) findViewById(R.id.btn_complete);
		btnComplete.setOnClickListener(this);
	}

	void initActionBar() {
		setTitleText("我的相册");
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeAsUpIndicator(R.drawable.btn_actionbar_back_selector);
		actionBar.setDisplayUseLogoEnabled(false);
		actionBar.setHomeButtonEnabled(true);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_complete:
			this.finish();
			break;

		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		
	}
}
