package com.pictureit.noambaroz.beauticianapp.utilities;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

public class PixelsConverter {
	/**
	 * This method converts dp unit to equivalent pixels, depending on device
	 * density.
	 * 
	 * @param dp
	 *            A value in dp (density independent pixels) unit. Which we need
	 *            to convert into pixels
	 * @param context
	 *            Context to get resources and device specific display metrics
	 * @return A float value to represent px equivalent to dp depending on
	 *         device density
	 */
	public static float convertDpToPixel(float dp, Context context) {
		return dp * getDensityConst(context);
	}

	/**
	 * This method converts device specific pixels to density independent
	 * pixels.
	 * 
	 * @param px
	 *            A value in px (pixels) unit. Which we need to convert into db
	 * @param context
	 *            Context to get resources and device specific display metrics
	 * @return A float value to represent dp equivalent to px value
	 */
	public static float convertPixelsToDp(float px, Context context) {
		return px / getDensityConst(context);
	}

	private static float getDensityConst(Context ctx) {
		Resources resources = ctx.getResources();
		DisplayMetrics metrics = resources.getDisplayMetrics();
		return (metrics.densityDpi / 160f);
	}
}
