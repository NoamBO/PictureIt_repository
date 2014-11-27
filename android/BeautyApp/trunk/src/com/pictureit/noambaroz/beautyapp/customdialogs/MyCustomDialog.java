package com.pictureit.noambaroz.beautyapp.customdialogs;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.pictureit.noambaroz.beautyapp.R;

public class MyCustomDialog extends Dialog {

	private Context mContext;
	private View mView;
	private EditText mEditText;

	public MyCustomDialog(Context context) {
		super(context, R.style.Theme_DialodNoWindowTitle);
		mContext = context;
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mView = inflater.inflate(R.layout.my_custom_dialog, null);
		setContentView(mView);
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		lp.copyFrom(this.getWindow().getAttributes());
		lp.width = WindowManager.LayoutParams.MATCH_PARENT;
		lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
		this.getWindow().setAttributes(lp);
	}

	public EditText getEditText() {
		mEditText = (EditText) mView.findViewById(R.id.my_custom_dialog_edittext);
		mEditText.setVisibility(View.VISIBLE);
		return mEditText;
	}

	public MyCustomDialog setNegativeButton(String text, OnClickListener clickListener) {
		return setNegativeButton(0, text, clickListener);
	}

	public MyCustomDialog setNegativeButton(int resId, OnClickListener clickListener) {
		return setNegativeButton(resId, null, clickListener);
	}

	private MyCustomDialog setNegativeButton(int resId, String text, final OnClickListener clickListener) {
		TextView negativeButton = (TextView) mView.findViewById(R.id.my_custom_dialog_negative_button);
		if (negativeButton != null) {
			negativeButton.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					if (clickListener != null)
						clickListener.onClick(MyCustomDialog.this, 0);
					dismiss();
				}
			});
			negativeButton.setVisibility(View.VISIBLE);
			negativeButton.setText(text != null ? text : mContext.getString(resId));
		}
		return this;
	}

	public MyCustomDialog setPositiveButton(String text, OnClickListener clickListener) {
		return setPositiveButton(0, text, clickListener);
	}

	public MyCustomDialog setPositiveButton(int resId, OnClickListener clickListener) {
		return setPositiveButton(resId, null, clickListener);
	}

	private MyCustomDialog setPositiveButton(int resId, String text, final OnClickListener clickListener) {
		TextView positiveButton = (TextView) mView.findViewById(R.id.my_custom_dialog_positive_button);
		if (positiveButton != null) {
			positiveButton.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					if (clickListener != null)
						clickListener.onClick(MyCustomDialog.this, 0);
					dismiss();
				}
			});
			positiveButton.setVisibility(View.VISIBLE);
			positiveButton.setText(text != null ? text : mContext.getString(resId));
		}
		return this;
	}

	public MyCustomDialog setSubMessage(String subMessage) {
		TextView submessage = (TextView) mView.findViewById(R.id.my_custom_dialog_submessage);
		if (submessage != null) {
			submessage.setVisibility(View.VISIBLE);
			submessage.setText(subMessage);
		}
		return this;
	}

	public MyCustomDialog setMessage(String message) {
		TextView tvMessage = (TextView) mView.findViewById(R.id.my_custom_dialog_message);
		if (tvMessage != null) {
			tvMessage.setVisibility(View.VISIBLE);
			tvMessage.setText(message);
		}
		return this;
	}

	public MyCustomDialog setDialogTitle(int resId) {
		return setDialogTitle(null, resId);
	}

	public MyCustomDialog setDialogTitle(String title) {
		return setDialogTitle(title, 0);
	}

	private MyCustomDialog setDialogTitle(String title, int resId) {
		TextView tvTitle = (TextView) mView.findViewById(R.id.my_custom_dialog_title);
		if (tvTitle != null) {
			tvTitle.setVisibility(View.VISIBLE);
			tvTitle.setText(title == null ? mContext.getString(resId) : title);
		}
		return this;
	}
}
