package com.pictureit.noambaroz.beautyapp.server;

import org.json.JSONObject;

import utilities.server.BaseHttpPost;
import android.content.Context;

import com.pictureit.noambaroz.beautyapp.data.JsonToObject;

public class GetMarkers extends BaseHttpPost {

	private double latitude;
	private double longitude;

	public GetMarkers(Context ctx, HttpCallback callback, double latitude, double longitude) {
		super(ctx);
		this.callback = callback;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	@Override
	protected Object doInBackground(String... params) {
		prepare(ServerUtil.URL_REQUEST_GET_MARKERS);
		return super.doInBackground(params);
	}

	@Override
	protected Object continueInBackground(String result) {
		return JsonToObject.jsonToMarkers(result);
	}

	@Override
	protected void onPreExecute() {
		showProgressDialog = false;
		super.onPreExecute();
	}

	@Override
	protected void prepare(String request) {
		setUrl(request);
		JSONObject temp = new JSONObject();
		try {
			temp.put(ServerUtil.LATITUDE, latitude);
			temp.put(ServerUtil.LONGITUDE, longitude);
			mMainJson = temp;
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	// private String json =
	// "[{\"id\":\"123\",\"latitude\":\"32.067740\",\"longitude\":\"34.784276\",\"type\":\"\"},"
	// +
	// "{\"id\":\"234\",\"latitude\":\"32.067749\",\"longitude\":\"34.786025\",\"type\":\"\"},"
	// +
	// "{\"id\":\"345\",\"latitude\":\"32.068440\",\"longitude\":\"34.787870\",\"type\":\"\"},"
	// +
	// "{\"id\":\"456\",\"latitude\":\"32.067640\",\"longitude\":\"34.789351\",\"type\":\"\"},"
	// +
	// "{\"id\":\"567\",\"latitude\":\"32.066822\",\"longitude\":\"34.786357\",\"type\":\"\"}]";

}
