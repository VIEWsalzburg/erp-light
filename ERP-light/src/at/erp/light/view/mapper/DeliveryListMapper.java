package at.erp.light.view.mapper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import at.erp.light.view.dto.DeliveryListDTO;
import at.erp.light.view.dto.OutgoingDeliveryDTO;
import at.erp.light.view.model.DeliveryList;
import at.erp.light.view.model.OutgoingDelivery;
import at.erp.light.view.services.IDataBase;

public class DeliveryListMapper {
	
	private static DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
	
	private static final Logger log = Logger.getLogger(DeliveryListMapper.class
			.getName());
	
	public static DeliveryListDTO mapToDTO(DeliveryList entity) {
		if(entity == null)
		{
			return null;
		}
		
		DeliveryListDTO dto = new DeliveryListDTO();
		dto.setDeliveryListId(entity.getDeliveryListId());
		dto.setLastEditorId(entity.getLastEditor().getPersonId());
		dto.setName(entity.getName());
		dto.setDate(df.format(entity.getDate()));
		dto.setComment(entity.getComment());
		dto.setUpdateTimestamp(df.format(entity.getUpdateTimestamp()));
		dto.setDriver(entity.getDriver());
		dto.setPassenger(entity.getPassenger());
		
		Set<OutgoingDeliveryDTO> outgoingDeliveryDTOs = new HashSet<OutgoingDeliveryDTO>();
		for(OutgoingDelivery outgoingDelivery : entity.getOutgoingDeliveries())
		{
			outgoingDeliveryDTOs.add(OutgoingDeliveryMapper.mapToDTO(outgoingDelivery));
		}
		dto.setOutgoingDeliveryDTOs(outgoingDeliveryDTOs);
		
		return dto;
	}

	public static DeliveryList mapToEntity(DeliveryListDTO dto, IDataBase dataBase) {
		
		if(dto == null)
		{
			return null;
		}
		
		DeliveryList entity = new DeliveryList();
		entity.setDeliveryListId(dto.getDeliveryListId());
		try{
		entity.setLastEditor(dataBase.getPersonById(dto.getLastEditorId()));
		}
		catch(Exception e)
		{
			log.severe(e.getMessage());
			e.printStackTrace();
		}
		entity.setName(dto.getName());
		
		try {
			entity.setDate(df.parse(dto.getDate()));
		} catch (ParseException e) {
			entity.setDate(new Date());
			e.printStackTrace();
		}
		
		if (dto.getUpdateTimestamp() != null)
		{
			try {
				entity.setUpdateTimestamp(df.parse(dto.getUpdateTimestamp()));
			} catch (ParseException e) {
				entity.setUpdateTimestamp(new Date());
				e.printStackTrace();
			}
		}
		
		entity.setComment(dto.getComment());
		entity.setDriver(dto.getDriver());
		entity.setPassenger(dto.getPassenger());
		
		Set<OutgoingDelivery> outgoingDeliveries = new HashSet<OutgoingDelivery>();
		for(OutgoingDeliveryDTO outgoingDeliveryDTO : dto.getOutgoingDeliveryDTOs())
		{
			outgoingDeliveries.add(OutgoingDeliveryMapper.mapToEntity(outgoingDeliveryDTO, dataBase));
		}
		entity.setOutgoingDeliveries(outgoingDeliveries);
		
		return entity;
	}
}
