package com.pictureit.noambaroz.beautyapp.data;

import android.os.Parcel;
import android.os.Parcelable;

public class Beautician implements Parcelable {

	private String beautician_id;
	private String photo;
	private String name;
	private String classification;
	private Address address;
	private Rating rating;
	private String[] degrees;
	private String description;
	private String[] treatments;
	private boolean mobility;

	private Beautician(Parcel in) {
		beautician_id = in.readString();
		photo = in.readString();
		name = in.readString();
		classification = in.readString();
		address = (Address) in.readParcelable(Address.class.getClassLoader());
		rating = (Rating) in.readParcelable(Rating.class.getClassLoader());
		degrees = in.createStringArray();
		description = in.readString();
		treatments = in.createStringArray();
		mobility = in.readByte() != 0;
	}

	public Beautician() {
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(beautician_id);
		dest.writeString(photo);
		dest.writeString(name);
		dest.writeString(classification);
		dest.writeParcelable(address, flags);
		dest.writeParcelable(rating, flags);
		dest.writeStringArray(degrees);
		dest.writeString(description);
		dest.writeStringArray(treatments);
		dest.writeByte((byte) (mobility ? 1 : 0));
	}

	public static final Parcelable.Creator<Beautician> CREATOR = new Parcelable.Creator<Beautician>() {
		public Beautician createFromParcel(Parcel in) {
			return new Beautician(in);
		}

		public Beautician[] newArray(int size) {
			return new Beautician[size];
		}
	};

	public String getId() {
		return beautician_id;
	}

	public void setId(String id) {
		this.beautician_id = id;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getClassificationId() {
		return classification;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public Rating getRating() {
		return rating;
	}

	public void setRating(Rating rating) {
		this.rating = rating;
	}

	public String[] getDiplomas() {
		return degrees;
	}

	public void setDiplomas(String[] degrees) {
		this.degrees = degrees;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String[] getTreatments() {
		return treatments;
	}

	public void setTreatments(String[] treatments) {
		this.treatments = treatments;
	}

	public boolean isMobility() {
		return mobility;
	}

	public void setMobility(boolean mobility) {
		this.mobility = mobility;
	}

	public static class Rating implements Parcelable {
		private int ratersCounter;
		private double rateCalculated;

		public Rating() {
		}

		public int getRaters() {
			return ratersCounter;
		}

		public void setRaters(int raters) {
			this.ratersCounter = raters;
		}

		public double getRate() {
			return rateCalculated;
		}

		public void setRate(double rate) {
			this.rateCalculated = rate;
		}

		private Rating(Parcel in) {
			ratersCounter = in.readInt();
			rateCalculated = in.readDouble();
		}

		@Override
		public int describeContents() {
			return 0;
		}

		@Override
		public void writeToParcel(Parcel dest, int flags) {
			dest.writeInt(ratersCounter);
			dest.writeDouble(rateCalculated);
		}

		public static final Parcelable.Creator<Rating> CREATOR = new Parcelable.Creator<Rating>() {
			public Rating createFromParcel(Parcel in) {
				return new Rating(in);
			}

			public Rating[] newArray(int size) {
				return new Rating[size];
			}
		};

	}

	public static class Address implements Parcelable {
		private String street;
		private String city;

		public Address() {
		}

		public String getStreet() {
			return street;
		}

		public void setStreet(String street) {
			this.street = street;
		}

		public String getCity() {
			return city;
		}

		public void setCity(String city) {
			this.city = city;
		}

		@Override
		public int describeContents() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public void writeToParcel(Parcel dest, int flags) {
			dest.writeString(street);
			dest.writeString(city);
		}

		private Address(Parcel in) {
			street = in.readString();
			city = in.readString();
		}

		public static final Parcelable.Creator<Address> CREATOR = new Parcelable.Creator<Address>() {
			public Address createFromParcel(Parcel in) {
				return new Address(in);
			}

			public Address[] newArray(int size) {
				return new Address[size];
			}
		};
	}

}
