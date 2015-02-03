package com.pictureit.noambaroz.beautyapp.server;

import org.json.JSONException;
import org.json.JSONObject;

import utilities.Dialogs;
import utilities.server.BaseHttpPost;
import android.content.Context;

import com.pictureit.noambaroz.beautyapp.data.JsonToObject;

public class PostUpdateProfilePicture extends BaseHttpPost {

	private String image;

	public PostUpdateProfilePicture(Context ctx, HttpCallback callback, String image) {
		super(ctx, callback);
		this.image = image;

	}

	@Override
	protected Object doInBackground(String... params) {
		if (image.equalsIgnoreCase(""))
			cancel(true);
		prepare(ServerUtil.URL_REQUEST_UPDATE_USER_PROFILE_PICTURE);
		return super.doInBackground(params);
	}

	@Override
	protected Object continueInBackground(String result) {
		if (JsonToObject.isResponseOk(result))
			return result;
		return null;
	}

	@Override
	protected void onPostExecute(Object result) {
		super.onPostExecute(result);
		if (result != null)
			Dialogs.successToast(ctx);
	}

	@Override
	protected void prepare(String request) {
		setUrl(request);
		JSONObject temp = new JSONObject();
		try {
			temp.put(ServerUtil.UID, getUid());
			temp.put(ServerUtil.PROFILE_IMAGE, image);
			mMainJson = temp;
		} catch (JSONException e) {
			e.printStackTrace();
			cancel(true);
		}
	}
}
