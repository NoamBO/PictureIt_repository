package com.pictureit.noambaroz.beautyapp.server;

import utilities.server.BaseHttpGet;
import android.content.Context;

import com.pictureit.noambaroz.beautyapp.data.Beautician;
import com.pictureit.noambaroz.beautyapp.data.JsonToObject;

public class GetBeauticianById extends BaseHttpGet {

	public GetBeauticianById(Context ctx, HttpCallback callback) {
		super(ctx);
		this.callback = callback;
		prepare(ServerUtil.URL_REQUEST_GET_BEAUTICIAN_BY_ID);
		// TODO execute() from activity/fragment;
		// And Remove >
		onPostExecute(continueInBackground(json));
	}

	@Override
	protected Object continueInBackground(String result) {
		if (result == null)
			return null;

		Beautician b = JsonToObject.jsonToBeautician(result);
		return b;
	}

	@Override
	protected void prepare(String request) {
		setUrl(request);
	}

	String json = "{\"id\":\"3464647767\",\"photo\":\"http://img2.wikia.nocookie.net/__cb20110918202014/marvelmovies/images/d/df/Beast-x-men.jpeg\",\"name\":\"Motti\",\"address\":{\"street\":\"sirkin 12\",\"city\":\"ramat gan\"},\"rating\":{\"rate\":4.5,\"raters\":24},\"degrees\":[\"Degree 1\",\"Degree 2\",\"Degree 3\"],\"description\":\"Description1\",\"treatments\":[\"1\",\"2\",\"3\"]}";

}
