package com.campus;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.campus.utils.CommonUtil;
import com.campus.utils.Constant;
import com.campus.utils.HttpCallback;
import com.campus.utils.HttpRequestUtil;
import com.campus.utils.MD5Util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity implements OnClickListener {

	private Button btn_login = null;
	private EditText edit_userName = null;
	private EditText edit_userPass = null;
	private Button btn_regist = null;
	public String TAG = LoginActivity.this.getClass().getName();
	private WeakReference<ProgressDialog> weakPDialog = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		CommonUtil.init(this);
		Log.e(TAG, "oncreate");
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_login);
		edit_userName = (EditText) findViewById(R.id.edit_userName);
		edit_userPass = (EditText) findViewById(R.id.edit_password);
		btn_regist = (Button) findViewById(R.id.btn_regist);
		btn_regist.setOnClickListener(this);
		btn_login = (Button) findViewById(R.id.btn_login);
		btn_login.setOnClickListener(this);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.e(TAG, "onResume");
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.e(TAG, "onDestory");
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_regist:
			Intent intent = new Intent();
			intent.setClass(LoginActivity.this, RegistActivity.class);
			LoginActivity.this.startActivity(intent);
			break;
		case R.id.btn_login:
			loginBtnClick();
			break;
		default:
			break;
		}
	}

	private void loginBtnClick() {

		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(btn_login.getWindowToken(), 0);
		String userNameStr = edit_userName.getText().toString().trim();
		String passwordStr = edit_userPass.getText().toString().trim();
		// 正则表达式
		Pattern pattern = Pattern.compile("[a-zA-Z0-9_]{6,12}"); // 正则表达式匹配用户名6-12为的英文或数字的组合
		Pattern patternNum = Pattern.compile("[0][0-9]{5,11}"); // 正则表达式匹配用户名是纯数字的不能以0大头
		// 正则表达式匹配用户名为邮箱的情况
		Pattern patternMail = Pattern
				.compile("^([a-z0-9A-Z]+[-|.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?.)+[a-zA-Z]{2,}$");
		Pattern passPattern = Pattern.compile("[a-zA-Z0-9_]{6,16}");
		Matcher nameMatcher = pattern.matcher(userNameStr);
		Matcher nameMatcherNum = patternNum.matcher(userNameStr);
		Matcher passMatcher = passPattern.matcher(passwordStr);
		Matcher nameMatcherMail = patternMail.matcher(userNameStr);
		if (userNameStr.equals("")) {
			Toast.makeText(this, "用户名不能为空", Toast.LENGTH_SHORT).show();
			return;
		} else if (passwordStr.equals("")) {
			Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
			return;
		} else if (nameMatcherNum.matches()) {
			Toast.makeText(this, "用户名输入错误", Toast.LENGTH_SHORT).show();
			edit_userName.setText("");
			return;
		} else if ((nameMatcher.matches() || nameMatcherMail.matches()) == false) {
			Toast.makeText(this, "用户名为邮箱或6-12个英文或数字的组合", Toast.LENGTH_SHORT)
					.show();
			edit_userName.setText("");
			return;
		} else if (passMatcher.matches() == false) {
			Toast.makeText(this, "密码为6-16个英文或数字的组合", Toast.LENGTH_SHORT).show();
			edit_userPass.setText("");
			return;
		}

		HttpCallback callback = new HttpCallback() {

			@Override
			public void onSuccess(String msg) {
				// TODO Auto-generated method stub
				Log.e("TAG" + ":" + "onSuccess", msg);
				if (weakPDialog.get() != null) {
					weakPDialog.get().dismiss();
				}
				Log.e(TAG, Thread.currentThread().getName());
				JSONObject json;
				try {
					json = new JSONObject(msg);
					int state = json.getInt("state");
					String message = json.getString("message");
					if (state == 0) {
						Intent intent = new Intent();
						intent.setClass(LoginActivity.this, MainActivity.class);
						LoginActivity.this.startActivity(intent);
						CommonUtil.showToast(message);
					} else {
						CommonUtil.showToast(message);
					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					CommonUtil.showToast("登录失败");
				}
			}

			@Override
			public void onFailure(int type, String msg) {
				// TODO Auto-generated method stub
				Log.e("TAG:" + "onFailure", msg + "type:" + type);
				if (weakPDialog.get() != null) {
					weakPDialog.get().dismiss();
				}
				Log.e(TAG, Thread.currentThread().getName());
				CommonUtil.showToast("登录失败");
			}
		};
		char temp[] = new char[65];
		for (int i = 0; i < passwordStr.length(); i++) {
			temp[i] = (char) (passwordStr.charAt(i) ^ 'k');
		}
		ProgressDialog pDialog = new ProgressDialog(this);
		pDialog.setCancelable(false);
		pDialog.setMessage("登录中...");
		Log.e(TAG, Thread.currentThread().getName());
		weakPDialog = new WeakReference<ProgressDialog>(pDialog);
		if (weakPDialog.get() != null) {
			weakPDialog.get().show();
		}
		HttpRequestUtil.loginOrRegistRequest(Constant.ACTION_LOGIN,
				userNameStr, passwordStr, callback);
	}
}