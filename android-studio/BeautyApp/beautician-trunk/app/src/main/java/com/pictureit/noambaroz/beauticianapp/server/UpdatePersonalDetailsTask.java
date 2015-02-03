package com.pictureit.noambaroz.beauticianapp.server;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.text.TextUtils;

public class UpdatePersonalDetailsTask extends BaseHttpPost {

	private String name;
	private String classificationID;
	private String businessName;
	private String byteArray;

	private UpdatePersonalDetailsTask(Context ctx, HttpCallback callback, String name, String classificationID,
			String businessName, String byteArray) {
		super(ctx, callback);
		this.name = name;
		this.classificationID = classificationID;
		this.businessName = businessName;
		this.byteArray = byteArray;
		prepare(null);
	}

	@Override
	protected Object continueInBackground(String result) {
		if (result == null)
			return 1;
		else
			return JsonToObject.isResponseOk(result);

	}

	@Override
	protected void prepare(String request) {
		setUrl(ServerUtil.URL_REQUEST_UPDATE_MY_DETAILS);
		try {
			JSONObject temp = new JSONObject();
			temp.put(ServerUtil.UID, getUid());
			temp.put(ServerUtil.NAME, name);
			temp.put(ServerUtil.BUSINESS_NAME, businessName);
			temp.put(ServerUtil.CLASSIFICATION, classificationID);
			temp.put(ServerUtil.IMAGE, byteArray);
			mMainJson = temp;
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public static class Builder {
		private String name;
		private String classificationID;
		private String businessName;
		private String byteArray;

		public void addName(String name) {
			this.name = name;
		}

		public void addClassification(String classificationID) {
			this.classificationID = classificationID;
		}

		public void addBusinessName(String businessName) {
			this.businessName = businessName;
		}

		public void addImage(String byteArray) {
			this.byteArray = byteArray;
		}

		public UpdatePersonalDetailsTask build(Context ctx, HttpCallback callback) {
			UpdatePersonalDetailsTask task = new UpdatePersonalDetailsTask(ctx, callback, validate(name),
					validate(classificationID), validate(businessName), validate(byteArray));
			return task;
		}

		private String validate(String string) {
			return TextUtils.isEmpty(string) ? "" : string;
		}

	}

}
