package com.pictureit.noambaroz.beauticianapp.data;

public class OrderAroundMe {

	private String private_name;
	private String family_name;
	private double latitude;
	private double longitude;
	private String orderid;
	private boolean is_directed_to_me;

	public String getPrivateName() {
		return private_name;
	}

	public void setPrivateName(String private_name) {
		this.private_name = private_name;
	}

	public String getFamilyName() {
		return family_name;
	}

	public void setFamilyName(String family_name) {
		this.family_name = family_name;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public String getOrderid() {
		return orderid;
	}

	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}

	public boolean isDirectedToMe() {
		return is_directed_to_me;
	}

	public void setDirectedToMe(boolean is_directed_to_me) {
		this.is_directed_to_me = is_directed_to_me;
	}
}
