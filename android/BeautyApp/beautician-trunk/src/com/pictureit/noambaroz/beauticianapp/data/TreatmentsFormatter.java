package com.pictureit.noambaroz.beauticianapp.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.view.View;
import android.widget.TextView;

import com.pictureit.noambaroz.beautycianapp.R;

public class TreatmentsFormatter {

	private static TreatmentsFormatter INSTANCE;
	private HashMap<String, TreatmentType> mHash;
	private ArrayList<TreatmentType> mTreatmentsTypeArrayList;
	private Context mContext;

	// private ArrayList<ClassificationType> mClassificationArrayList;

	public List<TreatmentType> getAllTreatmentsType() {
		return getTreatmentsArrayList();
	}

	// public static List<ClassificationType> getAllClassificationType(Context
	// context) {
	// return getInstance(context).getClassificationsArrayList();
	// }

	public TreatmentType getTreatmentType(String treatmentId) {
		return getMap().get(treatmentId);
	}

	public static TreatmentsFormatter getSelf(Context context) {
		if (INSTANCE == null) {
			INSTANCE = new TreatmentsFormatter();
			INSTANCE.setContext(context);
			INSTANCE.setHash();
			// INSTANCE.setClassificationArray(context);
		}

		return INSTANCE;
	}

	private void setContext(Context context) {
		mContext = context;
	}

	private ArrayList<TreatmentType> getTreatmentsArrayList() {
		return mTreatmentsTypeArrayList;
	}

	// private ArrayList<ClassificationType> getClassificationsArrayList() {
	// return mClassificationArrayList;
	// }

	private void setHash() {
		mHash = new HashMap<String, TreatmentType>();
		mTreatmentsTypeArrayList = TreatmentList.genarate(mContext, null);
		for (int i = 0; i < mTreatmentsTypeArrayList.size(); i++) {
			mHash.put(mTreatmentsTypeArrayList.get(i).getTreatmentId(), mTreatmentsTypeArrayList.get(i));
		}
	}

	private HashMap<String, TreatmentType> getMap() {
		return mHash;
	}

	// private void setClassificationArray(Context context) {
	// mClassificationArrayList = new ArrayList<ClassificationType>();
	// Resources res = context.getResources();
	// TypedArray tar = res.obtainTypedArray(R.array.classification_list);
	// for (int i = 0; i < tar.length(); ++i) {
	// int id = tar.getResourceId(i, -1);
	// if (id > -1) {
	// String[] array = res.getStringArray(id);
	// ClassificationType ct = new ClassificationType();
	// ct.setTitle(array[1]);
	// ct.setId(array[0]);
	// mClassificationArrayList.add(ct);
	// } else {
	// // something wrong with the XML
	// }
	// }
	// tar.recycle(); // Important
	// }

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
							tt.setTreatmentId(array[0]);
							tt.setName(array[1]);
							tt.setDescription(array[2]);
							tt.setPrice(array[3]);
							treatmentsArray.add(tt);
						}
					} else {
						TreatmentType tt = new TreatmentType();
						tt.setTreatmentId(array[0]);
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

	public String getTreatmentName(ArrayList<TreatmentType> arr) {
		StringBuilder sb = new StringBuilder();
		sb.append(getTreatmentType(arr.get(0).getTreatmentId()).getName());
		if (arr.size() > 1) {
			sb.append(" ");
			sb.append(mContext.getString(R.string.and_more));
			sb.append(" ");
			sb.append(arr.size() - 1);
			sb.append(" ");
			sb.append(mContext.getString(R.string.additional));
		}
		return sb.toString();
	}

	public void setTreatmentsList(TextView tv1, TextView tv2, ArrayList<TreatmentType> treatmentsList) {
		tv1.setVisibility(View.VISIBLE);
		tv2.setVisibility(View.VISIBLE);
		StringBuilder sb1 = new StringBuilder();
		StringBuilder sb2 = new StringBuilder();
		int count = 0;
		for (TreatmentType t : treatmentsList) {
			if (Integer.valueOf(t.getAmount()) > 0) {
				String treatmentName = getTreatmentType(t.getTreatmentId()).getName();
				String text = mContext.getString(R.string.bullet) + treatmentName
						+ (t.getAmount() > 1 ? " (" + t.getAmount() + ")" : "") + "\n";
				if (count % 2 == 0)
					sb1.append(text);
				else
					sb2.append(text);
				count++;
			}
		}
		if (sb1.toString().length() == 0)
			tv1.setVisibility(View.GONE);
		tv1.setText(sb1.toString());
		if (sb2.toString().length() == 0)
			tv2.setVisibility(View.GONE);
		tv2.setText(sb2.toString());
	}
}
