package at.erp.light.view.controller.adressmanagement;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

import at.erp.light.view.dto.OrganisationDTO;
import at.erp.light.view.mapper.OrganisationMapper;
import at.erp.light.view.model.Category;
import at.erp.light.view.model.Organisation;
import at.erp.light.view.model.Person;
import at.erp.light.view.services.IDataBase;
import at.erp.light.view.state.ControllerMessage;

/**
 * This class is a RestController<br/>
 * It manages calls concerning organisations.
 * @author Matthias Schnöll
 *
 */
@RestController
public class OrganisationController {
	private static final Logger log = Logger.getLogger(OrganisationController.class
			.getName());
	
	@Autowired
	private IDataBase dataBaseService;
	
	/**
	 * Returns a list with all Organisations in the system.<br/>
	 * Also includes inactive organisations.
	 * @return list with OrganisationDTO objects
	 */
	@RequestMapping(value = "secure/organisation/getAllOrganisations")
	public List<OrganisationDTO> getAllOrganisations() {
		
		List<OrganisationDTO> list = new ArrayList<OrganisationDTO>();

		for (Organisation o : dataBaseService.getAllOrganisations(Organisation.FETCH_CONTACTPERSON)) {
			list.add(OrganisationMapper.mapToDTO(o));
		}
		
		log.info("returning all organisations");
		
		return list;
	}
	
	
	/**
	 * Returns a list with all Organisations in the system.<br/>
	 * ContactPersons are not delivered to save data transfer.<br/>
	 * Also includes inactive organisations.
	 * @return list with reduced dataset of all organisations
	 */
	@RequestMapping(value = "secure/organisation/reducedData/getAllOrganisations")
	public List<OrganisationDTO> getAllOrganisationsWithoutContactPersons() {
		
		List<OrganisationDTO> list = new ArrayList<OrganisationDTO>();

		for (Organisation o : dataBaseService.getAllOrganisations(0)) {
			list.add(OrganisationMapper.mapToDTO(o));
		}
		
		log.info("returning all organisations without contactPersons");
		
		return list;
	}
	
	
	/**
	 * Returns list with all active organisations in the system.<br/>
	 * Only organisations which are marked as active are returned.
	 * @return list with all active organisations
	 */
	@RequestMapping(value = "secure/organisation/getAllActiveOrganisations")
	public List<OrganisationDTO> getAllActiveOrganisations() {
		
		List<OrganisationDTO> list = new ArrayList<OrganisationDTO>();

		for (Organisation o : dataBaseService.getAllActiveOrganisations(Organisation.FETCH_CONTACTPERSON)) {
			list.add(OrganisationMapper.mapToDTO(o));
		}
		
		log.info("returning all active organisations");
		
		return list;
	}
	
	
	/**
	 * Returns list with all active organisations in the system.<br/>
	 * Only active organisations are returned.<br/>
	 * ContactPersons are not included to reduce data transfer
	 * @return list with reduced dataset of all active organisations
	 */
	@RequestMapping(value = "secure/organisation/reducedData/getAllActiveOrganisations")
	public List<OrganisationDTO> getAllActiveOrganisationsWithoutContactPersons() {
		
		List<OrganisationDTO> list = new ArrayList<OrganisationDTO>();

		for (Organisation o : dataBaseService.getAllActiveOrganisations(0)) {
			list.add(OrganisationMapper.mapToDTO(o));
		}
		
		log.info("returning all active organisations without contactPersons");
		
		return list;
	}
	
	
	/**
	 * Saves or updates a organisations in the system.
	 * @param organisation OrganisationDTO object comes from the frontend
	 * @param request
	 * @return ControllerMessage showing the success of the call
	 */
	@RequestMapping(value = "/secure/organisation/setOrganisation")
	public ControllerMessage setOrganisation(@RequestBody OrganisationDTO organisation, HttpServletRequest request) {
		
		try {
			
			int lastEditorId = (int)request.getSession().getAttribute("id");
			String currentUserPermission = dataBaseService.getPlatformuserById(lastEditorId).getPermission().getPermission();
			if (currentUserPermission.equals("Admin") == false)
			{
				return new ControllerMessage(false, "Keine Berechtigung für diese Aktion!");
			}
			
			Organisation entity = OrganisationMapper.mapToEntity(organisation, dataBaseService);
			entity.setLastEditor(dataBaseService.getPersonById(lastEditorId));
			dataBaseService.setOrganisation(entity);
		
			log.info("saved organisation with id "+entity.getOrganisationId());
			dataBaseService.insertLogging("[INFO] Organisation mit der id "+entity.getOrganisationId()+" gespeichert. "+entity.toString(), lastEditorId);
			return new ControllerMessage(true, "Speichern erfoglreich!");
			
		} catch (Exception e) {
			e.printStackTrace();
			return new ControllerMessage(false, "Speichern nicht erfolgreich!");
		}
	}
	
	
	/**
	 * Returns the organisation with the given Id 
	 * @param id Id of the requested organisation
	 * @return OrganisationDTO object with the given Id
	 */
	@RequestMapping(value = "secure/organisation/getOrganisationById/{id}")
	public OrganisationDTO getOrganisationById(@PathVariable int id) {
		Organisation o = dataBaseService.getOrganisationById(id);
		OrganisationDTO organisation = OrganisationMapper.mapToDTO(o);
		log.info("returning organisation with id "+id);
		return organisation;
	}

	
	/**
	 * Deletes the organisation with the given Id<br/>
	 * Deleted organisations are makred as inactive in the DB.<br/>
	 * The connection to all contactPersons is removed. 
	 * @param id Id of the organisation which should be deleted
	 * @param request
	 * @return ControllerMessage showing the success of the call
	 */
	@RequestMapping(value = "secure/organisation/deleteOrganisationById/{id}")
	public ControllerMessage deleteOrganisationById(@PathVariable int id, HttpServletRequest request) {

		int lastEditorId = (int) request.getSession().getAttribute("id");

		try {
			String currentUserPermission = dataBaseService.getPlatformuserById(lastEditorId).getPermission().getPermission();
			if (currentUserPermission.equals("Admin") == false)
			{
				return new ControllerMessage(false, "Keine Berechtigung für diese Aktion!");
			}
			
			dataBaseService.deleteOrganisationById(id);
			log.info("deleted organisation with id "+id);
			dataBaseService.insertLogging("[INFO] Organisation mit der id "+id+" gelöscht", lastEditorId);
			return new ControllerMessage(true, "Löschen erfolgreich!");
			
		} catch (HibernateException e)
		{
			e.printStackTrace();
			return new ControllerMessage(false, "Löschen fehlgeschlagen!");
		}
		
	}
	
	
	/**
	 * Returns a csv file including all organisations.<br/>
	 * The file is returned by writing directly to the response stream.
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "secure/organisation/getAllOrganisationsAsCSV")
	public void downloadCSV(HttpServletResponse response) throws IOException {

		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
		String currentDateString = sdf.format(new Date());
		
		String csvFileName = "Alle Organisationen_"+currentDateString+".csv";
		response.setContentType("text/csv");
		// creates mock data
		String headerKey = "Content-Disposition";
		String headerValue = String.format("attachment; filename=\"%s\"", csvFileName);

		response.setHeader(headerKey, headerValue);
		List<Organisation> listOrganisations = dataBaseService.getAllOrganisations(Organisation.FETCH_CONTACTPERSON);
		// uses the Super CSV API to generate CSV data from the model data
		ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(),
				CsvPreference.EXCEL_NORTH_EUROPE_PREFERENCE);

		csvWriter.writeHeader("Liste aller Organisationen:");
		csvWriter.writeHeader("Erstellungsdatum:", currentDateString);
		csvWriter.writeHeader("");
		
		String[] header = { "Organisation ID", "Name", "Ansprechperson(en)", "Anschrift",
				"PLZ", "Stadt", "Land", "Typen", "Kategorien", "Bemerkung", "Aktiv (1 = aktiv / 0 = gelöscht)" };

		csvWriter.writeHeader(header);

		
		// write Data to CSV
		for (Organisation o : listOrganisations) {
			OrganisationDTO dto = OrganisationMapper.mapToDTO(o);
			
			String[] data = new String[11];
			
			// insert ID
			data[0] = ""+dto.getId();
			
			// insert Name
			data[1] = dto.getName();
			
			// insert contactPersons
			String contactPersons = "";
			for (int i=0; i<dto.getPersonIds().size(); i++)
			{
				Person p = dataBaseService.getPersonById(dto.getPersonIds().get(i));
				contactPersons += p.getLastName()+" "+p.getFirstName();
				if ( i < (dto.getPersonIds().size()-1) )
					contactPersons += ", ";
			}
			data[2] = contactPersons;
			
			// insert Address
			data[3] = dto.getAddress();
			
			// insert zip
			data[4] = dto.getZip();
			
			// insert city
			data[5] = dto.getCity();
			
			// insert country
			data[6] = dto.getCountry();
			
			// insert Types
			String types = "";
			for (int i=0; i<dto.getTypes().size(); i++)
			{
				types += dto.getTypes().get(i);
				if (i < (dto.getTypes().size()-1) )
					types += ", ";
			}
			data[7] = types;
			
			// insert ccategories
			String categories = "";
			for (int i=0; i<dto.getCategoryIds().size(); i++)
			{
				Category c = dataBaseService.getCategoryById(dto.getCategoryIds().get(i));
				categories += c.getCategory();
				if ( i < (dto.getCategoryIds().size()-1) )
					categories += ", ";
			}
			data[8] = categories;
			
			// insert comment
			data[9] = dto.getComment();
			
			// insert aktive flag
			data[10] = ""+o.getActive();
			
			csvWriter.writeHeader(data);
			
		}

		log.info("returning CSV Export of Organisations");
		
		csvWriter.close();
	}
	
}
