package com.pictureit.noambaroz.beauticianapp;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

public class Application extends android.app.Application {

	@Override
	public void onCreate() {
		super.onCreate();
		MyPreference.initPreference(getApplicationContext());
		initImageLoader();
	}

	private void initImageLoader() {
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())

		.tasksProcessingOrder(QueueProcessingType.LIFO)
		// default
				.denyCacheImageMultipleSizesInMemory().discCacheFileCount(100).build();

		ImageLoader.getInstance().init(config);
	}
}
