package com.pictureit.noambaroz.beautyapp.server;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import utilities.server.BaseHttpPost;
import android.content.Context;

import com.pictureit.noambaroz.beautyapp.data.JsonToObject;
import com.pictureit.noambaroz.beautyapp.data.TreatmentType;

public class PostSearchBeautician extends BaseHttpPost {

	public PostSearchBeautician(Context ctx, HttpCallback callback, String name, String location,
			ArrayList<TreatmentType> treatment) {
		super(ctx);
		this.callback = callback;
		prepare(ServerUtil.URL_REQUEST_SEARCH_BEAUTICIAN);
		setRequest(name, location, treatment);
	}

	private void setRequest(String name, String location, ArrayList<TreatmentType> treatments) {
		JSONObject temp = new JSONObject();
		try {
			temp.put(ServerUtil.NAME, name);
			temp.put(ServerUtil.LOCATION, location);
			JSONArray treatmentsJson = new JSONArray();
			if (treatments != null && treatments.size() > 0)
				for (TreatmentType treatment : treatments) {
					if (treatment.getAmount() > 0)
						treatmentsJson.put(treatment.getTreatments_id());
				}
			temp.put(ServerUtil.TREATMENT, treatmentsJson);
			mMainJson = temp;
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected Object continueInBackground(String result) {
		return JsonToObject.jsonToBeauticianArray(result);
	}

	@Override
	protected void prepare(String request) {
		setUrl(request);
	}

}
