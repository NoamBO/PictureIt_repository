package com.pictureit.noambaroz.beauticianapp.server;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

public class GetStatistics extends BaseHttpPost {

	public static enum StatisticType {
		TreatmentStatistics, PricesStatistics
	}

	public GetStatistics(Context ctx, HttpCallback callback, StatisticType urlType) {
		super(ctx, callback);
		String url = getUrlAccourdingToStatisticType(urlType);
		prepare(url);
	}

	private String getUrlAccourdingToStatisticType(StatisticType urlType) {
		if (urlType.equals(StatisticType.PricesStatistics))
			return ServerUtil.URL_REQUEST_GET_PRICE_STATISTICS;
		else if (urlType.equals(StatisticType.TreatmentStatistics))
			return ServerUtil.URL_REQUEST_GET_TREATMENTS_STATISTICS;
		return null;
	}

	@Override
	protected Object continueInBackground(String result) {
		if (!JsonToObject.isResponseOk(result))
			return 1;

		return JsonToObject.jsonToStatistics(result);
	}

	@Override
	protected void prepare(String request) {
		setUrl(request);
		try {
			JSONObject temp = new JSONObject();
			temp.put(ServerUtil.UID, getUid());
			temp.put("date", System.currentTimeMillis() / 1000);
			mMainJson = temp;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
