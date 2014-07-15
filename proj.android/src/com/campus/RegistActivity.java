package com.campus;

import java.lang.ref.WeakReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.campus.utils.CommonUtil;
import com.campus.utils.Constant;
import com.campus.utils.HttpCallback;
import com.campus.utils.HttpRequestUtil;

public class RegistActivity extends Activity implements OnClickListener {

	private Button btn_return_login = null;
	private Button btn_regist = null;
	private EditText userName = null;
	private EditText userPass = null;
	private EditText userPassConfirm = null;
	private String usernameString;
	private String passString;
	private String repassString;
	public static String TAG = RegistActivity.class.getSimpleName();
	private WeakReference<ProgressDialog> weakPDialog = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_regist);
		btn_return_login = (Button) findViewById(R.id.btn_return_login);
		btn_return_login.setOnClickListener(this);
		btn_regist = (Button) findViewById(R.id.btn_regist);
		btn_regist.setOnClickListener(this);
		userName = (EditText) findViewById(R.id.edit_userName);
		userPass = (EditText) findViewById(R.id.edit_password);
		userPassConfirm = (EditText) findViewById(R.id.edit_password_confirm);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_return_login:
			this.finish();
			break;
		case R.id.btn_regist:
			regist();
		default:
			break;
		}

	}

	public void regist() {
		usernameString = userName.getText().toString().trim();
		passString = userPass.getText().toString().trim();
		repassString = userPassConfirm.getText().toString().trim();
		Pattern pattern = Pattern.compile("[a-zA-Z0-9_]{6,12}"); // 正则表达式匹配用户名6-12为的英文或数字的组合
		Pattern patternNum = Pattern.compile("[0][0-9]{5,11}"); // 正则表达式匹配用户名是纯数字的不能以0大头
		// 正则表达式匹配用户名为邮箱的情况
		Pattern patternMail = Pattern
				.compile("^([a-z0-9A-Z]+[-|.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?.)+[a-zA-Z]{2,}$");
		Pattern passPattern = Pattern.compile("[a-zA-Z0-9_]{6,16}");
		Matcher nameMatcher = pattern.matcher(usernameString);
		Matcher nameMatcherNum = patternNum.matcher(usernameString);
		Matcher passMatcher = passPattern.matcher(passString);
		Matcher nameMatcherMail = patternMail.matcher(usernameString);
		String msg = null;
		ProgressDialog pDialog = new ProgressDialog(this);
		pDialog.setCancelable(false);
		pDialog.setMessage("注册中...");
		weakPDialog = new WeakReference<ProgressDialog>(pDialog);
		if ("".equals(usernameString)) {
			msg = "用户名不能为空";
		} else if ("".equals(passString)) {
			msg = "密码不能为空";
		} else if (!passString.equals(repassString)) {
			msg = "两次输入的密码不一样";
		} else if (nameMatcherNum.matches()) {
			msg = "纯数字账号不能以0开头";
			userName.setText("");
		} else if (nameMatcher.matches() == false
				&& nameMatcherMail.matches() == false) {
			msg = "请输入邮箱或6-12位数字字母组合";
			userName.setText("");
		} else if (passMatcher.matches() == false) {
			msg = "请输入6-16位英文或数字组合";
			userPass.setText("");
		}
		if (msg != null) {
			Toast.makeText(RegistActivity.this, msg, Toast.LENGTH_SHORT).show();
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
						intent.setClass(RegistActivity.this, MainActivity.class);
						RegistActivity.this.startActivity(intent);
						CommonUtil.showToast(message);
					} else {
						CommonUtil.showToast(message);
					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					CommonUtil.showToast("注册失败");
				}
			}

			@Override
			public void onFailure(int type, String msg) {
				// TODO Auto-generated method stub
				Log.e(TAG, "onFailure:type-" + type + "msg" + msg);
				if (weakPDialog.get() != null) {
					weakPDialog.get().dismiss();
				}
				CommonUtil.showToast("注册失败");
			}
		};
		if (weakPDialog.get() != null) {
			weakPDialog.get().show();
		}
		HttpRequestUtil.loginOrRegistRequest(Constant.ACTION_REGIST,
				usernameString, passString, callback);

	}
}
