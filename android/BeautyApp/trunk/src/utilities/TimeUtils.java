package utilities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import android.text.format.DateFormat;

public class TimeUtils {

	/**
	 * 
	 * @param date
	 *            in format of "dd/MM/yyyy HH:mm"
	 * @return timestamp (millisecond)
	 */
	public static long dateToTimestamp(String date) {
		Calendar calendar = Calendar.getInstance();

		SimpleDateFormat parser = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
		Date yourDate = null;
		try {
			yourDate = parser.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		calendar.setTime(yourDate);
		long timeInMilis = calendar.getTimeInMillis();
		return timeInMilis;
	}

	/**
	 * 
	 * @param timestamp
	 *            (millisecond)
	 * @return date in format of "dd/MM/yyyy HH:mm"
	 * 
	 */
	public static String timestampToDate(String timestamp) {
		Calendar cal = Calendar.getInstance(Locale.getDefault());
		cal.setTimeInMillis(Long.parseLong(timestamp) * 1000);
		cal.setTimeZone(TimeZone.getTimeZone("UTC"));
		String date = DateFormat.format("dd/MM/yyyy HH:mm", cal).toString();
		return date;
	}
}
