package com.pictureit.noambaroz.beautyapp.helper;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

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
		ctx = context;
	}

	private Context ctx;

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = ((Activity) ctx).getLayoutInflater().inflate(R.layout.row_main_providers_list, parent, false);
			holder.pic = findView(convertView, R.id.iv_base_beautician_row_pic);
			holder.name = findView(convertView, R.id.tv_base_beautician_row_name);
			holder.address = findView(convertView, R.id.tv_base_beautician_row_address);
			holder.raters = findView(convertView, R.id.tv_base_beautician_row_raters);
			holder.role = findView(convertView, R.id.tv_base_beautician_row_classification);
			holder.ratingBar = findView(convertView, R.id.rb_base_beautician_row_rating);
			holder.ratingBar.setOnTouchListener(new OnTouchListener() {
				public boolean onTouch(View v, MotionEvent event) {
					return true;
				}
			});
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		ImageLoaderUtil.display(getItem(position).getPhoto(), holder.pic, R.drawable.row_avatar, getContext());

		holder.name.setText(getItem(position).getName());
		if (getItem(position).getAddress() != null)
			holder.address.setText(BeauticianUtil.formatAddress(ctx, getItem(position).getAddress()));
		if (getItem(position).getRating() != null) {
			holder.raters.setText(BeauticianUtil.formatRaters(getItem(position).getRating().getRaters(), ctx));
			holder.ratingBar.setRating((float) getItem(position).getRating().getRate());
		}
		if (getItem(position).getClassificationId() != null) {
			holder.role.setText(BeauticianUtil.getClassificationTypeById(ctx, getItem(position).getClassificationId())
					.getTitle());
		}

		return convertView;
	}

	private static class ViewHolder {
		ImageView pic;
		TextView name;
		TextView address;
		TextView role;
		TextView raters;
		RatingBar ratingBar;
	}

}
