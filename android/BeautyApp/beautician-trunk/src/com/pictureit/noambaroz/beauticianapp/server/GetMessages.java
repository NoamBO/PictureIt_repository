package com.pictureit.noambaroz.beauticianapp.server;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.pictureit.noambaroz.beauticianapp.dialog.Dialogs;

public class GetMessages extends BaseHttpPost {

	public GetMessages(Context ctx, HttpCallback callback) {
		super(ctx, callback);
		prepare(null);
	}

	@Override
	protected Object continueInBackground(String result) {
		if (!JsonToObject.isResponseOk(result)) {
			Dialogs.showServerFailedDialog(ctx);
			return -1;
		} else {
			return JsonToObject.jsonToMessages(result);
		}
	}

	@Override
	protected void prepare(String request) {
		setUrl(ServerUtil.URL_REQUEST_GET_MESSAGES);
		mMainJson = new JSONObject();
		try {
			mMainJson.put(ServerUtil.UID, getUid());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
