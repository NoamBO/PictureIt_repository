package com.pictureit.noambaroz.beauticianapp;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ActivityTermsOfUse extends ActivityWithFragment {

	@Override
	protected void initActivity() {
	}

	@Override
	protected void setFragment() {
		fragment = new FragmentTermsOfUse();
	}

	@Override
	protected void setFragmentTag() {
		FRAGMENT_TAG = "fragment_terms_of_use";
	}

	private class FragmentTermsOfUse extends Fragment {
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View v = inflater.inflate(R.layout.fragment_terms_of_service, container, false);
			return v;
		}
	}

}
