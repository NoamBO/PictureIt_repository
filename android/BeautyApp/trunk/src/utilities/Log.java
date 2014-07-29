package utilities;

import com.pictureit.noambaroz.beautyapp.Settings;

public class Log {

	private final static String TAG = "my_app";

	public static void i(String message) {
		i(null, message);
	}

	public static void i(String tag, String message) {
		if (!Settings.DEBUG)
			return;

		if (tag != null)
			tag = TAG + " " + tag;
		else
			tag = TAG;

		android.util.Log.i(tag, message);
	}

}
