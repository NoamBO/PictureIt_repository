package com.pictureit.noambaroz.beautyapp.helper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TimePicker;

import com.pictureit.noambaroz.beautyapp.FragmentTreatmentSelection;
import com.pictureit.noambaroz.beautyapp.R;
import com.pictureit.noambaroz.beautyapp.ServiceOrder.OnFieldChangeListener;
import com.pictureit.noambaroz.beautyapp.data.TreatmentSummary;
import com.pictureit.noambaroz.beautyapp.data.TreatmentType;

public class ServiceOrderManager {

	private Activity activity;
	private TreatmentSummary mTreatment;
	private Dialog mDialog;

	private final int DIALOG_TYPE_FOR = 1;
	private final int DIALOG_TYPE_WHEN = 2;
	private final int DIALOG_TYPE_LOCATION = 3;

	public ServiceOrderManager(Activity activity) {
		this.activity = activity;
		mTreatment = new TreatmentSummary();
	}

	public interface OnItemSelectedListener {
		public void onItemSelected(String selection);
	}

	public interface OnTreatmentsSelectedListener {
		public void onTreatmentSelected(ArrayList<TreatmentType> treatmentTypes);
	}

	public void showTreatmentSelectionDialog(String[] treatments) {
		FragmentTreatmentSelection f = new FragmentTreatmentSelection();
		f.setListener(new OnTreatmentsSelectedListener() {

			@Override
			public void onTreatmentSelected(ArrayList<TreatmentType> tt) {
				mTreatment.tretments = tt;
			}
		});
		f.setTreatments(mTreatment.tretments);
		f.putTreatments(treatments);
		activity.getFragmentManager().beginTransaction().replace(R.id.fragment_container, f).addToBackStack(null)
				.commit();
	}

	public void showFORDialog(OnFieldChangeListener onFieldChangeListener) {
		String title = activity.getString(R.string.dialog_title_for);
		String[] stringArray = activity.getResources().getStringArray(R.array.for_who_array);
		int checkedItem = 0;
		if (mTreatment.forWho != null) {
			if (mTreatment.forWho.equalsIgnoreCase(stringArray[0]))
				checkedItem = 0;
			else if (mTreatment.forWho.equalsIgnoreCase(stringArray[1]))
				checkedItem = 1;
			else
				checkedItem = 2;
		}
		OnItemSelectedListener l = new OnItemSelectedListener() {

			@Override
			public void onItemSelected(String selection) {
				mTreatment.forWho = selection;
			}
		};

		showSingleChoice(title, checkedItem, stringArray, l, DIALOG_TYPE_FOR);
	}

	public void showLocationDialog() {
		String title = activity.getString(R.string.dialog_title_location);
		String[] stringArray = activity.getResources().getStringArray(R.array.dialog_location_array);
		int checkedItem = 0;
		if (mTreatment.whare != null) {
			if (mTreatment.whare.equalsIgnoreCase(stringArray[0]))
				checkedItem = 0;
			else if (mTreatment.whare.equalsIgnoreCase(stringArray[1]))
				checkedItem = 1;
			else
				checkedItem = 2;
		}
		OnItemSelectedListener l = new OnItemSelectedListener() {

			@Override
			public void onItemSelected(String selection) {
				mTreatment.whare = selection;
			}
		};
		showSingleChoice(title, checkedItem, stringArray, l, DIALOG_TYPE_LOCATION);
	}

	public void showWHENDialog() {
		String title = activity.getString(R.string.dialog_title_when);
		String[] stringArray = activity.getResources().getStringArray(R.array.dialog_when_array);
		int checkedItem = 0;
		if (mTreatment.when != null) {
			if (mTreatment.when.equalsIgnoreCase(stringArray[0]))
				checkedItem = 0;
			else
				checkedItem = 1;
		}
		OnItemSelectedListener l = new OnItemSelectedListener() {

			@Override
			public void onItemSelected(String selection) {
				final String tempTime = selection;
				final Calendar c = Calendar.getInstance();
				int currentHour = c.get(Calendar.HOUR_OF_DAY);
				int currentMinute = c.get(Calendar.MINUTE);
				TimePickerDialog tpd = new TimePickerDialog(activity, new TimePickerDialog.OnTimeSetListener() {

					@Override
					public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
						mTreatment.when = tempTime + " " + hourOfDay + ":" + minute;
					}
				}, currentHour, currentMinute, false);
				tpd.show();
			}
		};

		DialogFragment newFragment = new DatePickerFragment(l);
		newFragment.show(activity.getFragmentManager(), "timePicker");
	}

	public void showRemarksDialog() {
		AlertDialog.Builder b = new AlertDialog.Builder(activity);
		final EditText editText = new EditText(activity);
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		editText.setLayoutParams(lp);
		b.setView(editText);
		b.setTitle(R.string.dialog_title_remarks);
		if (mTreatment.remarks != null)
			editText.setText(mTreatment.remarks);
		b.setPositiveButton(R.string.dialog_ok_text, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				mTreatment.remarks = (editText.getText().toString());
			}
		}).setNegativeButton(R.string.dialog_cancel_text, null).create().show();

	}

	private void showSingleChoice(String title, int checkedItem, final String[] selections,
			final OnItemSelectedListener l, final int type) {
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setTitle(title);
		builder.setSingleChoiceItems(selections, checkedItem, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (which == selections.length - 1) {
					switch (type) {
					case DIALOG_TYPE_FOR:
						getEditableDialog(l, mTreatment.forWho).show();
						break;
					case DIALOG_TYPE_LOCATION:
						// TODO
						break;
					default:
						break;
					}
				} else
					l.onItemSelected(selections[which]);
				mDialog.dismiss();
			}
		});
		mDialog = builder.create();
		mDialog.show();
	}

	private Dialog getEditableDialog(final OnItemSelectedListener l, String text) {
		AlertDialog.Builder b = new AlertDialog.Builder(activity);
		final EditText editText = new EditText(activity);
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		editText.setLayoutParams(lp);
		b.setView(editText);
		if (text != null)
			editText.setText(text);
		return b.setPositiveButton(R.string.dialog_ok_text, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				l.onItemSelected(editText.getText().toString());
			}
		}).setNegativeButton(R.string.dialog_cancel_text, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				mDialog.show();
			}
		}).create();
	}

	public TreatmentSummary getTreatment() {
		return mTreatment;
	}

	public void setTreatment(TreatmentSummary treatment) {
		mTreatment = treatment;
	}

	private class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

		private OnItemSelectedListener mListener;

		public DatePickerFragment(OnItemSelectedListener l) {
			mListener = l;
		}

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			Calendar calendar = Calendar.getInstance();
			if (mTreatment.when != null) {
				SimpleDateFormat parser = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
				Date yourDate = null;
				try {
					yourDate = parser.parse(mTreatment.when);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				calendar.setTime(yourDate);
			}

			// Use the current date as the default date in the picker
			int year = calendar.get(Calendar.YEAR);
			int month = calendar.get(Calendar.MONTH);
			int day = calendar.get(Calendar.DAY_OF_MONTH);

			// Create a new instance of DatePickerDialog and return it
			return new DatePickerDialog(getActivity(), this, year, month, day);
		}

		public void onDateSet(DatePicker view, int year, int month, int day) {
			StringBuilder sb = new StringBuilder();
			sb.append(day).append("/").append(month + 1).append("/").append(year);
			String selection = sb.toString();
			mListener.onItemSelected(selection);
		}

	}

}
