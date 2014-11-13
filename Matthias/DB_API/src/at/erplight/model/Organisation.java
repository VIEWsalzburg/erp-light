package at.erplight.model;

// Generated 14.11.2014 00:05:19 by Hibernate Tools 3.4.0.CR1

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Organisation generated by hbm2java
 */
@Entity
@Table(name = "organisation", schema = "public")
public class Organisation implements java.io.Serializable {

	private int orgId;
	private Typ typ;
	private Ort ort;
	private Land land;
	private Adresse adresse;
	private Person person;
	private String orgName;
	private String bemerkungen;
	private Set<Person> persons = new HashSet<Person>(0);
	private Set<Kategorie> kategories = new HashSet<Kategorie>(0);
	private Set<Kundenlieferung> kundenlieferungs = new HashSet<Kundenlieferung>(
			0);
	private Set<EingangsLieferung> eingangsLieferungs = new HashSet<EingangsLieferung>(
			0);

	public Organisation() {
	}

	public Organisation(int orgId, Typ typ, Ort ort, Land land,
			Adresse adresse, Person person, String orgName, String bemerkungen) {
		this.orgId = orgId;
		this.typ = typ;
		this.ort = ort;
		this.land = land;
		this.adresse = adresse;
		this.person = person;
		this.orgName = orgName;
		this.bemerkungen = bemerkungen;
	}

	public Organisation(int orgId, Typ typ, Ort ort, Land land,
			Adresse adresse, Person person, String orgName, String bemerkungen,
			Set<Person> persons, Set<Kategorie> kategories,
			Set<Kundenlieferung> kundenlieferungs,
			Set<EingangsLieferung> eingangsLieferungs) {
		this.orgId = orgId;
		this.typ = typ;
		this.ort = ort;
		this.land = land;
		this.adresse = adresse;
		this.person = person;
		this.orgName = orgName;
		this.bemerkungen = bemerkungen;
		this.persons = persons;
		this.kategories = kategories;
		this.kundenlieferungs = kundenlieferungs;
		this.eingangsLieferungs = eingangsLieferungs;
	}

	@Id
	@Column(name = "org_id", unique = true, nullable = false)
	public int getOrgId() {
		return this.orgId;
	}

	public void setOrgId(int orgId) {
		this.orgId = orgId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "typ_id", nullable = false)
	public Typ getTyp() {
		return this.typ;
	}

	public void setTyp(Typ typ) {
		this.typ = typ;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ort_id", nullable = false)
	public Ort getOrt() {
		return this.ort;
	}

	public void setOrt(Ort ort) {
		this.ort = ort;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "land_id", nullable = false)
	public Land getLand() {
		return this.land;
	}

	public void setLand(Land land) {
		this.land = land;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "adresse_id", nullable = false)
	public Adresse getAdresse() {
		return this.adresse;
	}

	public void setAdresse(Adresse adresse) {
		this.adresse = adresse;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "person_id", nullable = false)
	public Person getPerson() {
		return this.person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	@Column(name = "org_name", nullable = false, length = 100)
	public String getOrgName() {
		return this.orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	@Column(name = "bemerkungen", nullable = false, length = 1024)
	public String getBemerkungen() {
		return this.bemerkungen;
	}

	public void setBemerkungen(String bemerkungen) {
		this.bemerkungen = bemerkungen;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "organisation")
	public Set<Person> getPersons() {
		return this.persons;
	}

	public void setPersons(Set<Person> persons) {
		this.persons = persons;
	}

	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "organisations")
	public Set<Kategorie> getKategories() {
		return this.kategories;
	}

	public void setKategories(Set<Kategorie> kategories) {
		this.kategories = kategories;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "organisation")
	public Set<Kundenlieferung> getKundenlieferungs() {
		return this.kundenlieferungs;
	}

	public void setKundenlieferungs(Set<Kundenlieferung> kundenlieferungs) {
		this.kundenlieferungs = kundenlieferungs;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "organisation")
	public Set<EingangsLieferung> getEingangsLieferungs() {
		return this.eingangsLieferungs;
	}

	public void setEingangsLieferungs(Set<EingangsLieferung> eingangsLieferungs) {
		this.eingangsLieferungs = eingangsLieferungs;
	}

}
