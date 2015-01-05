package com.pictureit.noambaroz.beauticianapp.server;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

public class GetUpcomingTreatment extends BaseHttpPost {

	public GetUpcomingTreatment(Context ctx, HttpCallback callback, String treatmentID) {
		super(ctx, callback);
		prepare(treatmentID);
	}

	@Override
	protected Object continueInBackground(String result) {
		if (!JsonToObject.isResponseOk(result))
			return 1;
		else
			return JsonToObject.jsonGetUpcomingTretment(result);
	}

	@Override
	protected void prepare(String treatmentID) {
		setUrl(ServerUtil.URL_REQUEST_GET_UPCOMING_TREATMENT_BY_ID);
		try {
			JSONObject temp = new JSONObject();
			temp.put(ServerUtil.UPCOMING_TREATMENT_ID, treatmentID);
			mMainJson = temp;
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}
