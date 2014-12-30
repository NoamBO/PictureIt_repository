package com.pictureit.noambaroz.beauticianapp.data;

import java.util.ArrayList;

public class MessageResponse {

	private String beautician_id;
	private String orderid;
	private String date;
	private String place;
	private String price;
	private String comments;
	private ArrayList<TreatmentType> treatments;

	String getBeauticianId() {
		return beautician_id;
	}

	void setBeauticianId(String beautician_id) {
		this.beautician_id = beautician_id;
	}

	String getOrderid() {
		return orderid;
	}

	void setOrderid(String orderid) {
		this.orderid = orderid;
	}

	String getDate() {
		return date;
	}

	void setDate(String date) {
		this.date = date;
	}

	String getPlace() {
		return place;
	}

	void setPlace(String place) {
		this.place = place;
	}

	String getPrice() {
		return price;
	}

	void setPrice(String price) {
		this.price = price;
	}

	String getComments() {
		return comments;
	}

	void setComments(String comments) {
		this.comments = comments;
	}

	ArrayList<TreatmentType> getTreatments() {
		return treatments;
	}

	void setTreatments(ArrayList<TreatmentType> treatments) {
		this.treatments = treatments;
	}
}
