package com.pictureit.noambaroz.beautyapp.helper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import utilities.Dialogs;
import utilities.Log;
import utilities.server.HttpBase.HttpCallback;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.pictureit.noambaroz.beautyapp.Application;
import com.pictureit.noambaroz.beautyapp.FragmentTreatmentSelection;
import com.pictureit.noambaroz.beautyapp.MainActivity;
import com.pictureit.noambaroz.beautyapp.R;
import com.pictureit.noambaroz.beautyapp.ServiceOrder.OnFieldChangeListener;
import com.pictureit.noambaroz.beautyapp.customdialogs.MyCustomDialog;
import com.pictureit.noambaroz.beautyapp.customdialogs.MySingleChoiseDialog;
import com.pictureit.noambaroz.beautyapp.data.Constant;
import com.pictureit.noambaroz.beautyapp.data.TreatmentSummary;
import com.pictureit.noambaroz.beautyapp.data.TreatmentType;
import com.pictureit.noambaroz.beautyapp.location.MyLocation;
import com.pictureit.noambaroz.beautyapp.location.MyLocation.LocationResult;
import com.pictureit.noambaroz.beautyapp.server.PostCancelOrder;
import com.pictureit.noambaroz.beautyapp.server.PostOrderTreatment;
import com.pictureit.noambaroz.beautyapp.server.PostVerifyAddress;

public class ServiceOrderManager {

	private boolean isTodaySelected;
	private Activity activity;
	private TreatmentSummary mTreatment;
	private Dialog dFor, dLocation, dGroup;
	private DatePickerFragment dDate;
	private HttpCallback placeOrderHttpCallback = new HttpCallback() {

		@Override
		public void onAnswerReturn(Object answer) {
			Log.i("finish");
			if (answer != null && !((String) answer).equalsIgnoreCase("")) {
				ServiceOrderManager.setPending(activity, true);
				Intent i = new Intent(activity, MainActivity.class);
				i.putExtra("exit", true);
				i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				activity.startActivity(i);
			}
		}
	};

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

	public interface OnOrderStatusChangeListener {
		public void onStatusChange(boolean isPending);
	}

	public void showTreatmentSelectionDialog(String[] treatments, final OnTreatmentsSelectedListener activityListener) {
		FragmentTreatmentSelection f = new FragmentTreatmentSelection();
		f.setListener(new OnTreatmentsSelectedListener() {

			@Override
			public void onTreatmentSelected(ArrayList<TreatmentType> tt) {
				mTreatment.tretments = tt;
				activityListener.onTreatmentSelected(tt);
			}
		});
		f.setTreatments(mTreatment.tretments);
		f.putTreatments(treatments);
		activity.getFragmentManager().beginTransaction().add(android.R.id.content, f).addToBackStack(null).commit();
	}

	public void showFORDialog(final OnFieldChangeListener onFieldChangeListener) {
		String title = activity.getString(R.string.dialog_title_for);
		String[] stringArray = activity.getResources().getStringArray(R.array.for_who_array);
		int checkedItem = 0;
		if (mTreatment.forWho != null) {
			if (mTreatment.forWho.equalsIgnoreCase(stringArray[0]))
				checkedItem = 0;
			else if (mTreatment.forWho.contains(stringArray[1]))
				checkedItem = 1;
			else
				checkedItem = 2;
		}
		OnItemSelectedListener l = new OnItemSelectedListener() {

			@Override
			public void onItemSelected(String selection) {
				mTreatment.forWho = selection;
				onFieldChangeListener.onFieldChange(selection);
			}
		};

		createDialogTypeFor(title, checkedItem, stringArray, l);
	}

	private void createDialogTypeFor(final String title, int checkedItem, final String[] selections,
			final OnItemSelectedListener l) {

		dFor = new MySingleChoiseDialog(activity, title, selections, checkedItem,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (which == selections.length - 1) {
							getEditableDialog(l, mTreatment.forWho, dFor, title).show();
						} else if (which == selections.length - 2) {
							createGroupDialog(l, selections[which]);
						} else
							l.onItemSelected(selections[which]);
						dFor.dismiss();
					}
				});
		dFor.show();
	}

	private void createGroupDialog(final OnItemSelectedListener l, String title) {
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.number_picker_dialog_layout, null);
		builder.setCustomTitle(Dialogs.getDialogTitleTextView(activity, title));
		Button bOk = (Button) view.findViewById(R.id.number_picker_dialog_ok);
		Button bCancel = (Button) view.findViewById(R.id.number_picker_dialog_cancel);
		final NumberPicker np = (NumberPicker) view.findViewById(R.id.number_picker_dialog_wheel);
		np.setMaxValue(20); // max value 20
		np.setMinValue(2); // min value 2
		np.setWrapSelectorWheel(false);
		bCancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dGroup.dismiss();
				dFor.show();
			}
		});
		bOk.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				l.onItemSelected(String.valueOf(activity.getString(R.string.for_group) + ": " + np.getValue()));
				dGroup.dismiss();
			}
		});
		builder.setView(view);
		dGroup = builder.create();
		dGroup.show();
	}

	public void showLocationDialog(final OnFieldChangeListener onFieldChangeListener) {
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
				onFieldChangeListener.onFieldChange(selection);
			}
		};
		createLocationDialog(title, checkedItem, stringArray, l);
	}

	private void createLocationDialog(final String title, int checkedItem, final String[] selections,
			final OnItemSelectedListener l) {
		dLocation = new MySingleChoiseDialog(activity, title, selections, checkedItem,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (which == selections.length - 1) {
							MyCustomDialog dialogEdit = new MyCustomDialog(activity);
							final EditText editText = dialogEdit.getEditText();
							dialogEdit.setDialogTitle(title)
									.setPositiveButton(R.string.dialog_ok_text, new OnClickListener() {

										@Override
										public void onClick(DialogInterface dialog, int which) {
											PostVerifyAddress httpPost = new PostVerifyAddress(activity,
													new HttpCallback() {

														@Override
														public void onAnswerReturn(Object answer) {
															Log.i(answer.toString());
															if (answer != null)
																l.onItemSelected(answer.toString());
														}
													}, editText.getText().toString());
											httpPost.execute();
										}
									}).setNegativeButton(R.string.dialog_cancel_text, new OnClickListener() {

										@Override
										public void onClick(DialogInterface dialog, int which) {
											dLocation.show();
										}
									}).show();
						} else
							l.onItemSelected(selections[which]);
						dLocation.dismiss();
					}
				});
		dLocation.show();
	}

	public void showWHENDialog(final OnFieldChangeListener onFieldChangeListener) {
		OnItemSelectedListener l = new OnItemSelectedListener() {

			@Override
			public void onItemSelected(String selection) {
				createTimeDialog(selection, onFieldChangeListener);
			}
		};

		dDate = new DatePickerFragment(l);
		dDate.show(activity.getFragmentManager(), "timePicker");
	}

	private boolean isTimeDialogCanceled;

	private void createTimeDialog(final String selection, final OnFieldChangeListener onFieldChangeListener) {
		final String tempTime = selection;
		final Calendar c = Calendar.getInstance();
		int currentHour = c.get(Calendar.HOUR_OF_DAY);
		int currentMinute = c.get(Calendar.MINUTE);
		TimePickerDialog dialog = new TimePickerDialog(activity, new TimePickerDialog.OnTimeSetListener() {

			private void updateDisplay(int hour, int minute) {
				String min = String.valueOf(minute);
				String hr = String.valueOf(hour);
				min = min.length() == 1 ? "0" + min : min;
				hr = hr.length() == 1 ? "0" + hr : hr;
				mTreatment.when = tempTime + " " + hr + ":" + min;
				onFieldChangeListener.onFieldChange(mTreatment.when);
			}

			@Override
			public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {
				if (isTimeDialogCanceled) {
					isTimeDialogCanceled = false;
					return;
				}
				if (isTodaySelected) {
					Calendar c = Calendar.getInstance();
					if (selectedHour < c.get(Calendar.HOUR_OF_DAY)
							|| (selectedHour == c.get(Calendar.HOUR_OF_DAY) && selectedMinute < c.get(Calendar.MINUTE))) {
						Toast.makeText(activity, "invalid time", Toast.LENGTH_SHORT).show();
						createTimeDialog(selection, onFieldChangeListener);
					} else {
						updateDisplay(selectedHour, selectedMinute);
					}
				} else {
					updateDisplay(selectedHour, selectedMinute);
				}
				isTimeDialogCanceled = true;
			}
		}, currentHour, currentMinute, false);
		dialog.show();
	}

	public void showRemarksDialog(final OnFieldChangeListener onFieldChangeListener) {
		MyCustomDialog dialog = new MyCustomDialog(activity);
		final EditText editText = dialog.getEditText();
		dialog.setDialogTitle(R.string.dialog_title_remarks);

		if (mTreatment.remarks != null)
			editText.setText(mTreatment.remarks);
		dialog.setPositiveButton(R.string.dialog_ok_text, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				mTreatment.remarks = (editText.getText().toString());
				onFieldChangeListener.onFieldChange(editText.getText().toString());
			}
		}).setNegativeButton(R.string.dialog_cancel_text, null).show();
	}

	private Dialog getEditableDialog(final OnItemSelectedListener l, String text, final Dialog d, String title) {
		MyCustomDialog dialog = new MyCustomDialog(activity);
		final EditText editText = dialog.getEditText();
		dialog.setDialogTitle(title).setNegativeButton(R.string.dialog_cancel_text, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				d.show();
			}
		}).setPositiveButton(R.string.dialog_ok_text, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				l.onItemSelected(editText.getText().toString());
			}
		});
		if (text != null)
			editText.setText(text);
		return dialog;
	}

	public TreatmentSummary getTreatment() {
		return mTreatment;
	}

	public void setTreatment(TreatmentSummary treatment) {
		mTreatment = treatment;
	}

	public void placeOrder(String beauticianId) {
		PostOrderTreatment httpPost = new PostOrderTreatment(activity, placeOrderHttpCallback, mTreatment.forWho,
				mTreatment.when, mTreatment.remarks, mTreatment.whare, mTreatment.tretments);
		if (beauticianId == null)
			placeOrderByLocation(httpPost);
		else
			placeOrderByBeauticianId(httpPost, beauticianId);
	}

	private void placeOrderByBeauticianId(PostOrderTreatment httpPost, String beauticianId) {
		try {
			httpPost.forSpecificBeautician(beauticianId);
		} catch (Exception e) {
			Log.i("failed while building the request to the server");
			e.printStackTrace();
		}
	}

	private void placeOrderByLocation(final PostOrderTreatment httpPost) {
		final ProgressDialog pd = new ProgressDialog(activity);
		pd.setCanceledOnTouchOutside(false);
		pd.show();
		LocationResult lr = new LocationResult() {

			@Override
			public void gotLocation(Location location) {
				pd.cancel();
				if (location == null) {
					Dialogs.generalDialog(activity, activity.getString(R.string.dialog_messege_no_location),
							activity.getString(R.string.dialog_title_error));
					return;
				}
				try {
					httpPost.byLocation(location);
				} catch (Exception e) {
					Log.i("failed while building the request to the server");
					e.printStackTrace();
				}
			}
		};
		new MyLocation().getLocation(activity, lr);
	}

	public static void setPending(Context context, boolean isPending) {
		context.getSharedPreferences(Constant.APP_PREFS_NAME, Context.MODE_PRIVATE).edit()
				.putBoolean(Constant.PREFS_KEY_IS_APP_WAITING, isPending).commit();
	}

	public static boolean isPending(Context context) {
		return context.getSharedPreferences(Constant.APP_PREFS_NAME, Context.MODE_PRIVATE).getBoolean(
				Constant.PREFS_KEY_IS_APP_WAITING, false);
	}

	public static void cancelRequest(final Context ctx, final OnOrderStatusChangeListener callback) {
		PostCancelOrder cancelInBackground = new PostCancelOrder(ctx, new HttpCallback() {

			@Override
			public void onAnswerReturn(Object answer) {
				if (answer == null)
					return;
				savePendingTreatmentId(ctx, "-1");
				setPending(ctx, false);
				callback.onStatusChange(false);
			}
		});
		cancelInBackground.execute();
	}

	public static void savePendingTreatmentId(Context ctx, String treatmentId) {
		PreferenceManager.getDefaultSharedPreferences(ctx).edit()
				.putString(Application.PENDING_TREATMENT_ID, treatmentId).commit();
	}

	public static String getPendingTreatmentId(Context ctx) {
		return PreferenceManager.getDefaultSharedPreferences(ctx).getString(Application.PENDING_TREATMENT_ID, "");
	}

	private class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

		private OnItemSelectedListener mListener;
		private boolean isPositiveButtonClicked;

		private int mYear, mMonth, mDay;

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
			mYear = calendar.get(Calendar.YEAR);
			mMonth = calendar.get(Calendar.MONTH);
			mDay = calendar.get(Calendar.DAY_OF_MONTH);

			DatePickerDialog dpd = new DatePickerDialog(getActivity(), this, mYear, mMonth, mDay);
			return dpd;
		}

		@Override
		public void onResume() {
			((AlertDialog) getDialog()).getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(
					new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							isPositiveButtonClicked = true;
							dismiss();
						}
					});
			super.onResume();
		}

		@Override
		public void onDateSet(DatePicker view, int year, int month, int day) {
			if (!isPositiveButtonClicked) {
				return;
			}
			isPositiveButtonClicked = false;
			if (mYear > year || (mYear == year && mMonth > month) || (mYear == year && mMonth == month && mDay > day)) {
				Toast.makeText(activity, "invalid date", Toast.LENGTH_SHORT).show();
				dDate.show(activity.getFragmentManager(), "timePicker");
				return;
			}
			if (mYear == year && mMonth == month && mDay == day)
				isTodaySelected = true;
			else
				isTodaySelected = false;
			StringBuilder sb = new StringBuilder();
			sb.append(day).append("/").append(month + 1).append("/").append(year);
			String selection = sb.toString();
			mListener.onItemSelected(selection);
		}

	}

}
