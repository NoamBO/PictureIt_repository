package utilities;

public class Log {

	public static final boolean DEBUG = true;

	private final static String TAG = "my_app";

	public static void i(String message) {
		i(null, message);
	}

	public static void i(String tag, String message) {
		if (!DEBUG)
			return;

		if (tag != null)
			tag = TAG + " " + tag;
		else
			tag = TAG;

		StackTraceElement ste = getCourrentStackTrace();
		if (ste != null)
			message = getClassNameMethodNameAndLineNumber(ste) + message;
		android.util.Log.i(tag, message);
	}

	private static int getLineNumber(StackTraceElement ste) {
		return ste.getLineNumber();
	}

	private static String getClassName(StackTraceElement ste) {
		String fileName = ste.getFileName();
		return fileName.substring(0, fileName.length() - 5);
	}

	private static String getMethodName(StackTraceElement ste) {
		return ste.getMethodName();
	}

	private static String getClassNameMethodNameAndLineNumber(StackTraceElement ste) {
		return "[" + getClassName(ste) + "." + getMethodName(ste) + "()-" + getLineNumber(ste) + "]: ";
	}

	private static StackTraceElement getCourrentStackTrace() {
		boolean seenTHISclassName = false;
		StackTraceElement e[] = Thread.currentThread().getStackTrace();
		for (int i = 0; i < e.length; i++) {
			if (e[i].getClassName().equals("utilities.Log")) {
				seenTHISclassName = true;
				continue;
			}
			if (!e[i].getClassName().equals("utilities.Log") && seenTHISclassName) {
				return e[i];
			}
		}
		return null;
	}
}
