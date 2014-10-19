package com.pictureit.noambaroz.beautyapp;

import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;

import com.pictureit.noambaroz.beautyapp.data.Constant;
import com.pictureit.noambaroz.beautyapp.data.DataProvider;

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
	}
}
