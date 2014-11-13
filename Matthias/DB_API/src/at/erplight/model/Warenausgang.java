package at.erplight.model;

// Generated 14.11.2014 00:05:19 by Hibernate Tools 3.4.0.CR1

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Warenausgang generated by hbm2java
 */
@Entity
@Table(name = "warenausgang", schema = "public")
public class Warenausgang implements java.io.Serializable {

	private int warenAusId;
	private Waren waren;
	private Kundenlieferung kundenlieferung;
	private Integer warenposten;
	private Double anzahlDerVeAusgang;
	private Double gesamtgewichtAusgang;

	public Warenausgang() {
	}

	public Warenausgang(int warenAusId, Waren waren,
			Kundenlieferung kundenlieferung) {
		this.warenAusId = warenAusId;
		this.waren = waren;
		this.kundenlieferung = kundenlieferung;
	}

	public Warenausgang(int warenAusId, Waren waren,
			Kundenlieferung kundenlieferung, Integer warenposten,
			Double anzahlDerVeAusgang, Double gesamtgewichtAusgang) {
		this.warenAusId = warenAusId;
		this.waren = waren;
		this.kundenlieferung = kundenlieferung;
		this.warenposten = warenposten;
		this.anzahlDerVeAusgang = anzahlDerVeAusgang;
		this.gesamtgewichtAusgang = gesamtgewichtAusgang;
	}

	@Id
	@Column(name = "waren_aus_id", unique = true, nullable = false)
	public int getWarenAusId() {
		return this.warenAusId;
	}

	public void setWarenAusId(int warenAusId) {
		this.warenAusId = warenAusId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "waren_id", nullable = false)
	public Waren getWaren() {
		return this.waren;
	}

	public void setWaren(Waren waren) {
		this.waren = waren;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "kunden_lieferungs_id", nullable = false)
	public Kundenlieferung getKundenlieferung() {
		return this.kundenlieferung;
	}

	public void setKundenlieferung(Kundenlieferung kundenlieferung) {
		this.kundenlieferung = kundenlieferung;
	}

	@Column(name = "warenposten")
	public Integer getWarenposten() {
		return this.warenposten;
	}

	public void setWarenposten(Integer warenposten) {
		this.warenposten = warenposten;
	}

	@Column(name = "anzahl_der_ve_ausgang", precision = 17, scale = 17)
	public Double getAnzahlDerVeAusgang() {
		return this.anzahlDerVeAusgang;
	}

	public void setAnzahlDerVeAusgang(Double anzahlDerVeAusgang) {
		this.anzahlDerVeAusgang = anzahlDerVeAusgang;
	}

	@Column(name = "gesamtgewicht_ausgang", precision = 17, scale = 17)
	public Double getGesamtgewichtAusgang() {
		return this.gesamtgewichtAusgang;
	}

	public void setGesamtgewichtAusgang(Double gesamtgewichtAusgang) {
		this.gesamtgewichtAusgang = gesamtgewichtAusgang;
	}

}
