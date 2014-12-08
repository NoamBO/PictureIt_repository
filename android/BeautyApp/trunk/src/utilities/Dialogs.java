package utilities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.pictureit.noambaroz.beautyapp.R;
import com.pictureit.noambaroz.beautyapp.customdialogs.MyCustomDialog;

public class Dialogs {

	public static final String no_internet_connection = "no internet connection";
	public static final String somthing_went_wrong = "somthing went wrong";

	public interface OnDialogItemSelectedListener {
		public void onDialogItemSelected(String selection);
	}

	public static void generalDialog(Activity activity, int message, int title) {
		generalDialog(activity, activity.getString(message), activity.getString(title));
	}

	public static void generalDialog(Activity activity, String message) {
		generalDialog(activity, message, null);
	}

	public static void generalDialog(Activity activity, String message, String title) {
		MyCustomDialog dialog = new MyCustomDialog(activity);
		dialog.setMessage(message);
		if (title != null)
			dialog.setDialogTitle(title);
		dialog.setPositiveButton(activity.getString(R.string.dialog_ok_text), new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
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
			final OnDialogItemSelectedListener listener, String title) {
		AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
		builder.setTitle(title).setItems(strings, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				listener.onDialogItemSelected(strings[which]);
			}
		});
		builder.create().show();
	}

	public static TextView getDialogTitleTextView(Context context, String title) {
		TextView textView = new TextView(context);
		textView.setText(title);
		textView.setPadding(10, 10, 10, 10);
		textView.setGravity(Gravity.CENTER);
		textView.setTextColor(context.getResources().getColor(android.R.color.holo_blue_light));
		textView.setTextSize(30);
		return textView;
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
		MyCustomDialog dialog = new MyCustomDialog(context);
		dialog.setDialogTitle(R.string.dialog_title_error);
		dialog.setMessage(R.string.dialog_messege_server_error);
		dialog.setPositiveButton(R.string.dialog_ok_text, null);
		dialog.show();
	}

	public static void successToast(Context ctx) {
		Toast.makeText(ctx, ctx.getResources().getString(R.string.success), Toast.LENGTH_SHORT).show();
	}
}
