package com.pictureit.noambaroz.beautyapp;

import utilities.BaseFragment;
import utilities.Log;
import utilities.server.HttpBase.HttpCallback;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.pictureit.noambaroz.beautyapp.data.TreatmentSummary;
import com.pictureit.noambaroz.beautyapp.data.TreatmentType;
import com.pictureit.noambaroz.beautyapp.helper.ServiceOrderManager;
import com.pictureit.noambaroz.beautyapp.server.PostOrderTreatment;

public class ServiceSummaryFragment extends BaseFragment {

	private TreatmentSummary mTreatment;
	private String beauticianId;
	private String beauticianName;

	public void setBeautician(String beauticianId, String beauticianName) {
		this.beauticianId = beauticianId;
		this.beauticianName = beauticianName;
	}

	public void setTreatment(TreatmentSummary treatment) {
		mTreatment = treatment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		if (mTreatment == null)
			return;
		super.onCreate(savedInstanceState);
	}

	private Button order;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.service_order_summary_layout, container, false);
		TextView tvFor = findView(v, R.id.tv_service_summary_for);
		TextView tvDate = findView(v, R.id.tv_service_summary_date);
		TextView tvLocation = findView(v, R.id.tv_service_summary_location);
		TextView tvRemarks = findView(v, R.id.tv_service_summary_remarks);
		TextView tvWantedTreatment1 = findView(v, R.id.tv_service_summary_wanted_treatment1);
		TextView tvWantedTreatment2 = findView(v, R.id.tv_service_summary_wanted_treatment2);
		order = findView(v, R.id.b_service_summary_order);

		tvFor.setText(mTreatment.forWho);
		tvDate.setText(mTreatment.when);
		tvLocation.setText(mTreatment.whare);
		setTreatmentsList(tvWantedTreatment1, tvWantedTreatment2);
		if (mTreatment.remarks != null && !mTreatment.remarks.equals(""))
			tvRemarks.setText(mTreatment.remarks);
		else
			tvRemarks.setVisibility(View.GONE);
		return v;
	}

	@Override
	public void onResume() {
		super.onResume();
		order.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				PostOrderTreatment httpPost = new PostOrderTreatment(getActivity(), new HttpCallback() {

					@Override
					public void onAnswerReturn(Object answer) {
						Log.i("finish");
						if (answer != null && !((String) answer).equalsIgnoreCase("")) {
							ServiceOrderManager.setPending(getActivity(), true);
							getActivity().finish();
						}
					}
				});
				try {
					httpPost.start(mTreatment.forWho, mTreatment.when, mTreatment.remarks, mTreatment.whare,
							mTreatment.tretments);
				} catch (Exception e) {
					Log.i("failed while building the request to the server");
					e.printStackTrace();
				}
			}
		});
	}

	private void setTreatmentsList(TextView tv1, TextView tv2) {
		StringBuilder sb1 = new StringBuilder();
		StringBuilder sb2 = new StringBuilder();
		int count = 0;
		for (TreatmentType t : mTreatment.tretments) {
			if (Integer.valueOf(t.getCount()) > 0) {
				if (count % 2 == 0)
					sb1.append(t.getName()).append("\n");
				else
					sb2.append(t.getName()).append("\n");
				count++;
			}
		}
		if (count < 2)
			tv2.setVisibility(View.GONE);
		tv1.setSingleLine(false);
		tv2.setSingleLine(false);
		tv1.setText(sb1.toString());
		tv2.setText(sb2.toString());
	}
}
