package com.pictureit.noambaroz.beauticianapp;

import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.pictureit.noambaroz.beauticianapp.data.Formatter;
import com.pictureit.noambaroz.beauticianapp.data.Message;
import com.pictureit.noambaroz.beauticianapp.data.TimeUtils;
import com.pictureit.noambaroz.beauticianapp.dialog.Dialogs;
import com.pictureit.noambaroz.beauticianapp.server.GetMessages;
import com.pictureit.noambaroz.beauticianapp.server.HttpBase.HttpCallback;
import com.pictureit.noambaroz.beauticianapp.server.ImageLoaderUtil;
import com.pictureit.noambaroz.beauticianapp.server.RemoveMessageFromListTask;

public class ActivityMessages extends ActivityWithFragment {

	@Override
	protected void initActivity() {
	}

	@Override
	protected void setFragment() {
		fragment = new MessagesFragment();
	}

	@Override
	protected void setFragmentTag() {
		FRAGMENT_TAG = "messages_fragment";
	}

	@Override
	public void onBackPressed() {
		backPressed();
	}

	public static boolean active = false;

	@Override
	public void onStart() {
		super.onStart();
		active = true;
	}

	@Override
	public void onStop() {
		super.onStop();
		active = false;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		((MessagesFragment) fragment).onMyActivityResult(requestCode, resultCode, data);
		super.onActivityResult(requestCode, resultCode, data);
	}

	public static class MessagesFragment extends BaseFragment implements HttpCallback, OnItemClickListener {

        public MessagesFragment(){}

		private ListView listView;
		private TextView emptyListIndicator;

		private MyAdapter adapter;

		private BroadcastReceiver mReceiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				if (intent.hasExtra(Constant.EXTRA_ORDER_ID)) {
					String orderId = intent.getStringExtra(Constant.EXTRA_ORDER_ID);
					for (int i = 0; i < adapter.getCount(); i++) {
						if (adapter.getItem(i).getOrderid().equalsIgnoreCase(orderId)) {
							adapter.remove(adapter.getItem(i));
							adapter.notifyDataSetChanged();
						}
					}
				} else {
					new GetMessages(getActivity(), MessagesFragment.this).execute();
				}
			}
		};

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			getActivity().registerReceiver(mReceiver, new IntentFilter(Constant.INTENT_FILTER_MESSAGE_DELETED));
		}

		@Override
		public void onDestroy() {
            getActivity().unregisterReceiver(mReceiver);
			super.onDestroy();
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

			View v = inflater.inflate(R.layout.fragment_list_layout, container, false);
			listView = findView(v, R.id.lv_fragment_list_listview);
			emptyListIndicator = findView(v, R.id.tv_fragment_list_empty_list_indicator);
			emptyListIndicator.setText(R.string.no_messages);
			if (adapter == null) {
				new GetMessages(getActivity(), this).execute();
			}
			return v;
		}

		@SuppressWarnings("unchecked")
		@Override
		public void onAnswerReturn(Object answer) {
			if (answer instanceof Integer) {
				Dialogs.showServerFailedDialog(getActivity());
				return;
			}

			ArrayList<Message> arrayList = (ArrayList<Message>) answer;

			adapter = new ActivityMessages.MyAdapter(getActivity(), android.R.layout.simple_list_item_2, arrayList);
			listView.setAdapter(adapter);
			listView.setOnItemClickListener(this);
			listView.setEmptyView(emptyListIndicator);

		}

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			if (!adapter.isClickEnable(position))
				return;

			Message m = adapter.getItem(position);
			Intent intent = new Intent(getActivity(), ActivityMessage.class);
			intent.putExtra(Constant.EXTRA_MESSAGE_OBJECT, m);
			getActivity().startActivityForResult(intent, Constant.REQUEST_CODE_SINGLE_MESSAGE);
            getActivity().overridePendingTransition(R.anim.activity_enter_slidein_anim, R.anim.activity_exit_shrink_anim);
		}

		public void onMyActivityResult(int requestCode, int resultCode, Intent data) {
			if (requestCode == Constant.REQUEST_CODE_SINGLE_MESSAGE && resultCode == RESULT_OK
					&& data.getExtras() != null)

				for (int i = 0; i < adapter.getCount(); i++) {
					if (adapter.getItem(i).getOrderid().equalsIgnoreCase(data.getStringExtra(Constant.EXTRA_ORDER_ID))) {
						adapter.remove(adapter.getItem(i));
						adapter.notifyDataSetChanged();
					}
				}
		}
	}

	private static class MyAdapter extends ArrayAdapter<Message> {

		public MyAdapter(Context context, int resource, List<Message> objects) {
			super(context, resource, objects);
		}

		public boolean isClickEnable(int itemPosition) {
			return !getItem(itemPosition).isMessageDisabled();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final ViewHolder holder;

			if (convertView == null) {
				holder = new ViewHolder();
				LayoutInflater inflater = LayoutInflater.from(getContext());
				convertView = inflater.inflate(R.layout.row_upcoming_treatments, parent, false);

				holder.image = (ImageView) convertView.findViewById(R.id.iv_row_upcoming_treatments);
				holder.date = (TextView) convertView.findViewById(R.id.tv_row_upcoming_treatments_date);
				holder.address = (TextView) convertView.findViewById(R.id.tv_row_upcoming_treatments_address);
				holder.name = (TextView) convertView.findViewById(R.id.tv_row_upcoming_treatments_customer_name);
				holder.treatment = (TextView) convertView.findViewById(R.id.tv_row_upcoming_treatments_type_name);

				holder.disableContainer = (FrameLayout) convertView.findViewById(R.id.fl_row_upcoming_treatments_disabled_container);
				holder.statusDisableTitle = (TextView) convertView.findViewById(R.id.tv_row_upcoming_treatments_disabled_container_title);
				holder.statusDisableRemove = (TextView) convertView.findViewById(R.id.tv_row_upcoming_treatments_disabled_container_remove);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.date.setText(TimeUtils.timestampToDateWithHour(getItem(position).getMessageSentTime()));
			holder.name.setText(getItem(position).getClientName());
			holder.address.setText(getItem(position).getClientAdress());
			holder.treatment.setText(Formatter.getSelf(getContext())
					.getTreatmentName(getItem(position).getTreatments()));

			{
				int w = (int) getContext().getResources().getDimension(R.dimen.beautician_picture_width);
				int h = (int) getContext().getResources().getDimension(R.dimen.beautician_picture_height);
				ImageLoaderUtil.display(getItem(position).getImageUrl(), holder.image, -1, w, h);
			}

			if (getItem(position).isMessageDisabled()) {
				holder.disableContainer.setVisibility(View.VISIBLE);
				holder.statusDisableTitle.setText(R.string.message_declined);
				holder.statusDisableRemove.setTag(position);
				holder.statusDisableRemove.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						final int position = (Integer) v.getTag();
						new RemoveMessageFromListTask(getContext(), new HttpCallback() {

							@Override
							public void onAnswerReturn(Object answer) {
								if (answer instanceof Integer)
									Dialogs.showServerFailedDialog(getContext());
								else
									remove(getItem(position));
							}
						}, getItem(position).getOrderid()).execute();
					}
				});
			} else {
				holder.disableContainer.setVisibility(View.GONE);
				holder.statusDisableRemove.setOnClickListener(null);
			}

			return convertView;
		}

		private class ViewHolder {
			ImageView image;
			TextView date, name, address, treatment;
			FrameLayout disableContainer;
			TextView statusDisableTitle, statusDisableRemove;
		}

	}

}
