package com.pictureit.noambaroz.beauticianapp;

import android.app.Activity;
import android.view.View;

public class BaseActivity extends Activity {

	@SuppressWarnings("unchecked")
	protected <T extends View> T findView(int id) {
		return (T) findViewById(id);
	}

	@SuppressWarnings("unchecked")
	protected <T extends View> T findView(View rootView, int id) {
		return (T) rootView.findViewById(id);
	}

}
