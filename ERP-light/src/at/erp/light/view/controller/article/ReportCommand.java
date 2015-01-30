package at.erp.light.view.controller.article;


public class ReportCommand {
	private String dateFrom;
	private String dateTo;

	private boolean weightPerDeliveringOrganisation;
	private boolean totalWeightAllDeliveringOrganisations;	// total weight of delivered articles of all organisations
	private boolean totalPriceAllDeliveringOrganisations;	// total price of delivered articles of all organisations
	
	private boolean weightPerReceivingOrganisation;
	private boolean pricePerReceivingOrganisation;

	public ReportCommand() {
	}

	public ReportCommand(String from, String to,
			boolean weightPerDeliveringCompany,
			boolean completeWeightDeliveringCompany,
			boolean completePriceDeliveringCompany,
			boolean weightPerReceivingCompany, boolean pricePerReceivingCompany) {
		super();
		this.dateFrom = from;
		this.dateTo = to;
		this.weightPerDeliveringOrganisation = weightPerDeliveringCompany;
		this.totalWeightAllDeliveringOrganisations = completeWeightDeliveringCompany;
		this.totalPriceAllDeliveringOrganisations = completePriceDeliveringCompany;
		this.weightPerReceivingOrganisation = weightPerReceivingCompany;
		this.pricePerReceivingOrganisation = pricePerReceivingCompany;
	}

	public String getFrom() {
		return dateFrom;
	}

	public void setFrom(String from) {
		this.dateFrom = from;
	}

	public String getTo() {
		return dateTo;
	}

	public void setTo(String to) {
		this.dateTo = to;
	}

	public boolean isWeightPerDeliveringCompany() {
		return weightPerDeliveringOrganisation;
	}

	public void setWeightPerDeliveringCompany(boolean weightPerDeliveringCompany) {
		this.weightPerDeliveringOrganisation = weightPerDeliveringCompany;
	}

	public boolean isCompleteWeightDeliveringCompany() {
		return totalWeightAllDeliveringOrganisations;
	}

	public void setCompleteWeightDeliveringCompany(
			boolean completeWeightDeliveringCompany) {
		this.totalWeightAllDeliveringOrganisations = completeWeightDeliveringCompany;
	}

	public boolean isCompletePriceDeliveringCompany() {
		return totalPriceAllDeliveringOrganisations;
	}

	public void setCompletePriceDeliveringCompany(
			boolean completePriceDeliveringCompany) {
		this.totalPriceAllDeliveringOrganisations = completePriceDeliveringCompany;
	}

	public boolean isWeightPerReceivingCompany() {
		return weightPerReceivingOrganisation;
	}

	public void setWeightPerReceivingCompany(boolean weightPerReceivingCompany) {
		this.weightPerReceivingOrganisation = weightPerReceivingCompany;
	}

	public boolean isPricePerReceivingCompany() {
		return pricePerReceivingOrganisation;
	}

	public void setPricePerReceivingCompany(boolean pricePerReceivingCompany) {
		this.pricePerReceivingOrganisation = pricePerReceivingCompany;
	}
	
	
}
