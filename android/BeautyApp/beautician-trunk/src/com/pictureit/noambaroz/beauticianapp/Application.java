package com.pictureit.noambaroz.beauticianapp;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.parse.Parse;
import com.parse.ParseCrashReporting;
import com.pictureit.noambaroz.beauticianapp.alarm.AlarmManager;

public class Application extends android.app.Application {

	@Override
	public void onCreate() {
		super.onCreate();
		ParseCrashReporting.enable(this);
		Parse.initialize(this, "d73qebORJ68gLOW9k5qjW9FPUKe8OET55qAlNSkG", "jnzjs6zo0aYyy1xOie0l9J4qNL41dK7KN6fN7Ag3");

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
