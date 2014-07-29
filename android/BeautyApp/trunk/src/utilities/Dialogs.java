package utilities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;

public class Dialogs {
	
	public static final String no_internet_connection = "no internet connection";
	public static final String somthing_went_wrong = "somthing went wrong";

	public static void generalDialog(Activity activity, String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setMessage(message);
		Dialog d = builder.create();
		d.show();
	}
}
