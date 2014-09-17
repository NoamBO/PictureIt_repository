package com.pictureit.noambaroz.beautyapp.server;

import org.json.JSONException;
import org.json.JSONObject;

import utilities.server.BaseHttpPost;
import android.content.Context;

import com.pictureit.noambaroz.beautyapp.data.JsonToObject;

public class PostSearchBeautician extends BaseHttpPost {

	public PostSearchBeautician(Context ctx, HttpCallback callback, String name, String location, String type, String treatment) {
		super(ctx);
		this.callback = callback;
		prepare(ServerUtil.URL_REQUEST_SEARCH_BEAUTICIAN);
		setRequest(name, location, type, treatment);
	}

	private void setRequest(String name, String location, String type, String treatment) {
		JSONObject temp = new JSONObject();
		try {
			temp.put(ServerUtil.NAME, name);
			temp.put(ServerUtil.LOCATION, location);
			temp.put(ServerUtil.TYPE, type);
			temp.put(ServerUtil.TREATMENT, treatment);
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

	String json = "{\"d\":[{\"id\":\"3464647767\",\"photo\":\"url\",\"name\":\"1\",\"address\":{\"street\":\"sirkin 12\",\"city\":\"ramat gan\"},\"rating\":{\"rate\":4.5,\"raters\":24},\"degrees\":[\"\",\"\"],\"description\":\"\",\"treatments\":[\"6\",\"7\",\"8\"]},"
			+ "{\"id\":\"3464647767\",\"photo\":\"url\",\"name\":\"2\",\"address\":{\"street\":\"sirkin 12\",\"city\":\"ramat gan\"},\"rating\":{\"rate\":4.5,\"raters\":24},\"degrees\":[\"\",\"\"],\"description\":\"\",\"treatments\":[\"6\",\"7\",\"8\"]},"
			+ "{\"id\":\"3464647767\",\"photo\":\"url\",\"name\":\"3\",\"address\":{\"street\":\"sirkin 12\",\"city\":\"ramat gan\"},\"rating\":{\"rate\":4.5,\"raters\":24},\"degrees\":[\"\",\"\"],\"description\":\"\",\"treatments\":[\"6\",\"7\",\"8\"]},"
			+ "{\"id\":\"3464647767\",\"photo\":\"url\",\"name\":\"4\",\"address\":{\"street\":\"sirkin 12\",\"city\":\"ramat gan\"},\"rating\":{\"rate\":4.5,\"raters\":24},\"degrees\":[\"\",\"\"],\"description\":\"\",\"treatments\":[\"6\",\"7\",\"8\"]},"
			+ "{\"id\":\"3464647767\",\"photo\":\"url\",\"name\":\"5\",\"address\":{\"street\":\"sirkin 12\",\"city\":\"ramat gan\"},\"rating\":{\"rate\":4.5,\"raters\":24},\"degrees\":[\"\",\"\"],\"description\":\"\",\"treatments\":[\"6\",\"7\",\"8\"]},"
			+ "{\"id\":\"3464647767\",\"photo\":\"url\",\"name\":\"6\",\"address\":{\"street\":\"sirkin 12\",\"city\":\"ramat gan\"},\"rating\":{\"rate\":4.5,\"raters\":24},\"degrees\":[\"\",\"\"],\"description\":\"\",\"treatments\":[\"6\",\"7\",\"8\"]},"
			+ "{\"id\":\"3464647767\",\"photo\":\"url\",\"name\":\"7\",\"address\":{\"street\":\"sirkin 12\",\"city\":\"ramat gan\"},\"rating\":{\"rate\":4.5,\"raters\":24},\"degrees\":[\"\",\"\"],\"description\":\"\",\"treatments\":[\"6\",\"7\",\"8\"]},"
			+ "{\"id\":\"3464647767\",\"photo\":\"url\",\"name\":\"8\",\"address\":{\"street\":\"sirkin 12\",\"city\":\"ramat gan\"},\"rating\":{\"rate\":4.5,\"raters\":24},\"degrees\":[\"\",\"\"],\"description\":\"\",\"treatments\":[\"6\",\"7\",\"8\"]},"
			+ "{\"id\":\"3464647767\",\"photo\":\"url\",\"name\":\"9\",\"address\":{\"street\":\"sirkin 12\",\"city\":\"ramat gan\"},\"rating\":{\"rate\":4.5,\"raters\":24},\"degrees\":[\"\",\"\"],\"description\":\"\",\"treatments\":[\"6\",\"7\",\"8\"]},"
			+ "{\"id\":\"3464647767\",\"photo\":\"url\",\"name\":\"10\",\"address\":{\"street\":\"sirkin 12\",\"city\":\"ramat gan\"},\"rating\":{\"rate\":4.5,\"raters\":24},\"degrees\":[\"\",\"\"],\"description\":\"\",\"treatments\":[\"6\",\"7\",\"8\"]},"
			+ "{\"id\":\"3464647767\",\"photo\":\"url\",\"name\":\"19\",\"address\":{\"street\":\"sirkin 12\",\"city\":\"ramat gan\"},\"rating\":{\"rate\":4.5,\"raters\":24},\"degrees\":[\"\",\"\"],\"description\":\"\",\"treatments\":[\"6\",\"7\",\"8\"]}"
			+ "]}";

}
