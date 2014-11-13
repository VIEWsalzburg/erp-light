package at.erplight.model;

// Generated 13.11.2014 22:13:18 by Hibernate Tools 3.4.0.CR1

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
 * Kundenlieferung generated by hbm2java
 */
@Entity
@Table(name = "kundenlieferung", schema = "public")
public class Kundenlieferung implements java.io.Serializable {

	private int kundenLieferungsId;
	private Organisation organisation;
	private Lieferliste lieferliste;
	private Person person;
	private Integer liefernr;
	private Date datum;
	private String bemerkung;
	private Set<Warenausgang> warenausgangs = new HashSet<Warenausgang>(0);

	public Kundenlieferung() {
	}

	public Kundenlieferung(int kundenLieferungsId, Organisation organisation,
			Lieferliste lieferliste, Person person, Date datum, String bemerkung) {
		this.kundenLieferungsId = kundenLieferungsId;
		this.organisation = organisation;
		this.lieferliste = lieferliste;
		this.person = person;
		this.datum = datum;
		this.bemerkung = bemerkung;
	}

	public Kundenlieferung(int kundenLieferungsId, Organisation organisation,
			Lieferliste lieferliste, Person person, Integer liefernr,
			Date datum, String bemerkung, Set<Warenausgang> warenausgangs) {
		this.kundenLieferungsId = kundenLieferungsId;
		this.organisation = organisation;
		this.lieferliste = lieferliste;
		this.person = person;
		this.liefernr = liefernr;
		this.datum = datum;
		this.bemerkung = bemerkung;
		this.warenausgangs = warenausgangs;
	}

	@Id
	@Column(name = "kunden_lieferungs_id", unique = true, nullable = false)
	public int getKundenLieferungsId() {
		return this.kundenLieferungsId;
	}

	public void setKundenLieferungsId(int kundenLieferungsId) {
		this.kundenLieferungsId = kundenLieferungsId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "org_id", nullable = false)
	public Organisation getOrganisation() {
		return this.organisation;
	}

	public void setOrganisation(Organisation organisation) {
		this.organisation = organisation;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "lieferliste_id", nullable = false)
	public Lieferliste getLieferliste() {
		return this.lieferliste;
	}

	public void setLieferliste(Lieferliste lieferliste) {
		this.lieferliste = lieferliste;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "person_id", nullable = false)
	public Person getPerson() {
		return this.person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	@Column(name = "liefernr_")
	public Integer getLiefernr() {
		return this.liefernr;
	}

	public void setLiefernr(Integer liefernr) {
		this.liefernr = liefernr;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "datum", nullable = false, length = 13)
	public Date getDatum() {
		return this.datum;
	}

	public void setDatum(Date datum) {
		this.datum = datum;
	}

	@Column(name = "bemerkung", nullable = false, length = 1000)
	public String getBemerkung() {
		return this.bemerkung;
	}

	public void setBemerkung(String bemerkung) {
		this.bemerkung = bemerkung;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "kundenlieferung")
	public Set<Warenausgang> getWarenausgangs() {
		return this.warenausgangs;
	}

	public void setWarenausgangs(Set<Warenausgang> warenausgangs) {
		this.warenausgangs = warenausgangs;
	}

}
