package com.pictureit.noambaroz.beautyapp.server;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.process.BitmapProcessor;
import com.pictureit.noambaroz.beautyapp.R;

public class ImageLoaderUtil {

	private static DisplayImageOptions.Builder getDisplayImageOptions(int onFailImageResId) {
		int avatarResId = onFailImageResId == -1 ? R.drawable.profile_avatar : onFailImageResId;
		return new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisc(true)
				.bitmapConfig(Bitmap.Config.RGB_565).displayer(new RoundedBitmapDisplayer(4))
				.showImageForEmptyUri(avatarResId).showImageOnFail(avatarResId).showImageOnLoading(avatarResId);
	}

	public static void display(String uri, ImageView imageView, Context ctx) {
		display(uri, imageView, -1, ctx);
	}

	public static void display(String uri, ImageView imageView, int onFailImageResId, final Context context) {
		com.nostra13.universalimageloader.core.ImageLoader imageLoader = com.nostra13.universalimageloader.core.ImageLoader
				.getInstance();
		imageLoader.displayImage(uri, imageView,
				getDisplayImageOptions(onFailImageResId).postProcessor(new BitmapProcessor() {

					@Override
					public Bitmap process(Bitmap bitmap) {

						int w = (int) context.getResources().getDimension(R.dimen.beautician_picture_width);
						int h = (int) context.getResources().getDimension(R.dimen.beautician_picture_height);

						if (w > 0 && h > 0)
							return Bitmap.createScaledBitmap(bitmap, w, h, false);
						else
							return bitmap;
					}
				}).build());
	}
}