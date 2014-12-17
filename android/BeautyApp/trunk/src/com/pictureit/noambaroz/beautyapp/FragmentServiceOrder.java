package com.pictureit.noambaroz.beautyapp;

import java.util.ArrayList;

import utilities.BaseFragment;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

import com.pictureit.noambaroz.beautyapp.ServiceOrder.OnFieldChangeListener;
import com.pictureit.noambaroz.beautyapp.customdialogs.MyCustomDialog;
import com.pictureit.noambaroz.beautyapp.data.TreatmentType;
import com.pictureit.noambaroz.beautyapp.helper.ServiceOrderManager;
import com.pictureit.noambaroz.beautyapp.helper.ServiceOrderManager.OnTreatmentsSelectedListener;

public class FragmentServiceOrder extends BaseFragment implements OnClickListener {

	public FragmentServiceOrder() {
	}

	public void setBeauticianIdNameAndTreatments(String beauticianId, String beauticianName,
			String[] treatmentStringArray) {
		this.beauticianId = beauticianId;
		this.beauticianName = beauticianName;
		this.treatmentStringArray = treatmentStringArray;
	}

	protected String beauticianId;
	private String beauticianName;
	private String[] treatmentStringArray;

	protected TextView tvFor, tvWhen, tvLocation, tvRemarks, tvOrder;
	protected TextView bFor, bWhen, bTreatmentSelection, bLocation, bRemarks;
	protected TextView tvTreatmentsList1, tvTreatmentsList2;

	protected ServiceOrderManager mManager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (mManager == null)
			mManager = new ServiceOrderManager(getActivity());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.order_service_layout, container, false);
		tvFor = findView(v, R.id.tv_service_order_for);
		bFor = findView(v, R.id.b_service_order_for);

		tvWhen = findView(v, R.id.tv_service_order_when);
		bWhen = findView(v, R.id.b_service_order_when);

		bTreatmentSelection = findView(v, R.id.b_service_order_select_treatment);
		tvTreatmentsList1 = findView(v, R.id.tv_service_order_treatment_list2);
		tvTreatmentsList2 = findView(v, R.id.tv_service_order_treatment_list1);

		tvLocation = findView(v, R.id.tv_service_order_location);
		bLocation = findView(v, R.id.b_service_order_location);

		tvRemarks = findView(v, R.id.tv_service_order_remarks);
		bRemarks = findView(v, R.id.b_service_order_remarks);

		tvOrder = findView(v, R.id.tv_service_order_one);
		return v;
	}

	@Override
	public void onResume() {
		super.onResume();
		initListeners();
	}

	private void initListeners() {
		bFor.setOnClickListener(this);
		bWhen.setOnClickListener(this);
		bLocation.setOnClickListener(this);
		bRemarks.setOnClickListener(this);
		bTreatmentSelection.setOnClickListener(this);
		tvOrder.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.b_service_order_for:
			mManager.showFORDialog(new OnFieldChangeListener() {
				@Override
				public void onFieldChange(String result) {
					if (result.equalsIgnoreCase(""))
						setRowOrder(tvFor, result, bFor, getResources().getString(R.string.who_the_service_for),
								R.id.dividerServiceOrderFor);
					else
						setRowOrder(tvFor, result, bFor, R.id.dividerServiceOrderFor);
				}
			});
			break;
		case R.id.b_service_order_when:
			mManager.showWHENDialog(new OnFieldChangeListener() {
				@Override
				public void onFieldChange(String result) {
					if (result.equalsIgnoreCase(""))
						setRowOrder(tvWhen, result, bWhen, getResources().getString(R.string.when_the_service_happen),
								R.id.dividerServiceOrderWhen);
					else
						setRowOrder(tvWhen, result, bWhen, R.id.dividerServiceOrderWhen);
				}
			});
			break;
		case R.id.b_service_order_select_treatment:
			mManager.showTreatmentSelectionDialog(treatmentStringArray, new OnTreatmentsSelectedListener() {

				@Override
				public void onTreatmentSelected(ArrayList<TreatmentType> tl) {
					BeauticianUtil.setTreatmentsList(getActivity(), tvTreatmentsList1, tvTreatmentsList2, tl);
					if (tvTreatmentsList1.getText().toString().equalsIgnoreCase("")
							&& tvTreatmentsList2.getText().toString().equalsIgnoreCase(""))
						setRowOrder(null, null, bTreatmentSelection, getString(R.string.select_treatment),
								R.id.dividerServiceOrderSelectTreatment);
					else
						setRowOrder(null, null, bTreatmentSelection, R.id.dividerServiceOrderSelectTreatment);
				}
			});
			break;
		case R.id.b_service_order_location:
			mManager.showLocationDialog(new OnFieldChangeListener() {

				@Override
				public void onFieldChange(String result) {
					if (result.equalsIgnoreCase(""))
						setRowOrder(tvLocation, result, bLocation, getResources().getString(R.string.select_location),
								R.id.dividerServiceOrderWhen);
					else
						setRowOrder(tvLocation, result, bLocation, R.id.dividerServiceOrderLocation);
				}
			});
			break;
		case R.id.b_service_order_remarks:
			mManager.showRemarksDialog(new OnFieldChangeListener() {

				@Override
				public void onFieldChange(String result) {
					setRowOrder(tvRemarks, result, bRemarks, -1);
				}
			});
			break;
		case R.id.tv_service_order_one:
			order();
			break;

		default:
			break;
		}
	}

	protected void setRowOrder(TextView textView, String text, TextView button, int dividerResId) {
		setRowOrder(textView, text, button, null, dividerResId);
	}

	protected void setRowOrder(TextView textView, String text, TextView button, String buttonText, int dividerResId) {
		if (buttonText != null) {
			if (dividerResId != -1)
				findView(getView(), dividerResId).setVisibility(View.GONE);
			button.getLayoutParams().width = LayoutParams.MATCH_PARENT;
			button.getLayoutParams().height = LayoutParams.WRAP_CONTENT;
			button.setBackgroundResource(R.drawable.btn_select_shape);
			button.setText(buttonText);
			button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);

			if (textView != null)
				textView.setVisibility(View.GONE);

			return;
		}

		if (dividerResId != -1)
			findView(getView(), dividerResId).setVisibility(View.VISIBLE);
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

	public void order() {
		if (!isOrderOk()) {

			new MyCustomDialog(getActivity()).setDialogTitle(R.string.dialog_title_error)
					.setMessage(R.string.dialog_error_message_must_fill_all_feilde)
					.setPositiveButton(R.string.dialog_ok_text, null).show();

			return;
		}
		mManager.placeOrder(beauticianId);
	}

	public boolean isOrderOk() {
		boolean isOk = true;
		if (mManager.getTreatment().forwho == null || mManager.getTreatment().forwho.equalsIgnoreCase(""))
			isOk = false;
		if (mManager.getTreatment().location == null || mManager.getTreatment().location.equalsIgnoreCase(""))
			isOk = false;
		if (mManager.getTreatment().date == null || mManager.getTreatment().date.equalsIgnoreCase(""))
			isOk = false;
		if (mManager.getTreatment().treatments != null) {
			boolean temp = false;
			for (TreatmentType t : mManager.getTreatment().treatments) {
				if (Integer.valueOf(t.getAmount()) > 0)
					temp = true;
			}
			if (!temp)
				isOk = false;
		} else
			isOk = false;
		return isOk;
	}

}
