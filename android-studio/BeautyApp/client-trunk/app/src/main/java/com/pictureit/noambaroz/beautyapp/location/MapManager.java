package com.pictureit.noambaroz.beautyapp.location;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import utilities.Log;
import utilities.server.HttpBase.HttpCallback;
import utilities.view.MyFontTextView;
import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLoadedCallback;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.pictureit.noambaroz.beautyapp.ActivityBeautician;
import com.pictureit.noambaroz.beautyapp.R;
import com.pictureit.noambaroz.beautyapp.data.Constants;
import com.pictureit.noambaroz.beautyapp.data.MarkerData;
import com.pictureit.noambaroz.beautyapp.location.MyLocation.LocationResult;
import com.pictureit.noambaroz.beautyapp.server.GetMarkers;


import com.google.android.gms.location.LocationServices;

public class MapManager implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        OnCameraChangeListener, ConnectionCallbacks, OnConnectionFailedListener,
		OnInfoWindowClickListener, LocationListener, OnMarkerClickListener {

	private static final float INITIAL_ZOOM = 17;

    /**
     * Sets the maximum zoom out before hiding all markers and stop updating the map with new markers
     */
    private static final float MIN_ZOOM_ALLOWED = INITIAL_ZOOM - 1;

	private Activity mActivity;
	private GoogleMap mMap;

    /**
     * Helps to recognize if the current zoom is more then the maximum
     */
	private int currentZoom = -1;

	private static MapManager INSTANCE;
	private HashMap<Marker, String> mVisibleMarkers;
	private boolean hideAllMarkers;

    /**
     * The center point of the screen when updating the markers. while this point is still visible, we won't update the map with new markers
     */
	private LatLng latsPositionBeforeUpdating;

    /**
     * My current location Marker
     */
	private Marker mMyPositionMarker;

    /**
     * My current location MarkerOptions
     */
    private MarkerOptions mMyPositionMarkerOptions;

	private LoadingMapIndicator mLoadingMapIndicator;

    private GoogleApiClient mGoogleApiClient;

    private LocationRequest mLocationRequest;

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

    /**
     * Initiate the MapManager singleton
     * @param activity
     */
	private MapManager(Activity activity) {
		mActivity = activity;
		mVisibleMarkers = new HashMap<Marker, String>();
		mLoadingMapIndicator = new LoadingMapIndicator(mActivity);

        if(mGoogleApiClient == null)
        mGoogleApiClient = new GoogleApiClient.Builder(mActivity)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
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
		showMapLoadingIndicator();
		if (mMap == null) {
			mMap = ((MapFragment) mActivity.getFragmentManager().findFragmentById(R.id.map)).getMap();
			// Check if we were successful in obtaining the map.
			if (mMap != null) {
				mMap.setOnMapLoadedCallback(new OnMapLoadedCallback() {

					@Override
					public void onMapLoaded() {
						onMapFullyLoaded();
					}
				});
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

		ImageButton myLocation = (ImageButton) mActivity.findViewById(R.id.my_location_button);
		myLocation.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onMyLocationButtonClick();
			}
		});

		onMyLocationChange(location);
		mMap.setOnMarkerClickListener(this);
		mMap.setOnInfoWindowClickListener(this);
		mMap.setOnCameraChangeListener(this);
		mMap.getUiSettings().setRotateGesturesEnabled(false);
		mMap.getUiSettings().setTiltGesturesEnabled(false);
		mMap.getUiSettings().setZoomControlsEnabled(false);
		mMap.setInfoWindowAdapter(new InfoWindowAdapter() {

			@Override
			public View getInfoWindow(Marker arg0) {
				return null;
			}

			@Override
			public View getInfoContents(Marker arg0) {
				MyFontTextView textView = new MyFontTextView(mActivity);
				textView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
				textView.setText(arg0.getTitle());
				return textView;
			}
		});
	}

    /**
     * @param arrayList contains {@code MarkerData}
     */
	public void setUpMarkers(ArrayList<MarkerData> arrayList) {
		if (hideAllMarkers)
			return;

		if (this.mMap != null) {
			mMap.clear();
			mMyPositionMarker = mMap.addMarker(mMyPositionMarkerOptions);
			mVisibleMarkers.clear();
			for (MarkerData item : arrayList) {
				Marker m = mMap.addMarker(getMarkerForItem(item));
				mVisibleMarkers.put(m, item.getBeautician_id());
			}
		}
	}

	private void hideAllMarkers() {
		hideAllMarkers = true;
		mMap.clear();
		mVisibleMarkers.clear();
	}

    /**
     *
     * @param markerData as is
     * @return MarkerOptions
     */
	private MarkerOptions getMarkerForItem(MarkerData markerData) {
		MarkerOptions mo = new MarkerOptions();

		LatLng position = new LatLng(markerData.getLatitude(), markerData.getLongitude());
		mo.icon(BitmapDescriptorFactory.fromResource(getMarkerIcon(markerData.getClassification())));
		mo.position(position);
		if (markerData.getName() != null && !markerData.getName().equalsIgnoreCase("")) {
			mo.title(markerData.getName());
		}

		return mo;
	}

	private int getMarkerIcon(String id) {
		int classification = Integer.parseInt(id);
		int resId = R.drawable.location_ic_yellow;
		switch (classification) {
		case MarkerData.TYPE_DEFAULT:
			resId = R.drawable.location_ic_yellow;
			break;
		case MarkerData.TYPE_PEDICURE:
			resId = R.drawable.location_ic_blue;
			break;
		case MarkerData.TYPE_MAKEUP:
			resId = R.drawable.location_ic_green;
			break;
		case MarkerData.TYPE_AESTHETIC_MEDICINE:
			resId = R.drawable.location_ic_red;
			break;
		case MarkerData.TYPE_BUSSINESS:
			resId = R.drawable.location_ic_bussiness;
			break;
		}
		return resId;
	}

	@Override
	public void onInfoWindowClick(Marker marker) {
		if (mVisibleMarkers.containsKey(marker)) {
			Intent intent = new Intent(mActivity, ActivityBeautician.class);
			intent.putExtra(Constants.EXTRA_BEAUTICIAN_ID, mVisibleMarkers.get(marker));
			mActivity.startActivity(intent);
			mActivity.overridePendingTransition(R.anim.activity_enter_slidein_anim, R.anim.activity_exit_shrink_anim);
		}
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
		onMyLocationChange(arg0);
	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		// TODO
	}

	@Override
	public void onConnected(Bundle arg0) {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5000); // Update location every 5 second
        mLocationRequest.setSmallestDisplacement(15);

        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);

        Location l = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
		if (l != null)
			setUpMapIfNeeded(l);
		else {
			setCurrentLocation();
		}
	}

	@Override
	public void onDisconnected() {
	}

    @Override
    public void onConnectionSuspended(int i) {
    }

    /**
     * Called as backup only if mLocationService can't get location
     */
	private void setCurrentLocation() {
		LocationResult locationResult = new LocationResult() {

			@Override
			public void gotLocation(Location location) {
				if (location != null)
					setUpMapIfNeeded(location);
			}
		};
		MyLocation myLocation = new MyLocation();
		boolean mapCanBeLoaded = myLocation.getLocation(mActivity, locationResult);
		if (!mapCanBeLoaded)
			onLoadingMapImpossible();
	}

	private void onLoadingMapImpossible() {
		showNoMapIndicator();
	}

	private void showMapLoadingIndicator() {
		mLoadingMapIndicator.showMapLoadingIndicator();
	}

	private void showNoMapIndicator() {
		mLoadingMapIndicator.showNoMapIndicator();
	}

	private void onMapFullyLoaded() {
		mLoadingMapIndicator.mapFullyLoaded();
	}

	public boolean checkIfMarkerOnScreen(LatLng markerPosition, LatLngBounds bounds) {
		if (!bounds.contains(markerPosition))
			return false;
		return true;
	}

    /**
     * Updating the map's Markers when {@code lastPositionBeforeUpdating} is no longer on screen
     * @param latLng new position of the center of screen
     */
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

	public boolean onMyLocationButtonClick() {
        Location l = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
		if (l == null)
			return false;
		LatLng latLng = new LatLng(l.getLatitude(), l.getLongitude());
		CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, INITIAL_ZOOM);
		mMap.animateCamera(cameraUpdate);
		return true;
	}

	public void onMyLocationChange(Location location) {

		if (location == null)
			return;

		if (mMyPositionMarker != null) {
			mMyPositionMarker.remove();
		}

		mMyPositionMarkerOptions = new MarkerOptions().flat(true)
				.icon(BitmapDescriptorFactory.fromResource(R.drawable.location_ic_me)).anchor(0.5f, 0.5f)
				.position(new LatLng(location.getLatitude(), location.getLongitude()));

		mMyPositionMarker = mMap.addMarker(mMyPositionMarkerOptions);

	}

	@Override
	public boolean onMarkerClick(Marker arg0) {
		if (arg0.equals(mMyPositionMarker))
			return true;
		return false;
	}

    /**
     * Called from activity's {@code onStart} in order to get location updates from google api
     */
    public void onStart() {
        if(mGoogleApiClient != null)
            mGoogleApiClient.connect();
    }

    /**
     * Called from activity's {@code onStop} in order to stop location updates from google api
     */
    public void onStop() {
        if(mGoogleApiClient != null)
            mGoogleApiClient.disconnect();
    }

}
