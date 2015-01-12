package com.pictureit.noambaroz.beauticianapp.dialog;

import net.simonvt.numberpicker.NumberPicker;
import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.pictureit.noambaroz.beauticianapp.R;

public class MyNumberPickerDialog extends BaseDialog implements android.view.View.OnClickListener {

	private NumberPicker mNumberPicker;

	private OnClickListener positiveListener;
	private OnClickListener negativeListener;

	public MyNumberPickerDialog(Activity a) {
		super(a);

		mNumberPicker = (NumberPicker) mView.findViewById(R.id.number_picker_dialog_wheel);
		mNumberPicker.setMaxValue(99);
		mNumberPicker.setMinValue(0);
		mNumberPicker.setWrapSelectorWheel(false);

		mView.findViewById(R.id.number_picker_dialog_cancel).setOnClickListener(this);
		mView.findViewById(R.id.number_picker_dialog_ok).setOnClickListener(this);

	}

	public void setPositiveButtonListener(final OnClickListener listener) {
		positiveListener = listener;
	}

	public void setNegativeButtonListener(final OnClickListener listener) {
		negativeListener = listener;
	}

	@Override
	protected int getViewResourceId() {
		return R.layout.dialog_number_picker_layout;
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.number_picker_dialog_ok:
			if (positiveListener != null)
				positiveListener.onClick(MyNumberPickerDialog.this, mNumberPicker.getValue());
			break;
		case R.id.number_picker_dialog_cancel:
			if (negativeListener != null)
				negativeListener.onClick(MyNumberPickerDialog.this, mNumberPicker.getValue());
			break;
		default:
			break;
		}
		dismiss();
	}

	public void setValue(int value) {
		if (mNumberPicker != null)
			mNumberPicker.setValue(value);
	}

	@Override
	public void setTitle(int titleId) {
		setTitle(mContext.getString(titleId));
	}

	@Override
	public void setTitle(CharSequence title) {
		TextView tvTitle = (TextView) mView.findViewById(R.id.number_picker_dialog_title);
		if (tvTitle != null) {
			tvTitle.setText(title);
		}
	}
}
