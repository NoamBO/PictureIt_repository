package com.pictureit.noambaroz.beautyapp;

import android.app.ListFragment;

public class ActivityHistory extends ActivityWithFragment {

	@Override
	protected void initActivity() {
	}

	@Override
	protected void setFragment() {
		fragment = new FragmentHistory();
	}

	@Override
	protected void setFragmentTag() {
		FRAGMENT_TAG = "FragmentHistory";
	}

	private class FragmentHistory extends ListFragment {

	}

	// private class HistoryListAdapter extends ArrayAdapter<T> {}

}
