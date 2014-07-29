package com.pictureit.noambaroz.beautyapp;

import java.util.ArrayList;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.pictureit.noambaroz.beautyapp.data.Beautician;
import com.pictureit.noambaroz.beautyapp.helper.MainProviderListAdapter;

public class SearchProviderResultsFragment extends ListFragment {

	ArrayList<Beautician> mArrayList;

	public void putBeauticianList(ArrayList<Beautician> list) {
		mArrayList = list;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle data) {

		MainProviderListAdapter adapter = new MainProviderListAdapter(inflater.getContext(),
				android.R.layout.simple_list_item_1, mArrayList);
		setListAdapter(adapter);

		return super.onCreateView(inflater, container, data);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		BeauticianUtil.openBeauticianInNewActivity(getActivity(), (Beautician) getListAdapter().getItem(position));
		super.onListItemClick(l, v, position, id);
	}
}
