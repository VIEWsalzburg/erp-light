package at.erp.light.view.controller.testing;


import java.math.BigDecimal;
import java.math.BigInteger;
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
import at.erp.light.view.model.Article;
import at.erp.light.view.model.AvailArticleInDepot;
import at.erp.light.view.model.Category;
import at.erp.light.view.model.City;
import at.erp.light.view.model.Country;
import at.erp.light.view.model.Email;
import at.erp.light.view.model.IncomingArticle;
import at.erp.light.view.model.IncomingDelivery;
import at.erp.light.view.model.Organisation;
import at.erp.light.view.model.OutgoingArticle;
import at.erp.light.view.model.OutgoingDelivery;
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
	public void doSomething2(@RequestParam(value="param") int catId) {		
		
		System.out.println("before:");
		doSomething3();
		
		System.out.println("delete Category with id: "+catId);
		dataBaseService.deleteCategoryById(catId);
		
		// list all categories
		System.out.println("\nafter:");
		 doSomething3();
		 
		
		
	}
	
	@RequestMapping(value = "Test3")
	public void doSomething3() {		
		
		
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
	
	@RequestMapping(value = "WarenTest1")
	public void warenTest1() {		
		
		// create Articles
		Article article1 = new Article(0, "Bananen", "1kg", 1, new Date(2015, 1, 10), new BigDecimal(new BigInteger("99"), 2));
		Article article2 = new Article(0, "Gemüse", "5kg", 5, new Date(2015, 1, 11), new BigDecimal(new BigInteger("149"), 2));
		
		

		
		// create Delivery
		IncomingDelivery incomingDelivery = new IncomingDelivery();
		incomingDelivery.setComment("neue EingangsLieferung");
		incomingDelivery.setIncomingDeliveryId(0);
		incomingDelivery.setLastEditor(dataBaseService.getPersonById(36));
		incomingDelivery.setDate(new Date(System.currentTimeMillis()));
		incomingDelivery.setDeliveryNr(100);
		incomingDelivery.setOrganisation(dataBaseService.getOrganisationById(7));

		// create IncomingArticles
		// including the incomingDelivery is very important => IncomingArticle is the owning side => otherwise the not null constraint generates problems
		// as the foreign keys are updated at the end of the transaction (incomingdelivery is temporary NULL) when incomingDelivery is the owning side
		IncomingArticle incomingArticle1 = new IncomingArticle(0, null, article1, 0, 10.0);
		IncomingArticle incomingArticle2 = new IncomingArticle(0, null, article2, 1, 15.0);
		
		// add incomingArticles to the Delivery
		// following assignment is very important => otherwise the incomingArticles are not saved (though they represent the owning side of the relation
		incomingDelivery.getIncomingArticles().add(incomingArticle1);
		incomingDelivery.getIncomingArticles().add(incomingArticle2);
		
		
		try {
			dataBaseService.setNewIncomingDelivery(incomingDelivery);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
	@RequestMapping(value = "WarenTest2")
	public void warenTest2() {	
	
		List<AvailArticleInDepot> availArticleInDepots = dataBaseService.getAvailableArticlesInDepot();
		
		for (AvailArticleInDepot a : availArticleInDepots)
		{
			System.out.println("["+a.getArticleId()+"], "+a.getArticle().getDescription()+", "+a.getAvailNumberOfPUs());
		}
		
	}
	
	
	@RequestMapping(value = "WarenTest3")
	public void warenTest3() {	
	
		boolean validity = dataBaseService.checkInAndOutArticlePUs();
		
		System.out.println("The validity of the incoming and outgoing articles is: "+validity);
		
	}
	
	@RequestMapping(value = "WarenTest4")
	public void warenTest4() {	
	
		IncomingDelivery i = dataBaseService.getIncomingDeliveryById(24);
		
		System.out.println("Comm: "+i.getComment()+", OrgName: "+i.getOrganisation().getName()+", Date:"+i.getDate());
		
		for (IncomingArticle ia : i.getIncomingArticles())
		{
			System.out.println("#"+ia.getNumberpu()+", Desc:"+ia.getArticle().getDescription()+", PU:"+ia.getArticle().getPackagingUnit()+", Mdd:"+ia.getArticle().getMdd());
		}
		
	}
	
	@RequestMapping(value = "WarenTest5")
	public void warenTest5() {	
	
		try {
			dataBaseService.deleteIncomingDeliveryById(31);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@RequestMapping(value = "WarenTest6")
	public void warenTest6() {	
	
		List<IncomingDelivery> incomingDeliveries = dataBaseService.getAllIncomingDeliveries();
		
		for (IncomingDelivery i : incomingDeliveries)
		{
			System.out.println(i.getComment());
		}
		
	}
	
	
	// Test for outgoing Delivery
	@RequestMapping(value = "WarenTest10")
	public void warenTest10() {	
	
				
		// create Delivery
		OutgoingDelivery outgoingDelivery = new OutgoingDelivery();
		outgoingDelivery.setComment("neue AusgangsLieferung");
		outgoingDelivery.setOutgoingDeliveryId(0);
		outgoingDelivery.setLastEditor(dataBaseService.getPersonById(36));
		outgoingDelivery.setDate(new Date(System.currentTimeMillis()));
		outgoingDelivery.setDeliveryNr(90);
		outgoingDelivery.setOrganisation(dataBaseService.getOrganisationById(3));

		Article article1 = new Article();
		article1.setArticleId(53);
		Article article2 = new Article();
		article2.setArticleId(54);
		
		// 9 Brot von DB
		OutgoingArticle outgoingArticle1 = new OutgoingArticle(0, null, article1, 0, 9.0);
		// 18 x 0.5 kg Semmeln
		OutgoingArticle outgoingArticle2 = new OutgoingArticle(0, null, article2, 1, 18.0);
		
		outgoingDelivery.getOutgoingArticles().add(outgoingArticle1);
		outgoingDelivery.getOutgoingArticles().add(outgoingArticle2);
		
		
		try {
			dataBaseService.setNewOutgoingDelivery(outgoingDelivery);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value = "WarenTest11")
	public void warenTest11() {	
	
		try {
			dataBaseService.deleteOutgoingDeliveryById(22);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@RequestMapping(value = "WarenTest12")
	public void warenTest12() {	
	
		List<OutgoingDelivery> outgoingDeliveries = dataBaseService.getAllOutgoingDeliveries();
		
		for (OutgoingDelivery o : outgoingDeliveries)
		{
			System.out.println(o.getComment());
		}
		
	}
	
}
