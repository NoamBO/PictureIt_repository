package com.pictureit.noambaroz.beautyapp.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;

import com.pictureit.noambaroz.beautyapp.R;

public class StringArrays {

	private static StringArrays INSTANCE;
	private HashMap<String, TreatmentType> mHash;
	private ArrayList<TreatmentType> mTreatmentsTypeArrayList;
	private ArrayList<ClassificationType> mClassificationArrayList;

	public static List<TreatmentType> getAllTreatmentsType(Context context) {
		return getInstance(context).getTreatmentsArrayList();
	}

	public static List<ClassificationType> getAllClassificationType(Context context) {
		return getInstance(context).getClassificationsArrayList();
	}

	public static TreatmentType getTreatmentType(Context context, String treatmentId) {
		return getInstance(context).getMap().get(treatmentId);
	}

	private static StringArrays getInstance(Context context) {
		if (INSTANCE == null) {
			INSTANCE = new StringArrays();
			INSTANCE.setHash(context);
			INSTANCE.setClassificationArray(context);
		}

		return INSTANCE;
	}

	private ArrayList<TreatmentType> getTreatmentsArrayList() {
		return mTreatmentsTypeArrayList;
	}

	private ArrayList<ClassificationType> getClassificationsArrayList() {
		return mClassificationArrayList;
	}

	private void setHash(Context ctx) {
		mHash = new HashMap<String, TreatmentType>();
		mTreatmentsTypeArrayList = TreatmentList.genarate(ctx, null);
		for (int i = 0; i < mTreatmentsTypeArrayList.size(); i++) {
			mHash.put(mTreatmentsTypeArrayList.get(i).getTreatments_id(), mTreatmentsTypeArrayList.get(i));
		}
	}

	private HashMap<String, TreatmentType> getMap() {
		return mHash;
	}

	private void setClassificationArray(Context context) {
		mClassificationArrayList = new ArrayList<ClassificationType>();
		Resources res = context.getResources();
		TypedArray tar = res.obtainTypedArray(R.array.classification_list);
		for (int i = 0; i < tar.length(); ++i) {
			int id = tar.getResourceId(i, -1);
			if (id > -1) {
				String[] array = res.getStringArray(id);
				ClassificationType ct = new ClassificationType();
				ct.setTitle(array[1]);
				ct.setId(array[0]);
				mClassificationArrayList.add(ct);
			} else {
				// something wrong with the XML
			}
		}
		tar.recycle(); // Important
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
							tt.setPrice(array[3]);
							treatmentsArray.add(tt);
						}
					} else {
						TreatmentType tt = new TreatmentType();
						tt.setTreatments_id(array[0]);
						tt.setName(array[1]);
						tt.setDescription(array[2]);
						tt.setPrice(array[3]);
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
