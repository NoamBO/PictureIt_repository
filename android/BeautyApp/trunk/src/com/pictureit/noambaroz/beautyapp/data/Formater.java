package com.pictureit.noambaroz.beautyapp.data;

import java.text.DateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Formater {

	public String getDate(String text) {
		DateFormat df = DateFormat.getDateTimeInstance();
		df.setTimeZone(TimeZone.getTimeZone("UTC"));
		String date = df.format(new Date(Long.valueOf(text).longValue())).toString();
		return date;
	}
}
