package at.erp.light.view.controller.adressmanagement;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import at.erp.light.view.dto.CategoryDTO;
import at.erp.light.view.mapper.CategoryMapper;
import at.erp.light.view.model.Category;
import at.erp.light.view.services.IDataBase;

@RestController
public class CategoryController {

	@Autowired
	private IDataBase dataBaseService;

	@RequestMapping(value = "secure/category/getAllCategories")
	public List<CategoryDTO> getAllCategories() {
		List<CategoryDTO> catDTOList = new ArrayList<CategoryDTO>();
		for (Category entity : dataBaseService.getAllCategories()) {
			catDTOList.add(CategoryMapper.mapToDTO(entity));
		}

		return catDTOList;
	}
}
