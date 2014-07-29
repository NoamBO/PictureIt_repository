package com.pictureit.noambaroz.beautyapp.server;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import utilities.server.BaseHttpPost;
import android.content.Context;

import com.pictureit.noambaroz.beautyapp.data.DataUtil;
import com.pictureit.noambaroz.beautyapp.data.TreatmentType;

public class PostOrderTreatment extends BaseHttpPost {

	public PostOrderTreatment(Context ctx, HttpCallback callback) {
		super(ctx);
		this.callback = callback;
	}

	public void start(String for_who, String time, String comments,
			String location, ArrayList<TreatmentType> treatments)
			throws Exception {

		JSONObject tempJson = new JSONObject();
		tempJson.put(ServerUtil.UID, getUid());
		tempJson.put(ServerUtil.FOR, for_who);
		tempJson.put(ServerUtil.DATE, time);
		tempJson.put(ServerUtil.LOCATION, location);
		tempJson.put(ServerUtil.COMMENTS, comments);

		JSONArray array = new JSONArray();
		for (TreatmentType treatment : treatments) {
			if (treatment.count > 0)
				array.put(new JSONObject().put(ServerUtil.ID, treatment.id)
						.put(ServerUtil.AMOUNT, treatment.count));
		}
		tempJson.put(ServerUtil.TREATMENTS, array);
		mMainJson = tempJson;
		this.execute();
	}

	@Override
	protected Object continueInBackground(String result) {
		try {
			JSONObject json = new JSONObject(result);
			if (json.has(ServerUtil.ORDER_ID)) {
				DataUtil.savePendingTreatmentId(ctx,
						json.getString(ServerUtil.ORDER_ID));
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		return "";
	}

	@Override
	protected void prepare(String request) {
	}

}
