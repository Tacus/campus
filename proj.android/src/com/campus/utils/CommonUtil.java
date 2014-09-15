package com.campus.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.AssetManager;
import android.graphics.Point;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Toast;

public class CommonUtil {
	private static String deviceModel = null;
	private static String macAddress = null;
	private static String gameVersionName = null;
	private static int gameVersionCode = -1;
	private static AssetManager assetMng = null;
	private static Context mContext;
	private static final int MSG_SHOW_TOAST = 0x001;
	private static MyHandler handler = null;
	private static SharedPreferences spf = null;
	private static final String spfName = "spf_file";
	private static Point mScreenSize = null;

	public static void init(Context context) {
		mContext = context;
		handler = new MyHandler();
		spf = mContext.getSharedPreferences(spfName, Context.MODE_PRIVATE);
	}

	static class MyHandler extends Handler {

		public MyHandler() {
			mContext.getMainLooper();
		}

		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case MSG_SHOW_TOAST:
				String content = (String) msg.obj;
				Toast.makeText(mContext, content, Toast.LENGTH_SHORT).show();
				break;

			default:
				break;
			}
		}
	}

	@SuppressLint("NewApi")
	public static Point getScreenSize() {
		if (mScreenSize != null)
			return mScreenSize;
		WindowManager winMng = (WindowManager) mContext
				.getSystemService(Context.WINDOW_SERVICE);
		Display disPlay = winMng.getDefaultDisplay();

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			mScreenSize = new Point();
			disPlay.getSize(mScreenSize);
		} else {
			mScreenSize = new Point(disPlay.getWidth(), disPlay.getHeight());
		}
		return mScreenSize;
	}

	public String getAssetString(Context context, String fileName) {
		String content = "";
		InputStream in;
		if (assetMng == null) {
			assetMng = context.getAssets();
		}
		try {
			in = assetMng.open(fileName);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			content = br.readLine();
			br.close();
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return content;
	}

	public static String getUniqueAppUUID(Context context) {
		SharedPreferences userInfo = context.getSharedPreferences("app", 0);
		String appUUIDString = userInfo.getString("appUUID", "");
		if (appUUIDString.length() <= 0) {
			UUID appUuid = UUID.randomUUID();
			userInfo.edit().putString("appUUID", appUuid.toString()).commit();
			appUUIDString = appUuid.toString();
		}

		return appUUIDString;
	}

	public static String getUniqueDeviceId(Context context) {
		final TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);

		return tm.getDeviceId() + tm.getSimSerialNumber();
	}

	public static String getDeviceName() {
		final String device = Build.MANUFACTURER.replaceAll("\\W", "-") + "_"
				+ Build.MODEL.replaceAll("\\W", "-");
		return device;
	}

	public static String getMacAddress(Context context) {
		if (null == macAddress) {
			String str = "";
			try {
				Process pp = Runtime.getRuntime().exec(
						"cat /sys/class/net/wlan0/address ");
				InputStreamReader ir = new InputStreamReader(
						pp.getInputStream());
				LineNumberReader input = new LineNumberReader(ir);

				for (; null != str;) {
					str = input.readLine();
					if (str != null) {
						macAddress = str.trim();// 去空格
						break;
					}
				}
			} catch (IOException ex) {
				// 赋予默认值
				ex.printStackTrace();
				macAddress = getMacAddress(context);
			}
		}
		if (null == macAddress) {
			macAddress = getMacAddress(context);
		}

		return macAddress;
	}

	public static String getUniqueID(Context context) {

		// 1 compute IMEI
		TelephonyManager TelephonyMgr = (TelephonyManager) context
				.getSystemService(context.TELEPHONY_SERVICE);
		String m_szImei = TelephonyMgr.getDeviceId(); // Requires
														// READ_PHONE_STATE

		// 2 compute DEVICE ID
		String m_szDevIDShort = "35"
				+ // we make this look like a valid IMEI
				Build.BOARD.length() % 10 + Build.BRAND.length() % 10
				+ Build.CPU_ABI.length() % 10 + Build.DEVICE.length() % 10
				+ Build.DISPLAY.length() % 10 + Build.HOST.length() % 10
				+ Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10
				+ Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10
				+ Build.TAGS.length() % 10 + Build.TYPE.length() % 10
				+ Build.USER.length() % 10; // 13 digits
		// 3 android ID - unreliable
		String m_szAndroidID = Secure.getString(context.getContentResolver(),
				Secure.ANDROID_ID);

		// 4 wifi manager, read MAC address - requires
		// android.permission.ACCESS_WIFI_STATE or comes as null
		WifiManager wm = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		String m_szWLANMAC = wm.getConnectionInfo().getMacAddress();

		// 5 SUM THE IDs
		String m_szLongID = m_szImei + m_szDevIDShort + m_szAndroidID
				+ m_szWLANMAC;
		MessageDigest m = null;
		try {
			m = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		m.update(m_szLongID.getBytes(), 0, m_szLongID.length());
		byte p_md5Data[] = m.digest();

		String m_szUniqueID = new String();
		for (int i = 0; i < p_md5Data.length; i++) {
			int b = (0xFF & p_md5Data[i]);
			// if it is a single digit, make sure it have 0 in front (proper
			// padding)
			if (b <= 0xF)
				m_szUniqueID += "0";
			// add number to string
			m_szUniqueID += Integer.toHexString(b);
		}
		m_szUniqueID = m_szUniqueID.toUpperCase();
		Log.e("sxd", "getMacAddress:" + m_szUniqueID);
		return m_szUniqueID;
	}

	public static String getDeviceModel() {
		if (null == deviceModel) {
			String manufacturer = android.os.Build.MANUFACTURER;
			String model = android.os.Build.MODEL;
			String ret = manufacturer.toUpperCase() + "_" + model;
			if (model.startsWith(manufacturer)) {
				return model.toUpperCase();
			}
			deviceModel = ret + "_" + android.os.Build.VERSION.RELEASE;
		}
		return deviceModel;
	}

	public static String getVersionName() {
		if (null == gameVersionName) {
			PackageInfo packInfo;
			try {
				packInfo = mContext.getPackageManager().getPackageInfo(
						mContext.getPackageName(), 0);
				gameVersionName = packInfo.versionName;
				gameVersionCode = packInfo.versionCode;
			} catch (NameNotFoundException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return gameVersionName;
	}

	public static int getVersionCode() {
		if (-1 == gameVersionCode) {
			PackageInfo packInfo;
			try {
				packInfo = mContext.getPackageManager().getPackageInfo(
						mContext.getPackageName(), 0);
				gameVersionName = packInfo.versionName;
				gameVersionCode = packInfo.versionCode;
			} catch (NameNotFoundException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return gameVersionCode;
	}

	public static String getResString(Context context, int resId) {
		return context.getResources().getString(resId);
	}

	public static void showToast(String content) {
		Message msg = Message.obtain();
		msg.what = MSG_SHOW_TOAST;
		msg.obj = content;
		handler.sendMessage(msg);
	}

	public static void putSpfString(String name, String value) {
		spf.edit().putString(name, value).apply();
	}

	public static String getSpfString(String name) {
		// TODO Auto-generated method stub
		return spf.getString("accountid", "");
	}

	static class Size {
		float width;
		float height;
	}

}
