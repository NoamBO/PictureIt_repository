package com.pictureit.noambaroz.beautyapp;

import java.util.ArrayList;
import java.util.List;

import utilities.BaseFragment;
import utilities.server.HttpBase.HttpCallback;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.pictureit.noambaroz.beautyapp.data.Beautician;
import com.pictureit.noambaroz.beautyapp.data.JsonToObject;
import com.pictureit.noambaroz.beautyapp.server.PostSearchBeautician;

public class SearchProviderFragment extends BaseFragment {

	private final int SEARCH_STATUS_ALL_FIELDS_EMPTY = 1;
	private final int SEARCH_STATUS_NO_RESULTS = 2;

	EditText etName, etLocation, etType, etServiceType;
	CheckBox cbName, cbLocation, cbType, cbServiceType;
	TextView tvSearch;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_search_provider, container, false);

		etName = findView(v, R.id.et_search_provider_search_by_name);
		etLocation = findView(v, R.id.et_search_provider_search_by_location);
		etType = findView(v, R.id.et_search_provider_search_by_type);
		etServiceType = findView(v, R.id.et_search_provider_search_by_services_type);
		cbName = findView(v, R.id.cb_search_provider_search_by_name);
		cbLocation = findView(v, R.id.cb_search_provider_search_by_location);
		cbType = findView(v, R.id.cb_search_provider_search_by_type);
		cbServiceType = findView(v, R.id.cb_search_provider_search_by_services_type);
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
	}

	private void search() {
		if (!(!etName.getText().toString().equalsIgnoreCase("")
				|| !etLocation.getText().toString().equalsIgnoreCase("")
				|| !etType.getText().toString().equalsIgnoreCase("") || !etServiceType.getText().toString()
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
		}, etName.getText().toString(), etLocation.getText().toString(), etType.getText().toString(), etServiceType
				.getText().toString());
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
}
