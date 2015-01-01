package com.pictureit.noambaroz.beauticianapp.server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.pictureit.noambaroz.beauticianapp.data.MessageResponse;
import com.pictureit.noambaroz.beauticianapp.data.TimeUtils;
import com.pictureit.noambaroz.beauticianapp.data.TreatmentType;

public class BeauticianResponseTask extends BaseHttpPost {

	private MessageResponse mMessageResponse;

	public BeauticianResponseTask(Context ctx, HttpCallback callback, MessageResponse messageResponse) {
		super(ctx, callback);
		mMessageResponse = messageResponse;
		prepare(null);
	}

	@Override
	protected Object continueInBackground(String result) {
		if (JsonToObject.isResponseOk(result))
			return "success";
		else
			return "";
	}

	@Override
	protected void prepare(String request) {
		setUrl(ServerUtil.URL_REQUEST_BEAUTICIAN_RESPONSE);
		try {
			JSONObject temp = new JSONObject();
			temp.put(ServerUtil.UID, getUid());
			temp.put(ServerUtil.ORDER_ID, mMessageResponse.getOrderid());
			temp.put(ServerUtil.DATE, getDateInMillis());
			temp.put(ServerUtil.LOCATION, mMessageResponse.getPlace());
			temp.put(ServerUtil.PRICE, mMessageResponse.getPrice());
			temp.put(ServerUtil.REMARKS, mMessageResponse.getComments());
			temp.put(ServerUtil.TREATMENTS, getTreatmentsForJson());
			mMainJson = temp;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private long getDateInMillis() {
		StringBuilder sb = new StringBuilder();
		sb.append(mMessageResponse.getDate());
		sb.append(" ");
		sb.append(mMessageResponse.getHour());
		long date = TimeUtils.dateAndTimeToTimestamp(sb.toString());
		return date;
	}

	private JSONArray getTreatmentsForJson() {
		JSONArray array = new JSONArray();
		for (TreatmentType t : mMessageResponse.getTreatments()) {
			JSONObject j = new JSONObject();
			try {
				j.put("treatment_id", t.getTreatmentId());
				j.put("amount", t.getAmount());
				array.put(j);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return array;
	}

}
