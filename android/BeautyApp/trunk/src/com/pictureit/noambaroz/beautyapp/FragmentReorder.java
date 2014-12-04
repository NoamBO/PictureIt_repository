package com.pictureit.noambaroz.beautyapp;

import java.util.ArrayList;

import android.app.Activity;

import com.pictureit.noambaroz.beautyapp.data.TreatmentSummary;
import com.pictureit.noambaroz.beautyapp.data.TreatmentType;
import com.pictureit.noambaroz.beautyapp.helper.ServiceOrderManager;

public class FragmentReorder extends FragmentServiceOrder {

	public FragmentReorder(Activity activity, String forWho, String when, String location, String remarks,
			ArrayList<TreatmentType> treatments) {
		ServiceOrderManager manager = new ServiceOrderManager(activity);
		TreatmentSummary t = new TreatmentSummary();
		t.setForWho(forWho);
		t.setRemarks(remarks);
		t.setTretments(treatments);
		t.setWhare(location);
		t.setWhen(when);
		manager.setTreatment(t);
		setLocalManager(manager);
	}

	private void setLocalManager(ServiceOrderManager manager) {
		mManager = manager;
	}

	@Override
	public void onResume() {
		super.onResume();
		setAllFildes();
	}

	private void setAllFildes() {
		if (mManager.getTreatment().forwho.equalsIgnoreCase(""))
			setRowOrder(tvFor, mManager.getTreatment().forwho, bFor,
					getResources().getString(R.string.who_the_service_for));
		else
			setRowOrder(tvFor, mManager.getTreatment().forwho, bFor);

		BeauticianUtil.setTreatmentsList(getActivity(), tvTreatmentsList1, tvTreatmentsList2,
				mManager.getTreatment().treatments);
		if (tvTreatmentsList1.getText().toString().equalsIgnoreCase("")
				&& tvTreatmentsList2.getText().toString().equalsIgnoreCase(""))
			setRowOrder(null, null, bTreatmentSelection, getString(R.string.select_treatment));
		else
			setRowOrder(null, null, bTreatmentSelection);

		setRowOrder(tvLocation, mManager.getTreatment().location, bLocation);

		setRowOrder(tvRemarks, mManager.getTreatment().comments, bRemarks);
	}

}
