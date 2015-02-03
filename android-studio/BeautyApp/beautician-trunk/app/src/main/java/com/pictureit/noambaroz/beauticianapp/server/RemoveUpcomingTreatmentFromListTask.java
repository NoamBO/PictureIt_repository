package com.pictureit.noambaroz.beauticianapp.server;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

public class RemoveUpcomingTreatmentFromListTask extends BaseHttpPost {

	public RemoveUpcomingTreatmentFromListTask(Context ctx, HttpCallback callback, String upcomingTreatmentId) {
		super(ctx, callback);
		prepare(upcomingTreatmentId);
	}

	@Override
	protected Object continueInBackground(String result) {
		if (JsonToObject.isResponseOk(result))
			return "";
		else
			return 1;

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
