package com.pictureit.noambaroz.beautyapp.data;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import utilities.Log;
import android.content.ContentValues;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pictureit.noambaroz.beautyapp.server.PostVerifyUser;
import com.pictureit.noambaroz.beautyapp.server.ServerUtil;

public class JsonToObject {

	private interface JsonType {
		String TYPE_ARRAY = "JSONArray";
		String TYPE_OBJECT = "JSONObject";
	}

	private static String getJson(String json, String type) {
		String s = "";
		if (type.equals(JsonType.TYPE_ARRAY)) {

			try {
				JSONObject o = new JSONObject(json);
				JSONArray ja = o.getJSONArray("d");
				s = ja.toString();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else if (type.equals(JsonType.TYPE_OBJECT)) {

			try {
				JSONObject o = new JSONObject(json);
				JSONObject ja = o.getJSONObject("d");
				s = ja.toString();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return s;

	}

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
		try {
			String finalString = getJson(json, JsonType.TYPE_ARRAY);
			Type arrayType = new TypeToken<ArrayList<MarkerData>>() {
			}.getType();
			arrayList = new Gson().fromJson(finalString, arrayType);

		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		return arrayList;
	}

	public static ArrayList<Beautician> jsonToBeauticianArray(String json) {
		ArrayList<Beautician> arrayList = new ArrayList<Beautician>();
		String finalString = getJson(json, JsonType.TYPE_ARRAY);
		Type type = new TypeToken<ArrayList<Beautician>>() {
		}.getType();
		try {
			arrayList = new Gson().fromJson(finalString, type);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return arrayList;
	}

	public static Beautician jsonToBeautician(String json) {
		Beautician b = new Beautician();
		try {
			String finalString = getJson(json, JsonType.TYPE_OBJECT);
			b = new Gson().fromJson(finalString, Beautician.class);
		} catch (Exception e) {
			Log.i("");
			// TODO: handle exception
		}
		return b;
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

	public static String jsonToVerifyUser(String json) {
		try {
			JSONObject jsonObject = new JSONObject(json);
			boolean isActive = jsonObject.getBoolean("active");
			if (isActive)
				return PostVerifyUser.VERIFY_USER_RESULT_ACTIVE;
			else
				return PostVerifyUser.VERIFY_USER_RESULT_NOT_ACTIVE;
		} catch (JSONException e) {
			Log.i(e.getMessage());
		}
		return null;
	}

	public static String[] jsonToAddresses(String result) {
		try {
			JSONArray j = new JSONArray(getJson(result, JsonType.TYPE_ARRAY));
			String[] s = new String[j.length()];
			for (int i = 0; i < j.length(); i++) {
				s[i] = j.getString(i);
			}
			return s;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
