package com.pictureit.noambaroz.beauticianapp;

import android.content.Context;
import android.content.SharedPreferences;

public class MyPreference {

	private static final String STATE_REQUESTING_LOCATION = "requesting_location";

	private static final String APP_PREFS_NAME = "com.pictureit.beautyapp.prefs";

	private static final String PREFS_KEY_UID = "uid";

	private static final String PREFS_KEY_IS_AVAILABLE = "is_beautician_available";

	private static final String PREFS_KEY_PRE_TREATMENT_TIME_TO_ALERT = "alert_pre_treatment_time";

	private static SharedPreferences mPrefs;

	public static String getUID() {
		return mPrefs.getString(PREFS_KEY_UID, "");
	}

	public static boolean isAvailable() {
		return mPrefs.getBoolean(PREFS_KEY_IS_AVAILABLE, true);
	}

	public static void setAvailability(boolean isAvailable) {
		SharedPreferences.Editor editor = mPrefs.edit();
		editor.putBoolean(PREFS_KEY_IS_AVAILABLE, isAvailable);
		editor.commit();
	}

	public static boolean isLocationServiceOn() {
		return mPrefs.getBoolean(STATE_REQUESTING_LOCATION, false);
	}

	public static void setLocationServiceState(boolean isOn) {
		SharedPreferences.Editor editor = mPrefs.edit();
		editor.putBoolean(STATE_REQUESTING_LOCATION, isOn);
		editor.commit();
	}

	public static long getPreTreatmentAlertTimeInMillis() {
		return mPrefs.getLong(PREFS_KEY_PRE_TREATMENT_TIME_TO_ALERT, 15 * 60 * 1000);
	}

	public static void setPreTreatmentAlertTimeInMillis(long timeInMillis) {
		SharedPreferences.Editor editor = mPrefs.edit();
		editor.putLong(PREFS_KEY_PRE_TREATMENT_TIME_TO_ALERT, timeInMillis);
		editor.commit();
	}

	public static void initPreference(Context context) {
		mPrefs = context.getSharedPreferences(APP_PREFS_NAME, Context.MODE_PRIVATE);
		setAvailability(true);
		setLocationServiceState(false);

	}
}
