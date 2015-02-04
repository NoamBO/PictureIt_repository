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

	public static class FragmentTermsOfService extends Fragment {

		public FragmentTermsOfService() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			ViewGroup v = (ViewGroup) inflater.inflate(R.layout.fragment_terms_of_service, container, false);

			return v;
		}
	}

}
