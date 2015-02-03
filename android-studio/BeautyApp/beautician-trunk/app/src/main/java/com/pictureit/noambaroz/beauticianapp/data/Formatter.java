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

import com.pictureit.noambaroz.beauticianapp.R;

public class Formatter {

	private static Formatter INSTANCE;
	private HashMap<String, TreatmentType> mHash;
	private ArrayList<TreatmentType> mTreatmentsTypeArrayList;
	private Context mContext;

	private ArrayList<IntegerString> mClassificationArrayList;

	private ArrayList<IntegerString> mAreaArray;

	private ArrayList<IntegerString> mPaymentMethodArray;

	public List<TreatmentType> getAllTreatmentsType() {
		return getTreatmentsArrayList();
	}

	public List<IntegerString> getAllClassificationType(Context context) {
		return getSelf(context).getClassificationsArrayList();
	}

	public String getClassificationById(String classificationID) {
		for (IntegerString ct : mClassificationArrayList) {
			if (ct.getId().equalsIgnoreCase(classificationID))
				return ct.getTitle();
		}
		return "";
	}

	public TreatmentType getTreatmentType(String treatmentId) {
		return getMap().get(treatmentId);
	}

	public static Formatter getSelf(Context context) {
		if (INSTANCE == null) {
			INSTANCE = new Formatter();
			INSTANCE.setContext(context);
			INSTANCE.setHash();
			INSTANCE.setClassificationArray(context);
			INSTANCE.setAreaArray(context);
			INSTANCE.setPaymentMethodArray(context);
		}

		return INSTANCE;
	}

	private void setContext(Context context) {
		mContext = context;
	}

	private ArrayList<TreatmentType> getTreatmentsArrayList() {
		return mTreatmentsTypeArrayList;
	}

	private ArrayList<IntegerString> getClassificationsArrayList() {
		return mClassificationArrayList;
	}

	private ArrayList<IntegerString> getAreaArrayList() {
		return mAreaArray;
	}

	private ArrayList<IntegerString> getPaymentMethodArrayList() {
		return mPaymentMethodArray;
	}

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

	private void setClassificationArray(Context context) {
		mClassificationArrayList = new ArrayList<IntegerString>();
		Resources res = context.getResources();
		TypedArray tar = res.obtainTypedArray(R.array.classification_list);
		for (int i = 0; i < tar.length(); ++i) {
			int id = tar.getResourceId(i, -1);
			if (id > -1) {
				String[] array = res.getStringArray(id);
				IntegerString ct = new IntegerString();
				ct.setTitle(array[1]);
				ct.setId(array[0]);
				mClassificationArrayList.add(ct);
			} else {
				// something wrong with the XML
			}
		}
		tar.recycle(); // Important

	}

	private void setAreaArray(Context context) {
		mAreaArray = new ArrayList<IntegerString>();
		Resources res = context.getResources();
		TypedArray tar = res.obtainTypedArray(R.array.areas_list);
		for (int i = 0; i < tar.length(); ++i) {
			int id = tar.getResourceId(i, -1);
			if (id > -1) {
				String[] array = res.getStringArray(id);
				IntegerString at = new IntegerString();
				at.setTitle(array[1]);
				at.setId(array[0]);
				mAreaArray.add(at);
			} else {
				// something wrong with the XML
			}
		}
		tar.recycle(); // Important
	}

	private void setPaymentMethodArray(Context context) {
		mPaymentMethodArray = new ArrayList<IntegerString>();
		Resources res = context.getResources();
		TypedArray tar = res.obtainTypedArray(R.array.payment_method_list);
		for (int i = 0; i < tar.length(); ++i) {
			int id = tar.getResourceId(i, -1);
			if (id > -1) {
				String[] array = res.getStringArray(id);
				IntegerString at = new IntegerString();
				at.setTitle(array[1]);
				at.setId(array[0]);
				mPaymentMethodArray.add(at);
			} else {
				// something wrong with the XML
			}
		}
		tar.recycle(); // Important
	}

	public String getAreaById(String areaCode) {
		for (IntegerString ct : mAreaArray) {
			if (ct.getId().equalsIgnoreCase(areaCode))
				return ct.getTitle();
		}
		return "";
	}

	public String getPaymentMethodById(String areaCode) {
		for (IntegerString ct : mPaymentMethodArray) {
			if (ct.getId().equalsIgnoreCase(areaCode))
				return ct.getTitle();
		}
		return "";
	}

	public List<IntegerString> getAllAreaType(Context context) {
		return getSelf(context).getAreaArrayList();
	}

	public List<IntegerString> getAllPaymentMethod(Context context) {
		return getSelf(context).getPaymentMethodArrayList();
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
							treatmentsArray.add(getTreatmentTypeForArray(array));
						}
					} else {
						treatmentsArray.add(getTreatmentTypeForArray(array));
					}
				} else {
					// something wrong with the XML
				}
			}
			ta.recycle(); // Important
			return treatmentsArray;
		}

		private static TreatmentType getTreatmentTypeForArray(String[] array) {
			TreatmentType tt = new TreatmentType();
			tt.setTreatmentId(array[0]);
			tt.setName(array[1]);
			tt.setDescription(array[2]);
			tt.setPrice(array[3]);
			return tt;
		}
	}

	public String getTreatmentName(ArrayList<TreatmentType> arr) {
		StringBuilder sb = new StringBuilder();
		if (arr.size() == 0)
			return "";
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
		setTreatmentsList(tv1, tv2, treatmentsList, true);
	}

	public void setTreatmentsList(TextView tv1, TextView tv2, ArrayList<TreatmentType> treatmentsList,
			boolean considerAmount) {
		tv1.setVisibility(View.VISIBLE);
		tv2.setVisibility(View.VISIBLE);
		StringBuilder sb1 = new StringBuilder();
		StringBuilder sb2 = new StringBuilder();
		int count = 0;
		for (TreatmentType t : treatmentsList) {
			if (Integer.valueOf(t.getAmount()) > 0 || !considerAmount) {
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
