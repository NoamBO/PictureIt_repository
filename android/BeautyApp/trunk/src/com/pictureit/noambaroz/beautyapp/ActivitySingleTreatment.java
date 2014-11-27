package com.pictureit.noambaroz.beautyapp;

import utilities.Dialogs;
import utilities.OutgoingCommunication;
import utilities.TimeUtils;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.pictureit.noambaroz.beautyapp.data.Constant;
import com.pictureit.noambaroz.beautyapp.data.UpcomingTreatment;
import com.pictureit.noambaroz.beautyapp.server.ImageLoaderUtil;

public class ActivitySingleTreatment extends ActivityWithFragment {

	@Override
	public void onBackPressed() {
		backPressed();
	}

	@Override
	protected void initActivity() {
	}

	@Override
	protected void setFragment() {
		UpcomingTreatment ut = null;

		Bundle b = getIntent().getExtras();
		if (!b.isEmpty()) {
			ut = b.getParcelable(Constant.EXTRA_UPCOMING_TREATMENT);
		}
		if (ut == null) {
			Dialogs.makeToastThatCloseActivity(ActivitySingleTreatment.this, R.string.dialog_title_error);
			return;
		}
		fragment = new FragmentSingleTreatment();
		((FragmentSingleTreatment) fragment).setModel(ut);
	}

	@Override
	protected void setFragmentTag() {
		FRAGMENT_TAG = "single_treatment_screen";
	}

	class FragmentSingleTreatment extends Fragment {

		UpcomingTreatment mUpcomingTreatment;

		private FrameLayout bCall;
		private FrameLayout bDelete;

		public void setModel(UpcomingTreatment upcomingTreatment) {
			mUpcomingTreatment = upcomingTreatment;
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

			View v = inflater.inflate(R.layout.fragment_upcoming_treatment, container, false);
			ImageView pic = findView(v, R.id.iv_base_beautician_row_pic);
			TextView name = findView(v, R.id.tv_base_beautician_row_name);
			TextView address = findView(v, R.id.tv_base_beautician_row_address);
			TextView raters = findView(v, R.id.tv_base_beautician_row_raters);
			RatingBar ratingBar = findView(v, R.id.rb_base_beautician_row_rating);
			TextView date = findView(v, R.id.tv_message_inner_treatment_date);
			TextView location = findView(v, R.id.tv_message_inner_treatment_location);
			TextView remarks = findView(v, R.id.tv_message_inner_remarks);
			TextView price = findView(v, R.id.tv_message_inner_price);
			TextView treatments1 = findView(v, R.id.tv_message_wanted_treatment1);
			TextView treatments2 = findView(v, R.id.tv_message_wanted_treatment2);
			bCall = findView(v, R.id.fl_fragment_upcoming_treatment_call);
			bDelete = findView(v, R.id.fl_fragment_upcoming_treatment_delete);

			name.setText(mUpcomingTreatment.getBeautician_name());
			address.setText(mUpcomingTreatment.getBeautician_address());
			raters.setText(BeauticianUtil.formatRaters(mUpcomingTreatment.getBeautician_raters(), getActivity()));
			ratingBar.setRating((float) mUpcomingTreatment.getBeautician_rate());
			date.setText(TimeUtils.timestampToDate(mUpcomingTreatment.getTreatment_date()));
			location.setText(mUpcomingTreatment.getTreatment_location());
			remarks.setText(mUpcomingTreatment.getBeautician_nots());
			price.setText(getString(R.string.price) + " " + mUpcomingTreatment.getPrice());
			ImageLoaderUtil.display(mUpcomingTreatment.getPic(), pic);
			BeauticianUtil.setTreatmentsList(getActivity(), treatments1, treatments2,
					mUpcomingTreatment.getTreatmentsArray());
			return v;
		}

		@Override
		public void onResume() {
			super.onResume();
			bCall.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					OutgoingCommunication.call(getActivity(), mUpcomingTreatment.getPhone());
				}
			});
			bDelete.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
				}
			});
		}

	}

}
