package at.erp.light.view.mapper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import at.erp.light.view.dto.ArticleDTO;
import at.erp.light.view.model.Article;

public class ArticleMapper {

	private static DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
	
	public static ArticleDTO mapToDTO(Article entity) {
		if(entity == null)
		{
			return null;
		}
		
		ArticleDTO dto = new ArticleDTO();
		
		
		dto.setArticleId(entity.getArticleId());
		dto.setDescription(entity.getDescription());
		dto.setMdd(df.format(entity.getMdd()));
		dto.setPackagingUnit(entity.getPackagingUnit());
		dto.setPricepu(entity.getPricepu().doubleValue());
		dto.setWeightpu(entity.getWeightpu());
		
		return dto;
	}

}
