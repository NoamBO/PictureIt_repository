package com.pictureit.noambaroz.beautyapp;

import java.util.ArrayList;

import utilities.OutgoingCommunication;
import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.pictureit.noambaroz.beautyapp.data.Constant;
import com.pictureit.noambaroz.beautyapp.data.DataProvider;
import com.pictureit.noambaroz.beautyapp.data.DataUtil;
import com.pictureit.noambaroz.beautyapp.data.TreatmentType;
import com.pictureit.noambaroz.beautyapp.data.StringArrays;
import com.pictureit.noambaroz.beautyapp.server.ImageLoaderUtil;

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

		private boolean isConfirmed;

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

		private Button bConfirm;
		private Button bReject;
		private LinearLayout llCall;

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
			ImageView pic = findView(v, R.id.iv_message_beautician_pic);
			TextView name = findView(v, R.id.tv_message_beautician_name);
			TextView address = findView(v, R.id.tv_message_address);
			TextView raters = findView(v, R.id.tv_message_raters);
			RatingBar ratingBar = findView(v, R.id.rb__message_rating);
			TextView date = findView(v, R.id.tv_message_inner_treatment_date);
			TextView location = findView(v, R.id.tv_message_inner_treatment_location);
			TextView remarks = findView(v, R.id.tv_message_inner_remarks);
			TextView price = findView(v, R.id.tv_message_inner_price);
			TextView treatments1 = findView(v, R.id.tv_message_wanted_treatment1);
			TextView treatments2 = findView(v, R.id.tv_message_wanted_treatment2);
			bConfirm = findView(v, R.id.b_message_inner_confirm);
			bReject = findView(v, R.id.b_message_inner_reject);
			llCall = findView(v, R.id.ll_message_inner_call);
			TextView callToName = findView(v, R.id.tv_message_inner_name_to_call_to);

			name.setText(beauticianName);
			address.setText(beauticianAddress);
			raters.setText("( " + beauticianRaters + " )");
			ratingBar.setRating((float) beauticianRating);
			date.setText(treatmentDate);
			location.setText(treatmentLocation);
			remarks.setText(this.remarks);
			price.setText(getString(R.string.price) + " " + this.price);
			ImageLoaderUtil.display(picUrl, pic);
			callToName.setText(getString(R.string.call_to) + " " + beauticianName);
			setTreatmentsList(treatments1, treatments2);
			return v;
		}

		@Override
		public void onResume() {
			super.onResume();
			bConfirm.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					isConfirmed = true;
					if (!TextUtils.isEmpty(phone))
						llCall.setVisibility(View.VISIBLE);
				}
			});

			llCall.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					OutgoingCommunication.call(getActivity(), phone);
				}
			});

			bReject.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					isConfirmed = false;
				}
			});
		}

		@Override
		public void onDestroy() {
			super.onDestroy();
			if (isConfirmed)
				getContentResolver().delete(DataProvider.CONTENT_URI_MESSAGES,
						DataProvider.COL_NOTIFICATION_ID + " = ?", new String[] { messageId });
		}
	}
}
