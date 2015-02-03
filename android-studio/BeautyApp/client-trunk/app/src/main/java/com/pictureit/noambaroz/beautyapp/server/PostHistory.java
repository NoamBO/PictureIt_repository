package com.pictureit.noambaroz.beautyapp.server;

import org.json.JSONException;
import org.json.JSONObject;

import utilities.server.BaseHttpPost;
import android.content.Context;

import com.pictureit.noambaroz.beautyapp.data.JsonToObject;

public class PostHistory extends BaseHttpPost {

	public PostHistory(Context ctx, HttpCallback callback) {
		super(ctx, callback);
		prepare(null);
	}

	@Override
	protected Object continueInBackground(String result) {
		return JsonToObject.jsonToHistoryList(result);
	}

	@Override
	protected void prepare(String request) {
		setUrl(ServerUtil.URL_REQUEST_GET_HISTORY_LIST);
		mMainJson = new JSONObject();
		try {
			mMainJson.put(ServerUtil.UID, getUid());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
