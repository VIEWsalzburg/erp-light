package at.erp.light.view.dto;

public class PersonAddressReportDataDTO {

	String salutation;
	String title;
	String lastName;
	String firstName;
	String privateAddress;
	String privateZip;
	String privateCity;
	String privateCountry;
	String orgName;
	String orgType;
	String orgAddress;
	String orgZip;
	String orgCity;
	String orgCountry;
	
	public PersonAddressReportDataDTO() {
		super();
	}

	public PersonAddressReportDataDTO(String salutation, String title,
			String lastName, String firstName, String privateAddress,
			String privateZip, String privateCity, String privateCountry, String orgName,
			String orgType, String orgAddress, String orgZip, String orgCity,
			String orgCountry) {
		super();
		this.salutation = salutation;
		this.title = title;
		this.lastName = lastName;
		this.firstName = firstName;
		this.privateAddress = privateAddress;
		this.privateZip = privateZip;
		this.privateCity = privateCity;
		this.privateCountry = privateCountry;
		this.orgName = orgName;
		this.orgType = orgType;
		this.orgAddress = orgAddress;
		this.orgZip = orgZip;
		this.orgCity = orgCity;
		this.orgCountry = orgCountry;
	}

	public String getSalutation() {
		return salutation;
	}

	public void setSalutation(String salutation) {
		this.salutation = salutation;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getPrivateAddress() {
		return privateAddress;
	}

	public void setPrivateAddress(String privateAddress) {
		this.privateAddress = privateAddress;
	}

	public String getPrivateZip() {
		return privateZip;
	}

	public void setPrivateZip(String privateZip) {
		this.privateZip = privateZip;
	}

	public String getPrivateCity() {
		return privateCity;
	}

	public void setPrivateCity(String privateCity) {
		this.privateCity = privateCity;
	}

	public String getPrivateCountry() {
		return privateCountry;
	}

	public void setPrivateCountry(String privateCountry) {
		this.privateCountry = privateCountry;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getOrgType() {
		return orgType;
	}

	public void setOrgType(String orgType) {
		this.orgType = orgType;
	}

	public String getOrgAddress() {
		return orgAddress;
	}

	public void setOrgAddress(String orgAddress) {
		this.orgAddress = orgAddress;
	}

	public String getOrgZip() {
		return orgZip;
	}

	public void setOrgZip(String orgZip) {
		this.orgZip = orgZip;
	}

	public String getOrgCity() {
		return orgCity;
	}

	public void setOrgCity(String orgCity) {
		this.orgCity = orgCity;
	}

	public String getOrgCountry() {
		return orgCountry;
	}

	public void setOrgCountry(String orgCountry) {
		this.orgCountry = orgCountry;
	}
	
	
	
}
