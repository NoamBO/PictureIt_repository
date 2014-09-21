package com.pictureit.noambaroz.beautyapp.server;

import org.json.JSONException;
import org.json.JSONObject;

import utilities.server.BaseHttpPost;
import android.content.Context;

import com.pictureit.noambaroz.beautyapp.data.JsonToObject;

public class PostVerificationRegisterCode extends BaseHttpPost {

	public PostVerificationRegisterCode(Context ctx, HttpCallback callback, String code) {
		super(ctx);
		this.callback = callback;
		prepare(code);
	}

	@Override
	protected Object continueInBackground(String result) {
		if (JsonToObject.isResponseOk(result))
			return true;
		else
			return false;
	}

	@Override
	protected void prepare(String request) {
		setUrl(ServerUtil.URL_REQUEST_VERIFICATION_REGISTER_CODE);
		JSONObject temp = new JSONObject();
		try {
			temp.put(ServerUtil.UID, getUid());
			temp.put(ServerUtil.CODE, request);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mMainJson = temp;
	}

}
