package com.pictureit.noambaroz.beauticianapp;

import java.util.ArrayList;

import android.app.Activity;
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

import com.pictureit.noambaroz.beauticianapp.ActivityUpcomingTreatments.OnTreatmentCanceledListener;
import com.pictureit.noambaroz.beauticianapp.alarm.Alarm;
import com.pictureit.noambaroz.beauticianapp.alarm.AlarmManager;
import com.pictureit.noambaroz.beauticianapp.data.BeauticianOfferResponse;
import com.pictureit.noambaroz.beauticianapp.data.DataProvider;
import com.pictureit.noambaroz.beauticianapp.data.DataUtils;
import com.pictureit.noambaroz.beauticianapp.data.TimeUtils;
import com.pictureit.noambaroz.beauticianapp.data.UpcomingTreatment;
import com.pictureit.noambaroz.beauticianapp.dialog.BaseDialog;
import com.pictureit.noambaroz.beauticianapp.dialog.Dialogs;
import com.pictureit.noambaroz.beauticianapp.dialog.MySingleChoiseDialog;
import com.pictureit.noambaroz.beauticianapp.dialog.TreatmentConfirmedDialog;
import com.pictureit.noambaroz.beauticianapp.server.GetUpcomingTreatment;
import com.pictureit.noambaroz.beauticianapp.server.HttpBase.HttpCallback;
import com.pictureit.noambaroz.beauticianapp.server.ImageLoaderUtil;
import com.pictureit.noambaroz.beauticianapp.server.SetTreatmentStatusTask;
import com.pictureit.noambaroz.beauticianapp.utilities.OutgoingCommunication;

public class ActivityNotificationsDialog extends Activity implements LoaderCallbacks<Cursor> {

	private final int ID_ALARMS_TABLE = 22;
	private final int ID_RESPONSE_CONFIRMED_TABLE = 23;
	private boolean mFinishInitLoaders = false;;

	private ArrayList<Alarm> mAlarmsArraylist;
	private ArrayList<BeauticianOfferResponse> mResponseConfirmedArraylist;

	private Dialog mCurrentDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View v = new View(ActivityNotificationsDialog.this);
		v.setBackgroundColor(Color.parseColor("#A6000000"));
		setContentView(v, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		lp.copyFrom(this.getWindow().getAttributes());
		lp.width = WindowManager.LayoutParams.MATCH_PARENT;
		lp.height = WindowManager.LayoutParams.MATCH_PARENT;
		this.getWindow().setAttributes(lp);

		NotificationManager n = (NotificationManager) ActivityNotificationsDialog.this
				.getSystemService(Context.NOTIFICATION_SERVICE);
		n.cancelAll();

		mAlarmsArraylist = new ArrayList<Alarm>();
		mResponseConfirmedArraylist = new ArrayList<BeauticianOfferResponse>();
		getLoaderManager().initLoader(ID_ALARMS_TABLE, null, this);
		getLoaderManager().initLoader(ID_RESPONSE_CONFIRMED_TABLE, null, this);
	}

	private void checkStatusAndShowDialog() {
		if (mAlarmsArraylist.size() == 0 && mResponseConfirmedArraylist.size() == 0) {
			MyPreference.setHasAlarmsDialogsToShow(false);
			finish();
		} else {
			if (mResponseConfirmedArraylist.size() > 0) {
				showTreatmentConfirmedDialog(mResponseConfirmedArraylist.get(mResponseConfirmedArraylist.size() - 1));
			} else if (mAlarmsArraylist.size() > 0) {
				boolean isPlayed = (System.currentTimeMillis() - mAlarmsArraylist.get(mAlarmsArraylist.size() - 1).treatment_time) > 0;
				if (isPlayed)
					showDialogTreatmentIsOver(mAlarmsArraylist.get(mAlarmsArraylist.size() - 1));
				else {
					if (mAlarmsArraylist.get(mAlarmsArraylist.size() - 1).isPlayed == 1) {
						mAlarmsArraylist.remove(mAlarmsArraylist.size() - 1);
						checkStatusAndShowDialog();
					} else
						showDialogAlert(mAlarmsArraylist.get(mAlarmsArraylist.size() - 1));
				}
			}
		}
	}

	private void showTreatmentConfirmedDialog(final BeauticianOfferResponse responseConfirmed) {
		mCurrentDialog = new TreatmentConfirmedDialog(ActivityNotificationsDialog.this).setCallButtonListener(
				new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						OutgoingCommunication.call(ActivityNotificationsDialog.this, responseConfirmed.phone_number);
					}
				}).setCloseButtonListener(null);
		mCurrentDialog.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface dialog) {
				DataUtils.get(getApplicationContext()).deleteTreatmentConfirmedRow(responseConfirmed.orderid);
				mResponseConfirmedArraylist.remove(responseConfirmed);
				// checkStatusAndShowDialog();
			}
		});
		mCurrentDialog.setCanceledOnTouchOutside(false);
		mCurrentDialog.show();
	}

	private void showDialogTreatmentIsOver(Alarm alarm) {
		mCurrentDialog = new TreatmentBegin(ActivityNotificationsDialog.this, alarm);
		mCurrentDialog.setCanceledOnTouchOutside(false);
		mCurrentDialog.show();
	}

	private void showDialogAlert(final Alarm alarm) {
		mCurrentDialog = new ReminderDialog(ActivityNotificationsDialog.this, alarm);
		mCurrentDialog.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface dialog) {
				AlarmManager.getInstance().setAlertReminderWasShown(alarm.upcomingtreatment_id);
				mAlarmsArraylist.remove(alarm);
				// checkStatusAndShowDialog();
			}
		});
		mCurrentDialog.show();
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		CursorLoader loader = null;
		if (id == ID_ALARMS_TABLE) {
			loader = new CursorLoader(ActivityNotificationsDialog.this, DataProvider.CONTENT_URI_ALARMS, null,
					DataProvider.COL_NEED_TO_SHOW_DIALOG + " != 0", null, null);
		} else if (id == ID_RESPONSE_CONFIRMED_TABLE) {
			loader = new CursorLoader(ActivityNotificationsDialog.this, DataProvider.CONTENT_CONFIRMED_TREATMENTS,
					null, null, null, null);
		}
		return loader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		if (mCurrentDialog != null) {
			mCurrentDialog.setOnDismissListener(null);
			mCurrentDialog.dismiss();
		}
		if (loader.getId() == ID_ALARMS_TABLE) {
			mAlarmsArraylist.clear();
			if (cursor.moveToFirst())
				do {
					Alarm a = new Alarm();
					a.treatment_time = cursor.getLong(cursor.getColumnIndex(DataProvider.COL_TREATMENT_TIME));
					a.full_name = cursor.getString(cursor.getColumnIndex(DataProvider.COL_CUSTOMER_NAME));
					a.upcomingtreatment_id = cursor.getInt(cursor.getColumnIndex(DataProvider.COL_TREATMENT_ID));
					a.image_url = cursor.getString(cursor.getColumnIndex(DataProvider.COL_IMAGE_URL));
					a.treatment = cursor.getString(cursor.getColumnIndex(DataProvider.COL_TREATMENT));
					a.address = cursor.getString(cursor.getColumnIndex(DataProvider.COL_ADDRESS));
					a.isPlayed = cursor.getInt(cursor.getColumnIndex(DataProvider.COL_IS_PLAYED));
					mAlarmsArraylist.add(a);
				} while (cursor.moveToNext());
		} else if (loader.getId() == ID_RESPONSE_CONFIRMED_TABLE) {
			mResponseConfirmedArraylist.clear();
			if (cursor.moveToFirst()) {
				do {
					BeauticianOfferResponse rc = new BeauticianOfferResponse();
					rc.orderid = cursor.getString(cursor.getColumnIndex(DataProvider.COL_TREATMENT_ID));
					rc.phone_number = cursor.getString(cursor.getColumnIndex(DataProvider.COL_CUSTOMER_TELEPHONE));
					mResponseConfirmedArraylist.add(rc);
				} while (cursor.moveToNext());
			}
		}
		if (mFinishInitLoaders)
			checkStatusAndShowDialog();
		else
			mFinishInitLoaders = true;
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

			date.setText(TimeUtils.timestampToDateWithHour(String.valueOf(alarm.treatment_time / 1000)));
			name.setText(alarm.full_name);
			treatment.setText(alarm.treatment);
			ImageLoaderUtil.display(alarm.image_url, image);
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
							ActivityUpcomingTreatments.FragmentUpcomingTreatment fragment = new ActivityUpcomingTreatments.FragmentUpcomingTreatment();
							fragment.setUpcomingTreatment((UpcomingTreatment) answer);
							fragment.setOnTreatmentCanceledListener(onTreatmentCanceledListener);
							getFragmentManager().beginTransaction().replace(android.R.id.content, fragment)
									.addToBackStack(null).commit();
						}
					}
				}, String.valueOf(alarm.upcomingtreatment_id));
				task.execute();
				break;

			default:
				break;
			}
		}

		private OnTreatmentCanceledListener onTreatmentCanceledListener = new OnTreatmentCanceledListener() {

			@Override
			public void onTreatmentCanceled(UpcomingTreatment treatment) {
				AlarmManager.getInstance().deleteAlarmFromTable(alarm.upcomingtreatment_id);
				mAlarmsArraylist.remove(alarm);
				// checkStatusAndShowDialog();
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

			date.setText(TimeUtils.timestampToDateWithHour(String.valueOf(alarm.treatment_time / 1000)));
			name.setText(alarm.full_name);
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
				SetTreatmentStatusTask httpRequest = new SetTreatmentStatusTask(mContext, callback, true, alarm.upcomingtreatment_id, -1);
				httpRequest.execute();
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
				if (answer instanceof Integer) {
					Dialogs.showServerFailedDialog(mContext);
					if (!mCurrentDialog.isShowing())
						mCurrentDialog.show();
				} else {
					AlarmManager.getInstance().deleteAlarmFromTable(alarm.upcomingtreatment_id);
					mAlarmsArraylist.remove(alarm);
					dismiss();
					// checkStatusAndShowDialog();
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
							alarm.upcomingtreatment_id, which);
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
