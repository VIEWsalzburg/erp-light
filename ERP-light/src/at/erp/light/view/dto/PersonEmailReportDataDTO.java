package at.erp.light.view.dto;

public class PersonEmailReportDataDTO {

	int personId;
	String salutation;
	String title;
	String lastName;
	String firstName;
	String comment;
	String persType;
	String email;
	String emailType;
	String organisationName;
	
	public PersonEmailReportDataDTO() {
		super();
	}

	public PersonEmailReportDataDTO(int personId, String salutation,
			String title, String lastName, String firstName, String comment, String persType,
			String email, String emailType, String organisationName) {
		super();
		this.personId = personId;
		this.salutation = salutation;
		this.title = title;
		this.lastName = lastName;
		this.firstName = firstName;
		this.comment = comment;
		this.persType = persType;
		this.email = email;
		this.emailType = emailType;
		this.organisationName = organisationName;
	}

	public int getPersonId() {
		return personId;
	}

	public void setPersonId(int personId) {
		this.personId = personId;
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
	
	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
	
	public String getPersType() {
		return persType;
	}

	public void setPersType(String persType) {
		this.persType = persType;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmailType() {
		return emailType;
	}

	public void setEmailType(String emailType) {
		this.emailType = emailType;
	}

	public String getOrganisationName() {
		return organisationName;
	}

	public void setOrganisationName(String organisationName) {
		this.organisationName = organisationName;
	}
	
}
