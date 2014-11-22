package at.erplight.model;

// Generated 22.11.2014 18:02:50 by Hibernate Tools 3.4.0.CR1

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * DeliveryList generated by hbm2java
 */
@Entity
@Table(name = "delivery_list", schema = "public")
public class DeliveryList implements java.io.Serializable {

	private int deliveryListId;
	private Person person;
	private String name;
	private Date date;
	private String comment;
	private String driver;
	private String passenger;
	private Set<OutgoingDelivery> outgoingDeliveries = new HashSet<OutgoingDelivery>(
			0);

	public DeliveryList() {
	}

	public DeliveryList(int deliveryListId, Person person, String name,
			Date date, String comment, String driver, String passenger) {
		this.deliveryListId = deliveryListId;
		this.person = person;
		this.name = name;
		this.date = date;
		this.comment = comment;
		this.driver = driver;
		this.passenger = passenger;
	}

	public DeliveryList(int deliveryListId, Person person, String name,
			Date date, String comment, String driver, String passenger,
			Set<OutgoingDelivery> outgoingDeliveries) {
		this.deliveryListId = deliveryListId;
		this.person = person;
		this.name = name;
		this.date = date;
		this.comment = comment;
		this.driver = driver;
		this.passenger = passenger;
		this.outgoingDeliveries = outgoingDeliveries;
	}

	@Id
	@Column(name = "delivery_list_id", unique = true, nullable = false)
	public int getDeliveryListId() {
		return this.deliveryListId;
	}

	public void setDeliveryListId(int deliveryListId) {
		this.deliveryListId = deliveryListId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "person_id", nullable = false)
	public Person getPerson() {
		return this.person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	@Column(name = "name", nullable = false, length = 100)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
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

	@Column(name = "driver", nullable = false, length = 100)
	public String getDriver() {
		return this.driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	@Column(name = "passenger", nullable = false, length = 100)
	public String getPassenger() {
		return this.passenger;
	}

	public void setPassenger(String passenger) {
		this.passenger = passenger;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "deliveryList")
	public Set<OutgoingDelivery> getOutgoingDeliveries() {
		return this.outgoingDeliveries;
	}

	public void setOutgoingDeliveries(Set<OutgoingDelivery> outgoingDeliveries) {
		this.outgoingDeliveries = outgoingDeliveries;
	}

}
