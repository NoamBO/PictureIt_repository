package com.pictureit.noambaroz.beauticianapp;

import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.pictureit.noambaroz.beauticianapp.animation.AnimationManager;
import com.pictureit.noambaroz.beauticianapp.data.Formatter;
import com.pictureit.noambaroz.beauticianapp.data.TimeUtils;
import com.pictureit.noambaroz.beauticianapp.data.UpcomingTreatment;
import com.pictureit.noambaroz.beauticianapp.dialog.Dialogs;
import com.pictureit.noambaroz.beauticianapp.dialog.MySingleChoiseDialog;
import com.pictureit.noambaroz.beauticianapp.server.DeleteUpcomingTreatmentTask;
import com.pictureit.noambaroz.beauticianapp.server.GetUpcomingTreatments;
import com.pictureit.noambaroz.beauticianapp.server.HttpBase.HttpCallback;
import com.pictureit.noambaroz.beauticianapp.server.ImageLoaderUtil;
import com.pictureit.noambaroz.beauticianapp.server.RemoveUpcomingTreatmentFromListTask;
import com.pictureit.noambaroz.beauticianapp.utilities.OnFragmentDetachListener;
import com.pictureit.noambaroz.beauticianapp.utilities.OutgoingCommunication;

public class ActivityUpcomingTreatments extends ActivityWithFragment {

	private AdapterUpcomingTreatments mAdapter;

	@Override
	protected void initActivity() {
	}

	@Override
	protected void setFragment() {
		fragment = new UpcomingTreatmentsFragment();
	}

	@Override
	protected void setFragmentTag() {
		FRAGMENT_TAG = "upcoming_treatments";
	}

	@Override
	public void onBackPressed() {
		backPressed();
	}

	public interface OnTreatmentCanceledListener {
		public void onTreatmentCanceled(UpcomingTreatment treatment);
	}

	private class UpcomingTreatmentsFragment extends Fragment implements HttpCallback, OnItemClickListener,
			OnTreatmentCanceledListener {

		private ListView mListView;
		private TextView mEmptyListIndicator;

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View view = inflater.inflate(R.layout.fragment_list_layout, container, false);
			mListView = findView(view, R.id.lv_fragment_list_listview);
			mEmptyListIndicator = findView(view, R.id.tv_fragment_list_empty_list_indicator);
			mEmptyListIndicator.setText(R.string.no_treatments);
			return view;
		}

		@Override
		public void onViewCreated(View view, Bundle savedInstanceState) {
			if (mAdapter == null)
				new GetUpcomingTreatments(getActivity(), this).execute();
			else
				setListView();
		}

		@Override
		@SuppressWarnings("unchecked")
		public void onAnswerReturn(Object answer) {

			if (answer instanceof Integer) {
				Dialogs.showServerFailedDialog(getActivity());
				return;
			}

			ArrayList<UpcomingTreatment> arrayList = (ArrayList<UpcomingTreatment>) answer;
			if (arrayList.size() == 0)
				AnimationManager.fadeIn(getActivity(), mEmptyListIndicator);
			else {
				mAdapter = new AdapterUpcomingTreatments(getActivity(), R.layout.actionbar_title_view, arrayList);
				setListView();
			}
		}

		private void setListView() {
			mListView.setAdapter(mAdapter);
			mListView.setOnItemClickListener(this);
		}

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			if (!mAdapter.isClickEnable(position))
				return;

			FragmentUpcomingTreatment fragment = new FragmentUpcomingTreatment();
			fragment.setOnTreatmentCanceledListener(this);
			fragment.setUpcomingTreatment(mAdapter.getItem(position));

			getFragmentManager().beginTransaction().replace(FRAGMENT_CONTAINER, fragment, "single_treatment_screen")
					.addToBackStack(null).commit();
		}

		@Override
		public void onTreatmentCanceled(UpcomingTreatment treatment) {
			mAdapter.remove(treatment);
			mAdapter.notifyDataSetChanged();
		}
	}

	public static class FragmentUpcomingTreatment extends BaseFragment {

		private OnTreatmentCanceledListener onTreatmentCanceledListener;

		private OnFragmentDetachListener mDetachListener;

		private UpcomingTreatment mUpcomingTreatment;

		private ViewGroup bCall, bDelete;

		private MySingleChoiseDialog mSingleChoiseDialog;

		public void setOnTreatmentCanceledListener(OnTreatmentCanceledListener onTreatmentCanceledListener) {
			this.onTreatmentCanceledListener = onTreatmentCanceledListener;
		}

		public void setUpcomingTreatment(UpcomingTreatment upcomingTreatment) {
			mUpcomingTreatment = upcomingTreatment;
		}

		public void setOnDetachListener(OnFragmentDetachListener l) {
			mDetachListener = l;
		}

		@Override
		public void onDetach() {
			if (mDetachListener != null)
				mDetachListener.onDetach();
			super.onDetach();
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View v = inflater.inflate(R.layout.fragment_upcoming_treatment, container, false);
			bCall = findView(v, R.id.fl_upcoming_treatment_call);
			bDelete = findView(v, R.id.fl_upcoming_treatment_delete);

			TextView tvName, tvAddress, tvDate, tvTreatment1, tvTreatment2, tvLocation, tvRemarks, tvPrice;
			ImageView image;
			tvName = findView(v, R.id.tv_upcoming_treatment_name);
			tvAddress = findView(v, R.id.tv_upcoming_treatment_address);
			tvDate = findView(v, R.id.tv_upcoming_treatment_date);
			tvTreatment1 = findView(v, R.id.tv_upcoming_treatment_wanted_treatment1);
			tvTreatment2 = findView(v, R.id.tv_upcoming_treatment_wanted_treatment2);
			tvLocation = findView(v, R.id.tv_upcoming_treatment_location);
			tvRemarks = findView(v, R.id.tv_upcoming_treatment_remarks);
			tvPrice = findView(v, R.id.tv_upcoming_treatment_price);
			image = findView(v, R.id.iv_upcoming_treatment);

			tvName.setText(mUpcomingTreatment.getClientName());
			tvAddress.setText(mUpcomingTreatment.getClientAddress());
			tvDate.setText(TimeUtils.timestampToDateWithHour(mUpcomingTreatment.getTreatmentDate()));
			tvLocation.setText(mUpcomingTreatment.getClientAddress());
			tvRemarks.setText(mUpcomingTreatment.getClientComments());
			tvPrice.setText(getString(R.string.price) + " " + mUpcomingTreatment.getPrice() + " "
					+ getActivity().getString(R.string.currency));

			Formatter.getSelf(getActivity()).setTreatmentsList(tvTreatment1, tvTreatment2,
					mUpcomingTreatment.getTreatments());
			ImageLoaderUtil.display(mUpcomingTreatment.getImageUrl(), image);

			return v;
		}

		@Override
		public void onResume() {
			super.onResume();
			bCall.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					OutgoingCommunication.call(getActivity(), mUpcomingTreatment.getClientPhone());
				}
			});
			bDelete.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					onDeletePressed();
				}
			});
		}

		private void onDeletePressed() {
			if (mSingleChoiseDialog == null) {
				mSingleChoiseDialog = new MySingleChoiseDialog(getActivity(), getActivity().getResources()
						.getStringArray(R.array.cancel_request_dialog_list))
						.setMyTitle(R.string.cause_to_cancel_treatment)
						.setSubTitle(R.string.canceling_time_after_time_will_cause_your_account_to_be_closed)
						.showButtons(new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int cancelTreatmentReasonId) {
								if (cancelTreatmentReasonId != -1)
									doCancel(cancelTreatmentReasonId);
							}
						}, null);
			}
			mSingleChoiseDialog.show();
		}

		protected void doCancel(int cancelTreatmentReasonId) {
			DeleteUpcomingTreatmentTask httpRequest = new DeleteUpcomingTreatmentTask(getActivity(),
					new HttpCallback() {

						@Override
						public void onAnswerReturn(Object answer) {
							if (answer instanceof Integer)
								onTreatmentCanceled();
							else
								Dialogs.showServerFailedDialog(getActivity());
						}
					}, mUpcomingTreatment.getUpcomingtreatmentId(), cancelTreatmentReasonId);
			httpRequest.execute();
		}

		private void onTreatmentCanceled() {
			if (onTreatmentCanceledListener != null)
				onTreatmentCanceledListener.onTreatmentCanceled(mUpcomingTreatment);
			getFragmentManager().popBackStack();
		}
	}

	private class AdapterUpcomingTreatments extends ArrayAdapter<UpcomingTreatment> {

		public AdapterUpcomingTreatments(Context context, int resource, List<UpcomingTreatment> objects) {
			super(context, resource, objects);
		}

		public boolean isClickEnable(int itemPosition) {
			return !getItem(itemPosition).isTreatmentCanceled();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final ViewHolder holder;

			if (convertView == null) {
				holder = new ViewHolder();
				LayoutInflater inflater = LayoutInflater.from(getContext());
				convertView = inflater.inflate(R.layout.row_upcoming_treatments, parent, false);

				holder.image = findView(convertView, R.id.iv_row_upcoming_treatments);
				holder.date = findView(convertView, R.id.tv_row_upcoming_treatments_date);
				holder.address = findView(convertView, R.id.tv_row_upcoming_treatments_address);
				holder.name = findView(convertView, R.id.tv_row_upcoming_treatments_customer_name);
				holder.treatment = findView(convertView, R.id.tv_row_upcoming_treatments_type_name);

				holder.disableContainer = findView(convertView, R.id.fl_row_upcoming_treatments_disabled_container);
				holder.statusDisableTitle = findView(convertView,
						R.id.tv_row_upcoming_treatments_disabled_container_title);
				holder.statusDisableRemove = findView(convertView,
						R.id.tv_row_upcoming_treatments_disabled_container_remove);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.date.setText(TimeUtils.timestampToDateWithHour(getItem(position).getTreatmentDate()));
			holder.name.setText(getItem(position).getClientName());
			holder.address.setText(getItem(position).getClientAddress());
			holder.treatment.setText(Formatter.getSelf(getContext())
					.getTreatmentName(getItem(position).getTreatments()));

			ImageLoaderUtil.display(getItem(position).getImageUrl(), holder.image);
			if (getItem(position).isTreatmentCanceled()) {
				holder.disableContainer.setVisibility(View.VISIBLE);
				holder.statusDisableTitle.setText(R.string.treatment_canceled);
				holder.statusDisableRemove.setTag(position);
				holder.statusDisableRemove.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						final int position = (Integer) v.getTag();
						new RemoveUpcomingTreatmentFromListTask(getContext(), new HttpCallback() {

							@Override
							public void onAnswerReturn(Object answer) {
								if (answer instanceof Integer) {
									Dialogs.showServerFailedDialog(getContext());
									return;
								}
								remove(getItem(position));
							}
						}, getItem(position).getUpcomingtreatmentId()).execute();
					}
				});
			} else {
				holder.disableContainer.setVisibility(View.GONE);
				holder.statusDisableRemove.setOnClickListener(null);
			}

			return convertView;
		}

		private class ViewHolder {
			ImageView image;
			TextView date, name, address, treatment;
			ViewGroup disableContainer;
			TextView statusDisableTitle, statusDisableRemove;
		}

	}

}
