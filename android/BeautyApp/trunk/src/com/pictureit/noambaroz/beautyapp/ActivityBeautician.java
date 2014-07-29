package com.pictureit.noambaroz.beautyapp;

import utilities.BaseFragment;
import utilities.server.HttpBase.HttpCallback;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.pictureit.noambaroz.beautyapp.data.Beautician;
import com.pictureit.noambaroz.beautyapp.data.Constant;
import com.pictureit.noambaroz.beautyapp.server.GetBeauticianById;

public class ActivityBeautician extends ActivityWithFragment {

	@Override
	public void onBackPressed() {
		backPressed();
	}

	@Override
	protected void initActivity() {
	}

	@Override
	protected void setFragment() {
		fragment = new FragmentBeautician();
	}

	@Override
	protected void setFragmentTag() {
		FRAGMENT_TAG = "single_beautician_screen";
	}

	private class FragmentBeautician extends BaseFragment {

		private String beautycianId;
		private Beautician mBeautician;

		private ImageView ivPic;
		private Button bOrder;
		private TextView tvName;
		private TextView tvAddress;
		private TextView tvRaters;
		private RatingBar rbRating;
		private Button bRate;
		private TextView tvDegrees;
		private TextView tvDescription;
		private TextView tvTreatment1;
		private TextView tvTreatment2;

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			if (getActivity().getIntent().getExtras() != null)
				beautycianId = getActivity().getIntent().getExtras().getString(Constant.EXTRA_BEAUTICIAN_ID, "");
			else
				return;
			if (beautycianId.equalsIgnoreCase(""))
				mBeautician = getActivity().getIntent().getExtras().getParcelable(Constant.EXTRA_BEAUTICIAN_OBJECT);
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

			if (beautycianId.equalsIgnoreCase("") && mBeautician == null) {
				getFragmentManager().popBackStack();
				getActivity().finish();
				return super.onCreateView(inflater, container, savedInstanceState);
			}
			View v = inflater.inflate(R.layout.fragment_beautician, container, false);

			ivPic = findView(v, R.id.iv_beautician_page_image);
			bOrder = findView(v, R.id.b_beautician_page_order);
			tvName = findView(v, R.id.tv_beautician_page_name);
			tvAddress = findView(v, R.id.tv_beautician_page_address);
			tvRaters = findView(v, R.id.tv_beautician_page_raters);
			rbRating = findView(v, R.id.rb_beautician_page_rating);
			bRate = findView(v, R.id.b_beautician_page_rate);
			tvDegrees = findView(v, R.id.tv_beautician_page_degrees);
			tvDescription = findView(v, R.id.tv_beautician_page_description);
			tvTreatment1 = findView(v, R.id.tv_beautician_page_treatments1);
			tvTreatment2 = findView(v, R.id.tv_beautician_page_treatments2);

			if (mBeautician == null) {
				GetBeauticianById httpGet = new GetBeauticianById(getActivity(), new HttpCallback() {

					@Override
					public void onAnswerReturn(Object answer) {
						if (answer != null) {
							mBeautician = (Beautician) answer;
							initPage();
						} else {
							// TODO error dialog
						}
					}
				});
			} else {
				initPage();
			}

			return v;
		}

		protected void initPage() {
			imageLoader.displayImage(mBeautician.getPhoto(), ivPic, options);
			tvName.setText(mBeautician.getName());
			tvAddress.setText(BeauticianUtil.formatAddress(mBeautician.getAddress()));
			tvRaters.setText(BeauticianUtil.formatRaters(mBeautician.getRating().getRaters(), getActivity()));
			rbRating.setRating((float) mBeautician.getRating().getRate());
			rbRating.setOnTouchListener(new OnTouchListener() {
				public boolean onTouch(View v, MotionEvent event) {
					return true;
				}
			});
			tvDegrees.setText(BeauticianUtil.formatDegrees(mBeautician.getDegrees()));
			tvDescription.setText(mBeautician.getDescription());
			String[] treatmentsList = BeauticianUtil.formatTreatmentsWith__(getActivity(), mBeautician.getTreatments())
					.split("__");
			tvTreatment1.setText(treatmentsList[0]);
			if (treatmentsList.length > 1)
				tvTreatment2.setText(treatmentsList[1]);
			else
				tvTreatment2.setVisibility(View.GONE);
		}
	}

}
