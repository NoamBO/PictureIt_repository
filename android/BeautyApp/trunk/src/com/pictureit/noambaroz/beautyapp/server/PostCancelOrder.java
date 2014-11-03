package com.pictureit.noambaroz.beautyapp.server;

import org.json.JSONException;
import org.json.JSONObject;

import utilities.server.BaseHttpPost;
import android.content.Context;

import com.pictureit.noambaroz.beautyapp.data.JsonToObject;

public class PostCancelOrder extends BaseHttpPost {

	public PostCancelOrder(Context ctx, HttpCallback callback) {
		super(ctx);
		this.callback = callback;
		prepare(ServerUtil.URL_REQUEST_CANCEL_TREATMENT_ORDER);
	}

	@Override
	protected Object continueInBackground(String result) {
		if (JsonToObject.isResponseOk(result))
			return "";
		return null;
	}

	@Override
	protected void prepare(String request) {
		mMainJson = new JSONObject();
		try {
			mMainJson.put(ServerUtil.UID, getUid());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setUrl(request);
	}

}
