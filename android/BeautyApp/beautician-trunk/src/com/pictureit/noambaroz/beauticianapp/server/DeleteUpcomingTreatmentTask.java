package com.pictureit.noambaroz.beauticianapp.server;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

public class DeleteUpcomingTreatmentTask extends BaseHttpPost {

	private String treatmentId, reasonToDelete;

	public DeleteUpcomingTreatmentTask(Context ctx, HttpCallback callback, String treatmentId, int reasonToDelete) {
		super(ctx, callback);
		this.treatmentId = treatmentId;
		this.reasonToDelete = String.valueOf(reasonToDelete);
		prepare(null);
	}

	@Override
	protected Object continueInBackground(String result) {
		if (JsonToObject.isResponseOk(result))
			return 1;
		else
			return "failed";
	}

	@Override
	protected void prepare(String request) {
		setUrl(ServerUtil.URL_REQUEST_CANCEL_UPCOMING_TREATMENT);

		try {
			JSONObject temp = new JSONObject();
			temp.put(ServerUtil.UPCOMING_TREATMENT_ID, treatmentId);
			temp.put(ServerUtil.REASON, reasonToDelete);
			mMainJson = temp;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
