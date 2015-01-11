package com.pictureit.noambaroz.beauticianapp;

import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.noambaroz.crop_image.Crop;
import com.pictureit.noambaroz.beauticianapp.data.AreaType;
import com.pictureit.noambaroz.beauticianapp.data.ClassificationType;
import com.pictureit.noambaroz.beauticianapp.data.Formatter;
import com.pictureit.noambaroz.beauticianapp.data.MyProfileDetails;
import com.pictureit.noambaroz.beauticianapp.data.TreatmentType;
import com.pictureit.noambaroz.beauticianapp.dialog.BaseDialog;
import com.pictureit.noambaroz.beauticianapp.dialog.Dialogs;
import com.pictureit.noambaroz.beauticianapp.dialog.MySingleChoiseDialog;
import com.pictureit.noambaroz.beauticianapp.server.GetBeauticianDetailsTask;
import com.pictureit.noambaroz.beauticianapp.server.HttpBase.HttpCallback;
import com.pictureit.noambaroz.beauticianapp.server.UpdatePersonalContactOptionsTask;
import com.pictureit.noambaroz.beauticianapp.server.UpdatePersonalDetailsTask;
import com.pictureit.noambaroz.beauticianapp.server.UpdatePersonalDetailsTask.Builder;
import com.pictureit.noambaroz.beauticianapp.server.UpdatePersonalDiplomasTask;
import com.pictureit.noambaroz.beauticianapp.server.UpdatePersonalTreatmentsTask;
import com.pictureit.noambaroz.beauticianapp.utilities.view.MyEditText;

public class ActivityMyProfile extends ActivityWithFragment {

	public static interface GetStringsListener {
		public void returnStrings(String[] strings);
	}

	@Override
	protected void initActivity() {
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
		private RatingBar ratingBar;

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
			ratingBar = findView(v, R.id.rb_my_profile_row1);

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

					if (answer instanceof Integer) {
						Dialogs.showServerFailedDialog(getActivity());
						return;
					} else {
						mDetails = (MyProfileDetails) answer;
						setActivityMainScreenDetails();
					}
				}
			}).execute();
			return v;
		}

		private void updateHeader() {
			tvName.setText(TextUtils.isEmpty(mDetails.getName()) ? "" : mDetails.getName());
			tvBusiness.setText(TextUtils.isEmpty(mDetails.getBusinessName()) ? "" : mDetails.getBusinessName());

			if (TextUtils.isEmpty(mDetails.getClassification())) {
				String classification = Formatter.getSelf(getActivity()).getClassificationById(
						mDetails.getClassification());
				tvClassification.setText(classification);
			}
			tvRaters.setText("( " + mDetails.getRaters() + " " + getString(R.string.raters) + " ) ");
			ratingBar.setRating((int) mDetails.getRate());
		}

		private void updateContactDetails() {
			String telephone = getString(R.string.telephone) + " "
					+ (TextUtils.isEmpty(mDetails.getPhone()) ? "" : mDetails.getPhone());
			tvPhone.setText(telephone);
			String email = getString(R.string.email) + " "
					+ (TextUtils.isEmpty(mDetails.getEmail()) ? "" : mDetails.getEmail());
			tvEmail.setText(email);
			String address = getString(R.string.business_address) + " "
					+ (TextUtils.isEmpty(mDetails.getBusinessAddress()) ? "" : mDetails.getBusinessAddress());
			tvAddress.setText(address);
			String billingAddress = getString(R.string.billing_address) + " "
					+ (TextUtils.isEmpty(mDetails.getBillingAddress()) ? "" : mDetails.getBillingAddress());
			tvReceiptsAddress.setText(billingAddress);
			String area = getString(R.string.area) + " "
					+ Formatter.getSelf(getActivity()).getAreaById(mDetails.getArea());
			tvArea.setText(area);
			String mobility = getString(R.string.arrive_at_the_customer_house) + " "
					+ (mDetails.isArrivedHome() ? getString(R.string.yes) : getString(R.string.no));
			tvMobility.setText(mobility);
		}

		private void updateTreatments() {
			ArrayList<TreatmentType> treatmentsArray = Formatter.TreatmentList.genarate(getActivity(),
					mDetails.getTreatments());
			Formatter.getSelf(getActivity()).setTreatmentsList(tvTreatmentsList1, tvTreatmentsList2, treatmentsArray,
					false);
		}

		private void updateDiplomas() {
			String diplomas = "";
			for (int i = 0; i < mDetails.getDegrees().length; i++) {
				diplomas = diplomas + getString(R.string.bullet) + mDetails.getDegrees()[i];
				if (i < (mDetails.getDegrees().length - 1))
					diplomas = diplomas + "\n";
			}

			tvDiplomas.setText(diplomas);
		}

		protected void setActivityMainScreenDetails() {
			updateHeader();
			updateContactDetails();
			updateTreatments();
			updateDiplomas();

			tvAbout.setText(TextUtils.isEmpty(mDetails.getAbout()) ? "" : mDetails.getAbout());
			String experience = getString(R.string.experience) + " "
					+ (TextUtils.isEmpty(mDetails.getExperience()) ? "" : mDetails.getExperience());
			tvExperience.setText(experience);
			String paymentMethod = getString(R.string.payment_method) + " "
					+ (TextUtils.isEmpty(mDetails.getPayment()) ? "" : mDetails.getPayment());
			tvPaymentMethod.setText(paymentMethod);

			if (!editHeader.hasOnClickListeners()) {
				editHeader.setOnClickListener(this);
				editContactOptions.setOnClickListener(this);
				editTreatments.setOnClickListener(this);
				editDiplomas.setOnClickListener(this);
				editPersonalDetails.setOnClickListener(this);
			}
		}

		@Override
		public void onClick(View v) {
			int id = v.getId();
			switch (id) {
			case R.id.ib_my_profile_row1_edit:
				showEditHeaderDialog();
				break;
			case R.id.ib_my_profile_row2_edit:
				showEditContactOptionsDialog();
				break;
			case R.id.ib_my_profile_row3_edit:
				showTreatmentSelectionDialog();
				break;
			case R.id.ib_my_profile_row4_edit:
				showEditDiplomasDialog();
				break;
			case R.id.ib_my_profile_row5_edit:

				break;
			default:
				break;
			}
		}

		private void showEditDiplomasDialog() {
			DialogEditDiplomas d = new DialogEditDiplomas(getActivity(), mDetails.getDegrees());
			d.setOnFinishClickListener(new GetStringsListener() {

				@Override
				public void returnStrings(final String[] strings) {
					if (strings == null)
						return;
					else {
						new UpdatePersonalDiplomasTask(getActivity(), new HttpCallback() {

							@Override
							public void onAnswerReturn(Object answer) {
								if (answer == null || answer instanceof Integer) {
									Dialogs.showServerFailedDialog(getActivity());
									return;
								}

								mDetails.setDegrees(strings);
								updateDiplomas();
							}
						}, strings).execute();
					}
				}
			});
			d.show();
		}

		private void showTreatmentSelectionDialog() {
			FragmentTreatmentSelection fragment = new FragmentTreatmentSelection();
			ArrayList<TreatmentType> array = new ArrayList<TreatmentType>();
			long time = System.currentTimeMillis();
			for (TreatmentType tt : Formatter.getSelf(getActivity()).getAllTreatmentsType()) {
				boolean wasAdded = false;
				for (int i = 0; i < mDetails.getTreatments().length; i++) {
					if (tt.getTreatmentId().equalsIgnoreCase(mDetails.getTreatments()[i])) {
						TreatmentType t = new TreatmentType();
						t.setAmount(1);
						t.setDescription(tt.getDescription());
						t.setName(tt.getName());
						t.setPrice(tt.getPrice());
						t.setTreatmentId(tt.getTreatmentId());
						array.add(t);
						wasAdded = true;
					} else {
						if (!wasAdded && i == (mDetails.getTreatments().length - 1))
							array.add(tt);
					}
				}
			}
			Log.d("time", "getAllTreatmentsType time = " + (System.currentTimeMillis() - time) + " millis");
			fragment.setTreatments(array);
			fragment.setListener(new FragmentTreatmentSelection.OnTreatmentListChangeListener() {

				@Override
				public void onTreatmentListChange(ArrayList<TreatmentType> treatments) {
					if (treatments == null)
						return;

					ArrayList<String> array = new ArrayList<String>();
					for (TreatmentType tt : treatments) {
						if (tt.getAmount() > 0)
							array.add(tt.getTreatmentId());
					}
					final String[] strings = array.toArray(new String[array.size()]);
					if (!isArraysTheSame(strings, mDetails.getTreatments()))
						new UpdatePersonalTreatmentsTask(getActivity(), new HttpCallback() {

							@Override
							public void onAnswerReturn(Object answer) {
								if (answer == null || answer instanceof Integer) {
									Dialogs.showServerFailedDialog(getActivity());
									return;
								}

								mDetails.setTreatments(strings);
								updateTreatments();
							}
						}, strings).execute();
				}
			});
			getFragmentManager().beginTransaction().add(FRAGMENT_CONTAINER, fragment).addToBackStack(null).commit();
		}

		private boolean isArraysTheSame(String[] array1, String[] array2) {
			if (array1.length != array2.length)
				return false;
			int i = 0;
			long time = System.currentTimeMillis();
			for (String s1 : array1) {
				for (String s2 : array2) {
					if (s1.equalsIgnoreCase(s2))
						i++;
				}
			}
			Log.d("time", "isArraysTheSame time = " + (System.currentTimeMillis() - time) + " millis");
			if (i == array1.length)
				return true;
			else
				return false;
		}

		private void showEditContactOptionsDialog() {
			getFragmentManager().beginTransaction().add(FRAGMENT_CONTAINER, new FragmentEditContactOptions())
					.addToBackStack(null).commit();
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
				tvClassification.setText(Formatter.getSelf(getActivity()).getClassificationById(classificationId));

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
					String[] list = new String[Formatter.getSelf(getActivity()).getAllClassificationType(getActivity())
							.size()];
					int i = 0;
					for (ClassificationType ct : Formatter.getSelf(getActivity()).getAllClassificationType(
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
							tvClassification.setText(Formatter.getSelf(getActivity()).getClassificationById(
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
					updateHeader();
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

		private class FragmentEditContactOptions extends Fragment {

			private ViewGroup bFinish;
			private TextView phone;
			private EditText mail, businessAddress, billingAddress;
			private RadioButton arrived, noArrived;
			private TextView tvArea;
			private MySingleChoiseDialog areaDialog;
			private int areaID;

			@Override
			public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
				View v = inflater.inflate(R.layout.dialog_my_profile_edit_contact_details, container, false);
				bFinish = findView(v, R.id.dialog_my_profile_editing_contact_finish);
				phone = findView(v, R.id.tv_dialog_my_profile_editing_contact_telephone);
				mail = findView(v, R.id.et_dialog_my_profile_editing_contact_mail);
				businessAddress = findView(v, R.id.et_dialog_my_profile_editing_contact_business_address);
				billingAddress = findView(v, R.id.et_dialog_my_profile_editing_contact_billing_address);
				arrived = findView(v, R.id.rb_mobile);
				noArrived = findView(v, R.id.rb_not_mobile);
				tvArea = findView(v, R.id.tv_dialog_my_profile_editing_contact_area);

				tvArea.setText(Formatter.getSelf(getActivity()).getAreaById(mDetails.getArea()));
				phone.setText(mDetails.getPhone());
				mail.setText(mDetails.getEmail());
				businessAddress.setText(mDetails.getBusinessAddress());
				billingAddress.setText(mDetails.getBillingAddress());
				boolean arrivedHome = mDetails.isArrivedHome();
				arrived.setChecked(arrivedHome);
				noArrived.setChecked(!arrivedHome);
				return v;
			}

			@Override
			public void onResume() {
				super.onResume();
				tvArea.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						showAreas();
					}
				});
				bFinish.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						onFinish();
					}
				});
			}

			private void showAreas() {
				if (areaDialog == null) {
					String[] list = new String[Formatter.getSelf(getActivity()).getAllAreaType(getActivity()).size()];
					int i = 0;
					for (AreaType at : Formatter.getSelf(getActivity()).getAllAreaType(getActivity())) {
						list[i] = at.getTitle();
						i++;
					}
					areaDialog = new MySingleChoiseDialog(getActivity(), list);
					int area = Integer.parseInt(mDetails.getArea()) - 1;
					areaDialog.setChecked(area > -1 ? area : 0);
					areaDialog.showButtons(new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int areaCode) {
							areaID = areaCode + 1;
							tvArea.setText(Formatter.getSelf(getActivity()).getAreaById(String.valueOf(areaCode)));
						}
					}, null);
				}
				areaDialog.show();
			}

			private void onFinish() {
				UpdatePersonalContactOptionsTask.Builder b = new UpdatePersonalContactOptionsTask.Builder();
				b.addAreaCode(areaID);
				b.addArrivedHome(arrived.isChecked());
				b.addBillingAddress(billingAddress.getText().toString());
				b.addBusinessAddress(businessAddress.getText().toString());
				b.addMail(mail.getText().toString());

				UpdatePersonalContactOptionsTask task = b.build(getActivity(), new HttpCallback() {

					@Override
					public void onAnswerReturn(Object answer) {
						if (answer instanceof Integer || !((Boolean) answer)) {
							Dialogs.showServerFailedDialog(getActivity());
						} else {
							Dialogs.successToast(getActivity());
							mDetails.setArea(String.valueOf(areaID));
							mDetails.setArrivedHome(arrived.isChecked());
							mDetails.setBillingAddress(billingAddress.getText().toString());
							mDetails.setBusinessAddress(businessAddress.getText().toString());
							mDetails.setEmail(mail.getText().toString());
							updateContactDetails();
							backPressed();
						}
					}
				});
				task.execute();
			}
		}

		private class DialogEditDiplomas extends BaseDialog {

			private LinearLayout llList;
			private ViewGroup bFinish;
			private GetStringsListener mListener;

			public DialogEditDiplomas(Context context, String[] diplomas) {
				super(context);
				llList = (LinearLayout) mView.findViewById(R.id.ll_dialog_my_profile_edit_diplomas_linear_layout);
				bFinish = (ViewGroup) mView.findViewById(R.id.dialog_my_profile_edit_diplomas_finish);
				if (diplomas.length > 0)
					for (String string : diplomas)
						addEditText(string);
				else
					addEditText("");

				bFinish.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						if (mListener != null) {
							List<String> list = new ArrayList<String>();
							for (int i = 0; i < llList.getChildCount(); i++) {
								MyEditText et = (MyEditText) llList.getChildAt(i);
								if (!TextUtils.isEmpty(et.getText().toString()))
									list.add(et.getText().toString());
							}
							mListener.returnStrings(list.toArray(new String[list.size()]));
						}
						dismiss();
					}
				});
			}

			public void setOnFinishClickListener(GetStringsListener listener) {
				mListener = listener;
			}

			private void addEditText(String string) {
				MyEditText et = new MyEditText(mContext);
				et.setText(string);
				et.setSingleLine(true);
				et.setImeOptions(EditorInfo.IME_ACTION_DONE);
				et.setOnEditorActionListener(new OnEditorActionListener() {

					@Override
					public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
						if (actionId == EditorInfo.IME_ACTION_DONE) {
							if (v.getText().toString().equalsIgnoreCase("") && llList.getChildCount() > 1)
								llList.removeView(v);
							else if (!v.getText().toString().equalsIgnoreCase("") && llList.getChildCount() < 2)
								addEditText("");
						}
						return false;
					}
				});
				et.requestFocus();
				llList.addView(et, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
						LinearLayout.LayoutParams.WRAP_CONTENT));

			}

			@Override
			protected int getViewResourceId() {
				return R.layout.dialog_my_profile_edit_diplomas;
			}

		}
	}

}
