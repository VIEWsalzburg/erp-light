package at.erp.light.view.spring;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import at.erp.light.view.model.Person;
import at.erp.light.view.services.DataBaseService;
import at.erp.light.view.services.IDataBase;

@Controller
public class PersonController {

	@Autowired
	private IDataBase dataBaseService;
	
	@RequestMapping(value="/Persons/")
	public ModelAndView home() {
		List<Person> allPersons = dataBaseService.getAllPersons();
		ModelAndView model = new ModelAndView("PersonList");
		model.addObject("allPersons", allPersons);
		return model;
	}
	
}
