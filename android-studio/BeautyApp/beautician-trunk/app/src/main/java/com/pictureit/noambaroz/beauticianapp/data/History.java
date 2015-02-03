package com.pictureit.noambaroz.beauticianapp.data;

import java.util.ArrayList;

public class History {

	private String historytreatment_id;
	private String treatment_date;
	private String client_name;
	private ArrayList<TreatmentType> treatments;
	private String client_address;
	private String treatment_price;

	public String getDate() {
		return treatment_date;
	}

	public void setDate(String date) {
		this.treatment_date = date;
	}

	public String getClientName() {
		return client_name;
	}

	public void setClientName(String private_name) {
		this.client_name = private_name;
	}

	public ArrayList<TreatmentType> getTreatments() {
		return treatments;
	}

	public void setTreatments(ArrayList<TreatmentType> treatments) {
		this.treatments = treatments;
	}

	public String getAddress() {
		return client_address;
	}

	public void setAddress(String treatment_address) {
		this.client_address = treatment_address;
	}

	public String getPrice() {
		return treatment_price;
	}

	public void setPrice(String price) {
		this.treatment_price = price;
	}

}
