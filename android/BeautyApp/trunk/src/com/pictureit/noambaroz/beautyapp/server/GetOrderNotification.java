package com.pictureit.noambaroz.beautyapp.server;

import org.json.JSONException;
import org.json.JSONObject;

import utilities.server.BaseHttpPost;
import android.content.ContentValues;
import android.content.Context;
import android.text.TextUtils;

import com.pictureit.noambaroz.beautyapp.data.DataUtil;
import com.pictureit.noambaroz.beautyapp.data.JsonToObject;

public class GetOrderNotification extends BaseHttpPost {

	public GetOrderNotification(Context ctx, String orderId, HttpCallback callback) {
		super(ctx);
		this.callback = callback;
		prepare(orderId);
	}

	@Override
	protected Object continueInBackground(String result) {
		String orderId = JsonToObject.jsonGetMessageId(result);
		if (TextUtils.isEmpty(orderId))
			return null;
		ContentValues values = JsonToObject.jsonToOrderNotificationContentValues(result);
		DataUtil.pushOrderNotificationToTable(ctx, values, orderId);
		return "";
	}

	@Override
	protected void prepare(String request) {
		setUrl(ServerUtil.URL_REQUEST_GET_ORDER_MESSAGE_NOTIFICATION);
		mMainJson = new JSONObject();
		try {
			mMainJson.put(ServerUtil.MESSAGE_ID, request);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
