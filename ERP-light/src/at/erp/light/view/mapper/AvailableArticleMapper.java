package at.erp.light.view.mapper;

import at.erp.light.view.dto.AvailableArticleDTO;
import at.erp.light.view.model.AvailArticleInDepot;

public class AvailableArticleMapper {

	public static AvailableArticleDTO mapToDTO(AvailArticleInDepot entity) {

		if (entity == null) {
			return null;
		}

		AvailableArticleDTO dto = new AvailableArticleDTO();
		dto.setArticleId(entity.getArticleId());
		dto.setAvailNumberOfPUs(entity.getAvailNumberOfPUs());
		dto.setArticleDTO(ArticleMapper.mapToDTO(entity.getArticle()));

		return dto;
	}

	public static AvailArticleInDepot mapToEntity(
			AvailableArticleDTO dto) {
		if(dto == null)
		{
			return null;
		}			
		
		AvailArticleInDepot entity = new AvailArticleInDepot();
		entity.setArticleId(dto.getArticleId());
		entity.setAvailNumberOfPUs(dto.getAvailNumberOfPUs());
		entity.setArticle(ArticleMapper.mapToEntity(dto.getArticleDTO()));
		
		return entity;
	}
}
