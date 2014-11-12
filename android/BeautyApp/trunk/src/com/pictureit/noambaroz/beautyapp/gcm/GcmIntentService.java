package com.pictureit.noambaroz.beautyapp.gcm;

import utilities.Log;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.pictureit.noambaroz.beautyapp.ActivityMessages;
import com.pictureit.noambaroz.beautyapp.R;
import com.pictureit.noambaroz.beautyapp.data.DataUtil;

public class GcmIntentService extends IntentService {
	public static final int NOTIFICATION_ID = 1;
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

		if (!extras.isEmpty()) { // has effect of unparcelling Bundle
			/*
			 * Filter messages based on message type. Since it is likely that
			 * GCM will be extended in the future with new message types, just
			 * ignore any message types you're not interested in, or that you
			 * don't recognize.
			 */
			if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
				// sendNotification("Send error: " + extras.toString(), extras);
			} else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
				// sendNotification("Deleted messages on server: " +
				// extras.toString(), extras);
				// If it's a regular GCM message, do some work.
			} else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
				onMessageNotification(extras);
				// Post notification of received message.
				sendNotification(extras);
				Log.i("Received: " + extras.toString());
			}
		}
		// Release the wake lock provided by the WakefulBroadcastReceiver.
		GcmBroadcastReceiver.completeWakefulIntent(intent);
	}

	// Put the message into a notification and post it.
	// This is just one simple example of what you might choose to do with
	// a GCM message.
	private void sendNotification(Bundle data) {
		mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

		Intent notificationIntent = new Intent(this, ActivityMessages.class);
		// notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
		// Intent.FLAG_ACTIVITY_SINGLE_TOP);
		notificationIntent.putExtras(data);

		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
		// Notification.FLAG_ONGOING_EVENT | Notification.FLAG_NO_CLEAR);

		String notificationMessage = data.getString("from");
		Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
				.setSmallIcon(R.drawable.ic_stat_gcm)
				.setContentTitle(getResources().getString(R.string.push_notification_title))
				.setStyle(
						new NotificationCompat.BigTextStyle().bigText(getResources().getString(
								R.string.push_notification_message)))
				.setContentText(getResources().getString(R.string.push_notification_message)).setAutoCancel(true)
				.setSound(uri);

		mBuilder.setContentIntent(contentIntent);
		mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
	}

	private void onMessageNotification(Bundle json) {
		String notificationId = json.getString("alert");
		if (notificationId != null)
			DataUtil.pushOrderNotificationIdToTable(getApplicationContext(), notificationId);
	}
}
