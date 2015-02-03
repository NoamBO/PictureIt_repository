package utilities.server;

//Written by @Noam Bar-Oz

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import android.content.Context;

public abstract class BaseHttpPost extends HttpBase {

	protected HttpCallback callback;
	protected String response;
	protected JSONObject mMainJson;

	public BaseHttpPost(Context ctx, HttpCallback callback) {
		super(ctx);
		this.callback = callback;
	}

	@Override
	protected Object doInBackground(String... params) {
		if (mMainJson == null)
			return null;

		HttpClient client = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(getUrl());
		httpPost.setHeader("Content-Type", "application/json");
		httpPost.setParams(getParams());
		try {

			StringEntity se = new StringEntity(mMainJson.toString(), "UTF-8");
			httpPost.setEntity(se);

			setResponse(client.execute(httpPost));
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
		super.onPostExecute(result);
		if (callback != null)
			callback.onAnswerReturn(result);
	}

}
