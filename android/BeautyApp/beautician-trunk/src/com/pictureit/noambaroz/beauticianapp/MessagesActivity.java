package com.pictureit.noambaroz.beauticianapp;

import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.pictureit.noambaroz.beauticianapp.animation.AnimationManager;
import com.pictureit.noambaroz.beauticianapp.data.Message;
import com.pictureit.noambaroz.beauticianapp.data.TimeUtils;
import com.pictureit.noambaroz.beauticianapp.data.TreatmentsFormatter;
import com.pictureit.noambaroz.beauticianapp.server.GetMessages;
import com.pictureit.noambaroz.beauticianapp.server.HttpBase.HttpCallback;
import com.pictureit.noambaroz.beauticianapp.server.ImageLoaderUtil;
import com.pictureit.noambaroz.beautycianapp.R;

public class MessagesActivity extends ActivityWithFragment {

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

	private class MessagesFragment extends Fragment implements HttpCallback, OnItemClickListener {

		private ListView listView;
		private TextView emptyListIndicator;

		private MyAdapter adapter;
		private ArrayList<Message> arrayList;

		@Override
		public void onCreate(Bundle savedInstanceState) {
			arrayList = new ArrayList<Message>();
			super.onCreate(savedInstanceState);
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

			View v = inflater.inflate(R.layout.fragment_messages, container, false);
			listView = findView(v, R.id.lv_fragment_messages_listview);
			emptyListIndicator = findView(v, R.id.tv_messages_empty_list_indicator);
			if (adapter == null)
				new GetMessages(getActivity(), this).execute();

			return v;
		}

		@SuppressWarnings("unchecked")
		@Override
		public void onAnswerReturn(Object answer) {
			if (answer instanceof Integer)
				return;

			arrayList = (ArrayList<Message>) answer;
			if (checkStatus()) {
				adapter = new MyAdapter(MessagesActivity.this, android.R.layout.simple_list_item_2, arrayList);
				listView.setAdapter(adapter);
				listView.setOnItemClickListener(this);
			}
		}

		private boolean checkStatus() {
			if (arrayList.size() > 0) {
				if (emptyListIndicator.getVisibility() == View.VISIBLE)
					AnimationManager.fadeOut(getActivity(), emptyListIndicator);
				return true;
			} else if (emptyListIndicator.getVisibility() != View.VISIBLE)
				AnimationManager.fadeIn(getActivity(), emptyListIndicator);
			return false;
		}

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			Message m = adapter.getItem(position);
			Intent intent = new Intent(getActivity(), MessageActivity.class);
			intent.putExtra(Constant.EXTRA_MESSAGE_OBJECT, m);
			startActivityForResult(intent, Constant.REQUEST_CODE_SINGLE_MESSAGE);
			overridePendingTransition(R.anim.activity_enter_slidein_anim, R.anim.activity_exit_shrink_anim);
		}

		@Override
		public void onActivityResult(int requestCode, int resultCode, Intent data) {
			super.onActivityResult(requestCode, resultCode, data);
			if (requestCode == Constant.REQUEST_CODE_SINGLE_MESSAGE && resultCode == RESULT_OK
					&& data.getExtras() != null)
				for (Message m : arrayList) {
					if (m.getOrderid().equalsIgnoreCase(data.getStringExtra(Constant.EXTRA_ORDER_ID))) {
						arrayList.remove(m);
						adapter.notifyDataSetChanged();
						return;
					}
				}
		}
	}

	private class MyAdapter extends ArrayAdapter<Message> {

		public MyAdapter(Context context, int resource, List<Message> objects) {
			super(context, resource, objects);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final ViewHolder holder;

			if (convertView == null) {
				holder = new ViewHolder();
				LayoutInflater inflater = LayoutInflater.from(getContext());
				convertView = inflater.inflate(R.layout.row_upcoming_treatments, parent, false);

				holder.image = findView(convertView, R.id.iv_row_upcoming_treatments);
				holder.date = findView(convertView, R.id.tv_row_upcoming_treatments_date);
				holder.address = findView(convertView, R.id.tv_row_upcoming_treatments_address);
				holder.name = findView(convertView, R.id.tv_row_upcoming_treatments_customer_name);
				holder.treatment = findView(convertView, R.id.tv_row_upcoming_treatments_type_name);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.date.setText(TimeUtils.timestampToDateWithHour(getItem(position).getMessageSentTime()));
			holder.name.setText(getItem(position).getClientName());
			holder.address.setText(getItem(position).getClientAdress());
			holder.treatment.setText(TreatmentsFormatter.getSelf(getContext()).getTreatmentName(
					getItem(position).getTreatments()));

			ImageLoaderUtil.display(getItem(position).getImageUrl(), holder.image);

			return convertView;
		}

		private class ViewHolder {
			ImageView image;
			TextView date, name, address, treatment;
		}

	}

}
