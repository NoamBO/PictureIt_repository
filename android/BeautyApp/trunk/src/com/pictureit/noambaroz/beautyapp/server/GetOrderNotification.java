package com.pictureit.noambaroz.beautyapp.server;

import utilities.server.BaseHttpGet;
import android.content.ContentValues;
import android.content.Context;

import com.pictureit.noambaroz.beautyapp.data.DataUtil;
import com.pictureit.noambaroz.beautyapp.data.JsonToObject;

public class GetOrderNotification extends BaseHttpGet {

	// TODO remove
	String orderId;

	public GetOrderNotification(Context ctx, String orderId, HttpCallback callback) {
		super(ctx);
		this.callback = callback;
		prepare(orderId);
		// TODO remove
		this.orderId = orderId;
		// TODO remove
		onPostExecute(continueInBackground(json));
	}

	@Override
	protected Object continueInBackground(String result) {
		// TODO add > String orderId = JsonToObject.jsonGetString(result,
		// "order_id");
		if (orderId == null || orderId.equalsIgnoreCase(""))
			return null;
		ContentValues values = JsonToObject.jsonToOrderNotificationContentValues(result);
		DataUtil.pushOrderNotificationToTable(ctx, values, orderId);
		return "";
	}

	@Override
	protected void prepare(String request) {
		setUrl("URL/" + request);
	}

	String json = "{" + "\"order_id\":\"34545650\"," + "\"beautician_id\":\"45645645645\"," + "\"name\":\"Olga Rova\","
			+ "\"pic\":\"http://saints.blogs.com/saint_of_the_month_club/images/olga.jpg\","
			+ "\"address\":\"Menachem Begin 55, Tel Aviv\"," + "\"raters\":24," + "\"rate\":2.5,"
			+ "\"at\":456566435456," + "\"location\":\"at the beautician\"," + "\"price\":\"56 nis\"" + "}";
}
