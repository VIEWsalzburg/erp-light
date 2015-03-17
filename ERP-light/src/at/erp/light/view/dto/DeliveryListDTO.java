package at.erp.light.view.dto;

import java.util.HashSet;
import java.util.Set;

/**
 * This class is a DTO representation of the entity DeliveryList.<br/>
 * It is mainly used to transmit data between the frontend and backend.
 * @author Matthias Schnöll
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
	
	/**
	 * Constructor
	 */
	public DeliveryListDTO(){}
	
	/**
	 * Constructor
	 * @param deliveryListId Id of the delivery list
	 * @param lastEditorId Id of the last editor
	 * @param name Name of the delivery list
	 * @param date Delivery date of the delivery list
	 * @param comment Comment of the delivery list
	 * @param driver Name of the driver
	 * @param passenger Name of the passenger
	 * @param outgoingDeliveryDTOs List of the assigned outgoing deliveries
	 * @param updateTimestamp UpdateTimestamp of the delivery list
	 * @param archived Archived state of the delivery list
	 */
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
	
	/**
	 * get Id of the delivery list
	 * @return Id
	 */
	public int getDeliveryListId() {
		return deliveryListId;
	}
	
	/**
	 * set the Id for the delivery list
	 * @param deliveryListId Id
	 */
	public void setDeliveryListId(int deliveryListId) {
		this.deliveryListId = deliveryListId;
	}
	
	/**
	 * get the Id of the last editor
	 * @return Id
	 */
	public int getLastEditorId() {
		return lastEditorId;
	}
	
	/**
	 * set the Id for the last editor
	 * @param lastEditorId Id
	 */
	public void setLastEditorId(int lastEditorId) {
		this.lastEditorId = lastEditorId;
	}
	
	/**
	 * get the name of the delivery list
	 * @return name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * set the name for the deliver list
	 * @param name name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * get the delivery date of the delivery list
	 * @return delivery date
	 */
	public String getDate() {
		return date;
	}
	
	/**
	 * set the delivery date for the delivery list
	 * @param date delivery date
	 */
	public void setDate(String date) {
		this.date = date;
	}
	
	/**
	 * get the comment for the delivery list
	 * @return comment
	 */
	public String getComment() {
		return comment;
	}
	
	/**
	 * set the comment for the delivery list
	 * @param comment comment
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}
	
	/**
	 * get the name of the driver
	 * @return name of the driver
	 */
	public String getDriver() {
		return driver;
	}
	
	/**
	 * set the name of the driver
	 * @param driver name of the driver
	 */
	public void setDriver(String driver) {
		this.driver = driver;
	}
	
	/**
	 * get the name of the passenger
	 * @return name of the passenger
	 */
	public String getPassenger() {
		return passenger;
	}
	
	/**
	 * set the name of the passenger
	 * @param passenger name of the passenger
	 */
	public void setPassenger(String passenger) {
		this.passenger = passenger;
	}
	
	/**
	 * get the list of assigned outgoing deliveries
	 * @return list of assigned outgoing deliveries
	 */
	public Set<OutgoingDeliveryDTO> getOutgoingDeliveryDTOs() {
		return outgoingDeliveryDTOs;
	}
	
	/**
	 * set the list of assigned outgoing deliveries
	 * @param outgoingDeliveryDTOs list of assigned outgoing deliveries
	 */
	public void setOutgoingDeliveryDTOs(
			Set<OutgoingDeliveryDTO> outgoingDeliveryDTOs) {
		this.outgoingDeliveryDTOs = outgoingDeliveryDTOs;
	}

	/**
	 * get the update timestamp of the delivery list 
	 * @return update timestamp
	 */
	public String getUpdateTimestamp() {
		return updateTimestamp;
	}

	/**
	 * set the update timestamp for the delivery list
	 * @param updateTimestamp update timestamp
	 */
	public void setUpdateTimestamp(String updateTimestamp) {
		this.updateTimestamp = updateTimestamp;
	}

	/**
	 * get the archived status of the delivery list
	 * @return archived
	 */
	public int getArchived() {
		return archived;
	}

	/**
	 * set the archived status for delivery list
	 * @param archived archived status
	 */
	public void setArchived(int archived) {
		this.archived = archived;
	}
	
	
}

