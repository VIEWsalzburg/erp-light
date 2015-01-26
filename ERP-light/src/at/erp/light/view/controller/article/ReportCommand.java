package at.erp.light.view.controller.article;

import java.util.Date;

public class ReportCommand {
	private Date from;
	private Date to;

	private boolean weightPerDeliveringCompany;
	private boolean completeWeightDeliveringCompany;
	private boolean completePriceDeliveringCompany;
	
	private boolean weightPerReceivingCompany;
	private boolean pricePerReceivingCompany;

	public ReportCommand() {
	}

	public ReportCommand(Date from, Date to,
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

	public Date getFrom() {
		return from;
	}

	public void setFrom(Date from) {
		this.from = from;
	}

	public Date getTo() {
		return to;
	}

	public void setTo(Date to) {
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
