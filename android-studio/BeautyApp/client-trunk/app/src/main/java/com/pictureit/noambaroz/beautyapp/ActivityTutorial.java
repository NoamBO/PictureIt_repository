package com.pictureit.noambaroz.beautyapp;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.viewpagerindicator.CirclePageIndicator;

public class ActivityTutorial extends FragmentActivity {

	/**
	 * The number of pages (wizard steps) to show in this demo.
	 */
	private static final int NUM_PAGES = 5;

	/**
	 * The pager widget, which handles animation and allows swiping horizontally
	 * to access previous and next wizard steps.
	 */
	private ViewPager mPager;

	/**
	 * The pager adapter, which provides the pages to the view pager widget.
	 */
	private PagerAdapter mPagerAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tutorial);

		// Instantiate a ViewPager and a PagerAdapter.
		mPager = (ViewPager) findViewById(R.id.pager);
		mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
		mPager.setAdapter(mPagerAdapter);
		mPager.setPageTransformer(true, new ZoomOutPageTransformer());

		CirclePageIndicator indicator = (CirclePageIndicator) findViewById(R.id.myindicator);
		indicator.setViewPager(mPager);
		initIndicator(indicator);

		getActionBar().setTitle("");

	}

	private void initIndicator(CirclePageIndicator indicator) {
		final float density = getResources().getDisplayMetrics().density;
		indicator.setBackgroundColor(getResources().getColor(android.R.color.black));
		indicator.setRadius(8 * density);
		indicator.setPageColor(getResources().getColor(android.R.color.black));
		indicator.setFillColor(getResources().getColor(android.R.color.white));
		indicator.setStrokeColor(getResources().getColor(android.R.color.holo_blue_bright));
		indicator.setStrokeWidth(2 * density);
	}

	@Override
	public void onBackPressed() {
		if (mPager.getCurrentItem() == 0) {
			// If the user is currently looking at the first step, allow the
			// system to handle the
			// Back button. This calls finish() on this activity and pops the
			// back stack.
			super.onBackPressed();
		} else {
			// Otherwise, select the previous step.
			mPager.setCurrentItem(mPager.getCurrentItem() - 1);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_activity_treatments, menu);
		menu.findItem(R.id.action_ask_for_service).setIcon(R.drawable.btn_close)
		// TODO
				.setTitle("baaa");
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.action_ask_for_service) {
			// TODO
			Toast.makeText(ActivityTutorial.this, "FINISH HIM!!!", Toast.LENGTH_SHORT).show();
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A simple pager adapter that represents 5 ScreenSlidePageFragment objects,
	 * in sequence.
	 */
	private class ScreenSlidePagerAdapter extends FragmentPagerAdapter {
		public ScreenSlidePagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			Fragment f = new ScreenSlidePageFragment();
			((ScreenSlidePageFragment) f).setDrawable(getDrawableForPosition(position));
			return f;
		}

		private Drawable getDrawableForPosition(int position) {
			int resId = 0;
			switch (position) {
			case 0:
				resId = R.drawable.android_icon;
				break;
			case 1:
				resId = R.drawable.star_full;
				break;
			case 2:
				resId = R.drawable.powered_by_google_light;
				break;
			case 3:
				resId = R.drawable.powered_by_google_dark;
				break;
			case 4:
				resId = R.drawable.star_none;
				break;
			default:
				resId = R.drawable.android_icon;
				break;
			}
			return getResources().getDrawable(resId);
		}

		@Override
		public int getCount() {
			return NUM_PAGES;
		}
	}

	public class ScreenSlidePageFragment extends Fragment {

		private ImageView image;
		private Drawable png;

		public void setDrawable(Drawable d) {
			png = d;
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.tutorial_page, container, false);
			image = (ImageView) rootView.findViewById(R.id.iv_pager_image);
			image.setImageDrawable(png);
			return rootView;
		}
	}

	public class ZoomOutPageTransformer implements ViewPager.PageTransformer {
		private static final float MIN_SCALE = 0.85f;
		private static final float MIN_ALPHA = 0.5f;

		public void transformPage(View view, float position) {
			int pageWidth = view.getWidth();
			int pageHeight = view.getHeight();

			if (position < -1) { // [-Infinity,-1)
				// This page is way off-screen to the left.
				view.setAlpha(0);

			} else if (position <= 1) { // [-1,1]
				// Modify the default slide transition to shrink the page as
				// well
				float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
				float vertMargin = pageHeight * (1 - scaleFactor) / 2;
				float horzMargin = pageWidth * (1 - scaleFactor) / 2;
				if (position < 0) {
					view.setTranslationX(horzMargin - vertMargin / 2);
				} else {
					view.setTranslationX(-horzMargin + vertMargin / 2);
				}

				// Scale the page down (between MIN_SCALE and 1)
				view.setScaleX(scaleFactor);
				view.setScaleY(scaleFactor);

				// Fade the page relative to its size.
				view.setAlpha(MIN_ALPHA + (scaleFactor - MIN_SCALE) / (1 - MIN_SCALE) * (1 - MIN_ALPHA));

			} else { // (1,+Infinity]
				// This page is way off-screen to the right.
				view.setAlpha(0);
			}
		}
	}
}
