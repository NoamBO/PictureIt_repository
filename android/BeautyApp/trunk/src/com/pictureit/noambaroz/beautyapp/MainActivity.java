package com.pictureit.noambaroz.beautyapp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import utilities.BaseActivity;
import utilities.server.HttpBase.HttpCallback;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
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
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.pictureit.noambaroz.beautyapp.animation.AnimationManager;
import com.pictureit.noambaroz.beautyapp.animation.BaseAnimationListener;
import com.pictureit.noambaroz.beautyapp.data.Beautician;
import com.pictureit.noambaroz.beautyapp.data.JsonToObject;
import com.pictureit.noambaroz.beautyapp.gcm.GcmUtil;
import com.pictureit.noambaroz.beautyapp.helper.MainProviderListAdapter;
import com.pictureit.noambaroz.beautyapp.location.MapManager;
import com.pictureit.noambaroz.beautyapp.server.GetBeauticianArrayByIds;

public class MainActivity extends BaseActivity {

	boolean isOkToFinishApp;
	private ImageButton mTouchToOpenSlider;
	private MainProviderListAdapter mAdapter;
	private static final int REQUEST_UPDATE_GOOGLE_PLAY_APK = 12;
	private GetBeauticianArrayByIds getBeauticianArrayByIds;

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
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_search_radius) {
			return launchActivity(PreferenceActivityRadius.class);
		} else if (id == R.id.action_ask_for_service) {
			return launchActivity(ServiceOrder.class);
		} else if (id == R.id.action_explanation) {

		} else if (id == R.id.action_terms_of_service) {
			return launchActivity(ActivityTermsOfService.class);
		} else if (id == R.id.action_search_providers) {
			return launchActivity(SearchProviderActivity.class);
		} else if (id == R.id.action_my_profile) {
			return launchActivity(PreferenceActivityMyProfile.class);
		}
		return super.onOptionsItemSelected(item);
	}

	private <T> boolean launchActivity(Class<T> T) {
		Intent intent = new Intent(MainActivity.this, T);
		startActivity(intent);
		overridePendingTransition(R.anim.activity_enter_slidein_anim, R.anim.activity_exit_shrink_anim);
		return true;
	}

	@Override
	protected void onResume() {
		super.onResume();
		resume();
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
			new AlertDialog.Builder(MainActivity.this).setTitle(R.string.main_screen_slider_empty_dialog_title)
					.setMessage(R.string.main_screen_slider_empty_dialog_messege)
					.setNeutralButton(R.string.dialog_ok_text, new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							onFooterClick();
						}
					}).setCancelable(false).create().show();
			return;
		}

		getBeauticianArrayByIds = new GetBeauticianArrayByIds(getApplicationContext(), new HttpCallback() {

			@Override
			public void onAnswerReturn(Object answer) {
				if (answer != null) {
					List<Beautician> result = JsonToObject.convertObjectToBeauticianArrayList(answer);
					if (result.size() > 1) {
						mAdapter.addAll(result);
						mAdapter.notifyDataSetChanged();
					}
				}
				getBeauticianArrayByIds = null;
			}
		}, IDs);
		// TODO getBeauticianArrayByIds.execute();
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

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_UPDATE_GOOGLE_PLAY_APK) {
			if (resultCode == RESULT_OK) {
				initActivity();
				resume();
			} else {
				finish();
			}
		}
	};

}
