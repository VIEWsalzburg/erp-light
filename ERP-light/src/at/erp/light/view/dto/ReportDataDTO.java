package at.erp.light.view.dto;

import java.util.Date;

public class ReportDataDTO {
	
	private String dateFrom;
	private String dateTo;
	
	private String organisationName;
	private int organisationId;
	
	private double totalWeight;
	private double totalPrice;
	
	public ReportDataDTO() {
	}

	
	
	public ReportDataDTO(String dateFrom, String dateTo, double totalWeight,
			double totalPrice) {
		super();
		this.dateFrom = dateFrom;
		this.dateTo = dateTo;
		this.totalWeight = totalWeight;
		this.totalPrice = totalPrice;
	}



	public ReportDataDTO(String dateFrom, String dateTo, String organisationName,
			int organisationId, double totalWeight, double totalPrice) {
		super();
		this.dateFrom = dateFrom;
		this.dateTo = dateTo;
		this.organisationName = organisationName;
		this.organisationId = organisationId;
		this.totalWeight = totalWeight;
		this.totalPrice = totalPrice;
	}



	public String getDateFrom() {
		return dateFrom;
	}



	public void setDateFrom(String dateFrom) {
		this.dateFrom = dateFrom;
	}



	public String getDateTo() {
		return dateTo;
	}



	public void setDateTo(String dateTo) {
		this.dateTo = dateTo;
	}



	public String getOrganisationName() {
		return organisationName;
	}



	public void setOrganisationName(String organisationName) {
		this.organisationName = organisationName;
	}



	public int getOrganisationId() {
		return organisationId;
	}



	public void setOrganisationId(int organisationId) {
		this.organisationId = organisationId;
	}



	public double getTotalWeight() {
		return totalWeight;
	}



	public void setTotalWeight(double totalWeight) {
		this.totalWeight = totalWeight;
	}



	public double getTotalPrice() {
		return totalPrice;
	}



	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}
	
}
