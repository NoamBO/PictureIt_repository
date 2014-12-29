package com.pictureit.noambaroz.beauticianapp;

import java.util.HashMap;

import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
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
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.pictureit.noambaroz.beauticianapp.data.DataProvider;
import com.pictureit.noambaroz.beautycianapp.R;

public class MapFragment extends MapFragmentBase implements OnMarkerClickListener, LocationListener,
		LoaderCallbacks<Cursor> {

	private static GoogleMap mMap;

	private static final float INITIAL_ZOOM = 17;

	private static final float MIN_ZOOM_ALLOWED = INITIAL_ZOOM - 1;

	private Marker mCurrentLocationMarker;
	private MarkerOptions mCurrentLocationMarkerOptions;

	private HashMap<Marker, String> allMarkers;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_map_container, container, false);
		allMarkers = new HashMap<Marker, String>();
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
			getLoaderManager().initLoader(0, null, MapFragment.this);

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
		if (mMap != null)
			updateMyLocationMarker(location);
	}

	private void updateMyLocationMarker(Location location) {
		if (mCurrentLocationMarker != null)
			mCurrentLocationMarker.remove();
		if (location != null) {
			mCurrentLocationMarkerOptions = new MarkerOptions().flat(true)
					.icon(BitmapDescriptorFactory.fromResource(R.drawable.location_ic_me)).anchor(0.5f, 0.5f)
					.position(new LatLng(location.getLatitude(), location.getLongitude()));
			mCurrentLocationMarker = mMap.addMarker(mCurrentLocationMarkerOptions);
		}
	}

	private MarkerOptions getMarkerOptionsForOrder(double latitude, double longitude) {
		MarkerOptions mo = new MarkerOptions();
		mo.position(new LatLng(latitude, longitude)).flat(true)
				.icon(BitmapDescriptorFactory.fromResource(R.drawable.location_ic_order)).anchor(0.5f, 0.5f);
		return mo;
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		CursorLoader loader = new CursorLoader(getActivity(), DataProvider.CONTENT_URI_ORDERS_AROUND_ME, new String[] {
				// DataProvider.COL_ID, DataProvider.COL_FIRST_NAME,
				// DataProvider.COL_LAST_NAME,
				DataProvider.COL_LATITUDE, DataProvider.COL_LONGITUDE, DataProvider.COL_ORDER_ID }, null, null, null);
		return loader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		Log.i("has " + data.getCount() + " rows on table");
		mMap.clear();
		if (mCurrentLocationMarkerOptions != null)
			mCurrentLocationMarker = mMap.addMarker(mCurrentLocationMarkerOptions);
		allMarkers.clear();
		if (data.moveToFirst()) {
			while (data.moveToNext()) {
				String orderId = data.getString(data.getColumnIndex(DataProvider.COL_ORDER_ID));
				double lat = data.getDouble(data.getColumnIndex(DataProvider.COL_LATITUDE));
				double lng = data.getDouble(data.getColumnIndex(DataProvider.COL_LONGITUDE));
				Marker m = mMap.addMarker(getMarkerOptionsForOrder(lat, lng));
				allMarkers.put(m, orderId);
			}
		}
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
	}

	@Override
	public boolean onMarkerClick(Marker marker) {
		String orderID = allMarkers.get(marker);
		return false;
	}

}
