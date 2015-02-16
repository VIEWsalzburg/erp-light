package at.erp.light.view.controller.adressmanagement;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import at.erp.light.view.controller.article.DeliveryController;
import at.erp.light.view.dto.OrganisationDTO;
import at.erp.light.view.mapper.OrganisationMapper;
import at.erp.light.view.model.Organisation;
import at.erp.light.view.services.IDataBase;
import at.erp.light.view.state.ControllerMessage;

@RestController
public class OrganisationController {
	private static final Logger log = Logger.getLogger(OrganisationController.class
			.getName());
	
	@Autowired
	private IDataBase dataBaseService;
	
	@RequestMapping(value = "secure/organisation/getAllOrganisations")
	public List<OrganisationDTO> getAllOrganisations() {
		
		List<OrganisationDTO> list = new ArrayList<OrganisationDTO>();

		for (Organisation o : dataBaseService.getAllOrganisations()) {
			list.add(OrganisationMapper.mapToDTO(o));
		}
		
		log.info("returning all organisations");
		
		return list;
	}
	
	@RequestMapping(value = "secure/organisation/getAllActiveOrganisations")
	public List<OrganisationDTO> getAllActiveOrganisations() {
		
		List<OrganisationDTO> list = new ArrayList<OrganisationDTO>();

		for (Organisation o : dataBaseService.getAllActiveOrganisations()) {
			list.add(OrganisationMapper.mapToDTO(o));
		}
		
		log.info("returning all active organisations");
		
		return list;
	}
	
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
			dataBaseService.insertLogging("[INFO] Organisation mit der id "+entity.getOrganisationId()+" gespeichert", lastEditorId);
			return new ControllerMessage(true, "Speichern erfoglreich!");
			
		} catch (Exception e) {
			e.printStackTrace();
			return new ControllerMessage(false, "Speichern nicht erfolgreich!");
		}
	}
	
	@RequestMapping(value = "secure/organisation/getOrganisationById/{id}")
	public OrganisationDTO getOrganisationById(@PathVariable int id) {
		Organisation o = dataBaseService.getOrganisationById(id);
		OrganisationDTO organisation = OrganisationMapper.mapToDTO(o);
		log.info("returning organisation with id "+id);
		return organisation;
	}

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
	
}
