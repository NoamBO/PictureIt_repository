package com.pictureit.noambaroz.beautyapp;

import utilities.BaseFragment;
import utilities.server.HttpBase.HttpCallback;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.pictureit.noambaroz.beautyapp.data.Constant;
import com.pictureit.noambaroz.beautyapp.data.DataUtil;
import com.pictureit.noambaroz.beautyapp.server.PostRegister;
import com.pictureit.noambaroz.beautyapp.server.PostVerifyAddress;

public class ActivityRegistrationPersonalData extends Activity {

	protected final int FRAGMENT_CONTAINER = R.id.fragment_container;
	Fragment fragment1 = new FragmentRegistrationPersonalData();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_container);
		if (savedInstanceState == null)
			getFragmentManager().beginTransaction().add(FRAGMENT_CONTAINER, fragment1).commit();
	}

	private class FragmentRegistrationPersonalData extends BaseFragment {

		private ViewGroup bProceed;
		private EditText etFirstName, etLastName, etEmail, etAddress;
		private ViewGroup invalidMailIndicator;

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View v = inflater.inflate(R.layout.fragment_registration, container, false);
			bProceed = findView(v, R.id.rl_registration_page_one_proceed);
			etFirstName = findView(v, R.id.et_registration_first_name);
			etLastName = findView(v, R.id.et_registration_last_name);
			etEmail = findView(v, R.id.et_registration_email);
			etAddress = findView(v, R.id.et_registration_address);
			invalidMailIndicator = findView(v, R.id.fl_first_registration_screen_invalid_email_alert);
			return v;
		}

		@Override
		public void onResume() {
			super.onResume();
			bProceed.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (verifyFeilds()) {
						verifyAddress(etAddress.getText().toString());
					}
				}
			});
		}

		protected void verifyAddress(String address) {
			PostVerifyAddress httpPost = new PostVerifyAddress(getActivity(), new HttpCallback() {

				@Override
				public void onAnswerReturn(Object answer) {
					if (answer != null) {
						setValidAddress((String) answer);
						setPhoneNumberFragment();
					} else {
						Toast.makeText(getActivity(), "invalid address", Toast.LENGTH_SHORT).show();
					}

				};

			}, address);
			httpPost.execute();
		}

		private void setValidAddress(String address) {
			etAddress.setText(address);
		}

		private void setPhoneNumberFragment() {
			DataUtil.updateMyProfile(getActivity(), etFirstName.getText().toString(), etLastName.getText().toString(),
					etEmail.getText().toString(), etAddress.getText().toString());

			getFragmentManager().beginTransaction().replace(FRAGMENT_CONTAINER, new FragmentRegistrationPhoneField())
					.addToBackStack(null).commit();
		}

		protected boolean verifyFeilds() {
			boolean isOk = true;
			if (etFirstName.getText().toString().equalsIgnoreCase("")) {
				etFirstName.setHintTextColor(getResources().getColor(android.R.color.holo_red_light));
				isOk = false;
			}
			if (etLastName.getText().toString().equalsIgnoreCase("")) {
				etLastName.setHintTextColor(getResources().getColor(android.R.color.holo_red_light));
				isOk = false;
			}
			if (etAddress.getText().toString().equalsIgnoreCase("")) {
				etAddress.setHintTextColor(getResources().getColor(android.R.color.holo_red_light));
				isOk = false;
			}
			if (etEmail.getText().toString().equalsIgnoreCase("")) {
				etEmail.setHintTextColor(getResources().getColor(android.R.color.holo_red_light));
				isOk = false;
			}
			if (!isOk) {
				new AlertDialog.Builder(getActivity())
						.setMessage(getResources().getString(R.string.dialog_error_message_must_fill_all_feilde))
						.setTitle(getResources().getString(R.string.dialog_title_error)).create().show();
			}
			if (!android.util.Patterns.EMAIL_ADDRESS.matcher(etEmail.getText().toString()).matches()) {
				invalidMailIndicator.setVisibility(View.VISIBLE);
				isOk = false;
			} else {
				invalidMailIndicator.setVisibility(View.GONE);

			}
			return isOk;
		}
	}

	private class FragmentRegistrationPhoneField extends BaseFragment {

		private String phoneNumber;
		private EditText etTelephoneNum;
		private ViewGroup bProceed;

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View v = inflater.inflate(R.layout.fragment_registration_page_two, container, false);
			etTelephoneNum = findView(v, R.id.et_registration_telephone_number);
			bProceed = findView(v, R.id.rl_registration_page_two_proceed);
			etTelephoneNum.setText(DataUtil.getLocalPhoneNumber(getActivity()));
			return v;
		}

		@Override
		public void onResume() {
			super.onResume();
			etTelephoneNum.requestFocus();
			bProceed.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					phoneNumber = etTelephoneNum.getText().toString();
					SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

					PostRegister httpPost = new PostRegister(getActivity(), new HttpCallback() {

						@Override
						public void onAnswerReturn(Object uid) {
							if (uid == null || ((String) uid).equalsIgnoreCase("")) {
								Toast t = Toast.makeText(getApplicationContext(), R.string.dialog_messege_server_error,
										Toast.LENGTH_LONG);
								t.setGravity(Gravity.CENTER, 0, 0);
								t.show();
								return;
							}
							storeUidOnPreference(uid);
							DataUtil.storePhoneNumberOnPreference(getActivity(), phoneNumber);
							Intent intent = new Intent(getActivity(), ActivityRegistrationPhoneAuthentication.class);
							startActivity(intent);
							overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
							finish();
						}

						private void storeUidOnPreference(Object uid) {
							SharedPreferences prefs = getSharedPreferences(Constant.APP_PREFS_NAME,
									Context.MODE_PRIVATE);
							SharedPreferences.Editor editor = prefs.edit();
							editor.putString(Constant.PREFS_KEY_UID, (String) uid);
							editor.commit();
						}
					}, prefs.getString(getString(R.string.preference_key_my_profile_first_name), ""), prefs.getString(
							getString(R.string.preference_key_my_profile_last_name), ""), prefs.getString(
							getString(R.string.preference_key_my_profile_email), ""), prefs.getString(
							getString(R.string.preference_key_my_profile_address), ""), phoneNumber);
					httpPost.execute();
				}
			});
		}

	}

}
