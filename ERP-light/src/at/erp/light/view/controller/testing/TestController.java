package at.erp.light.view.controller.testing;


import java.sql.Date;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import at.erp.light.view.model.Address;
import at.erp.light.view.model.Category;
import at.erp.light.view.model.City;
import at.erp.light.view.model.Country;
import at.erp.light.view.model.Email;
import at.erp.light.view.model.Organisation;
import at.erp.light.view.model.Permission;
import at.erp.light.view.model.Person;
import at.erp.light.view.model.Platformuser;
import at.erp.light.view.model.Telephone;
import at.erp.light.view.model.Type;
import at.erp.light.view.services.IDataBase;

@RestController
public class TestController {

	
	@Autowired
	private IDataBase dataBaseService;
	
	private TestController()
	{
		super();
	}
	
	
	@RequestMapping(value = "Test1")
	public void doSomething(@RequestParam(value="param") String param) {		
		
		// create new Person with all infos
		
		// Person mPerson = new Person(0, "Herr", "Dr.", "Herbert", "Huber", "ist Professor", new Date(System.currentTimeMillis()), 1);
		Person mPerson = dataBaseService.getPersonById(41);
		mPerson.setAddress(new Address(0, "Herberts Adresse"));
		mPerson.setCity(new City(0, "Salzburg", "5020"));
		mPerson.setCountry(new Country(0, "Österreich"));
		
		Set<Telephone> telephones = new HashSet<Telephone>();
		telephones.add(new Telephone(0, new Type(0, "privat"), "000012345"));
		mPerson.setTelephones(telephones);
		
		Set<Email> emails = new HashSet<Email>();
		emails.add(new Email(0, new Type(0, "privat"), "email@email.com"));
		mPerson.setEmails(emails);
		
		Set<Type> types = new HashSet<Type>();
		types.add(new Type(0, "Mitglied"));
		types.add(new Type(0, "Kunde"));
		mPerson.setTypes(types);
		
		dataBaseService.setPerson(mPerson);
		
		
		
		// new Platformuser assign Person to Platformuser
		Permission adminPermission = dataBaseService.getPermissionByPermission("ADMIN");
		Platformuser platformuser = new Platformuser(adminPermission, mPerson, "password", "loginemail@email.com");
		dataBaseService.setPlatformuser(platformuser);
		
		// update existing Platformuser
//		Platformuser platformuser = dataBaseService.getPlatformuserById(mPerson.getPersonId());
//		platformuser.setPassword("neues Passwort");
//		platformuser.setLoginEmail("neue loginemail");
//		dataBaseService.setPlatformuser(platformuser);
		
		// remove platformuser from person
//		dataBaseService.removePlatformuserById(mPerson.getPersonId());
		
		
		
		
		
		// dataBaseService.telephoneTest();
		
//		Person mPerson = dataBaseService.getPersonById(36);
//		
//		mPerson.getTypes().clear();
//		
//		mPerson.getTypes().add(dataBaseService.getTypeById(Type.KUNDE));
//		mPerson.getTypes().add(dataBaseService.getTypeById(Type.UNTERSTÜTZER));
//		
//		dataBaseService.setPerson(mPerson);
//		
//		for (Type type : mPerson.getTypes())
//		{
//			System.out.println(type.getName());
//		}
//		
//		System.out.println("hallo");
//		
//		Permission adminPermission = dataBaseService.getPermissionByPermission("ADMIN");
//		System.out.println(adminPermission.getPermission());
//		
//		Type typeGesch = dataBaseService.getTypeByType(param);
//		System.out.println("param: "+param);
//		System.out.println(typeGesch.getName());
		
		
//		***** PlatformTest *****
//		Person mPerson = dataBaseService.getPersonById(36);
//		Permission permissionAdmin = dataBaseService.getPermissionById(Permission.ADMIN);
//		
//		Platformuser mPlatformuser = dataBaseService.getPlatformuserById(mPerson.getPersonId());
//		
//		if (mPlatformuser==null)
//		{
//			mPlatformuser = new Platformuser();
//			mPlatformuser.setLoginEmail("neueloginemail@mail.com");
//			mPlatformuser.setPassword("neues Passwort");
//			mPlatformuser.setPermission(permissionAdmin);
//			mPlatformuser.setPerson(mPerson);
//		}
//		else
//		{
//			mPlatformuser.setLoginEmail("updatedloginemail@mail.com");
//			mPlatformuser.setPassword("updatedPassword");
//		}
//		
//		// dataBaseService.removePlatformuserById(36);
//		
//		dataBaseService.setPlatformuser(mPlatformuser);
		
		
		
		
		
		
//		***** Telephone and Email Test *****
//		Type typePrivat = dataBaseService.getTypeById(Type.PRIVAT);
//		Type typeGesch = dataBaseService.getTypeById(Type.GESCHÄFTLICH);
//		
//		Telephone mTelephone = new Telephone(0, typePrivat, "00001234");
//		Telephone mTelephone2 = new Telephone(0, typeGesch, "00001235");
//		
//		Email mEmail = new Email(0, typeGesch, "mschnoell@Privat.at");
//		Email mEmail2 = new Email(0, typePrivat, "mschnoell@gesch.at");
//		
//		Person mPerson = dataBaseService.getPersonById(36);
//		
//		mPerson.getTelephones().clear();
//		mPerson.getTelephones().add(mTelephone);
//		mPerson.getTelephones().add(mTelephone2);
//		
//		mPerson.getEmails().clear();
//		mPerson.getEmails().add(mEmail);
//		mPerson.getEmails().add(mEmail2);
//		
//		dataBaseService.setPerson(mPerson);
//		
//		System.out.println("called Test1");
//		
//		for (Telephone telephone : mPerson.getTelephones())
//		{
//			System.out.println(telephone.getTelephoneId()+" "+telephone.getTelephone()+" "+telephone.getType().getName());
//		}
//		
//		for (Email email : mPerson.getEmails())
//		{
//			System.out.println(email.getEmailId()+" "+email.getEmail()+" "+email.getType().getName());
//		}
		
		
	}
	
	@RequestMapping(value = "Test2")
	public void doSomething2(@RequestParam(value="param") String param) {		
		
		Category catFleisch = new Category(0, "Fleisch", "lecker Fleisch");
		dataBaseService.setCategory(catFleisch);
		
		
		Category catFisch = new Category(1, "Fisch", "lecker Fisch");
		dataBaseService.setCategory(catFisch);
		
		 Category ks = dataBaseService.getCategoryByCategory("großer Kühlschrank");
		 ks.setDescription("haben einen großen Kühlschrank");
		 dataBaseService.setCategory(ks);
		
		
		
	}
	
	@RequestMapping(value = "Test3")
	public void doSomething3() {		
		
		List<Category> categories = dataBaseService.getAllCategories();
		
		for (Category c : categories)
		{
			System.out.println(c.getCategoryId()+" "+c.getCategory()+" "+c.getDescription());
		}
	}
	
	@RequestMapping(value = "Test4")
	public void doSomething4(@RequestParam(value="param") String param) {		
		
		Organisation mOrganisation = new Organisation(0, param, param+"comment", new Date(System.currentTimeMillis()), 1);
		
		mOrganisation.setAddress(new Address(0, "Alte Landstraße 10A"));
		mOrganisation.setCity(new City(0, "Lamprechtshausen", "5112"));
		mOrganisation.setCountry(new Country(0, "Österreich"));
		
		mOrganisation.getTypes().add(new Type(0, "Kunde"));
		mOrganisation.getTypes().add(new Type(0, "Sponsor"));
		
		Person contact1 = new Person();
		contact1.setPersonId(36);
		Person contact2 = new Person();
		contact2.setPersonId(42);
		mOrganisation.getContactPersons().add(contact1);
		mOrganisation.getContactPersons().add(contact2);
		
		mOrganisation.getCategories().add(new Category(0,"großer Kühlschrank",""));
		mOrganisation.getCategories().add(new Category(0,"Fleisch", ""));
		
		mOrganisation.setLastEditor(dataBaseService.getPersonById(36));
		
		dataBaseService.setOrganisation(mOrganisation);
	}
	

}
