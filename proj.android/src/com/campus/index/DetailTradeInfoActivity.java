package com.campus.index;

import com.campus.MainActivity;
import com.campus.R;
import com.campus.R.layout;
import com.campus.domain.TradeInfo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

public class DetailTradeInfoActivity extends ActionBarActivity {

	private TradeInfo tradeInfo;
	private static final String Tag = DetailTradeInfoActivity.class
			.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);
		tradeInfo = (TradeInfo) getIntent().getExtras().getSerializable(
				"detailTradeInfo");
		initActionBar();
		initViews();
		System.out.println("current task Id" + getTaskId());
	}

	private void initActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
	}

	private void initViews() {

	}

	@Override
	public Intent getSupportParentActivityIntent() {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		intent.setClass(this, MainActivity.class);
		this.startActivity(intent);
		this.finish();
		return super.getSupportParentActivityIntent();
	}
}
