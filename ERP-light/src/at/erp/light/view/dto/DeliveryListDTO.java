package at.erp.light.view.dto;

import java.util.HashSet;
import java.util.Set;

public class DeliveryListDTO {
	private int deliveryListId;
	private PersonDTO lastEditor;
	private String name;
	private String date;
	private String comment;
	private String driver;
	private String passenger;
	private Set<OutgoingDeliveryDTO> outgoingDeliverieDTOs = new HashSet<OutgoingDeliveryDTO>(
			0);
	
	public DeliveryListDTO(){}
	
	public DeliveryListDTO(int deliveryListId, PersonDTO lastEditor,
			String name, String date, String comment, String driver,
			String passenger, Set<OutgoingDeliveryDTO> outgoingDeliverieDTOs) {
		super();
		this.deliveryListId = deliveryListId;
		this.lastEditor = lastEditor;
		this.name = name;
		this.date = date;
		this.comment = comment;
		this.driver = driver;
		this.passenger = passenger;
		this.outgoingDeliverieDTOs = outgoingDeliverieDTOs;
	}
	public int getDeliveryListId() {
		return deliveryListId;
	}
	public void setDeliveryListId(int deliveryListId) {
		this.deliveryListId = deliveryListId;
	}
	public PersonDTO getLastEditor() {
		return lastEditor;
	}
	public void setLastEditor(PersonDTO lastEditor) {
		this.lastEditor = lastEditor;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public String getDriver() {
		return driver;
	}
	public void setDriver(String driver) {
		this.driver = driver;
	}
	public String getPassenger() {
		return passenger;
	}
	public void setPassenger(String passenger) {
		this.passenger = passenger;
	}
	public Set<OutgoingDeliveryDTO> getOutgoingDeliverieDTOs() {
		return outgoingDeliverieDTOs;
	}
	public void setOutgoingDeliverieDTOs(
			Set<OutgoingDeliveryDTO> outgoingDeliverieDTOs) {
		this.outgoingDeliverieDTOs = outgoingDeliverieDTOs;
	}
	
	
}

