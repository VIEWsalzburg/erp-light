package at.erp.light.view.mapper;

import at.erp.light.view.dto.CategoryDTO;
import at.erp.light.view.model.Category;

/**
 * This class acts as mapper class between entity Category and DTO CategoryDTO.
 * @author Matthias Schnöll
 *
 */
public class CategoryMapper {
	
	/**
	 * Maps the given entity to a DTO.
	 * @param category Entity from the DB
	 * @return DTO object
	 */
	public static CategoryDTO mapToDTO(Category category) {
		CategoryDTO categoryDTO = new CategoryDTO();
		
		categoryDTO.setCategory(category.getCategory());
		categoryDTO.setCategoryId(category.getCategoryId());
		categoryDTO.setDescription(category.getDescription());
		return categoryDTO;
		
	}
	
	/**
	 * Maps the given DTO to an entity.
	 * @param dto DTO object from the frontend
	 * @return entity
	 */
	public static Category mapToEntity(CategoryDTO dto) {
		Category category = new Category();
		
		category.setCategory(dto.getCategory());
		category.setCategoryId(dto.getCategoryId());
		category.setDescription(dto.getDescription());
		return category;
		
	}
}
