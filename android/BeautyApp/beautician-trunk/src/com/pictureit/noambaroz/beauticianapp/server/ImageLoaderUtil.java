package com.pictureit.noambaroz.beauticianapp.server;

import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.pictureit.noambaroz.beauticianapp.R;

public class ImageLoaderUtil {

	private static DisplayImageOptions getDisplayImageOptions(int onFailImageResId) {
		int avatarResId = onFailImageResId == -1 ? R.drawable.profile_avatar : onFailImageResId;
		return new DisplayImageOptions.Builder().resetViewBeforeLoading(false).cacheInMemory(true).cacheOnDisc(true)
				.considerExifParams(false).displayer(new RoundedBitmapDisplayer(0)).showImageForEmptyUri(avatarResId)
				.showImageOnFail(avatarResId).showImageOnLoading(avatarResId).imageScaleType(ImageScaleType.EXACTLY)
				.build();
	}

	public static void display(String uri, ImageView imageView) {
		display(uri, imageView, -1);
	}

	public static void display(String uri, ImageView imageView, int onFailImageResId) {
		com.nostra13.universalimageloader.core.ImageLoader imageLoader = com.nostra13.universalimageloader.core.ImageLoader
				.getInstance();
		imageLoader.displayImage(uri, imageView, getDisplayImageOptions(onFailImageResId));
	}
}
