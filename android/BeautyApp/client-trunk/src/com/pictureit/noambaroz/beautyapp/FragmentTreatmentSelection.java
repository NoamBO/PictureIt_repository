package com.pictureit.noambaroz.beautyapp;

import java.util.ArrayList;

import utilities.view.MyNumberPicker;
import utilities.view.MyNumberPicker.onValueChangeListener;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.TextView;

import com.pictureit.noambaroz.beautyapp.customdialogs.MyCustomDialog;
import com.pictureit.noambaroz.beautyapp.data.StringArrays;
import com.pictureit.noambaroz.beautyapp.data.TreatmentType;
import com.pictureit.noambaroz.beautyapp.helper.ServiceOrderManager.OnTreatmentsSelectedListener;

public class FragmentTreatmentSelection extends Fragment {

	ListView mListView;
	ArrayList<TreatmentType> treatmentsArrayList;
	private OnTreatmentsSelectedListener mListener;
	private String[] treatmentStringArray;
	private ViewGroup buttonConfirm;
	private boolean isActivitySearchProvider;

	public FragmentTreatmentSelection() {
	}

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

		if (getActivity() instanceof SearchProviderActivity) {
			isActivitySearchProvider = true;
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.dialog_select_treatment_layout, container, false);
		mListView = (ListView) v.findViewById(R.id.lv_treatment_selection);
		buttonConfirm = (ViewGroup) v.findViewById(R.id.vg_treatment_selection_ok);
		return v;
	}

	@Override
	public void onResume() {
		super.onResume();
		MyAdapter adapter = new MyAdapter();
		mListView.setAdapter(adapter);
		buttonConfirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getActivity().onBackPressed();
			}
		});
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
		public View getView(final int position, View convertView, ViewGroup parent) {
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
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.tvDescription.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					showDescription(getItem(position));
				}
			});
			if (isActivitySearchProvider)
				holder.picker.setVisibility(View.INVISIBLE);

			holder.tvName.setText(getItem(position).getName());

			holder.picker.setValue(getItem(position).getAmount());
			holder.checkBox.setOnCheckedChangeListener(null);
			holder.checkBox.setChecked(getItem(position).getAmount() != 0 ? true : false);
			holder.picker.addOnValueChangeListener(new onValueChangeListener() {

				@Override
				public void onValueChange(int value) {
					getItem(position).setAmount(value);
					notifyDataSetChanged();
				}
			});
			holder.checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					if (isChecked) {
						if (holder.picker.getValue() == 0)
							getItem(position).setAmount(1);
					} else
						getItem(position).setAmount(0);
					notifyDataSetChanged();
				}
			});

			return convertView;
		}

		protected void showDescription(TreatmentType treatmentType) {
			StringBuilder messageBuilder = new StringBuilder();
			messageBuilder.append(getString(R.string.price));
			messageBuilder.append(" ");
			messageBuilder.append(treatmentType.getPrice());
			messageBuilder.append(" ");
			messageBuilder.append(getString(R.string.currency));
			new MyCustomDialog(getActivity()).setDialogTitle(treatmentType.getName())
					.setIcon(android.R.drawable.ic_dialog_info).setMessage(treatmentType.getDescription())
					.setSubMessage(messageBuilder.toString()).show();
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
