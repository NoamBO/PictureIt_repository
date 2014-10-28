package com.pictureit.noambaroz.beautyapp.server;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import utilities.TimeUtils;
import utilities.server.BaseHttpPost;
import android.content.Context;
import android.location.Location;

import com.pictureit.noambaroz.beautyapp.data.JsonToObject;
import com.pictureit.noambaroz.beautyapp.data.TreatmentType;
import com.pictureit.noambaroz.beautyapp.helper.ServiceOrderManager;

public class PostOrderTreatment extends BaseHttpPost {

	String for_who;
	String time;
	String comments;
	String location;
	ArrayList<TreatmentType> treatments;

	public PostOrderTreatment(Context ctx, HttpCallback callback, String for_who, String time, String comments,
			String location, ArrayList<TreatmentType> treatments) {
		super(ctx);
		this.callback = callback;
		this.for_who = for_who;
		this.time = time;
		this.comments = comments;
		this.location = location;
		this.treatments = treatments;
	}

	public void byLocation(Location location) throws Exception {
		mMainJson = new JSONObject();
		mMainJson.put(ServerUtil.LATITUDE, location.getLatitude());
		mMainJson.put(ServerUtil.LONGITUDE, location.getLongitude());
		mMainJson.put(ServerUtil.UID, getUid());
		prepare(ServerUtil.URL_REQUEST_ORDER_TREATMENT_BY_LOCATION);
		start();
	}

	public void forSpecificBeautician(String beauticianId) throws Exception {
		mMainJson = new JSONObject();
		mMainJson.put(ServerUtil.BEAUTICIAN_UID, beauticianId);
		mMainJson.put(ServerUtil.CUSTOMER_UID, getUid());
		prepare(ServerUtil.URL_REQUEST_ORDER_TREATMENT);
		start();
	}

	private void start() throws Exception {

		mMainJson.put(ServerUtil.FOR, for_who);

		long timeInMilis = TimeUtils.dateToTimestamp(time);

		mMainJson.put(ServerUtil.DATE, timeInMilis);
		mMainJson.put(ServerUtil.LOCATION, location);
		mMainJson.put(ServerUtil.COMMENTS, comments == null ? "" : comments);

		JSONArray array = new JSONArray();
		for (TreatmentType treatment : treatments) {
			if (treatment.getAmount() > 0)
				array.put(new JSONObject().put(ServerUtil.TREATMENT_ID, treatment.getTreatments_id()).put(
						ServerUtil.AMOUNT, treatment.getAmount()));
		}
		mMainJson.put(ServerUtil.TREATMENTS, array);
		this.execute();
	}

	@Override
	protected Object continueInBackground(String result) {
		if (result == null || !JsonToObject.isResponseOk(result))
			return null;
		String s = "";
		try {
			JSONObject json = new JSONObject(result).getJSONObject("d");
			if (json.has(ServerUtil.ORDER_ID)) {
				ServiceOrderManager.savePendingTreatmentId(ctx, json.getString(ServerUtil.ORDER_ID));
				s = json.getString(ServerUtil.ORDER_ID);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		return s;
	}

	@Override
	protected void prepare(String request) {
		setUrl(request);
	}

}
