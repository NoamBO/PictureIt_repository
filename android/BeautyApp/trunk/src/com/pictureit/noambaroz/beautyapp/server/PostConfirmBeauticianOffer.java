package com.pictureit.noambaroz.beautyapp.server;

import org.json.JSONException;
import org.json.JSONObject;

import utilities.server.BaseHttpPost;
import android.content.Context;

import com.pictureit.noambaroz.beautyapp.data.JsonToObject;

public class PostConfirmBeauticianOffer extends BaseHttpPost {

	public PostConfirmBeauticianOffer(Context ctx, HttpCallback callback, String messageId) {
		super(ctx);
		this.callback = callback;
		prepare(messageId);
	}

	@Override
	protected Object continueInBackground(String result) {
		return JsonToObject.isResponseOk(result);
	}

	@Override
	protected void prepare(String request) {
		setUrl(ServerUtil.URL_REQUEST_CONFIRM_BEAUTICIAN_OFFER);
		mMainJson = new JSONObject();
		try {
			mMainJson.put(ServerUtil.MESSAGE_ID, request);
			mMainJson.put("isaccepted", "true");
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}
