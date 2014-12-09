package at.erp.light.view.dto;

import java.util.List;

public class OrganisationDTO {
	private List<String> types;
	private String name;
	private String comment;
	private String address;
	private String zip;
	private String city;
	private List<Integer> personIds;
	private List<String> categories;
	private String updateTimestamp;
	private String lastEditor;

	
	public OrganisationDTO(){}
	
	
	public OrganisationDTO(List<String> types, String name, String comment,
			String address, String zip, String city, List<Integer> personIds,
			List<String> categories, String updateTimestamp, String lastEditor) {
		super();
		this.types = types;
		this.name = name;
		this.comment = comment;
		this.address = address;
		this.zip = zip;
		this.city = city;
		this.personIds = personIds;
		this.categories = categories;
		this.updateTimestamp = updateTimestamp;
		this.lastEditor = lastEditor;
	}


	public List<String> getTypes() {
		return types;
	}
	public void setTypes(List<String> types) {
		this.types = types;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getZip() {
		return zip;
	}
	public void setZip(String zip) {
		this.zip = zip;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public List<Integer> getPersonIds() {
		return personIds;
	}
	public void setPersonIds(List<Integer> personIds) {
		this.personIds = personIds;
	}
	public List<String> getCategories() {
		return categories;
	}
	public void setCategories(List<String> categories) {
		this.categories = categories;
	}
	public String getUpdateTimestamp() {
		return updateTimestamp;
	}
	public void setUpdateTimestamp(String updateTimestamp) {
		this.updateTimestamp = updateTimestamp;
	}

	public String getLastEditor() {
		return lastEditor;
	}

	public void setLastEditor(String lastEditor) {
		this.lastEditor = lastEditor;
	}

}
