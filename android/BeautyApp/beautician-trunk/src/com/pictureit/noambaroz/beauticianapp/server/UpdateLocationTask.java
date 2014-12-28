package com.pictureit.noambaroz.beauticianapp.server;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.location.Location;

import com.pictureit.noambaroz.beauticianapp.Log;

public class UpdateLocationTask extends BaseHttpPost {

	private Location mLocation;

	public UpdateLocationTask(Context ctx, Location location) {
		super(ctx, null);
		showProgressDialog = false;
		showNoConnectionDialog = false;
		mLocation = location;
		prepare(null);
	}

	@Override
	protected Object continueInBackground(String result) {
		if (!JsonToObject.isResponseOk(result)) {
			Log.i("Failed..");
		} else {
			Log.i("Success!");
		}
		return "";
	}

	@Override
	protected void prepare(String request) {
		if (mLocation == null) {
			Log.i("Location == null");
			return;
		}
		setUrl(ServerUtil.URL_REQUEST_UPDATE_LOCATION);
		JSONObject temp = new JSONObject();
		try {
			temp.put(ServerUtil.LATITUDE, mLocation.getLatitude());
			temp.put(ServerUtil.LONGITUDE, mLocation.getLongitude());
			temp.put(ServerUtil.UID, getUid());
			mMainJson = temp;
		} catch (JSONException e) {
			Log.i("Can't build json object");
			e.printStackTrace();
		}
	}

}
