package at.erp.light.view.controller.article;


public class ReportCommand {
	private String from;
	private String to;

	private boolean weightPerDeliveringCompany;
	private boolean completeWeightDeliveringCompany;
	private boolean completePriceDeliveringCompany;
	
	private boolean weightPerReceivingCompany;
	private boolean pricePerReceivingCompany;

	public ReportCommand() {
	}

	public ReportCommand(String from, String to,
			boolean weightPerDeliveringCompany,
			boolean completeWeightDeliveringCompany,
			boolean completePriceDeliveringCompany,
			boolean weightPerReceivingCompany, boolean pricePerReceivingCompany) {
		super();
		this.from = from;
		this.to = to;
		this.weightPerDeliveringCompany = weightPerDeliveringCompany;
		this.completeWeightDeliveringCompany = completeWeightDeliveringCompany;
		this.completePriceDeliveringCompany = completePriceDeliveringCompany;
		this.weightPerReceivingCompany = weightPerReceivingCompany;
		this.pricePerReceivingCompany = pricePerReceivingCompany;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public boolean isWeightPerDeliveringCompany() {
		return weightPerDeliveringCompany;
	}

	public void setWeightPerDeliveringCompany(boolean weightPerDeliveringCompany) {
		this.weightPerDeliveringCompany = weightPerDeliveringCompany;
	}

	public boolean isCompleteWeightDeliveringCompany() {
		return completeWeightDeliveringCompany;
	}

	public void setCompleteWeightDeliveringCompany(
			boolean completeWeightDeliveringCompany) {
		this.completeWeightDeliveringCompany = completeWeightDeliveringCompany;
	}

	public boolean isCompletePriceDeliveringCompany() {
		return completePriceDeliveringCompany;
	}

	public void setCompletePriceDeliveringCompany(
			boolean completePriceDeliveringCompany) {
		this.completePriceDeliveringCompany = completePriceDeliveringCompany;
	}

	public boolean isWeightPerReceivingCompany() {
		return weightPerReceivingCompany;
	}

	public void setWeightPerReceivingCompany(boolean weightPerReceivingCompany) {
		this.weightPerReceivingCompany = weightPerReceivingCompany;
	}

	public boolean isPricePerReceivingCompany() {
		return pricePerReceivingCompany;
	}

	public void setPricePerReceivingCompany(boolean pricePerReceivingCompany) {
		this.pricePerReceivingCompany = pricePerReceivingCompany;
	}
	
	
}
