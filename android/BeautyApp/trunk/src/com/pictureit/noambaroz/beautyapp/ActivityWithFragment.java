package com.pictureit.noambaroz.beautyapp;

import utilities.BaseActivity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.MenuItem;

public abstract class ActivityWithFragment extends BaseActivity {

	protected String FRAGMENT_TAG = "";
	protected Fragment fragment;
	protected final int FRAGMENT_CONTAINER = R.id.fragment_container;
	protected boolean initActionBar = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_container);
		setFragment();
		setFragmentTag();

		initActivity();
		initActionBar();
	}

	@Override
	protected void onResume() {
		super.onResume();
		getFragmentManager().beginTransaction().replace(FRAGMENT_CONTAINER, fragment, FRAGMENT_TAG).commit();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (getFragmentManager().findFragmentByTag(FRAGMENT_TAG) != null)
			getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentByTag(FRAGMENT_TAG))
					.commit();

	}

	protected void initActionBar() {
		if (initActionBar) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		// Respond to the action bar's Up/Home button
		case android.R.id.home:
			backPressed();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	protected abstract void initActivity();

	protected abstract void setFragment();

	protected abstract void setFragmentTag();

	protected void backPressed() {
		if (getFragmentManager().getBackStackEntryCount() == 0) {
			finish();
			overridePendingTransition(R.anim.activity_enter_extend_anim, R.anim.activity_exit_slideout_anim);
		} else
			getFragmentManager().popBackStack();
	}

}
