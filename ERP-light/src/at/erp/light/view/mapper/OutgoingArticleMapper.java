package at.erp.light.view.mapper;

import at.erp.light.view.dto.OutgoingArticleDTO;
import at.erp.light.view.model.OutgoingArticle;

public class OutgoingArticleMapper {

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
