package com.pictureit.noambaroz.beautyapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "beautyapp.db";
	private static final int DATABASE_VERSION = 1;

	public DbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// db.execSQL("create table " + DataProvider.TABLE_MY_PROFILE +
		// " (_id integer primary key autoincrement, "
		// + DataProvider.COL_MY_NAME + " text" + DataProvider.COL_MY_LAST_NAME
		// + " text"
		// + DataProvider.COL_MY_EMAIL + " text" + DataProvider.COL_MY_ADDRESS +
		// " text);");
		db.execSQL("create table "
				+ DataProvider.TABLE_HISTORY
				+ " (_id integer primary key autoincrement, for text, date text, treatments text, whare text, remarks text);");
		db.execSQL("create table " + DataProvider.TABLE_MESSAGES
				+ " (_id integer primary key autoincrement, notificationid text, " + DataProvider.COL_BEAUTICIAN_ID
				+ " text, " + DataProvider.COL_NAME + " text, " + DataProvider.COL_ADDRESS + " text, "
				+ DataProvider.COL_RATERS + " integer, " + DataProvider.COL_RATE + " integer, " + DataProvider.COL_AT
				+ " text, " + DataProvider.COL_LOCATION + " text, " + DataProvider.COL_REMARKS + " text, "
				+ DataProvider.COL_PRICE + " text, " + DataProvider.COL_PIC + " text, " + DataProvider.COL_PHONE
				+ " text, " + DataProvider.COL_TREATMENTS + " text);");
		db.execSQL("create table "
				+ DataProvider.TABLE_TREATMENTS
				+ " (_id integer primary key autoincrement, notificationid text, "
				+ DataProvider.COL_BEAUTICIAN_ID
				+ " text, "
				+ DataProvider.COL_NAME
				+ " text, address text, raters integer, rate integer, at text, location text, remarks text, price text, pic text);");
		// db.execSQL("create table profile (_id integer primary key autoincrement, name text, email text unique, count integer default 0, at datetime default current_timestamp);");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

}
