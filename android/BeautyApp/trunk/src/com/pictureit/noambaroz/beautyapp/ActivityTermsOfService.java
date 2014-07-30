package com.pictureit.noambaroz.beautyapp;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ActivityTermsOfService extends ActivityWithFragment {

	@Override
	public void onBackPressed() {
		backPressed();
	}

	@Override
	protected void initActivity() {
	}

	@Override
	protected void setFragment() {
		fragment = new FragmentTermsOfService();
	}

	@Override
	protected void setFragmentTag() {
		FRAGMENT_TAG = "terms_of_service";
	}

	private class FragmentTermsOfService extends Fragment {

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			return inflater.inflate(R.layout.fragment_terms_of_service, container, false);
		}
	}

}
