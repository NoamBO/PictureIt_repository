package com.pictureit.noambaroz.beauticianapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "beauticianapp.db";
	private static final int DATABASE_VERSION = 1;

	public DbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table " + DataProvider.TABLE_ORDERS_AROUND_ME + " (" + DataProvider.COL_ID
				+ " integer primary key autoincrement, " + DataProvider.COL_FIRST_NAME + " text, "
				+ DataProvider.COL_LAST_NAME + " text, " + DataProvider.COL_LATITUDE + " text, "
				+ DataProvider.COL_LONGITUDE + " text, " + DataProvider.COL_IS_DIRECTED_TO_ME + " text, "
				+ DataProvider.COL_ORDER_ID + " text);");

		db.execSQL("create table " + DataProvider.TABLE_ALARMS + " (" + DataProvider.COL_ID
				+ " integer primary key autoincrement, " + DataProvider.COL_CUSTOMER_NAME + " text, "
				+ DataProvider.COL_TREATMENT + " text, " + DataProvider.COL_ADDRESS + " text, "
				+ DataProvider.COL_IMAGE_URL + " text, " + DataProvider.COL_ORDER_ID + " integer, "
				+ DataProvider.COL_NEED_TO_SHOW_DIALOG + " integer, " + DataProvider.COL_TREATMENT_TIME + " long, "
				+ DataProvider.COL_IS_PLAYED + " integer);");

		db.execSQL("create table " + DataProvider.TABLE_CONFIRMED_TREATMENTS + " (" + DataProvider.COL_ID
				+ " integer primary key autoincrement, " + DataProvider.COL_CUSTOMER_TELEPHONE + " text, "
				+ DataProvider.COL_ORDER_ID + " text);");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

}
