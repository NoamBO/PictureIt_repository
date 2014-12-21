package com.pictureit.noambaroz.beautyapp.server;

import org.json.JSONException;
import org.json.JSONObject;

import utilities.server.BaseHttpPost;
import android.content.Context;

import com.pictureit.noambaroz.beautyapp.data.JsonToObject;

public class GetOrderSummary extends BaseHttpPost {

	public GetOrderSummary(Context ctx, HttpCallback callback, String historyId) {
		super(ctx, callback);
		prepare(historyId);
	}

	@Override
	protected Object continueInBackground(String result) {
		return JsonToObject.jsonToReorderTreatmentSummary(result);
	}

	@Override
	protected void prepare(String request) {
		setUrl(ServerUtil.URL_REQUEST_REORDER);
		mMainJson = new JSONObject();
		try {
			mMainJson.put(ServerUtil.HISTORY_ROW_ID, request);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
