package utilities.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Locale;

import org.apache.http.HttpResponse;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import utilities.Dialogs;
import utilities.InternetSettings;
import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import com.pictureit.noambaroz.beautyapp.R;
import com.pictureit.noambaroz.beautyapp.data.Constants;

//Written by @Noam Bar-Oz

public abstract class HttpBase extends AsyncTask<String, String, Object> {

	private HttpResponse response;
	private String url;
	protected int statusCode;
	protected Context ctx;
	protected MyProgressDialog mProgressDialog;
	protected boolean showProgressDialog = true;

	public HttpBase(Context ctx) {
		this.ctx = ctx;
	}

	protected abstract Object continueInBackground(String result);

	@Override
	protected abstract Object doInBackground(String... params);

	protected boolean isConnectionAvailable(Context context) {
		if (!InternetSettings.isNetworkAvailable(context)) {
			Dialogs.showErrorDialog(context, R.string.no_internet_connection);
			return false;
		}
		return true;
	}

	@Override
	protected void onPreExecute() {
		if (!isConnectionAvailable(ctx)) {
			this.cancel(true);
			return;
		}

		if (showProgressDialog) {
			showProgressDialog();
		}
		super.onPreExecute();
	}

	protected void showProgressDialog() {
		mProgressDialog = new MyProgressDialog();
		mProgressDialog.show(((Activity) ctx).getFragmentManager(), "progressDialog");
	}

	@Override
	protected void onPostExecute(Object result) {
		if (mProgressDialog != null)
			mProgressDialog.dismiss();
		super.onPostExecute(result);
	}

	protected HttpParams getParams() {
		int TIMEOUT_MILLISEC = 30 * 1000;
		HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, TIMEOUT_MILLISEC);
		HttpConnectionParams.setSoTimeout(httpParams, TIMEOUT_MILLISEC);
		return httpParams;
	}

	protected abstract void prepare(String request);

	public String processEntity() {
		if (response != null) {
			InputStream instream;
			try {
				statusCode = response.getStatusLine().getStatusCode();
				instream = response.getEntity().getContent();
				String result = convertStreamToString(instream);

				return result;
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return "";
	}

	private String convertStreamToString(InputStream is) {
		/*
		 * To convert the InputStream to String we use the
		 * BufferedReader.readLine() method. We iterate until the BufferedReader
		 * return null which means there's no more data to read. Each line will
		 * appended to a StringBuilder and returned as String.
		 */
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url.toLowerCase(Locale.ENGLISH);
	}

	public HttpResponse getResponse() {
		return response;
	}

	public void setResponse(HttpResponse response) {
		this.response = response;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public interface HttpCallback {
		public void onAnswerReturn(Object answer);
	}

	private class MyProgressDialog extends DialogFragment {
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			ProgressDialog p = new ProgressDialog(ctx);
			p.setMessage(ctx.getString(R.string.dialog_messege_please_wait));
			p.setCancelable(false);
			p.setCanceledOnTouchOutside(false);
			return p;
		}
	}

	protected String getUid() {
		SharedPreferences pref = ctx.getSharedPreferences(Constants.APP_PREFS_NAME, Context.MODE_PRIVATE);
		return pref.getString(Constants.PREFS_KEY_UID, "");
		// return Secure.getString(ctx.getContentResolver(), Secure.ANDROID_ID);
	}

	public static void showErrorDialog(Activity activity) {
		Dialogs.showErrorDialog(activity, R.string.dialog_messege_server_error);
	}
}
