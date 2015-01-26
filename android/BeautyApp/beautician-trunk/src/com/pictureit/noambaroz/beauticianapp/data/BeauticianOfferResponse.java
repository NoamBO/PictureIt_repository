package com.pictureit.noambaroz.beauticianapp.data;

import java.util.ArrayList;

import com.pictureit.noambaroz.beauticianapp.alarm.Alarm;

public class BeauticianOfferResponse extends Alarm {

	public static final String RESPONSE_STATUS_CONFIRMED = "confirmed";
	public static final String RESPONSE_STATUS_DECLINED = "declined";

	public String phone_number;
	public String status;
	public ArrayList<TreatmentType> treatments;

}
