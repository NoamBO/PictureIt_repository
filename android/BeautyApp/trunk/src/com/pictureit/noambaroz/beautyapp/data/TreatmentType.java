package com.pictureit.noambaroz.beautyapp.data;

public class TreatmentType {

	private String treatments_id;
	public String name;
	public String description;
	private int amount;

	public TreatmentType() {
	}

	public TreatmentType(String treatments_id, String name, String description, int amount) {
		this.setTreatments_id(treatments_id);
		this.setAmount(amount);
		this.name = name;
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getTreatments_id() {
		return treatments_id;
	}

	public void setTreatments_id(String treatments_id) {
		this.treatments_id = treatments_id;
	}

}
