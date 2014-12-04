package at.erp.light.view.spring;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import at.erp.light.view.model.Address;
import at.erp.light.view.model.City;
import at.erp.light.view.model.Country;
import at.erp.light.view.model.Email;
import at.erp.light.view.model.Person;
import at.erp.light.view.model.Telephone;
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
	
	@RequestMapping(value="/Person/ID", params = {"id"}, method = RequestMethod.GET, headers="Accept=application/json")
	public @ResponseBody Person getPersonById( @RequestParam(value="id") int Id ){		
		Person p = dataBaseService.getPersonById(Id,Person.FETCH_ALL);
		
		Person mPerson = new Person(p.getPersonId(), p.getSalutation(), p.getTitle(), p.getFirstName(),
				p.getLastName(), p.getComment(), p.getUpdateTimestamp(), p.getActive());
		mPerson.setAddress(new Address(p.getAddress().getAddressId(), p.getAddress().getAddress()));
		
		return mPerson;
	}
	
	@RequestMapping(value="/Person/set/", params = {"id", "salutation", "title", "firstname",
			"lastname", "comment", "active", "address", "city", "zip", "country"})
	public ModelAndView setPerson(
			@RequestParam(value="id") int id,
			@RequestParam(value="salutation") String salutation,
			@RequestParam(value="title") String title,
			@RequestParam(value="firstname") String firstName,
			@RequestParam(value="lastname") String lastName,
			@RequestParam(value="comment") String comment,
			@RequestParam(value="active") int active,
			@RequestParam(value="address") String address,
			@RequestParam(value="city") String city,
			@RequestParam(value="zip") String zip,
			@RequestParam(value="country") String country
			){		
		
		Person mPerson = dataBaseService.getPersonById(id);
		if (mPerson == null)
		{
			mPerson = new Person(0, salutation, title, firstName, lastName, comment, new Date(System.currentTimeMillis()), active);
		}
		
		mPerson.setSalutation(salutation);
		mPerson.setTitle(title);
		mPerson.setFirstName(firstName);
		mPerson.setLastName(lastName);
		mPerson.setComment(comment);
		mPerson.setActive(active);
		mPerson.setUpdateTimestamp(new Date(System.currentTimeMillis()));
		
		if (address.equals(""))
			mPerson.setAddress(null);
		else
			mPerson.setAddress(new Address(0, address));
		
		if ( (city.equals("")) && (zip.equals("")) )
			mPerson.setCity(null);
		else
			mPerson.setCity(new City(0, city, zip));
		
		if (country.equals(""))
			mPerson.setCountry(null);
		else	
			mPerson.setCountry(new Country(0, country));
		
		dataBaseService.setPerson(mPerson);
		
		List<Person> personList = dataBaseService.getAllPersons();
		ModelAndView model = new ModelAndView("PersonList");
		model.addObject("allPersons", personList);
		return model;
	}
	
	
	
	@RequestMapping(value="/doSomething")
	public void doSomething() {
		
		// Exception Handling is best done in the calling context
		try{

			// Person mPerson = dataBaseService.getPersonById(33);

			Person mPerson = new Person(0,"Herr","","04 Matthias","Schnöll","Student",new Date(System.currentTimeMillis()), 1);

			mPerson.setAddress(new Address(0, "Alte Landstraße 10B"));
			mPerson.setCountry(new Country(10, "Irland"));
			mPerson.setCity(new City(25, "Oberndorf","5112"));
			// mPerson.setAddress(null);

			System.out.println("mPerson: "+mPerson.toString());

			// mPerson.setComment("neues Kommentar");

			dataBaseService.setPerson(mPerson);

			mPerson = dataBaseService.getPersonById(12);
			System.out.println("mPerson: "+mPerson.toString());
		}
		catch (Exception e)
		{
			System.out.println("Problem");
			e.printStackTrace();
		}
		
	}
	
	
	
	@RequestMapping(value="/telephoneTest")
	public void telephoneTest() {
		
		// Exception Handling is best done in the calling context
		try{

			dataBaseService.telephoneTest();
			
//			List<Person> allPersons = dataBaseService.getAllPersons();
//			
//			System.out.println("size: "+allPersons.size());
//			
//			for (Person mPerson : allPersons)
//			{
//			
//				// Person mPerson = dataBaseService.getPersonById(36, Person.FETCH_TELEPHONES | Person.FETCH_EMAILS);
//				
//				System.out.println("mPerson: "+mPerson.toString());
//				
//				for (Telephone telephone : mPerson.getTelephones())
//				{
//					System.out.println(telephone.getTelephone());
//				}
//				
//				for (Email email : mPerson.getEmails())
//				{
//					System.out.println(email.getEmail());
//				}
//			
//			}
			
		}
		catch (Exception e)
		{
			System.out.println("Problem");
			e.printStackTrace();
		}
		
	}
	
}
