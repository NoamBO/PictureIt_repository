package com.pictureit.noambaroz.beauticianapp;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.app.Fragment;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pictureit.noambaroz.beauticianapp.data.Staticsic;
import com.pictureit.noambaroz.beauticianapp.data.Staticsic.MonthlyIncome;
import com.pictureit.noambaroz.beauticianapp.dialog.Dialogs;
import com.pictureit.noambaroz.beauticianapp.server.GetStatistics;
import com.pictureit.noambaroz.beauticianapp.server.HttpBase.HttpCallback;
import com.pictureit.noambaroz.beauticianapp.utilities.PixelsConverter;
import com.pictureit.noambaroz.beauticianapp.utilities.view.HorizontalListView;

public class ActivityStatistics extends ActivityWithFragment {

	@Override
	protected void initActivity() {
	}

	@Override
	protected void setFragment() {
		fragment = new FragmentStatistics();
	}

	@Override
	protected void setFragmentTag() {
		FRAGMENT_TAG = "fragment_statistics";
	}

	private class FragmentStatistics extends Fragment {

		private HttpCallback priceStatisticsCallback = new HttpCallback() {

			@Override
			public void onAnswerReturn(Object answer) {
				if (answer == null || answer instanceof Integer) {
					Dialogs.showServerFailedDialog(getActivity());
					return;
				}
				mStatistic = (Staticsic) answer;
				updateUi();
			}
		};

		private Staticsic mStatistic;

		private HorizontalListView listview;
		private TextView tvTotalSum;
		private TextView tvMiddleSum;

		private double highest;
		private int[] colHeight;
		private Double[] prices;
		private String[] datelabel;

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View v = inflater.inflate(R.layout.fragment_statistics, container, false);
			listview = findView(v, R.id.chart_listview);
			tvTotalSum = findView(v, R.id.tv_statistics_max_value);
			tvMiddleSum = findView(v, R.id.tv_statistics_medium_value);
			new GetStatistics(getActivity(), priceStatisticsCallback).execute();
			return v;
		}

		private void updateUi() {
			prices = new Double[mStatistic.getPrices().size()];
			datelabel = new String[mStatistic.getPrices().size()];
			for (int i = 0; i < mStatistic.getPrices().size(); i++) {
				MonthlyIncome m = mStatistic.getPrices().get(i);
				prices[i] = m.getPrice();
				datelabel[i] = getMonthForInteger(m.getMonth());
			}

			List<Double> b = Arrays.asList(prices);
			highest = (Collections.max(b));
			tvTotalSum.setText(String.valueOf(highest));
			tvMiddleSum.setText(String.valueOf((int) highest / 2));

			colHeight = new int[prices.length];

			updateSizeInfo();
		}

		private void updateSizeInfo() {

			/**
			 * This is onWindowFocusChanged method is used to allow the chart to
			 * update the chart according to the orientation. Here h is the
			 * integer value which can be updated with the orientation
			 */
			int h = 200;
			if (getResources().getConfiguration().orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
				h = (int) (listview.getHeight());
				if (h == 0) {
					h = 200;
				}
			}
			// else {
			// h = (int) (listview.getHeight());
			// if (h == 0) {
			// h = 130;
			// }
			// }
			for (int i = 0; i < colHeight.length; i++) {
				colHeight[i] = (int) ((h * prices[i]) / highest);
			}
			listview.setAdapter(new ChartAdapter(getActivity(), datelabel, prices, colHeight));
		}

		private String getMonthForInteger(int num) {
			String month = "";
			switch (num) {
			case 1:
				month = getString(R.string.january);
				break;
			case 2:
				month = getString(R.string.february);
				break;
			case 3:
				month = getString(R.string.march);
				break;
			case 4:
				month = getString(R.string.april);
				break;
			case 5:
				month = getString(R.string.may);
				break;
			case 6:
				month = getString(R.string.june);
				break;
			case 7:
				month = getString(R.string.july);
				break;
			case 8:
				month = getString(R.string.august);
				break;
			case 9:
				month = getString(R.string.september);
				break;
			case 10:
				month = getString(R.string.october);
				break;
			case 11:
				month = getString(R.string.november);
				break;
			case 12:
				month = getString(R.string.december);
				break;
			}
			return month;
		}

		public class ChartAdapter extends BaseAdapter {
			Activity activity;
			private int[] colHeight;
			private Double[] prices;
			private String[] datelabel;

			public ChartAdapter(Activity activity, String[] datelabel, Double[] prices, int[] colHeight) {
				this.activity = activity;
				this.datelabel = datelabel;
				this.prices = prices;
				this.colHeight = colHeight;
			}

			public int getCount() {
				return this.datelabel.length;
			}

			public Object getItem(int position) {
				return this.datelabel[position];
			}

			public long getItemId(int position) {
				return this.datelabel.length;
			}

			public View getView(final int position, View convertView, ViewGroup parent) {
				final ViewHolder holder;
				if (convertView == null) {
					LayoutInflater inflater = LayoutInflater.from(activity);
					convertView = inflater.inflate(R.layout.row_chart_statistic, parent, false);
					holder = new ViewHolder();
					holder.month = findView(convertView, R.id.title);
					holder.col = findView(convertView, R.id.statisticChartCol);
					holder.income = findView(convertView, R.id.text01);
					convertView.setTag(holder);
				} else {
					holder = (ViewHolder) convertView.getTag();
				}
				DecimalFormat df = new DecimalFormat("#.##");

				int colHeight = (int) (this.colHeight[position] - PixelsConverter.convertDpToPixel(30, getActivity()));
				holder.col.getLayoutParams().height = colHeight > 0 ? colHeight : 0;
				holder.month.setText(this.datelabel[position]);
				holder.income.setText(df.format(this.prices[position]));

				return convertView;
			}
		}

	}

	private static class ViewHolder {
		public TextView income;
		public TextView month;
		public View col;
	}

}
