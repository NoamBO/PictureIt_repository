package com.pictureit.noambaroz.beauticianapp;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pictureit.noambaroz.beautycianapp.R;

public class UpcomingTreatmentsActivity extends ActivityWithFragment {

	@Override
	protected void initActivity() {
	}

	@Override
	protected void setFragment() {
		fragment = new UpcomingTreatmentsFragment();
	}

	@Override
	protected void setFragmentTag() {
		FRAGMENT_TAG = "upcoming_treatments";
	}

	@Override
	public void onBackPressed() {
		backPressed();
	}

	private static class UpcomingTreatmentsFragment extends Fragment {

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View view = inflater.inflate(R.layout.fragment_upcoming_treatments, container, false);
			return view;
		}
	}

}
