package com.pictureit.noambaroz.beauticianapp;

import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;

import com.pictureit.noambaroz.beauticianapp.utilities.view.MyFontTextView;

public abstract class ActivityWithFragment extends BaseActivity {

	protected String FRAGMENT_TAG = "";
	protected Fragment fragment;
	protected final int FRAGMENT_CONTAINER = android.R.id.content;
	protected boolean initActionBar = true;
	protected boolean isTitleVisible = true;

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
		Resources r = context.getResources();
		float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX,
				r.getDimension(R.dimen.actionbar_top_line_height), r.getDisplayMetrics());

		int topViewHeightPix = (int) px;
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
		customView.setBackgroundColor(context.getResources().getColor(R.color.app_white));
		actionBarContainer.addView(customView);
	}

	@Override
	protected void onResume() {
		if (initActionBar)
			addViewToTopOfActionBar(ActivityWithFragment.this);
		super.onResume();
		if (getFragmentManager().findFragmentByTag(FRAGMENT_TAG) == null)
			getFragmentManager().beginTransaction().replace(FRAGMENT_CONTAINER, fragment, FRAGMENT_TAG).commit();
		if (MyPreference.hasAlarmsDialogsToShow())
			startActivity(new Intent(ActivityWithFragment.this, ActivityNotificationsDialog.class));
	}

	private void setActionBarTitleFont() {
		this.getActionBar().setDisplayShowCustomEnabled(true);
		this.getActionBar().setDisplayShowTitleEnabled(false);

		LayoutInflater inflator = LayoutInflater.from(this);
		View v = inflator.inflate(R.layout.actionbar_title_view,
				(ViewGroup) getWindow().getDecorView().findViewById(android.R.id.content), false);
		MyFontTextView tv = findView(v, R.id.actionbar_title);

		tv.setText(this.getTitle());
		this.getActionBar().setCustomView(tv);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (getFragmentManager().findFragmentByTag(FRAGMENT_TAG) != null)
			getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentByTag(FRAGMENT_TAG))
					.commit();

	}

	protected void initActionBar(ActionBar actionBar) {
		if (initActionBar)
			actionBar.setDisplayHomeAsUpEnabled(true);

		if (!isTitleVisible)
			actionBar.setTitle("");
		else
			setActionBarTitleFont();
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

	private Class<?> getClassName() {
		ActivityManager activityManager = (ActivityManager) ActivityWithFragment.this
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> services = activityManager.getRunningTasks(Integer.MAX_VALUE);
		Class<?> act = null;
		try {
			act = Class.forName(services.get(0).topActivity.getClassName());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return act;
	}
}
