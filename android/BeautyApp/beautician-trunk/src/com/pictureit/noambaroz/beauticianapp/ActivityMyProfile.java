package com.pictureit.noambaroz.beauticianapp;

import java.util.ArrayList;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.noambaroz.crop_image.Crop;
import com.pictureit.noambaroz.beauticianapp.data.ClassificationType;
import com.pictureit.noambaroz.beauticianapp.data.MyProfileDetails;
import com.pictureit.noambaroz.beauticianapp.data.TreatmentType;
import com.pictureit.noambaroz.beauticianapp.data.TreatmentsFormatter;
import com.pictureit.noambaroz.beauticianapp.dialog.Dialogs;
import com.pictureit.noambaroz.beauticianapp.dialog.MySingleChoiseDialog;
import com.pictureit.noambaroz.beauticianapp.server.GetBeauticianDetailsTask;
import com.pictureit.noambaroz.beauticianapp.server.HttpBase.HttpCallback;
import com.pictureit.noambaroz.beauticianapp.server.UpdatePersonalDetailsTask;
import com.pictureit.noambaroz.beauticianapp.server.UpdatePersonalDetailsTask.Builder;

public class ActivityMyProfile extends ActivityWithFragment {

	@Override
	protected void initActivity() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void setFragment() {
		fragment = new FragmentMyProfile();
	}

	@Override
	protected void setFragmentTag() {
		FRAGMENT_TAG = "my_profile_fragment";
	}

	private class FragmentMyProfile extends Fragment implements OnClickListener {

		private MyProfileDetails mDetails;

		private ImageButton editHeader, editContactOptions, editTreatments, editDiplomas, editPersonalDetails;

		private ImageView ivImage;
		private TextView tvName, tvBusiness, tvClassification, tvRaters;
		private TextView tvPhone, tvEmail, tvAddress, tvReceiptsAddress, tvArea, tvMobility;
		private TextView tvTreatmentsList1, tvTreatmentsList2;
		private TextView tvDiplomas;
		private TextView tvAbout, tvExperience, tvPaymentMethod;

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View v = inflater.inflate(R.layout.fragment_my_profile, container, false);

			editHeader = findView(v, R.id.ib_my_profile_row1_edit);
			ivImage = findView(v, R.id.iv_my_profile_row1);
			ivImage.setDrawingCacheEnabled(true);
			tvName = findView(v, R.id.tv_my_profile_row1_name);
			tvBusiness = findView(v, R.id.tv_my_profile_row1_business_name);
			tvClassification = findView(v, R.id.tv_my_profile_row1_classification);
			tvRaters = findView(v, R.id.tv_my_profile_row1_raters);

			editContactOptions = findView(v, R.id.ib_my_profile_row2_edit);
			tvPhone = findView(v, R.id.tv_my_profile_row2_telephone);
			tvEmail = findView(v, R.id.tv_my_profile_row2_mail);
			tvAddress = findView(v, R.id.tv_my_profile_row2_business_address);
			tvReceiptsAddress = findView(v, R.id.tv_my_profile_row2_receipt_address);
			tvArea = findView(v, R.id.tv_my_profile_row2_area);
			tvMobility = findView(v, R.id.tv_my_profile_row2_mobility);

			editTreatments = findView(v, R.id.ib_my_profile_row3_edit);
			tvTreatmentsList1 = findView(v, R.id.tv_my_profile_row3_treatment_list1);
			tvTreatmentsList2 = findView(v, R.id.tv_my_profile_row3_treatment_list2);

			editDiplomas = findView(v, R.id.ib_my_profile_row4_edit);
			tvDiplomas = findView(v, R.id.tv_my_profile_row4_diplomas);

			editPersonalDetails = findView(v, R.id.ib_my_profile_row5_edit);
			tvAbout = findView(v, R.id.tv_my_profile_row5_about);
			tvExperience = findView(v, R.id.tv_my_profile_row5_experience);
			tvPaymentMethod = findView(v, R.id.tv_my_profile_row5_payment_method);

			new GetBeauticianDetailsTask(getActivity(), new HttpCallback() {

				@Override
				public void onAnswerReturn(Object answer) {
					mDetails = (MyProfileDetails) answer;
					if (answer instanceof Integer || mDetails == null) {
						Dialogs.showServerFailedDialog(getActivity());
						return;
					}

					setDetails(mDetails);
				}
			}).execute();
			return v;
		}

		protected void setDetails(MyProfileDetails d) {
			tvName.setText(TextUtils.isEmpty(d.getName()) ? "" : d.getName());
			tvBusiness.setText(TextUtils.isEmpty(d.getBusinessName()) ? "" : d.getBusinessName());

			if (TextUtils.isEmpty(d.getClassification())) {
				String classification = TreatmentsFormatter.getSelf(getActivity()).getClassificationById(
						d.getClassification());
				tvClassification.setText(classification);
			}
			tvRaters.setText("( " + d.getRaters() + " ) " + getString(R.string.raters));
			tvPhone.setText(TextUtils.isEmpty(d.getPhone()) ? "" : d.getPhone());
			tvEmail.setText(TextUtils.isEmpty(d.getEmail()) ? "" : d.getEmail());
			tvAddress.setText(TextUtils.isEmpty(d.getBusinessAddress()) ? "" : d.getBusinessAddress());
			tvReceiptsAddress.setText(TextUtils.isEmpty(d.getBillingAddress()) ? "" : d.getBillingAddress());
			tvArea.setText(TextUtils.isEmpty(d.getArea()) ? "" : d.getArea());
			tvMobility.setText(TextUtils.isEmpty(d.getIsArrivedHome()) ? "" : d.getIsArrivedHome());

			ArrayList<TreatmentType> treatmentsArray = TreatmentsFormatter.TreatmentList.genarate(getActivity(),
					d.getTreatments());
			TreatmentsFormatter.getSelf(getActivity()).setTreatmentsList(tvTreatmentsList1, tvTreatmentsList2,
					treatmentsArray);

			String diplomas = "";
			for (String s : d.getDegrees()) {
				diplomas = diplomas + s + "\n";
			}
			tvDiplomas.setText(diplomas);
			tvAbout.setText(TextUtils.isEmpty(d.getAbout()) ? "" : d.getAbout());
			tvExperience.setText(TextUtils.isEmpty(d.getExperience()) ? "" : d.getExperience());
			tvPaymentMethod.setText(TextUtils.isEmpty(d.getPayment()) ? "" : d.getPayment());

			editHeader.setOnClickListener(this);
			editContactOptions.setOnClickListener(this);
			editTreatments.setOnClickListener(this);
			editDiplomas.setOnClickListener(this);
			editPersonalDetails.setOnClickListener(this);
		}

		@Override
		public void onClick(View v) {
			int id = v.getId();
			switch (id) {
			case R.id.ib_my_profile_row1_edit:
				showEditHeaderDialog();
				break;
			case R.id.ib_my_profile_row2_edit:

				break;
			case R.id.ib_my_profile_row3_edit:

				break;
			case R.id.ib_my_profile_row4_edit:

				break;
			case R.id.ib_my_profile_row5_edit:

				break;
			default:
				break;
			}
		}

		private void showEditHeaderDialog() {
			getFragmentManager().beginTransaction().add(FRAGMENT_CONTAINER, new FragmentEditHeader())
					.addToBackStack(null).commit();
		}

		private class FragmentEditHeader extends Fragment implements HttpCallback {
			private EditText etPrivateName, etBusinessName;
			private TextView tvClassification;
			private ViewGroup bDone;
			private ImageView image;
			private String classificationId;
			private String imageByts;
			private Bitmap bitmap;
			private MySingleChoiseDialog classificationDialog;

			private BroadcastReceiver receiver = new BroadcastReceiver() {

				@Override
				public void onReceive(Context context, Intent intent) {
					String message = intent.getStringExtra("message");
					if (TextUtils.isEmpty(message) || message.equalsIgnoreCase(Crop.ACTION_CANCEL))
						return;

					if (message.equalsIgnoreCase(Crop.ACTION_IMAGE_TURN_INTO_BITMAP)) {
						bitmap = (Bitmap) intent.getParcelableExtra("image");
						setImage();
					}

					if (message.equalsIgnoreCase(Crop.ACTION_IMAGE_SELECTED)) {
						imageByts = intent.getStringExtra("image");
					}
				}
			};

			@Override
			public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
				registerReceiver(receiver, new IntentFilter(Crop.cropFilter));

				View v = inflater.inflate(R.layout.dialog_my_profile_edit_header, container, false);
				classificationId = mDetails.getClassification();
				image = findView(v, R.id.iv_dialog_my_profile_editing_header);
				etPrivateName = findView(v, R.id.et_dialog_my_profile_editing_header_name);
				etBusinessName = findView(v, R.id.et_dialog_my_profile_editing_header_business_name);
				tvClassification = findView(v, R.id.tv_dialog_my_profile_editing_header_classification);
				bDone = findView(v, R.id.dialog_my_profile_editing_header_finish);

				resetImage();

				etBusinessName.setText(mDetails.getBusinessName());
				etPrivateName.setText(mDetails.getName());
				tvClassification.setText(TreatmentsFormatter.getSelf(getActivity()).getClassificationById(
						classificationId));

				return v;
			}

			private void resetImage() {
				ivImage.setDrawingCacheEnabled(true);
				ivImage.buildDrawingCache(false);
				Bitmap b = Bitmap.createBitmap(ivImage.getDrawingCache());
				if (b != null)
					image.setImageBitmap(b);

				ivImage.setDrawingCacheEnabled(false);
			}

			@Override
			public void onResume() {
				super.onResume();
				bDone.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						UpdatePersonalDetailsTask.Builder builder = new Builder();
						builder.addBusinessName(etBusinessName.getText().toString());
						builder.addClassification(classificationId);
						builder.addName(etPrivateName.getText().toString());
						builder.addImage(imageByts);
						UpdatePersonalDetailsTask task = builder.build(getActivity(), FragmentEditHeader.this);
						task.execute();
					}
				});
				tvClassification.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						onClassificationDialog();
					}
				});
				image.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Crop.doCrop(getActivity(), receiver);
					}
				});
			}

			@Override
			public void onDestroy() {
				unregisterReceiver(receiver);
				super.onDestroy();
			}

			protected void onClassificationDialog() {
				if (classificationDialog == null) {
					String[] list = new String[TreatmentsFormatter.getSelf(getActivity())
							.getAllClassificationType(getActivity()).size()];
					int i = 0;
					for (ClassificationType ct : TreatmentsFormatter.getSelf(getActivity()).getAllClassificationType(
							getActivity())) {
						list[i] = ct.getTitle();
						i++;
					}
					classificationDialog = new MySingleChoiseDialog(getActivity(), list);
					classificationDialog.setMyTitle(R.string.classification);
					classificationDialog.setOnItemClickListener(new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							classificationId = String.valueOf(which + 1);
							tvClassification.setText(TreatmentsFormatter.getSelf(getActivity()).getClassificationById(
									classificationId));
							dialog.dismiss();
						}
					});
				}
				classificationDialog.show();
			}

			@Override
			public void onAnswerReturn(Object answer) {
				if (answer instanceof Integer || !((Boolean) answer)) {
					Dialogs.showServerFailedDialog(getActivity());
				} else {
					Dialogs.successToast(getActivity());
					mDetails.setName(etPrivateName.getText().toString());
					mDetails.setBusiness_name(etBusinessName.getText().toString());
					mDetails.setClassification(classificationId);
					if (bitmap != null)
						ivImage.setImageBitmap(bitmap);
					backPressed();
				}
			}

			private void setImage() {
				if (bitmap != null)
					image.setImageBitmap(bitmap);
				else
					image.setImageDrawable(getResources().getDrawable(R.drawable.profile_avatar));
			}

		}
	}

}
