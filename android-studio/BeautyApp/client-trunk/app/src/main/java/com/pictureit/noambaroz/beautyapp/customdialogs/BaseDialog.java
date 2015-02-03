package com.pictureit.noambaroz.beautyapp.customdialogs;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.pictureit.noambaroz.beautyapp.R;

public abstract class BaseDialog extends Dialog {

	protected Context mContext;
	protected View mView;

	public BaseDialog(Context context) {
		super(context, R.style.Theme_DialodNoWindowTitle);
		mContext = context;
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mView = inflater.inflate(getViewResourceId(), null);
		setContentView(mView);
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		lp.copyFrom(this.getWindow().getAttributes());
		lp.width = WindowManager.LayoutParams.MATCH_PARENT;
		lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
		this.getWindow().setAttributes(lp);
	}

	protected abstract int getViewResourceId();
}
