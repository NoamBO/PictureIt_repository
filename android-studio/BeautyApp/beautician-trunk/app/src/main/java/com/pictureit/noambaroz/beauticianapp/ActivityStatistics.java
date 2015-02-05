package com.pictureit.noambaroz.beauticianapp;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.app.Fragment;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pictureit.noambaroz.beauticianapp.data.Statistic;
import com.pictureit.noambaroz.beauticianapp.data.Statistic.MonthlyIncome;
import com.pictureit.noambaroz.beauticianapp.dialog.Dialogs;
import com.pictureit.noambaroz.beauticianapp.server.GetStatistics;
import com.pictureit.noambaroz.beauticianapp.server.GetStatistics.StatisticType;
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

	public static class FragmentStatistics extends BaseFragment {

		private HttpCallback taskCallback = new HttpCallback() {

			@Override
			public void onAnswerReturn(Object answer) {
				if (answer == null || answer instanceof Integer) {
					Dialogs.showServerFailedDialog(getActivity());
					return;
				}
				mStatistic = (Statistic) answer;
				updateUi();
			}
		};

		private Statistic mStatistic;

		private HorizontalListView listview;
		private TextView tvTotalSum;
		private TextView tvMiddleSum;

		private AutoCompleteTextView tvStatisticType;
		private TextView tvGeneralDetails;

		private boolean generalDetailsSet;

		private double highest;
		private int[] columnHeight;
		private Double[] columnValues;
		private String[] datelabel;

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View v = inflater.inflate(R.layout.fragment_statistics, container, false);
			listview = findView(v, R.id.chart_listview);
			tvTotalSum = findView(v, R.id.tv_statistics_max_value);
			tvMiddleSum = findView(v, R.id.tv_statistics_medium_value);
			tvGeneralDetails = findView(v, R.id.tv_statistics_summary);
			tvStatisticType = findView(v, R.id.tv_statistics_title);
			setTitleClick();
			new GetStatistics(getActivity(), taskCallback, StatisticType.PricesStatistics).execute();
			return v;
		}

		private void setTitleClick() {
			String[] s = new String[] { getString(R.string.income), getString(R.string.treatments) };
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.simple_textview, s);
			tvStatisticType.setAdapter(adapter);
			tvStatisticType.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					tvStatisticType.showDropDown();
				}
			});
			tvStatisticType.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					StatisticType st;
					switch (position) {
					case 0:
						st = StatisticType.PricesStatistics;
						break;
					case 1:
						st = StatisticType.TreatmentStatistics;
						break;
					default:
						st = StatisticType.PricesStatistics;
						break;
					}
					new GetStatistics(getActivity(), taskCallback, st).execute();
				}
			});
		}

		private void updateUi() {
			columnValues = new Double[mStatistic.getMonthsValues().size()];
			datelabel = new String[mStatistic.getMonthsValues().size()];
			for (int i = 0; i < mStatistic.getMonthsValues().size(); i++) {
				MonthlyIncome m = mStatistic.getMonthsValues().get(i);
				columnValues[i] = m.getValue();
				datelabel[i] = getMonthForInteger(m.getMonth());
			}

			List<Double> b = Arrays.asList(columnValues);
			highest = (Collections.max(b));
			tvTotalSum.setText(String.valueOf(highest));
			tvMiddleSum.setText(String.valueOf((int) highest / 2));

			columnHeight = new int[columnValues.length];

			updateSizeInfo();
			setGeneralDetailsText();
		}

		private void setGeneralDetailsText() {
			if (!generalDetailsSet) {
				Calendar c = Calendar.getInstance();
				int year = c.get(Calendar.YEAR);
				StringBuilder sb = new StringBuilder();
				sb.append(getString(R.string.total_income)).append(" ").append(mStatistic.getTotalMoney()).append(" ")
						.append(getString(R.string.currency)).append("\n");

				sb.append(getString(R.string.total_income_at_current_year)).append(" ").append(String.valueOf(year))
						.append(": ").append(mStatistic.getCurrentYearTotalMoney()).append(" ")
						.append(getString(R.string.currency)).append("\n");

				sb.append(getString(R.string.total_treatments)).append(" ").append(mStatistic.getTotalTreatments())
						.append("\n");

				sb.append(getString(R.string.total_treatments_at_current_year)).append(" ")
						.append(String.valueOf(year)).append(": ").append(mStatistic.getCurrentYearTotalTreatments());

				tvGeneralDetails.setText(sb.toString());
				generalDetailsSet = true;
			}
		}

		private void updateSizeInfo() {

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
			for (int i = 0; i < columnHeight.length; i++) {
				columnHeight[i] = (int) ((h * columnValues[i]) / highest);
			}
			listview.setAdapter(new ChartAdapter(getActivity(), datelabel, columnValues, columnHeight));
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
			private Double[] values;
			private String[] datelabel;

			public ChartAdapter(Activity activity, String[] datelabel, Double[] values, int[] colHeight) {
				this.activity = activity;
				this.datelabel = datelabel;
				this.values = values;
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
				holder.income.setText(df.format(this.values[position]));

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
