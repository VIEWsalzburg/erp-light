package at.erp.light.view.controller.article;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.supercsv.io.CsvListWriter;
import org.supercsv.prefs.CsvPreference;

import at.erp.light.view.dto.AvailableArticleDTO;
import at.erp.light.view.dto.DeliveryListDTO;
import at.erp.light.view.dto.IncomingArticleDTO;
import at.erp.light.view.dto.IncomingDeliveryDTO;
import at.erp.light.view.dto.OutgoingArticleDTO;
import at.erp.light.view.dto.OutgoingDeliveryDTO;
import at.erp.light.view.generation.WordGenerator;
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
	 * 
	 * @return a dto representation for displaying them
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
			log.info("no incoming deliveries found");

			return null;
		}
	}

	/**
	 * Get all unarchived incoming deliveries.
	 * 
	 * @return a dto representation for displaying them
	 */
	@RequestMapping(value = "secure/incomingDelivery/getAllUnarchived")
	public List<IncomingDeliveryDTO> getAllunarchivedIncomingDeliveries() {

		List<IncomingDeliveryDTO> list = new ArrayList<IncomingDeliveryDTO>();
		List<IncomingDelivery> entityList = null;

		try {
			entityList = dataBaseService.getAllIncomingDeliveries(0);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		if (entityList.size() > 0) {
			for (IncomingDelivery id : entityList) {
				list.add(IncomingDeliveryMapper.mapToDTO(id));
			}
			log.info("returning all unarchived incoming deliveries");

			return list;
		} else {
			log.info("no incoming deliveries found");

			return null;
		}
	}

	/**
	 * Get an incoming delivery by id.
	 * 
	 * @param id
	 *            of the requested delivery
	 * @return a dto representation of the requested delivery
	 */
	@RequestMapping(value = "secure/incomingDelivery/getById/{id}")
	public IncomingDeliveryDTO getIncomingDeliveryById(@PathVariable int id) {
		try {
			IncomingDelivery incomingDelivery = dataBaseService
					.getIncomingDeliveryById(id);
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
	 * Set incoming delivery to archived state via id
	 * 
	 * @param id
	 *            of the requested delivery
	 * @param state
	 *            0/1 un/archived
	 * @return MessageOb
	 */
	@RequestMapping(value = "secure/incomingDelivery/setArchivedState/{id}/{state}")
	public ControllerMessage setIncomingDeliveryState(@PathVariable int id,
			@PathVariable int state) {
		try {
			dataBaseService.archiveIncomingDeliveryById(id, state);

			log.info("set incoming delivery with: " + id + " to " + state + ".");
			return new ControllerMessage(true,
					"Set archived state for delivery successful");
		} catch (Exception e) {
			log.info("no incoming delivery with id " + id + " found");
			return new ControllerMessage(false, e.getMessage());
		}
	}

	/**
	 * Set outgoing delivery to archived via id
	 * 
	 * @param id
	 *            of the requested delivery
	 * @param state
	 *            0/1 un/archived
	 * @return MessageObject with state
	 */
	@RequestMapping(value = "secure/outgoingDelivery/setArchivedState/{id}/{state}")
	public ControllerMessage setOutgoingDeliveryState(@PathVariable int id,
			@PathVariable int state) {
		try {
			dataBaseService.archiveOutgoingDeliveryById(id, state);

			log.info("set outgoing delivery with: " + id + " to " + state + ".");
			return new ControllerMessage(true,
					"Archive operation for delivery successful");
		} catch (Exception e) {
			log.info("no incoming delivery with id " + id + " found");
			return new ControllerMessage(false, e.getMessage());
		}
	}

	/**
	 * Set a delivery. Sets also last editor via the logged in id.
	 * 
	 * @param dto
	 *            to save
	 * @param request
	 *            to determine the editor
	 * @return a message which contains a state for successful or not and a
	 *         textual description
	 */
	@RequestMapping(value = "secure/incomingDelivery/set")
	public ControllerMessage setIncomingDelivery(
			@RequestBody IncomingDeliveryDTO dto, HttpServletRequest request) {

		// first scenario: incomingDeliveryId == 0 => new IncomingDelivery

		try {
			int lastEditorId = (int) request.getSession().getAttribute("id"); // get
																				// current
																				// editor
																				// id

			IncomingDelivery entity = IncomingDeliveryMapper.mapToEntity(dto); // map
																				// incomingDelivery
																				// to
																				// entity
			// set current Times for updateTimestamp
			entity.setUpdateTimestamp(new Date(System.currentTimeMillis()));

			// set Organisation and LastEditor for the entity
			entity.setOrganisation(dataBaseService.getOrganisationById(dto
					.getOrganisationId()));
			entity.setLastEditor(dataBaseService.getPersonById(lastEditorId));

			// first scenario: incomingDeliveryId == 0 => create new
			// IncomingDelivery
			if (entity.getIncomingDeliveryId() == 0) {
				dataBaseService.setNewIncomingDelivery(entity); // set
																// NewIncomingDelivery
			} else // second scenario: if incomingDeliveryId != 0 => update
					// existing IncomingDelivery
			{
				dataBaseService.updateIncomingDelivery(entity);
			}

			// if no exception occurs
			return new ControllerMessage(true, "Speichern erfolgreich!");

		} catch (Exception e) {
			e.printStackTrace();

			// if an exception occurs
			return new ControllerMessage(false, "Speichern nicht erfolgreich!");
		}

	}

	/**
	 * Deletes an incomingDelivery by the specified id.
	 * 
	 * @param id
	 *            to delete
	 * @return a message with a state if successful or not and a textual
	 *         description
	 */
	@RequestMapping(value = "secure/incomingDelivery/deleteById/{id}")
	public ControllerMessage deleteIncomingDeliveryById(@PathVariable int id) {

		try {
			dataBaseService.deleteIncomingDeliveryById(id);
		} catch (Exception e) {
			e.printStackTrace();
			return new ControllerMessage(false, "Löschen nicht erfolgreich: "
					+ e.getMessage());
		}

		return new ControllerMessage(true, "Löschen erfolgreich");
	}

	/***** [END] incoming Deliveries *****/

	/***** [START] outgoing Deliveries *****/

	/**
	 * Gets all outgoing deliveries.
	 * 
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
	 * Gets all unarchived outgoing deliveries.
	 * 
	 * @return a dto representation
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
			log.info("returning all unarchived outgoing deliveries");

			return list;
		} else {
			log.info("no outgoing deliveries found");

			return null;
		}
	}

	/**
	 * Gets all outgoing deliveries, which are not booked (available).
	 * 
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
	 * 
	 * @param id
	 *            of the requested outgoing delivery id
	 * @return a dto representation of the delivery
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
	 * Sets an outgoing delivery dto.
	 * 
	 * @param dto
	 *            to save
	 * @param request
	 *            to determine the last editor
	 * @return a message with a state and a textual description
	 */
	@RequestMapping(value = "secure/outgoingDelivery/set")
	public ControllerMessage setOutgoingDelivery(
			@RequestBody OutgoingDeliveryDTO dto, HttpServletRequest request) {

		if (dto.getOutgoingDeliveryId() != 0) {
			try {
				dataBaseService.deleteOutgoingDeliveryById(dto
						.getOutgoingDeliveryId());
			} catch (Exception e) {
				e.printStackTrace();
				return new ControllerMessage(false,
						"Speichern nicht erfolgreich: " + e.getMessage());
			}
		}

		dto.setOutgoingDeliveryId(0);
		dto.setLastEditorId(dataBaseService.getPersonById(
				(int) request.getSession().getAttribute("id")).getPersonId());
		OutgoingDelivery entity = OutgoingDeliveryMapper.mapToEntity(dto,
				dataBaseService);
		// set current Times for updateTimestamp
		entity.setUpdateTimestamp(new Date(System.currentTimeMillis()));

		try {
			dataBaseService.setNewOutgoingDelivery(entity);
			log.info("saved outgoing delivery with id "
					+ entity.getOutgoingDeliveryId());
			return new ControllerMessage(true, "Speichern erfolgreich!");
		} catch (Exception e) {
			e.printStackTrace();
			log.severe("failed to save outgoing delivery with id "
					+ entity.getOutgoingDeliveryId());
			return new ControllerMessage(false, "Speichern nicht erfolgreich!");
		}

	}

	/**
	 * Deletes an outgoing delivery.
	 * 
	 * @param id
	 *            of object to delete
	 * @return a message with a state and textual description
	 */
	@RequestMapping(value = "secure/outgoingDelivery/deleteById/{id}")
	public ControllerMessage deleteOutgoingDeliveryById(@PathVariable int id) {

		try {
			dataBaseService.deleteOutgoingDeliveryById(id);
			return new ControllerMessage(true, "Löschen erfolgreich!");
		} catch (Exception e) {
			e.printStackTrace();
			return new ControllerMessage(false, "Löschen nicht erfolgreich: "
					+ e.getMessage());
		}

	}

	/***** [END] outgoing Deliveries *****/

	/***** [START] availableArticles *****/

	/**
	 * To determine all available articles in the storage.
	 * 
	 * @return a dto representation
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
		return availableDTOs;
	}

	/***** [END] availableArticles *****/

	/***** [START] Delivery list *****/

	/**
	 * Get all unarchived delivery lists.
	 * 
	 * @return dto representation
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
		return deliveryListDTOs;
	}
	
	/**
	 * Set DeliveryList to archived via id
	 * 
	 * @param id
	 *            of the requested delivery
	 * @param state
	 *            0/1 un/archived
	 * @return MessageObject with state
	 */
	@RequestMapping(value = "secure/deliveryList/setArchivedState/{id}/{state}")
	public ControllerMessage setDeliveryListState(@PathVariable int id,
			@PathVariable int state) {
		try {
			dataBaseService.archiveDeliveryListById(id, state);

			log.info("set delivery list with: " + id + " to " + state + ".");
			return new ControllerMessage(true,
					"Archive operation for delivery list successful");
		} catch (Exception e) {
			log.info("no delivery list with id " + id + " found");
			return new ControllerMessage(false, e.getMessage());
		}
	}
	
	/**
	 * Get all delivery lists.
	 * 
	 * @return dto representation
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
		return deliveryListDTOs;
	}

	/**
	 * Returns the requested delivery list.
	 * 
	 * @param id
	 *            of the requested list
	 * @return a dto representation
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

		return deliveryListDTO;
	}

	/**
	 * Sets a deliveryList.
	 * 
	 * @param dto
	 *            to set
	 * @param request
	 *            to determine the last editor
	 * @return a message with a state and a textual description
	 */
	@RequestMapping(value = "secure/deliveryList/set")
	public ControllerMessage setDeliveryList(@RequestBody DeliveryListDTO dto,
			HttpServletRequest request) {

		try {
			dto.setLastEditorId((int) request.getSession().getAttribute("id"));
			DeliveryList entity = DeliveryListMapper.mapToEntity(dto,
					dataBaseService);
			entity.setUpdateTimestamp(new Date(System.currentTimeMillis()));

			dataBaseService.setDeliveryList(entity);

			return new ControllerMessage(true, "Speichern erfolgreich!");

		} catch (Exception e) {
			e.printStackTrace();
			log.severe(e.getMessage());
			return new ControllerMessage(false, "Speichern nicht erfolgreich!");
		}
	}

	/**
	 * Deletes a delivery list
	 * 
	 * @param id
	 *            of object to delete
	 * @return a message with a state and textual description
	 */
	@RequestMapping(value = "secure/deliveryList/deleteById/{id}")
	public ControllerMessage deleteDeliveryListById(@PathVariable int id) {

		try {
			dataBaseService.deleteDeliveryListById(id);
			return new ControllerMessage(true, "Löschen erfolgreich!");
		} catch (Exception e) {
			e.printStackTrace();
			return new ControllerMessage(false, "Löschen nicht erfolgreich: "
					+ e.getMessage());
		}
	}

	/**
	 * Deletes a delivery list
	 * 
	 * @param id
	 *            of object to delete
	 * @return a message with a state and textual description
	 */
	@RequestMapping(value = "secure/deliveryList/exportAsWord/{id}")
	public void generateDeliveryListById(@PathVariable int id,
			HttpServletResponse httpServletResponse) {
		try {
			// Correct mime type
			httpServletResponse
					.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
			// get your file as InputStream

			DeliveryListDTO deliveryListDTO = DeliveryListMapper
					.mapToDTO(dataBaseService.getDeliveryListById(id));

			FileInputStream wordFile = new FileInputStream(
					WordGenerator.generate(
							deliveryListDTO,
							dataBaseService.getPersonById(
									deliveryListDTO.getLastEditorId())
									.getFirstName()
									+ " "
									+ dataBaseService.getPersonById(
											deliveryListDTO.getLastEditorId())
											.getLastName(),dataBaseService));
			// copy it to response's OutputStream
			IOUtils.copy(wordFile, httpServletResponse.getOutputStream());
			httpServletResponse.flushBuffer();
		} catch (Exception ex) {
			log.info("Error writing file to output stream." + ex.getMessage());
			throw new RuntimeException(ex);
		}
	}

	// DELETE OF DELIVERY LIST AND IF, HOW


	
}
