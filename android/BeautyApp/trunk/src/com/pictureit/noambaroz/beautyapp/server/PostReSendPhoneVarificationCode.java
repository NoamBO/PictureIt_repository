package com.pictureit.noambaroz.beautyapp.server;

import org.json.JSONException;
import org.json.JSONObject;

import utilities.server.BaseHttpPost;
import android.content.Context;

import com.pictureit.noambaroz.beautyapp.data.JsonToObject;

public class PostReSendPhoneVarificationCode extends BaseHttpPost {

	public PostReSendPhoneVarificationCode(Context ctx, HttpCallback callback) {
		super(ctx);
		this.callback = callback;
		prepare(null);
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
		setUrl(ServerUtil.URL_REQUEST_RE_SEND_REGISTER_CODE);
		JSONObject temp = new JSONObject();
		try {
			temp.put(ServerUtil.UID, getUid());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
