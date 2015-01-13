package com.pictureit.noambaroz.beauticianapp.server;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

public class GetStatistics extends BaseHttpPost {

	public GetStatistics(Context ctx, HttpCallback callback) {
		super(ctx, callback);
		prepare(null);
	}

	@Override
	protected Object continueInBackground(String result) {
		if (!JsonToObject.isResponseOk(result))
			return 1;

		return JsonToObject.jsonToStatistics(result);
	}

	@Override
	protected void prepare(String request) {
		setUrl(ServerUtil.URL_REQUEST_GET_STATISTICS);
		try {
			JSONObject temp = new JSONObject();
			temp.put(ServerUtil.UID, getUid());
			temp.put("date", System.currentTimeMillis() / 1000);
			mMainJson = temp;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
