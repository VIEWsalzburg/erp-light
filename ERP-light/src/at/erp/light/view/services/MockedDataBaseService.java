package at.erp.light.view.services;

import java.sql.Date;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.HibernateException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sun.org.apache.xpath.internal.operations.Or;

import at.erp.light.view.dto.ReportDataDTO;
import at.erp.light.view.model.Address;
import at.erp.light.view.model.Article;
import at.erp.light.view.model.AvailArticleInDepot;
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
		initPermissions();
		
		Set<Type> pTypes = new HashSet<Type>();
		// this.sessionFactory = sessionFactory;

		// initialize Persons

		// first Person
		Person person1 = new Person(33, "Herr", "", "Josef", "Huber",
				"fährt of mit dem Bus", new Date(System.currentTimeMillis()), 1);
		Address address1 = new Address(53, "Bundesstraße 20B");
		City city1 = new City(1, "Mattsee", "5163");
		Country country1 = new Country(1, "Österreich");

		Email email1 = new Email(1, getTypeById(10), "seppi.huber@gmail.com");
		Telephone telephone1 = new Telephone(1, getTypeById(11), "0664 1234567");

		person1.setAddress(address1);
		person1.setCity(city1);
		person1.setCountry(country1);
		person1.getEmails().add(email1);
		person1.getTelephones().add(telephone1);
		person1.getTypes().add(getTypeById(3));
		person1.setLastEditor(person1);
		Platformuser platformUser1 = new Platformuser(getPermissionById(1), person1,
				"admin", "admin");
		platformUser1.setPersonId(1);
		person1.setPlatformuser(platformUser1);		// not intended for ProdDB
		
		
		// second Person
		Person person2 = new Person(14, "Frau", "", "Susanne", "Mayer",
				"", new Date(System.currentTimeMillis()), 1);
		Address address2 = new Address(2, "Karolingerstraße 2");
		City city2 = new City(2, "Salzburg", "5020");
		Country country2 = new Country(2, "Österreich");

		Email email2 = new Email(2, getTypeById(10), "susi.mayer@gmail.com");
		Telephone telephone2 = new Telephone(2, getTypeById(11), "0664 9876543");

		person2.setAddress(address2);
		person2.setCity(city2);
		person2.setCountry(country2);
		person2.getEmails().add(email2);
		person2.getTelephones().add(telephone2);
		person2.setLastEditor(person1);
		Platformuser platformUser2 = new Platformuser(getPermissionById(2), person2,
				"admin", "susi.mayer@gmail.com");
		platformUser2.setPersonId(2);
		person2.setPlatformuser(platformUser2);		// not intended for ProdDB

		// third Person
		Person person3 = new Person(19, "Herr", "", "Maximilian", "Neumann",
				"", new Date(System.currentTimeMillis()),
				1);
		Address address3 = new Address(3, "Dorfstraße 3");
		City city3 = new City(3, "Bergheim", "5101");
		Country country3 = new Country(3, "Österreich");

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
		person3.setPlatformuser(platformUser3);		// not intended for ProdDB

		mockedPersons.add(person1);
		mockedPersons.add(person2);
		mockedPersons.add(person3);

		mockedPlatformusers.add(platformUser1);
		mockedPlatformusers.add(platformUser2);
		mockedPlatformusers.add(platformUser3);
		
		
		
		
		
		
		
		// Organisations
		Organisation Org1 = new Organisation(33, "eine Organisation", "Lieferscheine Ja", new Date(System.currentTimeMillis()), 1);
		Org1.setAddress(new Address(0, "Org1 Straße"));
		Org1.setCity(new City(0, "Org1 City", "Org1 Zip"));
		Org1.setCountry(new Country(0, "Österreich"));
		Org1.setLastEditor(person1);	// updated by
		
		Org1.getContactPersons().add(person2);
		Org1.getContactPersons().add(person3);
		
		Org1.getCategories().add(mockedCategories.get(0));
		Org1.getCategories().add(mockedCategories.get(1));
		
		Org1.getTypes().add(getTypeById(5));
		
		
		
		Organisation Org2 = new Organisation(25, "Kunde2 Org", "Lieferung vor 12:00 Uhr", new Date(System.currentTimeMillis()), 1);
		Org2.setAddress(new Address(1, "Kunde2 Straße"));
		Org2.setCity(new City(1, "Kunde2 City", "Kunde2 Zip"));
		Org2.setCountry(new Country(1, "Österreich"));
		Org2.setLastEditor(person1);	// updated by
		
		Org2.getContactPersons().add(person2);
		
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
		Category Cat1 = new Category(10, "Fleisch", "Fleischdesc");
		Category Cat2 = new Category(20, "Fisch", "Fischdesc");
		Category Cat3 = new Category(30, "Brot", "Brotdesc");
		Category Cat4 = new Category(40, "Käse", "Käsedesc");
		
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
		
		Type typePrivat = new Type(10, "privat");
		Type typeGeschäftlich = new Type(11, "geschäftlich");
		
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
	public List<Person> getPersonsByType(Type type) {

		return null;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public List<Person> getAllPersons() {
		
		List<Person> list = new ArrayList<Person>();
		
		for (Person p : mockedPersons)
		{
			if (p.getActive() == 1)		// only return active persons
			{
				list.add(p);
			}
		}
		
		return list;
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
		
		if (person.getPersonId() == 0)
		{
			person.setPersonId(mockedPersons.size()+1);
		}
		
		mockedPersons.add(person);		// add new person
		return person.getPersonId();
	}

	
	@Override
	public int deletePersonById(int id) {
		Person p = this.getPersonById(id);
		p.setActive(0);		// set active flag to inactive (0)
		return 0;
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
			if (pUser.getPersonId() == id) {
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
		
		platformuser.setPersonId(platformuser.getPerson().getPersonId());
		
		Platformuser found = null;
		for (Platformuser p : mockedPlatformusers) {
			if (p.getPersonId() == platformuser.getPersonId())
				found = p;
		}
		mockedPlatformusers.remove(found);
		mockedPlatformusers.add(platformuser);
		
		getPersonById(platformuser.getPersonId()).setPlatformuser(platformuser);	// create loop for MockedDB <==> bidirectional in ProdDB
		
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
		List<Organisation> list = new ArrayList<Organisation>();
		
		for (Organisation o : mockedOrganisations)
		{
			if (o.getActive()==1)	// only add active organisations
			{
				list.add(o);
			}
		}
		
		return list;
	}

	@Override
	public int setOrganisation(Organisation organisation) {
		
		// assign Types
		Set<Type> types = new HashSet<Type>();
		for (Type type : organisation.getTypes())
		{
			types.add(this.getTypeByType(type.getName()));
		}
		organisation.setTypes(types);
		
		
		
		Organisation found = null;
		for (Organisation o : mockedOrganisations)
		{
			if (o.getOrganisationId() == organisation.getOrganisationId())
				found = o;
		}
		
		if (found != null)
			mockedOrganisations.remove(found);	// remove old existing person
		
		if (organisation.getOrganisationId() == 0)
		{
			organisation.setOrganisationId(mockedOrganisations.size()+1);
		}		
		
		
		mockedOrganisations.add(organisation);
		return organisation.getOrganisationId();
		
	}
	
	@Override
	public int deleteOrganisationById(int id) {
		Organisation o = this.getOrganisationById(id);
		o.setActive(0);		// set active flag to inactive (0)
		return 0;
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
		
		Category mCategory = new Category(0, category, "");
		this.setCategory(mCategory);
		return mCategory;
	}

	@Override
	public Category setCategory(Category category) {
		
		if (category.getCategoryId() == 0)
		{
			category.setCategoryId(mockedCategories.size()+1);
			mockedCategories.add(category);
			return category;
		}
		
		Category found = null;
		for (Category c : mockedCategories)
		{
			if (c.getCategoryId() == category.getCategoryId())
				found = c;
		}
		mockedCategories.remove(found);
		mockedCategories.add(category);
		
		return category;
	}
	
	public List<Category> getAllCategories()
	{
		return mockedCategories;
	}

	@Override
	public boolean deleteCategoryById(int id) {
		
		Category category = this.getCategoryById(id);
		mockedCategories.remove(category);
		
		return true;
	}

	@Override
	public List<Organisation> getOrganisationsByCategoryId(int id) {
		
		List<Organisation> list = new ArrayList<Organisation>();
		
		for (Organisation o : this.getAllOrganisations())
		{
			for (Category c : o.getCategories())
			{
				if (c.getCategoryId() == id)
					list.add(o);
			}
		}
		
		return list;
	}

	@Override
	public int setNewIncomingDelivery(IncomingDelivery incomingDelivery)
			throws HibernateException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean deleteIncomingDeliveryById(int id) throws HibernateException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<AvailArticleInDepot> getAvailableArticlesInDepot() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean checkInAndOutArticlePUs() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int setNewOutgoingDelivery(OutgoingDelivery outgoingDelivery)
			throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean deleteOutgoingDeliveryById(int id) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteDeliveryListById(int id) throws HibernateException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<OutgoingDelivery> getAvailableOutgoingDeliveries()
			throws HibernateException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int updateIncomingDelivery(IncomingDelivery incomingDelivery)
			throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean archiveIncomingDeliveryById(int id, int status)
			throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean archiveOutgoingDeliveryById(int id, int status)
			throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<IncomingDelivery> getAllIncomingDeliveries(int archivedStatus)
			throws HibernateException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<OutgoingDelivery> getAllOutgoingDeliveries(int archviedStatus)
			throws HibernateException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DeliveryList> getAllDeliveryLists(int archivedStatus)
			throws HibernateException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean archiveDeliveryListById(int id, int status)
			throws HibernateException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ReportDataDTO getIncomingReportByOrganisationId(int id,
			String dateFrom, String dateTo) throws ParseException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ReportDataDTO> getIncomingReportForAllOrganisations(
			String dateFrom, String dateTo) throws ParseException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ReportDataDTO getOutgoingReportByOrganisationId(int id,
			String dateFrom, String dateTo) throws ParseException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ReportDataDTO> getOutgoingReportForAllOrganisations(
			String dateFrom, String dateTo) throws ParseException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ReportDataDTO getTotalSumOfAllIncomingDeliveries(String dateFrom,
			String dateTo) throws ParseException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ReportDataDTO getTotalSumOfAllOutgoingDeliveries(String dateFrom,
			String dateTo) throws ParseException {
		// TODO Auto-generated method stub
		return null;
	}


	
	
}
