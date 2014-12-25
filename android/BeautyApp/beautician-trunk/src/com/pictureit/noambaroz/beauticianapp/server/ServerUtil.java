package com.pictureit.noambaroz.beauticianapp.server;

public class ServerUtil {

	private static final String URL_BASE = "http://pictureit.co.il/beautyappbeautician/BeautyAppWebService.asmx/";

	public static final String UID = "uid";
	public static final String SERVER_RESPONSE_STATUS = "status";
	public static final String SERVER_RESPONSE_STATUS_SUCCESS = "success";

	// public static final String LOCATION = "location";

	// First time registration
	public static final String URL_REQUEST_REGISTER = URL_BASE + "register";
	public static final String IMAGE = "image";
	public static final String CODE = "code";

	// Availability updating
	public static final String URL_REQUEST_UPDATE_AVAILABILITY = URL_BASE + "availabilityupdating";
	public static final String IS_AVAILABLE = "isavailable";

	// Update location
	public static final String URL_REQUEST_UPDATE_LOCATION = URL_BASE + "beauticianlocation";
	public static final String LATITUDE = "latitude";
	public static final String LONGITUDE = "longitude";

	// Get Upcoming Treatments
	public static final String URL_REQUEST_GET_UPCOMING_TREATMENTS = URL_BASE + "upcomingtreatments";

	// GCM Integration
	public static final String URL_REQUEST_SEND_GCM_REG_ID = URL_BASE + "updatepushregid";
	public static final String GCM_REGISTRATION_ID = "registration_id";
	public static final String OS = "os";

}
