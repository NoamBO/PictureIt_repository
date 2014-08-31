package com.pictureit.noambaroz.beautyapp;

import utilities.BaseFragment;
import utilities.server.HttpBase.HttpCallback;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.pictureit.noambaroz.beautyapp.server.PostVerifyAddress;

public class ActivityRegistrationPersonalData extends Activity {
	protected final int FRAGMENT_CONTAINER = R.id.fragment_container;
	Fragment fragment1 = new FragmentRegistrationPersonalData();
	Fragment fragment2 = new FragmentRegistrationPhoneField();

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
			PostVerifyAddress.verify(getActivity(), "", new HttpCallback() {

				@Override
				public void onAnswerReturn(Object answer) {
					// TODO
				}
			});
		}

		private void setPhoneNumberFragment() {
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
			if (!isOk) {
				new AlertDialog.Builder(getActivity())
						.setMessage(getResources().getString(R.string.dialog_error_message_must_fill_all_feilde))
						.setTitle(getResources().getString(R.string.dialog_title_error)).create().show();
			}
			return isOk;
		}
	}

	private class FragmentRegistrationPhoneField extends BaseFragment {

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View v = inflater.inflate(R.layout.fragment_registration_page_two, container, false);
			return v;
		}

	}

}
