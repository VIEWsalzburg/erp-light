package at.erplight.services;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.omg.CORBA.OBJ_ADAPTER;

import at.erplight.model.Article;
import at.erplight.model.Category;
import at.erplight.model.DeliveryList;
import at.erplight.model.IncomingArticle;
import at.erplight.model.IncomingDelivery;
import at.erplight.model.Organisation;
import at.erplight.model.OutgoingArticle;
import at.erplight.model.OutgoingDelivery;
import at.erplight.model.Person;
import at.erplight.model.Type;
import at.erplight.services.IDataBase;


public class DataBaseService implements IDataBase {

	private static SessionFactory factory;
	
	private static DataBaseService dataBaseInstance = new DataBaseService();
	
	private static Configuration configuration;
	private static Session session;
	
	private DataBaseService() {
		
		try {
			// http://stackoverflow.com/questions/8621906/is-buildsessionfactory-deprecated-in-hibernate-4 - 13.11.2014 22:29
			// http://www.tutorialspoint.com/hibernate/hibernate_examples.htm - 13.11.2014 22:30
			configuration = new Configuration();
			configuration.configure("hibernate.cfg.xml");
			ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
			factory = configuration.buildSessionFactory(serviceRegistry);
			
		} catch (Throwable ex) {
			System.err.println("Failed to create sessionFactory object. "+ex);
		}
	}
	
	public static IDataBase getInstance(){
		return dataBaseInstance;
	}
	
	public void openSession() {
		 session = factory.openSession();
	}
	
	public void closeSession() {
		 session.close();
	}
	
	public void closeFactory() {
		if (factory != null)
			factory.close();
	}
		
	@Override
	public Person getPersonById(int id) {
		Transaction tx = null;
		Person person = new Person();
		
		try {
			tx = session.beginTransaction();
			Query query = session.createQuery("FROM Person p WHERE p.personId = :id");
			query.setParameter("id", id);
			person = (Person)query.uniqueResult();
			tx.commit();
		} catch (HibernateException e){
				if (tx!=null) tx.rollback();
				e.printStackTrace();
		} finally {
			
		}
		
		return person;
	}

	@Override
	public List<Person> getPersonsByType(Type type) {

		return null;
	}

	@Override
	public List<Person> getAllPersons() {

		Transaction tx = null;
		List<Person> persons = new ArrayList<Person>();
		
		try {
			tx = session.beginTransaction();
			persons = session.createQuery("FROM Person").list();
			tx.commit();
		} catch (HibernateException e) {
			if (tx!=null) tx.rollback();
			e.printStackTrace();
		} finally {

		}
		
		return persons;
	}

	@Override
	public Person getPersonByLoginEmail(String loginEmail) {
 
		return null;
	}

	@Override
	public int setPerson(Person person) {
		
		Transaction tx = null;
		int personId = 0;
		
		try {
			tx = session.beginTransaction();
			
			// ask for existing person with the provided personId
			Person updatePerson = getPersonById(person.getPersonId());
			if (updatePerson!=null)
			{
				updatePerson = person;
			} else {
				personId = (Integer) session.save(person);
			}
			
			tx.commit();
			
		} catch (HibernateException e) {
			if (tx!=null) tx.rollback();
			e.printStackTrace();
		} finally {
			
		}
		return personId;
	}

	@Override
	public int setPersons(List<Person> persons) {

		return 0;
	}

	@Override
	public Organisation getOrganisationById(int id) {

		return null;
	}

	@Override
	public List<Organisation> getOrganisationsByCategory(Category category) {

		return null;
	}

	@Override
	public List<Organisation> getAllOrganisations() {

		return null;
	}

	@Override
	public int setOrganisation(Organisation organisation) {

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
	public int setIncomingDelivery(IncomingDelivery incomingDelivery) {
 
		return 0;
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
	public Article getAllArticles() {
 
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

}
