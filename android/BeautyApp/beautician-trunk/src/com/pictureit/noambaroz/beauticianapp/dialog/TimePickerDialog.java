package com.pictureit.noambaroz.beauticianapp.dialog;

import net.simonvt.numberpicker.NumberPicker;
import noam.baroz.timepicker.OnTimeSetListener;
import noam.baroz.timepicker.TimePicker;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.pictureit.noambaroz.beauticianapp.R;

public class TimePickerDialog extends BaseDialog {

	private final String DEFAULT_POSITIVE_BITTON_TEXT = "ok";
	private final String DEFAULT_NEGATIVE_BITTON_TEXT = "cancel";

	private NumberPicker mHour;
	private NumberPicker mMinuts;
	private TimePicker mPicker;
	private OnTimeSetListener mListener;

	private OnClickListener mPositiveListener;
	private OnClickListener mNegativeListener;

	private TextView mConfirm;
	private TextView mCancel;

	public TimePickerDialog(Context context, OnTimeSetListener onTimeSetListener) {
		super(context);

		mListener = onTimeSetListener;
		mPicker = (TimePicker) mView.findViewById(R.id.timepicker_dialog_picker);
		mHour = (NumberPicker) mPicker.findViewById(R.id.hour);
		mMinuts = (NumberPicker) mPicker.findViewById(R.id.minute);

		mCancel = (TextView) mView.findViewById(R.id.timepicker_dialog_cancel);
		mCancel.setText(DEFAULT_NEGATIVE_BITTON_TEXT);
		mCancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mNegativeListener != null)
					mNegativeListener.onClick(TimePickerDialog.this, 0);
				dismiss();
			}
		});

		mConfirm = (TextView) mView.findViewById(R.id.timepicker_dialog_ok);
		mConfirm.setText(DEFAULT_POSITIVE_BITTON_TEXT);
		mConfirm.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mPositiveListener != null)
					mPositiveListener.onClick(TimePickerDialog.this, 0);
				mListener.onTimeSet(mPicker, mHour.getValue(), mMinuts.getValue());
				dismiss();
			}
		});

	}

	@Override
	protected int getViewResourceId() {
		return R.layout.my_time_picker_dialog_layout;
	}

	public TimePickerDialog setNegativeButton(int resId, OnClickListener clickListener) {
		return setNegativeButton(mContext.getString(resId), clickListener);
	}

	private TimePickerDialog setNegativeButton(String text, OnClickListener clickListener) {
		mNegativeListener = clickListener;
		mCancel.setText(text != null ? text : DEFAULT_NEGATIVE_BITTON_TEXT);
		return this;
	}

	public TimePickerDialog setPositiveButton(int resId, OnClickListener clickListener) {
		return setPositiveButton(mContext.getString(resId), clickListener);
	}

	private TimePickerDialog setPositiveButton(String text, OnClickListener clickListener) {
		mPositiveListener = clickListener;
		mConfirm.setText(text != null ? text : DEFAULT_POSITIVE_BITTON_TEXT);
		return this;
	}

	public TimePickerDialog setDialogTitle(int resId) {
		return setDialogTitle(mContext.getString(resId));
	}

	private TimePickerDialog setDialogTitle(String title) {
		TextView tvTitle = (TextView) mView.findViewById(R.id.timepicker_dialog_title);
		if (tvTitle != null) {
			tvTitle.setVisibility(View.VISIBLE);
			tvTitle.setText(title);
		}
		return this;
	}

	public TimePickerDialog setIcon(int resId) {
		TextView title = (TextView) mView.findViewById(R.id.timepicker_dialog_title);
		title.setCompoundDrawablesWithIntrinsicBounds(0, 0, resId, 0);
		return this;
	}

}
