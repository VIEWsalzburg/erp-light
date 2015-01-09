package at.erp.light.view.dto;

import java.util.HashSet;
import java.util.Set;

public class OutgoingDeliveryDTO {
	private int outgoingDeliveryId;
	private int organisationId;
	private int lastEditorId;
	private Integer deliveryNr;
	private String date;
	private String comment;
	private Set<OutgoingArticleDTO> outgoingArticleDTOs = new HashSet<OutgoingArticleDTO>(
			0);
	
	public OutgoingDeliveryDTO(){}
	
	public OutgoingDeliveryDTO(int outgoingDeliveryId,
			int organisationId,
			int lastEditorId, Integer deliveryNr, String date,
			String comment, Set<OutgoingArticleDTO> outgoingArticleDTOs) {
		super();
		this.outgoingDeliveryId = outgoingDeliveryId;
		this.organisationId = organisationId;
		this.lastEditorId = lastEditorId;
		this.deliveryNr = deliveryNr;
		this.date = date;
		this.comment = comment;
		this.outgoingArticleDTOs = outgoingArticleDTOs;
	}

	public int getOutgoingDeliveryId() {
		return outgoingDeliveryId;
	}

	public void setOutgoingDeliveryId(int outgoingDeliveryId) {
		this.outgoingDeliveryId = outgoingDeliveryId;
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

	public Integer getDeliveryNr() {
		return deliveryNr;
	}

	public void setDeliveryNr(Integer deliveryNr) {
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

	public Set<OutgoingArticleDTO> getOutgoingArticleDTOs() {
		return outgoingArticleDTOs;
	}

	public void setOutgoingArticleDTOs(Set<OutgoingArticleDTO> outgoingArticleDTOs) {
		this.outgoingArticleDTOs = outgoingArticleDTOs;
	}
	
}
