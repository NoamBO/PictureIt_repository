package com.pictureit.noambaroz.beautyapp.server;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import utilities.Log;
import utilities.server.BaseHttpPost;
import android.content.Context;

import com.pictureit.noambaroz.beautyapp.data.JsonToObject;

public class GetBeauticianArrayByIds extends BaseHttpPost {

	private ArrayList<String> IDs;

	public GetBeauticianArrayByIds(Context ctx, HttpCallback callback, ArrayList<String> IDs) {
		super(ctx, callback);
		this.IDs = IDs;
		prepare(ServerUtil.URL_REQUEST_GET_BEAUTICIAN_ARRAY_BY_IDs);
	}

	@Override
	protected Object continueInBackground(String result) {
		return JsonToObject.jsonToBeauticianArray(result);
	}

	@Override
	protected void prepare(String request) {
		setUrl(request);
		JSONObject jsonObject = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		for (int i = 0; i < IDs.size(); i++) {
			try {
				jsonArray.put(IDs.get(i));
			} catch (Exception e) {
				Log.i("GetBeauticianArrayByIds", "failed to add String to JSONArray");
				e.printStackTrace();
			}
		}
		try {
			jsonObject.put(ServerUtil.IDs, jsonArray);
		} catch (JSONException e) {
			Log.i("GetBeauticianArrayByIds", "failed to put JSONArray in MainJson");
			e.printStackTrace();
		}
		mMainJson = jsonObject;
	}
}
