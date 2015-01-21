package com.pictureit.noambaroz.beauticianapp.data;

import java.util.ArrayList;

public class UpcomingTreatment {

	private String upcomingtreatment_id;
	private String image_url;
	private String client_name;
	private String client_address;
	private String client_comments;
	private String client_phone;
	private ArrayList<TreatmentType> treatments;
	private String location;
	private String treatment_date;
	private String treatment_price;
	private boolean isclientdeclined;

	public UpcomingTreatment() {
	}

	public String getUpcomingtreatmentId() {
		return upcomingtreatment_id;
	}

	public void setUpcomingtreatmentId(String upcomingtreatment_id) {
		this.upcomingtreatment_id = upcomingtreatment_id;
	}

	public String getImageUrl() {
		return image_url;
	}

	public void setImageUrl(String image_url) {
		this.image_url = image_url;
	}

	public String getClientName() {
		return client_name;
	}

	public void setClientName(String client_name) {
		this.client_name = client_name;
	}

	public String getClientAddress() {
		return client_address;
	}

	public void setClientAddress(String client_address) {
		this.client_address = client_address;
	}

	public String getClientComments() {
		return client_comments;
	}

	public void setClientComments(String client_comments) {
		this.client_comments = client_comments;
	}

	public String getClientPhone() {
		return client_phone;
	}

	public void setClientPhone(String client_phone) {
		this.client_phone = client_phone;
	}

	public ArrayList<TreatmentType> getTreatments() {
		return treatments;
	}

	public void setTreatments(ArrayList<TreatmentType> treatments) {
		this.treatments = treatments;
	}

	public String getTreatmentLocation() {
		return location;
	}

	public void setTreatmentLocation(String treatment_location) {
		this.location = treatment_location;
	}

	public String getTreatmentDate() {
		return treatment_date;
	}

	public void setTreatmentDate(String treatment_date) {
		this.treatment_date = treatment_date;
	}

	public String getPrice() {
		return treatment_price;
	}

	public void setPrice(String price) {
		this.treatment_price = price;
	}

	public boolean isTreatmentCanceled() {
		return isclientdeclined;
	}

	public void setTreatmentCanceled(boolean isclientdeclined) {
		this.isclientdeclined = isclientdeclined;
	}
}
