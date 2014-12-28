package com.pictureit.noambaroz.beauticianapp.gcm;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

import com.pictureit.noambaroz.beauticianapp.Log;

public class GcmBroadcastReceiver extends WakefulBroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i("GcmRecived");
		// Explicitly specify that GcmIntentService will handle the intent.
		ComponentName comp = new ComponentName(context.getPackageName(), GcmIntentService.class.getName());
		// Start the service, keeping the device awake while it is launching.
		startWakefulService(context, (intent.setComponent(comp)));
		// TODO setResultCode(Activity.RESULT_OK);
	}
}