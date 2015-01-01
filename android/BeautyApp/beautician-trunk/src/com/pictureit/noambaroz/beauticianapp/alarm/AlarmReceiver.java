package com.pictureit.noambaroz.beauticianapp.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.pictureit.noambaroz.beauticianapp.Log;

public class AlarmReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i("alarm receive!");
		Log.i("id: " + intent.getIntExtra(AlarmManager.ALARM_ID, -1));
		Log.i("treatment: " + intent.getStringExtra(AlarmManager.TREATMENT));
		Log.i("url: " + intent.getStringExtra(AlarmManager.IMAGE_URL));
		Log.i("name: " + intent.getStringExtra(AlarmManager.CUSTOMER_NAME));
	}

}
