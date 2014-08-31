package com.pictureit.noambaroz.beautyapp.server;

import utilities.server.BaseHttpPost;
import android.content.Context;

public class PostVerifyAddress extends BaseHttpPost {

	public static void verify(Context context, String address, HttpCallback callback) {
		PostVerifyAddress httpPost = new PostVerifyAddress(context, callback, address);
	}

	public PostVerifyAddress(Context ctx, HttpCallback callback, String address) {
		super(ctx);
		this.callback = callback;
		prepare(address);
	}

	@Override
	protected Object continueInBackground(String result) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void prepare(String request) {
		setUrl(ServerUtil.URL_REQUEST_VERIFY_ADDRESS);
	}

}
