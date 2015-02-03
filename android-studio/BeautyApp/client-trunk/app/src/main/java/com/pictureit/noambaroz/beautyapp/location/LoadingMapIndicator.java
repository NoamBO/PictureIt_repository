package com.pictureit.noambaroz.beautyapp.location;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.pictureit.noambaroz.beautyapp.R;
import com.pictureit.noambaroz.beautyapp.animation.AnimationManager;

public class LoadingMapIndicator {

	private ViewGroup mLoadingMapCoverScreen;
	private Activity mActivity;
	private Animator mAnim;

	public LoadingMapIndicator(Activity activity) {
		mActivity = activity;
		mAnim = AnimatorInflater.loadAnimator(activity, R.anim.rotatr_by_y);
	}

	private ViewGroup getScreenCover() {
		if (mLoadingMapCoverScreen == null) {

			LayoutInflater inflator = LayoutInflater.from(mActivity);
			mLoadingMapCoverScreen = (FrameLayout) inflator.inflate(R.layout.loading_map_indicator, null);
			mAnim.setTarget(mLoadingMapCoverScreen.findViewById(R.id.loadingMapStartRotatingImage));
			FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
					FrameLayout.LayoutParams.MATCH_PARENT);
			mActivity.addContentView(mLoadingMapCoverScreen, params);
		}
		return mLoadingMapCoverScreen;
	}

	public void showMapLoadingIndicator() {
		getScreenCover().findViewById(R.id.loadingMapFailedIndicator).setVisibility(View.GONE);
		View indicator = getScreenCover().findViewById(R.id.loadingMapStartIndicator);
		if (indicator.getVisibility() == View.GONE) {
			AnimationManager.fadeIn(mActivity, indicator);
			mAnim.start();
		}
	}

	public void showNoMapIndicator() {
		AnimationManager.fadeIn(mActivity, getScreenCover().findViewById(R.id.loadingMapFailedIndicator));
		AnimationManager.fadeOut(mActivity, getScreenCover().findViewById(R.id.loadingMapStartIndicator));
		mAnim.cancel();
	}

	public void mapFullyLoaded() {
		AnimationManager.fadeOut(mActivity, getScreenCover());
		mAnim.cancel();
	}

}
