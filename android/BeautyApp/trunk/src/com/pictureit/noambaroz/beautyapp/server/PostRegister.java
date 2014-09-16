package com.pictureit.noambaroz.beautyapp.server;

import org.json.JSONException;
import org.json.JSONObject;

import utilities.Dialogs;
import utilities.server.BaseHttpPost;
import android.app.Activity;
import android.content.Context;

import com.pictureit.noambaroz.beautyapp.R;
import com.pictureit.noambaroz.beautyapp.data.JsonToObject;

public class PostRegister extends BaseHttpPost {

	private String firstName, lastName, email, address, phoneNum;

	public PostRegister(Context ctx, HttpCallback callback, String firstName, String lastName, String email,
			String address, String phoneNumber) {
		super(ctx);
		this.callback = callback;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.address = address;
		this.phoneNum = phoneNumber;
		prepare(ServerUtil.URL_REQUEST_REGISTER);
	}

	@Override
	protected Object continueInBackground(String result) {
		if (result != null && JsonToObject.isResponseOk(result)) {
			try {
				String uid = new JSONObject(result).getJSONObject("d").getString(ServerUtil.UID);
				return uid;
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	protected void onPostExecute(Object result) {
		if (result == null)
			Dialogs.makeToastThatCloseActivity((Activity) ctx, R.string.dialog_messege_server_error);
		super.onPostExecute(result);
	}

	@Override
	protected void prepare(String request) {
		setUrl(request);
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put(ServerUtil.FIRST_NANE, firstName).put(ServerUtil.LAST_NAME, lastName)
					.put(ServerUtil.E_MAIL, email).put(ServerUtil.ADDRESS, address)
					.put(ServerUtil.PHONE_NUMBER, phoneNum);
		} catch (Exception e) {
			// TODO: handle exception
		}
		mMainJson = jsonObject;
	}

}
