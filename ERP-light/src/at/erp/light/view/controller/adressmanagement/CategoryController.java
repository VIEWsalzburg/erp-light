package at.erp.light.view.controller.adressmanagement;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import at.erp.light.view.controller.article.DeliveryController;
import at.erp.light.view.dto.CategoryDTO;
import at.erp.light.view.dto.OrganisationDTO;
import at.erp.light.view.mapper.CategoryMapper;
import at.erp.light.view.mapper.OrganisationMapper;
import at.erp.light.view.model.Category;
import at.erp.light.view.model.Organisation;
import at.erp.light.view.services.IDataBase;
import at.erp.light.view.state.ControllerMessage;

@RestController
public class CategoryController {
	private static final Logger log = Logger.getLogger(DeliveryController.class
			.getName());
	
	@Autowired
	private IDataBase dataBaseService;

	@RequestMapping(value = "secure/category/getAllCategories")
	public List<CategoryDTO> getAllCategories() {
		List<CategoryDTO> catDTOList = new ArrayList<CategoryDTO>();
		
		try {
			for (Category entity : dataBaseService.getAllCategories()) {
				catDTOList.add(CategoryMapper.mapToDTO(entity));
			}
		} catch (HibernateException e) {
			e.printStackTrace();
			log.severe("returning all categories failed");
			return null;
		}

		log.info("returning all categories");
		return catDTOList;
		
	}
	
	@RequestMapping(value = "secure/category/setCategory")
	public ControllerMessage setCategory(@RequestBody CategoryDTO category, HttpServletRequest request) {
		Category entity = CategoryMapper.mapToEntity(category);
		
		try {
			dataBaseService.setCategory(entity);
		} catch (HibernateException e) {
			e.printStackTrace();
			log.severe("saving category failed");
			return new ControllerMessage(false, "Speichern fehlgeschlagen!");
		}
		
		log.info("saving category successful");
		return new ControllerMessage(true, "Speichern erfoglreich!");
	}
	
	
	@RequestMapping(value = "secure/category/getCategoryById/{id}")
	public CategoryDTO getCategoryById(@PathVariable int id) {
		
		CategoryDTO c;
		
		try {
			Category category = dataBaseService.getCategoryById(id);
			c = CategoryMapper.mapToDTO(category);
			log.info("returning category with id "+id+" successful");
			return c;
		} catch (HibernateException e) {
			e.printStackTrace();
			log.severe("returning category with id "+id+" not successful");
		}
		
		return null;

	}

	@RequestMapping(value = "secure/category/deleteCategoryById/{id}")
	public ControllerMessage deleteCategoryById(@PathVariable int id) {

		
		try {
			dataBaseService.deleteCategoryById(id);
			log.info("deleting category with id "+id+" successful");
		} catch (HibernateException e) {
			e.printStackTrace();
			log.severe("deleting category with id "+id+" not successful");
			return new ControllerMessage(false, "Löschen fehlgeschlagen!");
		}
		
		return new ControllerMessage(true, "Löschen erfolgreich!");

	}
	
	@RequestMapping(value = "secure/category/getOrganisationsByCategoryId/{id}")
	public List<OrganisationDTO> getOrganisationsByCategory(@PathVariable int id) {
		
		try {
			List<OrganisationDTO> list = new ArrayList<OrganisationDTO>();
			List<Organisation> organisations = dataBaseService.getOrganisationsByCategoryId(id);
			
			for (Organisation o : organisations) {
				list.add(OrganisationMapper.mapToDTO(o));
			}
			log.info("getting organisations by category successful");
			
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			log.severe("getting organisations by category not successful");
		}
		
		return null;
		
	}
	
	
}
