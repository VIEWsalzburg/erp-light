package at.erp.light.view.dto;

/**
 * This class is a DTO representation of the entity Email.<br/>
 * It is mainly used to transmit data between the frontend and backend.
 * @author Matthias Schnöll
 *
 */
public class EmailDTO {
	private String mail;
	private String type;

	/**
	 * Constructor
	 */
	public EmailDTO() {
	}

	/**
	 * Constructor
	 * @param mail Email address
	 * @param type type of the email address
	 */
	public EmailDTO(String mail, String type) {
		super();
		this.mail = mail;
		this.type = type;
	}

	/**
	 * get the email address
	 * @return email address
	 */
	public String getMail() {
		return mail;
	}

	/**
	 * set the email address
	 * @param mail email address
	 */
	public void setMail(String mail) {
		this.mail = mail;
	}

	/**
	 * get the type of the email
	 * @return type
	 */
	public String getType() {
		return type;
	}

	/**
	 * set the type of the email
	 * @param type type
	 */
	public void setType(String type) {
		this.type = type;
	}
}
