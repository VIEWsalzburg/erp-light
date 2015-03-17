package at.erp.light.view.mapper;

import at.erp.light.view.dto.AvailableArticleDTO;
import at.erp.light.view.model.AvailArticleInDepot;

/**
 * This class acts as mapper class between entity AvailArticleInDepot and DTO AvailableArticleInDepot.
 * @author Matthias Schnöll
 *
 */
public class AvailableArticleMapper {

	
	/**
	 * Maps the given entity to a DTO
	 * @param entity Entity from DB
	 * @return DTO object
	 */
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

	/**
	 * Maps the given DTO to an entity
	 * @param dto DTO object from frontend
	 * @return entity
	 */
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
