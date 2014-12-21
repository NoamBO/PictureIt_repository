package com.pictureit.noambaroz.beautyapp;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.pictureit.noambaroz.beautyapp.data.DataProvider;

public class ActivityTermsOfService extends ActivityWithFragment {

	@Override
	public void onBackPressed() {
		backPressed();
	}

	@Override
	protected void initActivity() {
	}

	@Override
	protected void setFragment() {
		fragment = new FragmentTermsOfService();
	}

	@Override
	protected void setFragmentTag() {
		FRAGMENT_TAG = "terms_of_service";
	}

	private class FragmentTermsOfService extends Fragment {

		public FragmentTermsOfService() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			ViewGroup v = (ViewGroup) inflater.inflate(R.layout.fragment_terms_of_service, container, false);
			createButtonToDeleteAllRowInMessagesTable(v);
			return v;
		}

		// TODO delete
		private void createButtonToDeleteAllRowInMessagesTable(ViewGroup v) {
			Button b = new Button(getActivity());
			b.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT));
			b.setText("מחק את כל ההודעות!\n(לא ניתן לשחזור)");
			b.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					getActivity().getContentResolver().delete(DataProvider.CONTENT_URI_MESSAGES, null, null);
				}
			});
			v.addView(b);
		}
	}

}
