package at.erp.light.view.mapper;

import at.erp.light.view.dto.IncomingArticleDTO;
import at.erp.light.view.model.IncomingArticle;

/**
 * This class acts as mapper class between entity IncomingArticle and DTO IncomingArticleDTO.
 * @author Matthias Schnöll
 *
 */
public class IncomingArticleMapper {

	/**
	 * Maps the given entity to a DTO.
	 * @param incomingArticle Entity from the DB
	 * @return DTO object
	 */
	public static IncomingArticleDTO mapToDTO(IncomingArticle incomingArticle) {
		
		if(incomingArticle == null)
		{
			return null;
		}			
		
		IncomingArticleDTO dto = new IncomingArticleDTO();
		dto.setIncomingArticleId(incomingArticle.getIncomingArticleId());
		dto.setArticleNr(incomingArticle.getArticleNr());
		dto.setNumberpu(incomingArticle.getNumberpu());
		dto.setArticleDTO(ArticleMapper.mapToDTO(incomingArticle.getArticle()));
		
		return dto;
	}

	/**
	 * Maps the given DTO to an entity.
	 * @param dto DTO object from the frontend
	 * @return entity
	 */
	public static IncomingArticle mapToEntity(
			IncomingArticleDTO dto) {
		
		if(dto == null)
		{
			return null;
		}			
		
		IncomingArticle entity = new IncomingArticle();
		entity.setIncomingArticleId(dto.getIncomingArticleId());
		entity.setArticleNr(dto.getArticleNr());
		entity.setNumberpu(dto.getNumberpu());
		entity.setArticle(ArticleMapper.mapToEntity(dto.getArticleDTO()));
		
		return entity;
	}

}
