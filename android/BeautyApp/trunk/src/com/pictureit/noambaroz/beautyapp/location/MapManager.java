package com.pictureit.noambaroz.beautyapp.location;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import utilities.Log;
import utilities.server.HttpBase.HttpCallback;
import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.pictureit.noambaroz.beautyapp.ActivityBeautician;
import com.pictureit.noambaroz.beautyapp.R;
import com.pictureit.noambaroz.beautyapp.data.Constant;
import com.pictureit.noambaroz.beautyapp.data.MarkerData;
import com.pictureit.noambaroz.beautyapp.helper.ServiceOrderManager;
import com.pictureit.noambaroz.beautyapp.location.MyLocation.LocationResult;
import com.pictureit.noambaroz.beautyapp.server.GetMarkers;

public class MapManager implements OnCameraChangeListener, OnMarkerClickListener, ConnectionCallbacks,
		OnConnectionFailedListener, OnInfoWindowClickListener, LocationListener, OnMyLocationButtonClickListener {

	public interface MapMoovingListener {
		public void onZoomChange(CameraPosition position);
	}

	private static final float INITIAL_ZOOM = 17;
	private static final float MIN_ZOOM_ALLOWED = INITIAL_ZOOM - 1;

	private Activity mActivity;
	private GoogleMap mMap;
	public LocationClient mLocationClient;
	private int currentZoom = -1;
	private static MapManager INSTANCE;
	private HashMap<Marker, String> mVisibleMarkers;
	private boolean hideAllMarkers;
	private LatLng latsPositionBeforeUpdating;

	public GoogleMap getGoogleMap() {
		return mMap;
	}

	public static MapManager getInstance(Activity activity) {
		if (INSTANCE == null)
			INSTANCE = new MapManager(activity);

		return INSTANCE;
	}

	public static MapManager getInstance() {
		return INSTANCE;
	}

	private static final LocationRequest LOCATION_REQUEST = LocationRequest.create().setInterval(1 * 1 * 1000) // 5
																												// minutes
			.setFastestInterval(16) // 16ms = 60fps
			.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

	private MapManager(Activity activity) {
		mActivity = activity;
		mVisibleMarkers = new HashMap<Marker, String>();
	}

	public HashMap<Marker, String> getMarkers() {
		return mVisibleMarkers;
	}

	public void onDestroy() {
		INSTANCE = null;
	}

	public void setUpMapIfNeeded(Location location) {
		// Do a null check to confirm that we have not already instantiated the
		// map.
		if (mMap == null) {
			mMap = ((MapFragment) mActivity.getFragmentManager().findFragmentById(R.id.map)).getMap();
			// Check if we were successful in obtaining the map.
			if (mMap != null) {
				setUpMap(location);
			}
		}
	}

	private void setUpMap(Location location) {

		if (location != null) {
			double lat = location.getLatitude();
			double lng = location.getLongitude();
			LatLng coordinate = new LatLng(lat, lng);

			CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(coordinate, INITIAL_ZOOM);
			mMap.animateCamera(cameraUpdate);
		} else {
			// TODO show "no location found" dialog and exit app
		}

		mMap.setOnMarkerClickListener(this);
		mMap.setOnInfoWindowClickListener(this);
		mMap.setOnCameraChangeListener(this);
		mMap.setMyLocationEnabled(true);
		mMap.getUiSettings().setRotateGesturesEnabled(false);
		mMap.getUiSettings().setTiltGesturesEnabled(false);
		mMap.getUiSettings().setZoomControlsEnabled(false);
		mMap.setOnMyLocationButtonClickListener(this);
	}

	public void setUpLocationClientIfNeeded() {
		if (mLocationClient == null) {
			mLocationClient = new LocationClient(mActivity, this, // ConnectionCallbacks
					this); // OnConnectionFailedListener
		} else {

		}
	}

	public void setUpMarkers(ArrayList<MarkerData> arrayList) {
		if (hideAllMarkers)
			return;

		if (this.mMap != null) {
			// This is the current user-viewable region of the map

			// LatLngBounds bounds =
			// this.mMap.getProjection().getVisibleRegion().latLngBounds;

			// Loop through all the items that are available to be placed on the
			// map
			mMap.clear();
			mVisibleMarkers.clear();
			for (MarkerData item : arrayList) {

				mVisibleMarkers.put(mMap.addMarker(getMarkerForItem(item)), item.getBeautician_id());

				// If the item is within the the bounds of the screen
				// if (bounds.contains(new
				// LatLng(Double.parseDouble(item.getLatitude()),
				// Double.parseDouble(item
				// .getLongitude())))) {
				// // If the item isn't already being displayed
				// if (!mVisibleMarkers.containsKey(item.getId())) {
				// // Add the Marker to the Map and keep track of it with
				// // the HashMap
				// // getMarkerForItem just returns a MarkerOptions object
				// mVisibleMarkers.put(item.getId(),
				// this.mMap.addMarker(getMarkerForItem(item)));
				// }
				// }
				//
				// // If the marker is off screen
				// else {
				// // If the course was previously on screen
				// if (mVisibleMarkers.containsKey(item.getId())) {
				// // 1. Remove the Marker from the GoogleMap
				// mVisibleMarkers.get(item.getId()).remove();
				//
				// // 2. Remove the reference to the Marker from the //
				// // HashMap
				// mVisibleMarkers.remove(item.getId());
				// }
				// }

			}
		}
	}

	private void hideAllMarkers() {
		hideAllMarkers = true;
		mMap.clear();
		// Collection<Marker> values = mVisibleMarkers.values();
		// for (Marker m : values) {
		// m.remove();
		// }
		mVisibleMarkers.clear();
	}

	private MarkerOptions getMarkerForItem(MarkerData markerData) {
		MarkerOptions mo = new MarkerOptions();

		LatLng position = new LatLng(markerData.getLatitude(), markerData.getLongitude());
		mo.icon(BitmapDescriptorFactory.defaultMarker());
		mo.position(position);
		if (markerData.getName() != null && !markerData.getName().equalsIgnoreCase("")) {
			mo.title(markerData.getName());
		}

		return mo;
	}

	@Override
	public void onInfoWindowClick(Marker marker) {
		if (ServiceOrderManager.isPending(mActivity)) {
			Toast.makeText(mActivity, R.string.pending_order_toast, Toast.LENGTH_LONG).show();
			return;
		}
		if (mVisibleMarkers.containsKey(marker)) {
			Intent intent = new Intent(mActivity, ActivityBeautician.class);
			intent.putExtra(Constant.EXTRA_BEAUTICIAN_ID, mVisibleMarkers.get(marker));
			mActivity.startActivity(intent);
			mActivity.overridePendingTransition(R.anim.activity_enter_slidein_anim, R.anim.activity_exit_shrink_anim);
		}
	}

	@Override
	public boolean onMarkerClick(Marker arg0) {
		if (arg0.isInfoWindowShown())
			arg0.hideInfoWindow();
		return false;
	}

	boolean isFirst = true;

	@Override
	public void onCameraChange(CameraPosition pos) {
		boolean temp = hideAllMarkers;
		if (latsPositionBeforeUpdating == null)
			latsPositionBeforeUpdating = pos.target;
		LatLngBounds bounds = mMap.getProjection().getVisibleRegion().latLngBounds;
		Log.i("onCameraChange");
		// Check whether the zoom level change
		if (((int) pos.zoom) != currentZoom) {
			currentZoom = (int) pos.zoom;
			Log.i("onCameraZoomChange" + currentZoom);
			if (currentZoom < MIN_ZOOM_ALLOWED) {
				hideAllMarkers();
			} else
				hideAllMarkers = false;
		}
		if (!bounds.contains(latsPositionBeforeUpdating) || temp != hideAllMarkers) {
			updateMarkersOnbackground(pos.target);
			return;
		}
		if (isFirst) {
			updateMarkersOnbackground(pos.target);
			isFirst = false;
		}
	}

	@Override
	public void onLocationChanged(Location arg0) {
		setUpMapIfNeeded(arg0);
	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		// TODO
	}

	@Override
	public void onConnected(Bundle arg0) {
		mLocationClient.requestLocationUpdates(LOCATION_REQUEST, this);
		if (mLocationClient.getLastLocation() != null)
			setUpMapIfNeeded(mLocationClient.getLastLocation());
		else {
			setCurrentLocation();
		}
	}

	@Override
	public void onDisconnected() {
	}

	private void setCurrentLocation() {
		LocationResult locationResult = new LocationResult() {

			@Override
			public void gotLocation(Location location) {
				if (location != null)
					setUpMapIfNeeded(location);
			}
		};
		MyLocation myLocation = new MyLocation();
		myLocation.getLocation(mActivity, locationResult);
	}

	// private String getAvailableLocationProvider() {
	// LocationManager locationManager = (LocationManager)
	// mActivity.getSystemService(Context.LOCATION_SERVICE);
	// if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
	// return LocationManager.GPS_PROVIDER;
	// }
	// if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
	// {
	// return LocationManager.NETWORK_PROVIDER;
	// }
	// return null;
	// }

	private int getDistance(LatLng latLng) {
		if (mLocationClient != null && mLocationClient.isConnected() && mLocationClient.getLastLocation() != null) {
			Location loc = new Location("dummyprovider");

			loc.setLatitude(latLng.latitude);
			loc.setLongitude(latLng.longitude);
			double distance = mLocationClient.getLastLocation().distanceTo(loc);

			return (int) distance;
		}
		return -1;
	}

	public boolean checkIfMarkerOnScreen(LatLng markerPosition, LatLngBounds bounds) {
		if (!bounds.contains(markerPosition))
			return false;
		// int settingsDistance = Settings.getRadius(mActivity);
		// int distance = getDistance(markerPosition);
		// if (distance < 0 || distance > settingsDistance)
		// return false;

		return true;
	}

	public void updateMarkersOnbackground(LatLng latLng) {
		latsPositionBeforeUpdating = latLng;
		if (hideAllMarkers)
			return;
		GetMarkers getMarkers = new GetMarkers(mActivity, new HttpCallback() {

			@Override
			public void onAnswerReturn(Object answer) {
				if (answer != null) {
					ArrayList<MarkerData> result = new ArrayList<MarkerData>();
					if (answer instanceof ArrayList) {
						for (int i = 0; i < ((List<?>) answer).size(); i++) {
							Object item = ((List<?>) answer).get(i);
							if (item instanceof MarkerData) {
								result.add((MarkerData) item);
							}
						}

					}
					setUpMarkers(result);
				}
			}
		}, latLng.latitude, latLng.longitude);
		getMarkers.execute();
	}

	public GoogleMap getMap() {
		return mMap;
	}

	@Override
	public boolean onMyLocationButtonClick() {
		Location l = mMap.getMyLocation();
		if (l == null)
			return false;
		LatLng latLng = new LatLng(l.getLatitude(), l.getLongitude());
		CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, INITIAL_ZOOM);
		mMap.animateCamera(cameraUpdate);
		return true;
	}

	// private Handler mHandler = new Handler() {
	//
	// public void handleMessage(android.os.Message msg) {
	//
	// };
	// };
	//
	// private class UpdateMarkersVisibility extends AsyncTask<LatLngBounds,
	// Void, Void> {
	//
	// @Override
	// protected Void doInBackground(LatLngBounds... params) {
	// if (mMap != null) {
	// // This is the current user-viewable region of the map
	// LatLngBounds bounds = params[0];
	//
	// // Loop through all the items that are available to be placed on
	// // the
	// // map
	// for (Provider item : StaticData.getInstance().getProviders()) {
	//
	// // If the item is within the the bounds of the screen
	// if (bounds.contains(item.latLng)) {
	// // If the item isn't already being displayed
	// if (mVisibleMarkers.get(Integer.parseInt(item._id), null) == null) {
	// // Add the Marker to the Map and keep track of it
	// // with
	// // the HashMap
	// // getMarkerForItem just returns a MarkerOptions
	// // object
	// mVisibleMarkers.put(Integer.parseInt(item._id),
	// mMap.addMarker(getMarkerForItem(item)));
	// }
	// }
	//
	// // If the marker is off screen
	// else {
	// // If the course was previously on screen
	// if (mVisibleMarkers.get(Integer.parseInt(item._id), null) != null) {
	// // 1. Remove the Marker from the GoogleMap
	// mVisibleMarkers.get(Integer.parseInt(item._id)).remove();
	//
	// // 2. Remove the reference to the Marker from the
	// // HashMap
	// mVisibleMarkers.delete(Integer.parseInt(item._id));
	// }
	// }
	// }
	// }
	// return null;
	// }
	//
	// }

}
