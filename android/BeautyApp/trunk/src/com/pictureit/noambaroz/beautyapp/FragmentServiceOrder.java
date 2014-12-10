package com.pictureit.noambaroz.beautyapp;

import java.util.ArrayList;

import utilities.BaseFragment;
import android.app.AlertDialog;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

import com.pictureit.noambaroz.beautyapp.ServiceOrder.OnFieldChangeListener;
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

	protected TextView tvFor, tvWhen, tvTreatmentSelection, tvLocation, tvRemarks, tvOrder;
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

		tvTreatmentSelection = findView(v, R.id.tv_service_order_select_treatment);
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
						setRowOrder(tvFor, result, bFor, getResources().getString(R.string.who_the_service_for));
					else
						setRowOrder(tvFor, result, bFor);
				}
			});
			break;
		case R.id.b_service_order_when:
			mManager.showWHENDialog(new OnFieldChangeListener() {
				@Override
				public void onFieldChange(String result) {
					if (result.equalsIgnoreCase(""))
						setRowOrder(tvWhen, result, bWhen, getResources().getString(R.string.when_the_service_happen));
					else
						setRowOrder(tvWhen, result, bWhen);
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
						setRowOrder(null, null, bTreatmentSelection, getString(R.string.select_treatment));
					else
						setRowOrder(null, null, bTreatmentSelection);
				}
			});
			break;
		case R.id.b_service_order_location:
			mManager.showLocationDialog(new OnFieldChangeListener() {

				@Override
				public void onFieldChange(String result) {
					setRowOrder(tvLocation, result, bLocation);
				}
			});
			break;
		case R.id.b_service_order_remarks:
			mManager.showRemarksDialog(new OnFieldChangeListener() {

				@Override
				public void onFieldChange(String result) {
					setRowOrder(tvRemarks, result, bRemarks);
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

	protected void setRowOrder(TextView textView, String text, TextView button) {
		setRowOrder(textView, text, button, null);
	}

	protected void setRowOrder(TextView textView, String text, TextView button, String buttonText) {
		if (buttonText != null) {
			button.getLayoutParams().width = LayoutParams.MATCH_PARENT;
			button.getLayoutParams().height = LayoutParams.WRAP_CONTENT;
			button.setBackgroundResource(R.drawable.btn_shape);
			button.setText(buttonText);
			button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);

			if (textView != null)
				textView.setVisibility(View.GONE);

			return;
		}

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
			new AlertDialog.Builder(getActivity()).setTitle(getResources().getString(R.string.dialog_title_error))
					.setMessage(getResources().getString(R.string.dialog_error_message_must_fill_all_feilde))
					.setPositiveButton(R.string.dialog_ok_text, null).create().show();
			return;
		}
		mManager.placeOrder(beauticianId);
	}

	public boolean isOrderOk() {
		boolean isOk = true;
		int colorFilled = getResources().getColor(R.color.text_color_filled);
		int colorUnfilled = getResources().getColor(R.color.text_color_un_filled);
		if (mManager.getTreatment().forwho == null || mManager.getTreatment().forwho.equalsIgnoreCase("")) {
			isOk = false;
			tvFor.setTextColor(colorUnfilled);
		} else {
			tvFor.setTextColor(colorFilled);
		}
		if (mManager.getTreatment().location == null || mManager.getTreatment().location.equalsIgnoreCase("")) {
			isOk = false;
			tvLocation.setTextColor(colorUnfilled);
		} else {
			tvLocation.setTextColor(colorFilled);
		}
		if (mManager.getTreatment().date == null || mManager.getTreatment().date.equalsIgnoreCase("")) {
			isOk = false;
			tvWhen.setTextColor(colorUnfilled);
		} else {
			tvWhen.setTextColor(colorFilled);
		}
		if (mManager.getTreatment().treatments != null) {
			boolean temp = false;
			for (TreatmentType t : mManager.getTreatment().treatments) {
				if (Integer.valueOf(t.getAmount()) > 0)
					temp = true;
			}
			if (!temp) {
				isOk = false;
				tvTreatmentSelection.setTextColor(colorUnfilled);
			} else {
				tvTreatmentSelection.setTextColor(colorFilled);
			}
		} else {
			isOk = false;
			tvTreatmentSelection.setTextColor(colorUnfilled);
		}
		return isOk;
	}

}
