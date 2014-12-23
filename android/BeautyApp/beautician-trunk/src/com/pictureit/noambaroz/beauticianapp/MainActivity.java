package com.pictureit.noambaroz.beauticianapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.InflateException;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.pictureit.noambaroz.beautycianapp.R;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	protected void onResume() {
		super.onResume();
		ActivityWithFragment.addViewToTopOfActionBar(MainActivity.this);
		getActionBar().setTitle("");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			setDropdownTextViewFont();
		}

		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();

		return super.onOptionsItemSelected(item);
	}

	/**
	 * work on API Level 11+
	 */
	private void setDropdownTextViewFont() {
		getLayoutInflater().setFactory(new LayoutInflater.Factory() {
			@Override
			public View onCreateView(String name, Context context, AttributeSet attrs) {
				if (name.equalsIgnoreCase("com.android.internal.view.menu.IconMenuItemView")
						|| name.equalsIgnoreCase("TextView")) {
					try {
						LayoutInflater li = LayoutInflater.from(context);
						final View view = li.createView(name, null, attrs);
						new Handler().post(new Runnable() {
							public void run() {
								Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/FbExtrim-Regular.ttf");
								((TextView) view).setTypeface(tf, Typeface.NORMAL);
							}
						});
						return view;
					} catch (InflateException e) {
						// Handle any inflation exception here
					} catch (ClassNotFoundException e) {
						// Handle any ClassNotFoundException here
					}
				}
				return null;
			}

		});
	}

	private boolean isOkToFinishApp;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		final int LENGTH_SHORT = 2000; // 2 seconds
		if (keyCode != KeyEvent.KEYCODE_BACK)
			return super.onKeyDown(keyCode, event);
		if (getFragmentManager().getBackStackEntryCount() == 0) {
			if (isOkToFinishApp)
				this.finish();
			else {
				Toast.makeText(getApplicationContext(), R.string.press_again_to_exit, Toast.LENGTH_SHORT).show();
				isOkToFinishApp = true;
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						isOkToFinishApp = false;
					}
				}, LENGTH_SHORT);
			}
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void launchActivity(Class<?> class1) {
		Intent intent = new Intent(MainActivity.this, class1);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
	}
}
