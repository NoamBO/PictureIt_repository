package com.pictureit.noambaroz.beautyapp.customdialogs;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.pictureit.noambaroz.beautyapp.R;

public class MySingleChoiseDialog extends Dialog implements OnItemClickListener {

	private String[] mList;
	private int mChecked;
	private MyAdapter mAdapter;
	private OnClickListener mListaner;

	public MySingleChoiseDialog(Context context, String title, String[] list, int checked, OnClickListener listener) {
		super(context, R.style.Theme_DialodNoWindowTitle);
		setContentView(R.layout.my_single_choise_dialog_layout);
		mAdapter = new MyAdapter();
		setChecked(checked);
		setTitle(title);
		setList(list);
		mListaner = listener;
	}

	private void setList(String[] items) {
		mList = items;
		ListView listView = (ListView) findViewById(R.id.singlechois_dialog_list);
		listView.setAdapter(mAdapter);
		listView.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		mListaner.onClick(this, position);
	}

	public void setChecked(int which) {
		mChecked = which;
		mAdapter.notifyDataSetChanged();
	}

	private void setTitle(String title) {
		TextView textView = (TextView) findViewById(R.id.singlechois_dialog_title);
		textView.setText(title);
	}

	private class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return mList.length;
		}

		@Override
		public Object getItem(int position) {
			return mList[position];
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = getLayoutInflater().inflate(R.layout.my_single_choise_dialog_row, parent, false);
				holder.radio = (RadioButton) convertView.findViewById(R.id.dialog_row_radio);
				holder.text = (TextView) convertView.findViewById(R.id.dialog_row_text);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.text.setText(mList[position]);
			if (position == mChecked)
				holder.radio.setChecked(true);
			else
				holder.radio.setChecked(false);

			return convertView;
		}

	}

	static class ViewHolder {
		RadioButton radio;
		TextView text;
	}

}
