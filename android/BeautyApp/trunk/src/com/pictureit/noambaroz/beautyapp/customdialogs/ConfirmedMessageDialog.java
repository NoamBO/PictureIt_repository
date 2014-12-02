package com.pictureit.noambaroz.beautyapp.customdialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.pictureit.noambaroz.beautyapp.R;

public class ConfirmedMessageDialog extends Dialog {

	private Context mContext;
	private TextView bClose;
	private ViewGroup bCall;

	public ConfirmedMessageDialog(Context context) {
		super(context, R.style.Theme_DialodNoWindowTitle);
		mContext = context;
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.dialog_layout_message_confirmed, null);
		bCall = (ViewGroup) view.findViewById(R.id.fl_dialog_order_confirmed_layout_button_call);
		bClose = (TextView) findViewById(R.id.tv_dialog_order_confirmed_layout_button_close);
		setContentView(view);
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		lp.copyFrom(this.getWindow().getAttributes());
		lp.width = WindowManager.LayoutParams.MATCH_PARENT;
		lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
		this.getWindow().setAttributes(lp);
	}

	public void setCallButtonListener(final DialogInterface.OnClickListener listener) {
		bCall.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				listener.onClick(ConfirmedMessageDialog.this, 0);
				dismiss();
			}
		});

	}

	public void setCloseButtonListener(final DialogInterface.OnClickListener listener) {
		bClose.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				listener.onClick(ConfirmedMessageDialog.this, 0);
				dismiss();
			}
		});

	}

}
