package com.pictureit.noambaroz.beauticianapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;

import com.pictureit.noambaroz.beautycianapp.R;

public class Splash extends Activity implements Runnable {

	private final int LOAD_DELAY_TIME = 1 * 1000;

	private Handler handler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		handler = new Handler();
		super.onCreate(savedInstanceState);

		ImageView imageView = new ImageView(Splash.this);
		imageView.setBackgroundResource(R.drawable.splash);
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		setContentView(imageView, params);
	}

	@Override
	protected void onResume() {
		super.onResume();
		load();
	}

	private void load() {
		handler.postDelayed(this, LOAD_DELAY_TIME);
	}

	@Override
	public void run() {
		// if (TextUtils.isEmpty(Settings.getUID(Splash.this)))
		// launchActivity(RegisterActivity.class);
		// else
		launchActivity(MainActivity.class);
	}

	private void launchActivity(Class<?> class1) {
		Intent intent = new Intent(Splash.this, class1);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
	}
}
