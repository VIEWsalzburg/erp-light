package at.erp.light.view.model;

// Generated 22.11.2014 18:02:50 by Hibernate Tools 3.4.0.CR1

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * Country generated by hbm2java
 */
@Entity
@Table(name = "country", schema = "public")
public class Country implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6916586488281582023L;
	private int countryId;
	private String country;
	private Set<Organisation> organisations = new HashSet<Organisation>(0);
	private Set<Person> persons = new HashSet<Person>(0);

	public Country() {
	}

	public Country(int countryId, String country) {
		this.countryId = countryId;
		this.country = country;
	}

	public Country(int countryId, String country,
			Set<Organisation> organisations, Set<Person> persons) {
		this.countryId = countryId;
		this.country = country;
		this.organisations = organisations;
		this.persons = persons;
	}

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="gen_country_id")
	@SequenceGenerator(name="gen_country_id", sequenceName="country_country_id_seq", allocationSize=1)
	@Column(name = "country_id", unique = true, nullable = false)
	public int getCountryId() {
		return this.countryId;
	}

	public void setCountryId(int countryId) {
		this.countryId = countryId;
	}

	@Column(name = "country", nullable = false, length = 50)
	public String getCountry() {
		return this.country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "country")
	public Set<Organisation> getOrganisations() {
		return this.organisations;
	}

	public void setOrganisations(Set<Organisation> organisations) {
		this.organisations = organisations;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "country")
	public Set<Person> getPersons() {
		return this.persons;
	}

	public void setPersons(Set<Person> persons) {
		this.persons = persons;
	}

}
