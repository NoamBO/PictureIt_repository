package com.pictureit.noambaroz.beauticianapp;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.InflateException;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.pictureit.noambaroz.beauticianapp.data.DataProvider;
import com.pictureit.noambaroz.beauticianapp.dialog.Dialogs;
import com.pictureit.noambaroz.beauticianapp.gcm.GcmUtil;
import com.pictureit.noambaroz.beauticianapp.server.HttpBase.HttpCallback;
import com.pictureit.noambaroz.beauticianapp.server.UpdateAvailabilityTask;
import com.pictureit.noambaroz.beauticianapp.server.UpdateLocationTask;
import com.pictureit.noambaroz.beauticianapp.utilities.view.MySwitch;

public class MainActivity extends Activity implements LoaderCallbacks<Cursor>, ConnectionCallbacks,
		OnConnectionFailedListener, LocationListener {

	private static final int REQUEST_UPDATE_GOOGLE_PLAY_APK = 12;

	private static final String STATE_RESOLVING_ERROR = "resolving_error";

	private GoogleApiClient mGoogleApiClient;

	// Request code to use when launching the resolution activity
	private static final int REQUEST_RESOLVE_GOOGLE_CLIENT_ERROR = 1001;
	// Unique tag for the error dialog fragment
	private static final String DIALOG_ERROR = "dialog_error";
	// Bool to track whether the app is already resolving an error
	private boolean mResolvingError = false;

	private LocationRequest mLocationRequest;

	private boolean mRequestingLocationUpdates;

	private static LocationListener mMapFragmentLocationListener;

	private final int FRAGMENT_CONTAINER = R.id.main_screen_fragment_container;

	private MySwitch sIsAvailable;

	public static FragmentManager fragmentManager;

	private OnCheckedChangeListener mAvailabilityChackedChangeListener = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			updateServer(isChecked);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		fragmentManager = getFragmentManager();
		mResolvingError = savedInstanceState != null && savedInstanceState.getBoolean(STATE_RESOLVING_ERROR, false);

		if (googlePlayServicesAvailable()) {
			initActivity();
			GcmUtil.get(getApplicationContext()).registerToGcm();
			resume();
		}
		checkIfNotificationWaiting(getIntent());
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		checkIfNotificationWaiting(intent);
	}

	private void checkIfNotificationWaiting(Intent intent) {
		if (intent == null || intent.getExtras() == null
				|| intent.getExtras().containsKey(Constant.EXTRA_KEY_HAS_NOTIFICATION)
				|| !intent.getBooleanExtra(Constant.EXTRA_KEY_HAS_NOTIFICATION, false))
			return;

		int classType = intent.getIntExtra(Constant.EXTRA_KEY_HAS_NOTIFICATION, 0);
		switch (classType) {
		case Constant.EXTRA_CLASS_TYPE_MESSAGES:
			startActivity(new Intent(MainActivity.this, ActivityMessages.class));
			break;
		case Constant.EXTRA_CLASS_TYPE_NOTIFICATION:
			startActivity(new Intent(MainActivity.this, ActivityNotificationsDialog.class));
			break;
		case Constant.EXTRA_CLASS_TYPE_TREATMENTS:
			startActivity(new Intent(MainActivity.this, ActivityUpcomingTreatments.class));
			break;
		default:
			return;
		}
	}

	private void initActivity() {
		setContentView(R.layout.activity_main);
		sIsAvailable = (MySwitch) findViewById(R.id.s_main_activity_availability_switch);
		if (MyPreference.isAvailable())
			getFragmentManager().beginTransaction().add(FRAGMENT_CONTAINER, new FragmentMap()).commit();
		else {
			getFragmentManager().beginTransaction().add(FRAGMENT_CONTAINER, new FragmentUnAvailable()).commit();
			sIsAvailable.setChecked(false);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		ActivityWithFragment.addViewToTopOfActionBar(MainActivity.this);
		getActionBar().setTitle("");
		sIsAvailable.setOnCheckedChangeListener(mAvailabilityChackedChangeListener);
		resume();
	}

	private void updateServer(boolean isChecked) {
		UpdateAvailabilityTask httpRequest = new UpdateAvailabilityTask(MainActivity.this, new HttpCallback() {

			@Override
			public void onAnswerReturn(Object answer) {
				if ((Boolean) answer)
					onAvailabilityChanged(MyPreference.isAvailable());
				else {
					Dialogs.showServerFailedDialog(MainActivity.this);
					sIsAvailable.setOnCheckedChangeListener(null);
					sIsAvailable.setChecked(MyPreference.isAvailable());
					sIsAvailable.setOnCheckedChangeListener(mAvailabilityChackedChangeListener);
				}
			}
		}, isChecked);
		httpRequest.execute();
	}

	protected void onAvailabilityChanged(boolean isChecked) {
		Fragment f;
		if (isChecked) {

			if (getFragmentManager().getBackStackEntryCount() > 0)
				getFragmentManager().popBackStack();
			f = new FragmentMap();
			connectToGooglePlayServicesApi();

		} else {
			f = new FragmentUnAvailable();
			disconnectGooglePlayServices();
		}
		getFragmentManager().beginTransaction().replace(FRAGMENT_CONTAINER, f).commit();
	}

	private TextView tvNotificationBadge;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			setDropdownTextViewFont();
		}

		getMenuInflater().inflate(R.menu.main, menu);
		getLoaderManager().initLoader(0, null, this);
		final MenuItem badgeItem = menu.findItem(R.id.action_messages);
		RelativeLayout badgeLayout = (RelativeLayout) badgeItem.getActionView();
		tvNotificationBadge = (TextView) badgeLayout.findViewById(R.id.tv_orders_notification_badge);
		ImageView iv = (ImageView) badgeLayout.findViewById(R.id.myButton);
		iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onOptionsItemSelected(badgeItem);
			}
		});
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		switch (id) {
		case R.id.action_settings:
			launchActivity(ActivitySettings.class);
			break;
		case R.id.action_future_treatments:
			launchActivity(ActivityUpcomingTreatments.class);
			break;
		case R.id.action_messages:
			launchActivity(ActivityMessages.class);
			break;
		case R.id.action_treatment_history:
			launchActivity(ActivityHistory.class);
			break;
		case R.id.action_my_profile:
			launchActivity(ActivityMyProfile.class);
			break;
		case R.id.action_statistics:
			launchActivity(ActivityStatistics.class);
			break;
		case R.id.action_terms_of_use:
			launchActivity(ActivityTermsOfUse.class);
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private boolean isOkToFinishApp;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		final int LENGTH_SHORT = 2000; // 2 seconds
		if (keyCode != KeyEvent.KEYCODE_BACK)
			return super.onKeyDown(keyCode, event);
		if (getFragmentManager().getBackStackEntryCount() == 0) {
			if (isOkToFinishApp)
				this.finish();
			else {
				Toast.makeText(getApplicationContext(), R.string.press_again_to_exit, Toast.LENGTH_SHORT).show();
				isOkToFinishApp = true;
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						isOkToFinishApp = false;
					}
				}, LENGTH_SHORT);
			}
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void launchActivity(Class<?> class1) {
		Intent intent = new Intent(MainActivity.this, class1);
		startActivity(intent);
		overridePendingTransition(R.anim.activity_enter_slidein_anim, R.anim.activity_exit_shrink_anim);
	}

	private boolean googlePlayServicesAvailable() {
		int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
		if (resultCode != ConnectionResult.SUCCESS) {
			if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
				GooglePlayServicesUtil.getErrorDialog(resultCode, this, REQUEST_UPDATE_GOOGLE_PLAY_APK,
						new OnCancelListener() {

							@Override
							public void onCancel(DialogInterface dialog) {
								finish();
							}
						}).show();
			} else {
				finish();
			}
			return false;
		}
		return true;
	}

	private void resume() {
		if (MyPreference.hasAlarmsDialogsToShow())
			startActivity(new Intent(MainActivity.this, ActivityNotificationsDialog.class));
		if (googlePlayServicesAvailable()) {
			buildGoogleApiClient();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_UPDATE_GOOGLE_PLAY_APK) {
			if (resultCode == RESULT_OK) {
				initActivity();
				resume();
			} else {
				finish();
			}
		} else if (requestCode == REQUEST_RESOLVE_GOOGLE_CLIENT_ERROR) {
			mResolvingError = false;
			if (resultCode == RESULT_OK) {
				// Make sure the app is not already connected or attempting to
				// connect
				if (!mGoogleApiClient.isConnecting() && !mGoogleApiClient.isConnected()) {
					mGoogleApiClient.connect();
				}
			}
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putBoolean(STATE_RESOLVING_ERROR, mResolvingError);
	}

	protected void buildGoogleApiClient() {
		if (mGoogleApiClient == null) {
			mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this)
					.addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		if (MyPreference.isAvailable()) {
			connectToGooglePlayServicesApi();
		} else {

		}
	}

	private void connectToGooglePlayServicesApi() {
		if (!mResolvingError && !mGoogleApiClient.isConnecting() && !mGoogleApiClient.isConnected()) {
			mGoogleApiClient.connect();
		}
	}

	private void disconnectGooglePlayServices() {
		LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);

		mGoogleApiClient.disconnect();
		mRequestingLocationUpdates = false;
		MyPreference.setLocationServiceState(false);

	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		if (mResolvingError) {
			// Already attempting to resolve an error.
			return;
		} else if (result.hasResolution()) {
			try {
				mResolvingError = true;
				result.startResolutionForResult(this, REQUEST_RESOLVE_GOOGLE_CLIENT_ERROR);
			} catch (SendIntentException e) {
				// There was an error with the resolution intent. Try again.
				mGoogleApiClient.connect();
			}
		} else {
			// Show dialog using GooglePlayServicesUtil.getErrorDialog()
			showErrorDialog(result.getErrorCode());
			mResolvingError = true;
		}
	}

	@Override
	public void onConnected(Bundle connectionHint) {
		Log.i("connected to GoogleServicesApi");
		if (!mRequestingLocationUpdates) {
			createLocationRequestIfNeeded();
			startLocationUpdates();
		}
	}

	@Override
	public void onConnectionSuspended(int arg0) {
	}

	// The rest of this code is all about building the error dialog

	/* Creates a dialog for an error message */
	private void showErrorDialog(int errorCode) {
		// Create a fragment for the error dialog
		ErrorDialogFragment dialogFragment = new ErrorDialogFragment();
		// Pass the error that should be displayed
		Bundle args = new Bundle();
		args.putInt(DIALOG_ERROR, errorCode);
		dialogFragment.setArguments(args);
		dialogFragment.show(getFragmentManager(), "errordialog");
	}

	/* Called from ErrorDialogFragment when the dialog is dismissed. */
	public void onDialogDismissed() {
		mResolvingError = false;
	}

	/* A fragment to display an error dialog */
	public static class ErrorDialogFragment extends DialogFragment {
		public ErrorDialogFragment() {
		}

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// Get the error code and retrieve the appropriate dialog
			int errorCode = this.getArguments().getInt(DIALOG_ERROR);
			return GooglePlayServicesUtil.getErrorDialog(errorCode, this.getActivity(),
					REQUEST_RESOLVE_GOOGLE_CLIENT_ERROR);
		}

		@Override
		public void onDismiss(DialogInterface dialog) {
			((MainActivity) getActivity()).onDialogDismissed();
		}
	}

	protected void createLocationRequestIfNeeded() {
		int interval = 15 * 60 * 1000; // 15 Minutes
		int fastestInterval = 10 * 60 * 1000; // 10 Minutes

		// int interval = 10 * 1000; // 10 Seconds
		// int fastestInterval = 5 * 1000; // 5 Seconds

		if (mLocationRequest != null)
			return;
		Log.i("creating location request");
		mLocationRequest = new LocationRequest();
		mLocationRequest.setInterval(interval);
		mLocationRequest.setFastestInterval(fastestInterval);
		mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
	}

	protected void startLocationUpdates() {
		if (!MyPreference.isLocationServiceOn()) {
			Log.i("location updates started");
			MyPreference.setLocationServiceState(true);
			LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
			mRequestingLocationUpdates = true;
		}
		if (mMapFragmentLocationListener != null)
			mMapFragmentLocationListener.onLocationChanged(getLastLocation());
	}

	@Override
	public void onLocationChanged(Location location) {
		if (location == null)
			return;

		updateLocationOnBackendInBackground(location);
		if (mMapFragmentLocationListener != null)
			mMapFragmentLocationListener.onLocationChanged(location);
	}

	public Location getLastLocation() {
		return LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
	}

	private void updateLocationOnBackendInBackground(Location location) {
		Log.i("Got location! start updating the server");
		UpdateLocationTask httpRequest = new UpdateLocationTask(MainActivity.this, location);
		httpRequest.execute();
	}

	public void setMapFragmentLocationListener(LocationListener l) {
		mMapFragmentLocationListener = l;
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		CursorLoader loader = new CursorLoader(getApplicationContext(), DataProvider.CONTENT_URI_ORDERS_AROUND_ME,
				new String[] { DataProvider.COL_ID }, null, null, null);
		return loader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		int count = data.getCount();
		Log.i(count + " Pending Notifications");
		if (tvNotificationBadge != null) {
			tvNotificationBadge.setText(String.valueOf(data.getCount()));
			tvNotificationBadge.setVisibility(count < 1 ? View.GONE : View.VISIBLE);
		}
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
	};

	/**
	 * work on API Level 11+
	 */
	private void setDropdownTextViewFont() {
		getLayoutInflater().setFactory(new LayoutInflater.Factory() {
			@Override
			public View onCreateView(String name, Context context, AttributeSet attrs) {
				if (name.equalsIgnoreCase("com.android.internal.view.menu.IconMenuItemView")
						|| name.equalsIgnoreCase("TextView")) {
					try {
						LayoutInflater li = LayoutInflater.from(context);
						final View view = li.createView(name, null, attrs);
						new Handler().post(new Runnable() {
							public void run() {
								Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/FbExtrim-Regular.ttf");
								((TextView) view).setTypeface(tf, Typeface.NORMAL);
							}
						});
						return view;
					} catch (InflateException e) {
						// Handle any inflation exception here
					} catch (ClassNotFoundException e) {
						// Handle any ClassNotFoundException here
					}
				}
				return null;
			}

		});
	}

}
