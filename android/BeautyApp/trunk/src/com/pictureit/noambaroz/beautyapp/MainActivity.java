package com.pictureit.noambaroz.beautyapp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import utilities.BaseActivity;
import utilities.Log;
import utilities.server.HttpBase.HttpCallback;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.pictureit.noambaroz.beautyapp.animation.AnimationManager;
import com.pictureit.noambaroz.beautyapp.animation.BaseAnimationListener;
import com.pictureit.noambaroz.beautyapp.data.Beautician;
import com.pictureit.noambaroz.beautyapp.data.DataProvider;
import com.pictureit.noambaroz.beautyapp.data.JsonToObject;
import com.pictureit.noambaroz.beautyapp.gcm.GcmUtil;
import com.pictureit.noambaroz.beautyapp.helper.MainProviderListAdapter;
import com.pictureit.noambaroz.beautyapp.helper.ServiceOrderManager;
import com.pictureit.noambaroz.beautyapp.helper.ServiceOrderManager.OnOrderStatusChangeListener;
import com.pictureit.noambaroz.beautyapp.location.MapManager;
import com.pictureit.noambaroz.beautyapp.server.GetBeauticianArrayByIds;

public class MainActivity extends BaseActivity implements LoaderCallbacks<Cursor> {

	boolean isOkToFinishApp;
	private ImageButton mTouchToOpenSlider;
	private MainProviderListAdapter mAdapter;
	private static final int REQUEST_UPDATE_GOOGLE_PLAY_APK = 12;
	private GetBeauticianArrayByIds getBeauticianArrayByIds;
	private Dialog mPendingDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (googlePlayServicesAvailable()) {
			initActivity();
			GcmUtil.get(getApplicationContext()).registerToGcm();
		}

	}

	private void initActivity() {
		setContentView(R.layout.activity_main);
		mTouchToOpenSlider = findView(R.id.ib_main_touch_to_slide_list);
		getActionBar().setTitle("");
		MapManager.getInstance(MainActivity.this);
		initListeners();
		setListView();
	}

	private void setListView() {
		ListView listView = findView(R.id.lv_main_providers_list);
		mAdapter = new MainProviderListAdapter(MainActivity.this, android.R.layout.simple_list_item_1,
				new ArrayList<Beautician>());
		listView.setAdapter(mAdapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (isUserWaitingForTreatmentOrderResponse())
					Toast.makeText(getApplicationContext(), R.string.pending_order_toast, Toast.LENGTH_LONG).show();
				else
					BeauticianUtil.openBeauticianInNewActivity(MainActivity.this, mAdapter.getItem(position));
			}
		});
	}

	private void initListeners() {
		mTouchToOpenSlider.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onFooterClick();
			}
		});
	}

	private void onFooterClick() {
		View sliderContainer = findView(R.id.fl_main_providers_slider_list_container);
		final boolean isSliderInvisible = sliderContainer.getVisibility() == View.GONE;
		BaseAnimationListener l = new BaseAnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				if (isSliderInvisible) {
					changeProvidersUiList();
				}
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				if (!isSliderInvisible) {
					mAdapter.clear();
					mAdapter.notifyDataSetChanged();
				}
			}
		};
		AnimationManager.MainProvidersListAnimation.slide(sliderContainer, MainActivity.this, isSliderInvisible, l);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		CursorLoader loader = new CursorLoader(getApplicationContext(), DataProvider.CONTENT_URI_MESSAGES,
				new String[] { DataProvider.COL_ID }, null, null, DataProvider.COL_ID + " DESC");
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

	private TextView tvNotificationBadge;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		getLoaderManager().initLoader(0, null, this);
		final MenuItem badgeItem = menu.findItem(R.id.action_pending_orders);
		RelativeLayout badgeLayout = (RelativeLayout) badgeItem.getActionView();
		tvNotificationBadge = findView(badgeLayout, R.id.tv_orders_notification_badge);
		ImageView iv = findView(badgeLayout, R.id.myButton);
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
		if (id == R.id.action_explanation) {

		} else if (id == R.id.action_future_treatments) {
			launchActivityIfPossible(ActivityTreatments.class);
		} else if (id == R.id.action_pending_orders) {
			launchActivityIfPossible(ActivityMessages.class);
		} else if (id == R.id.action_history) {
			launchActivityIfPossible(ActivityHistory.class);
		} else if (id == R.id.action_terms_of_service) {
			launchActivityIfPossible(ActivityTermsOfService.class);
		} else if (id == R.id.action_search_providers) {
			launchActivityIfPossible(SearchProviderActivity.class);
		} else if (id == R.id.action_my_profile) {
			launchActivityIfPossible(ActivityMyProfile.class);
		}

		return true;
	}

	private <T> void launchActivityIfPossible(Class<T> T) {
		if (!isUserWaitingForTreatmentOrderResponse())
			launchActivity(T);
		else if (isUserWaitingForTreatmentOrderResponse() && T == ActivityMessages.class)
			launchActivity(T);
		else
			Toast.makeText(getApplicationContext(), R.string.pending_order_toast, Toast.LENGTH_LONG).show();
	}

	private <T> boolean launchActivity(Class<T> T) {
		Intent intent = new Intent(MainActivity.this, T);
		startActivity(intent);
		overridePendingTransition(R.anim.activity_enter_slidein_anim, R.anim.activity_exit_shrink_anim);
		return true;
	}

	private void changeProvidersUiList() {
		if (getBeauticianArrayByIds != null) {
			getBeauticianArrayByIds.cancel(true);
			getBeauticianArrayByIds = null;
		}

		LatLngBounds latLngBounds = MapManager.getInstance().getGoogleMap().getProjection().getVisibleRegion().latLngBounds;
		ArrayList<String> IDs = new ArrayList<String>();
		for (Map.Entry<Marker, String> entry : MapManager.getInstance().getMarkers().entrySet()) {
			boolean visible = MapManager.getInstance()
					.checkIfMarkerOnScreen(entry.getKey().getPosition(), latLngBounds);
			if (visible)
				IDs.add(entry.getValue());
		}
		if (IDs.size() < 1) {
			showEmptyBeauticianArrayDialog(R.string.main_screen_slider_empty_dialog_messege_no_pins_on_screen);
			return;
		}

		getBeauticianArrayByIds = new GetBeauticianArrayByIds(MainActivity.this, new HttpCallback() {

			@Override
			public void onAnswerReturn(Object answer) {
				if (answer != null) {
					List<Beautician> result = JsonToObject.convertObjectToBeauticianArrayList(answer);
					if (result.size() > 1) {
						mAdapter.addAll(result);
						mAdapter.notifyDataSetChanged();
					} else {
						showEmptyBeauticianArrayDialog(R.string.main_screen_slider_empty_dialog_messege_no_pins_on_screen);
					}
				} else {
					showEmptyBeauticianArrayDialog(R.string.dialog_messege_server_error);
				}
				getBeauticianArrayByIds = null;
			}
		}, IDs);
		getBeauticianArrayByIds.execute();
	}

	private void showEmptyBeauticianArrayDialog(int resId) {
		new AlertDialog.Builder(MainActivity.this).setTitle(R.string.main_screen_slider_empty_dialog_title)
				.setMessage(resId).setNeutralButton(R.string.dialog_ok_text, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						onFooterClick();
					}
				}).setCancelable(false).create().show();
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

	private boolean isUserWaitingForTreatmentOrderResponse() {
		return ServiceOrderManager.isPending(getApplicationContext());
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
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		resume();
		onPendingDialog(isUserWaitingForTreatmentOrderResponse());
	}

	private void onPendingDialog(boolean isPending) {
		if (mPendingDialog == null) {
			AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
			builder.setMessage(R.string.pending_order_dialog_message);
			builder.setPositiveButton(R.string.pending_order_dialog_button_wait, null);
			builder.setNegativeButton(R.string.pending_order_dialog_button_abort,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							ServiceOrderManager.cancelRequest(MainActivity.this, new OnOrderStatusChangeListener() {
								@Override
								public void onStatusChange(boolean isPending) {
									onPendingDialog(isPending);
								}
							});
						}
					});
			builder.setCancelable(false);
			mPendingDialog = builder.create();
		}
		if (isPending && !mPendingDialog.isShowing())
			mPendingDialog.show();
		else if (!isPending && mPendingDialog.isShowing())
			mPendingDialog.dismiss();
	}

	private void resume() {
		if (googlePlayServicesAvailable()) {
			MapManager.getInstance(MainActivity.this).setUpLocationClientIfNeeded();
			MapManager.getInstance(MainActivity.this).mLocationClient.connect();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (MapManager.getInstance(MainActivity.this).mLocationClient != null) {
			MapManager.getInstance(MainActivity.this).mLocationClient.disconnect();
		}
	}

	@Override
	protected void onDestroy() {
		MapManager.getInstance(MainActivity.this).onDestroy();
		super.onDestroy();
	}

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

}
