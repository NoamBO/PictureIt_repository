package com.pictureit.noambaroz.beauticianapp.server;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

public class GetMessageById extends BaseHttpPost {

	public GetMessageById(Context ctx, HttpCallback callback, String orderID) {
		super(ctx, callback);
		prepare(orderID);
	}

	@Override
	protected Object continueInBackground(String result) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void prepare(String request) {
		setUrl(ServerUtil.URL_REQUEST_GET_MESSAGE_BY_ID);
		JSONObject temp = new JSONObject();
		try {
			temp.put(ServerUtil.ORDER_ID, request);
			mMainJson = temp;
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}
