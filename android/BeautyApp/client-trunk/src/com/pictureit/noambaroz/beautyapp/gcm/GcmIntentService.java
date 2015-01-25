package com.pictureit.noambaroz.beautyapp.gcm;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import utilities.Log;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.pictureit.noambaroz.beautyapp.ActivityMessages;
import com.pictureit.noambaroz.beautyapp.R;
import com.pictureit.noambaroz.beautyapp.data.DataUtil;

public class GcmIntentService extends IntentService {
	public static final int NOTIFICATION_ID = 1;

	private static final String KEY_NOTIFICATION_TYPE = "type";
	private static final String KEY_NOTIFICATION_DATA = "data";

	private static final String NOTIFICATION_TYPE_MESSAGE = "message";

	private static final String FROM = "from";
	private static final String ORDER_ID = "response_id";

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
				if (extras.containsKey(KEY_NOTIFICATION_DATA)) {
					JSONObject jo = null;
					try {
						jo = new JSONObject(extras.getString(KEY_NOTIFICATION_DATA));
						notificationType = jo.getString(KEY_NOTIFICATION_TYPE);
					} catch (JSONException e) {
						e.printStackTrace();
					}

					if (!TextUtils.isEmpty(notificationType)) {
						if (notificationType.equalsIgnoreCase(NOTIFICATION_TYPE_MESSAGE))
							onMessageArrived(jo);
					}
				}
			} else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
			} else if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
			}
		}
		// Release the wake lock provided by the WakefulBroadcastReceiver.
		GcmBroadcastReceiver.completeWakefulIntent(intent);
	}

	private void onMessageArrived(JSONObject data) {
		if (data != null) {
			onMessageNotification(data);
			if (!isAppRunningInForeground()) {
				String from = "";
				if (data.has(FROM))
					try {
						from = data.getString(FROM);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				String title = getString(R.string.push_notification_messages_message_from)
						+ (from != null ? from : getString(R.string.beautician));
				String message = getString(R.string.push_notification_messages_message);
				sendNotification(null, message, title);
			} else {
				startActivity(ActivityMessages.class);
			}
		}
	}

	// Put the message into a notification and post it.
	// This is just one simple example of what you might choose to do with
	// a GCM message.
	private void sendNotification(Bundle data, String message, String title) {
		mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

		Intent notificationIntent = new Intent(this, ActivityMessages.class);
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

	private void onMessageNotification(JSONObject data) {
		String notificationId = null;
		try {
			notificationId = data.getString(ORDER_ID);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (notificationId != null)
			DataUtil.pushOrderNotificationIdToTable(getApplicationContext(), notificationId);
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

	private void startActivity(Class<?> class1) {
		getApplication().startActivity(
				new Intent(getBaseContext(), class1).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
						| Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
		Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		Ringtone r = RingtoneManager.getRingtone(this, notification);
		r.play();
	}
}
