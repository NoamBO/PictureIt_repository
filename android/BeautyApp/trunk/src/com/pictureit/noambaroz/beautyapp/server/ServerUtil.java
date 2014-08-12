package com.pictureit.noambaroz.beautyapp.server;

public class ServerUtil {

	private static final String URL_BASE = "http://192.168.1.50/BeautyUpService.asmx/";

	public static final String URL_REQUEST_SEARCH_BEAUTICIAN = URL_BASE + "";
	public static final String URL_REQUEST_GET_BEAUTICIAN_BY_ID = URL_BASE + "";
	public static final String URL_REQUEST_GET_BEAUTICIAN_ARRAY_BY_IDs = URL_BASE + "";
	public static final String URL_REQUEST_GET_MARKERS = URL_BASE + "PostLatitudeLongitude";

	public static final String UID = "uid";
	public static final String SERVER_RESPONSE_STATUS = "status";
	public static final String SERVER_RESPONSE_STATUS_SUCCESS = "success";
	public static final String LATITUDE = "latitude";
	public static final String LONGITUDE = "longitude";

	public static final String LOCATION = "location";

	// POST Order Treatment
	public static final String TREATMENTS = "treatments";
	public static final String COMMENTS = "comments";
	public static final String FOR = "for";
	public static final String DATE = "date";
	public static final String ID = "id";
	public static final String AMOUNT = "amount";
	public static final String ORDER_ID = "order_id";

	// POST Update Email
	public static final String E_MAIL = "email";

	// POST Search Beautician
	public static final String NAME = "name";
	public static final String TYPE = "type";
	public static final String TREATMENT = "treatment";

	// POST Get Beautician Array By IDs
	public static final String IDs = "ids";

}
