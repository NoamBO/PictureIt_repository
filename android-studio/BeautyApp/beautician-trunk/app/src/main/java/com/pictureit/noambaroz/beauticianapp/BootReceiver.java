package com.pictureit.noambaroz.beauticianapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.pictureit.noambaroz.beauticianapp.alarm.AlarmManager;

public class BootReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent aIntent) {

		if (AlarmManager.getInstance() == null)
			AlarmManager.initAlarmManager(context);

		AlarmManager.getInstance().resetAlarms();
	}
}