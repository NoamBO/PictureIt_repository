package com.pictureit.noambaroz.beautyapp;

import utilities.view.MyBitmapHelper;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class ActivityMyProfile extends ActivityWithFragment {

	private int EDIT_BUTTON_ID = 123123;

	public static int PROFILE_PIC_DRAWABLE_RESOURCE_ID = R.drawable.profile_avatar;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		ImageButton button = new ImageButton(ActivityMyProfile.this);
		getMenuInflater().inflate(R.menu.menu_activity_treatments, menu);
		menu.findItem(R.id.action_ask_for_service).setVisible(false);

		button.setPadding(5, 0, 20, 0);
		button.setBackgroundResource(R.drawable.ic_edit);
		final MenuItem m = menu.add(0, EDIT_BUTTON_ID, 1, R.string.edit);
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
		if (item.getItemId() == EDIT_BUTTON_ID) {
			startActivity(new Intent(ActivityMyProfile.this, ActivityMyProfileEdit.class));
			overridePendingTransition(R.anim.activity_enter_slidein_anim, R.anim.activity_exit_shrink_anim);
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		backPressed();
	}

	@Override
	protected void initActivity() {
	}

	@Override
	protected void setFragment() {
		fragment = new FragmentMyProfile();
	}

	@Override
	protected void setFragmentTag() {
		FRAGMENT_TAG = "my_profile_activity";
	}

	class FragmentMyProfile extends Fragment {

		private TextView tvName, tvPhone, tvEmail, tvAddress;
		private ImageView ivImage;
		private SharedPreferences mPrefs;

		public FragmentMyProfile() {
		}

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			mPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View v = inflater.inflate(R.layout.fragment_my_profile, container, false);
			tvName = findView(v, R.id.tv_my_profile_full_name);
			tvPhone = findView(v, R.id.tv_my_profile_telephone_num);
			tvEmail = findView(v, R.id.tv_my_profile_email);
			tvAddress = findView(v, R.id.tv_my_profile_address);
			ivImage = findView(v, R.id.iv_my_profile_image);
			return v;
		}

		@Override
		public void onResume() {
			super.onResume();
			setText();
			setImage();
		}

		private void setImage() {
			Bitmap b = MyBitmapHelper.decodeBase64(mPrefs.getString(
					getString(R.string.preference_key_my_profile_picture), ""));
			if (b != null)
				ivImage.setImageBitmap(b);
			else
				ivImage.setImageDrawable(getResources().getDrawable(PROFILE_PIC_DRAWABLE_RESOURCE_ID));
		}

		private void setText() {
			String name = mPrefs.getString(getString(R.string.preference_key_my_profile_first_name), "") + " "
					+ mPrefs.getString(getString(R.string.preference_key_my_profile_last_name), "");
			tvName.setText(name);
			tvAddress.setText(mPrefs.getString(getString(R.string.preference_key_my_profile_address), ""));
			tvEmail.setText(mPrefs.getString(getString(R.string.preference_key_my_profile_email), ""));
			tvPhone.setText(mPrefs.getString(getString(R.string.preference_key_my_profile_phone_number), ""));
		}
	}

}
