package com.pictureit.noambaroz.beautyapp.server;

import org.json.JSONException;
import org.json.JSONObject;

import utilities.Dialogs;
import utilities.server.BaseHttpPost;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

import com.pictureit.noambaroz.beautyapp.R;
import com.pictureit.noambaroz.beautyapp.customdialogs.MySingleChoiseDialog;
import com.pictureit.noambaroz.beautyapp.data.JsonToObject;

public class PostVerifyAddress extends BaseHttpPost {

	private HttpCallback myCallback;

	private static final String KEY_ADDRESS = "address";

	public PostVerifyAddress(Context ctx, HttpCallback callback, String address) {
		super(ctx, null);
		myCallback = callback;
		prepare(address);
	}

	@Override
	protected Object continueInBackground(String result) {
		return JsonToObject.jsonToAddresses(result);
	}

	@Override
	protected void onPostExecute(Object result) {
		if (result == null) {
			Dialogs.makeToastThatCloseActivity((Activity) ctx, R.string.dialog_messege_server_error);
			return;
		}
		final String[] array = (String[]) result;
		if (array.length == 0) {
			myCallback.onAnswerReturn(null);
		} else {

			MySingleChoiseDialog dialog = new MySingleChoiseDialog(ctx, ctx.getString(R.string.select_address), array,
					0, new OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							myCallback.onAnswerReturn(array[which]);
							dialog.dismiss();
						}
					});
			dialog.setCanceledOnTouchOutside(false);
			dialog.show();
		}
		super.onPostExecute(result);
	}

	@Override
	protected void prepare(String request) {
		setUrl(ServerUtil.URL_REQUEST_VERIFY_ADDRESS);
		JSONObject json = new JSONObject();
		try {
			json.put(KEY_ADDRESS, request);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mMainJson = json;
	}

}
