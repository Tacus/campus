package com.campus.publish;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;

import com.campus.R;
import com.campus.widgets.CenterAlignTitleActivity;

public class SelectFromGalleryActivity extends CenterAlignTitleActivity
		implements OnClickListener, OnItemClickListener,
		LoaderCallbacks<Cursor> {
	private GridView gridView;
	private Button btnComplete;
	private GridViewAdapter adapter;

	private static final String[] STORE_IMAGES = {
			MediaStore.Images.Media.DISPLAY_NAME, MediaStore.Images.Media._ID };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_publish_select_from_gallery);
		initView();
		initActionBar();
		getSupportLoaderManager().initLoader(0, null, this);
	}

	void initView() {
		gridView = (GridView) findViewById(R.id.gridview);
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

	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		// TODO Auto-generated method stub
		CursorLoader cursorLoader = new CursorLoader(this,
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI, STORE_IMAGES,
				null, null, null);
		return cursorLoader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor arg1) {
		// TODO Auto-generated method stub
		if (adapter == null) {
			adapter = new GridViewAdapter(this, arg1,
					CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
			gridView.setAdapter(adapter);
			gridView.setOnItemClickListener(this);
			return;
		}
		adapter.changeCursor(arg1);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		// TODO Auto-generated method stub
		adapter.changeCursor(null);
	}
}
