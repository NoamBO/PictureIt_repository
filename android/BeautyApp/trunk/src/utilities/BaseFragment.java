package utilities;

import android.app.Fragment;
import android.view.View;

//Written by @Noam Bar-Oz

public class BaseFragment extends Fragment {

	protected void showErrorDialog() {
		Dialogs.generalDialog(getActivity(), Dialogs.somthing_went_wrong);
	}

	@SuppressWarnings("unchecked")
	protected <T extends View> T findView(View rootView, int id) {
		return (T) rootView.findViewById(id);
	}

}
