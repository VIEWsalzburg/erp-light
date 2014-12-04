package at.erp.light.view.services;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

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
	
	Type typePrivat = new Type(Type.PRIVAT, "Privat");
	Permission permissionAdmin = new Permission(1, 1, "Admin");
		
	public MockedDataBaseService() {
		
		// this.sessionFactory = sessionFactory;
		
		// initialize Persons
		
		// first Person
		Person person1 = new Person(1, "Herr", "Dr.", "Seppi", "Huber", "kommt vom Land", new Date(System.currentTimeMillis()), 1); 
		Address address1 = new Address(1, "Seppis Straﬂe 1");
		City city1 = new City(1, "Seppis Stadt", "2345");
		Country country1 = new Country(1, "Seppis Land");
		
		Email email1 = new Email(1, typePrivat, "seppi.huber@gmail.com");
		Telephone telephone1 = new Telephone(1, typePrivat, "0664 1234567");
		
		person1.setAddress(address1);
		person1.setCity(city1);
		person1.setCountry(country1);
		person1.getEmails().add(email1);
		person1.getTelephones().add(telephone1);
		Platformuser platformUser1 = new Platformuser(permissionAdmin, person1, "admin", "seppi.huber@gmail.com");
		
		
		
		// second Person
		Person person2 = new Person(2, "Frau", "Bsc", "Susi", "Mayer", "kommt aus der Stadt", new Date(System.currentTimeMillis()), 1); 
		Address address2 = new Address(2, "Susis Straﬂe 2");
		City city2 = new City(2, "Susis Stadt", "3456");
		Country country2 = new Country(2, "Susis Land");
		
		Email email2 = new Email(2, typePrivat, "susi.mayer@gmail.com");
		Telephone telephone2 = new Telephone(2, typePrivat, "0664 9876543");
		
		person2.setAddress(address2);
		person2.setCity(city2);
		person2.setCountry(country2);
		person2.getEmails().add(email2);
		person2.getTelephones().add(telephone2);
		Platformuser platformUser2 = new Platformuser(permissionAdmin, person2, "admin", "susi.mayer@gmail.com");
		
		
		
		
		
		// third Person
		Person person3 = new Person(3, "Herr", "MSc", "Maxi", "Neumann", "unterrichtet an der FH", new Date(System.currentTimeMillis()), 1); 
		Address address3 = new Address(3, "Maxis Straﬂe 2");
		City city3 = new City(3, "Maxis Stadt", "4567");
		Country country3 = new Country(3, "Maxis Land");
		
		Email email3 = new Email(3, typePrivat, "maxi.neumann@gmail.com");
		Telephone telephone3 = new Telephone(1, typePrivat, "0664 5463728");
		
		person3.setAddress(address3);
		person3.setCity(city3);
		person3.setCountry(country3);
		person3.getEmails().add(email3);
		person3.getTelephones().add(telephone3);
		Platformuser platformUser3 = new Platformuser(permissionAdmin, person3, "admin", "maxi.neumann@gmail.com");
		
		mockedPersons.add(person1);
		mockedPersons.add(person2);
		mockedPersons.add(person3);
		
	}
	
		
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public Person getPersonById(int id) {				
		return mockedPersons.get(0);
	}
	
	@Override
	public int setIncomingDelivery(IncomingDelivery incomingDelivery) {
		// TODO Auto-generated method stub
		return 0;
	}


	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public Person getPersonById(int id, int FetchFlags) {
		return mockedPersons.get(0);
	}

	@Override
	public List<Person> getPersonsByType(Type type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public List<Person> getAllPersons() {
		return mockedPersons;
	}

	@Override
	public Person getPersonByLoginEmail(String loginEmail) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public int setPerson(Person person) {
		mockedPersons.add(person);
		return person.getPersonId();
		
	}

	@Override
	public int setPersons(List<Person> persons) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public Organisation getOrganisationById(int id) {
		return null;
	}

	@Override
	public List<Organisation> getOrganisationsByCategory(Category category) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Organisation> getAllOrganisations() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int setOrganisation(Organisation organisation) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int setOrganisations(List<Organisation> organisations) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public IncomingDelivery getIncomingDeliveryById(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<IncomingDelivery> getAllIncomingDeliveries() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int setIncomingDeliveries(List<IncomingDelivery> incomingDeliveries) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public IncomingArticle getIncomingArticleById(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<IncomingArticle> getAllIncomingArticles() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int setIncomingArticle(IncomingArticle incomingArticle) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int setIncomingArticles(List<IncomingArticle> incomingArticles) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Article getArticleById(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Article getAllArticles() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int setArticle(Article article) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int setArticles(List<Article> articles) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public OutgoingArticle getOutgoingArticleById(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<OutgoingArticle> getAllOutgoingArticles() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int setOutgoingArticle(OutgoingArticle outgoingArticle) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int setOutgoingArticles(List<OutgoingArticle> outgoingArticles) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public OutgoingDelivery getOutgoingDeliveryById(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<OutgoingDelivery> getAllOutgoingDeliveries() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int setOutgoingDelivery(OutgoingDelivery outgoingDelivery) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int setOutgoingDeliveries(List<OutgoingDelivery> outgoingDeliveries) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public DeliveryList getDeliveryListById(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DeliveryList> getAllDeliveryLists() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int setDeliveryList(DeliveryList deliveryList) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int setDeliveryLists(List<DeliveryList> deliveryLists) {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public int telephoneTest() {
		// TODO Auto-generated method stub
		return 0;
	}

}
