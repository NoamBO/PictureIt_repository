package com.pictureit.noambaroz.beautyapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Settings {

	public static final boolean DEBUG = true;

	public static int getRadius(Context context) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		int settingsDistance = prefs.getInt(context.getString(R.string.preference_key_settings_distance), 1000);
		return settingsDistance;
	}

}
