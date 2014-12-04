package com.pictureit.noambaroz.beautyapp.data;

public class ReorderObject extends TreatmentSummary {

	private String orderID;
	private String customerID;
	private String beauticianID;

	public String getOrderID() {
		return orderID;
	}

	public void setOrderID(String orderID) {
		this.orderID = orderID;
	}

	public String getCustomerID() {
		return customerID;
	}

	public void setCustomerID(String customerID) {
		this.customerID = customerID;
	}

	public String getBeauticianID() {
		return beauticianID;
	}

	public void setBeauticianID(String beauticianID) {
		this.beauticianID = beauticianID;
	}
}
