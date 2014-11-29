package at.erp.light.view.services;
import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import at.erp.light.view.model.Article;
import at.erp.light.view.model.Category;
import at.erp.light.view.model.DeliveryList;
import at.erp.light.view.model.IncomingArticle;
import at.erp.light.view.model.IncomingDelivery;
import at.erp.light.view.model.Organisation;
import at.erp.light.view.model.OutgoingArticle;
import at.erp.light.view.model.OutgoingDelivery;
import at.erp.light.view.model.Person;
import at.erp.light.view.model.Type;


public class DataBaseService implements IDataBase {

	private SessionFactory sessionFactory;
		
	public DataBaseService(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

		
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public Person getPersonById(int id) {				
		Query query = sessionFactory.getCurrentSession().createQuery("FROM Person p WHERE p.personId = :id");
		query.setParameter("id", id);
		Person person = (Person)query.uniqueResult();
		return person;
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public Person getPersonById(int id, int FetchFlags) {
		Query query = sessionFactory.getCurrentSession().createQuery("FROM Person p WHERE p.personId = :id");
		query.setParameter("id", id);
		Person person = (Person)query.uniqueResult();
		
		if ( (FetchFlags&Person.FETCH_ADDRESS)!=0)
		{
			Hibernate.initialize(person.getAddress());
		}
		
		if ( (FetchFlags&Person.FETCH_CITY)!=0)
		{
			Hibernate.initialize(person.getCity());
		}
		
		if ( (FetchFlags&Person.FETCH_COUNTRY)!=0)
		{
			Hibernate.initialize(person.getCountry());
		}
		
		if ( (FetchFlags&Person.FETCH_PLATFORMUSER)!=0)
		{
			Hibernate.initialize(person.getPlatformuser());
		}
		
		if ( (FetchFlags&Person.FETCH_TYPES)!=0)
		{
			Hibernate.initialize(person.getTypes());
		}
		
		if ( (FetchFlags&Person.FETCH_EMAILS)!=0)
		{
			Hibernate.initialize(person.getEmails());
		}
		
		if ( (FetchFlags&Person.FETCH_TELEFONES)!=0)
		{
			Hibernate.initialize(person.getTelefones());
		}
		
		if ( (FetchFlags&Person.FETCH_UPDATED_BY_PERSON)!=0)
		{
			Hibernate.initialize(person.getPerson());
		}
		
		if ( (FetchFlags&Person.FETCH_ORGANISATION_CONTACT)!=0)
		{
			Hibernate.initialize(person.getOrganisation());
		}
		
		if ( (FetchFlags&Person.FETCH_ORGANISATIONS_UPDATED)!=0)
		{
			Hibernate.initialize(person.getOrganisations());
		}
		
		if ( (FetchFlags&Person.FETCH_OUTGOINGDELIVERIES_UPDATED)!=0)
		{
			Hibernate.initialize(person.getOutgoingDeliveries());
		}
		
		if ( (FetchFlags&Person.FETCH_INCOMINGDELIVERIES_UPDATED)!=0)
		{
			Hibernate.initialize(person.getIncomingDeliveries());
		}
		
		if ( (FetchFlags&Person.FETCH_PERSONS_UPDATED)!=0)
		{
			Hibernate.initialize(person.getPersons());
		}
		
		if ( (FetchFlags&Person.FETCH_DELIVERYLISTS_UPDATED)!=0)
		{
			Hibernate.initialize(person.getDeliveryLists());
		}
		
		return person;
	}

	@Override
	public List<Person> getPersonsByType(Type type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public List<Person> getAllPersons() {
		@SuppressWarnings("unchecked")
		List<Person> persons = sessionFactory.getCurrentSession().createQuery("FROM Person").list();
		return persons;
	}

	@Override
	public Person getPersonByLoginEmail(String loginEmail) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public int setPerson(Person person) {
		
		// 
		
		sessionFactory.getCurrentSession().saveOrUpdate(person);
		return person.getPersonId();
		
//		int personId = 0;
//		// ask for existing person with the provided personId
//		Person existingPerson = getPersonById(person.getPersonId());
//		if (existingPerson != null)
//		{
//			existingPerson.setSalutation(person.getSalutation());
//			existingPerson.setTitle(person.getTitle());
//			existingPerson.setFirstName(person.getFirstName());
//			existingPerson.setLastName(person.getLastName());
//			existingPerson.setComment(person.getComment());
//			existingPerson.setUpdateTimestamp(person.getUpdateTimestamp());
//			existingPerson.setActive(person.getActive());
//		} else {
//			personId = (Integer) sessionFactory.getCurrentSession().save(person);
//		}
//		
//		return personId;
		
	}

	@Override
	public int setPersons(List<Person> persons) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public Organisation getOrganisationById(int id) {
		Query query = sessionFactory.getCurrentSession().createQuery("FROM Organisation o WHERE o.organisationId = :id");
		query.setParameter("id", id);
		Organisation organisation = (Organisation)query.uniqueResult();
		return organisation;
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
	public int setIncomingDelivery(IncomingDelivery incomingDelivery) {
		// TODO Auto-generated method stub
		return 0;
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

}
