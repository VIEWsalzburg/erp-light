package at.erp.light.view.dto;

import java.util.HashSet;
import java.util.Set;

/**
 * This class is a DTO representation of the entity DeliveryList.<br/>
 * It is mainly used to transmit data between the frontend and backend.
 * @author Matthias Schn�ll
 *
 */
public class DeliveryListDTO {
	private int deliveryListId;
	private int lastEditorId;
	private String name;
	private String date;
	private String comment;
	private String driver;
	private String passenger;
	private String updateTimestamp;
	private int archived;	// indicates the archived status (0: not archvied, 1: archived)
	private Set<OutgoingDeliveryDTO> outgoingDeliveryDTOs = new HashSet<OutgoingDeliveryDTO>(
			0);
	
	public DeliveryListDTO(){}
	
	public DeliveryListDTO(int deliveryListId, int lastEditorId,
			String name, String date, String comment, String driver,
			String passenger, Set<OutgoingDeliveryDTO> outgoingDeliveryDTOs,
			String updateTimestamp, int archived) {
		super();
		this.deliveryListId = deliveryListId;
		this.lastEditorId = lastEditorId;
		this.name = name;
		this.date = date;
		this.comment = comment;
		this.driver = driver;
		this.passenger = passenger;
		this.outgoingDeliveryDTOs = outgoingDeliveryDTOs;
		this.setUpdateTimestamp(updateTimestamp);
		this.setArchived(archived);
	}
	public int getDeliveryListId() {
		return deliveryListId;
	}
	public void setDeliveryListId(int deliveryListId) {
		this.deliveryListId = deliveryListId;
	}
	public int getLastEditorId() {
		return lastEditorId;
	}
	public void setLastEditorId(int lastEditorId) {
		this.lastEditorId = lastEditorId;
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
	public Set<OutgoingDeliveryDTO> getOutgoingDeliveryDTOs() {
		return outgoingDeliveryDTOs;
	}
	public void setOutgoingDeliveryDTOs(
			Set<OutgoingDeliveryDTO> outgoingDeliveryDTOs) {
		this.outgoingDeliveryDTOs = outgoingDeliveryDTOs;
	}

	public String getUpdateTimestamp() {
		return updateTimestamp;
	}

	public void setUpdateTimestamp(String updateTimestamp) {
		this.updateTimestamp = updateTimestamp;
	}

	public int getArchived() {
		return archived;
	}

	public void setArchived(int archived) {
		this.archived = archived;
	}
	
	
}

