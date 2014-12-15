package at.erp.light.view.mapper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import at.erp.light.view.dto.OrganisationDTO;
import at.erp.light.view.model.Address;
import at.erp.light.view.model.Category;
import at.erp.light.view.model.City;
import at.erp.light.view.model.Country;
import at.erp.light.view.model.Organisation;
import at.erp.light.view.model.Person;
import at.erp.light.view.model.Type;
import at.erp.light.view.services.IDataBase;

public class OrganisationMapper {

	private static DateFormat df = new SimpleDateFormat("dd.MM.yyyy");

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
		dto.setAddress(organisation.getAddress().getAddress());
		dto.setZip(organisation.getCity().getZip());
		dto.setCity(organisation.getCity().getCity());
		dto.setCountry(organisation.getCountry().getCountry());

		List<Integer> ids = new ArrayList<Integer>();
		for (Person p : organisation.getContactPersons()) {
			ids.add(p.getPersonId());
		}
		dto.setPersonIds(ids);

		List<Integer> categories = new ArrayList<Integer>();
		for (Category c : organisation.getCategories()) {
			categories.add(c.getCategoryId());
		}
		dto.setCategoryIds(categories);

		dto.setUpdateTimestamp(df.format(organisation.getUpdateTimestamp()));

		Person lastEditor = organisation.getLastEditor();
		dto.setLastEditor(lastEditor.getFirstName() + " "
				+ lastEditor.getLastName());
		return dto;
	}

	public static Organisation mapToEntity(OrganisationDTO dto, IDataBase dataBaseService) {
		Organisation entity = new Organisation();

		entity.setOrganisationId(dto.getId());
		entity.setName(dto.getName());
		entity.setComment(dto.getComment());
		entity.setAddress(new Address(0, dto.getAddress()));
		entity.setCity(new City(0, dto.getCity(), dto.getZip()));
		entity.setCountry(new Country(0, dto.getCountry()));
		
		Set<Person> pList = new HashSet<Person>();
		for (Integer id : dto.getPersonIds()) {
			pList.add(dataBaseService.getPersonById(id));
		}

		entity.setContactPersons(pList);

		Set<Category> categories = new HashSet<Category>();
		for (int categoryId : dto.getCategoryIds()) {
			categories.add(new Category(0, dataBaseService.getCategoryById(
					categoryId).getCategory(), ""));
		}
		entity.setCategories(categories);
		try {
			entity.setUpdateTimestamp(df.parse(dto.getUpdateTimestamp()));
		} catch (ParseException e) {
			e.printStackTrace();
		}

		Set<Type> types = new HashSet<Type>();
		for (String typeStr : dto.getTypes()) {
			types.add(new Type(0, typeStr));
		}

		entity.setTypes(types);
		entity.setUpdateTimestamp(new Date());
		return entity;
	}
}
