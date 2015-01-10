package at.erp.light.view.mapper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import at.erp.light.view.dto.IncomingArticleDTO;
import at.erp.light.view.dto.IncomingDeliveryDTO;
import at.erp.light.view.model.IncomingArticle;
import at.erp.light.view.model.IncomingDelivery;


public class IncomingDeliveryMapper {
	private static DateFormat df = new SimpleDateFormat("dd.MM.yyyy");

	public static IncomingDeliveryDTO mapToDTO(IncomingDelivery entity) {
			if(entity==null)
			{
				return null;
			}
		
				IncomingDeliveryDTO dto = new IncomingDeliveryDTO();
			
				dto.setIncomingDeliveryId(entity.getIncomingDeliveryId());
				dto.setDeliveryNr(entity.getDeliveryNr());
				dto.setOrganisationId(entity.getOrganisation().getOrganisationId());
				dto.setLastEditorId(entity.getLastEditor().getPersonId());
				dto.setDeliveryNr(entity.getDeliveryNr());
				dto.setDate(df.format(entity.getDate()));
				dto.setComment(entity.getComment());
				
				Set<IncomingArticleDTO> incomingArticleDTOs = new HashSet<IncomingArticleDTO>();
				for(IncomingArticle incomingArticle : entity.getIncomingArticles())
				{
					incomingArticleDTOs.add(IncomingArticleMapper.mapToDTO(incomingArticle));
				}
				
				dto.setIncomingArticleDTOs(incomingArticleDTOs);
				
		return dto;
	}


	public static IncomingDelivery mapToEntity(IncomingDeliveryDTO dto) throws Exception {
		
		if(dto==null)
		{
			return null;
		}
		IncomingDelivery entity = new IncomingDelivery();

		
			entity.setIncomingDeliveryId(dto.getIncomingDeliveryId());
			entity.setDeliveryNr(dto.getDeliveryNr());
			
			// organisation must be set outside of the mapper (the static functions require a static dataBaseService which is not supported and permanently return null)
			// lastEditor must be set outside of the mapper (the static functions require a static dataBaseService which is not supported and permanently return null)
			
			entity.setDeliveryNr(dto.getDeliveryNr());
			try {
				entity.setDate(df.parse(dto.getDate()));
			} catch (ParseException e) {
				// set current Date if parsing fails
				entity.setDate(new Date(System.currentTimeMillis()));
				e.printStackTrace();
			}
			entity.setComment(dto.getComment());
			
			Set<IncomingArticle> incomingArticles = new HashSet<IncomingArticle>();
			for(IncomingArticleDTO incomingArticleDTO : dto.getIncomingArticleDTOs())
			{
				incomingArticles.add(IncomingArticleMapper.mapToEntity(incomingArticleDTO));
			}
			
			entity.setIncomingArticles(incomingArticles);
			
			return entity;
		
	}
	
	
}
