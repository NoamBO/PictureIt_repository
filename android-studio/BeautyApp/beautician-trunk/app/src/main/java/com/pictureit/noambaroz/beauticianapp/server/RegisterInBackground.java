package com.pictureit.noambaroz.beauticianapp.server;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.pictureit.noambaroz.beauticianapp.Constant;

public class RegisterInBackground extends BaseHttpPost {

	public static final int REGISTER_CODE_INVALID = 12;
	public static final int REGISTER_SERVER_FAILED = 13;
	public static final int REGISTER_SUCCESS = 14;

	private String image, code;

	public RegisterInBackground(Context ctx, String registrationCode, String image, HttpCallback callback) {
		super(ctx, callback);
		this.image = image;
		this.code = registrationCode;
		prepare(null);
	}

	@Override
	protected Object continueInBackground(String result) {
		if (statusCode != 200)
			return REGISTER_SERVER_FAILED;
		if (JsonToObject.isResponseOk(result)) {
			String uid = JsonToObject.jsonGetUid(result);
			if (!TextUtils.isEmpty(uid)) {
				storeUidOnDevice(uid);
				return REGISTER_SUCCESS;
			}
		}
		return REGISTER_CODE_INVALID;
	}

	@Override
	protected void prepare(String request) {
		setUrl(ServerUtil.URL_REQUEST_REGISTER);
		JSONObject temp = new JSONObject();
		try {
			temp.put(ServerUtil.CODE, code);
			temp.put(ServerUtil.IMAGE, image != null ? image : "");
			mMainJson = temp;
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	private void storeUidOnDevice(String uid) {
		SharedPreferences.Editor editor = ctx.getSharedPreferences(Constant.APP_PREFS_NAME, Context.MODE_PRIVATE)
				.edit();
		editor.putString(Constant.PREFS_KEY_UID, uid);
		editor.commit();
	}

}
