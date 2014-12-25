package com.pictureit.noambaroz.beauticianapp;

import android.app.Fragment;
import android.view.View;

public class BaseFragment extends Fragment {

	@SuppressWarnings("unchecked")
	protected <T extends View> T findView(int id) {
		return (T) getActivity().findViewById(id);
	}

	@SuppressWarnings("unchecked")
	protected <T extends View> T findView(View rootView, int id) {
		return (T) rootView.findViewById(id);
	}

}
