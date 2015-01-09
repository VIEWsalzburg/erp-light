package at.erp.light.view.controller.article;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import at.erp.light.view.dto.IncomingDeliveryDTO;
import at.erp.light.view.dto.OutgoingDeliveryDTO;
import at.erp.light.view.mapper.IncomingDeliveryMapper;
import at.erp.light.view.mapper.OutgoingDeliveryMapper;
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

	@RequestMapping(value = "secure/incomingDelivery/getAll")
	public List<IncomingDeliveryDTO> getAllIncomingDeliveries() {

		List<IncomingDeliveryDTO> list = new ArrayList<IncomingDeliveryDTO>();

		List<IncomingDelivery> entityList = dataBaseService
				.getAllIncomingDeliveries();

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
	
	@RequestMapping(value = "secure/incomingDelivery/set")
	public ControllerMessage setIncomingDelivery(@RequestBody IncomingDeliveryDTO dto, HttpServletRequest request) {
		
		dto.setLastEditorId(dataBaseService.getPersonById((int) request.getSession().getAttribute("id")).getPersonId());
		IncomingDelivery entity = IncomingDeliveryMapper.mapToEntity(dto);
		
		dataBaseService.setNewIncomingDelivery(entity);
		
		return new ControllerMessage(true, "Speichern erfolgreich");
	}
	
	@RequestMapping(value = "secure/incomingDelivery/deleteById/{id}")
	public ControllerMessage deleteIncomingDeliveryById(@PathVariable int id) {
		
		try {
			dataBaseService.deleteIncomingDeliveryById(id);
		} catch (Exception e) {
			e.printStackTrace();
			return new ControllerMessage(true, "Löschen nicht erfolgreich: " + e.getMessage());
		}
		
		return new ControllerMessage(true, "Löschen erfolgreich");
	}
	
	
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

	@RequestMapping(value = "secure/outgoingDelivery/getById/{id}")
	public OutgoingDeliveryDTO getOutgoingDeliveryById(@PathVariable int id) {
		try {
			OutgoingDelivery outgoingDelivery = dataBaseService.getOutgoingDeliveryById(id);
			if (outgoingDelivery == null)
				throw new Exception();
			log.info("returning outgoing delivery with id: " + id);
			return OutgoingDeliveryMapper.mapToDTO(outgoingDelivery);
			
		} catch (Exception e) {
			log.info("no outgoing delivery with id " + id + " found");
			return null;
		}
	}
	
	
	@RequestMapping(value = "secure/outgoingDelivery/set")
	public ControllerMessage setOutgoingDelivery(@RequestBody OutgoingDeliveryDTO dto, HttpServletRequest request) {
		
		dto.setLastEditorId(dataBaseService.getPersonById((int) request.getSession().getAttribute("id")).getPersonId());
		OutgoingDelivery entity = OutgoingDeliveryMapper.mapToEntity(dto);
		
		dataBaseService.setOutgoingDelivery(entity);
		log.info("saved outgoing delivery with id " + entity.getOutgoingDeliveryId());

		return new ControllerMessage(true, "Speichern erfolgreich");
	}
	
	@RequestMapping(value = "secure/outgoingDelivery/deleteById/{id}")
	public ControllerMessage deleteOutgoingDeliveryById(@PathVariable int id) {
		throw new NotImplementedException();

//		try {
//			//dataBaseService.delete
//		} catch (Exception e) {
//			e.printStackTrace();
//			return new ControllerMessage(true, "Löschen nicht erfolgreich: " + e.getMessage());
//		}
		
//		return new ControllerMessage(true, "Löschen erfolgreich");
	}
	
}
