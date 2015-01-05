package com.pictureit.noambaroz.beauticianapp;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.LoaderManager.LoaderCallbacks;
import android.app.NotificationManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.pictureit.noambaroz.beauticianapp.alarm.Alarm;
import com.pictureit.noambaroz.beauticianapp.alarm.AlarmManager;
import com.pictureit.noambaroz.beauticianapp.data.DataProvider;
import com.pictureit.noambaroz.beauticianapp.data.TimeUtils;
import com.pictureit.noambaroz.beauticianapp.dialog.BaseDialog;
import com.pictureit.noambaroz.beauticianapp.server.ImageLoaderUtil;
import com.pictureit.noambaroz.beautycianapp.R;

public class AlarmActivity extends Activity implements LoaderCallbacks<Cursor> {

	private ArrayList<Alarm> mArraylist;

	private Dialog mCurrentDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View v = new View(AlarmActivity.this);
		v.setBackgroundColor(Color.parseColor("#A6000000"));
		setContentView(v, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		lp.copyFrom(this.getWindow().getAttributes());
		lp.width = WindowManager.LayoutParams.MATCH_PARENT;
		lp.height = WindowManager.LayoutParams.MATCH_PARENT;
		this.getWindow().setAttributes(lp);

		NotificationManager n = (NotificationManager) AlarmActivity.this.getSystemService(Context.NOTIFICATION_SERVICE);
		n.cancelAll();

		mArraylist = new ArrayList<Alarm>();
		getLoaderManager().initLoader(1, null, this);
	}

	private void checkStatusAndShowDialog() {
		if (mArraylist.size() == 0) {
			MyPreference.setHasAlarmsDialogsToShow(false);
			finish();
		} else {
			boolean isPlayed = (System.currentTimeMillis() - mArraylist.get(mArraylist.size() - 1).treatmentTime) > 0;
			if (isPlayed)
				showDialogTreatmentIsOver();
			else
				showDialogAlert();
		}
	}

	private void showDialogTreatmentIsOver() {
		AlertDialog.Builder b = new AlertDialog.Builder(AlarmActivity.this);
		b.setMessage(mArraylist.get(mArraylist.size() - 1).customer_name);
		b.setTitle(mArraylist.get(mArraylist.size() - 1).treatment);
		b.setPositiveButton("treatmentPass", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				AlarmManager.getInstance().deleteAlarmFromTable(mArraylist.get(mArraylist.size() - 1).id);
				mArraylist.remove(mArraylist.get(mArraylist.size() - 1));
				checkStatusAndShowDialog();
			}
		});
		mCurrentDialog = b.create();
		mCurrentDialog.show();
	}

	private void showDialogAlert() {
		mCurrentDialog = new ReminderDialog(AlarmActivity.this, mArraylist.get(mArraylist.size() - 1));
		mCurrentDialog.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface dialog) {
				mArraylist.remove(mArraylist.get(mArraylist.size() - 1));
				checkStatusAndShowDialog();
			}
		});
		mCurrentDialog.show();
	}

	private class ReminderDialog extends BaseDialog implements android.view.View.OnClickListener {

		private final Alarm alarm;

		public ReminderDialog(Context context, Alarm alarm) {
			super(context);
			this.alarm = alarm;

			mView.findViewById(R.id.relativeLayout1AlarmDialog).setOnClickListener(this);
			TextView date = (TextView) mView.findViewById(R.id.tv_row_dialog_reminder_date);
			TextView name = (TextView) mView.findViewById(R.id.tv_row_dialog_reminder_customer_name);
			TextView treatment = (TextView) mView.findViewById(R.id.tv_row_dialog_reminder_type_name);
			ImageView image = (ImageView) mView.findViewById(R.id.iv_row_dialog_reminder);

			date.setText(TimeUtils.timestampToDateWithHour(String.valueOf(alarm.treatmentTime / 1000)));
			name.setText(alarm.customer_name);
			treatment.setText(alarm.treatment);
			ImageLoaderUtil.display(alarm.imageUrl, image);
		}

		@Override
		protected int getViewResourceId() {
			return R.layout.dialog_alarm_reminder;
		}

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.relativeLayout1AlarmDialog:
				dismiss();
				break;

			default:
				break;
			}
		}

	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		CursorLoader loader = new CursorLoader(AlarmActivity.this, DataProvider.CONTENT_URI_ALARMS, null,
				DataProvider.COL_NEED_TO_SHOW_DIALOG + " != 0", null, null);
		return loader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		mArraylist.clear();
		if (mCurrentDialog != null) {
			mCurrentDialog.setOnDismissListener(null);
			mCurrentDialog.dismiss();
		}
		if (cursor.moveToFirst())
			do {
				Alarm a = new Alarm();
				a.treatmentTime = cursor.getLong(cursor.getColumnIndex(DataProvider.COL_TREATMENT_TIME));
				a.customer_name = cursor.getString(cursor.getColumnIndex(DataProvider.COL_CUSTOMER_NAME));
				a.id = cursor.getInt(cursor.getColumnIndex(DataProvider.COL_ORDER_ID));
				a.imageUrl = cursor.getString(cursor.getColumnIndex(DataProvider.COL_IMAGE_URL));
				a.treatment = cursor.getString(cursor.getColumnIndex(DataProvider.COL_TREATMENT));
				mArraylist.add(a);
			} while (cursor.moveToNext());
		checkStatusAndShowDialog();
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
	}
}
