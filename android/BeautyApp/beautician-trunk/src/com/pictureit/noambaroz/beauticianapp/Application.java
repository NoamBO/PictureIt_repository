package com.pictureit.noambaroz.beauticianapp;

public class Application extends android.app.Application {

	@Override
	public void onCreate() {
		super.onCreate();
		MyPreference.initPreference(getApplicationContext());
	}
}
