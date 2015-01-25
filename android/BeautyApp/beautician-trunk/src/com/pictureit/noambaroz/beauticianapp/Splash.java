package com.pictureit.noambaroz.beauticianapp;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;

import com.pictureit.noambaroz.beauticianapp.alarm.AlarmManager;
import com.pictureit.noambaroz.beauticianapp.data.DataProvider;
import com.pictureit.noambaroz.beauticianapp.data.Message;
import com.pictureit.noambaroz.beauticianapp.server.GetMessages;
import com.pictureit.noambaroz.beauticianapp.server.HttpBase.HttpCallback;

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
		if (!TextUtils.isEmpty(MyPreference.getUID())) {
			checkMessagesAndOrderAroundMe();
			AlarmManager.getInstance().syncAlarmsInBackground();
		}
	}

	private void checkMessagesAndOrderAroundMe() {
		new GetMessages(Splash.this, new HttpCallback() {

			@Override
			public void onAnswerReturn(Object answer) {
				if (answer instanceof Integer) {
					return;
				}

				ArrayList<Message> arrayList = (ArrayList<Message>) answer;
				if (arrayList.size() == 0) {
					getContentResolver().delete(DataProvider.CONTENT_URI_ORDERS_AROUND_ME, null, null);
					return;
				} else {
					Cursor c = getContentResolver().query(DataProvider.CONTENT_URI_ORDERS_AROUND_ME, null, null, null,
							null);
					if (c.moveToFirst())
						do {
							for (Message m : arrayList) {
								String rowOrderId = c.getString(c.getColumnIndex(DataProvider.COL_ORDER_ID));
								if (rowOrderId.equalsIgnoreCase(m.getOrderid())) {
									break;
								}
								getContentResolver().delete(DataProvider.CONTENT_URI_ORDERS_AROUND_ME,
										DataProvider.COL_ORDER_ID + " = ?", new String[] { rowOrderId });
							}
						} while (c.moveToNext());
				}
			}
		}).setIsWithNoConnectionDialog(false).setIsWithProgressDialog(false).execute();

	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.i("uid", MyPreference.getUID());
		load();
	}

	private void load() {
		handler.postDelayed(this, LOAD_DELAY_TIME);
	}

	@Override
	public void run() {
		if (TextUtils.isEmpty(MyPreference.getUID())) {
			launchActivity(ActivityRegister.class);
		} else
			launchActivity(MainActivity.class);
	}

	private void launchActivity(Class<?> class1) {
		Intent intent = new Intent(Splash.this, class1);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
	}
}
