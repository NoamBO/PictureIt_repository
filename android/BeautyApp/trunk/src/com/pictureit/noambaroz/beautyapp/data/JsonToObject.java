package com.pictureit.noambaroz.beautyapp.data;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

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

}
