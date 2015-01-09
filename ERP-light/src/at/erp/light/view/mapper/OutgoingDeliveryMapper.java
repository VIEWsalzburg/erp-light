package at.erp.light.view.mapper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import at.erp.light.view.dto.OutgoingArticleDTO;
import at.erp.light.view.dto.OutgoingDeliveryDTO;
import at.erp.light.view.model.OutgoingArticle;
import at.erp.light.view.model.OutgoingDelivery;
import at.erp.light.view.services.IDataBase;

public class OutgoingDeliveryMapper {
	private static DateFormat df = new SimpleDateFormat("dd.MM.yyyy");

	@Autowired
	private static IDataBase dataBaseService;
	
	public static OutgoingDeliveryDTO mapToDTO(OutgoingDelivery entity) {
		if (entity == null) {
			return null;
		}

		OutgoingDeliveryDTO dto = new OutgoingDeliveryDTO();

		dto.setOutgoingDeliveryId(entity.getOutgoingDeliveryId());
		dto.setDeliveryNr(entity.getDeliveryNr());
		dto.setOrganisationId(entity.getOrganisation().getOrganisationId());
		dto.setLastEditorId(entity.getLastEditor().getPersonId());
		dto.setDeliveryNr(entity.getDeliveryNr());
		dto.setDate(df.format(entity.getDate()));
		dto.setComment(entity.getComment());

		Set<OutgoingArticleDTO> outgoingArticleDTOs = new HashSet<OutgoingArticleDTO>();
		for (OutgoingArticle outgoingArticle : entity.getOutgoingArticles()) {
			outgoingArticleDTOs.add(OutgoingArticleMapper
					.mapToDTO(outgoingArticle));
		}

		dto.setOutgoingArticleDTOs(outgoingArticleDTOs);

		return dto;
	}

	public static OutgoingDelivery mapToEntity(OutgoingDeliveryDTO dto) {
		
		if(dto==null)
		{
			return null;
		}
		OutgoingDelivery entity = new OutgoingDelivery();

		
		entity.setOutgoingDeliveryId(dto.getOutgoingDeliveryId());
		entity.setDeliveryNr(dto.getDeliveryNr());
		entity.setOrganisation(dataBaseService.getOrganisationById(dto.getOrganisationId()));
		entity.setLastEditor(dataBaseService.getPersonById(dto.getLastEditorId()));
		entity.setDeliveryNr(dto.getDeliveryNr());
		try {
			entity.setDate(df.parse(dto.getDate()));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		entity.setComment(dto.getComment());
		
		Set<OutgoingArticle> outgoingArticles = new HashSet<OutgoingArticle>();
		for(OutgoingArticleDTO outgoingArticleDTO : dto.getOutgoingArticleDTOs())
		{
			outgoingArticles.add(OutgoingArticleMapper.mapToEntity(outgoingArticleDTO));
		}
		
		entity.setOutgoingArticles(outgoingArticles);
		
		return entity;
		
	}
}
