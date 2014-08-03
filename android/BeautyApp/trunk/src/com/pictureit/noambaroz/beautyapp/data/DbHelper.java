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
		db.execSQL("create table history (_id integer primary key autoincrement, for text, date text, treatments text, whare text, remarks text);");
		db.execSQL("create table ordersnotification (_id integer primary key autoincrement, notificationid text, beauticianid text, name text, address text, raters text, rate text, at text, location text, remarks text, price text);");
		// db.execSQL("create table profile (_id integer primary key autoincrement, name text, email text unique, count integer default 0, at datetime default current_timestamp);");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

}
