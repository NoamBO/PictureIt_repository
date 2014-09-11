package com.pictureit.noambaroz.beautyapp;

import java.util.HashMap;

import utilities.Log;
import utilities.server.HttpBase.HttpCallback;
import android.app.ListFragment;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.pictureit.noambaroz.beautyapp.animation.AnimationManager;
import com.pictureit.noambaroz.beautyapp.animation.AnimationManager.OnRowCollapseCallback;
import com.pictureit.noambaroz.beautyapp.data.DataProvider;
import com.pictureit.noambaroz.beautyapp.server.GetOrderNotification;
import com.pictureit.noambaroz.beautyapp.server.ImageLoaderUtil;

public class ActivityTreatments extends ActivityWithFragment {

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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_activity_treatments, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_ask_for_service) {
			Intent intent = new Intent(ActivityTreatments.this, ServiceOrder.class);
			startActivity(intent);
			overridePendingTransition(R.anim.activity_enter_slidein_anim, R.anim.activity_exit_shrink_anim);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private class NotificationActivity extends ListFragment implements LoaderCallbacks<Cursor> {

		int mRowViewInitialHeight;
		View mRowViewToRemove;
		private HashMap<String, GetOrderNotification> getOrderNotificationThreadPoll;
		SimpleCursorAdapter adapter;

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			getOrderNotificationThreadPoll = new HashMap<String, GetOrderNotification>();

			String[] from = { DataProvider.COL_NAME, DataProvider.COL_ADDRESS, DataProvider.COL_PIC,
					DataProvider.COL_RATERS, DataProvider.COL_RATE, DataProvider.COL_AT, DataProvider.COL_LOCATION,
					DataProvider.COL_PRICE };
			int[] to = { R.id.tv_row_order_notification, R.id.tv_row_order_notification1,
					R.id.iv_row_order_notification_pic };

			adapter = new MySimpleCursorAdapter(getActivity(), R.layout.row_order_notification, null, from, to, 0);
			setListAdapter(adapter);
		}

		@Override
		public void onViewCreated(View view, Bundle savedInstanceState) {
			super.onViewCreated(view, savedInstanceState);
			initListview();
		}

		protected void getOrderInBackgroundByNotificationId(int position, String row_id, HttpCallback callback) {
			if (!getOrderNotificationThreadPoll.containsKey(row_id)) {
				getOrderNotificationThreadPoll.put(String.valueOf(position), new GetOrderNotification(getActivity(),
						row_id, callback));
				// TODO getOrderNotificationThreadPoll.get(row_id).execute();
			}
		}

		private void initListview() {
			getListView().setOnItemClickListener(null);
			getListView().setPadding(15, 0, 15, 0);
			getListView().setDivider(new ColorDrawable(getResources().getColor(R.color.transparent)));
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
					DataProvider.COL_PIC, DataProvider.COL_NAME, DataProvider.COL_ADDRESS, DataProvider.COL_RATERS,
					DataProvider.COL_RATE, DataProvider.COL_AT, DataProvider.COL_LOCATION, DataProvider.COL_REMARKS,
					DataProvider.COL_PRICE }, null, null, DataProvider.COL_ID + " DESC");
			return loader;
		}

		@Override
		public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
			Log.i("");
			adapter.swapCursor(data);
			if (mRowViewInitialHeight != 0 && mRowViewToRemove != null) {
				mRowViewToRemove.getLayoutParams().height = mRowViewInitialHeight;
				mRowViewToRemove.requestLayout();
				mRowViewInitialHeight = 0;
				mRowViewToRemove = null;
			}
		}

		@Override
		public void onLoaderReset(Loader<Cursor> loader) {
			adapter.swapCursor(null);
		}

		private class MySimpleCursorAdapter extends SimpleCursorAdapter {

			private DisplayImageOptions options = ImageLoaderUtil.getDisplayImageOptions();
			private com.nostra13.universalimageloader.core.ImageLoader imageLoader = com.nostra13.universalimageloader.core.ImageLoader
					.getInstance();

			public MySimpleCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
				super(context, layout, c, from, to, flags);
			}

			@Override
			public void setViewImage(ImageView view, String value) {
				Log.i("");
				if (view.getId() == R.id.iv_row_order_notification_pic && value != null && value.contains("http")) {
					imageLoader.displayImage(value, (ImageView) view, options);
				}
			}

			@Override
			public void bindView(final View view, final Context context, final Cursor cursor) {
				Log.i("");
				super.bindView(view, context, cursor);

				String rowId = getCursor().getString(getCursor().getColumnIndex(DataProvider.COL_NOTIFICATION_ID));
				if (getCursor().getString(getCursor().getColumnIndex(DataProvider.COL_NAME)) == null && rowId != null) {
					getOrderInBackgroundByNotificationId(cursor.getPosition(), rowId, new HttpCallback() {

						@Override
						public void onAnswerReturn(Object answer) {
							if (answer != null) {
								Log.i("getView", "Finised downloading notification");
								findView(view, R.id.pb_row_order_notification_spinner).setVisibility(View.GONE);
								findView(view, R.id.ll_row_order_notification_data_container).setVisibility(
										View.VISIBLE);
							}
						}
					});
				} else {
					findView(view, R.id.pb_row_order_notification_spinner).setVisibility(View.GONE);
					findView(view, R.id.ll_row_order_notification_data_container).setVisibility(View.VISIBLE);
				}

				Button b = findView(view, R.id.b_row_order_notification_dismiss);
				b.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						AnimationManager.collapseCursorAdapterRow(view, 0, new OnRowCollapseCallback() {

							@Override
							public void onCollapse(View v, int initialHeight) {
								context.getContentResolver().delete(
										Uri.withAppendedPath(DataProvider.CONTENT_URI_ORDER_OPTIONS,
												cursor.getString(cursor.getColumnIndex("_id"))), null, null);
								mRowViewInitialHeight = initialHeight;
								mRowViewToRemove = v;
							}

						});
					}
				});
			}
		}
	}
}