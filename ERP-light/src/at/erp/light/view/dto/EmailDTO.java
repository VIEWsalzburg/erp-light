package at.erp.light.view.dto;

public class EmailDTO {
	private String mail;
	private String type;

	public EmailDTO() {
	}

	public EmailDTO(String mail, String type) {
		super();
		this.mail = mail;
		this.type = type;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
