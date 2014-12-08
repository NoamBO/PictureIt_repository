package com.pictureit.noambaroz.beautyapp.server;

import org.json.JSONException;
import org.json.JSONObject;

import utilities.Dialogs;
import utilities.server.BaseHttpPost;
import android.content.Context;

import com.pictureit.noambaroz.beautyapp.data.JsonToObject;

public class PostRate extends BaseHttpPost {

	private int rating;
	private String beauticianID;

	public PostRate(Context ctx, HttpCallback callback, String beauticianID, int rating) {
		super(ctx, callback);
		this.beauticianID = beauticianID;
		this.rating = rating;
		prepare(null);
	}

	@Override
	protected Object continueInBackground(String result) {
		return JsonToObject.isResponseOk(result);
	}

	@Override
	protected void prepare(String request) {
		setUrl(ServerUtil.URL_REQUEST_RATE);
		mMainJson = new JSONObject();
		try {
			mMainJson.put(ServerUtil.RATE, rating);
			mMainJson.put(ServerUtil.BEAUTICIAN_UID, beauticianID);
			mMainJson.put(ServerUtil.CUSTOMER_UID, getUid());
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
