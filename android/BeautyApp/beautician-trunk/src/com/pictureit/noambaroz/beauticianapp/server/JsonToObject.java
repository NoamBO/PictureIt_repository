package com.pictureit.noambaroz.beauticianapp.server;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.pictureit.noambaroz.beauticianapp.data.Message;
import com.pictureit.noambaroz.beauticianapp.data.OrderAroundMe;
import com.pictureit.noambaroz.beauticianapp.data.UpcomingTreatment;

public class JsonToObject {

	private enum JsonType {
		TYPE_ARRAY, TYPE_OBJECT
	}

	private static String getJson(String json, JsonType type) {
		String s = "";
		if (type.equals(JsonType.TYPE_ARRAY)) {

			try {
				JSONObject o = new JSONObject(json);
				JSONArray ja = o.getJSONArray("d");
				s = ja.toString();
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (NullPointerException e) {
			} catch (Exception e) {
			}

		} else if (type.equals(JsonType.TYPE_OBJECT)) {

			try {
				JSONObject o = new JSONObject(json);
				JSONObject ja = o.getJSONObject("d");
				s = ja.toString();
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (NullPointerException e) {
			} catch (Exception e) {
			}

		}
		return s;
	}

	public static boolean isResponseOk(String json) {
		if (json == null)
			return false;

		boolean isOk = false;
		try {
			JSONObject j = new JSONObject(getJson(json, JsonType.TYPE_OBJECT));
			JSONObject statusModel = j.getJSONObject("base_status");
			if (statusModel.has(ServerUtil.SERVER_RESPONSE_STATUS)) {
				if (statusModel.getString(ServerUtil.SERVER_RESPONSE_STATUS).equalsIgnoreCase(
						ServerUtil.SERVER_RESPONSE_STATUS_SUCCESS)) {
					isOk = true;
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return isOk;
	}

	public static String jsonGetUid(String json) {
		String uid = null;

		String s = getJson(json, JsonType.TYPE_OBJECT);
		uid = jsonGetString(s, ServerUtil.UID);

		return uid;
	}

	public static ArrayList<UpcomingTreatment> getUpcomingTretments(String json) {
		String finalString = getJson(json, JsonType.TYPE_OBJECT);
		ArrayList<UpcomingTreatment> array = null;
		try {
			JSONArray j = new JSONObject(finalString).getJSONArray("upcomingtreatments");

			Type arrayType = new TypeToken<List<UpcomingTreatment>>() {
			}.getType();

			array = new GsonBuilder().serializeNulls().create().fromJson(j.toString(), arrayType);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return array;
	}

	public static ArrayList<OrderAroundMe> jsonToOrdersAroundMe(String json) {
		ArrayList<OrderAroundMe> array = null;

		String finalString = getJson(json, JsonType.TYPE_OBJECT);
		try {
			JSONArray j = new JSONObject(finalString).getJSONArray("beauticians_around");

			Type arrayType = new TypeToken<List<OrderAroundMe>>() {
			}.getType();

			array = new GsonBuilder().serializeNulls().create().fromJson(j.toString(), arrayType);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return array;
	}

	public static ArrayList<Message> jsonToMessages(String json) {
		ArrayList<Message> array = null;

		Message.class.getClass();
		String finalString = getJson(json, JsonType.TYPE_OBJECT);
		try {
			JSONArray j = new JSONObject(finalString).getJSONArray("messages");

			Type arrayType = new TypeToken<List<Message>>() {
			}.getType();

			array = new GsonBuilder().serializeNulls().create().fromJson(j.toString(), arrayType);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return array;
	}

	/* **********************************************************************************************************************
	 * **********************************************************************************************************************
	 */

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
			if (o != null)
				value = o.getInt(key);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return value;
	}

}
