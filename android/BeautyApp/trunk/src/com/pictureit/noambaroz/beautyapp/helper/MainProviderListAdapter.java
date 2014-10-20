package com.pictureit.noambaroz.beautyapp.helper;

import java.util.ArrayList;

import utilities.view.AutoResizeTextView;
import android.app.Activity;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;

import com.pictureit.noambaroz.beautyapp.BeauticianUtil;
import com.pictureit.noambaroz.beautyapp.R;
import com.pictureit.noambaroz.beautyapp.data.Beautician;
import com.pictureit.noambaroz.beautyapp.server.ImageLoaderUtil;

public class MainProviderListAdapter extends ArrayAdapter<Beautician> {

	@SuppressWarnings("unchecked")
	protected <T extends View> T findView(View rootView, int id) {
		return (T) rootView.findViewById(id);
	}

	public MainProviderListAdapter(Context context, int resource, ArrayList<Beautician> objects) {
		super(context, resource, objects);
		// arrayList = objects;
		// itemsAll = (ArrayList<Beautician>) arrayList.clone();
		// suggestions = new ArrayList<Beautician>();
		ctx = context;
	}

	// private ArrayList<Beautician> arrayList;
	// private ArrayList<Beautician> itemsAll;
	// private ArrayList<Beautician> suggestions;
	private Context ctx;

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = ((Activity) ctx).getLayoutInflater().inflate(R.layout.row_main_providers_list, parent, false);
			holder.pic = findView(convertView, R.id.iv_row_providers_list);
			holder.name = findView(convertView, R.id.tv_row_providers_list_name);
			holder.address = findView(convertView, R.id.tv_row_providers_list_address);
			holder.raters = findView(convertView, R.id.tv_row_providers_list_raters);
			holder.role = findView(convertView, R.id.tv_row_providers_list_role);
			holder.ratingBar = findView(convertView, R.id.rb_row_providers_list_rating);
			holder.ratingBar.setOnTouchListener(new OnTouchListener() {
				public boolean onTouch(View v, MotionEvent event) {
					return true;
				}
			});
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		ImageLoaderUtil.display(getItem(position).getPhoto(), holder.pic);
		holder.name.setText(getItem(position).getName());
		if (getItem(position).getAddress() != null)
			holder.address.setText(BeauticianUtil.formatAddress(ctx, getItem(position).getAddress()));
		if (getItem(position).getRating() != null) {
			holder.raters.setText(BeauticianUtil.formatRaters(getItem(position).getRating().getRaters(), ctx));
			holder.ratingBar.setRating((float) getItem(position).getRating().getRate());
		}

		return convertView;
	}

	// public void filter() {
	// if (MapManager.getInstance() == null)
	// return;
	//
	// LatLngBounds latLngBounds =
	// MapManager.getInstance().getGoogleMap().getProjection().getVisibleRegion().latLngBounds;
	// for (Beautician beautician : itemsAll) {
	// boolean visible =
	// MapManager.getInstance().checkIfMarkerOnScreen(beautician.latLng,
	// latLngBounds);
	// if (visible)
	// suggestions.add(beautician);
	// }
	// arrayList.clear();
	// if (suggestions != null && suggestions.size() > 0) {
	// clear();
	// for (Beautician b : suggestions) {
	// add(b);
	// }
	// notifyDataSetChanged();
	// }
	// suggestions.clear();
	//
	// }

	private static class ViewHolder {
		ImageView pic;
		AutoResizeTextView name;
		AutoResizeTextView address;
		AutoResizeTextView role;
		AutoResizeTextView raters;
		RatingBar ratingBar;
	}

}
