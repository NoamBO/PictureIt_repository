package com.pictureit.noambaroz.beautyapp.server;

import org.json.JSONException;
import org.json.JSONObject;

import utilities.server.BaseHttpPost;
import android.content.Context;

import com.pictureit.noambaroz.beautyapp.data.JsonToObject;

public class PostRejectBeauticianOffer extends BaseHttpPost {

	public PostRejectBeauticianOffer(Context ctx, HttpCallback callback, String messageId) {
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
		setUrl(ServerUtil.URL_REQUEST_REJECT_BEAUTICIAN_OFFER);
		mMainJson = new JSONObject();
		try {
			mMainJson.put(ServerUtil.MESSAGE_ID, request);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}
