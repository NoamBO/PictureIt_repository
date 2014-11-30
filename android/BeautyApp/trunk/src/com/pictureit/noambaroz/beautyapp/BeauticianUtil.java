package com.pictureit.noambaroz.beautyapp;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.TextView;

import com.pictureit.noambaroz.beautyapp.data.Beautician;
import com.pictureit.noambaroz.beautyapp.data.Beautician.Address;
import com.pictureit.noambaroz.beautyapp.data.ClassificationType;
import com.pictureit.noambaroz.beautyapp.data.Constant;
import com.pictureit.noambaroz.beautyapp.data.StringArrays;
import com.pictureit.noambaroz.beautyapp.data.TreatmentType;

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
		if (degrees == null || degrees.length == 0)
			return context.getResources().getString(R.string.no_diplomas);
		StringBuilder builder = new StringBuilder();
		int length = degrees.length;
		for (int i = 0; i < length; i++) {
			builder.append(degrees[i]).append(".");
			if (i != length - 1)
				builder.append("\n");
		}
		return builder.toString();
	}

	public static ClassificationType getClassificationTypeById(Context context, String id) {
		ClassificationType classificationType = null;
		for (ClassificationType ct : StringArrays.getAllClassificationType(context)) {
			if (ct.getId().equalsIgnoreCase(id)) {
				classificationType = ct;
				break;
			}
		}
		return classificationType == null ? new ClassificationType() : classificationType;
	}

	public static String formatTreatments(Context context, String[] treatmentsId) {

		StringBuilder sb1 = new StringBuilder();
		ArrayList<TreatmentType> arrayList = StringArrays.TreatmentList.genarate(context, null);
		for (int i = 0; i < treatmentsId.length; i++) {
			for (TreatmentType type : arrayList) {
				if (type.getTreatments_id().equals(treatmentsId[i])) {
					sb1.append(type.getName()).append("\n");
					continue;
				}
			}
		}
		return sb1.toString();
	}

	public static void setTreatmentsList(Context context, TextView tv1, TextView tv2,
			ArrayList<TreatmentType> treatmentsList) {
		StringBuilder sb1 = new StringBuilder();
		StringBuilder sb2 = new StringBuilder();
		int count = 0;
		for (TreatmentType t : treatmentsList) {
			if (Integer.valueOf(t.getAmount()) > 0) {
				String treatmentName = StringArrays.getTreatmentType(context, t.getTreatments_id()).getName();
				if (count % 2 == 0)
					sb1.append(context.getString(R.string.bullet) + treatmentName).append("\n");
				else
					sb2.append(context.getString(R.string.bullet) + treatmentName).append("\n");
				count++;
			}
		}
		tv1.setText(sb1.toString());
		tv2.setText(sb2.toString());
	}
}
