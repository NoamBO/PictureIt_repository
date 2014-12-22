package com.pictureit.noambaroz.beauticianapp;

import android.content.Context;
import android.content.SharedPreferences;

public class Settings {

	public static String getUID(Context ctx) {
		SharedPreferences pref = ctx.getSharedPreferences(Constant.APP_PREFS_NAME, Context.MODE_PRIVATE);
		return pref.getString(Constant.PREFS_KEY_UID, "");
	}
	
	public static boolean isAvailable(Context ctx) {
		SharedPreferences pref = ctx.getSharedPreferences(Constant.APP_PREFS_NAME, Context.MODE_PRIVATE);
		return pref.getBoolean(Constant.PREFS_KEY_IS_AVAILABLE, false);
	}
}
