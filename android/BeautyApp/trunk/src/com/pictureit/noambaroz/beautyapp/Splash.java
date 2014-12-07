package com.pictureit.noambaroz.beautyapp;

import utilities.Dialogs;
import utilities.server.HttpBase.HttpCallback;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.pictureit.noambaroz.beautyapp.data.Constant;
import com.pictureit.noambaroz.beautyapp.server.PostVerifyUser;

public class Splash extends Activity {

	private HttpCallback callback = new HttpCallback() {

		@Override
		public void onAnswerReturn(Object answer) {
			utilities.Log.i("answer = " + answer);
			if (answer != null) {
				String result = (String) answer;
				if (result.equalsIgnoreCase(PostVerifyUser.VERIFY_USER_RESULT_ACTIVE))
					launchActivity(MainActivity.class);
				else if (result.equalsIgnoreCase(PostVerifyUser.VERIFY_USER_RESULT_NOT_ACTIVE)) {
					launchActivity(ActivityRegistrationPersonalData.class);
				}
			} else {
				Dialogs.makeToastThatCloseActivity(Splash.this, R.string.dialog_messege_server_error);
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
		Log.i("uid",
				getSharedPreferences(Constant.APP_PREFS_NAME, Context.MODE_PRIVATE).getString(Constant.PREFS_KEY_UID,
						""));

		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				utilities.Log.i("Requesting user validation");
				PostVerifyUser httpPost = new PostVerifyUser(Splash.this, callback);
				httpPost.execute();
			}
		}, 1000);
	}
}
