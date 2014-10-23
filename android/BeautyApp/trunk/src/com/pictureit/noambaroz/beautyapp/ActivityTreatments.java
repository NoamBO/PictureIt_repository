package com.pictureit.noambaroz.beautyapp;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ActivityTreatments extends ActivityWithFragment {

	@Override
	public void onBackPressed() {
		backPressed();
	}

	@Override
	protected void initActivity() {
		fragment = new FragmentTreatments();
	}

	@Override
	protected void setFragment() {
		// TODO
	}

	@Override
	protected void setFragmentTag() {
		FRAGMENT_TAG = "notification_fragment";
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_activity_treatments, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_ask_for_service) {
			Intent intent = new Intent(ActivityTreatments.this, ServiceOrder.class);
			startActivity(intent);
			overridePendingTransition(R.anim.activity_enter_slidein_anim, R.anim.activity_exit_shrink_anim);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private class FragmentTreatments extends Fragment {

		private ListView mListView;
		private ProgressBar mProgressBar;
		private TextView mTextView;

		@Override
		public void onCreate(Bundle savedInstanceState) {
			// TODO HTTP request: get all upcoming events
			super.onCreate(savedInstanceState);
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View v = inflater.inflate(R.layout.fragment_treatments, container, false);
			mListView = findView(v, R.id.lv_upcoming_treatments);
			mProgressBar = findView(v, R.id.pb_upcoming_treatments_loading_indicator);
			mTextView = findView(v, R.id.tv_upcoming_treatments_empty_list_indicator);
			return v;
		}

	}

}
