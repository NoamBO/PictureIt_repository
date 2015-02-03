package com.pictureit.noambaroz.beautyapp;

public class SearchProviderActivity extends ActivityWithFragment {

	@Override
	protected void initActivity() {
	}

	@Override
	protected void setFragment() {
		fragment = new SearchProviderFragment();
	}

	@Override
	protected void setFragmentTag() {
		FRAGMENT_TAG = "search_for_provider_fragment";
	}

}
