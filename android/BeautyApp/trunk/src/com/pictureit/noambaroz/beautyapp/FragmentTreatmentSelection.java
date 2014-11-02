package com.pictureit.noambaroz.beautyapp;

import java.util.ArrayList;

import utilities.view.MyNumberPicker;
import utilities.view.MyNumberPicker.onValueChangeListener;
import android.app.AlertDialog;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.pictureit.noambaroz.beautyapp.data.StringArrays;
import com.pictureit.noambaroz.beautyapp.data.TreatmentType;
import com.pictureit.noambaroz.beautyapp.helper.ServiceOrderManager.OnTreatmentsSelectedListener;

public class FragmentTreatmentSelection extends Fragment {

	ListView mListView;
	ArrayList<TreatmentType> treatmentsArrayList;
	private OnTreatmentsSelectedListener mListener;
	private String[] treatmentStringArray;

	@Override
	public void onDetach() {
		if (mListener != null)
			mListener.onTreatmentSelected(treatmentsArrayList);
		super.onDetach();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (treatmentsArrayList == null)
			treatmentsArrayList = StringArrays.TreatmentList.genarate(getActivity(), treatmentStringArray);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.dialog_select_treatment_layout, container, false);
		mListView = (ListView) v.findViewById(R.id.lv_treatment_selection);
		return v;
	}

	@Override
	public void onResume() {
		super.onResume();
		MyAdapter adapter = new MyAdapter();
		mListView.setAdapter(adapter);
	}

	private class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return treatmentsArrayList.size();
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = getActivity().getLayoutInflater().inflate(R.layout.dialog_select_treatment_row, parent,
						false);

				holder.tvDescription = (TextView) convertView
						.findViewById(R.id.tv_row_treatment_selection_additional_data);
				holder.tvName = (TextView) convertView.findViewById(R.id.tv_row_treatment_selection);
				holder.picker = (MyNumberPicker) convertView.findViewById(R.id.np_row_treatment_selection);
				holder.checkBox = (CheckBox) convertView.findViewById(R.id.cb_row_treatment_selection);
				holder.checkBox.setEnabled(false);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			final TreatmentType item = getItem(position);
			holder.tvDescription.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					showDescription(item);
				}
			});
			holder.tvName.setText(item.getName());

			holder.picker.setValue(item.getAmount());
			holder.checkBox.setChecked(item.getAmount() != 0 ? true : false);
			holder.picker.addOnValueChangeListener(new onValueChangeListener() {

				@Override
				public void onValueChange(int value) {
					item.setAmount(value);
					notifyDataSetChanged();
				}
			});

			return convertView;
		}

		protected void showDescription(TreatmentType treatmentType) {
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setTitle(treatmentType.getName());
			builder.setIcon(android.R.drawable.ic_dialog_info);
			StringBuilder sb = new StringBuilder();
			sb.append(treatmentType.getDescription());
			sb.append("\n");
			sb.append("מחיר: ");
			sb.append(treatmentType.getPrice());
			builder.setMessage(sb.toString());
			builder.create().show();
		}

		@Override
		public TreatmentType getItem(int position) {
			return treatmentsArrayList.get(position);
		}

	}

	private static class ViewHolder {
		MyNumberPicker picker;
		TextView tvDescription;
		TextView tvName;
		CheckBox checkBox;
	}

	public void setListener(OnTreatmentsSelectedListener l) {
		mListener = l;
	}

	public void setTreatments(ArrayList<TreatmentType> array) {
		treatmentsArrayList = array;
	}

	public void putTreatments(String[] treatments) {
		treatmentStringArray = treatments;
	}
}
