package at.erp.light.view.mapper;

import at.erp.light.view.dto.OutgoingArticleDTO;
import at.erp.light.view.model.OutgoingArticle;

/**
 * This class acts as mapper class between entity OutgoingArticle and DTO OutgoingArticleDTO.
 * @author Matthias Schnöll
 *
 */
public class OutgoingArticleMapper {

	/**
	 * Maps the given entity to a DTO.
	 * @param outgoingArticle Entity from the DB
	 * @return DTO object
	 */
	public static OutgoingArticleDTO mapToDTO(OutgoingArticle outgoingArticle) {

		if (outgoingArticle == null) {
			return null;
		}

		OutgoingArticleDTO dto = new OutgoingArticleDTO();
		dto.setOutgoingArticleId(outgoingArticle.getOutgoingArticleId());
		dto.setArticleNr(outgoingArticle.getArticleNr());
		dto.setNumberpu(outgoingArticle.getNumberpu());
		dto.setArticleDTO(ArticleMapper.mapToDTO(outgoingArticle.getArticle()));

		return dto;
	}

	/**
	 * Maps the given DTO to an entity.
	 * @param dto DTO object from the frontend
	 * @return entity
	 */
	public static OutgoingArticle mapToEntity(
			OutgoingArticleDTO dto) {
		if(dto == null)
		{
			return null;
		}			
		
		OutgoingArticle entity = new OutgoingArticle();
		entity.setOutgoingArticleId(dto.getOutgoingArticleId());
		entity.setArticleNr(dto.getArticleNr());
		entity.setNumberpu(dto.getNumberpu());
		entity.setArticle(ArticleMapper.mapToEntity(dto.getArticleDTO()));
		
		return entity;
	}
}
