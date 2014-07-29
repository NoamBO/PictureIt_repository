package com.pictureit.noambaroz.beautyapp.server;

import org.json.JSONException;
import org.json.JSONObject;

import utilities.server.BaseHttpPost;
import android.content.Context;

import com.pictureit.noambaroz.beautyapp.data.JsonToObject;

public class PostUpdateEmail extends BaseHttpPost {

	private String email;

	public PostUpdateEmail(Context ctx, String email, HttpCallback callback) {
		super(ctx);
		this.callback = callback;
		this.email = email;
	}

	@Override
	protected Object doInBackground(String... params) {
		prepare(email);
		return super.doInBackground(params);
	}

	@Override
	protected Object continueInBackground(String result) {
		if (JsonToObject.isResponseOk(result))
			return result;
		return null;
	}

	@Override
	protected void prepare(String request) {
		JSONObject temp = new JSONObject();
		try {
			temp.put(ServerUtil.UID, getUid());
			temp.put(ServerUtil.E_MAIL, request);
			mMainJson = temp;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
