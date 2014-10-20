package com.pictureit.noambaroz.beautyapp.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;

import com.pictureit.noambaroz.beautyapp.R;

public class Treatments {

	private static Treatments INSTANCE;
	private HashMap<String, TreatmentType> mHash;

	public static TreatmentType getTreatmentType(Context context, String treatmentId) {
		return getInstance(context).getAll().get(treatmentId);
	}

	private static Treatments getInstance(Context context) {
		if (INSTANCE == null) {
			INSTANCE = new Treatments();
			INSTANCE.setHash(context);
		}

		return INSTANCE;
	}

	private void setHash(Context ctx) {
		mHash = new HashMap<String, TreatmentType>();
		ArrayList<TreatmentType> arr = TreatmentList.genarate(ctx, null);
		for (int i = 0; i < arr.size(); i++) {
			mHash.put(arr.get(i).getTreatments_id(), arr.get(i));
		}
	}

	private HashMap<String, TreatmentType> getAll() {
		return mHash;
	}

	public static class TreatmentList {

		public static ArrayList<TreatmentType> genarate(Context context, String[] treatments) {
			HashSet<String> temp = new HashSet<String>();
			boolean needToCompareWithBeauticianTretmantsList = treatments == null ? false : true;
			if (needToCompareWithBeauticianTretmantsList)
				for (int i = 0; i < treatments.length; i++) {
					temp.add(treatments[i]);
				}
			ArrayList<TreatmentType> treatmentsArray = new ArrayList<TreatmentType>();
			Resources res = context.getResources();
			TypedArray ta = res.obtainTypedArray(R.array.treatments_list);
			for (int i = 0; i < ta.length(); ++i) {
				int id = ta.getResourceId(i, -1);
				if (id > -1) {
					String[] array = res.getStringArray(id);
					if (needToCompareWithBeauticianTretmantsList) {
						if (temp.contains(array[0])) {
							TreatmentType tt = new TreatmentType();
							tt.setTreatments_id(array[0]);
							tt.setName(array[1]);
							tt.setDescription(array[2]);
							treatmentsArray.add(tt);
						}
					} else {
						TreatmentType tt = new TreatmentType();
						tt.setTreatments_id(array[0]);
						tt.setName(array[1]);
						tt.setDescription(array[2]);
						treatmentsArray.add(tt);
					}
				} else {
					// something wrong with the XML
				}
			}
			ta.recycle(); // Important
			return treatmentsArray;
		}

	}
}
