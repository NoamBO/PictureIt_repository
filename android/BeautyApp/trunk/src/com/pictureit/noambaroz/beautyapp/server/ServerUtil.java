package com.pictureit.noambaroz.beautyapp.server;

public class ServerUtil {

	private static final String URL_BASE = "http://pictureit.co.il/beautyapp/BeautyUpService.asmx/";

	public static final String URL_REQUEST_SEARCH_BEAUTICIAN = URL_BASE + "postsearchbeautician";
	public static final String URL_REQUEST_GET_BEAUTICIAN_BY_ID = URL_BASE + "getbeauticianbyid";
	public static final String URL_REQUEST_GET_BEAUTICIAN_ARRAY_BY_IDs = URL_BASE + "postarraybeauticianbyid";
	public static final String URL_REQUEST_GET_MARKERS = URL_BASE + "PostLatitudeLongitude";
	public static final String URL_REQUEST_ORDER_TREATMENT = URL_BASE + "postordertreatment";
	public static final String URL_REQUEST_ORDER_TREATMENT_BY_LOCATION = URL_BASE + "postordertreatmentbylocation";
	public static final String URL_REQUEST_GET_ORDER_MESSAGE_NOTIFICATION = URL_BASE + "getordernotification";
	public static final String URL_REQUEST_VERIFY_USER = URL_BASE + "verifyuser";
	public static final String URL_REQUEST_VERIFY_ADDRESS = URL_BASE + "getvalidaddress";
	public static final String URL_REQUEST_SEND_GCM_REG_ID = URL_BASE + "updatepushregid";
	public static final String URL_REQUEST_REGISTER = URL_BASE + "register";
	public static final String URL_REQUEST_VERIFICATION_REGISTER_CODE = URL_BASE + "verificationRegisterCode";
	public static final String URL_REQUEST_RE_SEND_REGISTER_CODE = URL_BASE + "reSendCode";
	public static final String URL_REQUEST_UPDATE_USER_PROFILE_DATA = URL_BASE + "updateuserdetails";
	public static final String URL_REQUEST_UPDATE_USER_PROFILE_PICTURE = URL_BASE + "uploadimage";
	public static final String URL_REQUEST_GET_ALL_UPCOMING_TREATMENTS = URL_BASE + "upcomingtreatments";

	public static final String UID = "uid";
	public static final String SERVER_RESPONSE_STATUS = "status";
	public static final String SERVER_RESPONSE_STATUS_SUCCESS = "success";
	public static final String LATITUDE = "latitude";
	public static final String LONGITUDE = "longitude";
	public static final String GCM_REGISTRATION_ID = "registration_id";
	public static final String OS = "os";

	public static final String LOCATION = "location";

	// POST Order Treatment
	public static final String CUSTOMER_UID = "customeruid";
	public static final String BEAUTICIAN_UID = "beauticianuid";
	public static final String TREATMENTS = "treatments";
	public static final String COMMENTS = "comments";
	public static final String FOR = "forwho";
	public static final String DATE = "date";
	public static final String TREATMENT_ID = "treatments_id";
	public static final String AMOUNT = "amount";
	public static final String ORDER_ID = "order_id";

	// POST Update Email
	public static final String E_MAIL = "email";

	// POST Search Beautician
	public static final String NAME = "name";
	public static final String TYPE = "classification";
	public static final String TREATMENT = "treatment";

	// POST Get Beautician Array By IDs
	public static final String IDs = "ids";

	// POST Verify User
	public static final String EXIST = "exist";
	public static final String ACTIVE = "active";

	// POST Register
	public static final String FIRST_NANE = "first_name";
	public static final String LAST_NAME = "last_name";
	public static final String ADDRESS = "address";
	public static final String PHONE_NUMBER = "phone_number";
	public static final String CODE = "code";
	public static final String PROFILE_IMAGE = "image";
	public static final String SENDER = "sender";
	public static final String SENDER_TYPE_CUSTOMER = "customer";

}
