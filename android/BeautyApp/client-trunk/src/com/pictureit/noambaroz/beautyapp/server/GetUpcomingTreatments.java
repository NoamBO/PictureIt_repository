package com.pictureit.noambaroz.beautyapp.server;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import utilities.server.BaseHttpPost;
import android.content.Context;

import com.pictureit.noambaroz.beautyapp.data.JsonToObject;
import com.pictureit.noambaroz.beautyapp.data.UpcomingTreatment;

public class GetUpcomingTreatments extends BaseHttpPost {

	public GetUpcomingTreatments(Context ctx, HttpCallback callback) {
		super(ctx, callback);
		prepare(ServerUtil.URL_REQUEST_GET_ALL_UPCOMING_TREATMENTS);
	}

	@Override
	protected Object continueInBackground(String result) {
		ArrayList<UpcomingTreatment> array = null;
		if (result != null) {
			array = JsonToObject.jsonToUpcomingTreatments(result);
		}
		return array;
	}

	@Override
	protected void prepare(String request) {
		setUrl(request);
		mMainJson = new JSONObject();
		try {
			mMainJson.put(ServerUtil.UID, getUid());
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}
