package utilities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.WindowManager;
import android.widget.Toast;

import com.pictureit.noambaroz.beautyapp.R;
import com.pictureit.noambaroz.beautyapp.customdialogs.ErrorDialog;
import com.pictureit.noambaroz.beautyapp.customdialogs.MyCustomDialog;

public class Dialogs {

	public static final String somthing_went_wrong = "somthing went wrong";

	public interface OnDialogItemSelectedListener {
		public void onDialogItemSelected(String selection);
	}

	// public static void generalDialog(Activity activity, int message, int
	// title) {
	// generalDialog(activity, activity.getString(message),
	// activity.getString(title));
	// }
	//
	public static void generalDialog(Activity activity, String message) {
		generalDialog(activity, message, null);
	}

	public static void generalDialog(Context context, String message, String title) {
		MyCustomDialog dialog = new MyCustomDialog(context);
		dialog.setMessage(message);
		if (title != null)
			dialog.setDialogTitle(title);
		dialog.setPositiveButton(context.getString(R.string.dialog_ok_text), new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
	}

	public static void makeToastThatCloseActivity(Activity activity, int stringResId) {
		Toast.makeText(activity, stringResId, Toast.LENGTH_LONG).show();
		activity.finish();
	}

	public static void setDialogWidth(Dialog dialog) {
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		lp.copyFrom(dialog.getWindow().getAttributes());
		lp.width = WindowManager.LayoutParams.MATCH_PARENT;
		lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
		dialog.getWindow().setAttributes(lp);
	}

	public static void showServerFailedDialog(Context context) {
		showErrorDialog(context, R.string.dialog_messege_server_error);
	}

	public static void showErrorDialog(Context context, int messageResId) {
		ErrorDialog d = new ErrorDialog(context);
		d.setMessage(messageResId).setOnClickListener(null).show();
	}

	public static void successToast(Context ctx) {
		Toast.makeText(ctx, ctx.getResources().getString(R.string.success), Toast.LENGTH_SHORT).show();
	}
}
