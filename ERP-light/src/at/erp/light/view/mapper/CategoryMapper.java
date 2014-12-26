package at.erp.light.view.mapper;

import at.erp.light.view.dto.CategoryDTO;
import at.erp.light.view.model.Category;

public class CategoryMapper {
	public static CategoryDTO mapToDTO(Category category) {
		CategoryDTO categoryDTO = new CategoryDTO();
		
		categoryDTO.setCategory(category.getCategory());
		categoryDTO.setCategoryId(category.getCategoryId());
		categoryDTO.setDescription(category.getDescription());
		return categoryDTO;
		
	}
	
	public static Category mapToEntity(CategoryDTO dto) {
		Category category = new Category();
		
		category.setCategory(dto.getCategory());
		category.setCategoryId(dto.getCategoryId());
		category.setDescription(dto.getDescription());
		return category;
		
	}
}
