package com.pictureit.noambaroz.beautyapp.animation;

import java.util.HashMap;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import android.view.Display;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.Transformation;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.pictureit.noambaroz.beautyapp.R;

public class AnimationManager {

	public static void fadeIn(Context context, final View v) {
		if (context == null) {
			v.setVisibility(View.VISIBLE);
			return;
		}
		v.setVisibility(View.INVISIBLE);
		Animation animation = AnimationUtils.loadAnimation(context, R.anim.fade_in);
		AnimationListener l = new BaseAnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
				v.setVisibility(View.VISIBLE);
			}
		};
		animation.setAnimationListener(l);
		v.startAnimation(animation);
	}

	public static void fadeOut(Context context, final View v) {
		Animation animation = AnimationUtils.loadAnimation(context, R.anim.fade_out);
		AnimationListener l = new BaseAnimationListener() {
			@Override
			public void onAnimationEnd(Animation animation) {
				v.setVisibility(View.GONE);
			}
		};
		animation.setAnimationListener(l);
		v.startAnimation(animation);
	}

	public interface OnRowCollapseCallback {
		public void onCollapse(View v, int initialHeight);
	}

	public static int getHeightToExpand(int heightPersents, Activity activity, HashMap<String, View> additionalViews) {
		Display display = activity.getWindowManager().getDefaultDisplay();

		double persents = heightPersents / 100f;

		@SuppressWarnings("deprecation")
		int height = display.getHeight();
		int targetHeight = (int) (height * persents);

		if (additionalViews != null) {
			if (additionalViews.containsKey("add")) {
				View v = additionalViews.get("add");
				v.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
						MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
				int eh = v.getMeasuredHeight();
				targetHeight = +eh;
			}
			if (additionalViews.containsKey("remove")) {
				View v = additionalViews.get("remove");
				v.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
						MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
				int eh = v.getHeight();
				targetHeight -= eh;
			}
		}

		return targetHeight;
	}

	public static void expand(View v) {
		expand(v, 0, null);
	}

	public static void expand(final View v, int additionalDurationTime) {
		expand(v, additionalDurationTime, null);
	}

	public static void expand(final View v, int additionalDurationTime, BaseAnimationListener animationListener) {
		final int targetHeight;
		if (v instanceof ListView) {
			targetHeight = getListViewHeight((ListView) v);
		} else {
			v.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
					MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
			targetHeight = v.getMeasuredHeight();
		}
		v.getLayoutParams().height = 0;
		v.setVisibility(View.VISIBLE);
		Animation a = new Animation() {
			@Override
			protected void applyTransformation(float interpolatedTime, Transformation t) {
				v.getLayoutParams().height = interpolatedTime == 1 ? LayoutParams.WRAP_CONTENT
						: (int) (targetHeight * interpolatedTime);
				v.requestLayout();
			}

			@Override
			public boolean willChangeBounds() {
				return true;
			}
		};
		if (animationListener != null) {
			a.setAnimationListener(animationListener);
		}
		a.setDuration((int) (targetHeight / v.getContext().getResources().getDisplayMetrics().density)
				+ additionalDurationTime);
		v.startAnimation(a);
	}

	public static void collapse(final View v) {
		collapse(v, 0);
	}

	public static void collapse(final View v, int additionalDurationTime) {
		collapse(v, additionalDurationTime, null);
	}

	public static void collapse(final View v, int additionalDurationTime, AnimationListener listener) {
		collapse(v, additionalDurationTime, listener, true, null);
	}

	public static void collapseCursorAdapterRow(final View v, int additionalDurationTime,
			final OnRowCollapseCallback callback) {
		collapse(v, additionalDurationTime, null, false, callback);
	}

	private static void collapse(final View v, int additionalDurationTime, AnimationListener listener,
			final boolean leaveViewInvisible, final OnRowCollapseCallback callback) {
		final int initialHeight = v.getMeasuredHeight();

		Animation a = new Animation() {
			@Override
			protected void applyTransformation(float interpolatedTime, Transformation t) {
				if (interpolatedTime == 1) {
					if (leaveViewInvisible)
						v.setVisibility(View.GONE);
					else {
						if (callback != null)
							callback.onCollapse(v, initialHeight);
					}
				} else {
					v.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
					v.requestLayout();
				}
			}

			@Override
			public boolean willChangeBounds() {
				return true;
			}
		};

		// 1dp/ms
		a.setAnimationListener(listener);
		a.setDuration((int) (initialHeight / v.getContext().getResources().getDisplayMetrics().density)
				+ additionalDurationTime);
		v.startAnimation(a);
	}

	public static int getListViewHeight(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			// pre-condition
			return 0;
		}

		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View child = listAdapter.getView(i, null, listView);
			child.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

			child.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
					MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));

			totalHeight += child.getMeasuredHeight();
		}
		return totalHeight;
	}

	public static void setListViewHeightBasedOnChildren(ListView listView) {
		int totalHeight = getListViewHeight(listView);
		ListAdapter listAdapter = listView.getAdapter();
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
		listView.requestLayout();
	}

	public static class MainProvidersListAnimation {

		public static void slide(View v, Activity activity, boolean toExpand) {
			slide(v, activity, toExpand, null);
		}

		public static void slide(View v, Activity activity, boolean toExpand, BaseAnimationListener listener) {
			if (toExpand)
				expandByY(v, activity, listener);
			else
				collapseByY(v, activity, listener);
		}

		private static boolean expandByY(final View v, Activity activity, BaseAnimationListener listener) {
			final int targetHeight = getProvidersListSliderHeight(activity);
			v.getLayoutParams().height = 0;
			v.setVisibility(View.VISIBLE);
			Animation a = new Animation() {
				@Override
				protected void applyTransformation(float interpolatedTime, Transformation t) {
					v.getLayoutParams().height = interpolatedTime == 1 ? targetHeight
							: (int) (targetHeight * interpolatedTime);
					v.requestLayout();
				}

				@Override
				public boolean willChangeBounds() {
					return true;
				}
			};

			a.setAnimationListener(listener);
			a.setDuration(500);
			v.startAnimation(a);
			return true;
		}

		private static boolean collapseByY(final View view, Activity activity, AnimationListener listener) {

			final int targetHeight = getProvidersListSliderHeight(activity);
			Animation a = new Animation() {
				@Override
				protected void applyTransformation(float interpolatedTime, Transformation t) {
					if (interpolatedTime == 1) {
						view.setVisibility(View.GONE);
					} else {
						view.getLayoutParams().height = targetHeight - (int) (targetHeight * interpolatedTime);
						view.requestLayout();
					}
				}

				@Override
				public boolean willChangeBounds() {
					return true;
				}
			};

			a.setAnimationListener(listener);
			a.setDuration(500);
			view.startAnimation(a);
			return true;
		}

		private static int getProvidersListSliderHeight(Activity activity) {
			// int actionBarHeight = activity.getActionBar().getHeight();
			// View touchToSlideButton =
			// activity.findViewById(R.id.vg_main_touch_to_slide_list);
			// touchToSlideButton.measure(MeasureSpec.makeMeasureSpec(0,
			// MeasureSpec.UNSPECIFIED),
			// MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
			// int touchToSlideButtonHeight =
			// touchToSlideButton.getMeasuredHeight();
			int sliderHeight = getHeightToExpand(100, activity, null);
			// int i = sliderHeight - (actionBarHeight * 2 +
			// touchToSlideButtonHeight);
			int i = sliderHeight;
			return i;
		}
	}
}
