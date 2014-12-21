package com.pictureit.noambaroz.beautyapp.server;

import org.json.JSONException;
import org.json.JSONObject;

import utilities.server.BaseHttpPost;
import android.content.Context;

import com.pictureit.noambaroz.beautyapp.data.JsonToObject;

public class PostCancelTreatment extends BaseHttpPost {

	public PostCancelTreatment(Context ctx, HttpCallback callback, String upcomingTreatmentId) {
		super(ctx, callback);
		prepare(upcomingTreatmentId);
	}

	@Override
	protected Object continueInBackground(String result) {
		return JsonToObject.isResponseOk(result);
	}

	@Override
	protected void prepare(String request) {
		setUrl(ServerUtil.URL_REQUEST_CANCEL_UPCOMING_TREATMENT);
		mMainJson = new JSONObject();
		try {
			mMainJson.put(ServerUtil.UPCOMING_TREATMENT_ID, request);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}
