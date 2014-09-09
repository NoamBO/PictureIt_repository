package com.pictureit.noambaroz.beautyapp.server;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import utilities.server.BaseHttpPost;
import android.content.Context;

import com.pictureit.noambaroz.beautyapp.R;
import com.pictureit.noambaroz.beautyapp.data.DataUtil;
import com.pictureit.noambaroz.beautyapp.data.TreatmentType;

public class PostOrderTreatment extends BaseHttpPost {

	public PostOrderTreatment(Context ctx, HttpCallback callback) {
		super(ctx);
		this.callback = callback;
	}

	public void start(String for_who, String time, String comments, String location, ArrayList<TreatmentType> treatments)
			throws Exception {

		String[] stringArray = ctx.getResources().getStringArray(R.array.dialog_when_array);
		JSONObject tempJson = new JSONObject();
		tempJson.put(ServerUtil.UID, getUid());
		tempJson.put(ServerUtil.FOR, for_who);
		if (stringArray[0].equalsIgnoreCase(time)) {
			SimpleDateFormat sdf = new SimpleDateFormat("mm/dd/yyyy", Locale.US);
			String currentDateandTime = sdf.format(new Date());
			time = currentDateandTime;
		}
		tempJson.put(ServerUtil.DATE, time);
		tempJson.put(ServerUtil.LOCATION, location);
		tempJson.put(ServerUtil.COMMENTS, comments);

		JSONArray array = new JSONArray();
		for (TreatmentType treatment : treatments) {
			if (treatment.count > 0)
				array.put(new JSONObject().put(ServerUtil.TREATMENT_ID, treatment.id).put(ServerUtil.AMOUNT, treatment.count));
		}
		tempJson.put(ServerUtil.TREATMENTS, array);
		mMainJson = tempJson;
		prepare(ServerUtil.URL_REQUEST_ORDER_TREATMENT);
		this.execute();
	}

	@Override
	protected Object continueInBackground(String result) {
		try {
			JSONObject json = new JSONObject(result);
			if (json.has(ServerUtil.ORDER_ID)) {
				DataUtil.savePendingTreatmentId(ctx, json.getString(ServerUtil.ORDER_ID));
			}
			callback.onAnswerReturn(null);
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		return "";
	}

	@Override
	protected void prepare(String request) {
		setUrl(request);
	}

}
