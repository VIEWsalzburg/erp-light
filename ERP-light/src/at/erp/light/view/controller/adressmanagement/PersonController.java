package at.erp.light.view.controller.adressmanagement;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import at.erp.light.view.dto.PersonDTO;
import at.erp.light.view.mapper.PersonMapper;
import at.erp.light.view.model.Person;
import at.erp.light.view.services.IDataBase;
import at.erp.light.view.state.ControllerMessage;

@RestController
public class PersonController {

	@Autowired
	private IDataBase dataBaseService;

	private PersonController() {
		super();
	}

	@RequestMapping(value = "secure/person/getAll")
	public List<PersonDTO> getAllPersons() {
		List<PersonDTO> list = new ArrayList<PersonDTO>();

		for (Person p : dataBaseService.getAllPersons()) {
			list.add(PersonMapper.mapToDTO(p));
		}
		return list;
	}

	@RequestMapping(value = "secure/person/getPersonById/{id}")
	public PersonDTO getPersonById(@PathVariable int id) {
		List<PersonDTO> list = new ArrayList<PersonDTO>();

		for (Person p : dataBaseService.getAllPersons()) {
			list.add(PersonMapper.mapToDTO(p));
		}

		int found = -1, i = 0;
		for (PersonDTO element : list) {
			if (element.getPersonId() == id) {
				found = i;
			}
			i++;
		}

		if (found != -1)
			return list.get(found);
		else
			return null;
	}

	@RequestMapping(value = "secure/person/getCurrentUser")
	public PersonDTO getCurrentUser(HttpServletRequest request,
			HttpServletResponse response) {
		List<PersonDTO> list = new ArrayList<PersonDTO>();
		int personId = (int) request.getSession().getAttribute("id");
		for (Person p : dataBaseService.getAllPersons()) {
			list.add(PersonMapper.mapToDTO(p));
		}

		for (PersonDTO element : list) {
			if (element.getPersonId() == personId) {
				return element;
			}
		}

		return null;
	}

	// TODO Remove operation
	@RequestMapping(value = "/secure/person/setPerson")
	boolean setPerson(@RequestBody PersonDTO person) {
		Person entity = PersonMapper.mapToEntity(person, dataBaseService);

		if (dataBaseService.getPersonById(person.getPersonId()) == null) {
			dataBaseService.setPerson(entity);
		} else {
			List<Person> pList = dataBaseService.getAllPersons();
			int found = 0, i = 0;
			for (Person p : pList) {
				if (p.getPersonId() == person.getPersonId()) {
					found = i;
				}
				i++;
			}
			pList.remove(found);
			pList.add(found, entity);
			dataBaseService.setPersons(pList);
		}
		return true;
	}

	@RequestMapping(value = "secure/person/deletePersonById/{id}")
	public ControllerMessage deletePersonById(@PathVariable int id) {

		List<Person> pList = dataBaseService.getAllPersons();
		List<Person> returnList = new ArrayList<Person>();

		for (Person p : pList) {
			if (p.getPersonId() != id) {
				returnList.add(p);
			}
		}

		dataBaseService.setPersons(returnList);

		return new ControllerMessage(true, "delete successful");
	}
	@RequestMapping(value = "secure/person/getAllPersonsAsCSV")
	public void downloadCSV(HttpServletResponse response) throws IOException {
	
		String csvFileName = "personen.csv";
		response.setContentType("text/csv");
		// creates mock data
		String headerKey = "Content-Disposition";
		String headerValue = String.format("attachment; filename=\"%s\"",
		csvFileName);
		
		response.setHeader(headerKey, headerValue);
		List<Person> listPersons = dataBaseService.getAllPersons();
		// uses the Super CSV API to generate CSV data from the model data
		ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(),
		CsvPreference.EXCEL_NORTH_EUROPE_PREFERENCE);

		String[] header = { "Personen ID", "Anrede", "Titel", "Vorname",
		"Nachname", "Kommentar", "Letzte Änderung", "Aktiv", "Adresse", "Stadt", "Postleitzahl", "Land", "Login", "Berechtigung", "Typen" , "Emails", "Tel. Nummern"};

		String[] objectHeader = { "personId", "salutation", "title", "firstName",
				"lastName", "comment", "updateTimestamp", "active", "address", "city", "zip", "country", "loginEmail", "permission", "types" , "emails", "telephones"};

		
		csvWriter.writeHeader(header);
		
		for (Person p : listPersons) {
			csvWriter.write(PersonMapper.mapToDTO(p), objectHeader);
		}
		
		csvWriter.close();
	}
	
}
