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
	public static final String URL_REQUEST_GET_UPCOMING_TREATMENT_BY_ID = URL_BASE + "getupcomingtreatment";

	// Cancel treatment
	public static final String URL_REQUEST_CANCEL_UPCOMING_TREATMENT = URL_BASE + "cancelupcomingtreatment";
	public static final String UPCOMING_TREATMENT_ID = "upcomingtreatmentid";
	public static final String REASON = "reason";

	// GCM Integration
	public static final String URL_REQUEST_SEND_GCM_REG_ID = URL_BASE + "updatepushregid";
	public static final String GCM_REGISTRATION_ID = "registration_id";
	public static final String OS = "os";

	// Messages
	public static final String URL_REQUEST_GET_MESSAGES = URL_BASE + "checkmessages";
	public static final String URL_REQUEST_GET_MESSAGE_BY_ID = URL_BASE + "orderdetails";
	public static final String URL_REQUEST_REMOVE_MESSAGE_FROM_LIST = URL_BASE + "removemessage";
	public static final String ORDER_ID = "order_id";
	public static final String MESSAGE_ID = "message_id";

	// Verify address
	public static final String URL_REQUEST_VERIFY_ADDRESS = "http://pictureit.co.il/beautyapp/BeautyUpService.asmx/getvalidaddress";

	// Beautician response
	public static final String URL_REQUEST_BEAUTICIAN_RESPONSE = URL_BASE + "beauticianresponse";
	public static final String DATE = "date";
	public static final String LOCATION = "location";
	public static final String PRICE = "price";
	public static final String REMARKS = "comments";
	public static final String TREATMENTS = "treatments";

	// Cancel treatment request
	public static final String URL_REQUEST_CANCEL_CUSTOMER_REQUEST = URL_BASE + "canceltreatmentrequest";
	public static final String URL_REQUEST_REMOVE_CANCELED_TREATMENT_FROM_LIST = URL_BASE + "removeupcomingtreatment";

	// Update treatment status
	public static final String URL_REQUEST_UPDATE_TREATMENT_STATUS = URL_BASE + "statustreatment";
	public static final String IS_ACCEPTED = "isaccepted";

	// History
	public static final String URL_REQUEST_GET_HISTORY = URL_BASE + "history";

	// Profile
	public static final String URL_REQUEST_GET_BEAUTICIAN_DETAILS = URL_BASE + "beauticiandetails";
	public static final String URL_REQUEST_UPDATE_MY_DETAILS = URL_BASE + "updatepersonaldetails";
	public static final String URL_REQUEST_UPDATE_CONTACT_DETAILS = URL_BASE + "updatecontactdetails";
	public static final String URL_REQUEST_UPDATE_TREATMENTS = URL_BASE + "updatebeauticiantreatments";
	public static final String URL_REQUEST_UPDATE_DIPLOMAS = URL_BASE + "updatebeauticiandegrees";
	public static final String URL_REQUEST_UPDATE_PERSONAL_DETAILS = URL_BASE + "updateprivatedetails";
	public static final String NAME = "name";
	public static final String BUSINESS_NAME = "business_name";
	public static final String CLASSIFICATION = "classification";
	public static final String EMAIL = "email";
	public static final String BUSINESS_ADDRESS = "business_address";
	public static final String BILLING_ADDRESS = "billing_address";
	public static final String AREA = "area";
	public static final String IS_ARRIVED_HOME = "isarrivedhome";
	public static final String DEGREES = "degrees";
	public static final String ABOUT = "about";
	public static final String EXPERIENCE = "experience";
	public static final String PAYMENT = "payment";

	// Statistics
	public static final String URL_REQUEST_GET_PRICE_STATISTICS = URL_BASE + "pricestatistics";
	public static final String URL_REQUEST_GET_TREATMENTS_STATISTICS = URL_BASE + "treatmentstatistics";
}
