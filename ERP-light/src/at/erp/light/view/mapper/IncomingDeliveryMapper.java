package at.erp.light.view.mapper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
				dto.setOrganisationDTO(OrganisationMapper.mapToDTO(entity.getOrganisation()));
				dto.setLastEditorDTO(PersonMapper.mapToDTO(entity.getLastEditor()));
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
	
	
}
