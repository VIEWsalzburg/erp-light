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

import at.erp.light.view.dto.CategoryDTO;
import at.erp.light.view.dto.OrganisationDTO;
import at.erp.light.view.mapper.CategoryMapper;
import at.erp.light.view.mapper.OrganisationMapper;
import at.erp.light.view.model.Category;
import at.erp.light.view.model.Organisation;
import at.erp.light.view.services.IDataBase;
import at.erp.light.view.state.ControllerMessage;

/**
 * This class is a RestController.<br/>
 * It manages calls concerning Categories.
 * @author Matthias Schnöll
 *
 */
@RestController
public class CategoryController {
	private static final Logger log = Logger.getLogger(CategoryController.class
			.getName());
	
	@Autowired
	private IDataBase dataBaseService;

	/**
	 * Returns a list with all categories of the system
	 * @return list of all categories
	 */
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
	
	/**
	 * Saves a new category in the system.<br/>
	 * @param category DTO object of the category; comes from the frontend
	 * @param request
	 * @return ControllerMessage showing the success of the call
	 */
	@RequestMapping(value = "secure/category/setCategory")
	public ControllerMessage setCategory(@RequestBody CategoryDTO category, HttpServletRequest request) {
		Category entity = CategoryMapper.mapToEntity(category);
		int lastEditorId = (int) request.getSession().getAttribute("id");
		
		try {
			String currentUserPermission = dataBaseService.getPlatformuserById(lastEditorId).getPermission().getPermission();
			if (currentUserPermission.equals("Admin") == false)
			{
				return new ControllerMessage(false, "Keine Berechtigung für diese Aktion!");
			}
			
			dataBaseService.setCategory(entity);
		} catch (HibernateException e) {
			e.printStackTrace();
			log.severe("saving category failed");
			return new ControllerMessage(false, "Speichern fehlgeschlagen!");
		}
		
		log.info("saving category successful");
		dataBaseService.insertLogging("[INFO] Kategorie mit der id "+entity.getCategoryId()+" gespeichert", lastEditorId);
		return new ControllerMessage(true, "Speichern erfoglreich!");
	}
	
	/**
	 * Gets a category with the given id
	 * @param id Id of the requested category
	 * @return CategoryDTO object or null, if Id not found
	 */
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

	/**
	 * Deletes a category with the given Id
	 * @param id Id of the category which should be deleted
	 * @param request
	 * @return ControllerMessage showing the success of the call
	 */
	@RequestMapping(value = "secure/category/deleteCategoryById/{id}")
	public ControllerMessage deleteCategoryById(@PathVariable int id, HttpServletRequest request) {

		int lastEditorId = (int) request.getSession().getAttribute("id");
		try {
			String currentUserPermission = dataBaseService.getPlatformuserById(lastEditorId).getPermission().getPermission();
			if (currentUserPermission.equals("Admin") == false)
			{
				return new ControllerMessage(false, "Keine Berechtigung für diese Aktion!");
			}
			
			dataBaseService.deleteCategoryById(id);
			log.info("deleting category with id "+id+" successful");
		} catch (HibernateException e) {
			e.printStackTrace();
			log.severe("deleting category with id "+id+" not successful");
			return new ControllerMessage(false, "Löschen fehlgeschlagen!");
		}
		
		dataBaseService.insertLogging("[INFO] Kategorie mit der id "+id+" gelöscht", lastEditorId);
		return new ControllerMessage(true, "Löschen erfolgreich!");

	}
	
	/**
	 * Returns a list of organisations, which are assigned with the given category.
	 * @param id Id of the category
	 * @return List of OrganisationDTO objects
	 */
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
