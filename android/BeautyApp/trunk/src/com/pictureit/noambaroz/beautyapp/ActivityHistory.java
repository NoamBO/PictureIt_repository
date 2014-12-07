package com.pictureit.noambaroz.beautyapp;

import java.util.ArrayList;
import java.util.List;

import utilities.Dialogs;
import utilities.OutgoingCommunication;
import utilities.TimeUtils;
import utilities.server.HttpBase.HttpCallback;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.pictureit.noambaroz.beautyapp.animation.AnimationManager;
import com.pictureit.noambaroz.beautyapp.customdialogs.DialogRate;
import com.pictureit.noambaroz.beautyapp.data.HistoryObject;
import com.pictureit.noambaroz.beautyapp.data.ReorderObject;
import com.pictureit.noambaroz.beautyapp.data.StringArrays;
import com.pictureit.noambaroz.beautyapp.data.UpcomingTreatment;
import com.pictureit.noambaroz.beautyapp.server.GetOrderSummary;
import com.pictureit.noambaroz.beautyapp.server.PostHistory;
import com.pictureit.noambaroz.beautyapp.server.PostRate;

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

	private class FragmentHistory extends Fragment {

		private ListView mListView;
		private HistoryListAdapter mAdapter;

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View view = inflater.inflate(R.layout.fragment_history, container, false);
			mListView = findView(view, R.id.history_listView);
			return view;
		}

		@Override
		public void onViewCreated(final View view, Bundle savedInstanceState) {
			super.onViewCreated(view, savedInstanceState);
			initListview(mListView);
			PostHistory httpRequest = new PostHistory(getActivity(), new HttpCallback() {

				@SuppressWarnings("unchecked")
				@Override
				public void onAnswerReturn(Object answer) {
					ArrayList<HistoryObject> arr = (ArrayList<HistoryObject>) answer;
					AnimationManager.fadeIn(getActivity(), findView(view, R.id.vg_history_container));
					findView(view, R.id.pb_history_loading_indicator).setVisibility(View.GONE);
					if (answer == null)
						Dialogs.showServerFailedDialog(getActivity());
					else if (arr.size() == 0) {
						findView(view, R.id.vg_history_empty_list_indicator).setVisibility(View.VISIBLE);
					} else {
						mAdapter = new HistoryListAdapter(getActivity(), R.layout.row_history, arr);
						mListView.setAdapter(mAdapter);
					}
				}
			});
			httpRequest.execute();
		}

		private void initListview(ListView listView) {
			listView.setPadding(15, 0, 15, 0);
			listView.setDivider(new ColorDrawable(getResources().getColor(R.color.transparent)));
			listView.setDividerHeight((int) getResources().getDimension(R.dimen.activity_horizontal_margin));
		}

	}

	private class HistoryListAdapter extends ArrayAdapter<HistoryObject> {

		public HistoryListAdapter(Context context, int resource, List<HistoryObject> objects) {
			super(context, resource, objects);
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
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

			holder.tvDate.setText(TimeUtils.timestampToDate(getItem(position).getTreatment_date()));
			holder.tvBeauticianName.setText(getItem(position).getBeautician_name());
			holder.tvTreatments.setText(getTreatmentName(getItem(position)));
			holder.tvAddress.setText(getItem(position).getTreatment_location());
			holder.tvPrice.setText(getItem(position).getPrice() + getString(R.string.currency));

			holder.bCall.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					OutgoingCommunication.call((Activity) getContext(), getItem(position).getPhone());
				}
			});
			holder.bRate.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					rate(v, getItem(position));
				}
			});
			holder.bReorder.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					onReorder(getItem(position).getBeautician_nots());
				}
			});
			return convertView;
		}

		protected void onReorder(String string) {
			GetOrderSummary httpRequest = new GetOrderSummary(getContext(), new HttpCallback() {

				@Override
				public void onAnswerReturn(Object answer) {
					if (answer == null) {
						Dialogs.showServerFailedDialog(getContext());
					} else {
						ReorderObject o = (ReorderObject) answer;
						FragmentReorder fragment = new FragmentReorder(ActivityHistory.this, o.getForWho(),
								o.getWhen(), o.getWhare(), o.getRemarks(), o.getTretments());
						ActivityHistory.this.getFragmentManager().beginTransaction().add(FRAGMENT_CONTAINER, fragment)
								.addToBackStack(null).commit();
					}
				}
			}, "10");
			httpRequest.execute();
		}

		protected void rate(View v, final UpcomingTreatment t) {
			new DialogRate(getContext()).setOkButton(t.getBeautician_name(), new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int rating) {
					PostRate httpRequest = new PostRate(getContext(), new HttpCallback() {

						@Override
						public void onAnswerReturn(Object answer) {
							if ((Boolean) answer) {

							} else {
								Dialogs.showServerFailedDialog(getContext());
							}
						}
					}, t.getBeautician_id(), rating);
					httpRequest.execute();
				}
			}).setCancelButton(null).show(v);
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
