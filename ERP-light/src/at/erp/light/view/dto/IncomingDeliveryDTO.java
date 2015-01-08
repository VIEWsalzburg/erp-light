package at.erp.light.view.dto;

import java.util.HashSet;
import java.util.Set;

public class IncomingDeliveryDTO {
	private int incomingDeliveryId;
	private OrganisationDTO organisationDTO;
	private PersonDTO lastEditorDTO;
	private int deliveryNr;
	private String date;
	private String comment;
	private Set<IncomingArticleDTO> incomingArticleDTOs = new HashSet<IncomingArticleDTO>(
			0);
	
	public IncomingDeliveryDTO(){}
	
	public IncomingDeliveryDTO(int incomingDeliveryId,
			OrganisationDTO organisationDTO, PersonDTO lastEditorDTO,
			int deliveryNr, String date, String comment,
			Set<IncomingArticleDTO> incomingArticleDTOs) {
		super();
		this.incomingDeliveryId = incomingDeliveryId;
		this.organisationDTO = organisationDTO;
		this.lastEditorDTO = lastEditorDTO;
		this.deliveryNr = deliveryNr;
		this.date = date;
		this.comment = comment;
		this.incomingArticleDTOs = incomingArticleDTOs;
	}

	public int getIncomingDeliveryId() {
		return incomingDeliveryId;
	}

	public void setIncomingDeliveryId(int incomingDeliveryId) {
		this.incomingDeliveryId = incomingDeliveryId;
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
	
	
}
