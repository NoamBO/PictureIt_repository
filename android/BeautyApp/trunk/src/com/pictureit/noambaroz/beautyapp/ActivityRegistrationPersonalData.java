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
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.pictureit.noambaroz.beautyapp.data.Constant;
import com.pictureit.noambaroz.beautyapp.server.PostRegister;
import com.pictureit.noambaroz.beautyapp.server.PostVerifyAddress;

public class ActivityRegistrationPersonalData extends Activity {

	private String mFirstName, mLastName, mEmail, mAddress, mPhoneNumber;
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

		private Button bProceed;
		private EditText etFirstName, etLastName, etEmail, etAddress;

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View v = inflater.inflate(R.layout.fragment_registration, container, false);
			bProceed = findView(v, R.id.b_registration_page_one_proceed);
			etFirstName = findView(v, R.id.et_registration_first_name);
			etLastName = findView(v, R.id.et_registration_last_name);
			etEmail = findView(v, R.id.et_registration_email);
			etAddress = findView(v, R.id.et_registration_address);
			return v;
		}

		@Override
		public void onResume() {
			super.onResume();
			bProceed.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (verifyFeilds()) {
						if (etAddress.getText().toString() != null
								&& !etAddress.getText().toString().equalsIgnoreCase("")) {
							verifyAddress(etAddress.getText().toString());
						} else
							setPhoneNumberFragment();
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
			mFirstName = etFirstName.getText().toString();
			mLastName = etLastName.getText().toString();
			mEmail = etEmail.getText().toString();
			mAddress = etAddress.getText().toString();
			getFragmentManager().beginTransaction().add(FRAGMENT_CONTAINER, new FragmentRegistrationPhoneField())
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
			if (etEmail.getText().toString().equalsIgnoreCase("")) {
				etEmail.setHintTextColor(getResources().getColor(android.R.color.holo_red_light));
				isOk = false;
			} else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(etEmail.getText().toString()).matches()) {
				etEmail.setText("");
				etEmail.setHint(getResources().getString(R.string.please_write_valid_email_string));
				etEmail.setHintTextColor(getResources().getColor(android.R.color.holo_red_light));
				isOk = false;
			}
			if (etAddress.getText().toString().equalsIgnoreCase("")) {
				etAddress.setHintTextColor(getResources().getColor(android.R.color.holo_red_light));
				isOk = false;
			}
			if (!isOk) {
				new AlertDialog.Builder(getActivity())
						.setMessage(getResources().getString(R.string.dialog_error_message_must_fill_all_feilde))
						.setTitle(getResources().getString(R.string.dialog_title_error)).create().show();
			}
			return isOk;
		}
	}

	private class FragmentRegistrationPhoneField extends BaseFragment {

		private EditText etTelephoneNum;
		private Button bProceed;

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View v = inflater.inflate(R.layout.fragment_registration_page_two, container, false);
			etTelephoneNum = findView(v, R.id.et_registration_telephone_number);
			bProceed = findView(v, R.id.b_registration_page_two_proceed);
			etTelephoneNum.setText(getLocalPhoneNumber());
			return v;
		}

		@Override
		public void onResume() {
			super.onResume();
			etTelephoneNum.requestFocus();
			bProceed.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					mPhoneNumber = etTelephoneNum.getText().toString();
					PostRegister httpPost = new PostRegister(getActivity(), new HttpCallback() {

						@Override
						public void onAnswerReturn(Object uid) {
							if (uid == null || ((String) uid).equalsIgnoreCase("")) {
								Toast.makeText(getApplicationContext(), R.string.dialog_messege_server_error,
										Toast.LENGTH_SHORT).show();
								finish();
							}
							storeUidOnDevice(uid);
							Intent intent = new Intent(getActivity(), MainActivity.class);
							startActivity(intent);
							overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
							finish();
						}

						private void storeUidOnDevice(Object uid) {
							SharedPreferences prefs = getSharedPreferences(Constant.APP_PREFS_NAME,
									Context.MODE_PRIVATE);
							SharedPreferences.Editor editor = prefs.edit();
							editor.putString(Constant.PREFS_KEY_UID, (String) uid);
							editor.commit();
						}
					}, mFirstName, mLastName, mEmail, mAddress, mPhoneNumber);
					httpPost.execute();
				}
			});
		}

		private String getLocalPhoneNumber() {
			TelephonyManager tMgr = (TelephonyManager) getApplicationContext().getSystemService(
					Context.TELEPHONY_SERVICE);
			return tMgr.getLine1Number();
		}

	}

}
