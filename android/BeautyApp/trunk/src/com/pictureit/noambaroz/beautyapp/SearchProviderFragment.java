package com.pictureit.noambaroz.beautyapp;

import java.util.ArrayList;
import java.util.List;

import utilities.BaseFragment;
import utilities.server.HttpBase.HttpCallback;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.TextView;

import com.pictureit.noambaroz.beautyapp.customdialogs.MyCustomDialog;
import com.pictureit.noambaroz.beautyapp.data.Beautician;
import com.pictureit.noambaroz.beautyapp.data.JsonToObject;
import com.pictureit.noambaroz.beautyapp.data.TreatmentType;
import com.pictureit.noambaroz.beautyapp.helper.ServiceOrderManager;
import com.pictureit.noambaroz.beautyapp.helper.ServiceOrderManager.OnTreatmentsSelectedListener;
import com.pictureit.noambaroz.beautyapp.server.PostSearchBeautician;
import com.pictureit.noambaroz.beautyapp.server.PostVerifyAddress;

public class SearchProviderFragment extends BaseFragment {

	private final int SEARCH_STATUS_ALL_FIELDS_EMPTY = 1;
	private final int SEARCH_STATUS_NO_RESULTS = 2;

	private ServiceOrderManager mOrderMenager;

	TextView tvName, tvLocation, tvTreatmentsList1, tvTreatmentsList2;
	TextView bName, bLocation, bTreatments, bSearch;

	String stringName, stringLocation, stringTreatments1, stringTreatments2;

	private MyCustomDialog dialogName, dialogLocation;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		stringName = "";
		stringLocation = "";
		stringTreatments1 = "";
		stringTreatments2 = "";
		dialogName = getDialogSearchByName();
		dialogLocation = getDialogSearchByLocation();
		mOrderMenager = new ServiceOrderManager(getActivity());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_search_provider, container, false);

		tvName = findView(v, R.id.tv_search_provider_by_name);
		tvLocation = findView(v, R.id.tv_search_provider_by_location);
		tvTreatmentsList1 = findView(v, R.id.tv_search_provider_treatment_list2);
		tvTreatmentsList2 = findView(v, R.id.tv_search_provider_treatment_list1);
		bName = findView(v, R.id.b_search_provider_by_name);
		bLocation = findView(v, R.id.b_search_provider_by_location);
		bTreatments = findView(v, R.id.b_search_provider_search_by_treatment);
		bSearch = findView(v, R.id.tv_search_provider_search);

		return v;
	}

	@Override
	public void onResume() {
		super.onResume();
		bSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				search();
			}
		});
		bTreatments.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showTreatmentsDialog();
			}
		});
		bName.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialogName.show();
			}
		});
		bLocation.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialogLocation.show();
			}
		});
	}

	protected void showTreatmentsDialog() {
		mOrderMenager.showTreatmentSelectionDialog(null, new OnTreatmentsSelectedListener() {

			@Override
			public void onTreatmentSelected(ArrayList<TreatmentType> treatmentTypes) {
				String[] treatments = new String[treatmentTypes.size()];
				for (int i = 0; i < treatments.length; i++) {
					treatments[i] = treatmentTypes.get(i).getTreatments_id();
				}
				BeauticianUtil.setTreatmentsList(getActivity(), tvTreatmentsList1, tvTreatmentsList2, treatmentTypes);
				setRowOrder(null, null, bTreatments);
			}
		});
	}

	private MyCustomDialog getDialogSearchByName() {
		MyCustomDialog dialogName = new MyCustomDialog(getActivity());
		final EditText editText = dialogName.getEditText();
		dialogName.setDialogTitle(R.string.search_provider_search_by_name).setPositiveButton(R.string.dialog_ok_text,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						setRowOrder(tvName, editText.getText().toString(), bName);
					}
				});
		return dialogName;
	}

	private MyCustomDialog getDialogSearchByLocation() {
		MyCustomDialog dialogName = new MyCustomDialog(getActivity());
		final EditText editText = dialogName.getEditText();
		dialogName.setDialogTitle(R.string.search_provider_search_by_location).setPositiveButton(
				R.string.dialog_ok_text, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (editText.getText().toString().equalsIgnoreCase(""))
							return;
						PostVerifyAddress httpPost = new PostVerifyAddress(getActivity(), new HttpCallback() {

							@Override
							public void onAnswerReturn(Object answer) {
								setRowOrder(tvLocation, (String) answer, bLocation);
							}
						}, editText.getText().toString());
						httpPost.execute();
					}
				});
		return dialogName;
	}

	private void search() {
		if (!(!tvName.getText().toString().equalsIgnoreCase("")
				|| !tvLocation.getText().toString().equalsIgnoreCase("") || !tvTreatmentsList1.getText().toString()
				.equalsIgnoreCase(""))) {
			onSearchFailed(SEARCH_STATUS_ALL_FIELDS_EMPTY);
			return;
		}

		stringName = tvName.getText().toString();
		stringLocation = tvLocation.getText().toString();
		stringTreatments1 = tvTreatmentsList1.getText().toString();
		stringTreatments2 = tvTreatmentsList2.getText().toString();
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
		}, stringName, stringLocation, mOrderMenager.getTreatment().tretments);
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

	private void setRowOrder(TextView textView, String text, TextView button) {
		button.getLayoutParams().width = LayoutParams.WRAP_CONTENT;
		button.getLayoutParams().height = LayoutParams.WRAP_CONTENT;
		button.setBackgroundResource(R.drawable.btn_edit);
		button.setText(null);
		button.setTextSize(0);
		if (textView != null) {
			textView.setVisibility(View.VISIBLE);
			textView.setText(text);
		}
	}

}
