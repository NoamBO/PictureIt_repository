package utilities.server;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;

import android.content.Context;

//Written by @Noam Bar-Oz

public abstract class BaseHttpGet extends HttpBase {

	protected HttpCallback callback;
	protected String response;

	public BaseHttpGet(Context ctx) {
		super(ctx);
	}

	@Override
	protected Object doInBackground(String... params) {
		HttpGet httpGet = new HttpGet(getUrl());
		httpGet.setHeader(HTTP.CONTENT_TYPE, "application/json");
		httpGet.setParams(getParams());
		try {
			setResponse(new DefaultHttpClient().execute(httpGet));
			response = processEntity();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return continueInBackground(response);
	}

	@Override
	protected void onPostExecute(Object result) {
		if (callback != null)
			callback.onAnswerReturn(result);
		super.onPostExecute(result);
	}

}
