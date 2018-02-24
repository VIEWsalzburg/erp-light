package at.erp.light.view.controller.article;

import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import at.erp.light.view.dto.AvailableArticleDTO;
import at.erp.light.view.dto.DeliveryListDTO;
import at.erp.light.view.dto.IncomingDeliveryDTO;
import at.erp.light.view.dto.OutgoingDeliveryDTO;
import at.erp.light.view.mapper.AvailableArticleMapper;
import at.erp.light.view.mapper.DeliveryListMapper;
import at.erp.light.view.mapper.IncomingDeliveryMapper;
import at.erp.light.view.mapper.OutgoingDeliveryMapper;
import at.erp.light.view.model.AvailArticleInDepot;
import at.erp.light.view.model.DeliveryList;
import at.erp.light.view.model.IncomingDelivery;
import at.erp.light.view.model.OutgoingDelivery;
import at.erp.light.view.model.Person;
import at.erp.light.view.services.IDataBase;
import at.erp.light.view.state.ControllerMessage;

/**
 * This class is a RestController.<br/>
 * It manages all calls concerning all sorts of deliveries.
 * @author Matthias Schnöll
 *
 */
@RestController
public class DeliveryController {
	private static final Logger log = Logger.getLogger(DeliveryController.class
			.getName());

	@Autowired
	private IDataBase dataBaseService;

	/***** [START] incoming Deliveries *****/

	/**
	 * Returns a list with all incoming deliveries.<br/>
	 * Archived incoming deliveries are also included.
	 * @return list with all incoming deliveries
	 */
	@RequestMapping(value = "secure/incomingDelivery/getAll")
	public List<IncomingDeliveryDTO> getAllIncomingDeliveries() {

		List<IncomingDeliveryDTO> list = new ArrayList<IncomingDeliveryDTO>();
		List<IncomingDelivery> entityList = null;

		try {
			entityList = dataBaseService.getAllIncomingDeliveries();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		if (entityList.size() > 0) {
			for (IncomingDelivery id : entityList) {
				list.add(IncomingDeliveryMapper.mapToDTO(id));
			}
			log.info("returning all incoming deliveries");

			return list;
		} else {
			log.severe("no incoming deliveries found");

			return null;
		}
	}
	
	/**
	 * Returns a list with incoming deliveries from the given year.<br/>
	 * @param year Year where the incoming deliveries arrived
	 * @return list with incoming deliveries in the given year
	 */
	@RequestMapping(value = "secure/incomingDelivery/getAllByYear/{year}")
	public List<IncomingDeliveryDTO> getArchievedByYearIncomingDeliveries(@PathVariable int year) {

		List<IncomingDeliveryDTO> list = new ArrayList<IncomingDeliveryDTO>();
		List<IncomingDelivery> entityList = null;
		try {
			entityList = dataBaseService.getAllByYearIncomingDeliveries(year);			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		if (entityList.size() > 0) {
			for (IncomingDelivery id : entityList) {
				list.add(IncomingDeliveryMapper.mapToDTO(id));
			}
			log.info("returning incoming deliveries from year " + year);

			return list;
		} else {
			log.severe("no incoming deliveries found");

			return null;
		}
	}

	/**
	 * Returns all unarchived incoming deliveries.
	 * @return list with all unarchived incoming deliveries 
	 */
	@RequestMapping(value = "secure/incomingDelivery/getAllUnarchived")
	public List<IncomingDeliveryDTO> getAllunarchivedIncomingDeliveries() {

		List<IncomingDeliveryDTO> list = new ArrayList<IncomingDeliveryDTO>();
		List<IncomingDelivery> entityList = null;

		try {
			entityList = dataBaseService.getAllIncomingDeliveries(0);
		
			if (entityList.size() > 0) {
				for (IncomingDelivery id : entityList) {
					list.add(IncomingDeliveryMapper.mapToDTO(id));
				}
				log.info("returning all unarchived incoming deliveries");
	
				return list;
			} else {
				log.severe("no incoming deliveries found");
	
				return null;
			}
		
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}

	/**
	 * Returns the incoming delivery with the given Id.
	 * @param id Id of the requested incoming delivery
	 * @return dto representation of the requested incoming delivery
	 */
	@RequestMapping(value = "secure/incomingDelivery/getById/{id}")
	public IncomingDeliveryDTO getIncomingDeliveryById(@PathVariable int id) {
		try {
			IncomingDelivery incomingDelivery = dataBaseService
					.getIncomingDeliveryById(id);
			if (incomingDelivery == null)
				throw new Exception();
			log.info("returning incomingDelivery with id: " + id);
			return IncomingDeliveryMapper.mapToDTO(incomingDelivery);

		} catch (Exception e) {
			log.severe("no incomingDelivery with id " + id + " found");
			return null;
		}
	}

	/**
	 * Set incoming delivery to archived state via id
	 * @param id of the requested delivery
	 * @param state 0=unarchived; 1=archived
	 * @return ControllerMessage showing the success of the call
	 */
	@RequestMapping(value = "secure/incomingDelivery/setArchivedState/{id}/{state}")
	public ControllerMessage setIncomingDeliveryState(@PathVariable int id,
			@PathVariable int state, HttpServletRequest request) {
		try {
			int lastEditorId = (int)request.getSession().getAttribute("id");
			String currentUserPermission = dataBaseService.getPlatformuserById(lastEditorId).getPermission().getPermission();
			if (currentUserPermission.equals("Admin") == false && currentUserPermission.equals("ReadWrite") == false)
			{
				return new ControllerMessage(false, "Keine Berechtigung für diese Aktion!");
			}
			
			dataBaseService.archiveIncomingDeliveryById(id, state);

			log.info("set incoming delivery with: " + id + " to archivedState " + state + ".");
			dataBaseService.insertLogging("[INFO] Wareneingang mit der id "+id+" auf Archivierungsstatus "+state+" gesetzt", lastEditorId);
			return new ControllerMessage(true,"Set archived state for delivery successful");
		} catch (Exception e) {
			log.severe("no incomingDelivery with id " + id + " found");
			return new ControllerMessage(false, e.getMessage());
		}
	}

	/**
	 * Set outgoing delivery to archived via id
	 * @param id of the requested delivery
	 * @param state 0=unarchived; 1=archived
	 * @return ControllerMessage showing the success of the call
	 */
	@RequestMapping(value = "secure/outgoingDelivery/setArchivedState/{id}/{state}")
	public ControllerMessage setOutgoingDeliveryState(@PathVariable int id,
			@PathVariable int state, HttpServletRequest request) {
		try {
			int lastEditorId = (int)request.getSession().getAttribute("id");
			String currentUserPermission = dataBaseService.getPlatformuserById(lastEditorId).getPermission().getPermission();
			if (currentUserPermission.equals("Admin") == false && currentUserPermission.equals("ReadWrite") == false)
			{
				return new ControllerMessage(false, "Keine Berechtigung für diese Aktion!");
			}
			
			dataBaseService.archiveOutgoingDeliveryById(id, state);

			log.info("set outgoingDelivery with: " + id + " to archivedState " + state + ".");
			dataBaseService.insertLogging("[INFO] Warenausgang mit der id "+id+" auf Archivierungsstatus "+state+" gesetzt", lastEditorId);
			return new ControllerMessage(true,
					"Archive operation for delivery successful");
		} catch (Exception e) {
			log.severe("no incoming delivery with id " + id + " found");
			return new ControllerMessage(false, e.getMessage());
		}
	}

	/**
	 * Saves or updates an incoming delivery.
	 * @param dto incomingDelivery from the frontend
	 * @param request to determine the editor
	 * @return ControllerMessage showing the success of the call
	 */
	@RequestMapping(value = "secure/incomingDelivery/set")
	public ControllerMessage setIncomingDelivery(
			@RequestBody IncomingDeliveryDTO dto, HttpServletRequest request) {

		try {
			int lastEditorId = (int) request.getSession().getAttribute("id"); // get current editor id
			
			String currentUserPermission = dataBaseService.getPlatformuserById(lastEditorId).getPermission().getPermission();
			if (currentUserPermission.equals("Admin") == false && currentUserPermission.equals("ReadWrite") == false)
			{
				return new ControllerMessage(false, "Keine Berechtigung für diese Aktion!");
			}
			
			IncomingDelivery entity = IncomingDeliveryMapper.mapToEntity(dto); // map incomingDelivery to entity
			// set current Times for updateTimestamp
			entity.setUpdateTimestamp(new Date(System.currentTimeMillis()));

			// set Organisation and LastEditor for the entity
			entity.setOrganisation(dataBaseService.getOrganisationById(dto
					.getOrganisationId()));
			entity.setLastEditor(dataBaseService.getPersonById(lastEditorId));

			// first scenario: incomingDeliveryId == 0 => create new
			// IncomingDelivery
			if (entity.getIncomingDeliveryId() == 0) {
				dataBaseService.setNewIncomingDelivery(entity); // set NewIncomingDelivery
			} else // second scenario: if incomingDeliveryId != 0 => update existing IncomingDelivery
			{
				dataBaseService.updateIncomingDelivery(entity);
			}

			// if no exception occurs
			log.info("set incomingDelivery with id "+entity.getIncomingDeliveryId()+" successful");
			dataBaseService.insertLogging("[INFO] Wareneingang mit der id "+entity.getIncomingDeliveryId()
					+" gespeichert", lastEditorId);
			return new ControllerMessage(true, "Speichern erfolgreich!");

		} catch (Exception e) {
			e.printStackTrace();

			// if an exception occurs
			log.severe("setting incomingDelivery failed");
			return new ControllerMessage(false, "Speichern nicht erfolgreich!");
		}

	}

	/**
	 * Deletes the incomingDelivery with the given Id.
	 * @param id to delete
	 * @return ControllerMessage showing the success of the call
	 */
	@RequestMapping(value = "secure/incomingDelivery/deleteById/{id}")
	public ControllerMessage deleteIncomingDeliveryById(@PathVariable int id, HttpServletRequest request) {

		try {
			int lastEditorId = (int)request.getSession().getAttribute("id");
			String currentUserPermission = dataBaseService.getPlatformuserById(lastEditorId).getPermission().getPermission();
			if (currentUserPermission.equals("Admin") == false && currentUserPermission.equals("ReadWrite") == false)
			{
				return new ControllerMessage(false, "Keine Berechtigung für diese Aktion!");
			}
			
			dataBaseService.deleteIncomingDeliveryById(id);
			log.info("delete incomingDelivery with id "+id+" successful");
			dataBaseService.insertLogging("[INFO] Wareneingang mit der id "+id+" gelöscht", lastEditorId);
		} catch (Exception e) {
			e.printStackTrace();
			log.severe("delete incomingDelivery with id "+id+" not successful");
			return new ControllerMessage(false, "Löschen nicht erfolgreich: " + e.getMessage());
		}

		return new ControllerMessage(true, "Löschen erfolgreich");
	}

	/***** [END] incoming Deliveries *****/

	/***** [START] outgoing Deliveries *****/

	/**
	 * Returns a list with all outgoingDeliveries in the system.<br/>
	 * Archived outgoingDeliveries are also included.
	 * @return list with all outgoingDeliveries
	 */
	@RequestMapping(value = "secure/outgoingDelivery/getAll")
	public List<OutgoingDeliveryDTO> getAllOutgoingDeliveries() {

		List<OutgoingDeliveryDTO> list = new ArrayList<OutgoingDeliveryDTO>();

		List<OutgoingDelivery> entityList = dataBaseService
				.getAllOutgoingDeliveries();

		if (entityList != null && entityList.size() > 0) {
			for (OutgoingDelivery od : entityList) {
				list.add(OutgoingDeliveryMapper.mapToDTO(od));
			}
			log.info("returning all outgoingDeliveries");

			return list;
		} else {
			log.severe("no outgoingDeliveries found");

			return null;
		}
	}
	
	/**
	 * Returns a list with incoming deliveries from the given year.<br/>
	 * @param year Year where the incoming deliveries arrived
	 * @return list with incoming deliveries in the given year
	 */
	@RequestMapping(value = "secure/outgoingDelivery/getAllByYear/{year}")
	public List<OutgoingDeliveryDTO> getArchievedByYearOutgoingDeliveries(@PathVariable int year) {

		List<OutgoingDeliveryDTO> list = new ArrayList<OutgoingDeliveryDTO>();
		List<OutgoingDelivery> entityList = null;

		try {
			entityList = dataBaseService.getAllByYearOutgoingDeliveries(year);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		if (entityList.size() > 0) {
			for (OutgoingDelivery id : entityList) {
				list.add(OutgoingDeliveryMapper.mapToDTO(id));
			}
			log.info("returning outgoing deliveries from year " + year);

			return list;
		} else {
			log.severe("no outgoing deliveries found");

			return null;
		}
	}

	/**
	 * Returns a list with all unarchived outgoingDeliveries in the system.
	 * @return list with all unarchived outgoingDeliveries
	 */
	@RequestMapping(value = "secure/outgoingDelivery/getAllUnarchived")
	public List<OutgoingDeliveryDTO> getAllUnarchivedOutgoingDeliveries() {

		List<OutgoingDeliveryDTO> list = new ArrayList<OutgoingDeliveryDTO>();

		List<OutgoingDelivery> entityList = dataBaseService
				.getAllOutgoingDeliveries(0);

		if (entityList != null && entityList.size() > 0) {
			for (OutgoingDelivery od : entityList) {
				list.add(OutgoingDeliveryMapper.mapToDTO(od));
			}
			log.info("returning all unarchived outgoingDeliveries");

			return list;
		} else {
			log.severe("no outgoingDeliveries found");

			return null;
		}
	}

	/**
	 * Gets all outgoing deliveries, which are not booked (available).
	 * @return List with available outgoingDeliveries
	 */
	@RequestMapping(value = "secure/outgoingDelivery/getAllAvailables")
	public List<OutgoingDeliveryDTO> getAllAvailableOutgoingDeliveries() {

		List<OutgoingDeliveryDTO> list = new ArrayList<OutgoingDeliveryDTO>();

		List<OutgoingDelivery> entityList = dataBaseService
				.getAvailableOutgoingDeliveries();

		if (entityList != null && entityList.size() > 0) {
			for (OutgoingDelivery od : entityList) {
				list.add(OutgoingDeliveryMapper.mapToDTO(od));
			}
			log.info("returning all available outgoing deliveries");

			return list;
		} else {
			log.info("no available outgoing deliveries found");

			return null;
		}
	}

	
	/**
	 * Returns the outgoingDelivery with the given Id.
	 * @param id Id of the requested outgoingDelivery
	 * @return outgoingDelivery with the given Id
	 */
	@RequestMapping(value = "secure/outgoingDelivery/getById/{id}")
	public OutgoingDeliveryDTO getOutgoingDeliveryById(@PathVariable int id) {
		try {
			OutgoingDelivery outgoingDelivery = dataBaseService
					.getOutgoingDeliveryById(id);
			if (outgoingDelivery == null)
				throw new Exception("Outgoing delivery null");
			log.info("returning outgoing delivery with id: " + id);
			return OutgoingDeliveryMapper.mapToDTO(outgoingDelivery);

		} catch (Exception e) {
			log.info("no outgoing delivery with id " + id + " found");
			return null;
		}
	}

	/**
	 * Saves or updates a outgoingDelivery in the system
	 * @param dto to save, comes from the frontend
	 * @param request to determine the last editor
	 * @return ControllerMessage showing the success of the call
	 */
	@RequestMapping(value = "secure/outgoingDelivery/set")
	public ControllerMessage setOutgoingDelivery(
			@RequestBody OutgoingDeliveryDTO dto, HttpServletRequest request) {

		try{
			
			int lastEditorId = (int) request.getSession().getAttribute("id"); // get current editor id
			
			String currentUserPermission = dataBaseService.getPlatformuserById(lastEditorId).getPermission().getPermission();
			if (currentUserPermission.equals("Admin") == false && currentUserPermission.equals("ReadWrite") == false)
			{
				return new ControllerMessage(false, "Keine Berechtigung für diese Aktion!");
			}
			
			dto.setLastEditorId(lastEditorId);
			
			// Organisation, lastEditor and updateTimestamp from DTO are set within the mapper method
			OutgoingDelivery outgoingDelivery = OutgoingDeliveryMapper.mapToEntity(dto, dataBaseService);
			
			// set updateTimestamp
			outgoingDelivery.setUpdateTimestamp(new Date(System.currentTimeMillis()));
			outgoingDelivery.setLastEditor(dataBaseService.getPersonById(lastEditorId));
			
			// first scenario outgoingDeliveryId == 0 => create new entry in DB
			if (outgoingDelivery.getOutgoingDeliveryId()==0)
			{
				dataBaseService.setNewOutgoingDelivery(outgoingDelivery);
				log.info("saved outgoingDelivery with id "
						+ outgoingDelivery.getOutgoingDeliveryId());
			}
			// second scenario outgoingDeliveryId != 0 => update existing entry in DB
			else
			{
				dataBaseService.updateOutgoingDelivery(outgoingDelivery);
				log.info("updated outgoingDelivery with id "
						+ outgoingDelivery.getOutgoingDeliveryId());
			}
			
			// if no error occurs
			dataBaseService.insertLogging("[INFO] Warenausgang mit der id "
					+outgoingDelivery.getOutgoingDeliveryId() +" gespeichert", lastEditorId);
			return new ControllerMessage(true, "Speichern erfolgreich!");
			
			
		} catch (Exception e) {
			e.printStackTrace();
			
			// if an exception occurs
			return new ControllerMessage(false, "Speichern nicht erfolgreich: "+e.getMessage());
		}

	}

	/**
	 * Deletes the outgoing delivery with the given Id.
	 * @param id of the object to delete
	 * @return ControllerMessage showing the success of the call
	 */
	@RequestMapping(value = "secure/outgoingDelivery/deleteById/{id}")
	public ControllerMessage deleteOutgoingDeliveryById(@PathVariable int id, HttpServletRequest request) {

		try {
			int lastEditorId = (int) request.getSession().getAttribute("id");
			String currentUserPermission = dataBaseService.getPlatformuserById(lastEditorId).getPermission().getPermission();
			if (currentUserPermission.equals("Admin") == false && currentUserPermission.equals("ReadWrite") == false)
			{
				return new ControllerMessage(false, "Keine Berechtigung für diese Aktion!");
			}
			
			dataBaseService.deleteOutgoingDeliveryById(id);
			log.info("deleting outgoingDelivery with id "+id+" successful");
			dataBaseService.insertLogging("[INFO] Warenausgang mit der id "
					+id+" gelöscht", lastEditorId);
			return new ControllerMessage(true, "Löschen erfolgreich!");
		} catch (Exception e) {
			e.printStackTrace();
			log.info("deleting outgoingDelivery with id "+id+" not successful");
			return new ControllerMessage(false, "Löschen nicht erfolgreich: "
					+ e.getMessage());
		}

	}

	/***** [END] outgoing Deliveries *****/

	
	
	/***** [START] availableArticles *****/

	/**
	 * Returns a list with all available articles in the virtual depot.
	 * @return list with availabe articles
	 */
	@RequestMapping(value = "secure/articles/getAvailableArticles")
	public List<AvailableArticleDTO> getAvailableArticles() {
		List<AvailableArticleDTO> availableDTOs = new ArrayList<AvailableArticleDTO>();
		List<AvailArticleInDepot> articleInDepots = new ArrayList<AvailArticleInDepot>();
		try {
			articleInDepots = dataBaseService.getAvailableArticlesInDepot();
		} catch (Exception e) {
			log.severe(e.getMessage());
		}

		for (AvailArticleInDepot article : articleInDepots) {
			availableDTOs.add(AvailableArticleMapper.mapToDTO(article));
		}
		log.info("returning all available articles");
		return availableDTOs;
	}

	/***** [END] availableArticles *****/
	
	/***** [START] Delivery list *****/

	/**
	 * Returns a list with all unarchived delivery lists in the system
	 * @return list with all unarchived delivery lists
	 */
	@RequestMapping(value = "secure/deliveryList/getAllUnarchived")
	public List<DeliveryListDTO> getAllUnarchivedDeliveryLists() {
		List<DeliveryListDTO> deliveryListDTOs = new ArrayList<DeliveryListDTO>();
		List<DeliveryList> deliveryLists = new ArrayList<DeliveryList>();
		try {
			deliveryLists = dataBaseService.getAllDeliveryLists(0);
		} catch (Exception e) {
			log.severe(e.getMessage());
		}
		if (deliveryLists == null) {
			return deliveryListDTOs;
		}
		for (DeliveryList list : deliveryLists) {
			deliveryListDTOs.add(DeliveryListMapper.mapToDTO(list));
		}
		log.info("returning all unarchived deliveryLists");
		return deliveryListDTOs;
	}
	
	/**
	 * Set DeliveryList to archived via id
	 * @param id of the requested delivery
	 * @param state 0=unarchived; 1=archived
	 * @return ControllerMessage showing the success of the call
	 */
	@RequestMapping(value = "secure/deliveryList/setArchivedState/{id}/{state}")
	public ControllerMessage setDeliveryListState(@PathVariable int id,
			@PathVariable int state, HttpServletRequest request) {
		try {
			int lastEditorId = (int) request.getSession().getAttribute("id");
			String currentUserPermission = dataBaseService.getPlatformuserById(lastEditorId).getPermission().getPermission();
			if (currentUserPermission.equals("Admin") == false && currentUserPermission.equals("ReadWrite") == false)
			{
				return new ControllerMessage(false, "Keine Berechtigung für diese Aktion!");
			}
			
			dataBaseService.archiveDeliveryListById(id, state);

			log.info("set delivery list with: " + id + " to archivedState" + state + ".");
			dataBaseService.insertLogging("[INFO] Lieferliste mit der id "+id
					+" auf Archivierungsstatus "+state+" gesetzt", lastEditorId);
			return new ControllerMessage(true,
					"Archive operation for delivery list successful");
		} catch (Exception e) {
			log.info("no delivery list with id " + id + " found");
			return new ControllerMessage(false, e.getMessage());
		}
	}
	
	/**
	 * Returns list with all delivery lists in the system.<br/>
	 * Archived delivery lists are also included.
	 * @return list with all delivery lists
	 */
	@RequestMapping(value = "secure/deliveryList/getAll")
	public List<DeliveryListDTO> getAllDeliveryLists() {
		List<DeliveryListDTO> deliveryListDTOs = new ArrayList<DeliveryListDTO>();
		List<DeliveryList> deliveryLists = new ArrayList<DeliveryList>();
		try {
			deliveryLists = dataBaseService.getAllDeliveryLists();
		} catch (Exception e) {
			log.severe(e.getMessage());
		}
		if (deliveryLists == null) {
			return deliveryListDTOs;
		}
		for (DeliveryList list : deliveryLists) {
			deliveryListDTOs.add(DeliveryListMapper.mapToDTO(list));
		}
		log.info("returning all deliveryLists");
		return deliveryListDTOs;
	}

	/**
	 * Returns the requested delivery list.
	 * @param id of the requested list
	 * @return delivery lists object
	 */
	@RequestMapping(value = "secure/deliveryList/getById/{id}")
	public DeliveryListDTO getDeliverListById(@PathVariable int id) {

		DeliveryListDTO deliveryListDTO = new DeliveryListDTO();

		try {
			deliveryListDTO = DeliveryListMapper.mapToDTO(dataBaseService
					.getDeliveryListById(id));
		} catch (Exception e) {
			log.severe(e.getMessage());
		}
		log.info("returning deliveryList with id "+id);
		return deliveryListDTO;
	}

	/**
	 * Saves or updates the given delivery list in the system.
	 * @param dto to save, comes from the frontend
	 * @param request to determine the last editor
	 * @return ControllerMessage showing the success of the call
	 */
	@RequestMapping(value = "secure/deliveryList/set")
	public ControllerMessage setDeliveryList(@RequestBody DeliveryListDTO dto,
			HttpServletRequest request) {

		try {
			int lastEditorId = (int) request.getSession().getAttribute("id");
			String currentUserPermission = dataBaseService.getPlatformuserById(lastEditorId).getPermission().getPermission();
			if (currentUserPermission.equals("Admin") == false && currentUserPermission.equals("ReadWrite") == false)
			{
				return new ControllerMessage(false, "Keine Berechtigung für diese Aktion!");
			}
			
			dto.setLastEditorId(lastEditorId);
			DeliveryList entity = DeliveryListMapper.mapToEntity(dto,
					dataBaseService);
			entity.setUpdateTimestamp(new Date(System.currentTimeMillis()));

			dataBaseService.setDeliveryList(entity);
			log.info("saving deliveryList successful");
			
			dataBaseService.insertLogging("[INFO] Lieferliste mit der id "+entity.getDeliveryListId()
					+" gespeichert", lastEditorId);
			return new ControllerMessage(true, "Speichern erfolgreich!");

		} catch (Exception e) {
			e.printStackTrace();
			log.severe(e.getMessage());
			return new ControllerMessage(false, "Speichern nicht erfolgreich!");
		}
	}

	/**
	 * Deletes the delivery list witht the given Id.
	 * @param id of the delivery list which should be deleted
	 * @return ControllerMessage showing the success of the call
	 */
	@RequestMapping(value = "secure/deliveryList/deleteById/{id}")
	public ControllerMessage deleteDeliveryListById(@PathVariable int id, HttpServletRequest request) {

		try {
			int lastEditorId = (int) request.getSession().getAttribute("id");
			String currentUserPermission = dataBaseService.getPlatformuserById(lastEditorId).getPermission().getPermission();
			if (currentUserPermission.equals("Admin") == false && currentUserPermission.equals("ReadWrite") == false)
			{
				return new ControllerMessage(false, "Keine Berechtigung für diese Aktion!");
			}
			
			dataBaseService.deleteDeliveryListById(id);
			log.info("deleting deliveryList successful");
			
			dataBaseService.insertLogging("[INFO] Lieferliste mit der id "+id+" gelöscht", lastEditorId);
			
			return new ControllerMessage(true, "Löschen erfolgreich!");
		} catch (Exception e) {
			e.printStackTrace();
			log.severe("deleting deliveryList not successful");
			return new ControllerMessage(false, "Löschen nicht erfolgreich: "
					+ e.getMessage());
		}
	}
	
	
	/**
	 * Exports the deliverList with the given Id as word file.<br/>
	 * The word file is returned by writing the File object to the response stream.
	 * @param id Id of the delivery list
	 * @param httpServletResponse
	 */
	@RequestMapping(value = "secure/deliveryList/exportAsWord/{id}")
	public void generateDeliveryListById(@PathVariable int id,
			HttpServletResponse httpServletResponse) {
		try {
			// Correct mime type
			httpServletResponse
					.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
				
			DeliveryList deliveryList = dataBaseService.getDeliveryListById(id);

			Person lastEditor = deliveryList.getLastEditor();
			
			// wordGeneration is called within dataBaseService to load lazy objects (contactPerson within Organisation)
			FileInputStream wordFile = new FileInputStream(dataBaseService.generateDeliveryExport(id, lastEditor));
			
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
			String filename = "Lieferliste_"+sdf.format(deliveryList.getDate())+".docx";
			httpServletResponse.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", filename));
			
			// copy it to response's OutputStream
			IOUtils.copy(wordFile, httpServletResponse.getOutputStream());
			httpServletResponse.flushBuffer();
		} catch (Exception ex) {
			log.info("Error writing file to output stream." + ex.getMessage());
			throw new RuntimeException(ex);
		}
	}
	
}
