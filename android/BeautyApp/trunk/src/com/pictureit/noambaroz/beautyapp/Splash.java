package com.pictureit.noambaroz.beautyapp;

import utilities.Log;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class Splash extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_screen);
		Log.i("test");
		StackTraceElement e[] = Thread.currentThread().getStackTrace();
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				Intent intent = new Intent(Splash.this, MainActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
				finish();
			}
		}, 1000);
	}
}
