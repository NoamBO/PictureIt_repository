package com.pictureit.noambaroz.beauticianapp.server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

public class UpdatePersonalTreatmentsTask extends BaseHttpPost {

	private String[] treatmentIDs;

	public UpdatePersonalTreatmentsTask(Context ctx, HttpCallback callback, String[] treatmentIDs) {
		super(ctx, callback);
		this.treatmentIDs = treatmentIDs;
		prepare(null);
	}

	@Override
	protected Object continueInBackground(String result) {
		if (result == null)
			return 1;
		else
			return JsonToObject.isResponseOk(result);

	}

	@Override
	protected void prepare(String request) {
		setUrl(ServerUtil.URL_REQUEST_UPDATE_TREATMENTS);
		try {
			JSONObject temp = new JSONObject();
			temp.put(ServerUtil.UID, getUid());
			temp.put(ServerUtil.TREATMENTS, new JSONArray(treatmentIDs));
			mMainJson = temp;
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}
