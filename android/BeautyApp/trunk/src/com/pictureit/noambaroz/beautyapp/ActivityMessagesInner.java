package com.pictureit.noambaroz.beautyapp;

import java.util.ArrayList;

import utilities.Dialogs;
import utilities.OutgoingCommunication;
import utilities.server.HttpBase.HttpCallback;
import android.app.Fragment;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.pictureit.noambaroz.beautyapp.customdialogs.ConfirmedMessageDialog;
import com.pictureit.noambaroz.beautyapp.customdialogs.MyCustomDialog;
import com.pictureit.noambaroz.beautyapp.data.Constant;
import com.pictureit.noambaroz.beautyapp.data.DataProvider;
import com.pictureit.noambaroz.beautyapp.data.DataUtil;
import com.pictureit.noambaroz.beautyapp.data.StringArrays;
import com.pictureit.noambaroz.beautyapp.data.TreatmentType;
import com.pictureit.noambaroz.beautyapp.server.ImageLoaderUtil;
import com.pictureit.noambaroz.beautyapp.server.PostConfirmBeauticianOffer;
import com.pictureit.noambaroz.beautyapp.server.PostRejectBeauticianOffer;

public class ActivityMessagesInner extends ActivityWithFragment {

	protected String messageId;

	@Override
	protected void initActivity() {
		Bundle data = getIntent().getExtras();
		if (data != null)
			messageId = data.getString(Constant.EXTRA_MESSAGE_ID, "");
	}

	@Override
	protected void setFragment() {
		fragment = new FragmentInnerMessage();
	}

	@Override
	protected void setFragmentTag() {
		FRAGMENT_TAG = "inner_message_fragment";
	}

	@Override
	public void onBackPressed() {
		backPressed();
	}

	class FragmentInnerMessage extends Fragment {

		private boolean isFinished;

		private String picUrl;
		private String beauticianName;
		private String beauticianAddress;
		private int beauticianRaters;
		private double beauticianRating;
		private String treatmentDate;
		private String treatmentLocation;
		private String remarks;
		private String price;
		private String phone;
		private String unFormattedTreatments;

		private TextView bConfirm;
		private TextView bReject;

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			getMessageData();
		}

		private boolean getMessageData() {
			Cursor c = getContentResolver().query(DataProvider.CONTENT_URI_MESSAGES, null,
					DataProvider.COL_NOTIFICATION_ID + " = ?", new String[] { messageId }, null);
			if (c.moveToFirst()) {
				picUrl = c.getString(c.getColumnIndex(DataProvider.COL_PIC));
				beauticianName = c.getString(c.getColumnIndex(DataProvider.COL_NAME));
				beauticianAddress = c.getString(c.getColumnIndex(DataProvider.COL_ADDRESS));
				beauticianRaters = c.getInt(c.getColumnIndex(DataProvider.COL_RATERS));
				beauticianRating = c.getDouble(c.getColumnIndex(DataProvider.COL_RATE));
				treatmentDate = c.getString(c.getColumnIndex(DataProvider.COL_AT));
				treatmentLocation = c.getString(c.getColumnIndex(DataProvider.COL_LOCATION));
				remarks = c.getString(c.getColumnIndex(DataProvider.COL_REMARKS));
				price = c.getString(c.getColumnIndex(DataProvider.COL_PRICE));
				phone = c.getString(c.getColumnIndex(DataProvider.COL_PHONE));
				unFormattedTreatments = c.getString(c.getColumnIndex(DataProvider.COL_TREATMENTS));
				return true;
			} else
				return false;
		}

		private void setTreatmentsList(TextView tv1, TextView tv2) {
			ArrayList<TreatmentType> treatments = DataUtil.convertStringToArray(unFormattedTreatments);
			StringBuilder sb1 = new StringBuilder();
			StringBuilder sb2 = new StringBuilder();
			int count = 0;
			for (TreatmentType t : treatments) {
				if (Integer.valueOf(t.getAmount()) > 0) {
					String treatmentName = StringArrays.getTreatmentType(getActivity(), t.getTreatments_id()).getName();
					if (count % 2 == 0)
						sb1.append(treatmentName).append("\n");
					else
						sb2.append(treatmentName).append("\n");
					count++;
				}
			}
			if (count < 2)
				tv2.setVisibility(View.GONE);
			tv1.setText(sb1.toString());
			tv2.setText(sb2.toString());
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View v = inflater.inflate(R.layout.fragment_message, container, false);
			ImageView pic = findView(v, R.id.iv_base_beautician_row_pic);
			TextView name = findView(v, R.id.tv_base_beautician_row_name);
			TextView address = findView(v, R.id.tv_base_beautician_row_address);
			TextView raters = findView(v, R.id.tv_base_beautician_row_raters);
			RatingBar ratingBar = findView(v, R.id.rb_base_beautician_row_rating);
			TextView date = findView(v, R.id.tv_message_inner_treatment_date);
			TextView location = findView(v, R.id.tv_message_inner_treatment_location);
			TextView remarks = findView(v, R.id.tv_message_inner_remarks);
			TextView price = findView(v, R.id.tv_message_inner_price);
			TextView treatments1 = findView(v, R.id.tv_message_wanted_treatment1);
			TextView treatments2 = findView(v, R.id.tv_message_wanted_treatment2);
			bConfirm = findView(v, R.id.tv_message_inner_confirm);
			bReject = findView(v, R.id.tv_message_inner_reject);

			name.setText(beauticianName);
			address.setText(beauticianAddress);
			raters.setText("( " + beauticianRaters + " )");
			ratingBar.setRating((float) beauticianRating);
			date.setText(treatmentDate);
			location.setText(treatmentLocation);
			remarks.setText(this.remarks);
			price.setText(getString(R.string.price) + " " + this.price);
			ImageLoaderUtil.display(picUrl, pic);
			setTreatmentsList(treatments1, treatments2);
			return v;
		}

		@Override
		public void onResume() {
			super.onResume();
			bConfirm.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					onButtonConfirm();
				}
			});

			bReject.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					onReject();
				}
			});
		}

		private void onReject() {
			if (isFinished)
				return;

			MyCustomDialog dialog = new MyCustomDialog(getActivity());
			dialog.setPositiveButton(getString(R.string.dialog_ok_text), new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					HttpCallback callback = new HttpCallback() {

						@Override
						public void onAnswerReturn(Object answer) {
							if ((Boolean) answer) {
								isFinished = true;
								backPressed();
							} else {
								Dialogs.showServerFailedDialog(getActivity());
							}
						}
					};
					PostRejectBeauticianOffer httpRequest = new PostRejectBeauticianOffer(getActivity(), callback,
							messageId);
					httpRequest.execute();
				}
			});
			dialog.setNegativeButton(getString(R.string.dialog_cancel_text), null);
			dialog.setMessage(getString(R.string.reject_dialog_message));
			dialog.show();
		}

		private void onButtonConfirm() {
			if (isFinished)
				return;

			PostConfirmBeauticianOffer httpRequest = new PostConfirmBeauticianOffer(getActivity(), new HttpCallback() {

				@Override
				public void onAnswerReturn(Object answer) {
					if ((Boolean) answer) {
						onConfirmed();
					} else {
						Dialogs.showServerFailedDialog(getActivity());
					}
				}
			}, messageId);
			httpRequest.execute();
		}

		private void onConfirmed() {
			isFinished = true;
			ConfirmedMessageDialog dialog = new ConfirmedMessageDialog(getActivity());
			dialog.setCallButtonListener(new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					OutgoingCommunication.call(getActivity(), phone);
				}
			});
			dialog.setCloseButtonListener(new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					backPressed();
				}
			});
		}

		@Override
		public void onDestroy() {
			super.onDestroy();
			if (isFinished)
				getContentResolver().delete(DataProvider.CONTENT_URI_MESSAGES,
						DataProvider.COL_NOTIFICATION_ID + " = ?", new String[] { messageId });
		}
	}
}
