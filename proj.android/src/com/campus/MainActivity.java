package com.campus;

import android.app.LocalActivityManager;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;

import com.campus.index.IndexActivity;
import com.campus.utils.CommonUtil;

public class MainActivity extends ActionBarActivity {

	private RadioCheckedListener radioListenner;
	private TabHost tabHost;
	private String Tag;
	private static final String[] m = { "上海", "北京", "张江", "马戏城", "黑人牙膏" };

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
		// actionbar.setDisplayHomeAsUpEnabled(true);
		actionbar.setHomeButtonEnabled(true);
		Spinner spinner = new Spinner(this);
		ArrayAdapter<String> spAdapter = (new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, m));
		spinner.setAdapter(spAdapter);
		spAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		ActionBar.LayoutParams lp = new ActionBar.LayoutParams(
				ActionBar.LayoutParams.WRAP_CONTENT,
				ActionBar.LayoutParams.WRAP_CONTENT);
		lp.gravity = Gravity.CENTER_HORIZONTAL;
		actionbar.setCustomView(spinner, lp);
		actionbar.setTitle("首页");
		actionbar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM,
				ActionBar.DISPLAY_SHOW_CUSTOM);
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_activity_action, menu);
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		MenuItem searchItem = menu.findItem(R.id.action_search);
		SearchView searchView = (SearchView)MenuItemCompat.getActionView(searchItem);
		searchView.setSearchableInfo(searchManager
				.getSearchableInfo(getComponentName()));
		return super.onCreateOptionsMenu(menu);
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
