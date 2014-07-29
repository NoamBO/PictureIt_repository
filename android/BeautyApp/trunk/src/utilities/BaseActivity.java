package utilities;

import android.app.Activity;
import android.view.View;

//Written by @Noam Bar-Oz

public class BaseActivity extends Activity {

	protected void showErrorDialog() {
		Dialogs.generalDialog(BaseActivity.this, Dialogs.somthing_went_wrong);
	}

	@SuppressWarnings("unchecked")
	protected <T extends View> T findView(int id) {
		return (T) findViewById(id);
	}

	@SuppressWarnings("unchecked")
	protected <T extends View> T findView(View rootView, int id) {
		return (T) rootView.findViewById(id);
	}

}
