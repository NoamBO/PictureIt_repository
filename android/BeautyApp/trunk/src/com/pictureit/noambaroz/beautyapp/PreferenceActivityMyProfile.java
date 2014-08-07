package com.pictureit.noambaroz.beautyapp;

import java.io.File;

import utilities.view.PreferenceImageView;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.MultiSelectListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceGroup;
import android.preference.PreferenceScreen;

import com.pictureit.noambaroz.beautyapp.cropimage.CropMenager;

public class PreferenceActivityMyProfile extends ActivityWithFragment {

	@Override
	protected void initActivity() {
	}

	@Override
	protected void setFragment() {
		fragment = new MyProfilePreferenceContainer();
	}

	@Override
	protected void setFragmentTag() {
		FRAGMENT_TAG = "my_profile";
	}

	private class MyProfilePreferenceContainer extends PreferenceFragment implements OnSharedPreferenceChangeListener {

		public void onResume() {
			super.onResume();
			getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
		};

		public void onPause() {
			super.onPause();
			getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
		};

		PreferenceImageView prefImage;
		CropMenager cropUtil;

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(R.xml.preference_my_profile);
			prefImage = (PreferenceImageView) findPreference(getString(R.string.preference_key_my_profile_picture));
			cropUtil = new CropMenager();
			initSummary(getPreferenceScreen());
		}

		@Override
		public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
			if (preference.getKey().equalsIgnoreCase(getString(R.string.preference_key_my_profile_picture))) {
				selectImage();
			}
			return super.onPreferenceTreeClick(preferenceScreen, preference);
		}

		public void selectImage() {
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
					Bitmap photo = extras.getParcelable("data");

					prefImage.setNewImage(photo);
					// TODO update server for new image
				}

				File f = new File(cropUtil.getImageCaptureUri().getPath());

				if (f.exists())
					f.delete();

				break;

			}
		}

		@Override
		public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
			updatePrefSummary(findPreference(key));
		}

		private void initSummary(Preference p) {
			if (p instanceof PreferenceGroup) {
				PreferenceGroup pGrp = (PreferenceGroup) p;
				for (int i = 0; i < pGrp.getPreferenceCount(); i++) {
					initSummary(pGrp.getPreference(i));
				}
			} else {
				updatePrefSummary(p);
			}
		}

		private void updatePrefSummary(Preference p) {
			if (p instanceof ListPreference) {
				ListPreference listPref = (ListPreference) p;
				p.setSummary(listPref.getEntry());
			} else if (p instanceof EditTextPreference) {
				EditTextPreference editTextPref = (EditTextPreference) p;

				p.setSummary(editTextPref.getText());

			} else if (p instanceof MultiSelectListPreference) {
				EditTextPreference editTextPref = (EditTextPreference) p;
				p.setSummary(editTextPref.getText());
			}
		}

	}

}
