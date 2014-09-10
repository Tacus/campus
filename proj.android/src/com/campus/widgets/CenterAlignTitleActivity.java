package com.campus.widgets;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.campus.R;

public class CenterAlignTitleActivity extends ActionBarActivity {

	private TextView textView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		ActionBar actionBar = getSupportActionBar();
		View view = LayoutInflater.from(this).inflate(
				R.layout.view_centeralign_title, null);
		textView = (TextView) view.findViewById(R.id.text_title);
		ActionBar.LayoutParams lp = new ActionBar.LayoutParams(Gravity.CENTER);
		actionBar.setCustomView(view, lp);
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
	}

	public void setTitleText(String title) {
		textView.setText(title);
	}

	public void setTitleTextColor(int color) {
		textView.setTextColor(color);
	}

	public void setTitleBg(int color) {

	}
}
