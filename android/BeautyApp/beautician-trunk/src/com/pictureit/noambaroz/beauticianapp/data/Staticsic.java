package com.pictureit.noambaroz.beauticianapp.data;

import java.util.ArrayList;

public class Staticsic {

	private String total_money;
	private String current_year_total_money;
	private String total_treatments;
	private String current_year_total_treatments;
	private ArrayList<MonthlyIncome> prices;

	public static class MonthlyIncome {
		private int month;
		private double price;

		public int getMonth() {
			return month;
		}

		public void setMonth(int month) {
			this.month = month;
		}

		public double getPrice() {
			return price;
		}

		public void setPrice(double price) {
			this.price = price;
		}
	}

	public String getTotal_money() {
		return total_money;
	}

	public void setTotal_money(String total_money) {
		this.total_money = total_money;
	}

	public String getCurrent_year_total_money() {
		return current_year_total_money;
	}

	public void setCurrent_year_total_money(String current_year_total_money) {
		this.current_year_total_money = current_year_total_money;
	}

	public String getTotal_treatments() {
		return total_treatments;
	}

	public void setTotal_treatments(String total_treatments) {
		this.total_treatments = total_treatments;
	}

	public String getCurrent_year_total_treatments() {
		return current_year_total_treatments;
	}

	public void setCurrent_year_total_treatments(String current_year_total_treatments) {
		this.current_year_total_treatments = current_year_total_treatments;
	}

	public ArrayList<MonthlyIncome> getPrices() {
		return prices;
	}

	public void setPrices(ArrayList<MonthlyIncome> prices) {
		this.prices = prices;
	}

}
