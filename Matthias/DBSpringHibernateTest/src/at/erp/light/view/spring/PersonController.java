package at.erp.light.view.spring;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import at.erp.light.view.model.Person;
import at.erp.light.view.services.DataBaseService;
import at.erp.light.view.services.IDataBase;

@Controller
public class PersonController {

	@Autowired
	private IDataBase dataBaseService;
	
	@RequestMapping(value="/Person/all")
	public ModelAndView home() {
		List<Person> allPersons = dataBaseService.getAllPersons();
		ModelAndView model = new ModelAndView("PersonList");
		model.addObject("allPersons", allPersons);
		return model;
	}
	
	@RequestMapping(value="/Person/", params = {"id"})
	public ModelAndView personById( @RequestParam(value="id") int Id ){		
		List<Person> personList = new ArrayList<Person>();
		Person person = dataBaseService.getPersonById(Id);
		personList.add(person);
		ModelAndView model = new ModelAndView("PersonList");
		model.addObject("allPersons", personList);
		return model;
	}
	
}
