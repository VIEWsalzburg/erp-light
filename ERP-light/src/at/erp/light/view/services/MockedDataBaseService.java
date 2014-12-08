package at.erp.light.view.services;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import at.erp.light.view.model.Address;
import at.erp.light.view.model.Article;
import at.erp.light.view.model.Category;
import at.erp.light.view.model.City;
import at.erp.light.view.model.Country;
import at.erp.light.view.model.DeliveryList;
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

public class MockedDataBaseService implements IDataBase {

	// private SessionFactory sessionFactory;

	private List<Person> mockedPersons = new ArrayList<Person>();
	private List<Platformuser> mockedPlatformusers = new ArrayList<Platformuser>();
	
	
	private List<Organisation> mockedOrganisations = new ArrayList<Organisation>();
	private List<Category> mockedCategories = new ArrayList<Category>(); 
	
	private List<Type> mockedTypes = new ArrayList<Type>();
	
	private List<Permission> mockedPermissions = new ArrayList<Permission>();
	
	
	
	public MockedDataBaseService() {
		initCategories();
		initTypes();
		
		Set<Type> pTypes = new HashSet<Type>();
		// this.sessionFactory = sessionFactory;

		// initialize Persons

		// first Person
		Person person1 = new Person(1, "Herr", "Dr.", "Seppi", "Huber",
				"kommt vom Land", new Date(System.currentTimeMillis()), 1);
		Address address1 = new Address(1, "Seppis Straße 1");
		City city1 = new City(1, "Seppis Stadt", "2345");
		Country country1 = new Country(1, "Seppis Land");

		Email email1 = new Email(1, getTypeById(10), "seppi.huber@gmail.com");
		Telephone telephone1 = new Telephone(1, getTypeById(11), "0664 1234567");

		person1.setAddress(address1);
		person1.setCity(city1);
		person1.setCountry(country1);
		person1.getEmails().add(email1);
		person1.getTelephones().add(telephone1);
		person1.getTypes().add(getTypeById(3));
		Platformuser platformUser1 = new Platformuser(getPermissionById(1), person1,
				"admin", "admin");
		platformUser1.setPersonId(1);

		// second Person
		Person person2 = new Person(2, "Frau", "Bsc", "Susi", "Mayer",
				"kommt aus der Stadt", new Date(System.currentTimeMillis()), 1);
		Address address2 = new Address(2, "Susis Straße 2");
		City city2 = new City(2, "Susis Stadt", "3456");
		Country country2 = new Country(2, "Susis Land");

		Email email2 = new Email(2, getTypeById(10), "susi.mayer@gmail.com");
		Telephone telephone2 = new Telephone(2, getTypeById(11), "0664 9876543");

		person2.setAddress(address2);
		person2.setCity(city2);
		person2.setCountry(country2);
		person2.getEmails().add(email2);
		person2.getTelephones().add(telephone2);
		Platformuser platformUser2 = new Platformuser(getPermissionById(2), person2,
				"admin", "susi.mayer@gmail.com");
		platformUser2.setPersonId(2);

		// third Person
		Person person3 = new Person(3, "Herr", "MSc", "Maxi", "Neumann",
				"unterrichtet an der FH", new Date(System.currentTimeMillis()),
				1);
		Address address3 = new Address(3, "Maxis Straße 2");
		City city3 = new City(3, "Maxis Stadt", "4567");
		Country country3 = new Country(3, "Maxis Land");

		Email email3 = new Email(3, getTypeById(10), "maxi.neumann@gmail.com");
		Telephone telephone3 = new Telephone(1, getTypeById(11), "0664 5463728");

		person3.setAddress(address3);
		person3.setCity(city3);
		person3.setCountry(country3);
		person3.getEmails().add(email3);
		person3.getTelephones().add(telephone3);
		Platformuser platformUser3 = new Platformuser(getPermissionById(3), person3,
				"admin", "maxi.neumann@gmail.com");
		platformUser3.setPersonId(3);

		mockedPersons.add(person1);
		mockedPersons.add(person2);
		mockedPersons.add(person3);

		mockedPlatformusers.add(platformUser1);
		mockedPlatformusers.add(platformUser2);
		mockedPlatformusers.add(platformUser3);
		
		
		
		
		
		
		
		// Organisations
		Organisation Org1 = new Organisation(0, "eine Organisation", "Lieferscheine Ja", new Date(System.currentTimeMillis()), 1);
		Org1.setAddress(new Address(0, "Org1 Straße"));
		Org1.setCity(new City(0, "Org1 City", "Org1 Zip"));
		Org1.setCountry(new Country(0, "Österreich"));
		Org1.setPerson(person1);	// updated by
		
		Org1.getPersons().add(person2);
		Org1.getPersons().add(person3);
		
		Org1.getCategories().add(mockedCategories.get(0));
		Org1.getCategories().add(mockedCategories.get(1));
		
		Org1.getTypes().add(getTypeById(5));
		
		
		
		Organisation Org2 = new Organisation(1, "Kunde2 Org", "Lieferung vor 12:00 Uhr", new Date(System.currentTimeMillis()), 1);
		Org2.setAddress(new Address(1, "Kunde2 Straße"));
		Org2.setCity(new City(1, "Kunde2 City", "Kunde2 Zip"));
		Org2.setCountry(new Country(1, "Österreich"));
		Org2.setPerson(person1);	// updated by
		
		Org2.getPersons().add(person2);
		
		Org2.getCategories().add(mockedCategories.get(0));
		Org2.getCategories().add(mockedCategories.get(2));
		
		Org2.getTypes().add(getTypeById(6));
		
		mockedOrganisations.add(Org1);
		mockedOrganisations.add(Org2);
		
	}
	
	private void initPermissions()
	{
		Permission adminPermission = new Permission(1, "Admin", "");
		Permission readWritePermission = new Permission(2, "ReadWrite", "");
		Permission readPermission = new Permission(3, "Read", "");
		
		mockedPermissions.add(adminPermission);
		mockedPermissions.add(readWritePermission);
		mockedPermissions.add(readPermission);
	}
	
	private void initCategories()
	{
		// Categories
		Category Cat1 = new Category(0, "Fleisch", "Fleischdesc");
		Category Cat2 = new Category(1, "Fisch", "Fischdesc");
		Category Cat3 = new Category(2, "Brot", "Brotdesc");
		Category Cat4 = new Category(3, "Käse", "Käsedesc");
		
		mockedCategories.add(Cat1);
		mockedCategories.add(Cat2);
		mockedCategories.add(Cat3);
		mockedCategories.add(Cat4);
	}
	
	private void initTypes()
	{
		Type typeMitarbeiter = new Type(1, "Mitarbeiter");
		Type typeUnterstützer = new Type(2, "Unterstützer");
		Type typeMitglied = new Type(3, "Mitglied");
		Type typeGast = new Type(4, "Gast");
		Type typeLieferant = new Type(5, "Lieferant");
		Type typeKunde = new Type(6, "Kunde");
		Type typeSponsor = new Type(7, "Sponsor");
		
		Type typePrivat = new Type(10, "Privat");
		Type typeGeschäftlich = new Type(11, "Geschäftlich");
		
		mockedTypes.add(typeMitarbeiter);
		mockedTypes.add(typeUnterstützer);
		mockedTypes.add(typeMitglied);
		mockedTypes.add(typeGast);
		mockedTypes.add(typeLieferant);
		mockedTypes.add(typeKunde);
		mockedTypes.add(typeSponsor);
		
		mockedTypes.add(typePrivat);
		mockedTypes.add(typeGeschäftlich);
	}
	
	

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Person getPersonById(int id) {
		for (Person p : mockedPersons) {
			if (p.getPersonId() == id) {
				return p;
			}
		}
		return null;
	}

	@Override
	public int setIncomingDelivery(IncomingDelivery incomingDelivery) {

		return 0;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public Person getPersonById(int id, int FetchFlags) {
		for (Person p : mockedPersons) {
			if (p.getPersonId() == id) {
				return p;
			}
		}
		return null;
	}

	@Override
	public List<Person> getPersonsByType(Type type) {

		return null;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public List<Person> getAllPersons() {
		return mockedPersons;
	}

	@Override
	public Person getPersonByLoginEmail(String loginEmail) {
		return null;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public int setPerson(Person person) {
		
		Person found = null;
		for (Person p : mockedPersons)
		{
			if (p.getPersonId() == person.getPersonId())
				found = p;
		}
		
		if (found != null)
			mockedPersons.remove(found);	// remove old existing person
		
		mockedPersons.add(person);		// add new person
		return person.getPersonId();
	}

	
	@Override
	public Type getTypeById(int id) {
		
		for (Type type : mockedTypes)
		{
			if (type.getTypeId() == id)
				return type;
		}
		
		return null;
	}
	
	@Override
	public Type getTypeByType(String type) {
		
		for (Type mType : mockedTypes)
		{
			if (mType.getName().equals(type))
				return mType;
		}
		
		return null;
	}
	
	
	@Override
	public List<Type> getAllTypes() {
		return mockedTypes;
	}
	
	
	@Override
	public Platformuser getPlatformuserById(int id) {

		for (Platformuser pUser : mockedPlatformusers) {
			if (pUser.getPerson().getPersonId() == id) {
				return pUser;
			}
		}
		return null;
	}

	@Override
	public Platformuser getPlatformuserbyLoginEmail(String loginEmail) {

		for (Platformuser platformuser : mockedPlatformusers) {
			if (platformuser.getLoginEmail().equals(loginEmail)) {
				return platformuser;
			}
		}

		return null;
	}
	
	@Override
	public Platformuser setPlatformuser(Platformuser platformuser) {
		
		Platformuser found = null;
		for (Platformuser p : mockedPlatformusers) {
			if (p.getPerson().getPersonId() == platformuser.getPerson().getPersonId())
				found = p;
		}
		mockedPlatformusers.remove(found);
		mockedPlatformusers.add(platformuser);
		return platformuser;
	}

	@Override
	public void removePlatformuserById(int id) {
		
		Platformuser found = null;
		for (Platformuser p : mockedPlatformusers) {
			if (p.getPerson().getPersonId() == id)
				found = p;
		}
		mockedPlatformusers.remove(found);
	}
	

	@Override
	public Permission getPermissionById(int id) {
		
		for (Permission p : mockedPermissions)
		{
			if (p.getPermissionId() == id)
				return p;
		}
		
		return null;
	}

	@Override
	public Permission getPermissionByPermission(String permission) {
		
		for (Permission p : mockedPermissions)
		{
			if (p.getPermission().equals(permission))
				return p;
		}
		
		return null;
	}

	
	
	
	
	
	@Override
	public int setPersons(List<Person> persons) {

		mockedPersons=persons;
		return 0;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Organisation getOrganisationById(int id) {
		
		for (Organisation organisation : mockedOrganisations)
		{
			if (organisation.getOrganisationId()==id)
				return organisation;
		}
		
		return null;
	}

	@Override
	public List<Organisation> getOrganisationsByCategory(Category category) {

		return null;
	}

	@Override
	public List<Organisation> getAllOrganisations() {
		return mockedOrganisations;
	}

	@Override
	public int setOrganisation(Organisation organisation) {
		organisation.setOrganisationId(mockedOrganisations.size()+1);
		mockedOrganisations.add(organisation);
		return organisation.getOrganisationId();
	}

	@Override
	public int setOrganisations(List<Organisation> organisations) {

		return 0;
	}

	@Override
	public IncomingDelivery getIncomingDeliveryById(int id) {

		return null;
	}

	@Override
	public List<IncomingDelivery> getAllIncomingDeliveries() {

		return null;
	}

	@Override
	public int setIncomingDeliveries(List<IncomingDelivery> incomingDeliveries) {

		return 0;
	}

	@Override
	public IncomingArticle getIncomingArticleById(int id) {

		return null;
	}

	@Override
	public List<IncomingArticle> getAllIncomingArticles() {

		return null;
	}

	@Override
	public int setIncomingArticle(IncomingArticle incomingArticle) {

		return 0;
	}

	@Override
	public int setIncomingArticles(List<IncomingArticle> incomingArticles) {

		return 0;
	}

	@Override
	public Article getArticleById(int id) {

		return null;
	}

	@Override
	public List<Article> getAllArticles() {

		return null;
	}

	@Override
	public int setArticle(Article article) {

		return 0;
	}

	@Override
	public int setArticles(List<Article> articles) {

		return 0;
	}

	@Override
	public OutgoingArticle getOutgoingArticleById(int id) {

		return null;
	}

	@Override
	public List<OutgoingArticle> getAllOutgoingArticles() {

		return null;
	}

	@Override
	public int setOutgoingArticle(OutgoingArticle outgoingArticle) {

		return 0;
	}

	@Override
	public int setOutgoingArticles(List<OutgoingArticle> outgoingArticles) {

		return 0;
	}

	@Override
	public OutgoingDelivery getOutgoingDeliveryById(int id) {

		return null;
	}

	@Override
	public List<OutgoingDelivery> getAllOutgoingDeliveries() {

		return null;
	}

	@Override
	public int setOutgoingDelivery(OutgoingDelivery outgoingDelivery) {

		return 0;
	}

	@Override
	public int setOutgoingDeliveries(List<OutgoingDelivery> outgoingDeliveries) {

		return 0;
	}

	@Override
	public DeliveryList getDeliveryListById(int id) {

		return null;
	}

	@Override
	public List<DeliveryList> getAllDeliveryLists() {

		return null;
	}

	@Override
	public int setDeliveryList(DeliveryList deliveryList) {

		return 0;
	}

	@Override
	public int setDeliveryLists(List<DeliveryList> deliveryLists) {

		return 0;
	}

	@Override
	public int telephoneTest() {

		return 0;
	}

	

	







	
	
	@Override
	public Category getCategoryById(int id)
	{
		for (Category category : mockedCategories)
		{
			if (category.getCategoryId() == id)
				return category;
		}
		
		return null;
	}
	
	@Override
	public Category getCategoryByCategory(String category)
	{
		for (Category mCategory : mockedCategories)
		{
			if (mCategory.getCategory().equals(category))
				return mCategory;
		}
		
		return null;
	}

	@Override
	public Category setCategory(Category category) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
