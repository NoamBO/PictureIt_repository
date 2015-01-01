package com.pictureit.noambaroz.beauticianapp.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class DataProvider extends ContentProvider {

	public static final String SORT_ORDER_LAST_TO_FIRST = "DESC";
	public static final String SORT_ORDER_FIRST_TO_LAST = "ASC";

	public static final String COL_ID = "_id";

	public static final String TABLE_ORDERS_AROUND_ME = "tableorders";
	public static final String COL_FIRST_NAME = "private_name";
	public static final String COL_LAST_NAME = "family_name";
	public static final String COL_LATITUDE = "latitude";
	public static final String COL_LONGITUDE = "longitude";
	public static final String COL_ORDER_ID = "orderid";
	public static final String COL_IS_DIRECTED_TO_ME = "is_first_priority";

	public static final String TABLE_ALARMS = "tablealarms";
	public static final String COL_CUSTOMER_NAME = "name";
	public static final String COL_TREATMENT = "treatment";
	public static final String COL_IMAGE_URL = "image_url";
	public static final String COL_IS_PLAYED = "ia_played";
	public static final String COL_TREATMENT_TIME = "treatment_time";
	public static final String COL_ALARM_TIME = "alarm_time";

	private DbHelper dbHelper;

	public static final Uri CONTENT_URI_ORDERS_AROUND_ME = Uri
			.parse("content://com.pictureit.noambaroz.beauticianapp.provider/" + TABLE_ORDERS_AROUND_ME);
	public static final Uri CONTENT_URI_ALARMS = Uri.parse("content://com.pictureit.noambaroz.beauticianapp.provider/"
			+ TABLE_ALARMS);

	private static final int ORDERS_AROUND_ME_ALLROWS = 7;
	private static final int ORDERS_AROUND_ME_SINGLE_ROW = 8;
	private static final int ALARMS_ALLROWS = 3;
	private static final int ALARMS_SINGLE_ROW = 4;

	private static final UriMatcher uriMatcher;
	static {
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI("com.pictureit.noambaroz.beauticianapp.provider", TABLE_ORDERS_AROUND_ME,
				ORDERS_AROUND_ME_ALLROWS);
		uriMatcher.addURI("com.pictureit.noambaroz.beauticianapp.provider", TABLE_ORDERS_AROUND_ME + "/#",
				ORDERS_AROUND_ME_SINGLE_ROW);

		uriMatcher.addURI("com.pictureit.noambaroz.beauticianapp.provider", TABLE_ALARMS, ALARMS_ALLROWS);
		uriMatcher.addURI("com.pictureit.noambaroz.beauticianapp.provider", TABLE_ALARMS + "/#", ALARMS_SINGLE_ROW);
	}

	@Override
	public boolean onCreate() {
		dbHelper = new DbHelper(getContext());
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

		switch (uriMatcher.match(uri)) {
		case ORDERS_AROUND_ME_ALLROWS:
		case ALARMS_ALLROWS:
			qb.setTables(getTableName(uri));
			break;

		case ORDERS_AROUND_ME_SINGLE_ROW:
		case ALARMS_SINGLE_ROW:
			qb.setTables(getTableName(uri));
			qb.appendWhere("_id = " + uri.getLastPathSegment());
			break;

		default:
			throw new IllegalArgumentException("Unsupported URI: " + uri);
		}
		Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);
		c.setNotificationUri(getContext().getContentResolver(), uri);
		return c;
	}

	@Override
	public String getType(Uri uri) {
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();

		long id;
		switch (uriMatcher.match(uri)) {
		case ORDERS_AROUND_ME_ALLROWS:
			id = db.insertOrThrow(TABLE_ORDERS_AROUND_ME, null, values);
			break;
		case ALARMS_ALLROWS:
			id = db.insertOrThrow(TABLE_ALARMS, null, values);
			break;
		default:
			throw new IllegalArgumentException("Unsupported URI: " + uri);
		}

		Uri insertUri = ContentUris.withAppendedId(uri, id);
		getContext().getContentResolver().notifyChange(insertUri, null);
		return insertUri;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		db.beginTransaction();
		int count;
		switch (uriMatcher.match(uri)) {
		case ORDERS_AROUND_ME_ALLROWS:
		case ALARMS_ALLROWS:
			count = db.delete(getTableName(uri), selection, selectionArgs);
			break;

		case ORDERS_AROUND_ME_SINGLE_ROW:
		case ALARMS_SINGLE_ROW:
			count = db.delete(getTableName(uri), "_id = ?", new String[] { uri.getLastPathSegment() });
			break;

		default:
			throw new IllegalArgumentException("Unsupported URI: " + uri);
		}
		db.setTransactionSuccessful();
		db.endTransaction();
		// getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();

		int count;
		switch (uriMatcher.match(uri)) {
		case ORDERS_AROUND_ME_ALLROWS:
		case ALARMS_ALLROWS:
			count = db.update(getTableName(uri), values, selection, selectionArgs);
			break;

		case ORDERS_AROUND_ME_SINGLE_ROW:
		case ALARMS_SINGLE_ROW:
			count = db.update(getTableName(uri), values, "_id = ?", new String[] { uri.getLastPathSegment() });
			break;

		default:
			throw new IllegalArgumentException("Unsupported URI: " + uri);
		}

		getContext().getContentResolver().notifyChange(uri, null);
		return count;

	}

	private String getTableName(Uri uri) {
		switch (uriMatcher.match(uri)) {
		case ORDERS_AROUND_ME_ALLROWS:
		case ORDERS_AROUND_ME_SINGLE_ROW:
			return TABLE_ORDERS_AROUND_ME;
		case ALARMS_ALLROWS:
		case ALARMS_SINGLE_ROW:
			return TABLE_ALARMS;
		}

		return null;
	}
}
