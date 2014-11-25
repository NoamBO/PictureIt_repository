package com.pictureit.noambaroz.beautyapp.data;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

public class UpcomingTreatment implements Parcelable {

	private String image_url;
	private String beautician_id;
	private String beautician_name;
	private String beautician_address;
	private int beautician_raters;
	private double beautician_rate;
	private String treatment_date;
	private String treatment_location;
	private String treatment_price;
	private String beautician_nots;
	private String beautician_phone;
	private String unFormattedTreatments;
	private ArrayList<TreatmentType> treatments;

	public String getPic() {
		return image_url;
	}

	public void setPic(String image_url) {
		this.image_url = image_url;
	}

	public String getBeautician_id() {
		return beautician_id;
	}

	public void setBeautician_id(String beautician_id) {
		this.beautician_id = beautician_id;
	}

	public String getBeautician_name() {
		return beautician_name;
	}

	public void setBeautician_name(String beautician_name) {
		this.beautician_name = beautician_name;
	}

	public String getBeautician_address() {
		return beautician_address;
	}

	public void setBeautician_address(String beautician_address) {
		this.beautician_address = beautician_address;
	}

	public int getBeautician_raters() {
		return beautician_raters;
	}

	public void setBeautician_raters(int beautician_raters) {
		this.beautician_raters = beautician_raters;
	}

	public double getBeautician_rate() {
		return beautician_rate;
	}

	public void setBeautician_rate(double beautician_rate) {
		this.beautician_rate = beautician_rate;
	}

	public String getTreatment_date() {
		return treatment_date;
	}

	public void setTreatment_date(String treatment_date) {
		this.treatment_date = treatment_date;
	}

	public String getTreatment_location() {
		return treatment_location;
	}

	public void setTreatment_location(String treatment_location) {
		this.treatment_location = treatment_location;
	}

	public String getBeautician_nots() {
		return beautician_nots;
	}

	public void setBeautician_nots(String beautician_nots) {
		this.beautician_nots = beautician_nots;
	}

	public String getPrice() {
		return treatment_price;
	}

	public void setPrice(String price) {
		this.treatment_price = price;
	}

	public String getPhone() {
		return beautician_phone;
	}

	public void setPhone(String phone) {
		this.beautician_phone = phone;
	}

	public String getUnFormattedTreatments() {
		return unFormattedTreatments;
	}

	public void setUnFormattedTreatments(String unFormattedTreatments) {
		this.unFormattedTreatments = unFormattedTreatments;
	}

	public ArrayList<TreatmentType> getTreatmentsArray() {
		return treatments;
	}

	public void setTreatmentsArray(ArrayList<TreatmentType> treatmentsArray) {
		this.treatments = treatmentsArray;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeString(image_url);
		out.writeString(beautician_id);
		out.writeString(beautician_name);
		out.writeString(beautician_address);

		out.writeInt(beautician_raters);
		out.writeDouble(beautician_rate);

		out.writeString(treatment_date);
		out.writeString(treatment_location);
		out.writeString(beautician_nots);
		out.writeString(treatment_price);
		out.writeString(beautician_phone);

		out.writeTypedList(treatments);
	}

	private UpcomingTreatment(Parcel in) {
		image_url = in.readString();
		beautician_id = in.readString();
		beautician_name = in.readString();
		beautician_address = in.readString();

		beautician_raters = in.readInt();
		beautician_rate = in.readDouble();

		treatment_date = in.readString();
		treatment_location = in.readString();
		beautician_nots = in.readString();
		treatment_price = in.readString();
		beautician_phone = in.readString();

		treatments = new ArrayList<TreatmentType>();
		in.readTypedList(treatments, TreatmentType.CREATOR);
	}

	public static final Parcelable.Creator<UpcomingTreatment> CREATOR = new Parcelable.Creator<UpcomingTreatment>() {
		public UpcomingTreatment createFromParcel(Parcel in) {
			return new UpcomingTreatment(in);
		}

		public UpcomingTreatment[] newArray(int size) {
			return new UpcomingTreatment[size];
		}
	};
}
