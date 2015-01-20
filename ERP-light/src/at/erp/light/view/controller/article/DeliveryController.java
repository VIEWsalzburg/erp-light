package at.erp.light.view.controller.article;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;
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
import at.erp.light.view.services.IDataBase;
import at.erp.light.view.state.ControllerMessage;

@RestController
public class DeliveryController {
	private static final Logger log = Logger.getLogger(DeliveryController.class
			.getName());

	@Autowired
	private IDataBase dataBaseService;

	
	/***** [START] incoming Deliveries *****/
	
	/**
	 * Get all incoming deliveries.
	 * @return a dto representation for displaying them
	 */
	@RequestMapping(value = "secure/incomingDelivery/getAll")
	public List<IncomingDeliveryDTO> getAllIncomingDeliveries() {

		List<IncomingDeliveryDTO> list = new ArrayList<IncomingDeliveryDTO>();
		List<IncomingDelivery> entityList = null;
		
		try {
		entityList = dataBaseService
				.getAllIncomingDeliveries();
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
			log.info("no incoming deliveries found");

			return null;
		}
	}
	
	/**
	 * Get an incoming delivery by id.
	 * @param id of the requested delivery
	 * @return a dto representation of the requested delivery
	 */
	@RequestMapping(value = "secure/incomingDelivery/getById/{id}")
	public IncomingDeliveryDTO getIncomingDeliveryById(@PathVariable int id) {
		try {
			IncomingDelivery incomingDelivery = dataBaseService.getIncomingDeliveryById(id);
			if (incomingDelivery == null)
				throw new Exception();
			log.info("returning delivery with id: " + id);
			return IncomingDeliveryMapper.mapToDTO(incomingDelivery);
			
		} catch (Exception e) {
			log.info("no incoming delivery with id " + id + " found");
			return null;
		}
	}
	
	/**
	 * Set a delivery. Sets also last editor via the logged in id.
	 * @param dto to save
	 * @param request to determine the editor
	 * @return a message which contains a state for successful or not and a textual description
	 */
	@RequestMapping(value = "secure/incomingDelivery/set")
	public ControllerMessage setIncomingDelivery(@RequestBody IncomingDeliveryDTO dto, HttpServletRequest request) {
		
		// if Id != 0 => delete existing
		if (dto.getIncomingDeliveryId() != 0)
		{
			try {
				dataBaseService.deleteIncomingDeliveryById(dto.getIncomingDeliveryId());
			} catch (Exception e) {
				e.printStackTrace();
				return new ControllerMessage(false, "Speichern nicht erfolgreich!");
			}
		}
		
		// and save new one
		try {
			int lastEditorId = (int) request.getSession().getAttribute("id");
			
			IncomingDelivery entity = IncomingDeliveryMapper.mapToEntity(dto);
			// set current Times for updateTimestamp
			entity.setUpdateTimestamp(new Date(System.currentTimeMillis()));
			
			// set Organisation and LastEditor for the entity
			entity.setOrganisation(dataBaseService.getOrganisationById(dto.getOrganisationId()));
			entity.setLastEditor(dataBaseService.getPersonById(lastEditorId));
			
			dataBaseService.setNewIncomingDelivery(entity);
			
			return new ControllerMessage(true, "Speichern erfolgreich!");
			
		} catch (Exception e) {
			e.printStackTrace();
			
			return new ControllerMessage(false, "Speichern nicht erfolgreich!");
		}
		
		
	}
	
	/**
	 * Deletes an incomingDelivery by the specified id.
	 * @param id to delete
	 * @return a message with a state if successful or not and a textual description
	 */
	@RequestMapping(value = "secure/incomingDelivery/deleteById/{id}")
	public ControllerMessage deleteIncomingDeliveryById(@PathVariable int id) {
		
		try {
			dataBaseService.deleteIncomingDeliveryById(id);
		} catch (Exception e) {
			e.printStackTrace();
			return new ControllerMessage(false, "Löschen nicht erfolgreich: " + e.getMessage());
		}
		
		return new ControllerMessage(true, "Löschen erfolgreich");
	}
	
	/***** [END] incoming Deliveries *****/
	
	
	
	
	
	
	
	/***** [START] outgoing Deliveries *****/
	
	
	/**
	 * Gets all outgoing deliveries.
	 * @return a dto representation
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
			log.info("returning all outgoing deliveries");

			return list;
		} else {
			log.info("no outgoing deliveries found");

			return null;
		}
	}
	
	/**
	 * Gets all outgoing deliveries, which are not booked (available).
	 * @return a dto representation
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
	 * Gets only the requested outgoing delivery.
	 * @param id of the requested outgoing delivery id
	 * @return a dto representation of the delivery
	 */
	@RequestMapping(value = "secure/outgoingDelivery/getById/{id}")
	public OutgoingDeliveryDTO getOutgoingDeliveryById(@PathVariable int id) {
		try {
			OutgoingDelivery outgoingDelivery = dataBaseService.getOutgoingDeliveryById(id);
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
	 * Sets an outgoing delivery dto.
	 * @param dto to save
	 * @param request to determine the last editor
	 * @return a message with a state and a textual description
	 */
	@RequestMapping(value = "secure/outgoingDelivery/set")
	public ControllerMessage setOutgoingDelivery(@RequestBody OutgoingDeliveryDTO dto, HttpServletRequest request) {
		
		if (dto.getOutgoingDeliveryId() != 0)
		{
			try {
				dataBaseService.deleteOutgoingDeliveryById(dto.getOutgoingDeliveryId());
			} catch (Exception e) {
				e.printStackTrace();
				return new ControllerMessage(false, "Speichern nicht erfolgreich!");
			}
		}
		
		dto.setOutgoingDeliveryId(0);
		dto.setLastEditorId(dataBaseService.getPersonById((int) request.getSession().getAttribute("id")).getPersonId());
		OutgoingDelivery entity = OutgoingDeliveryMapper.mapToEntity(dto, dataBaseService);
		// set current Times for updateTimestamp
		entity.setUpdateTimestamp(new Date(System.currentTimeMillis()));
		
		try {
			dataBaseService.setNewOutgoingDelivery(entity);
			log.info("saved outgoing delivery with id " + entity.getOutgoingDeliveryId());
			return new ControllerMessage(true, "Speichern erfolgreich!");
		} catch (Exception e) {
			e.printStackTrace();
			log.severe("failed to save outgoing delivery with id " + entity.getOutgoingDeliveryId());
			return new ControllerMessage(false, "Speichern nicht erfolgreich!");
		}

	}
	
	/**
	 * Deletes an outgoing delivery.
	 * @param id of object to delete
	 * @return a message with a state and textual description 
	 */
	@RequestMapping(value = "secure/outgoingDelivery/deleteById/{id}")
	public ControllerMessage deleteOutgoingDeliveryById(@PathVariable int id) {

		try {
			dataBaseService.deleteOutgoingDeliveryById(id);
			return new ControllerMessage(true, "Löschen erfolgreich!");
		} catch (Exception e) {
			e.printStackTrace();
			return new ControllerMessage(false, "Löschen nicht erfolgreich: " + e.getMessage());
		}
		
	}
	
	/***** [END] outgoing Deliveries *****/
	
	
	/***** [START] availableArticles *****/
	
	/**
	 * To determine all available articles in the storage.
	 * @return a dto representation
	 */
	@RequestMapping(value = "secure/articles/getAvailableArticles")
	public List<AvailableArticleDTO> getAvailableArticles() 
	{
		List<AvailableArticleDTO> availableDTOs = new ArrayList<AvailableArticleDTO>();
		List<AvailArticleInDepot> articleInDepots = new ArrayList<AvailArticleInDepot>();
		try{
			articleInDepots= dataBaseService.getAvailableArticlesInDepot();
		}
		catch(Exception e)
		{
			log.severe(e.getMessage());
		}
		
		for(AvailArticleInDepot article : articleInDepots)
		{
			availableDTOs.add(AvailableArticleMapper.mapToDTO(article));
		}
		return availableDTOs;
	}
	
	/***** [END] availableArticles *****/
	
	
	
	/***** [START] Delivery list *****/

	/**
	 * Get all delivery lists.
	 * @return dto representation
	 */
	@RequestMapping(value = "secure/deliveryList/getAll")
	public List<DeliveryListDTO> getAllDeliveryLists() 
	{
		List<DeliveryListDTO> deliveryListDTOs = new ArrayList<DeliveryListDTO>();
		List<DeliveryList> deliveryLists = new ArrayList<DeliveryList>();
		try{
			deliveryLists= dataBaseService.getAllDeliveryLists();
		}
		catch(Exception e)
		{
			log.severe(e.getMessage());
		}
		if(deliveryLists == null)
		{
			return deliveryListDTOs;
		}
		for(DeliveryList list : deliveryLists)
		{
			deliveryListDTOs.add(DeliveryListMapper.mapToDTO(list));
		}
		return deliveryListDTOs;
	}
	
	/**
	 * Returns the requested delivery list.
	 * @param id of the requested list
	 * @return a dto representation
	 */
	@RequestMapping(value = "secure/deliveryList/getById/{id}")
	public DeliveryListDTO getDeliverListById(@PathVariable int id) { 

		DeliveryListDTO deliveryListDTO = new DeliveryListDTO();
	
		try{
			deliveryListDTO = DeliveryListMapper.mapToDTO(dataBaseService.getDeliveryListById(id));
		}
		catch(Exception e)
		{
			log.severe(e.getMessage());
		}
		
		return deliveryListDTO;
	}
	
	/**
	 * Sets a deliveryList.
	 * @param dto to set
	 * @param request to determine the last editor
	 * @return a message with a state and a textual description
	 */
	@RequestMapping(value = "secure/deliveryList/set")
	public ControllerMessage setDeliveryList(@RequestBody DeliveryListDTO dto, HttpServletRequest request) {
		
		try {
			dto.setLastEditorId((int) request.getSession().getAttribute("id"));
			DeliveryList entity = DeliveryListMapper.mapToEntity(dto, dataBaseService);
			entity.setUpdateTimestamp(new Date(System.currentTimeMillis()));
			
			dataBaseService.setDeliveryList(entity);
			
			return new ControllerMessage(true, "Speichern erfolgreich!");
			
		} catch (Exception e) {
			e.printStackTrace();
			log.severe(e.getMessage());
			return new ControllerMessage(false, "Speichern nicht erfolgreich!");
		}
	}
	
	//DELETE OF DELIVERY LIST AND IF, HOW
	
	/***** [END] Delivery list *****/

}
