package com.pictureit.noambaroz.beauticianapp;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pictureit.noambaroz.beauticianapp.alarm.AlarmManager;
import com.pictureit.noambaroz.beauticianapp.dialog.MyNumberPickerDialog;
import com.pictureit.noambaroz.beauticianapp.R;

public class ActivitySettings extends ActivityWithFragment {

	@Override
	protected void initActivity() {
	}

	@Override
	protected void setFragment() {
		fragment = new SettingsFragment();
	}

	@Override
	protected void setFragmentTag() {
		FRAGMENT_TAG = "settings_fragment";
	}

	@Override
	public void onBackPressed() {
		backPressed();
	}

	public static class SettingsFragment extends BaseFragment {

		private ViewGroup bEdit;
		private TextView tvPreAlertTime;
		private MyNumberPickerDialog mNumberPickerDialog;

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			mNumberPickerDialog = new MyNumberPickerDialog(getActivity());
			mNumberPickerDialog.setPositiveButtonListener(new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int value) {
					MyPreference.setPreTreatmentAlertTimeInMillis(value * 60 * 1000);
					tvPreAlertTime.setText(getPreTreatmentAlertTimeInString(getActivity()));
					AlarmManager.getInstance().resetAlarms();
				}
			});
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View v = inflater.inflate(R.layout.fragment_settings, container, false);
			bEdit = findView(v, R.id.ll_fragment_setting_pre_tretment_alesrt_time_edit);
			tvPreAlertTime = findView(v, R.id.tv_settings_alert_time);
			tvPreAlertTime.setText(getPreTreatmentAlertTimeInString(getActivity()));
			return v;
		}

		@Override
		public void onResume() {
			super.onResume();
			bEdit.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					onEditPressed();
				}
			});
		}

		private void onEditPressed() {
			int value = (int) (MyPreference.getPreTreatmentAlertTimeInMillis() / (60 * 1000));
			mNumberPickerDialog.setValue(value);
			mNumberPickerDialog.show();
		}

		public static String getPreTreatmentAlertTimeInString(Context ctx) {
			long millis = MyPreference.getPreTreatmentAlertTimeInMillis();
			String time = "";
			int minutes = (int) (millis / (60 * 1000));
			if (minutes > 59) {
				time = minutes / 60 + ":" + (minutes % 60 < 10 ? "0" + minutes % 60 : minutes % 60) + " "
						+ ctx.getString(R.string.hours);
			} else {
				time = minutes + " " + ctx.getString(R.string.minutes);
			}
			return time;
		}
	}

}
