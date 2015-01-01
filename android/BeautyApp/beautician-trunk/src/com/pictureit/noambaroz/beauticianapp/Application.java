package com.pictureit.noambaroz.beauticianapp;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.pictureit.noambaroz.beauticianapp.alarm.AlarmManager;

public class Application extends android.app.Application {

	@Override
	public void onCreate() {
		super.onCreate();
		AlarmManager.initAlarmManager(Application.this);
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
