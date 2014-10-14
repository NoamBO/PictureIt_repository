package com.pictureit.noambaroz.beautyapp.data;

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

	// public static final String TABLE_MY_PROFILE = "tabletreatments";
	// public static final String COL_MY_NAME = "myname";
	// public static final String COL_MY_LAST_NAME = "mylastname";
	// public static final String COL_MY_ADDRESS = "myaddress";
	// public static final String COL_MY_EMAIL = "myemail";

	public static final String TABLE_MESSAGES = "tablemessages";
	public static final String TABLE_TREATMENTS = "tabletreatments";
	public static final String COL_BEAUTICIAN_ID = "beauticianid";
	public static final String COL_NOTIFICATION_ID = "notificationid";
	public static final String COL_NAME = "beautician_name";
	public static final String COL_PIC = "pic";
	public static final String COL_ADDRESS = "address";
	public static final String COL_RATERS = "raters";
	public static final String COL_RATE = "rate";
	public static final String COL_AT = "at";
	public static final String COL_LOCATION = "location";
	public static final String COL_PRICE = "price";
	public static final String COL_NOTES = "notes";
	public static final String COL_PHONE = "phone";

	public static final String TABLE_HISTORY = "history";
	public static final String COL_FOR = "for";
	public static final String COL_DATE = "date";
	public static final String COL_TREATMENTS = "treatments";
	public static final String COL_WHARE = "whare";
	public static final String COL_REMARKS = "remarks";

	private DbHelper dbHelper;

	// public static final Uri CONTENT_URI_MY_PROFILE =
	// Uri.parse("content://com.pictureit.noambaroz.beautyapp.provider/"
	// + TABLE_HISTORY);
	public static final Uri CONTENT_URI_HISTORY = Uri.parse("content://com.pictureit.noambaroz.beautyapp.provider/"
			+ TABLE_HISTORY);
	public static final Uri CONTENT_URI_TREATMENTS = Uri.parse("content://com.pictureit.noambaroz.beautyapp.provider/"
			+ TABLE_TREATMENTS);
	public static final Uri CONTENT_URI_MESSAGES = Uri.parse("content://com.pictureit.noambaroz.beautyapp.provider/"
			+ TABLE_TREATMENTS);

	// private static final int MY_PROFILE_ALLROWS = 5;
	// private static final int MY_PROFILE_SINGLE_ROW = 6;
	private static final int HISTORY_ALLROWS = 1;
	private static final int HISTORY_SINGLE_ROW = 2;
	private static final int ORDER_OPTIONS_ALLROWS = 3;
	private static final int ORDER_OPTIONS_SINGLE_ROW = 4;
	private static final int MESSAGES_ALLROWS = 7;
	private static final int MESSAGES_SINGLE_ROW = 8;

	private static final UriMatcher uriMatcher;
	static {
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI("com.pictureit.noambaroz.beautyapp.provider", TABLE_HISTORY, HISTORY_ALLROWS);
		uriMatcher.addURI("com.pictureit.noambaroz.beautyapp.provider", TABLE_HISTORY + "/#", HISTORY_SINGLE_ROW);
		uriMatcher.addURI("com.pictureit.noambaroz.beautyapp.provider", TABLE_TREATMENTS + "/", ORDER_OPTIONS_ALLROWS);
		uriMatcher.addURI("com.pictureit.noambaroz.beautyapp.provider", TABLE_TREATMENTS + "/#",
				ORDER_OPTIONS_SINGLE_ROW);
		uriMatcher.addURI("com.pictureit.noambaroz.beautyapp.provider", TABLE_MESSAGES, MESSAGES_ALLROWS);
		uriMatcher.addURI("com.pictureit.noambaroz.beautyapp.provider", TABLE_MESSAGES + "/#", MESSAGES_SINGLE_ROW);
		// uriMatcher.addURI("com.pictureit.noambaroz.beautyapp.provider",
		// TABLE_MY_PROFILE, MY_PROFILE_ALLROWS);
		// uriMatcher.addURI("com.pictureit.noambaroz.beautyapp.provider",
		// TABLE_MY_PROFILE + "/#", MY_PROFILE_SINGLE_ROW);
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
		case HISTORY_ALLROWS:
		case ORDER_OPTIONS_ALLROWS:
		case MESSAGES_ALLROWS:
			// case MY_PROFILE_ALLROWS:
			qb.setTables(getTableName(uri));
			break;

		case HISTORY_SINGLE_ROW:
		case ORDER_OPTIONS_SINGLE_ROW:
		case MESSAGES_SINGLE_ROW:
			// case MY_PROFILE_SINGLE_ROW:
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
		case HISTORY_ALLROWS:
			id = db.insertOrThrow(TABLE_HISTORY, null, values);
			break;
		case ORDER_OPTIONS_ALLROWS:
			id = db.insertOrThrow(TABLE_TREATMENTS, null, values);
			break;
		case MESSAGES_ALLROWS:
			id = db.insertOrThrow(TABLE_MESSAGES, null, values);
			break;
		// case MY_PROFILE_ALLROWS:
		// id = db.insertOrThrow(TABLE_MY_PROFILE, null, values);
		// break;
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

		int count;
		switch (uriMatcher.match(uri)) {
		case HISTORY_ALLROWS:
		case ORDER_OPTIONS_ALLROWS:
		case MESSAGES_ALLROWS:
			count = db.delete(getTableName(uri), selection, selectionArgs);
			break;

		case HISTORY_SINGLE_ROW:
		case ORDER_OPTIONS_SINGLE_ROW:
		case MESSAGES_SINGLE_ROW:
			count = db.delete(getTableName(uri), "_id = ?", new String[] { uri.getLastPathSegment() });
			break;

		default:
			throw new IllegalArgumentException("Unsupported URI: " + uri);
		}

		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();

		int count;
		switch (uriMatcher.match(uri)) {
		case HISTORY_ALLROWS:
		case ORDER_OPTIONS_ALLROWS:
		case MESSAGES_ALLROWS:
			// case MY_PROFILE_ALLROWS:
			count = db.update(getTableName(uri), values, selection, selectionArgs);
			break;

		case HISTORY_SINGLE_ROW:
		case ORDER_OPTIONS_SINGLE_ROW:
		case MESSAGES_SINGLE_ROW:
			// case MY_PROFILE_SINGLE_ROW:
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
		case HISTORY_ALLROWS:
		case HISTORY_SINGLE_ROW:
			return TABLE_HISTORY;
		case ORDER_OPTIONS_ALLROWS:
		case ORDER_OPTIONS_SINGLE_ROW:
			return TABLE_TREATMENTS;
		case MESSAGES_ALLROWS:
		case MESSAGES_SINGLE_ROW:
			return TABLE_MESSAGES;
			// case MY_PROFILE_ALLROWS:
			// case MY_PROFILE_SINGLE_ROW:
			// return TABLE_MY_PROFILE;
		}

		return null;
	}
}
