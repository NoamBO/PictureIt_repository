package com.pictureit.noambaroz.beautyapp.data;

public class TreatmentType {

	public String id;
	public String name;
	public String description;
	public int count;

	public TreatmentType() {
	}

	public TreatmentType(String id, String name, String description, int count) {
		this.id = id;
		this.count = count;
		this.name = name;
		this.description = description;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
}
