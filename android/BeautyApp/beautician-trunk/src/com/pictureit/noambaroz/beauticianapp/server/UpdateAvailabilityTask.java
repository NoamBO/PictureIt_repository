package com.pictureit.noambaroz.beauticianapp.server;

import org.json.JSONObject;

import android.content.Context;

import com.pictureit.noambaroz.beauticianapp.Log;
import com.pictureit.noambaroz.beauticianapp.MyPreference;

public class UpdateAvailabilityTask extends BaseHttpPost {

	private boolean isAvailable;

	public UpdateAvailabilityTask(Context ctx, HttpCallback callback, boolean isAvailable) {
		super(ctx, callback);
		this.isAvailable = isAvailable;
		prepare(null);
	}

	@Override
	protected Object continueInBackground(String result) {
		if (JsonToObject.isResponseOk(result)) {
			MyPreference.setAvailability(isAvailable);
			Log.i("Availability changed to: " + isAvailable);
			return true;
		} else
			return false;
	}

	@Override
	protected void prepare(String request) {
		setUrl(ServerUtil.URL_REQUEST_UPDATE_AVAILABILITY);
		try {
			JSONObject j = new JSONObject();
			j.put(ServerUtil.UID, getUid());
			j.put(ServerUtil.IS_AVAILABLE, String.valueOf(this.isAvailable));
			mMainJson = j;
		} catch (Exception e) {
			Log.i("Cant build json object");
		}
	}

}
