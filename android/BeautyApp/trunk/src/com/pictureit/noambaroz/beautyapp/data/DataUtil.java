package com.pictureit.noambaroz.beautyapp.data;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.preference.PreferenceManager;

import com.pictureit.noambaroz.beautyapp.Application;

public class DataUtil {

	public static void saveHistoryRow(TreatmentSummary treatment, Context context) {
		ContentValues values = new ContentValues(2);
		values.put(DataProvider.COL_DATE, treatment.when);
		values.put(DataProvider.COL_FOR, treatment.forWho);
		values.put(DataProvider.COL_REMARKS, treatment.remarks);
		values.put(DataProvider.COL_TREATMENTS, convertArrayToString(treatment.tretments));
		values.put(DataProvider.COL_WHARE, treatment.whare);
		context.getContentResolver().insert(DataProvider.CONTENT_URI_HISTORY, values);
	}

	private static String treatmentSeparator = "__,__";

	private static String idSeparator = "--,--";

	private static String countSeparator = "-_,_-";

	public static String convertArrayToString(ArrayList<TreatmentType> arrayList) {
		String str = "";
		for (int i = 0; i < arrayList.size(); i++) {
			if (arrayList.get(i).getCount() < 1)
				continue;
			str = str + String.valueOf(arrayList.get(i).id) + idSeparator + arrayList.get(i).getCount()
					+ countSeparator + arrayList.get(i).getName();
			// Do not append comma at the end of last element
			if (i < arrayList.size() - 1) {
				str = str + treatmentSeparator;
			}
		}

		return str;
	}

	public static ArrayList<TreatmentType> convertStringToArray(String str) {
		ArrayList<TreatmentType> arrayList = new ArrayList<TreatmentType>();
		String[] arr = str.split(treatmentSeparator);
		for (String s : arr) {
			TreatmentType treatment = new TreatmentType();
			String[] t = s.split(idSeparator + "|" + countSeparator);
			treatment.setId(t[0]);
			treatment.setCount(Integer.valueOf(t[1]));
			treatment.setName(t[2]);
			arrayList.add(treatment);
		}
		return arrayList;
	}

	public static void savePendingTreatmentId(Context ctx, String treatmentId) {
		PreferenceManager.getDefaultSharedPreferences(ctx).edit()
				.putString(Application.PENDING_TREATMENT_ID, treatmentId).commit();
	}

	public static String getPendingTreatmentId(Context ctx) {
		return PreferenceManager.getDefaultSharedPreferences(ctx).getString(Application.PENDING_TREATMENT_ID, "");
	}

	public static void pushOrderNotificationIdToTable(Context ctx, String notification_id) {
		ContentValues values = new ContentValues(1);
		values.put(DataProvider.COL_NOTIFICATION_ID, notification_id);
		ctx.getContentResolver().insert(DataProvider.CONTENT_URI_ORDER_OPTIONS, values);
	}

	public static void pushOrderNotificationToTable(Context ctx, ContentValues values, String orderIdInRow) {
		ctx.getContentResolver().update(DataProvider.CONTENT_URI_ORDER_OPTIONS, values,
				DataProvider.COL_NOTIFICATION_ID + " =" + orderIdInRow, null);
	}

}
