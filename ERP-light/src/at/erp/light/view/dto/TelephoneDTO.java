package at.erp.light.view.dto;

/**
 * This class is a DTO representation of the entity Telephone.<br/>
 * It is mainly used to transmit data between the frontend and backend.
 * @author Matthias Schnöll
 *
 */
public class TelephoneDTO {
	private String telephone;
	private String type;

	/**
	 * Constructor
	 */
	public TelephoneDTO() {
	}

	/**
	 * Constructor
	 * @param telephone Telephone number
	 * @param type type of the telephone number (private or business)
	 */
	public TelephoneDTO(String telephone, String type) {
		super();
		this.telephone = telephone;
		this.type = type;
	}

	/**
	 * get telephone number
	 * @return telephone number
	 */
	public String getTelephone() {
		return telephone;
	}

	/**
	 * set telephone number
	 * @param telephone telephone number
	 */
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	/**
	 * get type of telephone
	 * @return type
	 */
	public String getType() {
		return type;
	}

	/**
	 * set type for the telephone
	 * @param type type
	 */
	public void setType(String type) {
		this.type = type;
	}
}
