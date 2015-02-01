package at.erp.light.view.controller.article;


public class ReportCommand {
	private Integer organisationId;
	
	private String dateFrom;
	private String dateTo;

	private boolean incomingReportByOrganisationId;
	private boolean incomingReportForAllOrganisations;
	
	private boolean outgoingReportByOrganisationId;
	private boolean outgoingReportForAllOrganisations;
	
	private boolean totalSumOfAllIncomingDeliveries;
	private boolean totalSumOfAllOutgoingDeliveries;

	public ReportCommand() {
	}

	public ReportCommand(Integer organisationId, String dateFrom, String dateTo,
			boolean incomingReportByOrganisationId,
			boolean incomingReportForAllOrganisations,
			boolean outgoingReportByOrganisationId,
			boolean outgoingReportForAllOrganisations,
			boolean totalSumOfAllIncomingDeliveries,
			boolean totalSumOfAllOutgoingDeliveries) {
		super();
		this.organisationId = organisationId;
		this.dateFrom = dateFrom;
		this.dateTo = dateTo;
		this.incomingReportByOrganisationId = incomingReportByOrganisationId;
		this.incomingReportForAllOrganisations = incomingReportForAllOrganisations;
		this.outgoingReportByOrganisationId = outgoingReportByOrganisationId;
		this.outgoingReportForAllOrganisations = outgoingReportForAllOrganisations;
		this.totalSumOfAllIncomingDeliveries = totalSumOfAllIncomingDeliveries;
		this.totalSumOfAllOutgoingDeliveries = totalSumOfAllOutgoingDeliveries;
	}

	public Integer getOrganisationId() {
		return organisationId;
	}

	public void setOrganisationId(Integer organisationId) {
		this.organisationId = organisationId;
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

	public boolean isIncomingReportByOrganisationId() {
		return incomingReportByOrganisationId;
	}

	public void setIncomingReportByOrganisationId(
			boolean incomingReportByOrganisationId) {
		this.incomingReportByOrganisationId = incomingReportByOrganisationId;
	}

	public boolean isIncomingReportForAllOrganisations() {
		return incomingReportForAllOrganisations;
	}

	public void setIncomingReportForAllOrganisations(
			boolean incomingReportForAllOrganisations) {
		this.incomingReportForAllOrganisations = incomingReportForAllOrganisations;
	}

	public boolean isOutgoingReportByOrganisationId() {
		return outgoingReportByOrganisationId;
	}

	public void setOutgoingReportByOrganisationId(
			boolean outgoingReportByOrganisationId) {
		this.outgoingReportByOrganisationId = outgoingReportByOrganisationId;
	}

	public boolean isOutgoingReportForAllOrganisations() {
		return outgoingReportForAllOrganisations;
	}

	public void setOutgoingReportForAllOrganisations(
			boolean outgoingReportForAllOrganisations) {
		this.outgoingReportForAllOrganisations = outgoingReportForAllOrganisations;
	}

	public boolean isTotalSumOfAllIncomingDeliveries() {
		return totalSumOfAllIncomingDeliveries;
	}

	public void setTotalSumOfAllIncomingDeliveries(
			boolean totalSumOfAllIncomingDeliveries) {
		this.totalSumOfAllIncomingDeliveries = totalSumOfAllIncomingDeliveries;
	}

	public boolean isTotalSumOfAllOutgoingDeliveries() {
		return totalSumOfAllOutgoingDeliveries;
	}

	public void setTotalSumOfAllOutgoingDeliveries(
			boolean totalSumOfAllOutgoingDeliveries) {
		this.totalSumOfAllOutgoingDeliveries = totalSumOfAllOutgoingDeliveries;
	}
}
