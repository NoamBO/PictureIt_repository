package com.pictureit.noambaroz.beautyapp.server;

import utilities.server.BaseHttpGet;
import android.content.Context;

import com.pictureit.noambaroz.beautyapp.data.Beautician;
import com.pictureit.noambaroz.beautyapp.data.JsonToObject;

public class GetBeauticianById extends BaseHttpGet {

	public GetBeauticianById(Context ctx, HttpCallback callback, String id) {
		super(ctx);
		this.callback = callback;
		prepare(id);
	}

	@Override
	protected Object continueInBackground(String result) {
		if (result == null)
			return null;

		Beautician b = JsonToObject.jsonToBeautician(result);
		return b;
	}

	@Override
	protected void prepare(String id) {
		StringBuilder sb = new StringBuilder();
		sb.append(ServerUtil.URL_REQUEST_GET_BEAUTICIAN_BY_ID).append("?").append("ID=").append(id);
		setUrl(sb.toString());
	}

}
