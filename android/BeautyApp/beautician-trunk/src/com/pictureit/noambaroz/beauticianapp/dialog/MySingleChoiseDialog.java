package com.pictureit.noambaroz.beauticianapp.dialog;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.pictureit.noambaroz.beautycianapp.R;

public class MySingleChoiseDialog extends BaseDialog implements OnItemClickListener {

	private String[] mList;
	private int mChecked;
	private MyAdapter mAdapter;
	private OnClickListener mListaner;
	private TextView bOk, bCancel;

	public MySingleChoiseDialog(Context context, String[] list) {
		super(context);
		mChecked = -1;
		mAdapter = new MyAdapter();
		bOk = (TextView) findViewById(R.id.singlechois_dialog_ok);
		bCancel = (TextView) findViewById(R.id.singlechois_dialog_cancel);
		setList(list);
	}

	@Override
	protected int getViewResourceId() {
		return R.layout.my_single_choise_dialog_layout;
	}

	public MySingleChoiseDialog setSubTitle(int resID) {
		TextView tv = (TextView) findViewById(R.id.singlechois_dialog_subtitle);
		tv.setVisibility(View.VISIBLE);
		tv.setText(mContext.getString(resID));
		return this;
	}

	private void setList(String[] items) {
		mList = items;
		ListView listView = (ListView) findViewById(R.id.singlechois_dialog_list);
		listView.setAdapter(mAdapter);
		listView.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		mChecked = position;
		mAdapter.notifyDataSetChanged();

		if (mListaner != null)
			mListaner.onClick(this, position);
	}

	public MySingleChoiseDialog setChecked(int which) {
		mChecked = which;
		mAdapter.notifyDataSetChanged();
		return this;
	}

	public MySingleChoiseDialog setMyTitle(int resID) {
		return setMyTitle(mContext.getString(resID));
	}

	public MySingleChoiseDialog setMyTitle(String title) {
		TextView textView = (TextView) findViewById(R.id.singlechois_dialog_title);
		textView.setText(title);
		return this;
	}

	public MySingleChoiseDialog setOnItemClickListener(OnClickListener listener) {
		mListaner = listener;
		return this;
	}

	public MySingleChoiseDialog showButtons(final OnClickListener positiveListener,
			final OnClickListener negativeListener) {

		bOk.setVisibility(View.VISIBLE);
		bOk.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (positiveListener != null)
					positiveListener.onClick(MySingleChoiseDialog.this, mChecked);
				dismiss();
			}
		});

		bCancel.setVisibility(View.VISIBLE);
		bCancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (negativeListener != null)
					negativeListener.onClick(MySingleChoiseDialog.this, mChecked);
				dismiss();
			}
		});

		return this;
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

	private static class ViewHolder {
		RadioButton radio;
		TextView text;
	}

}
