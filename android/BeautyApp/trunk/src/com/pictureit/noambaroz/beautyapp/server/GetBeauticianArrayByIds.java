package com.pictureit.noambaroz.beautyapp.server;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import utilities.Log;
import utilities.server.BaseHttpPost;
import android.content.Context;

import com.pictureit.noambaroz.beautyapp.data.JsonToObject;

public class GetBeauticianArrayByIds extends BaseHttpPost {

	private ArrayList<String> IDs;

	public GetBeauticianArrayByIds(Context ctx, HttpCallback callback, ArrayList<String> IDs) {
		super(ctx);
		this.callback = callback;
		this.IDs = IDs;
		prepare(ServerUtil.URL_REQUEST_GET_BEAUTICIAN_ARRAY_BY_IDs);
		// TODO remove >
		onPostExecute(continueInBackground(json));
	}

	@Override
	protected Object continueInBackground(String result) {
		return JsonToObject.jsonToBeauticianArray(result);
	}

	@Override
	protected void prepare(String request) {
		setUrl(request);
		JSONObject jsonObject = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		for (int i = 0; i < IDs.size(); i++) {
			try {
				jsonArray.put(IDs.get(i));
			} catch (Exception e) {
				Log.i("GetBeauticianArrayByIds", "failed to add String to JSONArray");
				e.printStackTrace();
			}
		}
		try {
			jsonObject.put(ServerUtil.IDs, jsonArray);
		} catch (JSONException e) {
			Log.i("GetBeauticianArrayByIds", "failed to put JSONArray in MainJson");
			e.printStackTrace();
		}
		mMainJson = jsonObject;
	}

	String json = "[{\"id\":\"3464647767\",\"photo\":\"http://i.dailymail.co.uk/i/pix/2013/01/14/article-2261943-16EB459B000005DC-367_634x369.jpg\",\"name\":\"1\",\"address\":{\"street\":\"sirkin 1\",\"city\":\"ramat gan\"},\"rating\":{\"rate\":4.5,\"raters\":20},\"degrees\":[\"\",\"\"],\"description\":\"description1\",\"treatments\":[\"1\",\"2\",\"3\"]},"
			+ "{\"id\":\"3464647767\",\"photo\":\"http://www.mwctoys.com/images/review_toystory_1.jpg\",\"name\":\"2\",\"address\":{\"street\":\"sirkin 2\",\"city\":\"ramat gan\"},\"rating\":{\"rate\":4.0,\"raters\":30},\"degrees\":[\"\",\"\"],\"description\":\"description2\",\"treatments\":[\"4\",\"5\",\"6\"]},"
			+ "{\"id\":\"3464647767\",\"photo\":\"http://www.binarymoon.co.uk/wp-content/uploads/2010/03/toy-story-3-woody.jpg\",\"name\":\"3\",\"address\":{\"street\":\"sirkin 3\",\"city\":\"ramat gan\"},\"rating\":{\"rate\":3.5,\"raters\":40},\"degrees\":[\"\",\"\"],\"description\":\"description3\",\"treatments\":[\"7\",\"8\",\"9\"]},"
			+ "{\"id\":\"3464647767\",\"photo\":\"http://www.dan-dare.org/FreeFun/Images/CMTVMore/ToyStory3Wallpaper1280x1024.jpg\",\"name\":\"4\",\"address\":{\"street\":\"sirkin 4\",\"city\":\"ramat gan\"},\"rating\":{\"rate\":3.0,\"raters\":50},\"degrees\":[\"\",\"\"],\"description\":\"description4\",\"treatments\":[\"2\",\"4\",\"6\"]},"
			+ "{\"id\":\"3464647767\",\"photo\":\"http://www.dan-dare.org/FreeFun/Images/CMTVMore/ToyStory3Wallpaper21600x1200.jpg\",\"name\":\"5\",\"address\":{\"street\":\"sirkin 5\",\"city\":\"ramat gan\"},\"rating\":{\"rate\":2.5,\"raters\":60},\"degrees\":[\"\",\"\"],\"description\":\"description5\",\"treatments\":[\"8\",\"10\",\"12\"]}"
			+ "]";

}
