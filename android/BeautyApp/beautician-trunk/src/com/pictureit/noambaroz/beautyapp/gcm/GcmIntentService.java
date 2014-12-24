package com.pictureit.noambaroz.beautyapp.gcm;

import org.json.JSONException;
import org.json.JSONObject;

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
import com.pictureit.noambaroz.beauticianapp.Log;
import com.pictureit.noambaroz.beauticianapp.MessagesActivity;
import com.pictureit.noambaroz.beautycianapp.R;

public class GcmIntentService extends IntentService {
	public static final int NOTIFICATION_ID = 1;

	private static final String NOTIFICATION_TYPE_MESSAGE = "message";

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
				try {
					setNotification(extras);
				} catch (JSONException e) {
					e.printStackTrace();
				}

			} else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
			} else if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
			}
		}
		// Release the wake lock provided by the WakefulBroadcastReceiver.
		GcmBroadcastReceiver.completeWakefulIntent(intent);
	}

	// Put the message into a notification and post it.
	// This is just one simple example of what you might choose to do with
	// a GCM message.
	private void sendNotification(Bundle data, String message, String title) {
		mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

		Intent notificationIntent = new Intent(this, MessagesActivity.class);
		// notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
		// Intent.FLAG_ACTIVITY_SINGLE_TOP);
		notificationIntent.putExtras(data);

		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
		// Notification.FLAG_ONGOING_EVENT | Notification.FLAG_NO_CLEAR);

		Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
				.setSmallIcon(R.drawable.ic_notification).setContentTitle(title)
				.setStyle(new NotificationCompat.BigTextStyle().bigText(message)).setContentText(message)
				.setAutoCancel(true).setSound(uri);

		mBuilder.setContentIntent(contentIntent);
		mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
	}

	private void onMessageNotification(JSONObject json) {
		String notificationId = null;
		try {
			notificationId = json.getString(ORDER_ID);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		if (notificationId != null) {
			// TODO
		}
	}

	private void setNotification(Bundle extras) throws JSONException {
		if (!extras.containsKey("data"))
			return;

		String dataString = (String) extras.get("data");
		JSONObject data = null;

		data = new JSONObject(dataString);

		String notificationType = data.getString("type");
		if (notificationType.equalsIgnoreCase(NOTIFICATION_TYPE_MESSAGE)) {
			onMessageNotification(data);
			String from = data.getString(FROM);
			// String title =
			// getString(R.string.push_notification_messages_message_from)
			// + (from != null ? from : getString(R.string.beautician));
			// String message =
			// getString(R.string.push_notification_messages_message);
			String title = "push_notification_messages_message_from";
			String message = "push_notification_messages_message";
			sendNotification(extras, message, title);
		}
	}
}
