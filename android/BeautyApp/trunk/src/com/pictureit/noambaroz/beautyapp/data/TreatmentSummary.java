package com.pictureit.noambaroz.beautyapp.data;

import java.util.ArrayList;

public class TreatmentSummary {

	public String forWho;
	public String when;
	public String whare;
	public String remarks;
	public ArrayList<TreatmentType> tretments;

	public String getForWho() {
		return forWho;
	}

	public TreatmentSummary setForWho(String forWho) {
		this.forWho = forWho;
		return this;
	}

	public String getWhen() {
		return when;
	}

	public TreatmentSummary setWhen(String when) {
		this.when = when;
		return this;
	}

	public String getWhare() {
		return whare;
	}

	public TreatmentSummary setWhare(String whare) {
		this.whare = whare;
		return this;
	}

	public String getRemarks() {
		return remarks;
	}

	public TreatmentSummary setRemarks(String remarks) {
		this.remarks = remarks;
		return this;
	}

	public ArrayList<TreatmentType> getTretments() {
		return tretments;
	}

	public TreatmentSummary setTretments(ArrayList<TreatmentType> tretments) {
		this.tretments = tretments;
		return this;
	}

}
