package com.campus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {

	private Button btn_login = null;
	private EditText edit_userName = null;
	private EditText edit_userPass = null;
	private Button btn_regist = null;
	public String TAG = LoginActivity.this.getClass().getName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_login);
		edit_userName = (EditText) findViewById(R.id.edit_userName);
		edit_userPass = (EditText) findViewById(R.id.edit_password);
		btn_regist = (Button) findViewById(R.id.btn_regist);
		btn_regist.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(LoginActivity.this, RegistActivity.class);
				LoginActivity.this.startActivity(intent);
			}
		});
		btn_login = (Button) findViewById(R.id.btn_login);
		btn_login.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				HttpCallback callback = new HttpCallback() {

					@Override
					public void onSuccess(String msg) {
						// TODO Auto-generated method stub
						Log.e("TAG" + ":" + "onSuccess", msg);
					}

					@Override
					public void onFailure(int type, String msg) {
						// TODO Auto-generated method stub
						Log.e("TAG:" + "onFailure", msg + "type:" + type);
					}
				};
				String userName = edit_userName.getText().toString();
				String userPass = edit_userPass.getText().toString();
				char temp[] = new char[65];
				for (int i = 0; i < userPass.length(); i++) {
					temp[i] = (char) (userPass.charAt(i) ^ 'k');
				}
				userPass = temp.toString();
				if (TextUtils.isEmpty(userPass) || TextUtils.isEmpty(userName)) {
					Toast.makeText(
							LoginActivity.this,
							CommonUtil.getResString(LoginActivity.this,
									R.string.name_pass_not_empty),
							Toast.LENGTH_SHORT).show();
				} else {

					// JSONObject json = new JSONObject();
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					String curTime = System.currentTimeMillis() + "";
					params.add(new BasicNameValuePair("time", curTime));
					params.add(new BasicNameValuePair("action",
							Constant.LOGIN_ACTION));
					params.add(new BasicNameValuePair("name", userName));
					params.add(new BasicNameValuePair("pwd", userPass));
					params.add(new BasicNameValuePair("ostype", "android"));
					params.add(new BasicNameValuePair("osvers",
							android.os.Build.VERSION.RELEASE));
					params.add(new BasicNameValuePair("client",
							android.os.Build.MODEL));
					params.add(new BasicNameValuePair("appvers", CommonUtil
							.getVersionCode(LoginActivity.this) + ""));
					params.add(new BasicNameValuePair(
							"sign",
							MD5Util.generateMd5(
									(Constant.LOGIN_ACTION + userName + curTime + Constant.KEY)
											.getBytes()).toLowerCase()));
					HttpRequestUtil.httpClientPost(Constant.loginUrl, params,
							callback);
				}
			}
		});
	}
}