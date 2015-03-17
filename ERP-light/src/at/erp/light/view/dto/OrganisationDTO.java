package at.erp.light.view.dto;

import java.util.List;

/**
 * This class is a DTO representation of the entity Organisation.<br/>
 * It is mainly used to transmit data between the frontend and backend.
 * @author Matthias Schnöll
 *
 */
public class OrganisationDTO {
	private List<String> types;
	private String name;
	private String comment;
	private String address;
	private String zip;
	private String city;
	private String country; 
	private List<Integer> personIds;
	private List<Integer> categoryIds;
	private String updateTimestamp;
	private String lastEditor;
	private int id;

	
	public OrganisationDTO(){}
	
	public OrganisationDTO(List<String> types, String name, String comment,
			String address, String zip, String city, String country,
			List<Integer> personIds, List<Integer> categoryIds,
			String updateTimestamp, String lastEditor, int id) {
		super();
		this.types = types;
		this.name = name;
		this.comment = comment;
		this.address = address;
		this.zip = zip;
		this.city = city;
		this.country = country;
		this.personIds = personIds;
		this.categoryIds = categoryIds;
		this.updateTimestamp = updateTimestamp;
		this.lastEditor = lastEditor;
		this.id = id;
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
	public List<Integer> getCategoryIds() {
		return categoryIds;
	}
	public void setCategoryIds(List<Integer> categories) {
		this.categoryIds = categories;
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


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getCountry() {
		return country;
	}


	public void setCountry(String country) {
		this.country = country;
	}

}
