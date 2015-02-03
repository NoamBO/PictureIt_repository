package com.pictureit.noambaroz.beautyapp.server;

import org.json.JSONException;
import org.json.JSONObject;

import utilities.server.BaseHttpPost;
import android.content.Context;

import com.pictureit.noambaroz.beautyapp.data.JsonToObject;

public class PostRemoveCanceledTreatment extends BaseHttpPost {

	public PostRemoveCanceledTreatment(Context ctx, HttpCallback callback, String treatmentID) {
		super(ctx, callback);
		prepare(treatmentID);
	}

	@Override
	protected Object continueInBackground(String result) {
		if (!JsonToObject.isResponseOk(result))
			return 1;
		else
			return "";
	}

	@Override
	protected void prepare(String request) {
		setUrl(ServerUtil.URL_REQUEST_REMOVE_CANCELED_TREATMENT_FROM_LIST);
		JSONObject temp = new JSONObject();
		try {
			temp.put(ServerUtil.UPCOMING_TREATMENT_ID, request);
			mMainJson = temp;
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}
