package com.pictureit.noambaroz.beautyapp.server;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.pictureit.noambaroz.beautyapp.R;

public class ImageLoaderUtil {

	public static DisplayImageOptions getDisplayImageOptions() {
		return new DisplayImageOptions.Builder().resetViewBeforeLoading(false).cacheInMemory(true).cacheOnDisc(true)
				.considerExifParams(false).displayer(new RoundedBitmapDisplayer(30))
				.showImageForEmptyUri(R.drawable.avatar).showImageOnFail(R.drawable.avatar)
				.showImageOnLoading(R.drawable.avatar).build();
	}
}
