package com.pictureit.noambaroz.beautyapp;

import java.util.ArrayList;
import java.util.List;

import utilities.Dialogs;
import utilities.TimeUtils;
import utilities.server.HttpBase.HttpCallback;
import android.app.ListFragment;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.pictureit.noambaroz.beautyapp.data.StringArrays;
import com.pictureit.noambaroz.beautyapp.data.UpcomingTreatment;
import com.pictureit.noambaroz.beautyapp.server.PostHistory;

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
		FRAGMENT_TAG = "FragmentHistory";
	}

	private class FragmentHistory extends ListFragment {

		@Override
		public void onViewCreated(View view, Bundle savedInstanceState) {
			super.onViewCreated(view, savedInstanceState);
			initListview();
			view.setBackgroundColor(getResources().getColor(R.color.app_most_common_yellow_color));
			PostHistory httpRequest = new PostHistory(getActivity(), new HttpCallback() {

				@SuppressWarnings("unchecked")
				@Override
				public void onAnswerReturn(Object answer) {
					ArrayList<UpcomingTreatment> arr = (ArrayList<UpcomingTreatment>) answer;
					if (answer == null)
						Dialogs.showServerFailedDialog(getActivity());
					else if (arr.size() == 0) {
						// TODO No History Indicator
					} else {
						HistoryListAdapter adapter = new HistoryListAdapter(getActivity(), R.layout.row_history, arr);
						setListAdapter(adapter);
					}
				}
			});
			httpRequest.execute();
		}

		private void initListview() {
			getListView().setPadding(15, 0, 15, 0);
			getListView().setDivider(new ColorDrawable(getResources().getColor(R.color.transparent)));
			getListView().setDividerHeight((int) getResources().getDimension(R.dimen.activity_horizontal_margin));
		}

	}

	private class HistoryListAdapter extends ArrayAdapter<UpcomingTreatment> {

		public HistoryListAdapter(Context context, int resource, List<UpcomingTreatment> objects) {
			super(context, resource, objects);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = getLayoutInflater().inflate(R.layout.row_history, parent, false);
				holder.bCall = findView(convertView, R.id.fl_row_history_call);
				holder.bRate = findView(convertView, R.id.fl_row_history_rate);
				holder.bReorder = findView(convertView, R.id.tv_row_history_reorder);
				holder.tvDate = findView(convertView, R.id.tv_row_history_date);
				holder.tvBeauticianName = findView(convertView, R.id.tv_row_history_beautician);
				holder.tvTreatments = findView(convertView, R.id.tv_row_history_treatment);
				holder.tvAddress = findView(convertView, R.id.tv_row_history_address);
				holder.tvPrice = findView(convertView, R.id.tv_row_history_price);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			// holder.bCall
			// holder.bRate
			// holder.bReorder
			holder.tvDate.setText(TimeUtils.timestampToDate(getItem(position).getTreatment_date()));
			holder.tvBeauticianName.setText(getItem(position).getBeautician_name());
			holder.tvTreatments.setText(getTreatmentName(getItem(position)));
			holder.tvAddress.setText(getItem(position).getTreatment_location());
			holder.tvPrice.setText(getItem(position).getPrice() + getString(R.string.currency));

			return convertView;
		}

		private String getTreatmentName(UpcomingTreatment t) {
			StringBuilder sb = new StringBuilder();
			sb.append(StringArrays.getTreatmentType(getContext(), t.getTreatmentsArray().get(0).getTreatments_id())
					.getName());
			if (t.getTreatmentsArray().size() > 1) {
				sb.append(" ");
				sb.append(getString(R.string.and_more));
				sb.append(" ");
				sb.append(t.getTreatmentsArray().size() - 1);
				sb.append(" ");
				sb.append(getString(R.string.additional));
			}
			return sb.toString();
		}

	}

	private static class ViewHolder {
		TextView tvDate;
		TextView tvBeauticianName;
		TextView tvTreatments;
		TextView tvAddress;
		TextView tvPrice;
		TextView bReorder;
		ViewGroup bRate;
		ViewGroup bCall;
	}

}
