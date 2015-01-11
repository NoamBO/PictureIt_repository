package com.pictureit.noambaroz.beauticianapp;

import java.util.ArrayList;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.pictureit.noambaroz.beauticianapp.animation.AnimationManager;
import com.pictureit.noambaroz.beauticianapp.data.History;
import com.pictureit.noambaroz.beauticianapp.data.TimeUtils;
import com.pictureit.noambaroz.beauticianapp.data.Formatter;
import com.pictureit.noambaroz.beauticianapp.dialog.Dialogs;
import com.pictureit.noambaroz.beauticianapp.server.GetHistoryTask;
import com.pictureit.noambaroz.beauticianapp.server.HttpBase.HttpCallback;

public class ActivityHistory extends ActivityWithFragment {

	@Override
	protected void initActivity() {
	}

	@Override
	protected void setFragment() {
		fragment = new FragmentHistory();
	}

	@Override
	protected void setFragmentTag() {
		FRAGMENT_TAG = "treatments_history";
	}

	private class FragmentHistory extends Fragment implements HttpCallback {

		private ArrayList<History> mArrayList;
		private ListView mListView;
		private TextView mEmptyListIndicator;
		private HistoryAdapter mAdapter;

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			mArrayList = new ArrayList<History>();
			mAdapter = new HistoryAdapter();
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View v = inflater.inflate(R.layout.fragment_list_layout, container, false);
			mListView = findView(v, R.id.lv_fragment_list_listview);
			mEmptyListIndicator = findView(v, R.id.tv_fragment_list_empty_list_indicator);
			mEmptyListIndicator.setText(R.string.no_history);
			GetHistoryTask httpRequest = new GetHistoryTask(getActivity(), this);
			httpRequest.execute();
			return v;
		}

		@SuppressWarnings("unchecked")
		@Override
		public void onAnswerReturn(Object answer) {
			if (answer instanceof Integer || ((ArrayList<History>) answer) == null) {
				Dialogs.showServerFailedDialog(getActivity());
				return;
			}

			ArrayList<History> arr = (ArrayList<History>) answer;
			if (arr.size() == 0)
				onEmptyResults();
			else
				setList(arr);
		}

		private void onEmptyResults() {
			AnimationManager.fadeIn(getActivity(), mEmptyListIndicator);
		}

		private void setList(ArrayList<History> arr) {
			mArrayList.addAll(arr);
			mListView.setAdapter(mAdapter);
		}

		private class HistoryAdapter extends BaseAdapter {

			@Override
			public int getCount() {
				return mArrayList.size();
			}

			@Override
			public History getItem(int position) {
				return mArrayList.get(position);
			}

			@Override
			public long getItemId(int position) {
				return 0;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				final ViewHolder holder;
				if (convertView == null) {
					holder = new ViewHolder();
					convertView = LayoutInflater.from(getActivity()).inflate(R.layout.row_history, parent, false);
					holder.date = findView(convertView, R.id.tv_row_history_date);
					holder.name = findView(convertView, R.id.tv_row_history_client);
					holder.treatment = findView(convertView, R.id.tv_row_history_treatment);
					holder.address = findView(convertView, R.id.tv_row_history_address);
					holder.price = findView(convertView, R.id.tv_row_history_price);
					convertView.setTag(holder);
				} else {
					holder = (ViewHolder) convertView.getTag();
				}

				holder.date.setText(TimeUtils.timestampToDateWithHour(getItem(position).getDate()));
				holder.name.setText(getItem(position).getClientName());
				holder.treatment.setText(Formatter.getSelf(getActivity()).getTreatmentName(
						getItem(position).getTreatments()));
				holder.address.setText(getItem(position).getAddress());
				holder.price.setText(getItem(position).getPrice() + " " + getString(R.string.currency));
				return convertView;
			}
		}
	}

	private static class ViewHolder {
		TextView date;
		TextView name;
		TextView treatment;
		TextView address;
		TextView price;
	}

}
