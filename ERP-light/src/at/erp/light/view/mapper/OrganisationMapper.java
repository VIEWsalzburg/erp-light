package at.erp.light.view.mapper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import at.erp.light.view.dto.OrganisationDTO;
import at.erp.light.view.model.Category;
import at.erp.light.view.model.Organisation;
import at.erp.light.view.model.Person;
import at.erp.light.view.model.Type;

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
		
		List<Integer> ids= new ArrayList<Integer>();
		for(Person p: organisation.getContactPersons())
		{
			ids.add(p.getPersonId());
		}
		dto.setPersonIds(ids);
		
		List<String> categories= new ArrayList<String>();
		for(Category c: organisation.getCategories())
		{
			categories.add(c.getCategory());
		}
		dto.setCategories(categories);
		
		dto.setUpdateTimestamp(df.format(organisation.getUpdateTimestamp()));
				
		Person lastEditor = organisation.getLastEditor();
		dto.setLastEditor(lastEditor.getFirstName() + " " + lastEditor.getLastName());
		return dto;
	}
}
