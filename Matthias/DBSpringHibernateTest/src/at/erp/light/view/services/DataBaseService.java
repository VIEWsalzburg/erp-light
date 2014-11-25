package at.erp.light.view.services;
import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Query;
import org.hibernate.SessionFactory;

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
	@Transactional
	public Person getPersonById(int id) {
				
		Query query = sessionFactory.getCurrentSession().createQuery("FROM Person p WHERE p.personId = :id");
		query.setParameter("id", id);
		Person person = (Person)query.uniqueResult();
		
		return person;
	}

	@Override
	public List<Person> getPersonsByType(Type type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Transactional
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
	@Transactional
	public int setPerson(Person person) {
		
//		Transaction tx = null;
//		int personId = 0;
//		
//		try {
//			tx = session.beginTransaction();
//			
//			// ask for existing person with the provided personId
//			Person updatePerson = getPersonById(person.getPersonId());
//			if (updatePerson!=null)
//			{
//				updatePerson = person;
//			} else {
//				personId = (Integer) session.save(person);
//			}
//			
//			tx.commit();
//			
//		} catch (HibernateException e) {
//			if (tx!=null) tx.rollback();
//			e.printStackTrace();
//		} finally {
//			
//		}
//		return personId;
		return 0;
	}

	@Override
	public int setPersons(List<Person> persons) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Organisation getOrganisationById(int id) {
		// TODO Auto-generated method stub
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
