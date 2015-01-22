package com.pictureit.noambaroz.beauticianapp.server;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

public class RemoveMessageFromListTask extends BaseHttpPost {

	public RemoveMessageFromListTask(Context ctx, HttpCallback callback, String orderID) {
		super(ctx, callback);
		prepare(orderID);
	}

	@Override
	protected Object continueInBackground(String result) {
		if (JsonToObject.isResponseOk(result))
			return "";
		else
			return 1;
	}

	@Override
	protected void prepare(String request) {
		setUrl(ServerUtil.URL_REQUEST_REMOVE_MESSAGE_FROM_LIST);
		JSONObject temp = new JSONObject();
		try {
			temp.put(ServerUtil.UID, getUid());
			temp.put(ServerUtil.MESSAGE_ID, request);
			mMainJson = temp;
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}
