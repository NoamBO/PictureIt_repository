package com.pictureit.noambaroz.beauticianapp.alarm;

import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

import com.pictureit.noambaroz.beauticianapp.Log;
import com.pictureit.noambaroz.beauticianapp.MyPreference;
import com.pictureit.noambaroz.beauticianapp.data.DataProvider;

public class AlarmManager {

	public static final int ALARM_ID = 84756;

	public static final String EXTRA_ALARM = "com.pictureit.noambaroz.beauticianapp.alarm";

	public static final String PREFS_HAS_ALARMS = "has_alarm_dialogs_toshow";

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
		ContentValues cv = new ContentValues(7);
		cv.put(DataProvider.COL_CUSTOMER_NAME, alarm.getFullName());
		cv.put(DataProvider.COL_TREATMENT_TIME, alarm.getTreatmentDate());
		cv.put(DataProvider.COL_IMAGE_URL, alarm.getImageUrl());
		cv.put(DataProvider.COL_TREATMENT, alarm.getTreatment());
		cv.put(DataProvider.COL_TREATMENT_ID, alarm.getUpcomingtreatment_id());
		cv.put(DataProvider.COL_ADDRESS, alarm.getAddress());
		cv.put(DataProvider.COL_NEED_TO_SHOW_DIALOG, 0);
		mContext.getContentResolver().insert(DataProvider.CONTENT_URI_ALARMS, cv);
		Log.i("alarm", "new alarm set with id = " + alarm.getUpcomingtreatment_id());
		resetAlarms();
	}

	public void resetAlarms() {
		Cursor cursor = mContext.getContentResolver().query(DataProvider.CONTENT_URI_ALARMS, null, null, null, null);
		Alarm alarm = new Alarm();
		long alertTime = 99999999999999999l;
		if (cursor.moveToFirst()) {
			do {
				long treatmentTime = cursor.getLong(cursor.getColumnIndex(DataProvider.COL_TREATMENT_TIME));
				long preTreatmentAlertTime = MyPreference.getPreTreatmentAlertTimeInMillis();
				long currentTime = System.currentTimeMillis();
				long l = treatmentTime - preTreatmentAlertTime;
				if (treatmentTime < currentTime) {
					continue;
				}
				if (currentTime < l) {
					if (alertTime < l)
						continue;
					else
						alertTime = l;
				} else {
					if (alertTime < treatmentTime)
						continue;
					else
						alertTime = treatmentTime;
				}
				Log.i("alarm", "new alarm set - treatmentTime: " + treatmentTime);
				Log.i("alarm", "new alarm set - alertTime    : " + alertTime);
				Log.i("alarm", "new alarm set - currentTime  : " + currentTime);
				alarm.setTreatmentDate(cursor.getLong(cursor.getColumnIndex(DataProvider.COL_TREATMENT_TIME)));
				alarm.setFullName(cursor.getString(cursor.getColumnIndex(DataProvider.COL_CUSTOMER_NAME)));
				alarm.setUpcomingtreatment_id(cursor.getInt(cursor.getColumnIndex(DataProvider.COL_TREATMENT_ID)));
				alarm.setImageUrl(cursor.getString(cursor.getColumnIndex(DataProvider.COL_IMAGE_URL)));
				alarm.setTreatment(cursor.getString(cursor.getColumnIndex(DataProvider.COL_TREATMENT)));
				alarm.setAddress(cursor.getString(cursor.getColumnIndex(DataProvider.COL_ADDRESS)));
			} while (cursor.moveToNext());
			updateAlarmTime(alarm, alertTime);
		}
	}

	private void updateAlarmTime(Alarm alarm, long alarmTime) {
		addAlarm(alarm, alarmTime);
		Log.i("alarm", "alarm updated");
	}

	public void deleteAlarmFromTable(int orderID) {
		mContext.getContentResolver().delete(DataProvider.CONTENT_URI_ALARMS, DataProvider.COL_TREATMENT_ID + " = ?",
				new String[] { String.valueOf(orderID) });
		resetAlarms();
		Log.i("alarm", "alarm deleted");
	}

	private void addAlarm(Alarm alarm, long alarmTime) {
		Intent intent = new Intent(mContext, AlarmReceiver.class);
		intent.putExtra(EXTRA_ALARM, alarm);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, ALARM_ID, intent,
				PendingIntent.FLAG_CANCEL_CURRENT);
		mAlarmManager.set(android.app.AlarmManager.RTC_WAKEUP, alarmTime, pendingIntent);
	}

	public void setRowToShowAnyDialogOnActivity(int orderID) {
		ContentValues cv = new ContentValues(1);
		cv.put(DataProvider.COL_NEED_TO_SHOW_DIALOG, 1);
		mContext.getContentResolver().update(DataProvider.CONTENT_URI_ALARMS, cv,
				DataProvider.COL_TREATMENT_ID + " = ?", new String[] { String.valueOf(orderID) });
		Log.i("alarm", "alarm set to show dialog");
	}

	public void setAlertReminderWasShown(int orderID) {
		ContentValues cv = new ContentValues(1);
		cv.put(DataProvider.COL_IS_PLAYED, 1);
		mContext.getContentResolver().update(DataProvider.CONTENT_URI_ALARMS, cv,
				DataProvider.COL_TREATMENT_ID + " LIKE ?", new String[] { String.valueOf(orderID) });
	}

}