package at.erp.light.view.services;
import java.util.List;
import java.util.Set;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
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
import at.erp.light.view.model.Person;
import at.erp.light.view.model.Telephone;
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
	
	@Override
	public int setIncomingDelivery(IncomingDelivery incomingDelivery) {
		// TODO Auto-generated method stub
		return 0;
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
		
		if ( (FetchFlags&Person.FETCH_TELEPHONES)!=0)
		{
			Hibernate.initialize(person.getTelephones());
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
//		List<Person> persons = sessionFactory.getCurrentSession()
//			.createQuery("FROM Person p JOIN FETCH p.address JOIN FETCH p.city JOIN FETCH p.country JOIN FETCH p.emails JOIN FETCH p.telephones JOIN FETCH p.types").list();
		List<Person> persons = sessionFactory.getCurrentSession().createQuery("FROM Person").list();
		return persons;
	}

	@Override
	public Person getPersonByLoginEmail(String loginEmail) {
		// TODO Auto-generated method stub
		return null;
	}
	
	// setCountry
	@Transactional(propagation=Propagation.REQUIRED)
	public Country getCountryByCountry(String country) {
		Country mCountry = (Country) sessionFactory.getCurrentSession().
				createQuery("FROM Country c WHERE c.country = :country").
				setParameter("country", country).uniqueResult();
		if (mCountry==null)
		{
			mCountry = new Country(0, country);
			setCountry(mCountry);
		}
		return mCountry;
	}
	
	// setCountry
	@Transactional(propagation=Propagation.REQUIRED)
	public Country setCountry(Country country) {
		sessionFactory.getCurrentSession().saveOrUpdate(country);
		return country;
	}
	
	// getAddressByAddress
	@Transactional(propagation=Propagation.REQUIRED)
	public Address getAddressByAddress(String address) {
		Address mAddress = (Address) sessionFactory.getCurrentSession().
				createQuery("FROM Address a WHERE a.address = :address").
				setParameter("address", address).uniqueResult();
		if (mAddress==null)
		{
			mAddress = new Address(0, address);
			setAddress(mAddress);
		}
		return mAddress;
	}
	
	// setAddress
	@Transactional(propagation=Propagation.REQUIRED)
	public Address setAddress(Address address) {
		sessionFactory.getCurrentSession().saveOrUpdate(address);
		return address;
	}
	
	// setCity
	@Transactional(propagation=Propagation.REQUIRED)
	public City getCityByCityAndZip(String city, String zip) {
		City mCity = (City) sessionFactory.getCurrentSession().
				createQuery("FROM City c WHERE c.city = :city AND c.zip = :zip").
				setParameter("city", city).setParameter("zip", zip).uniqueResult();
		if (mCity==null)
		{
			mCity = new City(0, city, zip);
			setCity(mCity);
		}
		return mCity;
	}
	
	// setCity
	@Transactional(propagation=Propagation.REQUIRED)
	public City setCity(City city) {
		sessionFactory.getCurrentSession().saveOrUpdate(city);
		return city;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public int setPerson(Person person) {
		
		// update Address
		// if address == null => delete FK
		if (person.getAddress() != null)
		{
			// checks if Address already exists in database (if yes => retrieve existing one, if no => create new one and get it)
			Address Address = getAddressByAddress(person.getAddress().getAddress());
			person.setAddress(Address);
		}
		
		// update Country
		if (person.getCountry() != null)
		{
			// checks if Country already exists in database (if yes => retrieve existing one, if no => create new one and get it)
			Country country = getCountryByCountry(person.getCountry().getCountry());
			person.setCountry(country);
		}
		
		// update City
		if (person.getCity() != null)
		{
			// checks if City and Zip already exist in database (if yes => retrieve existing one, if no => create new one and get it)
			City city = getCityByCityAndZip(person.getCity().getCity(), person.getCity().getZip());
			person.setCity(city);
		}
		
		
		
		sessionFactory.getCurrentSession().saveOrUpdate(person);
		
		// rollback test
		if (1==0)
			throw(new HibernateException("rollback that shit! ^^"));
		
		return person.getPersonId();
		
	}

	@Transactional(propagation=Propagation.REQUIRED)
	public Type getTypeById(int id) {
		Query query = sessionFactory.getCurrentSession().createQuery("FROM Type t WHERE t.typeId = :id");
		query.setParameter("id", id);
		Type type = (Type)query.uniqueResult();
		return type;
	}
	
	@Override
	@Transactional
	public int telephoneTest() {
	
		Person mPerson = getPersonById(36);
		
		mPerson.setActive(1);
		
		Telephone tel = new Telephone(0, getTypeById(Type.PRIVAT), "00 43 5678");
		sessionFactory.getCurrentSession().saveOrUpdate(tel);
		
		Email email = new Email(0, getTypeById(Type.GESCHÄFTLICH), "mschnoell@hotmail.com");
		sessionFactory.getCurrentSession().saveOrUpdate(email);
		
		mPerson.getTelephones().add(tel);
		mPerson.getEmails().add(email);
		
		// mPerson.getTelephones().remove(mPerson.getTelephones().toArray()[0]);
		
		// tel.setPerson(mPerson);
		
//		Set<Telephone> telephones = (Set<Telephone>) mPerson.getTelephones();
//		telephones.add(tel);
//		mPerson.setTelephones(telephones);
		
		// sessionFactory.getCurrentSession().saveOrUpdate(mPerson);
		
		return 0;
		
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
