package com.pictureit.noambaroz.beautyapp.customdialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.pictureit.noambaroz.beautyapp.R;

public class ErrorDialog extends Dialog {

	protected Context mContext;

	protected View mView;

	public ErrorDialog(Context context) {
		super(context, R.style.Theme_DialodNoWindowTitle);
		mContext = context;
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mView = inflater.inflate(getViewResourceId(), null);
		setContentView(mView);
	}

	protected int getViewResourceId() {
		return R.layout.dialog_error_layout;
	}

	public ErrorDialog setMessage(int resId) {
		String message = getContext().getString(resId);
		setMessage(message);
		return this;
	}

	public ErrorDialog setMessage(String message) {
		TextView textView = (TextView) mView.findViewById(R.id.my_error_dialog_message);
		textView.setText(message);
		return this;
	}

	public ErrorDialog setOnClickListener(final DialogInterface.OnClickListener clickListener) {
		TextView textView = (TextView) mView.findViewById(R.id.my_error_dialog_button);
		textView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (clickListener != null)
					clickListener.onClick(ErrorDialog.this, 0);
				dismiss();
			}
		});
		return this;
	}

}
