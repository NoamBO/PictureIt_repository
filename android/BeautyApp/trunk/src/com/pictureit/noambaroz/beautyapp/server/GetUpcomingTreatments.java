package com.pictureit.noambaroz.beautyapp.server;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import utilities.server.BaseHttpPost;
import android.content.Context;

import com.pictureit.noambaroz.beautyapp.data.JsonToObject;
import com.pictureit.noambaroz.beautyapp.data.UpcomingTreatment;

public class GetUpcomingTreatments extends BaseHttpPost {

	public GetUpcomingTreatments(Context ctx, HttpCallback callback) {
		super(ctx);
		this.callback = callback;
		prepare(ServerUtil.URL_REQUEST_GET_ALL_UPCOMING_TREATMENTS);
	}

	@Override
	protected Object continueInBackground(String result) {
		ArrayList<UpcomingTreatment> array = null;
		if (result != null) {
			// TODO delete
			result = getValidJson();
			array = JsonToObject.jsonToUpcomingTreatments(result);
		}
		return array;
	}

	@Override
	protected void prepare(String request) {
		setUrl(request);
		mMainJson = new JSONObject();
		try {
			mMainJson.put(ServerUtil.UID, getUid());
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	// TODO delete
	private String getValidJson() {
		return "{" + "\"d\": [" + "{" + "\"type\":\"WebServiceBU.Model.ModelClass.HistoryModel.HistoryClass\","
				+ "\"pic\":\"http://www.gameseek.co.uk/images/products/lego_storage_head_girl.jpg\","
				+ "\"beautician_id\":\"c4ffdsf354gffg\"," + "\"beautician_name\":\"sara\","
				+ "\"beautician_address\":\"תל אביב דרך השלום 55\"," + "\"beautician_raters\":6,"
				+ "\"beautician_rate\":4.0," + "\"price\": \"120 nis\","
				+ "\"beautician_nots\":\"אבגד גדיכ חחכגי לדחיכלחיד \"," + "\"phone\":\"0547475436\","
				+ "\"treatmentsArray\": [" + "{" + "\"__type\":\"WebServiceBU.Model.ModelClass.Order.TreatmentOrder\","
				+ "\"treatments_id\":1," + "\"amount\":1" + "}," + "{"
				+ "\"__type\":\"WebServiceBU.Model.ModelClass.Order.TreatmentOrder\"," + "\"treatments_id\":2,"
				+ "\"amount\":2" + "}," + "{" + "\"__type\":\"WebServiceBU.Model.ModelClass.Order.TreatmentOrder\","
				+ "\"treatments_id\":3," + "\"amount\":3" + "}" + "]," + "\"treatment_location\": \"במלון\","
				+ "\"treatment_date\":\"1414864800\"," + "\"treatment_name\":\"\"" + "}" + "]" + "}";
	}

}
