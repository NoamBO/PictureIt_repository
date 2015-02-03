package com.pictureit.noambaroz.beautyapp.customdialogs;

import android.content.Context;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;

import com.pictureit.noambaroz.beautyapp.R;

public class DialogRate extends BaseDialog {

	private TextView bRate, bCancel;
	private RatingBar mRatingBar;

	public DialogRate(Context context) {
		super(context);
		bRate = (TextView) findViewById(R.id.tv_dialog_rate_ok);
		bCancel = (TextView) findViewById(R.id.tv_dialog_rate_cancel);
		mRatingBar = (RatingBar) findViewById(R.id.rb_dialog_rate);
		mRatingBar.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {

			@Override
			public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
				if (rating < 1)
					ratingBar.setRating(1);
			}
		});
		mRatingBar.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (mRatingBar.getRating() < 1) {
					mRatingBar.setRating(1);
					return true;
				}
				return false;
			}
		});
	}

	@Override
	protected int getViewResourceId() {
		return R.layout.dialog_rate_layout;
	}

	public DialogRate setOkButton(String beauticianName, final OnClickListener clickListener) {
		bRate.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (clickListener != null)
					clickListener.onClick(DialogRate.this, mRatingBar.getProgress());
				dismiss();
			}
		});
		bRate.setText(bRate.getText().toString() + " " + beauticianName);
		return this;
	}

	public DialogRate setCancelButton(final OnClickListener clickListener) {
		bCancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (clickListener != null)
					clickListener.onClick(DialogRate.this, 0);
				dismiss();
			}
		});
		return this;
	}

	public void show(View v) {
		if (v == null) {
			show();
			return;
		}

		float top = ((View) v.getParent().getParent()).getTop();
		float height = ((View) v.getParent().getParent()).getHeight();
		float target = top + (height / 2);
		WindowManager.LayoutParams wmlp = this.getWindow().getAttributes();
		wmlp.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
		wmlp.y = (int) target;
		show();
	}

}
