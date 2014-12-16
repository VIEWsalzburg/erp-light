package at.erp.light.view.controller.adressmanagement;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import at.erp.light.view.dto.CategoryDTO;
import at.erp.light.view.mapper.CategoryMapper;
import at.erp.light.view.model.Category;
import at.erp.light.view.services.IDataBase;
import at.erp.light.view.state.ControllerMessage;

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
	
	@RequestMapping(value = "secure/category/setCategory")
	public ControllerMessage setCategory(@RequestBody CategoryDTO category, HttpServletRequest request) {
		Category entity = CategoryMapper.mapToEntity(category);
		
		try {
			dataBaseService.setCategory(entity);
		} catch (HibernateException e) {
			e.printStackTrace();
			return new ControllerMessage(false, "Speichern fehlgeschlagen!");
		}
		
		return new ControllerMessage(true, "Speichern erfoglreich!");
	}
	
	
	@RequestMapping(value = "secure/category/getCategoryById/{id}")
	public CategoryDTO getCategoryById(@PathVariable int id) {
		
		List<CategoryDTO> list = new ArrayList<CategoryDTO>();

		for (Category c : dataBaseService.getAllCategories()) {
			list.add(CategoryMapper.mapToDTO(c));
		}

		for (CategoryDTO element : list) {
			if (element.getCategoryId() == id) {
				return element;
			}
		}

		return null;
	}

	@RequestMapping(value = "secure/category/deleteCategoryById/{id}")
	public ControllerMessage deleteCategoryById(@PathVariable int id) {

		/*	not implemented in ProdDB by now */
		List<Category> cList = dataBaseService.getAllCategories();
		List<Category> returnList = new ArrayList<Category>();

		for (Category c : cList) {
			if (c.getCategoryId() != id) {
				returnList.add(c);
			}
		}

		for(Category cat:returnList )
		{
			dataBaseService.setCategory(cat);
		}
		
		return new ControllerMessage(true, "Löschen erfolgreich!");
	}
	
	
}
