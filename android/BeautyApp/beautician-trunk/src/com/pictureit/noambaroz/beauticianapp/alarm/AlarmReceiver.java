package com.pictureit.noambaroz.beauticianapp.alarm;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;

import com.pictureit.noambaroz.beauticianapp.ActivityAlarm;
import com.pictureit.noambaroz.beauticianapp.Log;
import com.pictureit.noambaroz.beauticianapp.MyPreference;
import com.pictureit.noambaroz.beauticianapp.ActivitySettings;
import com.pictureit.noambaroz.beauticianapp.R;

public class AlarmReceiver extends BroadcastReceiver {

	private NotificationManager mNotificationManager;
	private Context mContext;

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i("alarm receive!");
		mContext = context;

		Alarm alarm = intent.getParcelableExtra(AlarmManager.EXTRA_ALARM);
		MyPreference.setHasAlarmsDialogsToShow(true);

		boolean isPlayed = (System.currentTimeMillis() - alarm.treatmentTime) > 0;
		AlarmManager.getInstance().setRowToShowAnyDialogOnActivity(alarm.id);
		if (!isAppRunningInForeground()) {
			setNotificationVariables(alarm, isPlayed);
		} else {
			mContext.startActivity(new Intent(mContext, ActivityAlarm.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
		}

		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				AlarmManager.getInstance().resetAlarms();
			}
		}, 50);
	}

	private boolean isAppRunningInForeground() {

		ActivityManager activityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> services = activityManager.getRunningTasks(Integer.MAX_VALUE);
		boolean isActivityFound = false;

		if (services.get(0).topActivity.getPackageName().toString()
				.equalsIgnoreCase(mContext.getPackageName().toString())) {
			isActivityFound = true;
		}

		return isActivityFound;
	}

	private void setNotificationVariables(Alarm alarm, boolean isAlarmPlayed) {
		if (mNotificationManager == null)
			mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

		String message = "";
		String title = "";
		if (!isAlarmPlayed) {
			message = mContext.getString(R.string.treatment_of) + " " + alarm.customer_name + " "
					+ mContext.getString(R.string.start_soon);
			title = mContext.getString(R.string.treatment_start_within) + " "
					+ ActivitySettings.SettingsFragment.getPreTreatmentAlertTimeInString(mContext);
		} else {
			message = mContext.getString(R.string.enter_to_set_treatment_status);
			title = mContext.getString(R.string.treatment_just_started);
		}
		buildNotification(alarm, title, message);
	}

	private void buildNotification(Alarm alarm, String title, String message) {
		Intent notificationIntent = new Intent(mContext, ActivityAlarm.class);
		notificationIntent.putExtra(AlarmManager.EXTRA_ALARM, alarm);
		notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

		PendingIntent contentIntent = PendingIntent.getActivity(mContext, 0, notificationIntent,
				PendingIntent.FLAG_ONE_SHOT);

		Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

		NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext)
				.setSmallIcon(R.drawable.ic_notification).setContentTitle(title)
				.setStyle(new NotificationCompat.BigTextStyle().bigText(message)).setContentText(message)
				.setAutoCancel(true).setSound(uri);

		builder.setContentIntent(contentIntent);
		fireNotification(alarm.id, builder.build());
	}

	private void fireNotification(int notificationID, Notification notification) {
		mNotificationManager.notify(notificationID, notification);
		Log.i("alarm notification fired!");
	}

}
