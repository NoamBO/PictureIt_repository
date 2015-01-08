package com.pictureit.noambaroz.beauticianapp.server;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

public class GetBeauticianDetailsTask extends BaseHttpPost {

	public GetBeauticianDetailsTask(Context ctx, HttpCallback callback) {
		super(ctx, callback);
		prepare(null);
	}

	@Override
	protected Object continueInBackground(String result) {
		if (!JsonToObject.isResponseOk(result))
			return 1;
		else
			return JsonToObject.jsonToPersonalDetails(result);
	}

	@Override
	protected void prepare(String request) {
		setUrl(ServerUtil.URL_REQUEST_GET_BEAUTICIAN_DETAILS);
		try {
			mMainJson = new JSONObject().put(ServerUtil.UID, getUid());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
