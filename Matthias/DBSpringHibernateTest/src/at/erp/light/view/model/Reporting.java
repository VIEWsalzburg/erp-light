package at.erp.light.view.model;

// Generated 22.11.2014 18:02:50 by Hibernate Tools 3.4.0.CR1

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Reporting generated by hbm2java
 */
@Entity
@Table(name = "reporting", schema = "public")
public class Reporting implements java.io.Serializable {

	private int reportId;
	private Date date;
	private String description;
	private Character reportFile;

	public Reporting() {
	}

	public Reporting(int reportId, Date date) {
		this.reportId = reportId;
		this.date = date;
	}

	public Reporting(int reportId, Date date, String description,
			Character reportFile) {
		this.reportId = reportId;
		this.date = date;
		this.description = description;
		this.reportFile = reportFile;
	}

	@Id
	@Column(name = "report_id", unique = true, nullable = false)
	public int getReportId() {
		return this.reportId;
	}

	public void setReportId(int reportId) {
		this.reportId = reportId;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "date", nullable = false, length = 13)
	public Date getDate() {
		return this.date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@Column(name = "description", length = 1024)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "report_file", length = 1)
	public Character getReportFile() {
		return this.reportFile;
	}

	public void setReportFile(Character reportFile) {
		this.reportFile = reportFile;
	}

}