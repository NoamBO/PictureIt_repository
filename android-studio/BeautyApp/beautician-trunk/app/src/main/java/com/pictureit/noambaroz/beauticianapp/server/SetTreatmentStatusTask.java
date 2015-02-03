package com.pictureit.noambaroz.beauticianapp.server;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

public class SetTreatmentStatusTask extends BaseHttpPost {

	private String didWas;
	private String id;
	private String reasonID;

	public SetTreatmentStatusTask(Context ctx, HttpCallback callback, boolean didWas, int treatmentId, int reasonID) {
		super(ctx, callback);
		this.didWas = String.valueOf(didWas);
		this.id = String.valueOf(treatmentId);
		this.reasonID = String.valueOf(reasonID);
		prepare(null);
	}

	@Override
	protected Object continueInBackground(String result) {
		if (JsonToObject.isResponseOk(result))
			return "";
		else
			return 1;
	}

	@Override
	protected void prepare(String request) {
		setUrl(ServerUtil.URL_REQUEST_UPDATE_TREATMENT_STATUS);
		try {
			JSONObject temp = new JSONObject();
			temp.put(ServerUtil.UPCOMING_TREATMENT_ID, id);
			temp.put(ServerUtil.IS_ACCEPTED, didWas);
			temp.put(ServerUtil.REASON, reasonID);
			mMainJson = temp;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
