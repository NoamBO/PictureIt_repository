package com.noambaroz.crop_image;

import java.io.ByteArrayOutputStream;
import java.io.File;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;

public class Crop extends Activity{
	
	private CropMenager cropUtil;
	
	public static final String ACTION_IMAGE_SELECTED = "image_string";
	public static final String ACTION_IMAGE_TURN_INTO_BITMAP = "image_bitmap";
	public static final String ACTION_CANCEL = "cancel";
	
	
	public static final String cropFilter = "me.intent.action.BROADCAST";
	
	public static void doCrop(Context context, BroadcastReceiver receiver) {
		Intent intent = new Intent(context, Crop.class);
		context.startActivity(intent);
	}
	
	private DialogInterface.OnCancelListener mCancelListener = new OnCancelListener() {
		
		@Override
		public void onCancel(DialogInterface dialog) {
			finish();
			sendMessage(ACTION_CANCEL, null, null);
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		cropUtil = new CropMenager();
		cropUtil.start(Crop.this, mCancelListener);
	}
	
	private void sendMessage(String message, String image_byte, Bitmap bitmap) {
		Intent intent = new Intent();
		intent.setAction(cropFilter);
		intent.putExtra("message", message);
		if(image_byte != null) {
			intent.putExtra("image", image_byte);
		} else if(bitmap != null) {
			intent.putExtra("image", bitmap);
		}
		sendBroadcast(intent);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != RESULT_OK) {
			finish();
			sendMessage(ACTION_CANCEL, null, null);
			return;
		}

		switch (requestCode) {
		case CropMenager.PICK_FROM_CAMERA:
			cropUtil.doCrop(this, mCancelListener);
			break;
		case CropMenager.PICK_FROM_FILE:
			cropUtil.setImageCaptureUri(data.getData());
			cropUtil.doCrop(this, mCancelListener);
			break;
		case CropMenager.CROP_FROM_CAMERA:
			Bundle extras = data.getExtras();
			if (extras != null) {
				final Bitmap photo = extras.getParcelable("data");
				final String bitString = MyBitmap.encodeTobase64(photo);
				sendMessage(ACTION_IMAGE_SELECTED, bitString, null);
				new Handler().post(new Runnable() {

					@Override
					public void run() {
						Bitmap b = MyBitmap.decodeBase64(bitString);
						sendMessage(ACTION_IMAGE_TURN_INTO_BITMAP, null, b);
						finish();
					}
				});

			}
			File f = new File(cropUtil.getImageCaptureUri().getPath());

			if (f.exists())
				f.delete();
			break;
		}
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		sendMessage(ACTION_CANCEL, null, null);
	}

	public static class MyBitmap {
		public static String encodeTobase64(Bitmap image) {
			Bitmap immage = image;
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			immage.compress(Bitmap.CompressFormat.JPEG, 100, baos);
			byte[] b = baos.toByteArray();
			String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

			return imageEncoded;
		}

		public static Bitmap decodeBase64(String input) {
			byte[] decodedByte = Base64.decode(input.getBytes(), Base64.DEFAULT);
			return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
		}
	}

}
