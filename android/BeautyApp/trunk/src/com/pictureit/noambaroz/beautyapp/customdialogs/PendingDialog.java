package com.pictureit.noambaroz.beautyapp.customdialogs;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.pictureit.noambaroz.beautyapp.R;
import com.pictureit.noambaroz.beautyapp.animation.AnimationManager;

public class PendingDialog {

	private Activity mActivity;
	private View mView;

	public PendingDialog(Activity activity) {
		mActivity = activity;
		mView = activity.findViewById(R.id.pendingDialog);
		if (mView == null)
			return;

		mView.setVisibility(View.GONE);
	}

	public void dismiss() {
		if (mView == null)
			return;

		if (isShowing())
			AnimationManager.fadeOut(mActivity, mView);
	}

	public void show() {
		if (mView == null)
			return;

		if (!isShowing())
			AnimationManager.fadeIn(mActivity, mView);
	}

	public boolean isShowing() {
		if (mView == null)
			return false;

		return mView.getVisibility() == View.VISIBLE;
	}

	public void setCancelButton(OnClickListener onClickListener) {
		if (mView == null)
			return;

		TextView tvCancel = (TextView) mActivity.findViewById(R.id.pending_dialog_cancel);
		tvCancel.setOnClickListener(onClickListener);
	}
}
