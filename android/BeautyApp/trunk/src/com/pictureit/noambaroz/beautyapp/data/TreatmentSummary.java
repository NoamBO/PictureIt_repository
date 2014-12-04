package com.pictureit.noambaroz.beautyapp.data;

import java.util.ArrayList;

public class TreatmentSummary {

	public String forwho;
	public String date;
	public String location;
	public String comments;
	public ArrayList<TreatmentType> treatments;

	public String getForWho() {
		return forwho;
	}

	public TreatmentSummary setForWho(String forWho) {
		this.forwho = forWho;
		return this;
	}

	public String getWhen() {
		return date;
	}

	public TreatmentSummary setWhen(String when) {
		this.date = when;
		return this;
	}

	public String getWhare() {
		return location;
	}

	public TreatmentSummary setWhare(String whare) {
		this.location = whare;
		return this;
	}

	public String getRemarks() {
		return comments;
	}

	public TreatmentSummary setRemarks(String remarks) {
		this.comments = remarks;
		return this;
	}

	public ArrayList<TreatmentType> getTretments() {
		return treatments;
	}

	public TreatmentSummary setTretments(ArrayList<TreatmentType> tretments) {
		this.treatments = tretments;
		return this;
	}

}
