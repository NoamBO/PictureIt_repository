package com.pictureit.noambaroz.beauticianapp;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.CancelableCallback;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.pictureit.noambaroz.beautycianapp.R;

public class MapFragment extends MapFragmentBase implements LocationListener {

	private static GoogleMap mMap;

	private static final float INITIAL_ZOOM = 17;

	private static final float MIN_ZOOM_ALLOWED = INITIAL_ZOOM - 1;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_map_container, container, false);
		mLoadingMapCoverScreen = findView(v, R.id.myMap_fragment_loading_view);
		initIndicator();
		showMapLoadingIndicator();
		return v;
	}

	private synchronized void initMapIfNeeded(Location location) {

		if (location == null) {
			return;
		} else if (mMap == null) {

			mMap = ((com.google.android.gms.maps.MapFragment) MainActivity.fragmentManager
					.findFragmentById(R.id.location_map)).getMap();
			// Check if we were successful in obtaining the map.
			if (mMap == null)
				return;

			LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());

			mMap.getUiSettings().setRotateGesturesEnabled(false);
			mMap.getUiSettings().setTiltGesturesEnabled(false);
			mMap.getUiSettings().setZoomControlsEnabled(false);
			MapsInitializer.initialize(this.getActivity());
			CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(ll, INITIAL_ZOOM);
			mMap.animateCamera(cameraUpdate, new CancelableCallback() {

				@Override
				public void onFinish() {
					mapFullyLoaded();
				}

				@Override
				public void onCancel() {
				}
			});
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		((MainActivity) activity).setMapFragmentLocationListener(this);
	}

	@Override
	public void onDetach() {
		super.onDetach();
		((MainActivity) getActivity()).setMapFragmentLocationListener(null);
	}

	@Override
	public void onResume() {
		initMapIfNeeded(((MainActivity) getActivity()).getLastLocation());
		super.onResume();
	}

	@Override
	public void onPause() {
		if (mMap != null && getActivity().isFinishing()) {
			MainActivity.fragmentManager.beginTransaction()
					.remove(MainActivity.fragmentManager.findFragmentById(R.id.location_map)).commit();
			mMap = null;
		}
		super.onPause();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		if (mMap != null && !getActivity().isFinishing()) {
			MainActivity.fragmentManager.beginTransaction()
					.remove(MainActivity.fragmentManager.findFragmentById(R.id.location_map)).commit();
			mMap = null;
		}
	}

	@Override
	public void onLocationChanged(Location location) {
		initMapIfNeeded(location);
	}

}
