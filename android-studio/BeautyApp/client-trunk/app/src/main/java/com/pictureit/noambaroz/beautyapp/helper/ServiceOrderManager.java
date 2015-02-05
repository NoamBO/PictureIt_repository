package com.pictureit.noambaroz.beautyapp.helper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import net.simonvt.datepicker.DatePicker;
import net.simonvt.datepicker.DatePickerDialog;
import net.simonvt.numberpicker.NumberPicker;
import utilities.Dialogs;
import utilities.Log;
import utilities.server.HttpBase.HttpCallback;
import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.pictureit.noambaroz.beautyapp.Application;
import com.pictureit.noambaroz.beautyapp.FragmentTreatmentSelection;
import com.pictureit.noambaroz.beautyapp.MainActivity;
import com.pictureit.noambaroz.beautyapp.R;
import com.pictureit.noambaroz.beautyapp.ServiceOrder.OnFieldChangeListener;
import com.pictureit.noambaroz.beautyapp.customdialogs.MyCustomDialog;
import com.pictureit.noambaroz.beautyapp.customdialogs.MySingleChoiseDialog;
import com.pictureit.noambaroz.beautyapp.data.Constants;
import com.pictureit.noambaroz.beautyapp.data.TreatmentSummary;
import com.pictureit.noambaroz.beautyapp.data.TreatmentType;
import com.pictureit.noambaroz.beautyapp.location.MyLocation;
import com.pictureit.noambaroz.beautyapp.location.MyLocation.LocationResult;
import com.pictureit.noambaroz.beautyapp.server.PostCancelOrder;
import com.pictureit.noambaroz.beautyapp.server.PostOrderTreatment;
import com.pictureit.noambaroz.beautyapp.server.PostVerifyAddress;

public class ServiceOrderManager {

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
				mTreatment.treatments = tt;
				activityListener.onTreatmentSelected(tt);
			}
		});
		f.setTreatments(mTreatment.treatments);
		f.putTreatments(treatments);
		activity.getFragmentManager().beginTransaction().add(android.R.id.content, f).addToBackStack(null).commit();
	}

	public void showFORDialog(final OnFieldChangeListener onFieldChangeListener) {
		String title = activity.getString(R.string.dialog_title_for);
		String[] stringArray = activity.getResources().getStringArray(R.array.for_who_array);
		int checkedItem = 0;
		if (mTreatment.forwho != null) {
			if (mTreatment.forwho.equalsIgnoreCase(stringArray[0]))
				checkedItem = 0;
			else if (mTreatment.forwho.contains(stringArray[1]))
				checkedItem = 1;
			else
				checkedItem = 2;
		}
		OnItemSelectedListener l = new OnItemSelectedListener() {

			@Override
			public void onItemSelected(String selection) {
				mTreatment.forwho = selection;
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
							getEditableDialog(l, mTreatment.forwho, dFor, title).show();
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
		Dialog d = new Dialog(activity, R.style.Theme_DialodNoWindowTitle);
		LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.number_picker_dialog_layout, null);
		((TextView) view.findViewById(R.id.number_picker_dialog_title)).setText(title);
		TextView bOk = (TextView) view.findViewById(R.id.number_picker_dialog_ok);
		TextView bCancel = (TextView) view.findViewById(R.id.number_picker_dialog_cancel);
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
		d.setContentView(view);
		dGroup = d;
		Dialogs.setDialogWidth(dGroup);
		dGroup.show();
	}

	public void showLocationDialog(final OnFieldChangeListener onFieldChangeListener) {
		String title = activity.getString(R.string.dialog_title_location);
		String[] stringArray = activity.getResources().getStringArray(R.array.dialog_location_array);
		int checkedItem = 0;
		if (mTreatment.location != null) {
			if (mTreatment.location.equalsIgnoreCase(stringArray[0]))
				checkedItem = 0;
			else if (mTreatment.location.equalsIgnoreCase(stringArray[1]))
				checkedItem = 1;
			else
				checkedItem = 2;
		}
		OnItemSelectedListener l = new OnItemSelectedListener() {

			@Override
			public void onItemSelected(String selection) {
				mTreatment.location = selection;
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
				mTreatment.date = selection;
				onFieldChangeListener.onFieldChange(mTreatment.date);
			}
		};

        if(dDate == null)
		    dDate = new DatePickerFragment().setDate(mTreatment.date).setSelectionListener(l);
		dDate.show(activity.getFragmentManager(), "timePicker");
	}

	public void showRemarksDialog(final OnFieldChangeListener onFieldChangeListener) {
		MyCustomDialog dialog = new MyCustomDialog(activity);
		final EditText editText = dialog.getEditText();
		dialog.setDialogTitle(R.string.dialog_title_remarks);

		if (mTreatment.comments != null)
			editText.setText(mTreatment.comments);
		dialog.setPositiveButton(R.string.dialog_ok_text, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				mTreatment.comments = (editText.getText().toString());
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

	public void placeOrder(final String beauticianId) {

		final ProgressDialog pd = new ProgressDialog(activity);
		pd.setCanceledOnTouchOutside(false);
		pd.show();
		LocationResult lr = new LocationResult() {

			@Override
			public void gotLocation(Location location) {
				pd.cancel();
				if (location == null) {
					Dialogs.showErrorDialog(activity, R.string.dialog_messege_no_location);
					return;
				}
				try {
					PostOrderTreatment httpPost = new PostOrderTreatment(activity, placeOrderHttpCallback,
							mTreatment.forwho, mTreatment.date, mTreatment.comments, mTreatment.location,
							mTreatment.treatments).withLocation(location);
					if (TextUtils.isEmpty(beauticianId))
						httpPost.forAllBeauticians();
					else
						httpPost.forSpecificBeautician(beauticianId);
				} catch (Exception e) {
					Log.i("failed while building the request to the server");
					e.printStackTrace();
				}
			}
		};
		MyLocation l = new MyLocation();
		if (!l.getLocation(activity, lr)) {
			pd.cancel();
			Dialogs.showErrorDialog(activity, R.string.dialog_messege_no_location);
		}

	}

	public static void setPending(Context context, boolean isPending) {
		context.getSharedPreferences(Constants.APP_PREFS_NAME, Context.MODE_PRIVATE).edit()
				.putBoolean(Constants.PREFS_KEY_IS_APP_WAITING, isPending).commit();
	}

	public static boolean isPending(Context context) {
		return context.getSharedPreferences(Constants.APP_PREFS_NAME, Context.MODE_PRIVATE).getBoolean(
				Constants.PREFS_KEY_IS_APP_WAITING, false);
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

	public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

		private OnItemSelectedListener mListener;
		private boolean toReshow;

		private int mYear, mMonth, mDay;

        private String date;

		public DatePickerFragment() {}

        public DatePickerFragment setSelectionListener(OnItemSelectedListener l) {
            mListener = l;
            return this;
        }

        public DatePickerFragment setDate(String date) {
            this.date = date;
            return this;
        }

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			Calendar calendar = Calendar.getInstance();
			mYear = calendar.get(Calendar.YEAR);
			mMonth = calendar.get(Calendar.MONTH);
			mDay = calendar.get(Calendar.DAY_OF_MONTH);
			if (date != null) {
				SimpleDateFormat parser = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
				Date yourDate = null;
				try {
					yourDate = parser.parse(date);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				calendar.setTime(yourDate);
			}

            return new DatePickerDialog(getActivity(), this, calendar.get(Calendar.YEAR),
					calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));


		}

		@Override
		public void onDateSet(DatePicker view, int year, int month, int day) {
			if (mYear > year || (mYear == year && mMonth > month) || (mYear == year && mMonth == month && mDay > day)) {
				Toast.makeText(getActivity(), "invalid date", Toast.LENGTH_SHORT).show();
				toReshow = true;
				return;
			}

			String s = day +"/"+(month + 1)+"/"+year;

			mListener.onItemSelected(s);
		}

		@Override
		public void onDetach() {
			super.onDetach();
			if (toReshow) {
				toReshow = false;
				this.show(getActivity().getFragmentManager(), "timePicker");
			}
		}

	}
}
