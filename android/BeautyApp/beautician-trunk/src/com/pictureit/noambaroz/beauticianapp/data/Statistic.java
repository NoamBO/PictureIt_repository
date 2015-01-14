package com.pictureit.noambaroz.beauticianapp.data;

import java.util.ArrayList;

public class Statistic {

	private String total_money;
	private String current_year_total_money;
	private String total_treatments;
	private String current_year_total_treatments;
	private ArrayList<MonthlyIncome> statistics;

	public static class MonthlyIncome {
		private int month;
		private double value;

		public int getMonth() {
			return month;
		}

		public void setMonth(int month) {
			this.month = month;
		}

		public double getValue() {
			return value;
		}

		public void setValue(double value) {
			this.value = value;
		}
	}

	public String getTotalMoney() {
		return total_money;
	}

	public void setTotalMoney(String total_money) {
		this.total_money = total_money;
	}

	public String getCurrentYearTotalMoney() {
		return current_year_total_money;
	}

	public void setCurrentYearTotalMoney(String current_year_total_money) {
		this.current_year_total_money = current_year_total_money;
	}

	public String getTotalTreatments() {
		return total_treatments;
	}

	public void setTotalTreatments(String total_treatments) {
		this.total_treatments = total_treatments;
	}

	public String getCurrentYearTotalTreatments() {
		return current_year_total_treatments;
	}

	public void setCurrentYearTotalTreatments(String current_year_total_treatments) {
		this.current_year_total_treatments = current_year_total_treatments;
	}

	public ArrayList<MonthlyIncome> getMonthsValues() {
		return statistics;
	}

	public void setMonthsValues(ArrayList<MonthlyIncome> statistics) {
		this.statistics = statistics;
	}

}
