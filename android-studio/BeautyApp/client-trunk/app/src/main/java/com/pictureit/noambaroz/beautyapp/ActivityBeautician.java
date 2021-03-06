package com.pictureit.noambaroz.beautyapp;

import utilities.BaseFragment;
import utilities.Dialogs;
import utilities.server.HttpBase.HttpCallback;
import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.pictureit.noambaroz.beautyapp.R.anim;
import com.pictureit.noambaroz.beautyapp.data.Beautician;
import com.pictureit.noambaroz.beautyapp.data.Constants;
import com.pictureit.noambaroz.beautyapp.server.GetBeauticianById;
import com.pictureit.noambaroz.beautyapp.server.ImageLoaderUtil;

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
	protected void initActionBar(ActionBar actionBar) {
		super.initActionBar(actionBar);
		actionBar.setTitle("");
	}

	@Override
	protected void setFragmentTag() {
		FRAGMENT_TAG = "single_beautician_screen";
	}

	public static class FragmentBeautician extends BaseFragment {

		private String beauticianId;
		private Beautician mBeautician;

		private ImageView ivPic;
		private ViewGroup bOrder;
		private TextView tvName;
		private TextView tvAddress;
		private TextView tvRaters;
		private RatingBar rbRating;
		private TextView tvExtendedDescription;
		private TextView tvDiplomas;
		private TextView tvTreatments;
		private TextView tvClassification;
		private TextView tvMobility;

		public FragmentBeautician() {
		}

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			Bundle extras = getActivity().getIntent().getExtras();

			// extras must contain Beautician or BeauticianID
            if (extras == null)
				return;
			if (extras.containsKey(Constants.EXTRA_BEAUTICIAN_ID))
				beauticianId = extras.getString(Constants.EXTRA_BEAUTICIAN_ID, "");

			if (beauticianId == null || beauticianId.equalsIgnoreCase(""))
				mBeautician = getActivity().getIntent().getExtras().getParcelable(Constants.EXTRA_BEAUTICIAN_OBJECT);
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

			if (beauticianId != null && beauticianId.equalsIgnoreCase("") && mBeautician == null) {
				getFragmentManager().popBackStack();
				Dialogs.showErrorDialog(getActivity(), R.string.cant_load_beautician);
				return super.onCreateView(inflater, container, savedInstanceState);
			}
			View v = inflater.inflate(R.layout.fragment_beautician, container, false);

			ivPic = findView(v, R.id.iv_beautician_page_image);
			bOrder = findView(v, R.id.vg_beautician_page_order);
			tvName = findView(v, R.id.tv_beautician_page_name);
			tvAddress = findView(v, R.id.tv_beautician_page_address);
			tvRaters = findView(v, R.id.tv_beautician_page_raters);
			rbRating = findView(v, R.id.rb_beautician_page_rating);
			tvExtendedDescription = findView(v, R.id.tv_beautician_page_description);
			tvDiplomas = findView(v, R.id.tv_beautician_page_diplomas);
			tvTreatments = findView(v, R.id.tv_beautician_page_treatments1);
			tvClassification = findView(v, R.id.tv_beautician_page_classification);
			tvMobility = findView(v, R.id.tv_beautician_page_mobility);

			if (mBeautician == null) {
				GetBeauticianById httpGet = new GetBeauticianById(getActivity(), new HttpCallback() {

					@Override
					public void onAnswerReturn(Object answer) {
						if (answer != null) {
							mBeautician = (Beautician) answer;
							initPage();
						} else {
							Dialogs.showErrorDialog(getActivity(), R.string.cant_load_beautician);
						}
					}
				}, beauticianId);
				httpGet.execute();
			} else {
				initPage();
			}

			return v;
		}

		protected void initPage() {
			ImageLoaderUtil.display(mBeautician.getPhoto(), ivPic, getActivity());
			tvName.setText(mBeautician.getName());
			tvAddress.setText(BeauticianUtil.formatAddress(getActivity(), mBeautician.getAddress()));
			tvClassification.setText(BeauticianUtil.getClassificationTypeById(getActivity(),
					mBeautician.getClassificationId()).getTitle());
			int raters = mBeautician.getRating() == null ? 0 : mBeautician.getRating().getRaters();
			tvRaters.setText(BeauticianUtil.formatRaters(raters, getActivity()));
			float rating = (float) (mBeautician.getRating() == null ? 0 : mBeautician.getRating().getRate());
			rbRating.setRating(rating);
			rbRating.setOnTouchListener(new OnTouchListener() {
				public boolean onTouch(View v, MotionEvent event) {
					return true;
				}
			});
			tvExtendedDescription.setText(mBeautician.getDescription().length() == 0 ? getActivity().getString(
					R.string.no_beautician_description) : mBeautician.getDescription());
			tvDiplomas.setText(BeauticianUtil.formatDegrees(getActivity(), mBeautician.getDiplomas()));
			String treatmentsList = BeauticianUtil.formatTreatments(getActivity(), mBeautician.getTreatments());
			tvTreatments.setText(treatmentsList);
			writeMobilityOption();
			bOrder.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(getActivity(), ServiceOrder.class);
					intent.putExtra(Constants.EXTRA_BEAUTICIAN_ID, mBeautician.getId());
					intent.putExtra(Constants.EXTRA_BEAUTICIAN_NAME, mBeautician.getName());
					intent.putExtra(Constants.EXTRA_BEAUTICIAN_TREATMENT_STRING_ARRAY, mBeautician.getTreatments());
                    getActivity().startActivity(intent);
					getActivity().overridePendingTransition(anim.activity_enter_slidein_anim, anim.activity_exit_shrink_anim);
				}
			});
		}

		private void writeMobilityOption() {
			StringBuilder mobility = new StringBuilder();
			mobility.append(getString(R.string.arrival_at_the_customers_home));
			mobility.append(" ");
			mobility.append(mBeautician.isMobility() ? getString(R.string.yes) : getString(R.string.no));
			tvMobility.setText(mobility.toString());
		}
	}

}
