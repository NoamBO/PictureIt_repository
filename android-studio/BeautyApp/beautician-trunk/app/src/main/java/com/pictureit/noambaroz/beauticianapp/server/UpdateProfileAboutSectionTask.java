package com.pictureit.noambaroz.beauticianapp.server;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.text.TextUtils;

public class UpdateProfileAboutSectionTask extends BaseHttpPost {

	private String about;
	private String experience;
	private String payment;

	private UpdateProfileAboutSectionTask(Context ctx, HttpCallback callback, String about, String experience,
			String payment) {
		super(ctx, callback);
		this.about = about;
		this.experience = experience;
		this.payment = payment;
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
		setUrl(ServerUtil.URL_REQUEST_UPDATE_PERSONAL_DETAILS);
		try {
			JSONObject temp = new JSONObject();
			temp.put(ServerUtil.UID, getUid());
			temp.put(ServerUtil.ABOUT, about);
			temp.put(ServerUtil.EXPERIENCE, experience);
			temp.put(ServerUtil.PAYMENT, payment);
			mMainJson = temp;
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public static class Builder {
		private String about;
		private String experience;
		private String payment;

		public Builder addAbout(String about) {
			this.about = about;
			return this;
		}

		public Builder addExperience(String experience) {
			this.experience = experience;
			return this;
		}

		public Builder addPaymentMethod(String payment) {
			this.payment = payment;
			return this;
		}

		public UpdateProfileAboutSectionTask build(Context ctx, HttpCallback callback) {
			UpdateProfileAboutSectionTask task = new UpdateProfileAboutSectionTask(ctx, callback, validate(about),
					validate(experience), validate(payment));
			return task;
		}

		private String validate(String string) {
			return TextUtils.isEmpty(string) ? "" : string;
		}

	}

}
