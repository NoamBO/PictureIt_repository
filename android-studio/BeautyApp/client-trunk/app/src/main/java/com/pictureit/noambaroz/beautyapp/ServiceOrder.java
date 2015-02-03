package com.pictureit.noambaroz.beautyapp;

import android.os.Bundle;
import android.widget.Toast;

import com.pictureit.noambaroz.beautyapp.data.Constants;
import com.pictureit.noambaroz.beautyapp.helper.ServiceOrderManager;

public class ServiceOrder extends ActivityWithFragment {

	@Override
	protected void initActivity() {
		if (ServiceOrderManager.isPending(getApplicationContext())) {
			Toast.makeText(getApplicationContext(), R.string.pending_order_toast, Toast.LENGTH_LONG).show();
			backPressed();
		}
	}

	@Override
	protected void setFragment() {
		fragment = new FragmentServiceOrder();
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
			String beauticianId = bundle.getString(Constants.EXTRA_BEAUTICIAN_ID, null);
			String beauticianName = bundle.getString(Constants.EXTRA_BEAUTICIAN_NAME, "");
			String[] treatmentStringArray = bundle.getStringArray(Constants.EXTRA_BEAUTICIAN_TREATMENT_STRING_ARRAY);
			((FragmentServiceOrder) fragment).setBeauticianIdNameAndTreatments(beauticianId, beauticianName,
					treatmentStringArray);
		}
	}

	public interface OnFieldChangeListener {
		public void onFieldChange(String result);
	}

}
