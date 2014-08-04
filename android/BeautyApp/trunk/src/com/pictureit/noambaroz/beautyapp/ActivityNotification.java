package com.pictureit.noambaroz.beautyapp;

import java.util.HashMap;

import utilities.Log;
import android.app.ListFragment;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.SimpleCursorAdapter;
import android.widget.SimpleCursorAdapter.ViewBinder;

import com.pictureit.noambaroz.beautyapp.data.DataProvider;
import com.pictureit.noambaroz.beautyapp.server.GetOrderNotification;

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

		private HashMap<String, GetOrderNotification> getOrderNotificationThreadPoll;
		SimpleCursorAdapter adapter;

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			getOrderNotificationThreadPoll = new HashMap<String, GetOrderNotification>();
			adapter = new SimpleCursorAdapter(getActivity(), R.layout.row_order_notification, null, new String[] {
					DataProvider.COL_NAME, DataProvider.COL_PRICE }, new int[] { R.id.tv_row_order_notification,
					R.id.tv_row_order_notification1 }, 0);

			adapter.setViewBinder(new ViewBinder() {

				@Override
				public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
					if (cursor.getString(cursor.getColumnIndex(DataProvider.COL_NAME)) == null
							&& cursor.getString(cursor.getColumnIndex(DataProvider.COL_NOTIFICATION_ID)) != null) {
						getOrderInBackgroundByNotificationId(DataProvider.COL_NOTIFICATION_ID);
					}
					return false;
				}
			});
			setListAdapter(adapter);
		}

		@Override
		public void onViewCreated(View view, Bundle savedInstanceState) {
			super.onViewCreated(view, savedInstanceState);
			initListview();
		}

		protected void getOrderInBackgroundByNotificationId(String row_id) {
			if (!getOrderNotificationThreadPoll.containsKey(row_id)) {
				getOrderNotificationThreadPoll.put(row_id, new GetOrderNotification(getActivity(), row_id));
				// TODO getOrderNotificationThreadPoll.get(row_id).execute();
			}
		}

		private void initListview() {
			getListView().setOnItemClickListener(null);
			getListView().setPadding(15, 0, 15, 0);
			getListView().setDivider(new ColorDrawable(getResources().getColor(R.color.transparent)));
			getListView().setDividerHeight(15);
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
			CursorLoader loader = new CursorLoader(getActivity(), DataProvider.CONTENT_URI_ORDER_OPTIONS, new String[] {
					DataProvider.COL_ID, DataProvider.COL_NOTIFICATION_ID, DataProvider.COL_BEAUTICIAN_ID,
					DataProvider.COL_NAME, DataProvider.COL_ADDRESS, DataProvider.COL_RATERS, DataProvider.COL_RATE,
					DataProvider.COL_AT, DataProvider.COL_LOCATION, DataProvider.COL_REMARKS, DataProvider.COL_PRICE },
					null, null, DataProvider.COL_ID + " DESC");
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
