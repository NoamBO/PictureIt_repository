package com.pictureit.noambaroz.beautyapp;

import utilities.Dialogs;
import utilities.server.HttpBase;
import utilities.server.HttpBase.HttpCallback;
import android.app.Activity;
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

		private final String CODE_INVALID = "invalid";
		private final String CODE_VALID = "valid";

		private EditText editText;
		private TextView tvCounter;
		private ViewGroup bProceed;
		private Button bReSendCode;

		private CountDownTimer mTimer;
		private boolean isCounting;

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View v = inflater.inflate(R.layout.fragment_registration_page_3, container, false);

			editText = findView(v, R.id.et_registration_page_three);
			bProceed = findView(v, R.id.rl_registration_page_three_proceed);
			bReSendCode = findView(v, R.id.b_registration_page_three_resend);
			tvCounter = findView(v, R.id.tv_registration_page_three_counter);

			return v;
		}

		@Override
		public void onResume() {
			super.onResume();
			editText.setLongClickable(false);
			bReSendCode.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					new PostReSendPhoneVarificationCode(getActivity(), new HttpCallback() {

						@Override
						public void onAnswerReturn(Object answer) {
							if (answer == null) {
								HttpBase.showErrorDialog(getActivity());
								return;
							}
							if ((Boolean) answer) {
								Dialogs.generalDialog(getActivity(), getString(R.string.code_resended));
								startCountDown(false);
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
						showExpiredCodeDialog();
						return;
					}
					if (editText.getText().length() == 6)
						new PostVerificationRegisterCode(getActivity(), new HttpCallback() {

							@Override
							public void onAnswerReturn(Object answer) {
								if (answer == null) {
									HttpBase.showErrorDialog(getActivity());
									return;
								}
								String result = (String) answer;
								if (result.equalsIgnoreCase(CODE_VALID)) {
									Intent intent = new Intent(getActivity(), MainActivity.class);
									startActivity(intent);
									overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
									finish();
								} else {
									onIncorrectCodeTyped(result);
								}
							}
						}, editText.getText().toString()).execute();
				}
			});

			startCountDown(isCounting);
		}

		private void onIncorrectCodeTyped(String result) {
			if (result.equalsIgnoreCase(CODE_INVALID))
				Dialogs.generalDialog(getActivity(), getString(R.string.invalid_phone_verification_code),
						getString(R.string.dialog_title_error));
			else {
				showExpiredCodeDialog();
				cancelTimer();
			}
		}

		private void showExpiredCodeDialog() {
			Dialogs.generalDialog(getActivity(), getString(R.string.code_expaired),
					getString(R.string.dialog_title_error));
		}

		public void cancelTimer() {
			if (mTimer == null)
				return;

		}

		private void startCountDown(boolean isCount) {
			if (isCount)
				return;

			if (mTimer != null)
				mTimer.cancel();

			mTimer = new CountDownTimer(TIMER_TOTAL_TIME, TIMER_INTERVAL) {

				Activity activity = getActivity();

				@Override
				public void onTick(long millisUntilFinished) {
					int seconds = (int) (millisUntilFinished / 1000);
					int min = (int) ((millisUntilFinished / 1000) / 60);
					int sec = seconds < min ? (min * 60) - seconds : seconds - (min * 60);

					isCounting = true;
					StringBuilder sb = new StringBuilder();
					sb.append(activity.getString(R.string.time_left_to_type_code));
					sb.append("\n");
					sb.append(min);
					sb.append(":");
					sb.append(sec > 9 ? "" : "0");
					sb.append(sec);
					tvCounter.setText(sb.toString());
				}

				@Override
				public void onFinish() {
					isCounting = false;
					tvCounter.setText(activity.getString(R.string.code_expaired));
				}
			};
			mTimer.start();
		}
	}

}
