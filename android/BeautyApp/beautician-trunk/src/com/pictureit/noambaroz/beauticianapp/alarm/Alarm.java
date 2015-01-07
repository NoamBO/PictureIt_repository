package com.pictureit.noambaroz.beauticianapp.alarm;

import android.os.Parcel;
import android.os.Parcelable;

public class Alarm implements Parcelable {

	public int upcomingtreatment_id;
	public long treatment_time;
	public String full_name;
	public String treatment;
	public String image_url;
	public String address;
	public int isPlayed = 0;

	public Alarm() {
	}

	private Alarm(Parcel in) {
		upcomingtreatment_id = in.readInt();
		treatment_time = in.readLong();
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
		dest.writeLong(treatment_time);
		dest.writeString(full_name);
		dest.writeString(treatment);
		dest.writeString(image_url);
		dest.writeString(address);
		dest.writeInt(isPlayed);
	}

}
