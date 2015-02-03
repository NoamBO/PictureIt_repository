package com.pictureit.noambaroz.beautyapp;

import java.io.File;

import utilities.Dialogs;
import utilities.server.HttpBase.HttpCallback;
import utilities.view.MyBitmapHelper;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.pictureit.noambaroz.beautyapp.cropimage.CropMenager;
import com.pictureit.noambaroz.beautyapp.server.PostUpdateProfileData;
import com.pictureit.noambaroz.beautyapp.server.PostUpdateProfilePicture;
import com.pictureit.noambaroz.beautyapp.server.PostVerifyAddress;

public class ActivityMyProfileEdit extends ActivityWithFragment {

	private int FINISH_BUTTON_ID = 234234;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		ImageButton button = new ImageButton(ActivityMyProfileEdit.this);
		getMenuInflater().inflate(R.menu.menu_activity_treatments, menu);
		menu.findItem(R.id.action_ask_for_service).setVisible(false);

		button.setBackgroundResource(R.drawable.ic_finish_edit);
		button.setPadding(5, 0, 20, 0);

		final MenuItem m = menu.add(0, FINISH_BUTTON_ID, 1, R.string.finish);
		m.setActionView(button).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onOptionsItemSelected(m);
			}
		});
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == FINISH_BUTTON_ID) {
			((FragmentMyProfileEdit) fragment).saveIfNeeded();
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void initActivity() {
	}

	@Override
	protected void setFragment() {
		fragment = new FragmentMyProfileEdit();
	}

	@Override
	protected void setFragmentTag() {
		FRAGMENT_TAG = "edit_my_profile_activity";
	}

	@Override
	public void onBackPressed() {
		backPressed();
	}

	class FragmentMyProfileEdit extends Fragment {

		private EditText etFirstName, etLastName, etPhone, etEmail, etAddress;
		private ImageView ivImage;
		private ViewGroup invalidMailIndicator;
		private SharedPreferences mPrefs;
		private boolean isImageChanged;
		private CropMenager cropUtil;
		private String imageBase64String;

		private String tempFirstName, tempLastName, tempAddress, tempEmail;

		public FragmentMyProfileEdit() {
		}

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			mPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
			cropUtil = new CropMenager();
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View v = inflater.inflate(R.layout.fragment_my_profile_editing, container, false);
			etFirstName = findView(v, R.id.et_my_profile_edit_first_name);
			etLastName = findView(v, R.id.et_my_profile_edit_last_name);
			etPhone = findView(v, R.id.et_my_profile_edit_telephone_num);
			etPhone.setEnabled(false);
			etEmail = findView(v, R.id.et_my_profile_edit_email);
			etAddress = findView(v, R.id.et_my_profile_edit_address);
			ivImage = findView(v, R.id.iv_my_profile_edit_image);
			invalidMailIndicator = findView(v, R.id.fl_my_profile_edit_invalid_email_alert);
			return v;
		}

		@Override
		public void onResume() {
			super.onResume();
			setText();
			setImage(mPrefs.getString(getString(R.string.preference_key_my_profile_picture), ""));
			ivImage.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					saveTempData();
					selectImage();
				}

			});
		}

		private void saveTempData() {

			tempFirstName = etFirstName.getText().toString();
			tempLastName = etLastName.getText().toString();
			tempEmail = etEmail.getText().toString();
			tempAddress = etAddress.getText().toString();

		}

		private void selectImage() {
			cropUtil.start(this);
		}

		@Override
		public void onActivityResult(int requestCode, int resultCode, Intent data) {
			if (resultCode != RESULT_OK)
				return;

			switch (requestCode) {
			case CropMenager.PICK_FROM_CAMERA:
				cropUtil.doCrop(this);
				break;
			case CropMenager.PICK_FROM_FILE:
				cropUtil.setImageCaptureUri(data.getData());
				cropUtil.doCrop(this);
				break;
			case CropMenager.CROP_FROM_CAMERA:
				Bundle extras = data.getExtras();
				if (extras != null) {
					final Bitmap photo = extras.getParcelable("data");
					String bitString = MyBitmapHelper.encodeTobase64(photo);
					if (bitString.equalsIgnoreCase(mPrefs.getString(
							getString(R.string.preference_key_my_profile_picture), "")))
						return;
					isImageChanged = true;
					imageBase64String = bitString;
					new Handler().post(new Runnable() {

						@Override
						public void run() {
							setImage(imageBase64String);
						}
					});

				}
				File f = new File(cropUtil.getImageCaptureUri().getPath());

				if (f.exists())
					f.delete();

				break;
			}
		}

		private void setImage(String image) {
			Bitmap b = MyBitmapHelper.decodeBase64(image);
			if (b != null)
				ivImage.setImageBitmap(b);
			else
				ivImage.setImageDrawable(getResources().getDrawable(ActivityMyProfile.PROFILE_PIC_DRAWABLE_RESOURCE_ID));
		}

		private void setText() {

			if (tempFirstName != null)
				etFirstName.setText(tempFirstName);
			else
				etFirstName.setText(mPrefs.getString(getString(R.string.preference_key_my_profile_first_name), ""));
			if (tempLastName != null)
				etLastName.setText(tempLastName);
			else
				etLastName.setText(mPrefs.getString(getString(R.string.preference_key_my_profile_last_name), ""));
			if (tempAddress != null)
				etAddress.setText(tempAddress);
			else
				etAddress.setText(mPrefs.getString(getString(R.string.preference_key_my_profile_address), ""));
			if (tempEmail != null)
				etEmail.setText(tempEmail);
			else
				etEmail.setText(mPrefs.getString(getString(R.string.preference_key_my_profile_email), ""));
			etPhone.setText(mPrefs.getString(getString(R.string.preference_key_my_profile_phone_number), ""));
		}

		public void saveIfNeeded() {
			boolean saving = false;
			if (isDataChanged()) {
				saving = true;
				if (isEmailOk())
					if (verifyAddress()) {
						sendDataToBackEnd();
					}
			}
			if (isImageChanged) {
				saving = true;
				sendImageToBackEnd();
			}
			if (!saving)
				backPressed();
		}

		private boolean isEmailOk() {
			boolean isOk = true;
			if (!android.util.Patterns.EMAIL_ADDRESS.matcher(etEmail.getText().toString()).matches()) {
				invalidMailIndicator.setVisibility(View.VISIBLE);
				isOk = false;
			}
			return isOk;
		}

		private boolean verifyAddress() {
			if (!etAddress.getText().toString()
					.equalsIgnoreCase(mPrefs.getString(getString(R.string.preference_key_my_profile_address), ""))) {
				new PostVerifyAddress(getActivity(), new HttpCallback() {

					@Override
					public void onAnswerReturn(Object answer) {
						if (answer == null)
							etAddress.setText("");
						else
							etAddress.setText((String) answer);
						sendDataToBackEnd();
					}
				}, etAddress.getText().toString()).execute();
				return false;
			} else
				return true;
		}

		private boolean isDataChanged() {
			return (!etFirstName.getText().toString()
					.equalsIgnoreCase(mPrefs.getString(getString(R.string.preference_key_my_profile_first_name), ""))
					|| !etLastName
							.getText()
							.toString()
							.equalsIgnoreCase(
									mPrefs.getString(getString(R.string.preference_key_my_profile_last_name), ""))
					|| !etAddress
							.getText()
							.toString()
							.equalsIgnoreCase(
									mPrefs.getString(getString(R.string.preference_key_my_profile_address), "")) || !etEmail
					.getText().toString()
					.equalsIgnoreCase(mPrefs.getString(getString(R.string.preference_key_my_profile_email), "")));

		}

		private void sendDataToBackEnd() {
			PostUpdateProfileData httpPost = new PostUpdateProfileData(getActivity(), new HttpCallback() {

				@Override
				public void onAnswerReturn(Object answer) {
					if (answer != null) {
						saveData();
						if (!isImageChanged && !isDataChanged())
							backPressed();
					} else
						Dialogs.makeToastThatCloseActivity(getActivity(), R.string.dialog_messege_server_error);
				}
			}, etFirstName.getText().toString(), etLastName.getText().toString(), etAddress.getText().toString(),
					etEmail.getText().toString());
			httpPost.execute();
		}

		private void sendImageToBackEnd() {
			PostUpdateProfilePicture httpPost = new PostUpdateProfilePicture(getActivity(), new HttpCallback() {

				@Override
				public void onAnswerReturn(Object answer) {
					if (answer != null) {
						saveImage();
						isImageChanged = false;
						if (!isDataChanged())
							backPressed();
					} else
						Dialogs.makeToastThatCloseActivity(getActivity(), R.string.dialog_messege_server_error);
				}
			}, imageBase64String);
			httpPost.execute();
		}

		private void saveImage() {
			mPrefs.edit().putString(getString(R.string.preference_key_my_profile_picture), imageBase64String).commit();
		}

		private void saveData() {
			mPrefs.edit()
					.putString(getString(R.string.preference_key_my_profile_first_name),
							etFirstName.getText().toString())
					.putString(getString(R.string.preference_key_my_profile_last_name), etLastName.getText().toString())
					.putString(getString(R.string.preference_key_my_profile_email), etEmail.getText().toString())
					.putString(getString(R.string.preference_key_my_profile_address), etAddress.getText().toString())
					.commit();

		}
	}

}
