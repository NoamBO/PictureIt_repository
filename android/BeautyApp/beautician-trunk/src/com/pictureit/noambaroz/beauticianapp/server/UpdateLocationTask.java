package com.pictureit.noambaroz.beauticianapp.server;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.location.Location;

import com.pictureit.noambaroz.beauticianapp.Log;
import com.pictureit.noambaroz.beauticianapp.data.DataUtils;
import com.pictureit.noambaroz.beauticianapp.data.OrderAroundMe;

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
		ArrayList<OrderAroundMe> array = JsonToObject.jsonToOrdersAroundMe(result);
		if (array == null || array.size() == 0)
			return "";
		else
			DataUtils.get(ctx).refreshMapFragment(array);
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
