package com.pictureit.noambaroz.beautyapp;

import java.util.HashMap;

import utilities.Log;
import utilities.TimeUtils;
import utilities.server.HttpBase.HttpCallback;
import android.app.Fragment;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.pictureit.noambaroz.beautyapp.data.Constants;
import com.pictureit.noambaroz.beautyapp.data.DataProvider;
import com.pictureit.noambaroz.beautyapp.server.GetOrderNotification;
import com.pictureit.noambaroz.beautyapp.server.ImageLoaderUtil;

public class ActivityMessages extends ActivityWithFragment {

	@Override
	protected void initActivity() {
		Bundle data = getIntent().getExtras();
		if (data != null)
			Log.i(data.toString());
	}

	@Override
	protected void setFragment() {
		fragment = new FragmentMessages();
	}

	@Override
	protected void setFragmentTag() {
		FRAGMENT_TAG = "notification_fragment";
	}

	@Override
	public void onBackPressed() {
		backPressed();
	}

	private class FragmentMessages extends Fragment implements LoaderCallbacks<Cursor>, OnItemClickListener {

		int mRowViewInitialHeight;
		View mRowViewToRemove;
		private HashMap<String, GetOrderNotification> getOrderNotificationThreadPoll;
		SimpleCursorAdapter adapter;
		private ListView mListView;
		private ViewGroup emptyListIndicator;

		public FragmentMessages() {
		}

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			getOrderNotificationThreadPoll = new HashMap<String, GetOrderNotification>();

			String[] from = { DataProvider.COL_NAME, DataProvider.COL_ADDRESS, DataProvider.COL_AT,
					DataProvider.COL_PIC, DataProvider.COL_CLASSIFICATION };
			int[] to = { R.id.tv_base_beautician_row_name, R.id.tv_base_beautician_row_address,
					R.id.tv_row_message_received_date, R.id.iv_base_beautician_row_pic,
					R.id.tv_base_beautician_row_classification };

			adapter = new MySimpleCursorAdapter(getActivity(), R.layout.row_message, null, from, to, 0);
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View view = inflater.inflate(R.layout.fragment_messages, container, false);
			mListView = findView(view, R.id.messages_listView);
			emptyListIndicator = findView(view, R.id.vg_messages_empty_list_indicator);
			return view;
		}

		@Override
		public void onViewCreated(View view, Bundle savedInstanceState) {
			super.onViewCreated(view, savedInstanceState);
			initListview(mListView);
			mListView.setAdapter(adapter);
			mListView.setEmptyView(emptyListIndicator);
		}

		protected void getOrderInBackgroundByNotificationId(String row_id, HttpCallback callback) {
			if (!getOrderNotificationThreadPoll.containsKey(row_id)) {
				getOrderNotificationThreadPoll.put(row_id, new GetOrderNotification(getActivity(), row_id, callback));
				getOrderNotificationThreadPoll.get(row_id).execute();
			}
		}

		private void initListview(ListView listView) {
			listView.setOnItemClickListener(this);
			listView.setPadding(15, 0, 15, 0);
			listView.setDivider(new ColorDrawable(getResources().getColor(R.color.transparent)));
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
			CursorLoader loader = new CursorLoader(getActivity(), DataProvider.CONTENT_URI_MESSAGES, new String[] {
					DataProvider.COL_ID, DataProvider.COL_NOTIFICATION_ID, DataProvider.COL_BEAUTICIAN_ID,
					DataProvider.COL_PIC, DataProvider.COL_NAME, DataProvider.COL_ADDRESS, DataProvider.COL_RATERS,
					DataProvider.COL_RATE, DataProvider.COL_AT, DataProvider.COL_LOCATION, DataProvider.COL_REMARKS,
					DataProvider.COL_PRICE, DataProvider.COL_CLASSIFICATION }, null, null, DataProvider.COL_ID
					+ " DESC");
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

			public MySimpleCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
				super(context, layout, c, from, to, flags);
			}

			@Override
			public void setViewText(TextView v, String text) {
				if (v.getId() == R.id.tv_row_message_received_date && !TextUtils.isEmpty(text)
						&& !text.equalsIgnoreCase("null"))
					text = TimeUtils.timestampToDate(text);
				if (v.getId() == R.id.tv_base_beautician_row_classification && !TextUtils.isEmpty(text)
						&& !text.equalsIgnoreCase("null"))
					text = BeauticianUtil.getClassificationTypeById(getActivity(), text).getTitle();
				super.setViewText(v, text);
			}

			@Override
			public void setViewImage(ImageView v, String value) {
				super.setViewImage(v, value);
				if (v.getId() == R.id.iv_base_beautician_row_pic) {
					ImageLoaderUtil.display(value, v, R.drawable.row_avatar, getActivity());
				}
			}

			@Override
			public void bindView(final View view, final Context context, final Cursor cursor) {
				Log.i("");
				super.bindView(view, context, cursor);

				String rowId = getCursor().getString(getCursor().getColumnIndex(DataProvider.COL_NOTIFICATION_ID));
				if (getCursor().getString(getCursor().getColumnIndex(DataProvider.COL_NAME)) == null && rowId != null) {
					getOrderInBackgroundByNotificationId(rowId, new HttpCallback() {

						@Override
						public void onAnswerReturn(Object answer) {
							if (answer != null) {
								Log.i("getView", "Finised downloading notification");
								findView(view, R.id.pb_row_order_notification_spinner).setVisibility(View.GONE);
								findView(view, R.id.vg_row_order_notification_data_container).setVisibility(
										View.VISIBLE);
								notifyDataSetChanged();
							}
						}
					});
				} else {
					findView(view, R.id.pb_row_order_notification_spinner).setVisibility(View.GONE);
					findView(view, R.id.vg_row_order_notification_data_container).setVisibility(View.VISIBLE);
				}

				// Button b = findView(view,
				// R.id.b_row_order_notification_dismiss);
				// b.setOnClickListener(new OnClickListener() {
				//
				// @Override
				// public void onClick(View v) {
				// AnimationManager.collapseCursorAdapterRow(view, 0, new
				// OnRowCollapseCallback() {
				//
				// @Override
				// public void onCollapse(View v, int initialHeight) {
				// context.getContentResolver().delete(
				// Uri.withAppendedPath(DataProvider.CONTENT_URI_ORDER_OPTIONS,
				// cursor.getString(cursor.getColumnIndex("_id"))), null, null);
				// mRowViewInitialHeight = initialHeight;
				// mRowViewToRemove = v;
				// }
				//
				// });
				// }
				// });
			}
		}

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			Cursor c = ((MySimpleCursorAdapter) parent.getAdapter()).getCursor();
			c.moveToPosition(position);
			String messageId = c.getString(c.getColumnIndex(DataProvider.COL_NOTIFICATION_ID));
			String beauticianName = c.getString(c.getColumnIndex(DataProvider.COL_NAME));
			boolean everythingOk = !TextUtils.isEmpty(beauticianName) && !TextUtils.isEmpty(messageId);
			if (everythingOk) {
				Intent intent = new Intent(getActivity(), ActivityMessagesInner.class);
				intent.putExtra(Constants.EXTRA_MESSAGE_ID, messageId);
				startActivity(intent);
				overridePendingTransition(R.anim.activity_enter_slidein_anim, R.anim.activity_exit_shrink_anim);
			}
			// getActivity().getContentResolver().delete(
			// Uri.withAppendedPath(DataProvider.CONTENT_URI_MESSAGES,
			// c.getString(c.getColumnIndex("_id"))),
			// null, null);
		}
	}
}
