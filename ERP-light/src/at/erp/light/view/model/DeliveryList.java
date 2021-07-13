package at.erp.light.view.model;

// Generated 24.11.2014 20:09:12 by Hibernate Tools 3.4.0.CR1

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

/**
 * DeliveryList generated by hbm2java
 */
@Entity
@Table(name = "delivery_list", schema = "public")
public class DeliveryList implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6572505480465292743L;
	private int deliveryListId;
	private Person lastEditor;
	private String name;
	private Date date;
	private String comment;
	private String driver;
	private String passenger;
	private Date updateTimestamp;
	private int archived;
	private Set<OutgoingDelivery> outgoingDeliveries = new HashSet<OutgoingDelivery>(
			0);

	public DeliveryList() {
	}

	public DeliveryList(int deliveryListId, Person lastEditor, String name,
			Date date, String comment, String driver, String passenger, Date updateTimestamp, int archived) {
		this.deliveryListId = deliveryListId;
		this.lastEditor = lastEditor;
		this.name = name;
		this.date = date;
		this.comment = comment;
		this.driver = driver;
		this.passenger = passenger;
		this.updateTimestamp = updateTimestamp;
		this.archived = archived;
	}

	public DeliveryList(int deliveryListId, Person lastEditor, String name,
			Date date, String comment, String driver, String passenger,
			Set<OutgoingDelivery> outgoingDeliveries, Date updateTimestamp, int archived) {
		this.deliveryListId = deliveryListId;
		this.lastEditor = lastEditor;
		this.name = name;
		this.date = date;
		this.comment = comment;
		this.driver = driver;
		this.passenger = passenger;
		this.outgoingDeliveries = outgoingDeliveries;
		this.updateTimestamp = updateTimestamp;
		this.archived = archived;
	}

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="gen_delivery_list_id")
	@SequenceGenerator(name="gen_delivery_list_id", sequenceName="delivery_list_delivery_list_id_seq", allocationSize=1)
	@Column(name = "delivery_list_id", unique = true, nullable = false)
	public int getDeliveryListId() {
		return this.deliveryListId;
	}

	public void setDeliveryListId(int deliveryListId) {
		this.deliveryListId = deliveryListId;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "person_id", nullable = false)
	public Person getLastEditor() {
		return this.lastEditor;
	}

	public void setLastEditor(Person person) {
		this.lastEditor = person;
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
	
	@Temporal(TemporalType.DATE)
	@Column(name = "update_timestamp", nullable = false, length = 13)
	public Date getUpdateTimestamp() {
		return this.updateTimestamp;
	}

	public void setUpdateTimestamp(Date updateTimestamp) {
		this.updateTimestamp = updateTimestamp;
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

	@OneToMany(fetch = FetchType.EAGER)
	@JoinColumn(name="delivery_list_id", referencedColumnName="delivery_list_id")
	public Set<OutgoingDelivery> getOutgoingDeliveries() {
		return this.outgoingDeliveries;
	}

	public void setOutgoingDeliveries(Set<OutgoingDelivery> outgoingDeliveries) {
		this.outgoingDeliveries = outgoingDeliveries;
	}
	
	@Column(name = "archived", nullable = false)
	public int getArchived() {
		return archived;
	}

	public void setArchived(int archived) {
		this.archived = archived;
	}

}
