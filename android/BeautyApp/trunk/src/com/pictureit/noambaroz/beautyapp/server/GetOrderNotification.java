package com.pictureit.noambaroz.beautyapp.server;

import utilities.server.BaseHttpGet;
import android.content.ContentValues;
import android.content.Context;
import android.text.TextUtils;

import com.pictureit.noambaroz.beautyapp.data.DataUtil;
import com.pictureit.noambaroz.beautyapp.data.JsonToObject;

public class GetOrderNotification extends BaseHttpGet {

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
		StringBuilder sb = new StringBuilder();
		sb.append(ServerUtil.URL_REQUEST_GET_ORDER_MESSAGE_NOTIFICATION);
		sb.append("?order_id=");
		sb.append(request);
		setUrl(sb.toString());
	}

	// String json = "{" + "\"order_id\":\"67\"," + "\"beautician_id\":\"4\"," +
	// "\"name\":\"אלונה\","
	// +
	// "\"pic\":\"http://saints.blogs.com/saint_of_the_month_club/images/olga.jpg\","
	// + "\"address\":\"מנחם בגין 55, תל אביב\"," + "\"raters\":24," +
	// "\"rate\":2.5," + "\"at\":1410434070189,"
	// + "\"location\":\"at the beautician\"," + "\"price\":\"56 nis\"" + "}";
}
