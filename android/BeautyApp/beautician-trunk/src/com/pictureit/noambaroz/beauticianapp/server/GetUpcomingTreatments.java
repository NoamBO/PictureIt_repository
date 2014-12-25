package com.pictureit.noambaroz.beauticianapp.server;

import java.util.ArrayList;

import org.json.JSONObject;

import android.content.Context;

import com.pictureit.noambaroz.beauticianapp.data.UpcomingTreatment;

public class GetUpcomingTreatments extends BaseHttpPost {

	public GetUpcomingTreatments(Context ctx, HttpCallback callback) {
		super(ctx, callback);
		prepare(null);
	}

	@Override
	protected Object continueInBackground(String result) {
		ArrayList<UpcomingTreatment> array = JsonToObject.getUpcomingTretments(result);
		if (array == null)
			return 1;
		else
			return array;
	}

	@Override
	protected void prepare(String request) {
		setUrl(ServerUtil.URL_REQUEST_GET_UPCOMING_TREATMENTS);
		try {
			JSONObject temp = new JSONObject();
			temp.put(ServerUtil.UID, getUid());
			mMainJson = temp;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
