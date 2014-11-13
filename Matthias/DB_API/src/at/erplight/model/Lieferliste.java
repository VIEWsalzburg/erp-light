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
 * Lieferliste generated by hbm2java
 */
@Entity
@Table(name = "lieferliste", schema = "public")
public class Lieferliste implements java.io.Serializable {

	private int lieferlisteId;
	private Person person;
	private Date datum;
	private String bemerkung;
	private Set<Kundenlieferung> kundenlieferungs = new HashSet<Kundenlieferung>(
			0);

	public Lieferliste() {
	}

	public Lieferliste(int lieferlisteId, Person person, Date datum,
			String bemerkung) {
		this.lieferlisteId = lieferlisteId;
		this.person = person;
		this.datum = datum;
		this.bemerkung = bemerkung;
	}

	public Lieferliste(int lieferlisteId, Person person, Date datum,
			String bemerkung, Set<Kundenlieferung> kundenlieferungs) {
		this.lieferlisteId = lieferlisteId;
		this.person = person;
		this.datum = datum;
		this.bemerkung = bemerkung;
		this.kundenlieferungs = kundenlieferungs;
	}

	@Id
	@Column(name = "lieferliste_id", unique = true, nullable = false)
	public int getLieferlisteId() {
		return this.lieferlisteId;
	}

	public void setLieferlisteId(int lieferlisteId) {
		this.lieferlisteId = lieferlisteId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "person_id", nullable = false)
	public Person getPerson() {
		return this.person;
	}

	public void setPerson(Person person) {
		this.person = person;
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

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "lieferliste")
	public Set<Kundenlieferung> getKundenlieferungs() {
		return this.kundenlieferungs;
	}

	public void setKundenlieferungs(Set<Kundenlieferung> kundenlieferungs) {
		this.kundenlieferungs = kundenlieferungs;
	}

}
