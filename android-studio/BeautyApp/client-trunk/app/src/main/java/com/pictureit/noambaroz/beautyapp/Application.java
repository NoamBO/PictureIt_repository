package com.pictureit.noambaroz.beautyapp;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.parse.Parse;
import com.parse.ParseCrashReporting;

public class Application extends android.app.Application {

	public static final String PENDING_TREATMENT_ID = "pending_treatment_id";

	@Override
	public void onCreate() {
		ParseCrashReporting.enable(this);
		Parse.initialize(this, "5mBoLBhfZitSwKM5F4CH09w2ivUsWNM4AFEsudsu", "9OpBDEPTOq5RSqbO3lSf9NCpaUQfzO3waVOQagh6");

		initImageLoader();
		super.onCreate();
	}

	private void initImageLoader() {
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())

		.threadPoolSize(10)
		// default
				.threadPriority(Thread.NORM_PRIORITY - 1)
				// default
				.tasksProcessingOrder(QueueProcessingType.FIFO)
				// default
				.denyCacheImageMultipleSizesInMemory().discCacheFileCount(100).build();

		ImageLoader.getInstance().init(config);
	}
}
