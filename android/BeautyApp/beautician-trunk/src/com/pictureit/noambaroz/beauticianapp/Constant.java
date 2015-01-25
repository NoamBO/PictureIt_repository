package com.pictureit.noambaroz.beauticianapp;

import com.pictureit.noambaroz.beauticianapp.data.Message;

public class Constant {

	public static final String APP_PREFS_NAME = "com.pictureit.beautyapp.prefs";

	public static final String PREFS_KEY_UID = "uid";

	public static final String PREFS_KEY_IS_AVAILABLE = "is_beautician_available";

	public static final String EXTRA_ORDER_ID = "order_id";

	public static final String EXTRA_UPCOMING_TREATMENT_ID = "upcomingtreatment_id";

	public static final String EXTRA_MESSAGE_OBJECT = Message.class.getName();

	public static final int REQUEST_CODE_SINGLE_MESSAGE = 20;

	public static final String EXTRA_KEY_HAS_NOTIFICATION = "get_boolean_value";

	public static final String EXTRA_KEY_CLASS_TYPE = "class_to_launch";

	public static final int EXTRA_CLASS_TYPE_NOTIFICATION = 2;

	public static final int EXTRA_CLASS_TYPE_MESSAGES = 1;

	public static final int EXTRA_CLASS_TYPE_TREATMENTS = 3;

	public static final String INTENT_FILTER_MESSAGE_DELETED = "message.deleted.broadcast.receiver.intent.filter";

	public static final String INTENT_FILTER_UPCOMING_TREATMENT_STATUS_CHANGED = "upcoming.treatment.status.broadcast.receiver.intent.filter";

}
