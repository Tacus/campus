package com.campus;

import com.campus.utils.CommonUtil;

import android.app.LocalActivityManager;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioButton;
import android.widget.TabHost;
import android.widget.TabHost.TabContentFactory;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {

	private RadioCheckedListener radioListenner;
	private TabHost tabHost;
	private String Tag;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		CommonUtil.init(this.getApplicationContext());
		radioListenner = new RadioCheckedListener();
		initActionBar();
		initTabsAndContent(savedInstanceState);
		Tag = this.getClass().getSimpleName();
	}

	private void initActionBar() {
		ActionBar actionbar = getSupportActionBar();
		actionbar.setDisplayShowHomeEnabled(true);
		actionbar.setDisplayUseLogoEnabled(true);
		actionbar.setHomeButtonEnabled(true);
		actionbar.setTitle("首页");
	}

	private void initTabsAndContent(Bundle savedInstanceState) {
		tabHost = (TabHost) findViewById(android.R.id.tabhost);
		tabHost.setup();
		LocalActivityManager mLocalActivityManager = new LocalActivityManager(
				this, false);
		mLocalActivityManager.dispatchCreate(savedInstanceState);
		tabHost.setup(mLocalActivityManager);
		Intent index = new Intent(this, IndexActivity.class);
		tabHost.addTab(tabHost.newTabSpec("首页").setIndicator("index")
				.setContent(index));
		Intent category = new Intent(this, CategoryActivity.class);
		tabHost.addTab(tabHost.newTabSpec("分类").setIndicator("category")
				.setContent(category));
		Intent mine = new Intent(this, MineActivity.class);
		tabHost.addTab(tabHost.newTabSpec("个人中心").setIndicator("profile")
				.setContent(mine));
		RadioButton radioIndex = (RadioButton) findViewById(R.id.index);
		radioIndex.setOnCheckedChangeListener(radioListenner);
		RadioButton radioCat = (RadioButton) findViewById(R.id.category);
		radioCat.setOnCheckedChangeListener(radioListenner);
		RadioButton radioMine = (RadioButton) findViewById(R.id.mine);
		radioMine.setOnCheckedChangeListener(radioListenner);
		radioIndex.setChecked(true);
	}

	class RadioCheckedListener implements OnCheckedChangeListener {

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			if (!isChecked)
				return;
			// TODO Auto-generated method stub
			int curIndex = tabHost.getCurrentTab();
			int selIndex = curIndex;
			switch (buttonView.getId()) {
			case R.id.index:
				selIndex = 0;
				break;
			case R.id.category:
				selIndex = 1;
				break;
			case R.id.mine:
				selIndex = 2;
				break;
			default:
				break;
			}
			if (selIndex != curIndex)
				tabHost.setCurrentTab(selIndex);
			Log.e(Tag, "select idnex " + selIndex);

		}
	}

}
