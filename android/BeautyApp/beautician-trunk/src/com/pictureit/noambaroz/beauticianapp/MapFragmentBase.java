package com.pictureit.noambaroz.beauticianapp;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pictureit.noambaroz.beauticianapp.animation.AnimationManager;
import com.pictureit.noambaroz.beauticianapp.R;

public class MapFragmentBase extends BaseFragment {

	protected ViewGroup mLoadingMapCoverScreen;
	private Animator mAnim;

	protected void initIndicator() {
		mAnim = AnimatorInflater.loadAnimator(getActivity(), R.anim.rotatr_by_y);
		mAnim.setTarget(mLoadingMapCoverScreen.findViewById(R.id.loadingMapStartRotatingImage));
	}

	protected void showMapLoadingIndicator() {
		mLoadingMapCoverScreen.setVisibility(View.VISIBLE);
		mLoadingMapCoverScreen.findViewById(R.id.loadingMapFailedIndicator).setVisibility(View.GONE);
		View indicator = mLoadingMapCoverScreen.findViewById(R.id.loadingMapStartIndicator);
		if (indicator.getVisibility() == View.GONE) {
			AnimationManager.fadeIn(getActivity(), indicator);
			mAnim.start();
		}
	}

	protected void showNoMapIndicator() {
		AnimationManager.fadeIn(getActivity(), mLoadingMapCoverScreen.findViewById(R.id.loadingMapFailedIndicator));
		AnimationManager.fadeOut(getActivity(), mLoadingMapCoverScreen.findViewById(R.id.loadingMapStartIndicator));
		mAnim.cancel();
	}

	protected void mapFullyLoaded() {
		mLoadingMapCoverScreen.setVisibility(View.GONE);
		mAnim.cancel();
	}

}
