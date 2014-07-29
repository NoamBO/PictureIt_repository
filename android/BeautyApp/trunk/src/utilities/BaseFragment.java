package utilities;

import android.app.Fragment;
import android.view.View;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.pictureit.noambaroz.beautyapp.server.ImageLoaderUtil;

//Written by @Noam Bar-Oz

public class BaseFragment extends Fragment {

	protected void showErrorDialog() {
		Dialogs.generalDialog(getActivity(), Dialogs.somthing_went_wrong);
	}

	@SuppressWarnings("unchecked")
	protected <T extends View> T findView(int id) {
		return (T) findView(id);
	}

	@SuppressWarnings("unchecked")
	protected <T extends View> T findView(View rootView, int id) {
		return (T) rootView.findViewById(id);
	}

	protected DisplayImageOptions options = ImageLoaderUtil.getDisplayImageOptions();

	protected com.nostra13.universalimageloader.core.ImageLoader imageLoader = com.nostra13.universalimageloader.core.ImageLoader
			.getInstance();
}
