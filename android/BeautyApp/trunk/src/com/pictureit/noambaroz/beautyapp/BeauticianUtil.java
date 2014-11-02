package com.pictureit.noambaroz.beautyapp;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.pictureit.noambaroz.beautyapp.data.Beautician;
import com.pictureit.noambaroz.beautyapp.data.Beautician.Address;
import com.pictureit.noambaroz.beautyapp.data.Constant;
import com.pictureit.noambaroz.beautyapp.data.TreatmentType;
import com.pictureit.noambaroz.beautyapp.data.StringArrays;

public class BeauticianUtil {

	public static void openBeauticianInNewActivity(Activity activity, Beautician beautician) {
		Intent intent = new Intent(activity, ActivityBeautician.class);
		intent.putExtra(Constant.EXTRA_BEAUTICIAN_OBJECT, beautician);
		activity.startActivity(intent);
		activity.overridePendingTransition(R.anim.activity_enter_slidein_anim, R.anim.activity_exit_shrink_anim);
	}

	public static String formatAddress(Context context, Address address) {
		if (address == null)
			return context.getResources().getString(R.string.error_no_address);
		return new StringBuilder().append(address.getStreet()).append(", ").append(address.getCity()).toString();
	}

	public static String formatRaters(int raters, Context context) {
		return new StringBuilder().append("( ").append(raters).append(" ")
				.append(context.getString(R.string.beautician_page_raters)).append(" ").append(")").toString();
	}

	public static String formatDegrees(Context context, String[] degrees) {
		if (degrees == null)
			return context.getResources().getString(R.string.error_no_degrees);
		StringBuilder builder = new StringBuilder();
		int length = degrees.length;
		for (int i = 0; i < length; i++) {
			builder.append(degrees[i]).append(".");
			if (i != length - 1)
				builder.append("\n");
		}
		return builder.toString();
	}

	public static String formatTreatmentsWith__(Context context, String[] treatmentsId) {
		int half = (treatmentsId.length / 2);
		boolean __Added = false;
		StringBuilder sb1 = new StringBuilder();
		ArrayList<TreatmentType> arrayList = StringArrays.TreatmentList.genarate(context, null);
		for (int i = 0; i < treatmentsId.length; i++) {
			for (TreatmentType type : arrayList) {
				if (type.getTreatments_id().equals(treatmentsId[i])) {
					if (half == i)
						if (!__Added) {
							__Added = true;
							sb1.append("__");
						}
					sb1.append(type.getName()).append("\n");
					continue;
				}
			}
		}
		return sb1.toString();
	}
}
