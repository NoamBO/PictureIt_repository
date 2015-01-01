package com.pictureit.noambaroz.beauticianapp.alarm;

import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

import com.pictureit.noambaroz.beauticianapp.MyPreference;
import com.pictureit.noambaroz.beauticianapp.data.DataProvider;

public class AlarmManager {

	public static final String TREATMENT = "treatment";
	public static final String CUSTOMER_NAME = "name";
	public static final String IMAGE_URL = "image_url";
	public static final String ALARM_ID = "id";

	private static AlarmManager instance;

	private Context mContext;
	private android.app.AlarmManager mAlarmManager;

	public static void initAlarmManager(Context context) {
		if (instance == null)
			instance = new AlarmManager(context);
	}

	public static AlarmManager getInstance() {
		return instance;
	}

	public AlarmManager(Context context) {
		mContext = context.getApplicationContext();
		mAlarmManager = (android.app.AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
	}

	public void setAlarm(Alarm alarm) {
		Cursor cursor = mContext.getContentResolver().query(DataProvider.CONTENT_URI_ALARMS, null, null, null,
				DataProvider.SORT_ORDER_FIRST_TO_LAST);
		int id = cursor.getInt(cursor.getColumnIndex(DataProvider.COL_ID)) + 1;
		alarm.date = alarm.date - MyPreference.getPreTreatmentAlertTimeInMillis();
		alarm.id = id;
		addAlarm(alarm);
		ContentValues cv = new ContentValues(6);
		cv.put(DataProvider.COL_CUSTOMER_NAME, alarm.customer_name);
		cv.put(DataProvider.COL_DATE, alarm.date);
		cv.put(DataProvider.COL_IMAGE_URL, alarm.imageUrl);
		cv.put(DataProvider.COL_TREATMENT, alarm.treatment);
		cv.put(DataProvider.COL_ALARM_ID, alarm.id);
		cv.put(DataProvider.COL_IS_PLAYED, false);
		mContext.getContentResolver().insert(DataProvider.CONTENT_URI_ALARMS, cv);
	}

	public void resetAlarms() {
		Cursor cursor = mContext.getContentResolver().query(DataProvider.CONTENT_URI_ALARMS, null,
				DataProvider.COL_IS_PLAYED + " != ?", new String[] { "true" }, null);
		if (cursor.moveToFirst()) {
			do {
				Alarm alarm = new Alarm();
				alarm.date = cursor.getLong(cursor.getColumnIndex(DataProvider.COL_DATE));
				alarm.customer_name = cursor.getString(cursor.getColumnIndex(DataProvider.COL_CUSTOMER_NAME));
				alarm.id = cursor.getInt(cursor.getColumnIndex(DataProvider.COL_ALARM_ID));
				alarm.imageUrl = cursor.getString(cursor.getColumnIndex(DataProvider.COL_IMAGE_URL));
				alarm.treatment = cursor.getString(cursor.getColumnIndex(DataProvider.COL_TREATMENT));

				alarm.date = alarm.date - MyPreference.getPreTreatmentAlertTimeInMillis();
				if (alarm.date > 1000)
					updateAlarm(alarm);
			} while (cursor.moveToNext());
		}
	}

	private void updateAlarm(Alarm alarm) {
		removeAlarm(alarm.id);
		addAlarm(alarm);
		ContentValues cv = new ContentValues(1);
		cv.put(DataProvider.COL_DATE, alarm.date);
		mContext.getContentResolver().update(DataProvider.CONTENT_URI_ALARMS, cv, DataProvider.COL_ALARM_ID + " = ?",
				new String[] { String.valueOf(alarm.id) });
	}

	private void removeAlarm(int alarmId) {
		Intent intent = new Intent(mContext, AlarmReceiver.class);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, alarmId, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);
		mAlarmManager.cancel(pendingIntent);
	}

	private void addAlarm(Alarm alarm) {
		Intent intent = new Intent(mContext, AlarmReceiver.class);
		intent.putExtra(TREATMENT, alarm.treatment);
		intent.putExtra(CUSTOMER_NAME, alarm.customer_name);
		intent.putExtra(IMAGE_URL, alarm.imageUrl);
		intent.putExtra(ALARM_ID, alarm.id);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, alarm.id, intent,
				PendingIntent.FLAG_CANCEL_CURRENT);
		mAlarmManager.set(android.app.AlarmManager.RTC_WAKEUP, alarm.date, pendingIntent);
	}
}
