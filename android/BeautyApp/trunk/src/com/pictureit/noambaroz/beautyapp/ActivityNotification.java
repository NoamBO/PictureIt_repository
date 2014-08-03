package com.pictureit.noambaroz.beautyapp;

import utilities.Log;
import android.app.ListFragment;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.SimpleCursorAdapter;
import android.widget.SimpleCursorAdapter.ViewBinder;

import com.pictureit.noambaroz.beautyapp.data.DataProvider;

public class ActivityNotification extends ActivityWithFragment {

	@Override
	protected void initActivity() {
		Bundle data = getIntent().getExtras();
		if (data != null)
			Log.i(data.toString());
	}

	@Override
	protected void setFragment() {
		fragment = new NotificationActivity();
	}

	@Override
	protected void setFragmentTag() {
		FRAGMENT_TAG = "notification_fragment";
	}

	private class NotificationActivity extends ListFragment implements LoaderCallbacks<Cursor> {

		SimpleCursorAdapter adapter;

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			adapter = new SimpleCursorAdapter(getActivity(), R.layout.row_order_notification, null, new String[] {
					DataProvider.COL_NOTIFICATION_ID, DataProvider.COL_NAME }, new int[] {
					R.id.tv_row_order_notification, R.id.tv_row_order_notification1 }, 0);

			adapter.setViewBinder(new ViewBinder() {

				@Override
				public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
					switch (view.getId()) {
					case R.id.tv_row_order_notification1:
						if (cursor.getString(cursor.getColumnIndex(DataProvider.COL_NAME)) == null
								&& cursor.getString(cursor.getColumnIndex(DataProvider.COL_NOTIFICATION_ID)) != null) {
							getOrderInBackgroundByNotificationId(cursor.getString(0), DataProvider.COL_NOTIFICATION_ID);
						} else {

						}
						break;
					}
					return false;
				}
			});
			setListAdapter(adapter);
		}

		protected void getOrderInBackgroundByNotificationId(String row_id, String colNotificationId) {
			ContentValues values = new ContentValues(1);
			values.put(DataProvider.COL_NAME, "olga ");
			getContentResolver().update(DataProvider.CONTENT_URI_ORDER_OPTIONS, values, "_id =" + row_id, null);
		}

		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);
			getLoaderManager().initLoader(0, null, this);
		}

		@Override
		public void onDestroy() {
			Intent i = new Intent(getActivity(), MainActivity.class);
			i.putExtra("exit", true);
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(i);
			super.onDestroy();
		}

		@Override
		public Loader<Cursor> onCreateLoader(int id, Bundle args) {
			CursorLoader loader = new CursorLoader(getActivity(), DataProvider.CONTENT_URI_ORDER_OPTIONS,
					new String[] { DataProvider.COL_ID, DataProvider.COL_NOTIFICATION_ID,
							DataProvider.COL_BEAUTICIAN_ID, DataProvider.COL_NAME, DataProvider.COL_ADDRESS,
							DataProvider.COL_RATERS, DataProvider.COL_RATE, DataProvider.COL_WHEN,
							DataProvider.COL_LOCATION, DataProvider.COL_REMARKS, DataProvider.COL_PRICE }, null, null,
					DataProvider.COL_ID + " DESC");
			return loader;
		}

		@Override
		public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
			adapter.swapCursor(data);
		}

		@Override
		public void onLoaderReset(Loader<Cursor> loader) {
			adapter.swapCursor(null);
		}
	}
}
