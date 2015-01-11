package com.pictureit.noambaroz.beauticianapp.gcm;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.IntentService;
import android.app.Notification;
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
import com.pictureit.noambaroz.beauticianapp.ActivityNotificationsDialog;
import com.pictureit.noambaroz.beauticianapp.Constant;
import com.pictureit.noambaroz.beauticianapp.Log;
import com.pictureit.noambaroz.beauticianapp.MainActivity;
import com.pictureit.noambaroz.beauticianapp.MyPreference;
import com.pictureit.noambaroz.beauticianapp.R;
import com.pictureit.noambaroz.beauticianapp.alarm.AlarmManager;
import com.pictureit.noambaroz.beauticianapp.data.BeauticianOfferResponse;
import com.pictureit.noambaroz.beauticianapp.data.DataUtils;
import com.pictureit.noambaroz.beauticianapp.data.OrderAroundMe;
import com.pictureit.noambaroz.beauticianapp.data.Formatter;

public class GcmIntentService extends IntentService {
	public static final int NOTIFICATION_ID = 1;

	private static final String NOTIFICATION_TYPE_MESSAGE = "type_message";

	// when customer reply to beautician offer
	private static final String NOTIFICATION_TYPE_OFFER_RESPONSE = "type_message_response";

	// if customer canceled his treatment request or if he accepted other
	// beautician offer
	private static final String NOTIFICATION_TYPE_OFFER_DECLINED = "type_message_canceled";
	private static final String KEY_NOTIFICATION_TYPE = "type";
	private static final String KEY_NOTIFICATION_DATA = "data";

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
				if (extras.containsKey(KEY_NOTIFICATION_TYPE) && extras.containsKey(KEY_NOTIFICATION_DATA)) {
					notificationType = extras.getString(KEY_NOTIFICATION_TYPE);
					if (!TextUtils.isEmpty(notificationType)) {
						if (notificationType.equalsIgnoreCase(NOTIFICATION_TYPE_MESSAGE))
							onMessageArrived(extras.get(KEY_NOTIFICATION_DATA).toString());
						else if (notificationType.equalsIgnoreCase(NOTIFICATION_TYPE_OFFER_RESPONSE))
							onCustomerResponse(extras.get(KEY_NOTIFICATION_DATA).toString());
						else if (notificationType.equalsIgnoreCase(NOTIFICATION_TYPE_OFFER_DECLINED))
							onOrderCanceled(extras.get(KEY_NOTIFICATION_DATA).toString());
					}
				}

			} else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
			} else if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
			}
		}
		// Release the wake lock provided by the WakefulBroadcastReceiver.
		GcmBroadcastReceiver.completeWakefulIntent(intent);
	}

	private void onOrderCanceled(String data) {
		JSONObject j;
		try {
			j = new JSONObject(data);
			if (j.has("orderid")) {
				String orderid = j.getString("orderid");
				DataUtils.get(getApplicationContext()).deleteOrderAroundMe(orderid);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void onCustomerResponse(String beauticianOfferResponse) {
		BeauticianOfferResponse bor = new Gson().fromJson(beauticianOfferResponse, BeauticianOfferResponse.class);
		if (bor == null || bor.orderid == null)
			return;

		if (bor.status.equalsIgnoreCase(BeauticianOfferResponse.RESPONSE_STATUS_CONFIRMED))
			onCustomerConfimedTheOffer(bor);
		else if (bor.status.equalsIgnoreCase(BeauticianOfferResponse.RESPONSE_STATUS_DECLINED))
			onCustomerDeclinedTheOffer(bor);

	}

	private void onCustomerDeclinedTheOffer(BeauticianOfferResponse bro) {
		if (!isAppRunningInForeground()) {
			String title = getString(R.string.response_declined);
			String message = getString(R.string.your_offer_didnt_fit_to) + " " + bro.full_name + " "
					+ getString(R.string.and_he_declined_your_offer);
			sendNotification(0, null, message, title);
		}
	}

	private void onCustomerConfimedTheOffer(BeauticianOfferResponse offerResponse) {
		offerResponse.treatment = Formatter.getSelf(getApplicationContext()).getTreatmentName(
				offerResponse.treatments);
		offerResponse.treatment_time = offerResponse.treatment_time * 1000;
		AlarmManager.getInstance().setAlarm(offerResponse);

		DataUtils.get(getApplicationContext()).addTreatmentConfirmedRow(offerResponse);
		if (!isAppRunningInForeground()) {
			String title = getString(R.string.response_confirmed);
			String message = offerResponse.full_name + " " + getString(R.string.response_confirmed_message);
			sendNotification(Constant.EXTRA_CLASS_TYPE_NOTIFICATION, null, message, title);
		} else {
			getApplication().startActivity(
					new Intent(getBaseContext(), ActivityNotificationsDialog.class)
							.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP
									| Intent.FLAG_ACTIVITY_NEW_TASK));
		}
		MyPreference.setHasAlarmsDialogsToShow(true);
	}

	private void onMessageArrived(String orderAroundMe) {
		OrderAroundMe oam = new Gson().fromJson(orderAroundMe, OrderAroundMe.class);
		if (oam == null || oam.getOrderid() == null)
			return;

		DataUtils.get(getApplicationContext()).addOrderAroundMe(oam, true);
		// if (!isAppRunningInForeground()) {
		String title = getString(R.string.request_received);
		String message = getString(R.string.new_request_is_waiting_for_you_inside_the_app);
		sendNotification(Constant.EXTRA_CLASS_TYPE_MESSAGES, null, message, title);
		// } else {
		// TODO
		// }
	}

	// Put the message into a notification and post it.
	// This is just one simple example of what you might choose to do with
	// a GCM message.
	private void sendNotification(int classTypeToLoad, Bundle data, String message, String title) {
		mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

		Intent notificationIntent;
		if (classTypeToLoad != -1) {
			notificationIntent = new Intent(this, MainActivity.class);
			notificationIntent.putExtra(Constant.EXTRA_KEY_CLASS_TYPE, classTypeToLoad);
		} else {
			notificationIntent = new Intent();
		}
		if (data != null)
			notificationIntent.putExtras(data);
		notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
				PendingIntent.FLAG_ONE_SHOT);

		Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
				.setSmallIcon(R.drawable.ic_notification).setContentTitle(title)
				.setStyle(new NotificationCompat.BigTextStyle().bigText(message)).setContentText(message)
				.setAutoCancel(true).setSound(uri);

		builder.setContentIntent(contentIntent);
		Notification notification = builder.build();
		// notification.flags = Notification.FLAG_AUTO_CANCEL;
		mNotificationManager.notify(NOTIFICATION_ID, notification);
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