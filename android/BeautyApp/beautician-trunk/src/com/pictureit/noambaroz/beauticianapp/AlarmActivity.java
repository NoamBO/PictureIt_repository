package com.pictureit.noambaroz.beauticianapp;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.app.LoaderManager.LoaderCallbacks;
import android.app.NotificationManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
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

import com.pictureit.noambaroz.beauticianapp.UpcomingTreatmentsActivity.OnTreatmentCanceledListener;
import com.pictureit.noambaroz.beauticianapp.alarm.Alarm;
import com.pictureit.noambaroz.beauticianapp.alarm.AlarmManager;
import com.pictureit.noambaroz.beauticianapp.data.DataProvider;
import com.pictureit.noambaroz.beauticianapp.data.TimeUtils;
import com.pictureit.noambaroz.beauticianapp.data.UpcomingTreatment;
import com.pictureit.noambaroz.beauticianapp.dialog.BaseDialog;
import com.pictureit.noambaroz.beauticianapp.dialog.Dialogs;
import com.pictureit.noambaroz.beauticianapp.dialog.MySingleChoiseDialog;
import com.pictureit.noambaroz.beauticianapp.server.GetUpcomingTreatment;
import com.pictureit.noambaroz.beauticianapp.server.HttpBase.HttpCallback;
import com.pictureit.noambaroz.beauticianapp.server.ImageLoaderUtil;
import com.pictureit.noambaroz.beauticianapp.server.SetTreatmentStatusTask;
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
		mCurrentDialog = new TreatmentBegin(AlarmActivity.this, mArraylist.get(mArraylist.size() - 1));
		mCurrentDialog.setCanceledOnTouchOutside(false);
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
				a.address = cursor.getString(cursor.getColumnIndex(DataProvider.COL_ADDRESS));
				mArraylist.add(a);
			} while (cursor.moveToNext());
		checkStatusAndShowDialog();
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
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
				GetUpcomingTreatment task = new GetUpcomingTreatment(mContext, new HttpCallback() {

					@Override
					public void onAnswerReturn(Object answer) {
						if (answer == null || answer instanceof Integer)
							Dialogs.showServerFailedDialog(mContext);
						else {
							UpcomingTreatmentsActivity.FragmentUpcomingTreatment fragment = new UpcomingTreatmentsActivity.FragmentUpcomingTreatment();
							fragment.setUpcomingTreatment((UpcomingTreatment) answer);
							fragment.setOnTreatmentCanceledListener(onTreatmentCanceledListener);
							getFragmentManager().beginTransaction().replace(android.R.id.content, fragment)
									.addToBackStack(null).commit();
						}
					}
				}, String.valueOf(alarm.id));
				task.execute();
				break;

			default:
				break;
			}
		}

		private OnTreatmentCanceledListener onTreatmentCanceledListener = new OnTreatmentCanceledListener() {

			@Override
			public void onTreatmentCanceled(UpcomingTreatment treatment) {
				AlarmManager.getInstance().deleteAlarmFromTable(alarm.id);
				mArraylist.remove(alarm);
				checkStatusAndShowDialog();
			}
		};
	}

	private class TreatmentBegin extends BaseDialog implements android.view.View.OnClickListener {

		private Alarm alarm;
		private TextView date, name, address, treatment;
		private TextView bHappened, bNotHappened;

		public TreatmentBegin(Context context, Alarm alarm) {
			super(context);
			this.alarm = alarm;

			date = (TextView) mView.findViewById(R.id.tv_dialog_treatment_begin_date);
			name = (TextView) mView.findViewById(R.id.tv_dialog_treatment_begin_name);
			address = (TextView) mView.findViewById(R.id.tv_dialog_treatment_begin_address);
			treatment = (TextView) mView.findViewById(R.id.tv_dialog_treatment_begin_treatment_name);
			bHappened = (TextView) mView.findViewById(R.id.b_dialog_treatment_begin_happen);
			bNotHappened = (TextView) mView.findViewById(R.id.b_dialog_treatment_begin_not_happen);

			date.setText(TimeUtils.timestampToDateWithHour(String.valueOf(alarm.treatmentTime / 1000)));
			name.setText(alarm.customer_name);
			address.setText(alarm.address);
			treatment.setText(alarm.treatment);

			bHappened.setOnClickListener(this);
			bNotHappened.setOnClickListener(this);
		}

		@Override
		protected int getViewResourceId() {
			return R.layout.dialog_treatment_begin;
		}

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.b_dialog_treatment_begin_happen:
				SetTreatmentStatusTask httpRequest = new SetTreatmentStatusTask(mContext, callback, true, alarm.id, -1);
				httpRequest.execute();
				dismiss();
				break;

			case R.id.b_dialog_treatment_begin_not_happen:
				onCancelPressed();
				break;

			default:
				break;
			}
		}

		@Override
		public void onBackPressed() {
		}

		HttpCallback callback = new HttpCallback() {

			@Override
			public void onAnswerReturn(Object answer) {
				if (answer instanceof Integer)
					Dialogs.showServerFailedDialog(mContext);
				else {
					AlarmManager.getInstance().deleteAlarmFromTable(alarm.id);
					mArraylist.remove(alarm);
					checkStatusAndShowDialog();
				}
			}
		};

		private void onCancelPressed() {
			MySingleChoiseDialog d = new MySingleChoiseDialog(mContext, getResources().getStringArray(
					R.array.dialog_treatment_not_happend_reasons));
			d.setTitle(R.string.why_treatment_canceled);
			d.showButtons(new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					SetTreatmentStatusTask httpRequest = new SetTreatmentStatusTask(mContext, callback, false,
							alarm.id, which);
					httpRequest.execute();
				}
			}, new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					if (!mCurrentDialog.isShowing())
						mCurrentDialog.show();
				}
			});
			d.setOnCancelListener(new OnCancelListener() {

				@Override
				public void onCancel(DialogInterface dialog) {
					if (!mCurrentDialog.isShowing())
						mCurrentDialog.show();
				}
			});
			d.show();
		}

	}

}
