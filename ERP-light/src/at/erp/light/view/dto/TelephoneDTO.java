package at.erp.light.view.dto;

public class TelephoneDTO {
	private String telephone;
	private String type;

	public TelephoneDTO() {
	}

	public TelephoneDTO(String telephone, String type) {
		super();
		this.telephone = telephone;
		this.type = type;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
