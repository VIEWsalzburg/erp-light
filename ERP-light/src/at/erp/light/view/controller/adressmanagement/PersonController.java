package at.erp.light.view.controller.adressmanagement;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import at.erp.light.view.dto.PersonDTO;
import at.erp.light.view.mapper.PersonMapper;
import at.erp.light.view.model.Person;
import at.erp.light.view.services.IDataBase;

@RestController
public class PersonController {

	private List<PersonDTO> personList = new ArrayList<PersonDTO>();

	@Autowired
	private IDataBase dataBaseService;
	
	private PersonController()
	{
		super();
	}
	
	
	@RequestMapping(value = "secure/person/getAll")
	public List<PersonDTO> getAllPersons() {
		List<PersonDTO> list = new ArrayList<PersonDTO>();
		
		for(Person p : dataBaseService.getAllPersons())
		{
			list.add(PersonMapper.mapToDTO(p));
		}
		return list;
	}
	
	@RequestMapping(value = "secure/person/getPersonById/{id}")
	public PersonDTO getPersonById(@PathVariable int id) {
		List<PersonDTO> list = new ArrayList<PersonDTO>();
		
		for(Person p : dataBaseService.getAllPersons())
		{
			list.add(PersonMapper.mapToDTO(p));
		}		
		
		int found = -1, i = 0;
		for (PersonDTO element : list) {
			if (element.getPersonId() == id) {
				found = i;
			}
			i++;
		}
		
		if(found!=-1)
			return list.get(found);
		else
			return null;
	}

	//TODO Save person in service
	@RequestMapping(value = "/secure/person/setPerson")
	boolean setPerson(@RequestBody PersonDTO person) {
		
		if (person.getPersonId() == 0) {
			person.setPersonId(personList.size());
			personList.add(person);
		} else {
			// exisiting DTO edit person
			int found = -1, i = 0;
			for (PersonDTO element : personList) {
				if (element.getPersonId() == person.getPersonId()) {
					found = i;
				}
				i++;
			}
			if (found != -1) {
				personList.set(found, person);
			} else {//Edit id not found adding person
				personList.add(person);
			}
		}
		return true;
	}

}
