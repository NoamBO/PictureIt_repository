package com.pictureit.noambaroz.beautyapp.data;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;

import com.pictureit.noambaroz.beautyapp.R;

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
		if (arrayList == null)
			return "";
		String str = "";
		for (int i = 0; i < arrayList.size(); i++) {
			if (arrayList.get(i).getAmount() < 1)
				continue;
			str = str + String.valueOf(arrayList.get(i).getTreatments_id()) + idSeparator
					+ arrayList.get(i).getAmount() + countSeparator + arrayList.get(i).getName();
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
			treatment.setTreatments_id(t[0]);
			treatment.setAmount(Integer.valueOf(t[1]));
			treatment.setName(t[2]);
			arrayList.add(treatment);
		}
		return arrayList;
	}

	public static void pushOrderNotificationIdToTable(Context ctx, String notification_id) {
		ContentValues values = new ContentValues(1);
		values.put(DataProvider.COL_NOTIFICATION_ID, notification_id);
		ctx.getContentResolver().insert(DataProvider.CONTENT_URI_MESSAGES, values);
	}

	public static void pushOrderNotificationToTable(Context ctx, ContentValues values, String orderIdInRow) {
		ctx.getContentResolver().update(DataProvider.CONTENT_URI_MESSAGES, values,
				DataProvider.COL_NOTIFICATION_ID + " =" + orderIdInRow, null);
	}

	public static void storePhoneNumberOnPreference(Context context, String phoneNumber) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(context.getString(R.string.preference_key_my_profile_phone_number), phoneNumber);
		editor.commit();
	}

	public static String getLocalPhoneNumber(Context context) {
		TelephonyManager tMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		return PreferenceManager.getDefaultSharedPreferences(context).getString(
				context.getString(R.string.preference_key_my_profile_phone_number), tMgr.getLine1Number());
	}

	public static void updateMyProfile(Context context, String firstName, String lastName, String email, String address) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(context.getString(R.string.preference_key_my_profile_first_name), firstName);
		editor.putString(context.getString(R.string.preference_key_my_profile_last_name), lastName);
		editor.putString(context.getString(R.string.preference_key_my_profile_email), email);
		editor.putString(context.getString(R.string.preference_key_my_profile_address), address);
		editor.commit();
	}

	// public static void updateMyProfile(Context context, String firstName,
	// String lastName, String email, String address) {
	// ContentValues values = new ContentValues(4);
	// values.put(DataProvider.COL_MY_NAME, firstName);
	// values.put(DataProvider.COL_MY_LAST_NAME, lastName);
	// values.put(DataProvider.COL_MY_EMAIL, email);
	// values.put(DataProvider.COL_MY_ADDRESS, address);
	//
	// Cursor q =
	// context.getContentResolver().query(DataProvider.CONTENT_URI_MY_PROFILE,
	// null, null, null, null);
	// if (!q.moveToFirst()) {
	// context.getContentResolver().delete(DataProvider.CONTENT_URI_MY_PROFILE,
	// null, null);
	// }
	// context.getContentResolver().insert(DataProvider.CONTENT_URI_MY_PROFILE,
	// values);
	// }

}
