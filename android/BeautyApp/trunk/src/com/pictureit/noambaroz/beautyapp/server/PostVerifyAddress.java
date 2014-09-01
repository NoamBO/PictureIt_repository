package com.pictureit.noambaroz.beautyapp.server;

import org.json.JSONException;
import org.json.JSONObject;

import utilities.server.BaseHttpPost;
import android.content.Context;

import com.pictureit.noambaroz.beautyapp.data.JsonToObject;

public class PostVerifyAddress extends BaseHttpPost {

	private static final String KEY_ADDRESS = "address";

	public static void verify(Context context, String address, HttpCallback callback) {
		PostVerifyAddress httpPost = new PostVerifyAddress(context, callback, address);
		httpPost.execute();
	}

	public PostVerifyAddress(Context ctx, HttpCallback callback, String address) {
		super(ctx);
		this.callback = callback;
		prepare(address);
	}

	@Override
	protected Object continueInBackground(String result) {
		return JsonToObject.jsonToAddresses(result);
	}

	@Override
	protected void prepare(String request) {
		setUrl(ServerUtil.URL_REQUEST_VERIFY_ADDRESS);
		JSONObject json = new JSONObject();
		try {
			json.put(KEY_ADDRESS, request);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mMainJson = json;
	}

}
