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
		db.execSQL("create table " + DataProvider.TABLE_MESSAGES + " (_id integer primary key autoincrement, "
				+ DataProvider.COL_NOTIFICATION_ID + " text, " + DataProvider.COL_BEAUTICIAN_ID + " text, "
				+ DataProvider.COL_NAME + " text, " + DataProvider.COL_ADDRESS + " text, " + DataProvider.COL_RATERS
				+ " integer, " + DataProvider.COL_RATE + " integer, " + DataProvider.COL_AT + " text, "
				+ DataProvider.COL_LOCATION + " text, " + DataProvider.COL_REMARKS + " text, " + DataProvider.COL_PRICE
				+ " text, " + DataProvider.COL_PIC + " text, " + DataProvider.COL_PHONE + " text, "
				+ DataProvider.COL_TREATMENTS + " text);");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

}
