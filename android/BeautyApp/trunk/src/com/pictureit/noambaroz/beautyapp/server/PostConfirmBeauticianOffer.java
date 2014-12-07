package com.pictureit.noambaroz.beautyapp.server;

import org.json.JSONException;
import org.json.JSONObject;

import utilities.Dialogs;
import utilities.server.BaseHttpPost;
import android.content.Context;

import com.pictureit.noambaroz.beautyapp.data.JsonToObject;

public class PostConfirmBeauticianOffer extends BaseHttpPost {

	String isConfirm;

	public PostConfirmBeauticianOffer(Context ctx, HttpCallback callback, String messageId, String toConfirm) {
		super(ctx);
		this.callback = callback;
		isConfirm = toConfirm;
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
			mMainJson.put("isaccepted", isConfirm);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onPostExecute(Object result) {
		super.onPostExecute(result);
		if ((Boolean) result)
			Dialogs.successToast(ctx);
	}

}
