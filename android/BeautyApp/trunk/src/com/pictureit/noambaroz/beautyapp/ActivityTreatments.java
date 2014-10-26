package com.pictureit.noambaroz.beautyapp;

import java.util.ArrayList;
import java.util.List;

import utilities.Log;
import utilities.TimeUtils;
import utilities.server.HttpBase.HttpCallback;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.pictureit.noambaroz.beautyapp.data.Constant;
import com.pictureit.noambaroz.beautyapp.data.Treatments;
import com.pictureit.noambaroz.beautyapp.data.UpcomingTreatment;
import com.pictureit.noambaroz.beautyapp.server.GetUpcomingTreatments;

public class ActivityTreatments extends ActivityWithFragment {

	@Override
	public void onBackPressed() {
		backPressed();
	}

	@Override
	protected void initActivity() {
		fragment = new FragmentTreatments();
	}

	@Override
	protected void setFragment() {
		// TODO
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

	private class FragmentTreatments extends Fragment implements HttpCallback {

		private ListView mListView;
		private ProgressBar mProgressBar;
		private TextView mTextView;

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View v = inflater.inflate(R.layout.fragment_treatments, container, false);
			mListView = findView(v, R.id.lv_upcoming_treatments);
			mProgressBar = findView(v, R.id.pb_upcoming_treatments_loading_indicator);
			mTextView = findView(v, R.id.tv_upcoming_treatments_empty_list_indicator);
			return v;
		}

		@Override
		public void onViewCreated(View view, Bundle savedInstanceState) {
			GetUpcomingTreatments httpPost = new GetUpcomingTreatments(getActivity(), this);
			httpPost.execute();
			super.onViewCreated(view, savedInstanceState);
		}

		@SuppressWarnings("unchecked")
		@Override
		public void onAnswerReturn(Object answer) {
			ArrayList<UpcomingTreatment> arr = (ArrayList<UpcomingTreatment>) answer;
			Log.i("found " + arr.size() + " upcoming treatments");
			mProgressBar.setVisibility(View.GONE);
			if (arr == null || arr.size() == 0) {
				mTextView.setVisibility(View.VISIBLE);
				return;
			}

			UpcomingTreatmentsListViewAdapter mAdapter = new UpcomingTreatmentsListViewAdapter(getActivity(),
					android.R.layout.simple_list_item_2, arr);
			mListView.setAdapter(mAdapter);
		}

		@Override
		public void onResume() {
			super.onResume();
			mListView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					UpcomingTreatment t = (UpcomingTreatment) parent.getAdapter().getItem(position);
					Intent intent = new Intent(getActivity(), ActivitySingleTreatment.class);
					intent.putExtra(Constant.EXTRA_UPCOMING_TREATMENT, t);
					startActivity(intent);
					overridePendingTransition(R.anim.activity_enter_slidein_anim, R.anim.activity_exit_shrink_anim);
				}
			});
		}

	}

	private class UpcomingTreatmentsListViewAdapter extends ArrayAdapter<UpcomingTreatment> {

		public UpcomingTreatmentsListViewAdapter(Context context, int resource, List<UpcomingTreatment> objects) {
			super(context, resource, objects);
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
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.name.setText(t.getBeautician_name());
			holder.address.setText(t.getBeautician_address());
			holder.treatmentName.setText(getTreatmentName(t));
			holder.date.setText(TimeUtils.timestampToDate(t.getTreatment_date()));

			return convertView;
		}

		private String getTreatmentName(UpcomingTreatment t) {
			StringBuilder sb = new StringBuilder();
			sb.append(Treatments.getTreatmentType(getContext(), t.getTreatmentsArray().get(0).getTreatments_id())
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
	}

}
