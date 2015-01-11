package com.pictureit.noambaroz.beauticianapp.server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

public class UpdatePersonalDiplomasTask extends BaseHttpPost {

	private String[] diplomas;

	public UpdatePersonalDiplomasTask(Context ctx, HttpCallback callback, String[] diplomas) {
		super(ctx, callback);
		this.diplomas = diplomas;
		prepare(null);
	}

	@Override
	protected Object continueInBackground(String result) {
		if (result == null)
			return 1;
		else
			return JsonToObject.isResponseOk(result);

	}

	@Override
	protected void prepare(String request) {
		setUrl(ServerUtil.URL_REQUEST_UPDATE_DIPLOMAS);
		try {
			JSONObject temp = new JSONObject();
			temp.put(ServerUtil.UID, getUid());
			temp.put(ServerUtil.DEGREES, new JSONArray(diplomas));
			mMainJson = temp;
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}
