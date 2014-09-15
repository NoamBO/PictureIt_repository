package com.pictureit.noambaroz.beautyapp.server;

import org.json.JSONException;
import org.json.JSONObject;

import utilities.server.BaseHttpPost;
import android.content.Context;

import com.pictureit.noambaroz.beautyapp.data.JsonToObject;

public class PostVerifyUser extends BaseHttpPost {

	public static final String VERIFY_USER_RESULT_NOT_ACTIVE = "not_active";
	public static final String VERIFY_USER_RESULT_ACTIVE = "active";

	public PostVerifyUser(Context ctx, HttpCallback callback) {
		super(ctx);
		this.callback = callback;
		prepare(ServerUtil.URL_REQUEST_VERIFY_USER);
		// TODO remove
		onPostExecute(continueInBackground(TEMPORARY_JSON));
	}

	@Override
	protected Object continueInBackground(String result) {
		return JsonToObject.jsonToVerifyUser(result);
	}

	@Override
	protected void prepare(String request) {
		showProgressDialog = false;
		setUrl(request);
		JSONObject object = new JSONObject();
		try {
			object.put(ServerUtil.UID, getUid());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mMainJson = object;
	}

	private final String TEMPORARY_JSON = "{\"active\":\"false\"}";

}
