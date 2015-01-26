package com.pictureit.noambaroz.beauticianapp;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.noambaroz.crop_image.Crop;
import com.pictureit.noambaroz.beauticianapp.dialog.Dialogs;
import com.pictureit.noambaroz.beauticianapp.server.HttpBase.HttpCallback;
import com.pictureit.noambaroz.beauticianapp.server.RegisterInBackground;

public class ActivityRegister extends Activity {

	private ImageView ivProfilePic;
	private ViewGroup bProceed;
	private ViewGroup invalidFiledIndicator;
	private EditText mEditText;
	private TextView bGoToWeb;

	private Bitmap mBitmap;
	private String bitmapByteString;

	private BroadcastReceiver mReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String message = intent.getStringExtra("message");
			if (TextUtils.isEmpty(message) || message.equalsIgnoreCase(Crop.ACTION_CANCEL))
				return;

			if (message.equalsIgnoreCase(Crop.ACTION_IMAGE_TURN_INTO_BITMAP)) {
				mBitmap = (Bitmap) intent.getParcelableExtra("image");
				setImage();
			}

			if (message.equalsIgnoreCase(Crop.ACTION_IMAGE_SELECTED)) {
				bitmapByteString = intent.getStringExtra("image");
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);

		registerReceiver(mReceiver, new IntentFilter(Crop.cropFilter));

		ivProfilePic = (ImageView) findViewById(R.id.iv_registeration_image);
		bProceed = (ViewGroup) findViewById(R.id.rl_registration_proceed);
		invalidFiledIndicator = (ViewGroup) findViewById(R.id.fl_registration_invalid_filed_indicator);
		mEditText = (EditText) findViewById(R.id.et_registration_code);
		bGoToWeb = (TextView) findViewById(R.id.tv_registration_go_to_web);

	}

	@Override
	protected void onResume() {
		super.onResume();
		setImage();
		ivProfilePic.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Crop.doCrop(ActivityRegister.this, mReceiver);
			}
		});
		bProceed.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				proceed();
			}
		});
		bGoToWeb.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				launchBeautyappWebPageInBrowser();
			}
		});
	}

	protected void launchBeautyappWebPageInBrowser() {
		String url = "http://www.google.com";
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setData(Uri.parse(url));
		startActivity(i);
	}

	protected void proceed() {
		String code = mEditText.getText().toString();
		if (isCodeFormOk(code))
			registerInBackground(code);
	}

	private void registerInBackground(String code) {
		RegisterInBackground httpRequest = new RegisterInBackground(ActivityRegister.this, code, bitmapByteString,
				new HttpCallback() {

					@Override
					public void onAnswerReturn(Object answer) {
						if ((Integer) answer == RegisterInBackground.REGISTER_SERVER_FAILED)
							Dialogs.showServerFailedDialog(ActivityRegister.this);
						else if ((Integer) answer == RegisterInBackground.REGISTER_CODE_INVALID)
							Dialogs.showErrorDialog(ActivityRegister.this, R.string.invalid_registration_code);
						else if ((Integer) answer != RegisterInBackground.REGISTER_SUCCESS)
							Dialogs.generalDialog(ActivityRegister.this, Dialogs.somthing_went_wrong);
						else {
							Intent intent = new Intent(ActivityRegister.this, MainActivity.class);
							startActivity(intent);
							finish();
							overridePendingTransition(R.anim.activity_enter_slidein_anim,
									R.anim.activity_exit_slideout_anim);
						}
					}
				});
		httpRequest.execute();
	}

	private boolean isCodeFormOk(String code) {
		if (!TextUtils.isEmpty(code))
			return true;

		invalidFiledIndicator.setVisibility(View.VISIBLE);
		return false;

	}

	private void setImage() {
		if (mBitmap != null) {
			int h = ivProfilePic.getLayoutParams().height;
			int w = ivProfilePic.getLayoutParams().width;
			ivProfilePic.setImageBitmap(mBitmap);
			ivProfilePic.getLayoutParams().height = h;
			ivProfilePic.getLayoutParams().width = w;
		} else
			ivProfilePic.setImageDrawable(getResources().getDrawable(R.drawable.profile_avatar));
	}

	@Override
	protected void onDestroy() {
		unregisterReceiver(mReceiver);
		super.onDestroy();
	}

}
