package com.pictureit.noambaroz.beauticianapp.gcm;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.gson.Gson;
import com.pictureit.noambaroz.beauticianapp.Log;
import com.pictureit.noambaroz.beauticianapp.ActivityMessages;
import com.pictureit.noambaroz.beauticianapp.data.DataUtils;
import com.pictureit.noambaroz.beauticianapp.data.OrderAroundMe;
import com.pictureit.noambaroz.beautycianapp.R;

public class GcmIntentService extends IntentService {
	public static final int NOTIFICATION_ID = 1;

	private static final String NOTIFICATION_TYPE_MESSAGE = "message";
	private static final String NOTIFICATION_TYPE = "type";

	private static final String FROM = "from";
	private static final String ORDER_ID = "beauticianresponseid";

	// private NotificationManager mNotificationManager;
	NotificationCompat.Builder builder;
	private NotificationManager mNotificationManager;

	public GcmIntentService() {
		super("GcmIntentService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Bundle extras = intent.getExtras();
		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
		// The getMessageType() intent parameter must be the intent you received
		// in your BroadcastReceiver.
		String messageType = gcm.getMessageType(intent);
		Log.i("notification arrived");
		if (!extras.isEmpty()) { // has effect of unparcelling Bundle
			/*
			 * Filter messages based on message type. Since it is likely that
			 * GCM will be extended in the future with new message types, just
			 * ignore any message types you're not interested in, or that you
			 * don't recognize.
			 */
			// If it's a regular GCM message, do some work.
			if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
				String notificationType = null;
				if (extras.containsKey(NOTIFICATION_TYPE) && extras.containsKey("data")) {
					notificationType = extras.getString(notificationType);
					if (!TextUtils.isEmpty(notificationType)) {
						if (notificationType.equalsIgnoreCase(NOTIFICATION_TYPE_MESSAGE))
							onMessageArrived(extras.get("data").toString());
					}
				}

			} else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
			} else if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
			}
		}
		// Release the wake lock provided by the WakefulBroadcastReceiver.
		GcmBroadcastReceiver.completeWakefulIntent(intent);
	}

	private void onMessageArrived(String orderAroundMe) {
		OrderAroundMe oam = new Gson().fromJson(orderAroundMe, OrderAroundMe.class);
		if (oam == null || oam.getOrderid() == null)
			return;
		oam.setDirectedToMe("true");
		DataUtils.get(getApplicationContext()).addOrderAroundMe(oam, true);
		// if (!isAppRunningInForeground()) {
		String title = getString(R.string.request_received);
		String message = getString(R.string.new_request_is_waiting_for_you_inside_the_app);
		sendNotification(ActivityMessages.class, null, message, title);
		// } else {
		// TODO
		// }
	}

	// Put the message into a notification and post it.
	// This is just one simple example of what you might choose to do with
	// a GCM message.
	private void sendNotification(Class<?> classToLoad, Bundle data, String message, String title) {
		mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

		Intent notificationIntent;
		if (classToLoad != null)
			notificationIntent = new Intent(this, classToLoad);
		else
			notificationIntent = new Intent();
		if (data != null)
			notificationIntent.putExtras(data);
		notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

		Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
				.setSmallIcon(R.drawable.ic_notification).setContentTitle(title)
				.setStyle(new NotificationCompat.BigTextStyle().bigText(message)).setContentText(message)
				.setAutoCancel(true).setSound(uri);

		builder.setContentIntent(contentIntent);
		mNotificationManager.notify(NOTIFICATION_ID, builder.build());
	}

	private boolean isAppRunningInForeground() {

		ActivityManager activityManager = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> services = activityManager.getRunningTasks(Integer.MAX_VALUE);
		boolean isActivityFound = false;

		if (services.get(0).topActivity.getPackageName().toString().equalsIgnoreCase(this.getPackageName().toString())) {
			isActivityFound = true;
		}

		return isActivityFound;
	}
}