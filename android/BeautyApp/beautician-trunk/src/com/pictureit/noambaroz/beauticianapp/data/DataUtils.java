package com.pictureit.noambaroz.beauticianapp.data;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;

import com.pictureit.noambaroz.beauticianapp.MyPreference;

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
				DataProvider.COL_IS_DIRECTED_TO_ME + " NOT LIKE ?", new String[] { String.valueOf(true) });

		for (int i = 0; i < array.size(); i++) {
			if (array.get(i).isDirectedToMe() != null && array.get(i).isDirectedToMe().equalsIgnoreCase("true"))
				continue;

			addOrderAroundMe(array.get(i), false);
		}
	}

	public void addOrderAroundMe(OrderAroundMe order, boolean checkDuplicates) {
		if (checkDuplicates) {
			Cursor c = mContext.getContentResolver().query(DataProvider.CONTENT_URI_ORDERS_AROUND_ME, null,
					DataProvider.COL_ORDER_ID + " LIKE ?", new String[] { order.getOrderid() }, null);
			if (c.getCount() > 0)
				return;
		}

		ContentValues cv = new ContentValues(6);
		cv.put(DataProvider.COL_FIRST_NAME, order.getPrivateName());
		cv.put(DataProvider.COL_LAST_NAME, order.getFamilyName());
		cv.put(DataProvider.COL_LATITUDE, order.getLatitude());
		cv.put(DataProvider.COL_LONGITUDE, order.getLongitude());
		cv.put(DataProvider.COL_ORDER_ID, order.getOrderid());
		cv.put(DataProvider.COL_IS_DIRECTED_TO_ME, !TextUtils.isEmpty(order.isDirectedToMe()) ? order.isDirectedToMe()
				: "false");
		mContext.getContentResolver().insert(DataProvider.CONTENT_URI_ORDERS_AROUND_ME, cv);
	}

	public void deleteOrderAroundMe(String orderID) {
		mContext.getContentResolver().delete(DataProvider.CONTENT_URI_ORDERS_AROUND_ME,
				DataProvider.COL_ORDER_ID + " LIKE ?", new String[] { orderID });
	}

	public void addTreatmentConfirmedRow(BeauticianOfferResponse offerResponse) {
		ContentValues cv = new ContentValues(2);
		cv.put(DataProvider.COL_CUSTOMER_TELEPHONE, offerResponse.telephone);
		cv.put(DataProvider.COL_TREATMENT_ID, offerResponse.orderid);

		mContext.getContentResolver().insert(DataProvider.CONTENT_CONFIRMED_TREATMENTS, cv);
		MyPreference.setHasAlarmsDialogsToShow(true);
	}

	public void deleteTreatmentConfirmedRow(String orderID) {
		mContext.getContentResolver().delete(DataProvider.CONTENT_CONFIRMED_TREATMENTS,
				DataProvider.COL_TREATMENT_ID + " LIKE ?", new String[] { orderID });
	}
}
