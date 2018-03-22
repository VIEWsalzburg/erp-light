package at.erp.light.view.dto;

import java.util.HashSet;
import java.util.Set;

/**
 * This class is a DTO representation of the entity IncomingDelivery.<br/>
 * It is mainly used to transmit data between the frontend and backend.
 * @author Matthias Schnöll
 *
 */
public class IncomingDeliveryDTO {
	private int incomingDeliveryId;
	private int organisationId;
	private int lastEditorId;
	private int deliveryNr;
	private String date;
	private String comment;
	private String updateTimestamp;
	private int booked;		// indicates, if at least one article from this delivery has been booked
	private int bookedAll;	// number to show how many of the articles of a given incoming_delivery are already booked to outgoing_deliveries (all articles booked: 0, already unbooked articles: > 0)
	private int archived;	// indicates the archived Status (0: not archived, 1: archived)
	private Set<IncomingArticleDTO> incomingArticleDTOs = new HashSet<IncomingArticleDTO>(0);
	
	/**
	 * Constructor
	 */
	public IncomingDeliveryDTO(){}
	
	/**
	 * Constructor
	 * @param incomingDeliveryId Id of the incoming delivery
	 * @param organisationId Id of the delivering organisation
	 * @param lastEditorId Id of the last editor
	 * @param deliveryNr Order number of the incoming delivery; not used
	 * @param date Delivering date of the incoming delivery
	 * @param comment Comment
	 * @param incomingArticleDTOs IncomingArticles within the incoming delivery
	 * @param updateTimestamp UpdateTimestamp of the incoming delivery
	 * @param archived Archived status of the incoming delivery (0=unarchived, 1=archived)
	 * @param booked Booked status of the incoming delivery (0=not booked, 1=booked)
	 * @param bookedAll difference of unbooked Articles between a given incoming_delivery and outgoing_deliveries (0= all articles are booked, > 0 = there are unbooked articles)
	 */
	public IncomingDeliveryDTO(int incomingDeliveryId,
			int organisationId, int lastEditorId,
			int deliveryNr, String date, String comment,
			Set<IncomingArticleDTO> incomingArticleDTOs,
			String updateTimestamp, int archived, int booked,int bookedAll) {
		super();
		this.incomingDeliveryId = incomingDeliveryId;
		this.organisationId = organisationId;
		this.lastEditorId = lastEditorId;
		this.deliveryNr = deliveryNr;
		this.date = date;
		this.comment = comment;
		this.incomingArticleDTOs = incomingArticleDTOs;
		this.updateTimestamp = updateTimestamp;
		this.booked = booked;
		this.bookedAll = bookedAll;
		this.archived = archived;
	}

	/**
	 * get the Id of the incoming delivery
	 * @return Id
	 */
	public int getIncomingDeliveryId() {
		return incomingDeliveryId;
	}

	/**
	 * set the Id for the incoming delivery
	 * @param incomingDeliveryId Id
	 */
	public void setIncomingDeliveryId(int incomingDeliveryId) {
		this.incomingDeliveryId = incomingDeliveryId;
	}

	/**
	 * get the deliverer Id of the incoming delivery
	 * @return Id
	 */
	public int getOrganisationId() {
		return organisationId;
	}

	/**
	 * set the deliverer Id for the incoming delivery
	 * @param organisationId Id
	 */
	public void setOrganisationId(int organisationId) {
		this.organisationId = organisationId;
	}

	/**
	 * get the Id of the last editor
	 * @return Id
	 */
	public int getLastEditorId() {
		return lastEditorId;
	}

	/**
	 * set last editor Id for the incoming delivery
	 * @param lastEditorId Id
	 */
	public void setLastEditorId(int lastEditorId) {
		this.lastEditorId = lastEditorId;
	}

	/**
	 * get the order number
	 * @return order number
	 */
	public int getDeliveryNr() {
		return deliveryNr;
	}

	/**
	 * set the order number
	 * @param deliveryNr order number
	 */
	public void setDeliveryNr(int deliveryNr) {
		this.deliveryNr = deliveryNr;
	}

	/**
	 * get the delivering date
	 * @return delivering date
	 */
	public String getDate() {
		return date;
	}

	/**
	 * set the delivering date
	 * @param date delivering date
	 */
	public void setDate(String date) {
		this.date = date;
	}

	/**
	 * get the comment of the incoming delivery
	 * @return comment
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * set the comment for the incoming delivery
	 * @param comment comment
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}

	/**
	 * get all associated incomingArticles
	 * @return associated incomingArticles
	 */
	public Set<IncomingArticleDTO> getIncomingArticleDTOs() {
		return incomingArticleDTOs;
	}

	/**
	 * set the associated incomingArticles
	 * @param incomingArticleDTOs associated incomingArticles
	 */
	public void setIncomingArticleDTOs(Set<IncomingArticleDTO> incomingArticleDTOs) {
		this.incomingArticleDTOs = incomingArticleDTOs;
	}

	/**
	 * get the UpdateTimestamp
	 * @return UpdateTimestamp
	 */
	public String getUpdateTimestamp() {
		return updateTimestamp;
	}

	/**
	 * set the UpdateTimestamp
	 * @param updateTimestamp UpdateTimestamp
	 */
	public void setUpdateTimestamp(String updateTimestamp) {
		this.updateTimestamp = updateTimestamp;
	}

	/**
	 * get the booked status of the incomingDelivery
	 * @return booked status
	 */
	public int getBooked() {
		return booked;
	}

	/**
	 * set the booked status for the incomingDelivery
	 * @param booked booked status
	 */
	public void setBooked(int booked) {
		this.booked = booked;
	}
	
	/**
	 * get the status if all of the articles of the corresponding incoming_delivery are already booked (all articles booked: 0, already unbooked articles: > 0)
	 * @return number of unbooked articles
	 */
	public int getBookedAll() {
		return bookedAll;
	}

	/**
	 * set the status if all of the articles of the corresponding incoming_delivery are already booked (all articles booked: 1, still unbooked articles: 0)
	 * @param bookedAll status of unbooked articles inside the incoming_delivery
	 */
	public void setBookedAll(int bookedAll) {
		this.bookedAll = bookedAll;
	}

	/**
	 * get the archived status for the incomingDelivery
	 * @return archived status
	 */
	public int getArchived() {
		return archived;
	}

	/**
	 * set the archived status for the incomingDelivery
	 * @param archived archived status
	 */
	public void setArchived(int archived) {
		this.archived = archived;
	}
	
	
}
