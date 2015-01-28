package com.pictureit.noambaroz.beautyapp.server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import utilities.server.BaseHttpPost;
import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;

import com.pictureit.noambaroz.beautyapp.data.DataProvider;
import com.pictureit.noambaroz.beautyapp.data.JsonToObject;
import com.pictureit.noambaroz.beautyapp.data.JsonToObject.JsonType;

public class PostCheckMessages extends BaseHttpPost {

	public PostCheckMessages(Context ctx, HttpCallback callback) {
		super(ctx, callback);
		prepare(null);
	}

	@Override
	protected Object continueInBackground(String result) {
		syncMessagesInBackground(result);
		return "";
	}

	private void syncMessagesInBackground(String jsonArray) {
		if (TextUtils.isEmpty(jsonArray))
			return;

		JSONArray array;
		try {
			array = new JSONArray(JsonToObject.getJson(jsonArray, JsonType.TYPE_ARRAY));
		} catch (JSONException e) {
			e.printStackTrace();
			return;
		}

		if (array.length() == 0) {
			ctx.getContentResolver().delete(DataProvider.CONTENT_URI_MESSAGES, null, null);
			return;
		} else {
			Cursor c = ctx.getContentResolver().query(DataProvider.CONTENT_URI_MESSAGES, null, null, null, null);
			if (c.moveToFirst())
				do {
					for (int i = 0; i < array.length(); i++) {
						String notificationId = c.getString(c.getColumnIndex(DataProvider.COL_NOTIFICATION_ID));

						String json = null;
						try {
							json = array.get(i).toString();
						} catch (JSONException e) {
							e.printStackTrace();
						}
						if (TextUtils.isEmpty(json))
							continue;
						String messageID = JsonToObject.jsonGetMessageId(json);
						if (TextUtils.isEmpty(messageID))
							continue;

						if (notificationId.equalsIgnoreCase(messageID)) {
							break;
						}
						ctx.getContentResolver().delete(DataProvider.CONTENT_URI_MESSAGES,
								DataProvider.COL_NOTIFICATION_ID + " = ?", new String[] { notificationId });
					}

				} while (c.moveToNext());
		}
	}

	@Override
	protected void prepare(String request) {
		showProgressDialog = false;
		setUrl(ServerUtil.URL_REQUEST_CHECK_MESSAGES);
		try {
			mMainJson = new JSONObject();
			mMainJson.put(ServerUtil.UID, getUid());
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}
