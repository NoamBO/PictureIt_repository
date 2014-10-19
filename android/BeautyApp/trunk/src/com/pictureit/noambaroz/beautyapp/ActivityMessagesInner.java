package com.pictureit.noambaroz.beautyapp;

import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.pictureit.noambaroz.beautyapp.data.Constant;
import com.pictureit.noambaroz.beautyapp.data.DataProvider;
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

	class FragmentInnerMessage extends Fragment {

		String picUrl;
		String beauticianName;
		String beauticianAddress;
		int beauticianRaters;
		double beauticianRating;
		String treatmentDate;
		String treatmentLocation;
		String remarks;
		String price;
		String phone;

		Button bConfirm;
		Button bReject;

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
				return true;
			} else
				return false;
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
			bConfirm = findView(v, R.id.b_message_inner_confirm);
			bReject = findView(v, R.id.b_message_inner_reject);

			name.setText(beauticianName);
			address.setText(beauticianAddress);
			raters.setText("( " + beauticianRaters + " )");
			ratingBar.setRating((float) beauticianRating);
			date.setText(treatmentDate);
			location.setText(treatmentLocation);
			remarks.setText(this.remarks);
			price.setText(this.price);
			ImageLoaderUtil.display(picUrl, pic);
			return v;
		}
	}
}
