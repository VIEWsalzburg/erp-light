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
import at.erp.light.view.model.Permission;
import at.erp.light.view.model.Person;
import at.erp.light.view.model.Platformuser;
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
		int personId = (int) request.getSession().getAttribute("id");

		Person currentPerson = dataBaseService.getPersonById(personId);
		if (currentPerson == null)
			return null;

		PersonDTO currentPersonDTO = PersonMapper.mapToDTO(currentPerson);
		return currentPersonDTO;

	}

	// TODO Mapping of letzter bearbeiter
	@RequestMapping(value = "/secure/person/setPerson")
	boolean setPerson(@RequestBody PersonDTO person, HttpServletRequest request) {
		Person entity = PersonMapper.mapToEntity(person);
		
		// set current user for updater
		entity.setPerson(dataBaseService.getPersonById((int) request
				.getSession().getAttribute("id")));

		// persist Person to DB
		dataBaseService.setPerson(entity);

		boolean isPlatformuser = false;
		// TODO Check for Platformuser and include Checkbox in GUI
		if (person.getLoginEmail() != null)
			if (!person.getLoginEmail().isEmpty())
				isPlatformuser = true;

		if (isPlatformuser) {
			System.out.println("is platformuser");
			Platformuser existingPU = dataBaseService
					.getPlatformuserById(entity.getPersonId()); // get existing
																// Platformuser
			Permission mPermission = dataBaseService
					.getPermissionByPermission(person.getPermission());
			if (existingPU == null) // if platformuser does not exist
			{
				// create new one
				existingPU = new Platformuser(mPermission, entity, "default",
						person.getLoginEmail());
				System.out.println("created new one");
			} else { // if platformuser exists
				System.out.println("update existing one");
				existingPU.setPermission(mPermission); // update permission
				existingPU.setLoginEmail(person.getLoginEmail()); // update
																	// loginEmail
				existingPU.setPerson(entity);
			}
			dataBaseService.setPlatformuser(existingPU); // persist new/edited
															// platformuser to
															// DB
		} else // else remove platformuser (even call, if no platformuser
				// exists)
		{
			System.out.println("removed platformuser");
			dataBaseService.removePlatformuserById(person.getPersonId());
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

		return new ControllerMessage(true, "Speichern erfolgreich");
	}

	@RequestMapping(value = "secure/person/resetPasswordForId/{id}")
	public ControllerMessage resetPasswordForId(@PathVariable int id)
			throws IOException {
		Platformuser platformuser = dataBaseService.getPlatformuserById(id);
		platformuser.setPassword("default");
		dataBaseService.setPlatformuser(platformuser);
		return new ControllerMessage(true, "Zurücksetzen erfolgreich");
	}

	@RequestMapping(value = "secure/person/changeCurrentUserPassword")
	public ControllerMessage changeCurrentUserPassword(
			HttpServletRequest httpServletRequest,
			@RequestBody ChangePasswordObject changePasswordObject) throws IOException {

		Object currentId = httpServletRequest.getAttribute("id");
		if (currentId == null) {
			return new ControllerMessage(false, "Nicht eingeloggt");
		} else {
			Platformuser platformuser = dataBaseService
					.getPlatformuserById((int) currentId);
			if(!platformuser.getPassword().equals(changePasswordObject.getOldPassword()))
			{
				return new ControllerMessage(false, "Altes Passwort falsch");
			}
			
			platformuser.setPassword(changePasswordObject.getNewPassword());
			dataBaseService.setPlatformuser(platformuser);
			return new ControllerMessage(true, "Ändern erfolgreich");
		}
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
				"Nachname", "Kommentar", "Letzte Änderung", "Aktiv", "Adresse",
				"Stadt", "Postleitzahl", "Land", "Login", "Berechtigung",
				"Typen", "Emails", "Tel. Nummern" };

		String[] objectHeader = { "personId", "salutation", "title",
				"firstName", "lastName", "comment", "updateTimestamp",
				"active", "address", "city", "zip", "country", "loginEmail",
				"permission", "types", "emails", "telephones" };

		csvWriter.writeHeader(header);

		for (Person p : listPersons) {
			csvWriter.write(PersonMapper.mapToDTO(p), objectHeader);
		}

		csvWriter.close();
	}

}
