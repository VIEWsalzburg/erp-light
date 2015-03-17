package at.erp.light.view.mapper;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import at.erp.light.view.dto.ArticleDTO;
import at.erp.light.view.model.Article;

/**
 * This class acts as mapper class between entity Article and DTO ArticleDTO.
 * @author Matthias Schnöll
 *
 */
public class ArticleMapper {

	private static DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
	
	/**
	 * Maps the given entity to a DTO.
	 * @param entity Entity from the DB
	 * @return DTO object
	 */
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
		dto.setDelivererId(entity.getDelivererId());
		
		return dto;
	}

	/**
	 * Maps the given DTO to an entity.
	 * @param dto DTO object from the frontend
	 * @return entity
	 */
	public static Article mapToEntity(ArticleDTO dto) {
		if(dto == null)
		{
			return null;
		}
		
		Article entity = new Article();
		
		
		entity.setArticleId(dto.getArticleId());
		entity.setDescription(dto.getDescription());
		try {
			entity.setMdd(df.parse(dto.getMdd()));
		} catch (ParseException e) {
			e.printStackTrace();
			entity.setMdd(new Date(System.currentTimeMillis()));
		}
		entity.setPackagingUnit(dto.getPackagingUnit());
		entity.setPricepu(BigDecimal.valueOf(dto.getPricepu()));
		entity.setWeightpu(dto.getWeightpu());
		// delivererId must not be set (it is retrieved by the DB when getting the article elements
		
		return entity;
	}

}
