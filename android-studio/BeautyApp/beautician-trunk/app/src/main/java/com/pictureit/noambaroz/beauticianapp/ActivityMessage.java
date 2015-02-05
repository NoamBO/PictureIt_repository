package com.pictureit.noambaroz.beauticianapp;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import net.simonvt.numberpicker.NumberPicker;
import noam.baroz.timepicker.OnTimeSetListener;
import noam.baroz.timepicker.TimePicker;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pictureit.noambaroz.beauticianapp.FragmentTreatmentSelection.OnTreatmentListChangeListener;
import com.pictureit.noambaroz.beauticianapp.data.DataUtils;
import com.pictureit.noambaroz.beauticianapp.data.Formatter;
import com.pictureit.noambaroz.beauticianapp.data.Message;
import com.pictureit.noambaroz.beauticianapp.data.MessageResponse;
import com.pictureit.noambaroz.beauticianapp.data.TimeUtils;
import com.pictureit.noambaroz.beauticianapp.data.TreatmentType;
import com.pictureit.noambaroz.beauticianapp.dialog.Dialogs;
import com.pictureit.noambaroz.beauticianapp.dialog.MyCustomDialog;
import com.pictureit.noambaroz.beauticianapp.dialog.MySingleChoiseDialog;
import com.pictureit.noambaroz.beauticianapp.dialog.TimePickerDialog;
import com.pictureit.noambaroz.beauticianapp.server.BeauticianResponseTask;
import com.pictureit.noambaroz.beauticianapp.server.CancelRequestTask;
import com.pictureit.noambaroz.beauticianapp.server.HttpBase.HttpCallback;
import com.pictureit.noambaroz.beauticianapp.server.ImageLoaderUtil;
import com.pictureit.noambaroz.beauticianapp.server.PostVerifyAddress;
import com.pictureit.noambaroz.beauticianapp.utilities.MyBackPressedListener;

public class ActivityMessage extends ActivityWithFragment {

	private Message mMessage;

	private MyBackPressedListener mBackPressedListener;

	@Override
	public void onBackPressed() {
		if (mBackPressedListener == null || mBackPressedListener.onBackPressed())
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
		Dialogs.generalDialog(ActivityMessage.this, Dialogs.somthing_went_wrong);
		finish();
	}

    public void setBackPressedListener(MyBackPressedListener l) {
        mBackPressedListener = l;
    }

	@Override
	protected void setFragment() {
        fragment = new FragmentMessage();
        ((FragmentMessage) fragment).setMessage(mMessage);
	}

	@Override
	protected void setFragmentTag() {
		FRAGMENT_TAG = "single_message_fragment";
	}

	public static class FragmentMessage extends BaseFragment implements OnTimeSetListener, OnTreatmentListChangeListener,
			HttpCallback {

        private Message message;
		private MessageResponse messageResponse;

		private TextView tvHour, tvWantedTreatment1, tvWantedTreatment2, tvLocation, tvRemarks, tvPrice;
		private TextView tvSend, tvCancel;
		private TextView editLocation, editTreatments, editRemarks, editPrice;

		private View priceDivider, timeDivider;

		private TimePickerDialog timePikerDialog;
		private Dialog priceDialog;
		private MyCustomDialog customAddressDialog, remarksDialog;
		private MySingleChoiseDialog locationDialog;

		private String mTreatmentsArrayInString;

        public void setMessage(Message m) {
            message = m;
        }

		private BroadcastReceiver mReceiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				if (intent.hasExtra(Constant.EXTRA_ORDER_ID)) {
					String orderId = intent.getStringExtra(Constant.EXTRA_ORDER_ID);
					if (!TextUtils.isEmpty(orderId)) {
						if (orderId.equalsIgnoreCase(message.getOrderid())) {
							MyCustomDialog d = new MyCustomDialog(getActivity());
							d.setMessage(R.string.message_deleted);
							d.setPositiveButton(R.string.dialog_ok_text, new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog, int which) {
									dialog.dismiss();
									getActivity().finish();
								}
							});
							d.setCanceledOnTouchOutside(false);
							d.show();
						}
					}
				}
			}
		};

		@Override
		public void onDestroy() {
            getActivity().unregisterReceiver(mReceiver);
			super.onDestroy();
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            messageResponse = new MessageResponse();
            messageResponse.setComments("");
            messageResponse.setOrderid(message.getOrderid());
            messageResponse.setPlace(message.getLocation());
            mTreatmentsArrayInString = new Gson().toJson(message.getTreatments());
            messageResponse.setTreatments(message.getTreatments());
            messageResponse.setDate(TimeUtils.timestampToDate(message.getDate()));
            getActivity().registerReceiver(mReceiver, new IntentFilter(Constant.INTENT_FILTER_MESSAGE_DELETED));

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

			tvName.setText(message.getClientName());
			tvAddress.setText(message.getClientAdress());
			tvDate.setText(TimeUtils.timestampToDate(message.getDate()));
			{
				int w = (int) getResources().getDimension(R.dimen.beautician_picture_width);
				int h = (int) getResources().getDimension(R.dimen.beautician_picture_height);
				ImageLoaderUtil.display(message.getImageUrl(), image, -1, w, h);
			}
			Formatter.getSelf(getActivity()).setTreatmentsList(tvWantedTreatment1, tvWantedTreatment2,
					message.getTreatments());
			tvLocation.setText(message.getLocation());
			tvRemarks.setText(message.getComments());
			return v;
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
					MyBackPressedListener backPressedListener = (MyBackPressedListener) fragment;
                    ((ActivityMessage)getActivity()).setBackPressedListener(backPressedListener);
                    fragment.setTreatments(message.getTreatments());
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
			editRemarks.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					showRemarksDialog();
				}
			});
			tvSend.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (isMessageResponseOk()) {
						BeauticianResponseTask httpRequest = new BeauticianResponseTask(getActivity(),
								FragmentMessage.this, messageResponse);
						httpRequest.execute();
					} else {
						// TODO
					}
				}
			});
			tvCancel.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					CancelRequestTask httpRequest = new CancelRequestTask(getActivity(), FragmentMessage.this, message
							.getOrderid());
					httpRequest.execute();
				}
			});
		}

		@Override
		public void onAnswerReturn(Object answer) {
			if (TextUtils.isEmpty((String) answer)) {
				Dialogs.showServerFailedDialog(getActivity());
			} else {
				Dialogs.successToast(getActivity().getApplicationContext());
				onFinish();
			}
		}

		protected boolean isMessageResponseOk() {
			return (!TextUtils.isEmpty(messageResponse.getPrice()) && !TextUtils.isEmpty(messageResponse.getHour()));
		}

		@Override
		public void onTreatmentListChange(ArrayList<TreatmentType> treatments) {
            ((ActivityMessage)getActivity()).setBackPressedListener(null);
			ArrayList<TreatmentType> arr = new ArrayList<TreatmentType>();
			for (TreatmentType tt : treatments) {
				if (tt.getAmount() > 0) {
					arr.add(tt);
				}
			}
			messageResponse.setTreatments(getReleventTreatmentList(arr));

			Formatter.getSelf(getActivity()).setTreatmentsList(tvWantedTreatment1, tvWantedTreatment2,
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
			messageResponse.setHour(hour);
			timeDivider.setVisibility(View.VISIBLE);
			tvHour.getLayoutParams().width = LayoutParams.WRAP_CONTENT;
			tvHour.setBackground(null);
			tvHour.setText(hour);
		}

		private void onLocationSelected(String location) {
			tvLocation.setText(location);
			messageResponse.setPlace(location);
		}

		private void onCommentsChanged(String comments) {
			if (!TextUtils.isEmpty(comments)) {
				messageResponse.setComments(comments);
				StringBuilder commentBuilder = new StringBuilder();
				commentBuilder.append("\"");
                commentBuilder.append(message.getComments());
                commentBuilder.append("\"");
                commentBuilder.append("\n\n");
                commentBuilder.append(comments);
				tvRemarks.setText(commentBuilder.toString());
			}
		}

		private void showHourDialog() {
			if (timePikerDialog == null) {
				timePikerDialog = new TimePickerDialog(getActivity(), this);
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
						messageResponse.setPrice(String.valueOf(np.getValue()));
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
							}
						});
			}
			locationDialog.show();
		}

		private void showAddressDialog() {
			if (customAddressDialog == null) {
				customAddressDialog = new MyCustomDialog(getActivity());
				final EditText editText = customAddressDialog.getEditText();
				customAddressDialog.setDialogTitle(R.string.address)
						.setPositiveButton(R.string.dialog_ok_text, new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								PostVerifyAddress httpPost = new PostVerifyAddress(getActivity(), new HttpCallback() {

									@Override
									public void onAnswerReturn(Object answer) {
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
						});
			}
		}

		private void showRemarksDialog() {
			if (remarksDialog == null) {
				remarksDialog = new MyCustomDialog(getActivity());
				final EditText beauticianRemarks = remarksDialog.getEditText();
				remarksDialog.setMessage("\"" + message.getComments() + "\"").setDialogTitle(R.string.remarks)
						.setPositiveButton(R.string.dialog_ok_text, new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								onCommentsChanged(beauticianRemarks.getText().toString());
							}
						}).setNegativeButton(R.string.dialog_cancel_text, null);
			}
			remarksDialog.show();
		}

		private void onFinish() {
			DataUtils.get(getActivity()).deleteOrderAroundMe(message.getOrderid());
			getActivity().setResult(RESULT_OK, new Intent().putExtra(Constant.EXTRA_ORDER_ID, message.getOrderid()));
            ((ActivityMessage)getActivity()).backPressed();
		}

	}

}
