package com.pictureit.noambaroz.beauticianapp;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.pictureit.noambaroz.beauticianapp.ActivityUpcomingTreatments.OnTreatmentCanceledListener;
import com.pictureit.noambaroz.beauticianapp.animation.AnimationManager;
import com.pictureit.noambaroz.beauticianapp.data.TimeUtils;
import com.pictureit.noambaroz.beauticianapp.data.TreatmentsFormatter;
import com.pictureit.noambaroz.beauticianapp.data.UpcomingTreatment;
import com.pictureit.noambaroz.beauticianapp.dialog.Dialogs;
import com.pictureit.noambaroz.beauticianapp.server.GetUpcomingTreatments;
import com.pictureit.noambaroz.beauticianapp.server.HttpBase.HttpCallback;
import com.pictureit.noambaroz.beauticianapp.server.ImageLoaderUtil;
import com.pictureit.noambaroz.beautycianapp.R;

public class FragmentUnAvailable extends BaseFragment implements HttpCallback, OnItemClickListener {

	private TextView mEmptyListIndicator;
	private ListView mListView;
	private MyAdapter mAdapter;
	ArrayList<UpcomingTreatment> mArrayList;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_unavailable, container, false);
		mEmptyListIndicator = findView(view, R.id.tv_unavailable_status_upcoming_treatments_empty_list_indicator);
		mListView = findView(view, R.id.lv_unavailable_status_upcoming_treatments);
		if (mArrayList == null) {
			GetUpcomingTreatments httpRequest = new GetUpcomingTreatments(getActivity(), this);
			httpRequest.execute();
		} else
			setListView();
		return view;
	}

	@Override
	public void onAnswerReturn(Object answer) {
		if (answer instanceof Integer) {
			Dialogs.showServerFailedDialog(getActivity());
			return;
		}

		mArrayList = (ArrayList<UpcomingTreatment>) answer;
		if (mArrayList.size() == 0)
			AnimationManager.fadeIn(getActivity(), mEmptyListIndicator);
		else
			setListView();
	}

	private void setListView() {
		if (mAdapter == null)
			mAdapter = new MyAdapter();
		mListView.setAdapter(mAdapter);
		mListView.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_MOVE) {
					return true; // Indicates that this has been handled by you
									// and will not be forwarded further.
				}
				return false;
			}
		});
		mListView.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		ActivityUpcomingTreatments.FragmentUpcomingTreatment fragment = new ActivityUpcomingTreatments.FragmentUpcomingTreatment();
		fragment.setUpcomingTreatment(mAdapter.getItem(position));
		fragment.setOnTreatmentCanceledListener(new OnTreatmentCanceledListener() {

			@Override
			public void onTreatmentCanceled(UpcomingTreatment treatment) {
				if (treatment != null) {
					mArrayList.remove(treatment);
					mAdapter.notifyDataSetChanged();
					if (mArrayList.size() == 0)
						mEmptyListIndicator.setVisibility(View.VISIBLE);
				}
			}
		});
		getFragmentManager().beginTransaction()
				.replace(R.id.main_screen_fragment_container, fragment, "upcoming_fragment").addToBackStack(null)
				.commit();
	}

	private class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return (mArrayList.size() > 2 ? 3 : mArrayList.size());
		}

		@Override
		public UpcomingTreatment getItem(int position) {
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
				LayoutInflater inflater = getActivity().getLayoutInflater();
				convertView = inflater.inflate(R.layout.row_unavailable_upcoming_treatment, parent, false);
				convertView.getLayoutParams().height = parent.getHeight() / 3;
				convertView.requestLayout();
				holder.image = findView(convertView, R.id.iv_row_unavailable_upcoming_treatment_image);
				holder.date = findView(convertView, R.id.tv_row_unavailable_upcoming_treatment_date);
				holder.name = findView(convertView, R.id.tv_row_unavailable_upcoming_treatment_customer_name);
				holder.treatment = findView(convertView, R.id.tv_row_unavailable_upcoming_treatment_treatment_name);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.date.setText(TimeUtils.timestampToDate(getItem(position).getTreatmentDate()));
			holder.name.setText(getItem(position).getClientName());
			holder.treatment.setText(TreatmentsFormatter.getSelf(getActivity()).getTreatmentName(
					getItem(position).getTreatments()));

			ImageLoaderUtil.display(getItem(position).getImageUrl(), holder.image);

			return convertView;
		}

	}

	private static class ViewHolder {
		ImageView image;
		TextView name;
		TextView date;
		TextView treatment;
	}

}
