package com.pictureit.noambaroz.beautyapp;

import android.os.Bundle;
import android.preference.PreferenceFragment;

public class PreferenceActivityRadius extends ActivityWithFragment {

	private class SettingsFragment extends PreferenceFragment {

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(R.xml.settins_preference);
		}
	}

	@Override
	protected void initActivity() {
		initActionBar = false;
	}

	@Override
	protected void setFragment() {
		fragment = new SettingsFragment();
	}

	@Override
	protected void setFragmentTag() {
		FRAGMENT_TAG = "setting_fragmrnt";
	}
}
