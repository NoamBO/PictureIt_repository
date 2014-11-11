package com.pictureit.noambaroz.beautyapp.server;

import org.json.JSONException;
import org.json.JSONObject;

import utilities.server.BaseHttpPost;
import android.content.Context;

import com.pictureit.noambaroz.beautyapp.data.JsonToObject;

public class PostSearchBeautician extends BaseHttpPost {

	public PostSearchBeautician(Context ctx, HttpCallback callback, String name, String location, String type,
			String treatment) {
		super(ctx);
		this.callback = callback;
		prepare(ServerUtil.URL_REQUEST_SEARCH_BEAUTICIAN);
		setRequest(name, location, type, treatment);
	}

	private void setRequest(String name, String location, String type, String treatment) {
		JSONObject temp = new JSONObject();
		try {
			temp.put(ServerUtil.NAME, name);
			temp.put(ServerUtil.LOCATION, location);
			temp.put(ServerUtil.TYPE, (type == null ? "" : type));
			temp.put(ServerUtil.TREATMENT, (treatment == null ? "" : treatment));
			mMainJson = temp;
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected Object continueInBackground(String result) {
		return JsonToObject.jsonToBeauticianArray(result);
	}

	@Override
	protected void prepare(String request) {
		setUrl(request);
	}

}
