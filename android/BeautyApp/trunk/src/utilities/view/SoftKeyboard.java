package utilities.view;

import java.util.Locale;

import android.app.Activity;
import android.view.inputmethod.InputMethodManager;

public class SoftKeyboard {

	public static void hide(Activity activity) {
		InputMethodManager inputMethodManager = (InputMethodManager) activity
				.getSystemService(Activity.INPUT_METHOD_SERVICE);
		if (inputMethodManager != null && activity.getCurrentFocus() != null)
			inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
	}

	public static boolean isRTL(CharSequence charSequence) {
		return isRTLFromString(charSequence);
	}

	private static boolean isRTLFromString(CharSequence charSequence) {
		if (charSequence.length() < 1)
			return false;
		final int directionality = Character.getDirectionality(charSequence.charAt(0));
		return directionality == Character.DIRECTIONALITY_RIGHT_TO_LEFT
				|| directionality == Character.DIRECTIONALITY_RIGHT_TO_LEFT_ARABIC;

	}

	public static boolean isRTL() {
		return isRTL(Locale.getDefault());
	}

	private static boolean isRTL(Locale locale) {
		final int directionality = Character.getDirectionality(locale.getDisplayName().charAt(0));
		return directionality == Character.DIRECTIONALITY_RIGHT_TO_LEFT
				|| directionality == Character.DIRECTIONALITY_RIGHT_TO_LEFT_ARABIC;
	}

}
