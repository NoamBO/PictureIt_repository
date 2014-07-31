package com.pictureit.noambaroz.beautyapp;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

public class Application extends android.app.Application {

	public static final String PENDING_TREATMENT_ID = "pending_treatment_id";

	@Override
	public void onCreate() {
		super.onCreate();

		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())

		.threadPoolSize(3)
		// default
				.threadPriority(Thread.NORM_PRIORITY - 1)
				// default
				.tasksProcessingOrder(QueueProcessingType.FIFO)
				// default
				.denyCacheImageMultipleSizesInMemory().discCacheFileCount(100).build();

		ImageLoader.getInstance().init(config);
	}
}
