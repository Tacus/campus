package com.campus.widgets;

import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.campus.R;

public class CenterAlignTitleActivity extends ActionBarActivity {

	public void setTitle(String title) {
		ActionBar actionBar = getSupportActionBar();
		View view = LayoutInflater.from(this).inflate(
				R.layout.view_centeralign_title, null);
		TextView textView = (TextView) view.findViewById(R.id.text_title);
		textView.setText(title);
		actionBar.setCustomView(view);
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
	}
}
