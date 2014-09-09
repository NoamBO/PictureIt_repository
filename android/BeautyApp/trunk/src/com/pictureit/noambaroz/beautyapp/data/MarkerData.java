package com.pictureit.noambaroz.beautyapp.data;

public class MarkerData {

	private String beautician_id;
	private double latitude;
	private double longitude;
	private String name;

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBeautician_id() {
		return beautician_id;
	}

	public void setBeautician_id(String beautician_id) {
		this.beautician_id = beautician_id;
	}
}
