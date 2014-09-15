package com.campus.publish;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.campus.CustomHorizontalScrollView;
import com.campus.R;
import com.campus.widgets.CenterAlignTitleActivity;

public class PublishTradeActivity extends CenterAlignTitleActivity implements
		OnClickListener, OnTouchListener {

	private ImageButton imageBtn;
	private PopupWindow popWindow;
	private LinearLayout linPhotoes;
	private LinearLayout lin_wraper;
	private String Tag = "PublishTradeActivity";
	private int REQUEST_CODE_SELECT_IMG = 1;
	private ArrayList<String> selectedIds;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_publish);
		initActionBar();
		initView();
	}

	private void initActionBar() {
		// ActionBar actionBar = getSupportActionBar();
		this.setTitleText("发布");
	}

	private void initView() {
		CustomHorizontalScrollView scroll = (CustomHorizontalScrollView) findViewById(R.id.scroll_photoes);
		linPhotoes = (LinearLayout) findViewById(R.id.lin_photoes);
		imageBtn = (ImageButton) findViewById(R.id.img_addImg);
		imageBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.img_addImg:
			View view = LayoutInflater.from(this).inflate(
					R.layout.popview_img_from_select, null);
			view.setOnTouchListener(this);
			lin_wraper = (LinearLayout) view.findViewById(R.id.lin_wraper);
			Button take_photo = (Button) view.findViewById(R.id.btn_take_photo);
			Button selectF = (Button) view
					.findViewById(R.id.btn_select_from_gallery);
			Button cancel = (Button) view.findViewById(R.id.btn_cancle);
			take_photo.setOnClickListener(this);
			selectF.setOnClickListener(this);
			cancel.setOnClickListener(this);
			popWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT, true);
			popWindow.setOutsideTouchable(true);
			popWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
			lin_wraper.setAnimation(AnimationUtils.loadAnimation(this,
					R.anim.popwin_enter));
			break;
		case R.id.btn_cancle:
			dismissPopView();
			break;
		case R.id.btn_take_photo:
			dismissPopView();
			break;
		case R.id.btn_select_from_gallery:
			Intent intent = new Intent(this, SelectFromGalleryActivity.class);
			if (selectedIds != null && selectedIds.size() > 0)
				intent.putExtra("selectedIds", selectedIds);
			this.startActivityForResult(intent, REQUEST_CODE_SELECT_IMG);
			dismissPopView();
			break;
		default:
			break;
		}
	}

	private void dismissPopView() {
		if (popWindow != null && popWindow.isShowing()) {
			lin_wraper.startAnimation(AnimationUtils.loadAnimation(this,
					R.anim.popwin_exsit));
			lin_wraper.postDelayed(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					popWindow.dismiss();
				}
			}, 300);

		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		if (event.getAction() == MotionEvent.ACTION_UP
				&& v.getId() != R.id.lin_wraper)
			dismissPopView();
		return false;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_CODE_SELECT_IMG && resultCode == RESULT_OK) {
			selectedIds = data.getStringArrayListExtra("selectedIds");
		}
	}
}
