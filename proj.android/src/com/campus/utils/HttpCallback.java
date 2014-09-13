package com.campus.utils;

import com.campus.R;

public interface HttpCallback {

	public static int TIMEOUT_EXCEPTION = R.string.time_out;
	public static int FILENOTFOUND_EXCEPTION = R.string.file_not_find;
	public static int BADREQUEST_EXCEPTION = R.string.bad_request;
	public static int UNKOWN_RESPONSECODE = R.string.unknow_response;
	public static int UNKOWN_EXCEPTION = R.string.unknow_exception;
	public static int FILE_CHECK_ERROR = R.string.file_check_error;
	public static int TIME_OUT_DATE = R.string.check_sys_time;

	public void onSuccess(String msg);

	public void onFailure(int type, String msg);

}
