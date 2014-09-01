package utilities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

import com.pictureit.noambaroz.beautyapp.R;

public class Dialogs {

	public static final String no_internet_connection = "no internet connection";
	public static final String somthing_went_wrong = "somthing went wrong";

	public interface OnDialogItemSelectedListener {
		public void onDialogItemSelected(String selection);
	}

	public static void generalDialog(Activity activity, String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setMessage(message);
		Dialog d = builder.create();
		d.show();
	}

	public static void closeActivity(final Activity activity, String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setMessage(message);
		builder.setPositiveButton(activity.getResources().getString(R.string.dialog_ok_text), new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				activity.finish();
			}
		});
		Dialog d = builder.create();
		d.setCanceledOnTouchOutside(false);
		d.show();
	}

	public static void singleChoiseDialog(Context ctx, final String[] strings,
			final OnDialogItemSelectedListener listener) {
		AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
		builder.setTitle(R.string.select_address).setItems(strings, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				listener.onDialogItemSelected(strings[which]);
			}
		});
		builder.create().show();
	}
}
