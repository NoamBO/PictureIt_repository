package com.pictureit.noambaroz.beauticianapp;

import android.app.Activity;
import android.app.Fragment;
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
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.pictureit.noambaroz.beauticianapp.location.LoadingMapIndicator;
import com.pictureit.noambaroz.beautycianapp.R;

public class MapFragment extends Fragment implements LocationListener {

	private MapView mapView;
	private GoogleMap mMap;

	private static final float INITIAL_ZOOM = 17;

	private static final float MIN_ZOOM_ALLOWED = INITIAL_ZOOM - 1;

	private LoadingMapIndicator mLoadingMapIndicator;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (mLoadingMapIndicator == null) {
			mLoadingMapIndicator = new LoadingMapIndicator(getActivity());
			mLoadingMapIndicator.showMapLoadingIndicator();
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_map_container, container, false);
		mapView = (MapView) v.findViewById(R.id.mapview);
		mapView.onCreate(savedInstanceState);

		return v;
	}

	private void initMapIfNeeded(Location location) {

		if (location == null) {
			return;
		} else if (mMap != null) {
			mLoadingMapIndicator.mapFullyLoaded();
		} else {
			mMap = mapView.getMap();

			LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());

			mMap.getUiSettings().setRotateGesturesEnabled(false);
			mMap.getUiSettings().setTiltGesturesEnabled(false);
			mMap.getUiSettings().setZoomControlsEnabled(false);
			MapsInitializer.initialize(this.getActivity());
			CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(ll, INITIAL_ZOOM);
			mMap.animateCamera(cameraUpdate, new CancelableCallback() {

				@Override
				public void onFinish() {
					mLoadingMapIndicator.mapFullyLoaded();
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
	public void onResume() {
		mapView.onResume();

		initMapIfNeeded(((MainActivity) getActivity()).getLastLocation());
		super.onResume();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();

	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
		mapView.onLowMemory();
	}

	@Override
	public void onLocationChanged(Location location) {

		initMapIfNeeded(location);
	}

}
