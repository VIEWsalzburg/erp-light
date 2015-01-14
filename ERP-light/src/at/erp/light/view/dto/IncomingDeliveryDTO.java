package at.erp.light.view.dto;

import java.util.HashSet;
import java.util.Set;

public class IncomingDeliveryDTO {
	private int incomingDeliveryId;
	private int organisationId;
	private int lastEditorId;
	private int deliveryNr;
	private String date;
	private String comment;
	private String updateTimestamp;
	private Set<IncomingArticleDTO> incomingArticleDTOs = new HashSet<IncomingArticleDTO>(
			0);
	
	public IncomingDeliveryDTO(){}
	
	public IncomingDeliveryDTO(int incomingDeliveryId,
			int organisationId, int lastEditorId,
			int deliveryNr, String date, String comment,
			Set<IncomingArticleDTO> incomingArticleDTOs,
			String updateTimestamp) {
		super();
		this.incomingDeliveryId = incomingDeliveryId;
		this.organisationId = organisationId;
		this.lastEditorId = lastEditorId;
		this.deliveryNr = deliveryNr;
		this.date = date;
		this.comment = comment;
		this.incomingArticleDTOs = incomingArticleDTOs;
		this.updateTimestamp = updateTimestamp;
	}

	public int getIncomingDeliveryId() {
		return incomingDeliveryId;
	}

	public void setIncomingDeliveryId(int incomingDeliveryId) {
		this.incomingDeliveryId = incomingDeliveryId;
	}

	public int getOrganisationId() {
		return organisationId;
	}

	public void setOrganisationId(int organisationId) {
		this.organisationId = organisationId;
	}

	public int getLastEditorId() {
		return lastEditorId;
	}

	public void setLastEditorId(int lastEditorId) {
		this.lastEditorId = lastEditorId;
	}

	public int getDeliveryNr() {
		return deliveryNr;
	}

	public void setDeliveryNr(int deliveryNr) {
		this.deliveryNr = deliveryNr;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Set<IncomingArticleDTO> getIncomingArticleDTOs() {
		return incomingArticleDTOs;
	}

	public void setIncomingArticleDTOs(Set<IncomingArticleDTO> incomingArticleDTOs) {
		this.incomingArticleDTOs = incomingArticleDTOs;
	}

	public String getUpdateTimestamp() {
		return updateTimestamp;
	}

	public void setUpdateTimestamp(String updateTimestamp) {
		this.updateTimestamp = updateTimestamp;
	}
	
	
}
