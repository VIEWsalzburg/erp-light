package at.erp.light.view.dto;

import java.util.HashSet;
import java.util.Set;

public class OutgoingDeliveryDTO {
	private int outgoingDeliveryId;
	private DeliveryListDTO deliveryListDTO;
	private OrganisationDTO organisationDTO;
	private PersonDTO lastEditorDTO;
	private Integer deliveryNr;
	private String date;
	private String comment;
	private Set<OutgoingArticleDTO> outgoingArticleDTOs = new HashSet<OutgoingArticleDTO>(
			0);
	
	public OutgoingDeliveryDTO(){}
	
	public OutgoingDeliveryDTO(int outgoingDeliveryId,
			DeliveryListDTO deliveryListDTO, OrganisationDTO organisationDTO,
			PersonDTO lastEditorDTO, Integer deliveryNr, String date,
			String comment, Set<OutgoingArticleDTO> outgoingArticleDTOs) {
		super();
		this.outgoingDeliveryId = outgoingDeliveryId;
		this.deliveryListDTO = deliveryListDTO;
		this.organisationDTO = organisationDTO;
		this.lastEditorDTO = lastEditorDTO;
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

	public DeliveryListDTO getDeliveryListDTO() {
		return deliveryListDTO;
	}

	public void setDeliveryListDTO(DeliveryListDTO deliveryListDTO) {
		this.deliveryListDTO = deliveryListDTO;
	}

	public OrganisationDTO getOrganisationDTO() {
		return organisationDTO;
	}

	public void setOrganisationDTO(OrganisationDTO organisationDTO) {
		this.organisationDTO = organisationDTO;
	}

	public PersonDTO getLastEditorDTO() {
		return lastEditorDTO;
	}

	public void setLastEditorDTO(PersonDTO lastEditorDTO) {
		this.lastEditorDTO = lastEditorDTO;
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
