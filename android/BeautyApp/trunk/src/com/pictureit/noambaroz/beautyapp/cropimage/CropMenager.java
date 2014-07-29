package com.pictureit.noambaroz.beautyapp.cropimage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.ArrayAdapter;
import android.widget.Toast;

public class CropMenager {

	private Uri mImageCaptureUri;

	public static final int PICK_FROM_CAMERA = 1;
	public static final int CROP_FROM_CAMERA = 2;
	public static final int PICK_FROM_FILE = 3;

	public CropMenager() {
	}

	public void start(final Fragment fragment) {

		final String[] items = new String[] { "Take from camera", "Select from gallery" };
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(fragment.getActivity(), android.R.layout.select_dialog_item, items);
		AlertDialog.Builder builder = new AlertDialog.Builder(fragment.getActivity());

		builder.setTitle("Select Image");
		builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int item) { // pick from
																	// camera
				if (item == 0) {
					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

					setImageCaptureUri(Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "tmp_avatar_"
							+ String.valueOf(System.currentTimeMillis()) + ".jpg")));

					intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, getImageCaptureUri());

					try {
						intent.putExtra("return-data", true);

						fragment.startActivityForResult(intent, PICK_FROM_CAMERA);
					} catch (ActivityNotFoundException e) {
						e.printStackTrace();
					}
				} else { // pick from file
					Intent intent = new Intent();

					intent.setType("image/*");
					intent.setAction(Intent.ACTION_GET_CONTENT);

					fragment.startActivityForResult(Intent.createChooser(intent, "Complete action using"), PICK_FROM_FILE);
				}
			}
		});

		final AlertDialog dialog = builder.create();

		dialog.show();
	}

	public void doCrop(final Fragment fragment) {
		final ArrayList<CropOption> cropOptions = new ArrayList<CropOption>();

		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setType("image/*");

		List<ResolveInfo> list = fragment.getActivity().getPackageManager().queryIntentActivities(intent, 0);

		int size = list.size();

		if (size == 0) {
			Toast.makeText(fragment.getActivity(), "Can not find image crop app", Toast.LENGTH_SHORT).show();

			return;
		} else {
			intent.setData(getImageCaptureUri());

			intent.putExtra("outputX", 200);
			intent.putExtra("outputY", 200);
			intent.putExtra("aspectX", 1);
			intent.putExtra("aspectY", 1);
			intent.putExtra("scale", true);
			intent.putExtra("return-data", true);

			if (size == 1) {
				Intent i = new Intent(intent);
				ResolveInfo res = list.get(0);

				i.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));

				fragment.startActivityForResult(i, CROP_FROM_CAMERA);
			} else {
				for (ResolveInfo res : list) {
					final CropOption co = new CropOption();

					co.title = fragment.getActivity().getPackageManager().getApplicationLabel(res.activityInfo.applicationInfo);
					co.icon = fragment.getActivity().getPackageManager().getApplicationIcon(res.activityInfo.applicationInfo);
					co.appIntent = new Intent(intent);

					co.appIntent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));

					cropOptions.add(co);
				}

				CropOptionAdapter adapter = new CropOptionAdapter(fragment.getActivity().getApplicationContext(), cropOptions);

				AlertDialog.Builder builder = new AlertDialog.Builder(fragment.getActivity());
				builder.setTitle("Choose Crop App");
				builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int item) {
						fragment.startActivityForResult(cropOptions.get(item).appIntent, CROP_FROM_CAMERA);
					}
				});

				builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
					@Override
					public void onCancel(DialogInterface dialog) {

						if (getImageCaptureUri() != null) {
							fragment.getActivity().getContentResolver().delete(getImageCaptureUri(), null, null);
							setImageCaptureUri(null);
						}
					}
				});

				AlertDialog alert = builder.create();

				alert.show();
			}
		}
	}

	public Uri getImageCaptureUri() {
		return mImageCaptureUri;
	}

	public void setImageCaptureUri(Uri mImageCaptureUri) {
		this.mImageCaptureUri = mImageCaptureUri;
	}

}
