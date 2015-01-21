package com.pictureit.noambaroz.beauticianapp.data;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

public class Message implements Parcelable {

	private String client_name;
	private String client_adress;
	private String message_timestamp;
	private String image_url;
	private String orderid;
	private String customeruid;
	private String forwho;
	private String date;
	private String location;
	private String comments;
	private ArrayList<TreatmentType> treatments;
	private boolean isclientdeclined;

	public String getCustomeruid() {
		return customeruid;
	}

	public void setCustomeruid(String customeruid) {
		this.customeruid = customeruid;
	}

	public String getForwho() {
		return forwho;
	}

	public void setForwho(String forwho) {
		this.forwho = forwho;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getClientName() {
		return client_name;
	}

	public void setClientName(String client_name) {
		this.client_name = client_name;
	}

	public String getClientAdress() {
		return client_adress;
	}

	public void setClientAdress(String client_adress) {
		this.client_adress = client_adress;
	}

	public String getMessageSentTime() {
		return message_timestamp;
	}

	public void setMessageSentTime(String time) {
		this.message_timestamp = time;
	}

	public String getImageUrl() {
		return image_url;
	}

	public void setImageUrl(String image_url) {
		this.image_url = image_url;
	}

	public String getOrderid() {
		return orderid;
	}

	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}

	public ArrayList<TreatmentType> getTreatments() {
		return treatments;
	}

	public void setTreatments(ArrayList<TreatmentType> treatments) {
		this.treatments = treatments;
	}

	public boolean isMessageDisabled() {
		return isclientdeclined;
	}

	public void setMessageDisabled(boolean isclientdeclined) {
		this.isclientdeclined = isclientdeclined;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(client_name);
		dest.writeString(client_adress);
		dest.writeString(message_timestamp);
		dest.writeString(image_url);
		dest.writeString(orderid);
		dest.writeString(customeruid);
		dest.writeString(forwho);
		dest.writeString(date);
		dest.writeString(location);
		dest.writeString(comments);
		dest.writeTypedList(treatments);
		dest.writeInt(isclientdeclined ? 1 : 0);
	}

	public static final Parcelable.Creator<Message> CREATOR = new Parcelable.Creator<Message>() {
		public Message createFromParcel(Parcel in) {
			return new Message(in);
		}

		public Message[] newArray(int size) {
			return new Message[size];
		}
	};

	private Message(Parcel in) {
		client_name = in.readString();
		client_adress = in.readString();
		message_timestamp = in.readString();
		image_url = in.readString();
		orderid = in.readString();
		customeruid = in.readString();
		forwho = in.readString();
		date = in.readString();
		location = in.readString();
		comments = in.readString();

		treatments = new ArrayList<TreatmentType>();
		in.readTypedList(treatments, TreatmentType.CREATOR);

		isclientdeclined = in.readInt() == 1;
	}
}
