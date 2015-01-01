package com.pictureit.noambaroz.beauticianapp.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.pictureit.noambaroz.beauticianapp.Log;

public class AlarmReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i("alarm receive!");
	}

}
