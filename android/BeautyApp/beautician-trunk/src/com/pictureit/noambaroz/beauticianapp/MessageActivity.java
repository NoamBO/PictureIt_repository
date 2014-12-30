package com.pictureit.noambaroz.beauticianapp;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pictureit.noambaroz.beauticianapp.data.MessageResponse;
import com.pictureit.noambaroz.beauticianapp.dialog.Dialogs;
import com.pictureit.noambaroz.beauticianapp.server.HttpBase.HttpCallback;
import com.pictureit.noambaroz.beautycianapp.R;

public class MessageActivity extends ActivityWithFragment {

	@Override
	public void onBackPressed() {
		backPressed();
	}

	@Override
	protected void initActivity() {
	}

	@Override
	protected void setFragment() {
		fragment = new FragmentMessage();
	}

	@Override
	protected void setFragmentTag() {
		FRAGMENT_TAG = "single_message_fragment";
	}

	private class FragmentMessage extends Fragment implements HttpCallback {

		private MessageResponse mMessageResponse;

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View v = inflater.inflate(R.layout.fragment_message, container, false);
			// TODO
			// GetMessageById
			return v;
		}

		@Override
		public void onAnswerReturn(Object answer) {
			if (answer == null)
				Dialogs.showServerFailedDialog(getActivity());
			else {
				mMessageResponse = (MessageResponse) answer;
				setScreen();
			}
		}
	}

	public void setScreen() {
		// TODO Auto-generated method stub

	}

}
