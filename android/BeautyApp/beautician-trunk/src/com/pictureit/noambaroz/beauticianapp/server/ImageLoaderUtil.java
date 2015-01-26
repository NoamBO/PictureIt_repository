package com.pictureit.noambaroz.beauticianapp.server;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.process.BitmapProcessor;
import com.pictureit.noambaroz.beauticianapp.R;

public class ImageLoaderUtil {

	private static DisplayImageOptions.Builder getDisplayImageOptions(int onFailImageResId) {
		int avatarResId = onFailImageResId == -1 ? R.drawable.profile_avatar : onFailImageResId;
		return new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisc(true)
				.bitmapConfig(Bitmap.Config.RGB_565).displayer(new RoundedBitmapDisplayer(4))
				.showImageForEmptyUri(avatarResId).showImageOnFail(avatarResId).showImageOnLoading(avatarResId);
	}

	// public static void display(String uri, ImageView imageView) {
	// display(uri, imageView, -1, 0, 0);
	// }

	public static void display(String uri, ImageView imageView, int onFailImageResId, final int width, final int height) {
		com.nostra13.universalimageloader.core.ImageLoader imageLoader = com.nostra13.universalimageloader.core.ImageLoader
				.getInstance();
		imageLoader.displayImage(uri, imageView,
				getDisplayImageOptions(onFailImageResId).postProcessor(new BitmapProcessor() {

					@Override
					public Bitmap process(Bitmap bitmap) {
						if (width > 0 && height > 0)
							return Bitmap.createScaledBitmap(bitmap, width, height, false);
						else
							return bitmap;
					}
				}).build());
	}
}
