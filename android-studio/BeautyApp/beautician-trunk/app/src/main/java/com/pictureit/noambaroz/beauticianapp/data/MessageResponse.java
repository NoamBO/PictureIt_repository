package com.pictureit.noambaroz.beauticianapp.data;

import java.util.ArrayList;

public class MessageResponse {

	private String beautician_id;
	private String orderid;
	private String date;
	private String hour;
	private String place;
	private String price;
	private String comments;
	private ArrayList<TreatmentType> treatments;

	public String getBeauticianId() {
		return beautician_id;
	}

	public void setBeauticianId(String beautician_id) {
		this.beautician_id = beautician_id;
	}

	public String getOrderid() {
		return orderid;
	}

	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public ArrayList<TreatmentType> getTreatments() {
		return treatments;
	}

	public void setTreatments(ArrayList<TreatmentType> treatments) {
		this.treatments = treatments;
	}

	public String getHour() {
		return hour;
	}

	public void setHour(String hour) {
		this.hour = hour;
	}
}
