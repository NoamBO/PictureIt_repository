package com.pictureit.noambaroz.beauticianapp.data;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.text.TextUtils;

public class DataUtils {

	private static DataUtils dataUtils;

	private Context mContext;

	public DataUtils(Context ctx) {
		mContext = ctx;
	}

	public static DataUtils get(Context ctx) {
		if (dataUtils == null) {
			dataUtils = new DataUtils(ctx);
		}
		return dataUtils;
	}

	public void refreshMapFragment(ArrayList<OrderAroundMe> array) {

		mContext.getContentResolver().delete(DataProvider.CONTENT_URI_ORDERS_AROUND_ME,
				DataProvider.COL_IS_DIRECTED_TO_ME + " NOT LIKE ?", new String[] { "'true'" });

		for (int i = 0; i < array.size(); i++) {
			if (array.get(i).isDirectedToMe() != null && array.get(i).isDirectedToMe().equalsIgnoreCase("true"))
				continue;

			ContentValues cv = new ContentValues();
			cv.put(DataProvider.COL_FIRST_NAME, array.get(i).getPrivateName());
			cv.put(DataProvider.COL_LAST_NAME, array.get(i).getFamilyName());
			cv.put(DataProvider.COL_LATITUDE, array.get(i).getLatitude());
			cv.put(DataProvider.COL_LONGITUDE, array.get(i).getLongitude());
			cv.put(DataProvider.COL_ORDER_ID, array.get(i).getOrderid());
			cv.put(DataProvider.COL_IS_DIRECTED_TO_ME, !TextUtils.isEmpty(array.get(i).isDirectedToMe()) ? array.get(i)
					.isDirectedToMe() : "false");
			mContext.getContentResolver().insert(DataProvider.CONTENT_URI_ORDERS_AROUND_ME, cv);
		}
	}
}
