package com.pictureit.noambaroz.beauticianapp.server;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.text.TextUtils;

public class UpdatePersonalContactOptionsTask extends BaseHttpPost {

	private String mail, businessAddress, billingAddress;
	private boolean isArrivedHome;
	private int areaCode;

	private UpdatePersonalContactOptionsTask(Context ctx, HttpCallback callback, String mail, String businessAddress,
			String billingAddress, boolean isArrivedHome, int areaCode) {
		super(ctx, callback);
		this.mail = mail;
		this.businessAddress = businessAddress;
		this.billingAddress = billingAddress;
		this.isArrivedHome = isArrivedHome;
		this.areaCode = areaCode;
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
		setUrl(ServerUtil.URL_REQUEST_UPDATE_CONTACT_DETAILS);
		try {
			JSONObject temp = new JSONObject();
			temp.put(ServerUtil.UID, getUid());
			temp.put(ServerUtil.EMAIL, mail);
			temp.put(ServerUtil.BUSINESS_ADDRESS, businessAddress);
			temp.put(ServerUtil.BILLING_ADDRESS, billingAddress);
			temp.put(ServerUtil.AREA, areaCode);
			temp.put(ServerUtil.IS_ARRIVED_HOME, isArrivedHome);
			mMainJson = temp;
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public static class Builder {
		private String mail, businessAddress, billingAddress;
		private boolean isArrivedHome;
		private int areaCode;

		public UpdatePersonalContactOptionsTask build(Context ctx, HttpCallback callback) {
			UpdatePersonalContactOptionsTask task = new UpdatePersonalContactOptionsTask(ctx, callback, validate(mail),
					validate(businessAddress), validate(billingAddress), isArrivedHome, areaCode);
			return task;
		}

		private String validate(String string) {
			return TextUtils.isEmpty(string) ? "" : string;
		}

		public void addMail(String mail) {
			this.mail = mail;
		}

		public void addBusinessAddress(String businessAddress) {
			this.businessAddress = businessAddress;
		}

		public void addBillingAddress(String billingAddress) {
			this.billingAddress = billingAddress;
		}

		public void addArrivedHome(boolean isArrivedHome) {
			this.isArrivedHome = isArrivedHome;
		}

		public void addAreaCode(int areaCode) {
			this.areaCode = areaCode;
		}

	}

}
