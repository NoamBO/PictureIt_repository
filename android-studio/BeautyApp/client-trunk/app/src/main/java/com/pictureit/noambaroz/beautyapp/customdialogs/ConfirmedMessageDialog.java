package com.pictureit.noambaroz.beautyapp.customdialogs;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pictureit.noambaroz.beautyapp.R;

public class ConfirmedMessageDialog extends BaseDialog {

	private TextView bClose;
	private ViewGroup bCall;

	public ConfirmedMessageDialog(Context context) {
		super(context);
		this.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
	}

	@Override
	protected int getViewResourceId() {
		return R.layout.dialog_layout_message_confirmed;
	}

	public void setCallButtonListener(final DialogInterface.OnClickListener listener) {
		if (bCall == null)
			bCall = (ViewGroup) mView.findViewById(R.id.fl_dialog_order_confirmed_layout_button_call);
		bCall.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				listener.onClick(ConfirmedMessageDialog.this, 0);
			}
		});

	}

	public void setCloseButtonListener(final DialogInterface.OnClickListener listener) {
		if (bClose == null)
			bClose = (TextView) mView.findViewById(R.id.tv_dialog_order_confirmed_layout_button_close);
		bClose.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (listener != null)
					listener.onClick(ConfirmedMessageDialog.this, 0);
				dismiss();
			}
		});

	}

}
