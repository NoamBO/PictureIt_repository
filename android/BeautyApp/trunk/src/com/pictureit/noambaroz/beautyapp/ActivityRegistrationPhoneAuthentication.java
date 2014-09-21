package com.pictureit.noambaroz.beautyapp;

import utilities.Dialogs;
import utilities.server.HttpBase.HttpCallback;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.pictureit.noambaroz.beautyapp.server.PostReSendPhoneVarificationCode;
import com.pictureit.noambaroz.beautyapp.server.PostVerificationRegisterCode;

public class ActivityRegistrationPhoneAuthentication extends ActivityWithFragment {

	@Override
	protected void initActivity() {
		initActionBar = false;
	}

	@Override
	protected void setFragment() {
		fragment = new FragmentRegistrationPhoneAuthentication();
	}

	@Override
	protected void setFragmentTag() {
		FRAGMENT_TAG = "phone_authentication_fragment";
	}

	private class FragmentRegistrationPhoneAuthentication extends Fragment {

		private final long TIMER_INTERVAL = 1000; // 1 seconds
		private final long TIMER_TOTAL_TIME = 1000 * 60 * 2; // 2 minutes

		private EditText editText;
		private TextView tvCounter;
		private Button bProceed;
		private Button bReSendCode;

		private CountDownTimer mTimer;
		private boolean isCounting;

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View v = inflater.inflate(R.layout.fragment_registration_page_3, container, false);

			editText = findView(v, R.id.et_registration_page_three);
			bProceed = findView(v, R.id.b_registration_page_three_proceed);
			bReSendCode = findView(v, R.id.b_registration_page_three_resend);
			tvCounter = findView(v, R.id.tv_registration_page_three_counter);

			return v;
		}

		@Override
		public void onResume() {
			super.onResume();
			bReSendCode.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					new PostReSendPhoneVarificationCode(getActivity(), new HttpCallback() {

						@Override
						public void onAnswerReturn(Object answer) {
							if ((Boolean) answer) {
								startCountDown();
							} else {
								Dialogs.generalDialog(getActivity(), getString(R.string.failed_on_re_sending_code),
										getString(R.string.dialog_title_error));
							}
						}
					}).execute();
				}
			});
			bProceed.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (!isCounting) {
						Dialogs.generalDialog(getActivity(), getString(R.string.code_expaired),
								getString(R.string.dialog_title_error));
						return;
					}
					if (editText.getText().length() == 6)
						new PostVerificationRegisterCode(getActivity(), new HttpCallback() {

							@Override
							public void onAnswerReturn(Object answer) {
								if ((Boolean) answer) {
									Intent intent = new Intent(getActivity(), MainActivity.class);
									startActivity(intent);
									overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
									finish();
								} else {
									onIncorrectCodeTyped();
								}
							}
						}, editText.getText().toString()).execute();
				}
			});

			startCountDown();
		}

		private void onIncorrectCodeTyped() {
			Dialogs.generalDialog(getActivity(), getString(R.string.invalid_phone_verification_code),
					getString(R.string.dialog_title_error));
		}

		private void startCountDown() {
			if (isCounting)
				return;

			mTimer = new CountDownTimer(TIMER_TOTAL_TIME, TIMER_INTERVAL) {

				@Override
				public void onTick(long millisUntilFinished) {
					isCounting = true;
					tvCounter.setText(String.valueOf(millisUntilFinished / 1000));
				}

				@Override
				public void onFinish() {
					isCounting = false;
				}
			};
			mTimer.start();
		}
	}

}
