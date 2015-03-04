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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

/**
 * Organisation generated by hbm2java
 */
@Entity
@Table(name = "organisation", schema = "public")
public class Organisation implements java.io.Serializable {

	public final static int FETCH_CONTACTPERSON = 1;
	
	private int organisationId;
	private String name;
	private String comment;
	private Address address;
	private City city;
	private Person lastEditor;											// Person, von der upgedated
	private Country country;
	private Date updateTimestamp;
	private int active;
	private Set<Category> categories = new HashSet<Category>(0);
	private Set<Person> contactPersons = new HashSet<Person>(0);			// Persons, die Ansprechpersonen sind
	private Set<Type> types = new HashSet<Type>(0);

	public Organisation() {
	}
	
	public Organisation(int organisationId, String name, String comment, Date updateTimestamp, int active) {
		this.organisationId = organisationId;
		this.name = name;
		this.comment = comment;
		this.updateTimestamp = updateTimestamp;
		this.active = active;
	}

	public Organisation(int organisationId, Address address, City city,
			Person lastEditor, Country country, String name, String comment,
			Date updateTimestamp, int active) {
		this.organisationId = organisationId;
		this.address = address;
		this.city = city;
		this.lastEditor = lastEditor;
		this.country = country;
		this.name = name;
		this.comment = comment;
		this.updateTimestamp = updateTimestamp;
		this.active = active;
	}

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="gen_organisation_id")
	@SequenceGenerator(name="gen_organisation_id", sequenceName="organisation_organisation_id_seq", allocationSize=1)
	@Column(name = "organisation_id", unique = true, nullable = false)
	public int getOrganisationId() {
		return this.organisationId;
	}

	public void setOrganisationId(int organisationId) {
		this.organisationId = organisationId;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "address_id", nullable = true)
	public Address getAddress() {
		return this.address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "city_id", nullable = true)
	public City getCity() {
		return this.city;
	}

	public void setCity(City city) {
		this.city = city;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "person_id", nullable = false)
	public Person getLastEditor() {
		return this.lastEditor;
	}

	public void setLastEditor(Person person) {
		this.lastEditor = person;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "country_id", nullable = true)
	public Country getCountry() {
		return this.country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	@Column(name = "name", nullable = false, length = 100)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
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

	@Column(name = "active", nullable = false)
	public int getActive() {
		return this.active;
	}

	public void setActive(int active) {
		this.active = active;
	}

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "relorgcat", schema = "public", joinColumns = { @JoinColumn(name = "organisation_id", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "category_id", nullable = false, updatable = false) })
	public Set<Category> getCategories() {
		return this.categories;
	}

	public void setCategories(Set<Category> categories) {
		this.categories = categories;
	}

	@OneToMany(fetch = FetchType.LAZY)
	@JoinTable(name="orgcontactperson", schema = "public", joinColumns = { @JoinColumn(name = "organisation_id", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "person_id", nullable = false, updatable = false) })
	public Set<Person> getContactPersons() {
		return this.contactPersons;
	}

	public void setContactPersons(Set<Person> persons) {
		this.contactPersons = persons;
	}

	// @ManyToMany(fetch = FetchType.LAZY, mappedBy = "organisations")
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "relorgtype", schema = "public", inverseJoinColumns = { @JoinColumn(name = "type_id", nullable = false, updatable = false) }, joinColumns = { @JoinColumn(name = "organisation_id", nullable = false, updatable = false) })
	public Set<Type> getTypes() {
		return this.types;
	}

	public void setTypes(Set<Type> types) {
		this.types = types;
	}

}
