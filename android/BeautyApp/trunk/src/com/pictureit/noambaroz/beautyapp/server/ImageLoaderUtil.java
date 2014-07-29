package com.pictureit.noambaroz.beautyapp.server;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class ImageLoaderUtil {

	public static DisplayImageOptions getDisplayImageOptions() {
		return new DisplayImageOptions.Builder().resetViewBeforeLoading(false).cacheInMemory(true).cacheOnDisc(true)
				.considerExifParams(false).displayer(new RoundedBitmapDisplayer(30)).build();
	}
}
