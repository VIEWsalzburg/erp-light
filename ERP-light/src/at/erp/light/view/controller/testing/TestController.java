package at.erp.light.view.controller.testing;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import at.erp.light.view.authenticate.HashGenerator;
import at.erp.light.view.model.Address;
import at.erp.light.view.model.Article;
import at.erp.light.view.model.Category;
import at.erp.light.view.model.City;
import at.erp.light.view.model.Country;
import at.erp.light.view.model.Email;
import at.erp.light.view.model.IncomingArticle;
import at.erp.light.view.model.IncomingDelivery;
import at.erp.light.view.model.Organisation;
import at.erp.light.view.model.OutgoingArticle;
import at.erp.light.view.model.OutgoingDelivery;
import at.erp.light.view.model.Person;
import at.erp.light.view.model.Telephone;
import at.erp.light.view.model.Type;
import at.erp.light.view.services.IDataBase;

/**
 * This class is a RestController.<br/>
 * It is used for testing purposes of single functionalities.
 * @author Matthias Schnöll
 *
 */
@RestController
public class TestController {

	@Autowired
	private IDataBase dataBaseService;
	
	private TestController()
	{
		super();
	}
	
	@RequestMapping(value = "HashPassword-default")
	public String hashPassword() {
		return HashGenerator.hashPasswordWithSalt("default");
	}
	
	@RequestMapping(value = "getDatabaseSize")
	public String getDatabaseSize() {
		try {
			return ""+dataBaseService.getDatabaseSize();
		} catch(Exception e) {
			return "Error";
		}
	}
	
	@RequestMapping(value = "generateDummyPersons")
	public String generateDummyPersons(@RequestParam(value="count") int count, 
			@RequestParam(value="offset") int offset)
	{
		
		for (int i=offset; i<count+offset; i++)
		{
			Person p = new Person(0, "HeFr", "", "Firs"+i,"Las"+i,"ab", new Date(), 1);
			p.setAddress(new Address(0, "Address "+i));
			p.getTelephones().add(new Telephone(0, new Type(0, "TestType"), "Telefon"+i));
			p.getEmails().add(new Email(0, new Type(0, "TestType"), "Email"+i));
			p.setCity(new City(0, "Salzburg", "5020"));
			p.setCountry(new Country(0, "Österreich"));
			p.getTypes().add(new Type(0, "TestType"));
			
			dataBaseService.setPerson(p);
		
		}
		
		return "Inserted Persons "+offset+" to "+(offset+count);
	}
	
	
	@RequestMapping(value = "generateDummyOrganisations")
	public String generateDummyOrganisations(@RequestParam(value="count") int count, 
			@RequestParam(value="offset") int offset)
	{
		
		Person lastEditor = dataBaseService.getPersonById(1);
		
		for (int i=offset; i<count+offset; i++)
		{
			Organisation o = new Organisation(0, "Organisation "+i, "Comment", new Date(), 1);
			o.setAddress(new Address(0, "Address "+i));
			o.setCity(new City(0, "Salzburg", "5020"));
			o.setCountry(new Country(0, "Österreich"));
			o.getTypes().add(new Type(0, "TestType"));
			o.getCategories().add(new Category(1, "Salzburg", "Salzburg"));
			o.setLastEditor(lastEditor);
			dataBaseService.setOrganisation(o);
		
		}
		
		return "Inserted Organisations "+offset+" to "+(offset+count);
	}
	
	
	@RequestMapping(value = "generateDummyWaren")
	public String generateDummyWaren(@RequestParam(value="count") int count, 
			@RequestParam(value="offset") int offset) throws Exception
	{
		Organisation org = dataBaseService.getOrganisationById(1);
		Person lastEditor = dataBaseService.getPersonById(1);
		
		
		for (int i=offset; i<offset+count; i++)
		{
		
			// create new IncomingDelivery
			IncomingDelivery inc = new IncomingDelivery(0, org, lastEditor, new Date(), "Kommentar ein "+i, new Date(), 0);
			inc.setDeliveryNr(0);
			
			// create incomingArticles
			for (int j=0; j<4; j++)
			{
				Article a = new Article(0, "article"+i+"."+j, "packaging"+i+"."+j, j, new Date(), new BigDecimal(j));
				IncomingArticle ia = new IncomingArticle(0, inc, a, j, 10+j);
				inc.getIncomingArticles().add(ia);	
			}
		
			
			int id = dataBaseService.setNewIncomingDelivery(inc);
			IncomingDelivery incomingDelivery = dataBaseService.getIncomingDeliveryById(id);
			
			for (IncomingArticle ia : incomingDelivery.getIncomingArticles())
			{
				OutgoingDelivery out = new OutgoingDelivery(0, org, lastEditor, new Date(), "Kommentar aus "+i, new Date(), 0);
				out.setDeliveryNr(0);
				OutgoingArticle oa = new OutgoingArticle(0, out, ia.getArticle(), 0, ia.getNumberpu());
				
				out.getOutgoingArticles().add(oa);
				
				dataBaseService.setNewOutgoingDelivery(out);
				
				
			}
			
		}
		
		
		
		return "Inserted Waren "+offset+" to "+(offset+count);
	}
	
	
}
