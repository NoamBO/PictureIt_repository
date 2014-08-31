package com.pictureit.noambaroz.beautyapp;

import utilities.server.HttpBase.HttpCallback;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.pictureit.noambaroz.beautyapp.server.PostVerifyUser;

public class Splash extends Activity {

	private HttpCallback callback = new HttpCallback() {

		@Override
		public void onAnswerReturn(Object answer) {
			if (answer != null) {
				String result = (String) answer;
				if (result.equalsIgnoreCase(PostVerifyUser.VERIFY_USER_RESULT_ACTIVE))
					launchActivity(MainActivity.class);
				else if (result.equalsIgnoreCase(PostVerifyUser.VERIFY_USER_RESULT_NOT_ACTIVE)) {
					launchActivity(ActivityRegistrationPersonalData.class);
				}
			}
		}
	};

	private <T> void launchActivity(Class<T> T) {
		Intent intent = new Intent(Splash.this, T);
		startActivity(intent);
		overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
		finish();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_screen);
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				PostVerifyUser httpPost = new PostVerifyUser(Splash.this, callback);
				// TODO httpPost.execute()
			}
		}, 1000);
	}
}
