package com.pictureit.noambaroz.beauticianapp.data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import android.text.TextUtils;
import android.text.format.DateFormat;

public class TimeUtils {

	/**
	 * 
	 * @param date
	 *            in format of "dd/MM/yyyy"
	 * @return timestamp (seconds)
	 */
	public static long dateToGMTTimestamp(String date) {
		return dateToTimestamp(date, true);
	}

	/**
	 * 
	 * @param date
	 * @param isGMT
	 *            to return gmt time stamp( +0 ) in format of "dd/MM/yyyy"
	 * @return timestamp (seconds)
	 */
	public static long dateToLocalTimestamp(String date) {
		return dateToTimestamp(date, false);
	}

	private static long dateToTimestamp(String date, boolean isGMT) {
		Calendar calendar = Calendar.getInstance();

		SimpleDateFormat parser = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
		if (isGMT)
			parser.setTimeZone(TimeZone.getTimeZone("GMT"));
		Date yourDate = null;
		try {
			yourDate = parser.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		calendar.setTime(yourDate);
		long timeInMilis = calendar.getTimeInMillis();
		return timeInMilis / 1000;
	}

	/**
	 * 
	 * @param date
	 *            in format of "dd/MM/yyyy HH:mm"
	 * @return timestamp (second)
	 */
	public static long dateAndTimeToGMTTimestamp(String date) {
		return dateAndTimeToTimestamp(date, true);
	}

	/**
	 * 
	 * @param date
	 *            in format of "dd/MM/yyyy HH:mm"
	 * @return timestamp (second)
	 */
	public static long dateAndTimeToLocalTimestamp(String date) {
		return dateAndTimeToTimestamp(date, false);
	}

	/**
	 * 
	 * @param date
	 * @param isGMT
	 *            to return gmt time stamp( +0 ) in format of "dd/MM/yyyy HH:mm"
	 * @return timestamp (second)
	 */
	private static long dateAndTimeToTimestamp(String date, boolean isGMT) {
		Calendar calendar = Calendar.getInstance();

		SimpleDateFormat parser = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
		if (isGMT)
			parser.setTimeZone(TimeZone.getTimeZone("GMT"));
		Date yourDate = null;
		try {
			yourDate = parser.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		calendar.setTime(yourDate);
		long timeInMilis = calendar.getTimeInMillis();
		return timeInMilis / 1000;
	}

	/**
	 * 
	 * @param timestamp
	 *            (seconds)
	 * @return date in format of "dd/MM/yyyy HH:mm"
	 * 
	 */
	public static String timestampToDateWithHour(String timestamp) {
		if (TextUtils.isEmpty(timestamp))
			return "";
		// Calendar cal = Calendar.getInstance(Locale.getDefault());
		// cal.setTimeInMillis(Long.parseLong(timestamp) * 1000);
		// cal.setTimeZone(TimeZone.getTimeZone("UTC"));
		// String date = DateFormat.format("dd/MM/yyyy HH:mm", cal).toString();
		String date = DateFormat.format("dd/MM/yyyy HH:mm", Long.parseLong(timestamp) * 1000).toString();
		return date;
	}

	/**
	 * 
	 * @param timestamp
	 *            (seconds)
	 * @return date in format of "dd/MM/yyyy"
	 * 
	 */
	public static String timestampToDate(String timestamp) {
		if (TextUtils.isEmpty(timestamp))
			return "";

		String date = DateFormat.format("dd/MM/yyyy", Long.parseLong(timestamp) * 1000).toString();
		return date;
	}
}
