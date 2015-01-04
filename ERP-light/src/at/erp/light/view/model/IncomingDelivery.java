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
	private Set<IncomingArticle> incomingArticles = new HashSet<IncomingArticle>(
			0);

	public IncomingDelivery() {
	}

	public IncomingDelivery(int incomingDeliveryId, Organisation organisation,
			Person lastEditor, Date date, String comment) {
		this.incomingDeliveryId = incomingDeliveryId;
		this.organisation = organisation;
		this.lastEditor = lastEditor;
		this.date = date;
		this.comment = comment;
	}

	public IncomingDelivery(int incomingDeliveryId, Organisation organisation,
			Person lastEditor, Integer deliveryNr, Date date, String comment,
			Set<IncomingArticle> incomingArticles) {
		this.incomingDeliveryId = incomingDeliveryId;
		this.organisation = organisation;
		this.lastEditor = lastEditor;
		this.deliveryNr = deliveryNr;
		this.date = date;
		this.comment = comment;
		this.incomingArticles = incomingArticles;
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

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "incomingDelivery")						// removed this for unidirectional OneToMany
//	@OneToMany(fetch = FetchType.EAGER)														// added this for unidirectional OneToMany
	@Cascade({CascadeType.ALL})									// add Cascade to Save and Delete incomingArticles automatically
//	@JoinColumn(name="incoming_delivery_id", referencedColumnName="incoming_delivery_id")	// added this for unidirectional OneToMany
	public Set<IncomingArticle> getIncomingArticles() {
		return this.incomingArticles;
	}

	public void setIncomingArticles(Set<IncomingArticle> incomingArticles) {
		this.incomingArticles = incomingArticles;
	}

}
