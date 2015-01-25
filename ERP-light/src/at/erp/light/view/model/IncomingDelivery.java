package at.erp.light.view.model;

// Generated 22.11.2014 18:02:50 by Hibernate Tools 3.4.0.CR1

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Formula;

/**
 * IncomingDelivery generated by hbm2java
 */
@Entity
@Table(name = "incoming_delivery", schema = "public")
public class IncomingDelivery implements java.io.Serializable {

	private int incomingDeliveryId;
	private Organisation organisation;
	private Person lastEditor;
	private Integer deliveryNr;
	private Date date;
	private String comment;
	private Date updateTimestamp;
	private Set<IncomingArticle> incomingArticles = new HashSet<IncomingArticle>(
			0);
	private int archived;		// Archived status (1: archived, 0 not archived)
	private int booked;	// status to show if at least one of the articles of the delivery is already booked (booked: 1, not booked: 0)

	public IncomingDelivery() {
	}

	public IncomingDelivery(int incomingDeliveryId, Organisation organisation,
			Person lastEditor, Date date, String comment, Date updateTimestamp, int archived) {
		this.incomingDeliveryId = incomingDeliveryId;
		this.organisation = organisation;
		this.lastEditor = lastEditor;
		this.date = date;
		this.comment = comment;
		this.updateTimestamp = updateTimestamp;
		this.archived = archived;
	}

	public IncomingDelivery(int incomingDeliveryId, Organisation organisation,
			Person lastEditor, Integer deliveryNr, Date date, String comment,
			Date updateTimestamp, int archived,
			Set<IncomingArticle> incomingArticles) {
		this.incomingDeliveryId = incomingDeliveryId;
		this.organisation = organisation;
		this.lastEditor = lastEditor;
		this.deliveryNr = deliveryNr;
		this.date = date;
		this.comment = comment;
		this.incomingArticles = incomingArticles;
		this.updateTimestamp = updateTimestamp;
		this.archived = archived;
	}

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="gen_incoming_delivery_id")
	@SequenceGenerator(name="gen_incoming_delivery_id", sequenceName="incoming_delivery_incoming_delivery_id_seq", allocationSize=1)
	@Column(name = "incoming_delivery_id", unique = true, nullable = false)
	public int getIncomingDeliveryId() {
		return this.incomingDeliveryId;
	}

	public void setIncomingDeliveryId(int incomingDeliveryId) {
		this.incomingDeliveryId = incomingDeliveryId;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "organisation_id", nullable = false)
	public Organisation getOrganisation() {
		return this.organisation;
	}

	public void setOrganisation(Organisation organisation) {
		this.organisation = organisation;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "person_id", nullable = false)
	public Person getLastEditor() {
		return this.lastEditor;
	}

	public void setLastEditor(Person lastEditor) {
		this.lastEditor = lastEditor;
	}

	@Column(name = "delivery_nr")
	public Integer getDeliveryNr() {
		return this.deliveryNr;
	}

	public void setDeliveryNr(Integer deliveryNr) {
		this.deliveryNr = deliveryNr;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "date", nullable = false, length = 13)
	public Date getDate() {
		return this.date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@Column(name = "comment", nullable = false, length = 1000)
	public String getComment() {
		return this.comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
	
	@Temporal(TemporalType.DATE)
	@Column(name = "update_timestamp", nullable = false, length = 13)
	public Date getUpdateTimestamp() {
		return this.updateTimestamp;
	}

	public void setUpdateTimestamp(Date updateTimestamp) {
		this.updateTimestamp = updateTimestamp;
	}

	@Formula("(select CASE "
			+ "WHEN count(*) = 0 THEN 0	"
			+ "WHEN count(*) > 0 THEN 1 "
			+ "END "
			+ "from incoming_delivery id join incoming_article ia ON (id.incoming_delivery_id = ia.incoming_delivery_id) "
			+ "join outgoing_article oa ON (ia.article_id = oa.article_id) where id.incoming_delivery_id = incoming_delivery_id)")
	public int getBooked() {
		return booked;
	}

	public void setBooked(int booked) {
		this.booked = booked;
	}
	
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "incomingDelivery")						// removed this for unidirectional OneToMany
//	@OneToMany(fetch = FetchType.EAGER)														// added this for unidirectional OneToMany
	@Cascade({CascadeType.ALL})									// add Cascade to Save and Delete incomingArticles automatically
//	@JoinColumn(name="incoming_delivery_id", referencedColumnName="incoming_delivery_id")	// added this for unidirectional OneToMany
	public Set<IncomingArticle> getIncomingArticles() {
		return this.incomingArticles;
	}

	public void setIncomingArticles(Set<IncomingArticle> incomingArticles) {
		this.incomingArticles = incomingArticles;
	}

	@Column(name = "archived", nullable = false)
	public int getArchived() {
		return archived;
	}

	public void setArchived(int archived) {
		this.archived = archived;
	}



}
