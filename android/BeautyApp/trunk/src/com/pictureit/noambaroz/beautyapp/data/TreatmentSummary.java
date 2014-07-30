package com.pictureit.noambaroz.beautyapp.data;

import java.util.ArrayList;
import java.util.HashSet;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;

import com.pictureit.noambaroz.beautyapp.R;

public class TreatmentSummary {

	public String forWho;
	public String when;
	public String whare;
	public String remarks;
	public ArrayList<TreatmentType> tretments;

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
							tt.setId(array[0]);
							tt.setName(array[1]);
							tt.setDescription(array[2]);
							treatmentsArray.add(tt);
						}
					} else {
						TreatmentType tt = new TreatmentType();
						tt.setId(array[0]);
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
