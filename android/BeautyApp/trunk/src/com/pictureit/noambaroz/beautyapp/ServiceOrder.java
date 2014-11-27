package com.pictureit.noambaroz.beautyapp;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

import com.pictureit.noambaroz.beautyapp.data.Constant;
import com.pictureit.noambaroz.beautyapp.data.TreatmentType;
import com.pictureit.noambaroz.beautyapp.helper.ServiceOrderManager;
import com.pictureit.noambaroz.beautyapp.helper.ServiceOrderManager.OnTreatmentsSelectedListener;

public class ServiceOrder extends ActivityWithFragment {

	@Override
	protected void initActivity() {
		// initActionBar();
	}

	@Override
	protected void setFragment() {
		fragment = new OrderServiceFragment();
	}

	@Override
	protected void setFragmentTag() {
		FRAGMENT_TAG = "order_service_fragmrnt";
	}

	@Override
	protected void onResume() {
		super.onResume();
		Bundle bundle = getIntent().getExtras();
		if (bundle != null && !bundle.isEmpty()) {
			String beauticianId = bundle.getString(Constant.EXTRA_BEAUTICIAN_ID, null);
			String beauticianName = bundle.getString(Constant.EXTRA_BEAUTICIAN_NAME, "");
			String[] treatmentStringArray = bundle.getStringArray(Constant.EXTRA_BEAUTICIAN_TREATMENT_STRING_ARRAY);
			((OrderServiceFragment) fragment).setBeauticianIdNameAndTreatments(beauticianId, beauticianName,
					treatmentStringArray);
		}
	}

	public interface OnFieldChangeListener {
		public void onFieldChange(String result);
	}

	private class OrderServiceFragment extends Fragment implements OnClickListener {

		public void setBeauticianIdNameAndTreatments(String beauticianId, String beauticianName,
				String[] treatmentStringArray) {
			this.beauticianId = beauticianId;
			this.beauticianName = beauticianName;
			this.treatmentStringArray = treatmentStringArray;
		}

		private String beauticianId;
		private String beauticianName;
		private String[] treatmentStringArray;

		private TextView tvFor, tvWhen, tvTreatmentSelection, tvLocation, tvRemarks, tvOrder;
		private TextView bFor, bWhen, bTreatmentSelection, bLocation, bRemarks;
		private TextView tvTreatmentsList1, tvTreatmentsList2;
		private ServiceOrderManager mManager;

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			mManager = new ServiceOrderManager(ServiceOrder.this);
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
						setRowOrder(tvFor, result, bFor);
					}
				});
				break;
			case R.id.b_service_order_when:
				mManager.showWHENDialog(new OnFieldChangeListener() {
					@Override
					public void onFieldChange(String result) {
						setRowOrder(tvWhen, result, bWhen);
					}
				});
				break;
			case R.id.b_service_order_select_treatment:
				mManager.showTreatmentSelectionDialog(treatmentStringArray, new OnTreatmentsSelectedListener() {

					@Override
					public void onTreatmentSelected(ArrayList<TreatmentType> tl) {
						BeauticianUtil.setTreatmentsList(getActivity(), tvTreatmentsList1, tvTreatmentsList2, tl);
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

		public void order() {
			if (!isOrderOk()) {
				new AlertDialog.Builder(getActivity()).setTitle(getResources().getString(R.string.dialog_title_error))
						.setMessage(getResources().getString(R.string.dialog_error_message_must_fill_all_feilde))
						.setPositiveButton(R.string.dialog_ok_text, null).create().show();
				return;
			}
			mManager.placeOrder(beauticianId);
			// ServiceSummaryFragment f = new ServiceSummaryFragment();
			// f.setTreatment(mManager.getTreatment());
			// getActivity().getFragmentManager().beginTransaction().replace(FRAGMENT_CONTAINER,
			// f).addToBackStack(null)
			// .commit();
		}

		public boolean isOrderOk() {
			boolean isOk = true;
			int colorFilled = getResources().getColor(R.color.text_color_filled);
			int colorUnfilled = getResources().getColor(R.color.text_color_un_filled);
			if (mManager.getTreatment().forWho == null || mManager.getTreatment().forWho.equalsIgnoreCase("")) {
				isOk = false;
				tvFor.setTextColor(colorUnfilled);
			} else {
				tvFor.setTextColor(colorFilled);
			}
			if (mManager.getTreatment().whare == null || mManager.getTreatment().whare.equalsIgnoreCase("")) {
				isOk = false;
				tvLocation.setTextColor(colorUnfilled);
			} else {
				tvLocation.setTextColor(colorFilled);
			}
			if (mManager.getTreatment().when == null || mManager.getTreatment().when.equalsIgnoreCase("")) {
				isOk = false;
				tvWhen.setTextColor(colorUnfilled);
			} else {
				tvWhen.setTextColor(colorFilled);
			}
			if (mManager.getTreatment().tretments != null) {
				boolean temp = false;
				for (TreatmentType t : mManager.getTreatment().tretments) {
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

}
