package com.pictureit.noambaroz.beautyapp;

import java.util.ArrayList;
import java.util.List;

import utilities.BaseFragment;
import utilities.server.HttpBase.HttpCallback;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.pictureit.noambaroz.beautyapp.data.Beautician;
import com.pictureit.noambaroz.beautyapp.data.ClassificationType;
import com.pictureit.noambaroz.beautyapp.data.JsonToObject;
import com.pictureit.noambaroz.beautyapp.data.StringArrays;
import com.pictureit.noambaroz.beautyapp.data.TreatmentType;
import com.pictureit.noambaroz.beautyapp.server.PostSearchBeautician;

public class SearchProviderFragment extends BaseFragment {

	private final int SEARCH_STATUS_ALL_FIELDS_EMPTY = 1;
	private final int SEARCH_STATUS_NO_RESULTS = 2;

	EditText etName;
	Button bType, bServiceType;
	AutoCompleteTextView etLocation;
	TextView tvSearch;

	String mServiceType, mClassification = "";

	private Dialog serviceTypeDialog;
	private Dialog classificationTypeDialog;
	private ServiceTypeListAdapter serviceTypeListAdapter;
	private ClassificationListAdapter classificationListAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		serviceTypeDialog = getServiceTypeDialog();
		classificationTypeDialog = getClassificationTypeDialog();
	}

	private Dialog getClassificationTypeDialog() {
		List<ClassificationType> list = StringArrays.getAllClassificationType(getActivity());
		classificationListAdapter = new ClassificationListAdapter(getActivity(), android.R.layout.simple_list_item_2,
				list);
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(R.string.search_provider_search_by_type);
		builder.setSingleChoiceItems(classificationListAdapter, 0, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				ClassificationType t = classificationListAdapter.getItem(which);
				classificationListAdapter.setSelected(t.getId());
				classificationListAdapter.notifyDataSetChanged();
				mClassification = t.getTitle();
				bType.setText(mClassification);
				dialog.dismiss();
			}
		});
		return builder.create();
	}

	private Dialog getServiceTypeDialog() {
		List<TreatmentType> list = StringArrays.getAllTreatmentsType(getActivity());
		serviceTypeListAdapter = new ServiceTypeListAdapter(getActivity(), android.R.layout.simple_list_item_2, list);
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(R.string.service_select_treatment);
		builder.setSingleChoiceItems(serviceTypeListAdapter, 0, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				TreatmentType t = serviceTypeListAdapter.getItem(which);
				serviceTypeListAdapter.setSelected(t.getTreatments_id());
				serviceTypeListAdapter.notifyDataSetChanged();
				mServiceType = t.getName();
				bServiceType.setText(mServiceType);
				serviceTypeDialog.dismiss();
			}
		});
		return builder.create();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_search_provider, container, false);

		ArrayAdapter<String> locationsAdapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.select_dialog_item, getResources().getStringArray(R.array.israel_cities));
		etName = findView(v, R.id.et_search_provider_search_by_name);
		etLocation = findView(v, R.id.et_search_provider_search_by_location);
		etLocation.setThreshold(1);
		etLocation.setAdapter(locationsAdapter);
		bType = findView(v, R.id.b_search_provider_search_by_type);
		bServiceType = findView(v, R.id.b_search_provider_search_by_services_type);
		tvSearch = findView(v, R.id.tv_search_provider_search);

		return v;
	}

	@Override
	public void onResume() {
		super.onResume();
		tvSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				search();

			}
		});
		bServiceType.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				serviceTypeDialog.show();
			}
		});
		bType.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				classificationTypeDialog.show();
			}
		});
	}

	private void search() {
		if (!(!etName.getText().toString().equalsIgnoreCase("")
				|| !etLocation.getText().toString().equalsIgnoreCase("") || !mClassification.equalsIgnoreCase("") || !mServiceType
					.equalsIgnoreCase(""))) {
			onSearchFailed(SEARCH_STATUS_ALL_FIELDS_EMPTY);
			return;
		}
		PostSearchBeautician post = new PostSearchBeautician(getActivity(), new HttpCallback() {

			@Override
			public void onAnswerReturn(Object answer) {
				if (answer != null) {
					List<Beautician> result = JsonToObject.convertObjectToBeauticianArrayList(answer);
					if (result.size() > 0) {
						startResultsFragment(result);
						return;
					}
				}
				onSearchFailed(SEARCH_STATUS_NO_RESULTS);
			}
		}, etName.getText().toString(), etLocation.getText().toString(), classificationListAdapter.getSelected(),
				serviceTypeListAdapter.getSelected());
		post.execute();
	}

	protected void startResultsFragment(List<Beautician> result) {
		SearchProviderResultsFragment fragment = new SearchProviderResultsFragment();
		fragment.putBeauticianList((ArrayList<Beautician>) result);
		getFragmentManager().beginTransaction()
				.replace(((ActivityWithFragment) getActivity()).FRAGMENT_CONTAINER, fragment).addToBackStack(null)
				.commit();
	}

	private void onSearchFailed(int status) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(R.string.search_provider_error_dialog_title);
		switch (status) {
		case SEARCH_STATUS_ALL_FIELDS_EMPTY:
			builder.setMessage(R.string.dialog_error_message_all_feilds_are_empty);
			break;
		case SEARCH_STATUS_NO_RESULTS:
			builder.setMessage(R.string.search_provider_error_dialog_no_results);
			break;
		}
		builder.setPositiveButton(R.string.dialog_ok_text, null);
		builder.create().show();
	}

	private class ServiceTypeListAdapter extends ArrayAdapter<TreatmentType> {
		public ServiceTypeListAdapter(Context context, int resource, List<TreatmentType> objects) {
			super(context, resource, objects);
		}

		public void setSelected(String which) {
			selected = which;
		}

		public String getSelected() {
			return selected;
		}

		private String selected;

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final ServiceTypeListAdapterHolder holder;
			if (convertView == null) {
				holder = new ServiceTypeListAdapterHolder();
				LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(
						Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.row_single_choice_dialog, parent, false);
				holder.textView = findView(convertView, R.id.row_single_choise_textview);
				holder.radio = findView(convertView, R.id.row_single_choise_radio);
				convertView.setTag(holder);
			} else {
				holder = (ServiceTypeListAdapterHolder) convertView.getTag();
			}
			holder.radio.setChecked(getItem(position).getTreatments_id().equalsIgnoreCase(selected) ? true : false);
			holder.textView.setText(getItem(position).getName());
			return convertView;
		}

	}

	private class ClassificationListAdapter extends ArrayAdapter<ClassificationType> {
		public ClassificationListAdapter(Context context, int resource, List<ClassificationType> objects) {
			super(context, resource, objects);
		}

		public void setSelected(String which) {
			selected = which;
		}

		public String getSelected() {
			return selected;
		}

		private String selected;

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final ServiceTypeListAdapterHolder holder;
			if (convertView == null) {
				holder = new ServiceTypeListAdapterHolder();
				LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(
						Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.row_single_choice_dialog, parent, false);
				holder.textView = findView(convertView, R.id.row_single_choise_textview);
				holder.radio = findView(convertView, R.id.row_single_choise_radio);
				convertView.setTag(holder);
			} else {
				holder = (ServiceTypeListAdapterHolder) convertView.getTag();
			}
			holder.radio.setChecked(getItem(position).getId().equalsIgnoreCase(selected) ? true : false);
			holder.textView.setText(getItem(position).getTitle());
			return convertView;
		}

	}

	static class ServiceTypeListAdapterHolder {
		TextView textView;
		RadioButton radio;
	}
}
