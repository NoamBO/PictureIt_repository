package com.pictureit.noambaroz.beautyapp;

import android.app.AlertDialog;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pictureit.noambaroz.beautyapp.data.TreatmentType;
import com.pictureit.noambaroz.beautyapp.helper.ServiceOrderManager;

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

	private class OrderServiceFragment extends Fragment implements
			OnClickListener {

		private TextView tvFor, tvWhen, tvTreatmentSelection, tvLocation,
				tvRemarks, tvOrder;
		private ServiceOrderManager mManager;

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			mManager = new ServiceOrderManager(ServiceOrder.this);
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View v = inflater.inflate(R.layout.order_service_layout, container,
					false);
			tvFor = findView(v, R.id.tv_service_order_for);
			tvWhen = findView(v, R.id.tv_service_order_when);
			tvTreatmentSelection = findView(v,
					R.id.tv_service_order_select_treatment);
			tvLocation = findView(v, R.id.tv_service_order_location);
			tvRemarks = findView(v, R.id.tv_service_order_remarks);
			tvOrder = findView(v, R.id.tv_service_order_one);
			return v;
		}

		@Override
		public void onResume() {
			super.onResume();
			initListeners();
		}

		private void initListeners() {
			tvFor.setOnClickListener(this);
			tvWhen.setOnClickListener(this);
			tvRemarks.setOnClickListener(this);
			tvTreatmentSelection.setOnClickListener(this);
			tvLocation.setOnClickListener(this);
			tvOrder.setOnClickListener(this);
		}

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.tv_service_order_for:
				mManager.showFORDialog();
				break;
			case R.id.tv_service_order_when:
				mManager.showWHENDialog();
				break;
			case R.id.tv_service_order_select_treatment:
				mManager.showTreatmentSelectionDialog();
				break;
			case R.id.tv_service_order_location:
				mManager.showLocationDialog();
				break;
			case R.id.tv_service_order_remarks:
				mManager.showRemarksDialog();
				break;
			case R.id.tv_service_order_one:
				order();
				break;

			default:
				break;
			}
		}

		public void order() {
			if (!isOrderOk()) {
				new AlertDialog.Builder(getActivity())
						.setTitle("Can't procceed")
						.setMessage("Something is missing")
						.setPositiveButton(R.string.dialog_ok_text, null)
						.create().show();
				return;
			}
			ServiceSummaryFragment f = new ServiceSummaryFragment();
			f.setTreatment(mManager.getTreatment());
			getActivity().getFragmentManager().beginTransaction()
					.replace(FRAGMENT_CONTAINER, f).addToBackStack(null)
					.commit();
		}

		public boolean isOrderOk() {
			boolean isOk = true;
			if (mManager.getTreatment().forWho == null
					|| mManager.getTreatment().forWho.equalsIgnoreCase("")) {
				isOk = false;
				tvFor.setTextColor(getResources().getColor(
						R.color.text_color_un_filled));
			} else {
				tvFor.setTextColor(getResources().getColor(
						R.color.text_color_filled));
			}
			if (mManager.getTreatment().whare == null
					|| mManager.getTreatment().whare.equalsIgnoreCase("")) {
				isOk = false;
				tvLocation.setTextColor(getResources().getColor(
						R.color.text_color_un_filled));
			} else {
				tvLocation.setTextColor(getResources().getColor(
						R.color.text_color_filled));
			}
			if (mManager.getTreatment().when == null
					|| mManager.getTreatment().when.equalsIgnoreCase("")) {
				isOk = false;
				tvWhen.setTextColor(getResources().getColor(
						R.color.text_color_un_filled));
			} else {
				tvWhen.setTextColor(getResources().getColor(
						R.color.text_color_filled));
			}
			if (mManager.getTreatment().tretments != null) {
				boolean temp = false;
				for (TreatmentType t : mManager.getTreatment().tretments) {
					if (Integer.valueOf(t.getCount()) > 0)
						temp = true;
				}
				if (!temp) {
					isOk = false;
					tvTreatmentSelection.setTextColor(getResources().getColor(
							R.color.text_color_un_filled));
				} else {
					tvTreatmentSelection.setTextColor(getResources().getColor(
							R.color.text_color_filled));
				}
			} else {
				isOk = false;
				tvTreatmentSelection.setTextColor(getResources().getColor(
						R.color.text_color_un_filled));
			}
			return isOk;
		}

	}

}
