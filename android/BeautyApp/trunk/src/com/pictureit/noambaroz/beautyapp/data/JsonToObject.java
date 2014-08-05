package com.pictureit.noambaroz.beautyapp.data;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pictureit.noambaroz.beautyapp.server.ServerUtil;

public class JsonToObject {

	public static boolean isResponseOk(String json) {
		boolean isOk = false;
		try {
			JSONObject j = new JSONObject(json);
			if (j.has(ServerUtil.SERVER_RESPONSE_STATUS)) {
				if (j.getString(ServerUtil.SERVER_RESPONSE_STATUS).equalsIgnoreCase(
						ServerUtil.SERVER_RESPONSE_STATUS_SUCCESS)) {
					isOk = true;
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return isOk;
	}

	public static ArrayList<MarkerData> jsonToMarkers(String json) {
		ArrayList<MarkerData> arrayList = new ArrayList<MarkerData>();
		Type arrayType = new TypeToken<ArrayList<MarkerData>>() {
		}.getType();
		arrayList = new Gson().fromJson(json, arrayType);
		return arrayList;
	}

	public static ArrayList<Beautician> jsonToBeauticianArray(String json) {
		Type type = new TypeToken<ArrayList<Beautician>>() {
		}.getType();
		return new Gson().fromJson(json, type);
	}

	public static Beautician jsonToBeautician(String json) {
		return new Gson().fromJson(json, Beautician.class);
	}

	public static List<Beautician> convertObjectToBeauticianArrayList(Object object) {
		List<Beautician> result = new ArrayList<Beautician>();
		if (object instanceof List) {
			for (int i = 0; i < ((List<?>) object).size(); i++) {
				Object item = ((List<?>) object).get(i);
				if (item instanceof Beautician) {
					result.add((Beautician) item);
				}
			}
		}
		return result;
	}

	public static ContentValues jsonToOrderNotificationContentValues(String json) {
		ContentValues values = new ContentValues(9);
		String beauticianId = JsonToObject.jsonGetString(json, "beautician_id");
		String name = JsonToObject.jsonGetString(json, "name");
		String picUrl = JsonToObject.jsonGetString(json, "pic");
		String address = JsonToObject.jsonGetString(json, "address");
		int raters = JsonToObject.jsonGetInt(json, "raters");
		int rate = JsonToObject.jsonGetInt(json, "rate");
		String at = JsonToObject.jsonGetString(json, "at");
		String location = JsonToObject.jsonGetString(json, "location");
		String price = JsonToObject.jsonGetString(json, "price");
		if (beauticianId != null && !beauticianId.equalsIgnoreCase("")) {
			values.put(DataProvider.COL_BEAUTICIAN_ID, beauticianId);
		}
		if (name != null && !name.equalsIgnoreCase("")) {
			values.put(DataProvider.COL_NAME, name);
		}
		if (picUrl != null && !picUrl.equalsIgnoreCase("")) {
			values.put(DataProvider.COL_PIC, picUrl);
		}
		if (address != null && !address.equalsIgnoreCase("")) {
			values.put(DataProvider.COL_ADDRESS, address);
		}

		values.put(DataProvider.COL_RATERS, raters);
		values.put(DataProvider.COL_RATE, rate);

		if (at != null && !at.equalsIgnoreCase("")) {
			values.put(DataProvider.COL_AT, at);
		}
		if (location != null && !location.equalsIgnoreCase("")) {
			values.put(DataProvider.COL_LOCATION, location);
		}
		if (price != null && !price.equalsIgnoreCase("")) {
			values.put(DataProvider.COL_PRICE, price);
		}
		return values;
	}

	public static String jsonGetString(String json, String key) {
		String value = null;
		try {
			JSONObject o = new JSONObject(json);
			value = o.getString(key);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return value;
	}

	public static int jsonGetInt(String json, String key) {
		int value = 0;
		try {
			JSONObject o = new JSONObject(json);
			value = o.getInt(key);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return value;
	}
}
