package com.pictureit.noambaroz.beauticianapp.data;

import android.os.Parcel;
import android.os.Parcelable;

public class TreatmentType implements Parcelable {

	private String treatment_id;
	public String name;
	public String description;
	private String price;
	private int amount;

	public TreatmentType() {
	}

	public TreatmentType(String treatments_id, String name, String description, int amount) {
		this.setTreatmentId(treatments_id);
		this.setAmount(amount);
		this.name = name;
		this.description = description;
	}

	private TreatmentType(Parcel in) {
		treatment_id = in.readString();
		name = in.readString();
		description = in.readString();
		amount = in.readInt();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public String getTreatmentId() {
		return treatment_id;
	}

	public void setTreatmentId(String treatments_id) {
		this.treatment_id = treatments_id;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeString(treatment_id);
		out.writeString(name);
		out.writeString(description);
		out.writeInt(amount);
	}

	public static final Parcelable.Creator<TreatmentType> CREATOR = new Parcelable.Creator<TreatmentType>() {
		public TreatmentType createFromParcel(Parcel in) {
			return new TreatmentType(in);
		}

		public TreatmentType[] newArray(int size) {
			return new TreatmentType[size];
		}
	};

}
