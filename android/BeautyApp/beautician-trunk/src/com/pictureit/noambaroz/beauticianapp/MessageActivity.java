package com.pictureit.noambaroz.beauticianapp;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import net.simonvt.numberpicker.NumberPicker;
import android.app.Dialog;
import android.app.Fragment;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pictureit.noambaroz.beauticianapp.FragmentTreatmentSelection.OnTreatmentListChangeListener;
import com.pictureit.noambaroz.beauticianapp.data.Message;
import com.pictureit.noambaroz.beauticianapp.data.MessageResponse;
import com.pictureit.noambaroz.beauticianapp.data.TimeUtils;
import com.pictureit.noambaroz.beauticianapp.data.TreatmentType;
import com.pictureit.noambaroz.beauticianapp.data.TreatmentsFormatter;
import com.pictureit.noambaroz.beauticianapp.dialog.Dialogs;
import com.pictureit.noambaroz.beauticianapp.dialog.MyCustomDialog;
import com.pictureit.noambaroz.beauticianapp.dialog.MySingleChoiseDialog;
import com.pictureit.noambaroz.beauticianapp.server.HttpBase.HttpCallback;
import com.pictureit.noambaroz.beauticianapp.server.ImageLoaderUtil;
import com.pictureit.noambaroz.beauticianapp.server.PostVerifyAddress;
import com.pictureit.noambaroz.beautycianapp.R;

public class MessageActivity extends ActivityWithFragment {

	private Message mMessage;

	@Override
	public void onBackPressed() {
		backPressed();
	}

	@Override
	protected void initActivity() {
		Intent data = getIntent();
		if (!data.hasExtra(Constant.EXTRA_MESSAGE_OBJECT)) {
			stopLoadingActivity();
		} else {
			mMessage = data.getParcelableExtra(Constant.EXTRA_MESSAGE_OBJECT);
			if (mMessage == null)
				stopLoadingActivity();
		}
	}

	private void stopLoadingActivity() {
		Dialogs.generalDialog(MessageActivity.this, Dialogs.somthing_went_wrong);
		finish();
		return;
	}

	@Override
	protected void setFragment() {
		fragment = new FragmentMessage();
	}

	@Override
	protected void setFragmentTag() {
		FRAGMENT_TAG = "single_message_fragment";
	}

	private class FragmentMessage extends Fragment implements HttpCallback, OnTimeSetListener,
			OnTreatmentListChangeListener {

		private MessageResponse mMessageResponse;

		private TextView tvPatient, tvHour, tvWantedTreatment1, tvWantedTreatment2, tvLocation, tvRemarks, tvPrice;
		private TextView tvSend, tvCancel;
		private TextView editLocation, editTreatments, editRemarks, editPrice;;

		private View priceDivider, timeDivider;

		private TimePickerDialog timePikerDialog;
		private Dialog priceDialog, customAddressDialog;
		private MySingleChoiseDialog locationDialog;

		private String mTreatmentsArrayInString;

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			mMessageResponse = new MessageResponse();
			mMessageResponse.setComments(mMessage.getComments());
			mMessageResponse.setOrderid(mMessage.getOrderid());
			mMessageResponse.setPlace(mMessage.getLocation());
			mTreatmentsArrayInString = new Gson().toJson(mMessage.getTreatments());
			mMessageResponse.setTreatments(mMessage.getTreatments());
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View v = inflater.inflate(R.layout.fragment_message, container, false);

			ImageView image = findView(v, R.id.iv_message);
			TextView tvName = findView(v, R.id.tv_message_name);
			TextView tvAddress = findView(v, R.id.tv_message_address);
			// tvPatient = findView(v, R.id.tv_message);
			TextView tvDate = findView(v, R.id.tv_message_when_date);
			tvHour = findView(v, R.id.b_message_when);
			tvWantedTreatment1 = findView(v, R.id.tv_message_treatment_list1);
			tvWantedTreatment2 = findView(v, R.id.tv_message_treatment_list2);
			tvLocation = findView(v, R.id.tv_message_location);
			tvRemarks = findView(v, R.id.tv_message_remarks);
			tvPrice = findView(v, R.id.tv_message_price);
			tvSend = findView(v, R.id.tv_message_confirm_button);
			tvCancel = findView(v, R.id.tv_message_cancel_button);
			priceDivider = findView(v, R.id.divider_messages_price);
			timeDivider = findView(v, R.id.divider_messages_when);

			editLocation = findView(v, R.id.b_message_location);
			editTreatments = findView(v, R.id.b_message_select_treatment);
			editRemarks = findView(v, R.id.b_message_remarks);
			editPrice = findView(v, R.id.b_message_set_price);

			tvName.setText(mMessage.getClientName());
			tvAddress.setText(mMessage.getClientAdress());
			tvDate.setText(TimeUtils.timestampToDate(mMessage.getDate()));
			ImageLoaderUtil.display(mMessage.getImageUrl(), image);

			TreatmentsFormatter.getSelf(getActivity()).setTreatmentsList(tvWantedTreatment1, tvWantedTreatment2,
					mMessage.getTreatments());
			tvLocation.setText(mMessage.getLocation());
			tvRemarks.setText(mMessage.getComments());
			return v;
		}

		@Override
		public void onAnswerReturn(Object answer) {
			// TODO
		}

		@Override
		public void onResume() {
			super.onResume();
			tvHour.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					showHourDialog();
				}
			});
			editPrice.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					showPriceDialog();
				}
			});
			editTreatments.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					FragmentTreatmentSelection fragment = new FragmentTreatmentSelection();
					fragment.setTreatments(mMessage.getTreatments());
					fragment.setListener(FragmentMessage.this);
					getActivity().getFragmentManager().beginTransaction().add(android.R.id.content, fragment)
							.addToBackStack(null).commit();
				}
			});
			editLocation.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					showLocationDialog();
				}
			});
		}

		@Override
		public void onTreatmentListChange(ArrayList<TreatmentType> treatments) {
			ArrayList<TreatmentType> arr = new ArrayList<TreatmentType>();
			for (TreatmentType tt : treatments) {
				if (tt.getAmount() > 0) {
					arr.add(tt);
				}
			}
			mMessageResponse.setTreatments(getReleventTreatmentList(arr));

			TreatmentsFormatter.getSelf(getActivity()).setTreatmentsList(tvWantedTreatment1, tvWantedTreatment2,
					getReleventTreatmentList(arr));
		}

		private ArrayList<TreatmentType> getReleventTreatmentList(ArrayList<TreatmentType> arr) {
			ArrayList<TreatmentType> array = new ArrayList<TreatmentType>();
			Type arrayType = new TypeToken<List<TreatmentType>>() {
			}.getType();
			ArrayList<TreatmentType> a = new Gson().fromJson(mTreatmentsArrayInString, arrayType);
			for (TreatmentType tt : arr) {
				if (tt.getAmount() > 0)
					for (int i = 0; i < a.size(); i++) {
						if (a.get(i).getTreatmentId().equalsIgnoreCase(tt.getTreatmentId()))
							array.add(a.get(i));
					}
			}
			return array;
		}

		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			String hour = (hourOfDay < 10 ? "0" + hourOfDay : String.valueOf(hourOfDay)) + ":"
					+ (minute < 10 ? "0" + minute : String.valueOf(minute));
			mMessageResponse.setHour(hour);
			timeDivider.setVisibility(View.VISIBLE);
			tvHour.getLayoutParams().width = LayoutParams.WRAP_CONTENT;
			tvHour.setBackground(null);
			tvHour.setText(hour);
		}

		private void onLocationSelected(String location) {
			tvLocation.setText(location);
			mMessageResponse.setPlace(location);
		}

		private void showHourDialog() {
			if (timePikerDialog == null) {
				timePikerDialog = new TimePickerDialog(getActivity(), this, 12, 0, true);
			}
			timePikerDialog.show();
		}

		private void showPriceDialog() {
			if (priceDialog == null) {
				Dialog d = new Dialog(getActivity(), R.style.AppTheme_Dialog_DialodNoWindowTitle);
				LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(
						Context.LAYOUT_INFLATER_SERVICE);
				View view = inflater.inflate(R.layout.number_picker_dialog_layout, null);
				TextView bOk = (TextView) view.findViewById(R.id.number_picker_dialog_ok);
				TextView bCancel = (TextView) view.findViewById(R.id.number_picker_dialog_cancel);
				final NumberPicker np = (NumberPicker) view.findViewById(R.id.number_picker_dialog_wheel);
				np.setMaxValue(250000);
				np.setMinValue(1);
				np.setValue(75);
				np.setWrapSelectorWheel(false);
				bCancel.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						priceDialog.dismiss();
					}
				});
				bOk.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						tvPrice.setText(String.valueOf(np.getValue() + " " + getString(R.string.currency)));
						priceDivider.setVisibility(View.VISIBLE);
						editPrice.getLayoutParams().width = LayoutParams.WRAP_CONTENT;
						editPrice.setText("");
						editPrice.setBackgroundResource(R.drawable.btn_edit);
						mMessageResponse.setPrice(String.valueOf(np.getValue()));
						priceDialog.dismiss();
					}
				});
				d.setContentView(view);
				priceDialog = d;
				Dialogs.setDialogWidth(priceDialog);
			}
			priceDialog.show();
		}

		private void showLocationDialog() {
			if (locationDialog == null) {
				locationDialog = new MySingleChoiseDialog(getActivity(), getResources().getStringArray(
						R.array.dialog_location_array));
				locationDialog.setMyTitle(R.string.address).setOnItemClickListener(
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								if (which == getResources().getStringArray(R.array.dialog_location_array).length - 1) {
									showAddressDialog();
								} else
									onLocationSelected(getResources().getStringArray(R.array.dialog_location_array)[which]);
								dialog.dismiss();
							}
						});
			}
			locationDialog.show();
		}

		private void showAddressDialog() {
			if (customAddressDialog == null) {
				MyCustomDialog dialogEdit = new MyCustomDialog(getActivity());
				final EditText editText = dialogEdit.getEditText();
				dialogEdit.setDialogTitle(R.string.address)
						.setPositiveButton(R.string.dialog_ok_text, new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								PostVerifyAddress httpPost = new PostVerifyAddress(getActivity(), new HttpCallback() {

									@Override
									public void onAnswerReturn(Object answer) {
										Log.i(answer.toString());
										if (answer != null)
											onLocationSelected((String) answer);
									}
								}, editText.getText().toString());
								httpPost.execute();
							}
						}).setNegativeButton(R.string.dialog_cancel_text, new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								showLocationDialog();
							}
						}).show();
			}
		}

	}

}
