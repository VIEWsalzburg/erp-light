package at.erp.light.view.model;

// Generated 22.11.2014 18:02:50 by Hibernate Tools 3.4.0.CR1

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * Email generated by hbm2java
 */
@Entity
@Table(name = "email", schema = "public")
public class Email implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7694600484047938L;
	private int emailId;
	private Type type;
	// private Person person;
	private String email;

	public Email() {
	}

	public Email(int emailId, Type type, String email) {
		this.emailId = emailId;
		this.type = type;
		this.email = email;
	}

//	public Email(int emailId, Type type, Person person, String loginEmail) {
//		this.emailId = emailId;
//		this.type = type;
//		this.person = person;
//		this.loginEmail = loginEmail;
//	}

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="gen_email_id")
	@SequenceGenerator(name="gen_email_id", sequenceName="email_email_id_seq", allocationSize=1)
	@Column(name = "email_id", unique = true, nullable = false)
	public int getEmailId() {
		return this.emailId;
	}

	public void setEmailId(int emailId) {
		this.emailId = emailId;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "type_id", nullable = false)
	public Type getType() {
		return this.type;
	}

	public void setType(Type type) {
		this.type = type;
	}

//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "person_id")
//	public Person getPerson() {
//		return this.person;
//	}
//
//	public void setPerson(Person person) {
//		this.person = person;
//	}

	@Column(name = "email", nullable = false, length = 50)
	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
