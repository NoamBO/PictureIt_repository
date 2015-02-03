package com.pictureit.noambaroz.beauticianapp.server;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

public class CancelRequestTask extends BaseHttpPost {

	public CancelRequestTask(Context ctx, HttpCallback callback, String orderID) {
		super(ctx, callback);
		prepare(orderID);
	}

	@Override
	protected Object continueInBackground(String result) {
		if (JsonToObject.isResponseOk(result))
			return "success";
		else
			return "";

	}

	@Override
	protected void prepare(String request) {
		setUrl(ServerUtil.URL_REQUEST_CANCEL_CUSTOMER_REQUEST);
		try {
			JSONObject temp = new JSONObject();
			temp.put(ServerUtil.UID, getUid());
			temp.put(ServerUtil.ORDER_ID, request);
			mMainJson = temp;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
