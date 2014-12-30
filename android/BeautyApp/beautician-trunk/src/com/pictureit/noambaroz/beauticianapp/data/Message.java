package com.pictureit.noambaroz.beauticianapp.data;

import java.util.ArrayList;

public class Message {

	private String client_name;
	private String client_adress;
	private String time;
	private String image_url;
	private String orderid;
	private ArrayList<TreatmentType> treatments;
	private String customeruid;
	private String forwho;
	private String date;
	private String location;
	private String comments;

	public String getCustomeruid() {
		return customeruid;
	}

	public void setCustomeruid(String customeruid) {
		this.customeruid = customeruid;
	}

	public String getForwho() {
		return forwho;
	}

	public void setForwho(String forwho) {
		this.forwho = forwho;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getClientName() {
		return client_name;
	}

	public void setClientName(String client_name) {
		this.client_name = client_name;
	}

	public String getClientAdress() {
		return client_adress;
	}

	public void setClientAdress(String client_adress) {
		this.client_adress = client_adress;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getImageUrl() {
		return image_url;
	}

	public void setImageUrl(String image_url) {
		this.image_url = image_url;
	}

	public String getOrderid() {
		return orderid;
	}

	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}

	public ArrayList<TreatmentType> getTreatments() {
		return treatments;
	}

	public void setTreatments(ArrayList<TreatmentType> treatments) {
		this.treatments = treatments;
	}

}
