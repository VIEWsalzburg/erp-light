package at.erp.light.view.controller.article;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import at.erp.light.view.dto.IncomingDeliveryDTO;
import at.erp.light.view.mapper.IncomingDeliveryMapper;
import at.erp.light.view.model.IncomingDelivery;
import at.erp.light.view.services.IDataBase;

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

		if (entityList != null) {
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

		List<IncomingDelivery> entityList = dataBaseService
				.getAllIncomingDeliveries();

		if (entityList != null) {
			for (IncomingDelivery incomingDelivery : dataBaseService
					.getAllIncomingDeliveries()) {
				if (incomingDelivery.getIncomingDeliveryId() == id) {
					log.info("returning deliveries with id: " + id);
					return IncomingDeliveryMapper.mapToDTO(incomingDelivery);
				}
			}
		} else {
			log.info("no incoming delivery for id " + id + " found");

			return null;
		}
		return null;
	}
}
