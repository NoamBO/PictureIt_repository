package utilities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

//Written by @Noam Bar-Oz

public class OutgoingCommunication {

	/**
	 * This method will launch the dialer with phone number
	 * 
	 * @param activity
	 *            Activity or Context
	 * @param phoneNumber
	 *            The phone number that the user see on his dialer
	 * @return void
	 */
	public static void call(Activity activity, String phoneNumber) {
		Intent callIntent = new Intent(Intent.ACTION_DIAL);
		callIntent.setData(Uri.parse("tel:" + phoneNumber));
		activity.startActivity(callIntent);
	}

	public static void sendSms(Activity activity, String phoneNumber) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setData(Uri.parse("sms:" + phoneNumber));
		activity.startActivity(intent);
	}

	public static void sendEMail(Activity activity, String address) {
		Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", address, null));
		// emailIntent.putExtra(Intent.EXTRA_SUBJECT, "EXTRA_SUBJECT");
		activity.startActivity(Intent.createChooser(emailIntent, "Send email..."));
	}
}
