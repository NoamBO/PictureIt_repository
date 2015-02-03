package com.pictureit.noambaroz.beautyapp;

import java.util.ArrayList;
import java.util.List;

import utilities.Dialogs;
import utilities.TimeUtils;
import utilities.server.HttpBase.HttpCallback;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.pictureit.noambaroz.beautyapp.data.Constants;
import com.pictureit.noambaroz.beautyapp.data.StringArrays;
import com.pictureit.noambaroz.beautyapp.data.UpcomingTreatment;
import com.pictureit.noambaroz.beautyapp.server.GetUpcomingTreatments;
import com.pictureit.noambaroz.beautyapp.server.PostRemoveCanceledTreatment;

public class ActivityTreatments extends ActivityWithFragment {

	public static int RESULT_CODE_TREATMENT_CANCELED = 052605;

	public static boolean isRunning = false;

	@Override
	protected void onStart() {
		isRunning = true;
		super.onStart();
	}

	@Override
	protected void onStop() {
		isRunning = false;
		super.onStop();
	}

	@Override
	public void onBackPressed() {
		backPressed();
	}

	@Override
	protected void initActivity() {
	}

	@Override
	protected void setFragment() {
		fragment = new FragmentTreatments();
	}

	@Override
	protected void setFragmentTag() {
		FRAGMENT_TAG = "notification_fragment";
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_activity_treatments, menu);
		final MenuItem badgeItem = menu.findItem(R.id.action_ask_for_service);
		ViewGroup badgeLayout = (ViewGroup) badgeItem.getActionView();
		ImageView iv = findView(badgeLayout, R.id.action_layout_order_treatment_button);
		iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onOptionsItemSelected(badgeItem);
			}
		});
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

	private class FragmentTreatments extends Fragment implements HttpCallback {

		private ListView mListView;
		private ProgressBar mProgressBar;
		private LinearLayout mNoTreatmentsIndicator;
		private UpcomingTreatment mTempTreatment;

		private BroadcastReceiver mReceiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				new GetUpcomingTreatments(getActivity(), FragmentTreatments.this).execute();
			}
		};

		public FragmentTreatments() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View v = inflater.inflate(R.layout.fragment_treatments, container, false);
			mListView = findView(v, R.id.lv_upcoming_treatments);
			mProgressBar = findView(v, R.id.pb_upcoming_treatments_loading_indicator);
			mNoTreatmentsIndicator = findView(v, R.id.vg_upcoming_treatments_empty_list_indicator);
			return v;
		}

		@Override
		public void onViewCreated(View view, Bundle savedInstanceState) {
			GetUpcomingTreatments httpPost = new GetUpcomingTreatments(getActivity(), this);
			httpPost.execute();
			registerReceiver(mReceiver, new IntentFilter(Constants.INTENT_FILTER_UPCOMING_TREATMENTS));
			super.onViewCreated(view, savedInstanceState);
		}

		@Override
		public void onDestroy() {
			unregisterReceiver(mReceiver);
			Intent i = new Intent(getActivity(), MainActivity.class);
			i.putExtra("exit", true);
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(i);
			super.onDestroy();
		}

		@SuppressWarnings("unchecked")
		@Override
		public void onAnswerReturn(Object answer) {
			ArrayList<UpcomingTreatment> arr = (ArrayList<UpcomingTreatment>) answer;
			mProgressBar.setVisibility(View.GONE);

			UpcomingTreatmentsListViewAdapter mAdapter = new UpcomingTreatmentsListViewAdapter(getActivity(),
					android.R.layout.simple_list_item_2, arr);
			mListView.setAdapter(mAdapter);
			mListView.setEmptyView(mNoTreatmentsIndicator);
		}

		@Override
		public void onResume() {
			super.onResume();
			mListView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					if (((UpcomingTreatmentsListViewAdapter) parent.getAdapter()).isItemClickable(position)) {
						UpcomingTreatment t = (UpcomingTreatment) parent.getAdapter().getItem(position);
						Intent intent = new Intent(getActivity(), ActivitySingleTreatment.class);
						intent.putExtra(Constants.EXTRA_UPCOMING_TREATMENT, t);
						mTempTreatment = t;
						startActivityForResult(intent, 0);
						overridePendingTransition(R.anim.activity_enter_slidein_anim, R.anim.activity_exit_shrink_anim);
					}
				}
			});
		}

		@Override
		public void onActivityResult(int requestCode, int resultCode, Intent data) {
			super.onActivityResult(requestCode, resultCode, data);
			if (requestCode == 0) {
				if (resultCode == RESULT_CODE_TREATMENT_CANCELED && mTempTreatment != null) {
					((UpcomingTreatmentsListViewAdapter) mListView.getAdapter()).remove(mTempTreatment);
					((UpcomingTreatmentsListViewAdapter) mListView.getAdapter()).notifyDataSetChanged();
				}
			}
			mTempTreatment = null;
		}

	}

	private class UpcomingTreatmentsListViewAdapter extends ArrayAdapter<UpcomingTreatment> {

		public UpcomingTreatmentsListViewAdapter(Context context, int resource, List<UpcomingTreatment> objects) {
			super(context, resource, objects);
		}

		public boolean isItemClickable(int position) {
			return !getItem(position).isTreatmentCanceled();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final ViewHolder holder;
			UpcomingTreatment t = getItem(position);
			if (convertView == null) {
				holder = new ViewHolder();
				LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(
						Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.row_treatment, parent, false);
				holder.name = findView(convertView, R.id.tv_row_upcoming_treatment_beautician_name);
				holder.address = findView(convertView, R.id.tv_row_upcoming_treatment_address);
				holder.date = findView(convertView, R.id.tv_row_upcoming_treatment_date);
				holder.treatmentName = findView(convertView, R.id.tv_row_upcoming_treatment_name);

				holder.disableContainer = findView(convertView, R.id.fl_row_upcoming_treatments_disabled_container);
				holder.statusDisableTitle = findView(convertView,
						R.id.tv_row_upcoming_treatments_disabled_container_title);
				holder.statusDisableRemove = findView(convertView,
						R.id.tv_row_upcoming_treatments_disabled_container_remove);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.name.setText(t.getBeautician_name());
			holder.address.setText(t.getBeautician_address());
			holder.treatmentName.setText(getTreatmentName(t));
			holder.date.setText(TimeUtils.timestampToDate(t.getTreatment_date()));

			if (getItem(position).isTreatmentCanceled()) {
				holder.disableContainer.setVisibility(View.VISIBLE);
				holder.statusDisableRemove.setTag(position);
				holder.statusDisableRemove.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						final int position = (Integer) v.getTag();
						new PostRemoveCanceledTreatment(getContext(), new HttpCallback() {

							@Override
							public void onAnswerReturn(Object answer) {
								if (answer instanceof Integer)
									Dialogs.showServerFailedDialog(getContext());
								else
									remove(getItem(position));
							}
						}, getItem(position).getUpcomingTreatmentId()).execute();
					}
				});
			} else {
				holder.disableContainer.setVisibility(View.GONE);
				holder.statusDisableRemove.setOnClickListener(null);
			}

			return convertView;
		}

		private String getTreatmentName(UpcomingTreatment t) {
			if (t.getTreatmentsArray() == null || t.getTreatmentsArray().size() == 0)
				return "";
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

	public static class ViewHolder {
		TextView name;
		TextView address;
		TextView date;
		TextView treatmentName;
		ViewGroup disableContainer;
		TextView statusDisableTitle, statusDisableRemove;
	}

}
