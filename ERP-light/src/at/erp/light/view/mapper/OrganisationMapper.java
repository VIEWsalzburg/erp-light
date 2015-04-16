package at.erp.light.view.mapper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Hibernate;

import at.erp.light.view.dto.OrganisationDTO;
import at.erp.light.view.model.Address;
import at.erp.light.view.model.Category;
import at.erp.light.view.model.City;
import at.erp.light.view.model.Country;
import at.erp.light.view.model.Organisation;
import at.erp.light.view.model.Person;
import at.erp.light.view.model.Type;
import at.erp.light.view.services.IDataBase;

/**
 * This class acts as mapper class between entity Organisation and DTO OrganisationDTO.
 * @author Matthias Schnöll
 *
 */
public class OrganisationMapper {

	private static DateFormat df = new SimpleDateFormat("dd.MM.yyyy");

	/**
	 * Maps the given entity to a DTO.
	 * @param organisation Entity from the DB
	 * @return DTO object
	 */
	public static OrganisationDTO mapToDTO(Organisation organisation) {
		OrganisationDTO dto = new OrganisationDTO();

		List<String> typesString = new ArrayList<String>();
		for (Type t : organisation.getTypes()) {
			typesString.add(t.getName());
		}
		dto.setTypes(typesString);

		dto.setId(organisation.getOrganisationId());
		dto.setName(organisation.getName());
		dto.setComment(organisation.getComment());
		
		
		// check if addresses are null
		Address address = organisation.getAddress();
		City city = organisation.getCity();
		Country country = organisation.getCountry();
		String cityString, zipString, countryString, addressString;

		if (city == null) {
			cityString = "";
			zipString = "";
		} else {
			cityString = city.getCity();
			zipString = city.getZip();
		}

		if (country == null) {
			countryString = "";
		} else {
			countryString = country.getCountry();
		}

		if (address == null) {
			addressString = "";
		} else {
			addressString = address.getAddress();
		}
		
		dto.setAddress(addressString);
		dto.setZip(zipString);
		dto.setCity(cityString);
		dto.setCountry(countryString);

		// if loaded with contactpersons
		if (Hibernate.isInitialized(organisation.getContactPersons()))
		{
			List<Integer> ids = new ArrayList<Integer>();
			for (Person p : organisation.getContactPersons()) {
				ids.add(p.getPersonId());
			}
			dto.setPersonIds(ids);
		}

		List<Integer> categories = new ArrayList<Integer>();
		for (Category c : organisation.getCategories()) {
			categories.add(c.getCategoryId());
		}
		dto.setCategoryIds(categories);

		dto.setUpdateTimestamp(df.format(organisation.getUpdateTimestamp()));

		Person lastEditor = organisation.getLastEditor();
		if (lastEditor != null)
		{
			dto.setLastEditor(lastEditor.getFirstName() + " "
				+ lastEditor.getLastName());
		} else {
			dto.setLastEditor("");
		}
		
		return dto;
	}

	/**
	 * Maps the given DTO to an entity.
	 * @param dto DTO object from the frontend
	 * @param dataBaseService DataBaseService class for getting data, needed for the mapping
	 * @return entity
	 */
	public static Organisation mapToEntity(OrganisationDTO dto, IDataBase dataBaseService) {
		Organisation entity = new Organisation();

		entity.setOrganisationId(dto.getId());
		entity.setName(dto.getName());
		entity.setComment(dto.getComment());
		entity.setAddress(new Address(0, dto.getAddress()));
		entity.setCity(new City(0, dto.getCity(), dto.getZip()));
		entity.setCountry(new Country(0, dto.getCountry()));
		entity.setActive(1);	// set Entity active, every entity which is available in the frontend must be active
		
		Set<Person> pList = new HashSet<Person>();
		for (Integer id : dto.getPersonIds()) {
			pList.add(dataBaseService.getPersonById(id));
		}
		entity.setContactPersons(pList);

		Set<Category> categories = new HashSet<Category>();
		for (int categoryId : dto.getCategoryIds()) {
			categories.add(dataBaseService.getCategoryById(categoryId));
		}
		entity.setCategories(categories);

		Set<Type> types = new HashSet<Type>();
		for (String typeStr : dto.getTypes()) {
			types.add(new Type(0, typeStr));
		}

		entity.setTypes(types);
		entity.setUpdateTimestamp(new Date());
		return entity;
	}
}
