package com.pictureit.noambaroz.beautyapp.server;

import org.json.JSONException;
import org.json.JSONObject;

import utilities.server.BaseHttpPost;
import android.content.Context;

import com.pictureit.noambaroz.beautyapp.data.JsonToObject;

public class PostUpdateProfileData extends BaseHttpPost {

	private String email, firstName, lastName, address;

	public PostUpdateProfileData(Context ctx, HttpCallback callback, String firstName, String lastName, String address,
			String email) {
		super(ctx);
		this.callback = callback;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.address = address;
	}

	@Override
	protected Object doInBackground(String... params) {
		prepare(ServerUtil.URL_REQUEST_UPDATE_USER_PROFILE_DATA);
		return super.doInBackground(params);
	}

	@Override
	protected Object continueInBackground(String result) {
		if (JsonToObject.isResponseOk(result))
			return result;
		return null;
	}

	@Override
	protected void prepare(String request) {
		setUrl(request);
		JSONObject temp = new JSONObject();
		try {
			temp.put(ServerUtil.UID, getUid());
			temp.put(ServerUtil.FIRST_NANE, firstName);
			temp.put(ServerUtil.LAST_NAME, lastName);
			temp.put(ServerUtil.E_MAIL, email);
			temp.put(ServerUtil.ADDRESS, address);
			mMainJson = temp;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
