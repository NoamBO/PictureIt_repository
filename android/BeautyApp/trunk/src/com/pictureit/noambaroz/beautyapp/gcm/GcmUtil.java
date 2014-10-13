package com.pictureit.noambaroz.beautyapp.gcm;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import utilities.Log;
import utilities.server.BaseHttpPost;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.pictureit.noambaroz.beautyapp.data.JsonToObject;
import com.pictureit.noambaroz.beautyapp.server.ServerUtil;

public class GcmUtil {

	public final String EXTRA_MESSAGE = "message";
	private final String PROPERTY_APP_VERSION = "appVersion";
	public final String PROPERTY_REG_ID = "gcm_registration_id";

	private final String SENDER_ID = "564652940367";

	private final String PREF_GCM_KEY = "com.pictureit.noambaroz.beautyapp.gcm.GcmUtil";

	private GoogleCloudMessaging gcm;

	private Context context;

	public static GcmUtil INSTANCE;

	public static GcmUtil get(Context context) {
		if (INSTANCE == null) {
			INSTANCE = new GcmUtil();
			INSTANCE.setContext(context);
		}
		return INSTANCE;
	}

	private void setContext(Context context) {
		this.context = context;
	}

	public void registerToGcm() {
		gcm = GoogleCloudMessaging.getInstance(context);
		String regid = getRegistrationId();

		if (regid.isEmpty()) {
			registerInBackground();
		}
	}

	/**
	 * Gets the current registration ID for application on GCM service.
	 * <p>
	 * If result is empty, the app needs to register.
	 * 
	 * @return registration ID, or empty string if there is no existing
	 *         registration ID.
	 */
	private String getRegistrationId() {
		final SharedPreferences prefs = getGCMPreferences();
		String registrationId = prefs.getString(PROPERTY_REG_ID, "");
		if (registrationId.isEmpty()) {
			Log.i("Registration not found.");
			return "";
		}
		// Check if app was updated; if so, it must clear the registration ID
		// since the existing regID is not guaranteed to work with the new
		// app version.
		int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
		int currentVersion = getAppVersion();
		if (registeredVersion != currentVersion) {
			Log.i("App version changed.");
			return "";
		}
		return registrationId;
	}

	/**
	 * @return Application's {@code SharedPreferences}.
	 */
	private SharedPreferences getGCMPreferences() {
		// This sample app persists the registration ID in shared preferences,
		// but
		// how you store the regID in your app is up to you.
		return context.getSharedPreferences(PREF_GCM_KEY, Context.MODE_PRIVATE);
	}

	/**
	 * @return Application's version code from the {@code PackageManager}.
	 */
	private int getAppVersion() {
		try {
			PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			return packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			// should never happen
			throw new RuntimeException("Could not get package name: " + e);
		}
	}

	/**
	 * Registers the application with GCM servers asynchronously.
	 * <p>
	 * Stores the registration ID and app versionCode in the application's
	 * shared preferences.
	 */
	private void registerInBackground() {
		new AsyncTask<Void, Void, String>() {
			@Override
			protected String doInBackground(Void... params) {
				String msg = "";
				try {
					if (gcm == null)
						gcm = GoogleCloudMessaging.getInstance(context);

					String regid = gcm.register(SENDER_ID);
					msg = "Device registered, registration ID=" + regid;

					// You should send the registration ID to your server over
					// HTTP,
					// so it can use GCM/HTTP or CCS to send messages to your
					// app.
					// The request to your server should be authenticated if
					// your app
					// is using accounts.

					sendRegistrationIdToBackend(regid);

				} catch (IOException ex) {
					msg = "Error :" + ex.getMessage();
					// If there is an error, don't just keep trying to register.
					// Require the user to click a button again, or perform
					// exponential back-off.
				}
				return msg;
			}

		}.execute(null, null, null);
	}

	/**
	 * Stores the registration ID and app versionCode in the application's
	 * {@code SharedPreferences}.
	 * 
	 * @param context
	 *            application's context.
	 * @param regId
	 *            registration ID
	 */
	private void storeRegistrationId(String regId) {
		final SharedPreferences prefs = getGCMPreferences();
		int appVersion = getAppVersion();
		Log.i("Saving regId on app version " + appVersion);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(PROPERTY_REG_ID, regId);
		editor.putInt(PROPERTY_APP_VERSION, appVersion);
		editor.commit();
	}

	private void sendRegistrationIdToBackend(String regid) {
		RegistrationIdSender sender = new RegistrationIdSender(context, regid);
		sender.execute();
	}

	private class RegistrationIdSender extends BaseHttpPost {

		String regId;

		@Override
		protected void onPostExecute(Object result) {
			storeRegistrationId(regId);
			super.onPostExecute(result);
		}

		public RegistrationIdSender(Context ctx, String regId) {
			super(ctx);
			this.regId = regId;
			prepare(null);
		}

		@Override
		protected Object continueInBackground(String result) {
			if (JsonToObject.isResponseOk(result)) {
				Log.i("GCM regId server response SUCCESS.");
				return regId;
			}
			Log.i("GCM regId server response FAILED.");
			return null;
		}

		@Override
		protected void prepare(String request) {
			setUrl(ServerUtil.URL_REQUEST_SEND_GCM_REG_ID);
			showProgressDialog = false;
			JSONObject jsonObject = new JSONObject();
			try {
				jsonObject.put(ServerUtil.UID, getUid());
				jsonObject.put(ServerUtil.GCM_REGISTRATION_ID, regId);
				jsonObject.put(ServerUtil.OS, "android");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			mMainJson = jsonObject;
		}

	}

}
