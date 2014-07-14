package com.campus.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;
import android.util.Log;

public class HttpRequestUtil {

	private static String Tag = "AsyncDownloadFile";

	public static void httpPost(String requestUrl, String content,
			HttpCallback callback) {
		httpRequest(requestUrl, content, "POST", callback);
	}

	private static void httpRequest(final String requestUrl,
			final String content, final String requestMethod,
			final HttpCallback callback) {

		SSLSocketFactory.getSocketFactory().setHostnameVerifier(
				new AllowAllHostnameVerifier());
		Log.e("httpRequest", "url:" + requestUrl + " content:" + content
				+ " method:" + requestMethod);
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				byte[] xmlData = content.getBytes();
				HttpURLConnection urlCon = null;
				try {
					URL url;
					url = new URL(requestUrl);
					urlCon = (HttpURLConnection) url.openConnection();
					urlCon.setDoOutput(true);
					urlCon.setDoInput(true);
					urlCon.setUseCaches(false);
					urlCon.setRequestProperty("Content-Type",
							"multipart/form-data");
					urlCon.setConnectTimeout(3000);
					urlCon.setReadTimeout(3000);
					urlCon.setRequestMethod(requestMethod);
					if (!TextUtils.isEmpty(content)) {
						DataOutputStream printout = new DataOutputStream(
								urlCon.getOutputStream());
						printout.write(xmlData);
						printout.flush();
						printout.close();
					}
					int responseCode = urlCon.getResponseCode();
					Log.e("response code", responseCode + "");
					switch (responseCode) {
					case HttpURLConnection.HTTP_OK:
						StringBuffer sb = new StringBuffer();
						String readLine;
						BufferedReader responseReader;
						// 处理响应流，必须与服务器响应流输出的编码一致
						responseReader = new BufferedReader(
								new InputStreamReader(urlCon.getInputStream(),
										"UTF-8"));
						while ((readLine = responseReader.readLine()) != null) {
							sb.append(readLine).append("\n");
						}
						callback.onSuccess(sb.toString());
						responseReader.close();
						break;

					case HttpURLConnection.HTTP_BAD_REQUEST:
						callback.onFailure(HttpCallback.BADREQUEST_EXCEPTION,
								requestUrl);
						break;
					case HttpURLConnection.HTTP_NOT_FOUND:
						callback.onFailure(HttpCallback.FILENOTFOUND_EXCEPTION,
								requestUrl);
						break;
					default:
						callback.onFailure(HttpCallback.UNKOWN_RESPONSECODE,
								"url:" + requestUrl + ";responseCode:"
										+ responseCode);
						break;
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					if (e.getMessage() != null) {
						if (e.getMessage().contains("FileNotFoundException"))
							callback.onFailure(
									HttpCallback.FILENOTFOUND_EXCEPTION,
									requestUrl);
						else if (e.getMessage().toLowerCase()
								.contains("timeout")) {
							callback.onFailure(HttpCallback.TIMEOUT_EXCEPTION,
									requestUrl);
						} else if (e.getMessage().contains(
								"No peer certificate")) {
							callback.onFailure(HttpCallback.TIME_OUT_DATE,
									requestUrl);

						} else {
							callback.onFailure(
									HttpCallback.UNKOWN_EXCEPTION,
									"url:" + requestUrl + ";errorInfo:"
											+ e.getMessage());
						}
					} else {

						callback.onFailure(HttpCallback.UNKOWN_EXCEPTION,
								"url:" + requestUrl);
					}
				} finally {
					if (urlCon != null)
						urlCon.disconnect();
				}
			}
		}).start();
	}

	public static void httpClientPost(final String requestUrl,
			final List<NameValuePair> params, final HttpCallback callback) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				SSLSocketFactory.getSocketFactory().setHostnameVerifier(
						new AllowAllHostnameVerifier());
				HttpPost httpRequest = new HttpPost(requestUrl);
				Log.e("httpClientPost ", " url:" + requestUrl + " body:"
						+ params.toString());
				// Post运作传送变数必须用NameValuePair[]阵列储存
				// 传参数 服务端获取的方法为request.getParameter("name")
				try {
					// 发出HTTP request
					httpRequest.setEntity(new UrlEncodedFormEntity(params,
							HTTP.UTF_8));
					DefaultHttpClient client = new DefaultHttpClient();
					client.getParams().setParameter(
							CoreConnectionPNames.CONNECTION_TIMEOUT, 3000);
					client.getParams().setParameter(
							CoreConnectionPNames.SO_TIMEOUT, 3000);
					// 取得HTTP response
					HttpResponse httpResponse = client.execute(httpRequest);
					// 若状态码为200 ok
					int responseCode = httpResponse.getStatusLine()
							.getStatusCode();
					switch (responseCode) {
					case 200:
						// 取出回应字串
						callback.onSuccess(EntityUtils.toString(
								httpResponse.getEntity(), "UTF-8"));
						break;

					case HttpURLConnection.HTTP_BAD_REQUEST:
						callback.onFailure(HttpCallback.BADREQUEST_EXCEPTION,
								requestUrl);
						break;
					case HttpURLConnection.HTTP_NOT_FOUND:
						callback.onFailure(HttpCallback.FILENOTFOUND_EXCEPTION,
								requestUrl);
						break;
					default:
						callback.onFailure(HttpCallback.UNKOWN_RESPONSECODE,
								"url:" + requestUrl + ";responseCode:"
										+ responseCode);
						break;
					}
				} catch (Exception e) {
					e.printStackTrace();
					if (e.getMessage() != null) {
						if (e.getMessage().contains("FileNotFoundException"))
							callback.onFailure(
									HttpCallback.FILENOTFOUND_EXCEPTION,
									requestUrl);
						else if (e.getMessage().toLowerCase()
								.contains("timeout")) {
							callback.onFailure(HttpCallback.TIMEOUT_EXCEPTION,
									requestUrl);
						} else if (e.getMessage().contains(
								"No peer certificate")) {
							callback.onFailure(HttpCallback.TIME_OUT_DATE,
									requestUrl);
						} else {
							callback.onFailure(
									HttpCallback.UNKOWN_EXCEPTION,
									"url:" + requestUrl + ";errorInfo:"
											+ e.getMessage());
						}
					} else {
						callback.onFailure(HttpCallback.UNKOWN_EXCEPTION,
								"url:" + requestUrl);
					}
				}
			}
		}).start();
	}

	public static void httpClientGet(String url,
			HashMap<String, String> params, HttpCallback callback) {
		url += "?" + resolveGetParams(params);
		httpRequest(url, "", "Get", callback);
	}

	private static List<NameValuePair> resolvePostParams(
			HashMap<String, String> params) {
		List<NameValuePair> requestParams = new ArrayList<NameValuePair>();
		Iterator<Entry<String, String>> iterator = params.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<String, String> entry = iterator.next();
			String key = entry.getKey();
			String value = entry.getValue();
			requestParams.add(new BasicNameValuePair(key, value));
		}
		return requestParams;
	}

	private static String resolveGetParams(HashMap<String, String> params) {
		String requestUrl = "";
		Iterator<Entry<String, String>> iterator = params.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<String, String> entry = iterator.next();
			String key = entry.getKey();
			String value = entry.getValue();
			requestUrl += key + "=" + value + "&";
		}
		requestUrl = requestUrl.substring(0, requestUrl.length() - 1);
		return requestUrl;
	}
}
