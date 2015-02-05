package at.erp.light.view.controller.adressmanagement;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import at.erp.light.view.authenticate.HashGenerator;
import at.erp.light.view.controller.article.DeliveryController;
import at.erp.light.view.dto.PersonDTO;
import at.erp.light.view.mapper.PersonMapper;
import at.erp.light.view.model.Permission;
import at.erp.light.view.model.Person;
import at.erp.light.view.model.Platformuser;
import at.erp.light.view.services.IDataBase;
import at.erp.light.view.state.ControllerMessage;

@RestController
public class PersonController {
	private static final Logger log = Logger.getLogger(DeliveryController.class
			.getName());
	
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
		
		log.info("returning all Persons");
		
		return list;
	}

	@RequestMapping(value = "secure/person/getPersonById/{id}")
	public PersonDTO getPersonById(@PathVariable int id) {

		Person p = dataBaseService.getPersonById(id);
		PersonDTO person = PersonMapper.mapToDTO(p);
		
		log.info("returning person with id: "+person.getPersonId());
		return person;
		
	}

	@RequestMapping(value = "secure/person/getCurrentUser")
	public PersonDTO getCurrentUser(HttpServletRequest request,
			HttpServletResponse response) {
		int personId = (int) request.getSession().getAttribute("id");

		Person currentPerson = dataBaseService.getPersonById(personId);
		if (currentPerson == null)
			return null;

		PersonDTO currentPersonDTO = PersonMapper.mapToDTO(currentPerson);
		log.info("returning logged in Person");
		return currentPersonDTO;

	}

	@RequestMapping(value = "/secure/person/changeMyData")
	ControllerMessage changeMyData(@RequestBody PersonDTO person, HttpServletRequest request) {
		
		try {
			
			Person myNewPerson = PersonMapper.mapToEntity(person);
			int personId = (int)request.getSession().getAttribute("id");
			Person myPerson = dataBaseService.getPersonById(personId);
			
			myPerson.setSalutation(myNewPerson.getSalutation());
			myPerson.setTitle(myNewPerson.getTitle());
			myPerson.setFirstName(myNewPerson.getFirstName());
			myPerson.setLastName(myNewPerson.getLastName());
			myPerson.setAddress(myNewPerson.getAddress());
			myPerson.setCity(myNewPerson.getCity());
			myPerson.setCountry(myNewPerson.getCountry());
			myPerson.setTelephones(myNewPerson.getTelephones());
			myPerson.setEmails(myNewPerson.getEmails());
			
			myPerson.setLastEditor(dataBaseService.getPersonById(personId));
			dataBaseService.setPerson(myPerson);

			log.info("Changing MyData successful");
			return new ControllerMessage(true, "Speichern erfolgreich!");
		}
		catch (Exception e) {
			e.printStackTrace();
			log.info("Changing MyData not successful");
			return new ControllerMessage(false, "Speichern nicht erfolgreich!");
		}
		
	}
	
	
	@RequestMapping(value = "/secure/person/setPerson")
	ControllerMessage setPerson(@RequestBody PersonDTO person, HttpServletRequest request) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		try {
		
			Person entity = PersonMapper.mapToEntity(person);
			
			// set current user for updater
			entity.setLastEditor(dataBaseService.getPersonById((int) request
					.getSession().getAttribute("id")));
	
			// persist Person to DB
			dataBaseService.setPerson(entity);
			log.info("saved person with id: "+entity.getPersonId());
	
			boolean isPlatformuser = person.isSystemUser();
			
			if (isPlatformuser) {
				log.info("person is platformUser");
				Platformuser existingPU = dataBaseService
						.getPlatformuserById(entity.getPersonId()); // get existing Platformuser
				Permission mPermission = dataBaseService
						.getPermissionByPermission(person.getPermission());
				if (existingPU == null) // if platformuser does not exist
				{
					// create new one
					existingPU = new Platformuser(mPermission, entity, HashGenerator.hashPasswordWithSalt("default"),
							person.getLoginEmail());
					log.info("created new platfomUser for person with id: "+entity.getPersonId());
				} else { // if platformuser exists
					existingPU.setPermission(mPermission); // update permission
					existingPU.setLoginEmail(person.getLoginEmail()); // update loginEmail
					existingPU.setPerson(entity);
					log.info("updated existing platformUser for person with id: "+entity.getPersonId());
				}
				dataBaseService.setPlatformuser(existingPU); // persist new/edited platformuser to DB
			} else // else remove platformuser (even call, if no platformuser exists)
			{
				dataBaseService.removePlatformuserById(person.getPersonId());
				log.info("no platformUser");
			}
	
			return new ControllerMessage(true, "Speichern erfolgreich!");
		} catch (Exception e)
		{
			e.printStackTrace();
			log.severe("failed saving person");
			return new ControllerMessage(false, "Speichern fehlgeschlagen!");
		}
	
	}

	@RequestMapping(value = "secure/person/deletePersonById/{id}")
	public ControllerMessage deletePersonById(@PathVariable int id) {

		try {
			dataBaseService.deletePersonById(id);
		} catch (HibernateException e)
		{
			e.printStackTrace();
			log.severe("deleting person with id "+id+" failed");
			return new ControllerMessage(false, "Löschen fehlgeschlagen!");
		}
		
		log.info("deleted person with id: "+id);
		return new ControllerMessage(true, "Löschen erfolgreich!");
		
	}

	@RequestMapping(value = "secure/person/resetPasswordForId/{id}")
	public ControllerMessage resetPasswordForId(@PathVariable int id)
			throws IOException, NoSuchAlgorithmException {
		try {
			Platformuser platformuser = dataBaseService.getPlatformuserById(id);
			
			platformuser.setPassword(HashGenerator.hashPasswordWithSalt("default"));
			
			dataBaseService.setPlatformuser(platformuser);
			log.info("reset password for user with id "+id);
			return new ControllerMessage(true, "Zurücksetzen erfolgreich!");
		} catch (Exception e)
		{
			log.severe("resetting password for user "+id+" failed");
			return new ControllerMessage(false, "Zurücksetzen nicht erfolgreich!");
		}
	}

	@RequestMapping(value = "secure/person/changeCurrentUserPassword")
	public ControllerMessage changeCurrentUserPassword(
			HttpServletRequest httpServletRequest,
			@RequestBody ChangePasswordObject changePasswordObject) throws IOException {

		Object currentId = httpServletRequest.getSession().getAttribute("id");
		if (currentId == null) {
			return new ControllerMessage(false, "Nicht eingeloggt");
		} else {
			try {
				Platformuser platformuser = dataBaseService
						.getPlatformuserById((int) currentId);
				if (platformuser == null)
				{
					log.info("user does not exist");
					return new ControllerMessage(false, "User existiert nicht!");
				}
				
				if(!HashGenerator.comparePasswordWithHash(changePasswordObject.getOldPassword(),platformuser.getPassword()))
				{
					log.info("old password incorrect");
					return new ControllerMessage(false, "Altes Passwort falsch!");
				}
				
				platformuser.setPassword(HashGenerator.hashPasswordWithSalt(changePasswordObject.getNewPassword()));
				dataBaseService.setPlatformuser(platformuser);
				log.info("changed password successfully");
				return new ControllerMessage(true, "Ändern erfolgreich!");
			} catch (Exception e)
			{
				log.severe("changing the password failed");
				return new ControllerMessage(false, "Ändern fehlgeschlagen!");
			}
			
		}
	}

	@RequestMapping(value = "secure/person/getAllPersonsAsCSV")
	public void downloadCSV(HttpServletResponse response) throws IOException {

		String csvFileName = "personen.csv";
		response.setContentType("text/csv");
		// creates mock data
		String headerKey = "Content-Disposition";
		String headerValue = String.format("attachment; filename=\"%s\"", csvFileName);

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

		log.info("returning CSV Export of Persons");
		
		csvWriter.close();
	}

}
