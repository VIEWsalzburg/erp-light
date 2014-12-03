package at.erp.light.view.controller.adressmanagement;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import at.erp.light.view.dto.PersonDTO;

@RestController
public class PersonController {

	private List<PersonDTO> personList = new ArrayList<PersonDTO>();

	private PersonController()
	{
		super();
		List<String> types = new ArrayList<String>();
		types.add("Mitglied");
		types.add("User");

		List<String> emails = new ArrayList<String>();
		emails.add("f.sdfhj@doo.com");
		emails.add("huber@gmail.at");

		List<String> phoneNumbers = new ArrayList<String>();
		phoneNumbers.add("293847239423");
		phoneNumbers.add("032423423432");

		personList.add(new PersonDTO(0, "Herr", "Dr", "Sepp", "Huber",
				"Kommt vom Land", "12.9.2014", 1, "Salzweg 4", "Salzburg","5020",
				"÷sterreich", "seppi@test.com", "haha", "admin", types, emails,
				phoneNumbers));

		phoneNumbers.add("293427394799");

		personList.add(new PersonDTO(1, "Frau", "BSc", "Maria", "Schmidt",
				"Kommt aus der Stadt", "12.9.2013", 1, "Vogelweiderstraﬂe 7",
				"Obertrum", "5070","Deutschland", "maria@test.com", "muhaha", "admin",
				types, emails, phoneNumbers));
		personList.add(new PersonDTO(2, "Herr", "MSc", "Admin", "Admin",
				"Kommt aus der Stadt", "12.9.2013", 1, "Rudolfskai 18",
				"Salzburg", "5030","Frankreich", "admin@admin.admin", "admin",
				"admin", types, emails, phoneNumbers));
	}
	
	
	@RequestMapping(value = "secure/person/getAll")
	public List<PersonDTO> getAllPersons() {
		

		return personList;
	}
	
	@RequestMapping(value = "secure/person/getPersonById/{id}")
	public PersonDTO getPersonById(@PathVariable int id) {
		int found = -1, i = 0;
		for (PersonDTO element : personList) {
			if (element.getPersonId() == id) {
				found = i;
			}
			i++;
		}
		
		if(found!=-1)
			return personList.get(found);
		else
			return null;
	}

	@RequestMapping(value = "secure/person/setPerson")
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
