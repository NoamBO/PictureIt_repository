package com.pictureit.noambaroz.beauticianapp.alarm;

import android.os.Parcel;
import android.os.Parcelable;

public class Alarm implements Parcelable {

	public int id;
	public long treatmentTime;
	public String customer_name;
	public String treatment;
	public String imageUrl;
	public String address;

	public Alarm() {
	}

	private Alarm(Parcel in) {
		id = in.readInt();
		treatmentTime = in.readLong();
		customer_name = in.readString();
		treatment = in.readString();
		imageUrl = in.readString();
		address = in.readString();
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
		dest.writeInt(id);
		dest.writeLong(treatmentTime);
		dest.writeString(customer_name);
		dest.writeString(treatment);
		dest.writeString(imageUrl);
		dest.writeString(address);
	}

}
