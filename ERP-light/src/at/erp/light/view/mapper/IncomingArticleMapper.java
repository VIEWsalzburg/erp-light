package at.erp.light.view.mapper;

import at.erp.light.view.dto.IncomingArticleDTO;
import at.erp.light.view.model.IncomingArticle;

public class IncomingArticleMapper {

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
