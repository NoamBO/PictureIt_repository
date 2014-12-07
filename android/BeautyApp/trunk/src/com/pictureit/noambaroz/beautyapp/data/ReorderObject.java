package com.pictureit.noambaroz.beautyapp.data;

public class ReorderObject extends TreatmentSummary {

	private String orderID;
	private String customeruid;
	private String beauticianuid;

	public String getOrderID() {
		return orderID;
	}

	public void setOrderID(String orderID) {
		this.orderID = orderID;
	}

	public String getCustomerID() {
		return customeruid;
	}

	public void setCustomerID(String customerID) {
		this.customeruid = customerID;
	}

	public String getBeauticianID() {
		return beauticianuid;
	}

	public void setBeauticianID(String beauticianID) {
		this.beauticianuid = beauticianID;
	}
}
