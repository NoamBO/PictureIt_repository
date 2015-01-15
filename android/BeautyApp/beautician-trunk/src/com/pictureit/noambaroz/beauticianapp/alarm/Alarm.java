package com.pictureit.noambaroz.beauticianapp.alarm;

import android.os.Parcel;
import android.os.Parcelable;

public class Alarm implements Parcelable {

	private int upcomingtreatment_id;
	private String treatment_time;
	private String full_name;
	private String treatment;
	private String image_url;
	private String address;
	private int isPlayed = 0;

	public int getUpcomingtreatment_id() {
		return upcomingtreatment_id;
	}

	public void setUpcomingtreatment_id(int upcomingtreatment_id) {
		this.upcomingtreatment_id = upcomingtreatment_id;
	}

	public long getTreatmentDate() {
		return Long.parseLong(treatment_time);
	}

	public void setTreatmentDate(long treatment_date) {
		this.treatment_time = String.valueOf(treatment_date);
	}

	public String getFullName() {
		return full_name;
	}

	public void setFullName(String full_name) {
		this.full_name = full_name;
	}

	public String getTreatment() {
		return treatment;
	}

	public void setTreatment(String treatment) {
		this.treatment = treatment;
	}

	public String getImageUrl() {
		return image_url;
	}

	public void setImageUrl(String image_url) {
		this.image_url = image_url;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getIsPlayed() {
		return isPlayed;
	}

	public void setIsPlayed(int isPlayed) {
		this.isPlayed = isPlayed;
	}

	public static Parcelable.Creator<Alarm> getCreator() {
		return CREATOR;
	}

	public Alarm() {
	}

	private Alarm(Parcel in) {
		upcomingtreatment_id = in.readInt();
		treatment_time = in.readString();
		full_name = in.readString();
		treatment = in.readString();
		image_url = in.readString();
		address = in.readString();
		isPlayed = in.readInt();
	}

	public static final Parcelable.Creator<Alarm> CREATOR = new Parcelable.Creator<Alarm>() {
		public Alarm createFromParcel(Parcel in) {
			return new Alarm(in);
		}

		public Alarm[] newArray(int size) {
			return new Alarm[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(upcomingtreatment_id);
		dest.writeString(treatment_time);
		dest.writeString(full_name);
		dest.writeString(treatment);
		dest.writeString(image_url);
		dest.writeString(address);
		dest.writeInt(isPlayed);
	}

}
