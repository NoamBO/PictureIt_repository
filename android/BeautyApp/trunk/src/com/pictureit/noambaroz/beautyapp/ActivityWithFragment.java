package com.pictureit.noambaroz.beautyapp;

import utilities.BaseActivity;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;

public abstract class ActivityWithFragment extends BaseActivity {

	protected String FRAGMENT_TAG = "";
	protected Fragment fragment;
	// protected final int FRAGMENT_CONTAINER = R.id.fragment_container;
	protected final int FRAGMENT_CONTAINER = android.R.id.content;
	protected boolean initActionBar = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_container);
		setFragment();
		setFragmentTag();

		initActivity();
		initActionBar(getActionBar());
	}

	public static void addViewToTopOfActionBar(Activity context) {
		int topViewHeightPix = 6;
		int abContainerViewID = context.getResources().getIdentifier("action_bar_container", "id", "android");
		FrameLayout actionBarContainer = (FrameLayout) context.findViewById(abContainerViewID);
		final TypedArray styledAttributes = context.getTheme().obtainStyledAttributes(
				new int[] { android.R.attr.actionBarSize });
		int actionBarHeight = (int) styledAttributes.getDimension(0, 0);
		styledAttributes.recycle();
		actionBarContainer.getLayoutParams().height = actionBarHeight + topViewHeightPix;
		FrameLayout.LayoutParams params = (android.widget.FrameLayout.LayoutParams) actionBarContainer.getChildAt(0)
				.getLayoutParams();
		params.gravity = Gravity.BOTTOM;
		View customView = new View(context);
		customView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, topViewHeightPix));
		customView.setBackgroundColor(context.getResources().getColor(R.color.yellow));
		actionBarContainer.addView(customView);
	}

	@Override
	protected void onResume() {
		addViewToTopOfActionBar(ActivityWithFragment.this);
		super.onResume();
		if (getFragmentManager().findFragmentByTag(FRAGMENT_TAG) == null)
			getFragmentManager().beginTransaction().replace(FRAGMENT_CONTAINER, fragment, FRAGMENT_TAG).commit();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (getFragmentManager().findFragmentByTag(FRAGMENT_TAG) != null)
			getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentByTag(FRAGMENT_TAG))
					.commit();

	}

	protected void initActionBar(ActionBar actionBar) {
		if (initActionBar) {
			actionBar.setDisplayHomeAsUpEnabled(true);
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
