package com.pictureit.noambaroz.beautyapp.data;

public class HistoryObject extends UpcomingTreatment {

	private String treatmenthistory_id;
	private boolean rated;

	public String getTreatmenthistory_id() {
		return treatmenthistory_id;
	}

	public void setTreatmenthistory_id(String treatmenthistory_id) {
		this.treatmenthistory_id = treatmenthistory_id;
	}

	public boolean isRated() {
		return rated;
	}

	public void setRated(boolean rated) {
		this.rated = rated;
	}
}
