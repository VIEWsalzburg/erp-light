package at.erp.light.view.services;
import java.util.HashSet;
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

//TODO rollbackFor=[Exceptions] => check and create custom annotation if needed
// http://stackoverflow.com/questions/3701376/rollback-on-every-checked-exception-whenever-i-say-transactional

@Transactional(propagation=Propagation.REQUIRED)
public class DataBaseService implements IDataBase {

	private SessionFactory sessionFactory;
		
	public DataBaseService(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

		
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public Person getPersonById(int id) throws HibernateException {				
		Query query = sessionFactory.getCurrentSession().createQuery("FROM Person p left join fetch p.lastEditor WHERE p.personId = :id");
		query.setParameter("id", id);
		Person person = (Person)query.uniqueResult();
		return person;
	}
	
	

	@Override
	public List<Person> getPersonsByType(Type type) throws HibernateException {
		
		return null;
	}
	
	

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public List<Person> getAllPersons() throws HibernateException {
		@SuppressWarnings("unchecked")
		List<Person> persons = sessionFactory.getCurrentSession().createQuery("FROM Person p left join fetch p.lastEditor WHERE p.active=1 ORDER BY p.lastName").list();
		return persons;
	}

	
	
	@Override
	public Person getPersonByLoginEmail(String loginEmail) {
		
		return null;
	}
	
	
	
	// setCountry
	@Transactional(propagation=Propagation.REQUIRED)
	public Country getCountryByCountry(String country) throws HibernateException {
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
	public Country setCountry(Country country) throws HibernateException {
		sessionFactory.getCurrentSession().saveOrUpdate(country);
		return country;
	}
	
	// getAddressByAddress
	@Transactional(propagation=Propagation.REQUIRED)
	public Address getAddressByAddress(String address) throws HibernateException {
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
	public Address setAddress(Address address) throws HibernateException {
		sessionFactory.getCurrentSession().saveOrUpdate(address);
		return address;
	}
	
	// setCity
	@Transactional(propagation=Propagation.REQUIRED)
	public City getCityByCityAndZip(String city, String zip) throws HibernateException {
		City mCity = (City) sessionFactory.getCurrentSession()
				.createQuery("FROM City c WHERE c.city = :city AND c.zip = :zip")
				.setParameter("city", city).setParameter("zip", zip).uniqueResult();
		if (mCity==null)
		{
			mCity = new City(0, city, zip);
			this.setCity(mCity);
		}
		return mCity;
	}
	
	// setCity
	@Transactional(propagation=Propagation.REQUIRED)
	public City setCity(City city) throws HibernateException {
		sessionFactory.getCurrentSession().saveOrUpdate(city);
		return city;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public int setPerson(Person person) throws HibernateException {
		
		// update Address
		// if address == null => delete FK
		if (person.getAddress() != null)
		{
			// check if Address is not ""
			if (person.getAddress().getAddress().isEmpty())
			{
				person.setAddress(null);
			}	
			else
			{
				// checks if Address already exists in database (if yes => retrieve existing one, if no => create new one and get it)
				Address Address = getAddressByAddress(person.getAddress().getAddress());
				person.setAddress(Address);
			}
		}
		
		// update Country
		if (person.getCountry() != null)
		{
			// check if Country is not ""
			if (person.getCountry().getCountry().isEmpty())
			{
				person.setCountry(null);
			}	
			else
			{
				// checks if Country already exists in database (if yes => retrieve existing one, if no => create new one and get it)
				Country country = getCountryByCountry(person.getCountry().getCountry());
				person.setCountry(country);
			}
		}
		
		// update City
		if (person.getCity() != null)
		{
			if (person.getCity().getCity().isEmpty() && person.getCity().getZip().isEmpty())
			{
				person.setCity(null);
			}
			else
			{
				// checks if City and Zip already exist in database (if yes => retrieve existing one, if no => create new one and get it)
				City city = getCityByCityAndZip(person.getCity().getCity(), person.getCity().getZip());
				person.setCity(city);
			}
		}
		
		// update Telephones
		Set<Telephone> telephones = new HashSet<Telephone>();
		for (Telephone telephone : person.getTelephones())
		{
			telephones.add(this.getTelephoneByTelephone(telephone.getTelephone(), telephone.getType().getName()));
		}
		person.setTelephones(telephones);
		
		// update Emails
		Set<Email> emails = new HashSet<Email>();
		for (Email email : person.getEmails())
		{
			emails.add(this.getEmailByEmail(email.getEmail(), email.getType().getName()));
		}
		person.setEmails(emails);
		
		Set<Type> types = new HashSet<Type>();
		for (Type type : person.getTypes())
		{
			types.add(this.getTypeByType(type.getName()));
		}
		person.setTypes(types);
		
		// Set Types should be set in Controller because Types are "static"
		
		// final update all in DB
		sessionFactory.getCurrentSession().saveOrUpdate(person);
		
		return person.getPersonId();
		
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public int deletePersonById(int id) throws HibernateException {
		Person p = this.getPersonById(id);
		p.setActive(0);	// set active flag to inactive (0)
		return 0;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public Type getTypeById(int id) throws HibernateException {
		Query query = sessionFactory.getCurrentSession().createQuery("FROM Type t WHERE t.typeId = :id");
		query.setParameter("id", id);
		Type type = (Type)query.uniqueResult();
		return type;
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public Type getTypeByType(String type) throws HibernateException
	{
		Query query = sessionFactory.getCurrentSession().createQuery("FROM Type t WHERE t.name = :type");
		query.setParameter("type", type);
		Type mType = (Type)query.uniqueResult();
		return mType;
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED) 
	public List<Type> getAllTypes() throws HibernateException {
		Query query = sessionFactory.getCurrentSession().createQuery("FROM Type t ORDER BY t.name");
		@SuppressWarnings("unchecked")
		List<Type> types = (List<Type>)query.list();
		return types;
	}
	
	
	@Transactional(propagation=Propagation.REQUIRED)
	public Telephone setTelephone(Telephone telephone) throws HibernateException
	{
		sessionFactory.getCurrentSession().saveOrUpdate(telephone);
		return telephone;
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	public Telephone getTelephoneByTelephone(String telephone, String type) throws HibernateException
	{
		Telephone mTelephone = (Telephone) sessionFactory.getCurrentSession().createQuery("FROM Telephone t WHERE t.telephone = :telephone")
				.setParameter("telephone", telephone).uniqueResult();
		if (mTelephone == null)
		{
			mTelephone = new Telephone();
			mTelephone.setTelephone(telephone);
			mTelephone.setType(getTypeByType(type));
			this.setTelephone(mTelephone);
		}
		mTelephone.setType(getTypeByType(type));
		
		return mTelephone;
	}
	
	
	@Transactional(propagation=Propagation.REQUIRED)
	public Email setEmail(Email email) throws HibernateException
	{
		sessionFactory.getCurrentSession().saveOrUpdate(email);
		return email;
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	public Email getEmailByEmail(String email, String type) throws HibernateException
	{
		Email mEmail = (Email) sessionFactory.getCurrentSession().createQuery("FROM Email e WHERE e.email = :email")
				.setParameter("email", email).uniqueResult();
		if (mEmail == null)
		{
			mEmail = new Email();
			mEmail.setEmail(email);
			mEmail.setType(getTypeByType(type));
			this.setEmail(mEmail);
		}
		mEmail.setType(getTypeByType(type));
		
		return mEmail;
	}
	
	@Override
	@Transactional
	public Platformuser getPlatformuserById(int id) throws HibernateException
	{
		Platformuser platformuser = (Platformuser) sessionFactory.getCurrentSession().createQuery("FROM Platformuser p WHERE p.personId = :id")
				.setParameter("id", id).uniqueResult();
		return platformuser;
	}
	
	@Override
	@Transactional
	public Platformuser getPlatformuserbyLoginEmail(String loginEmail) throws HibernateException
	{
		Platformuser platformuser = (Platformuser) sessionFactory.getCurrentSession().createQuery("FROM Platformuser p WHERE p.loginEmail = :loginEmail")
				.setParameter("loginEmail", loginEmail).uniqueResult();
		return platformuser;
	}
	
	@Override
	@Transactional
	public Platformuser setPlatformuser(Platformuser platformuser) throws HibernateException
	{
		sessionFactory.getCurrentSession().saveOrUpdate(platformuser);
		return platformuser;
	}
	
	@Override
	@Transactional
	public void removePlatformuserById(int id) throws HibernateException
	{
		Platformuser platformuser = this.getPlatformuserById(id);
		if (platformuser != null)
			sessionFactory.getCurrentSession().delete(platformuser);
	}
	
	@Override
	@Transactional
	public Permission getPermissionById(int id) throws HibernateException
	{
		Permission permission = (Permission) sessionFactory.getCurrentSession().createQuery("From Permission p WHERE p.permissionId = :id")
				.setParameter("id", id).uniqueResult();
		return permission;
	}
	
	@Override
	@Transactional
	public Permission getPermissionByPermission(String permission) throws HibernateException
	{
		Permission mPermission = (Permission) sessionFactory.getCurrentSession().createQuery("From Permission p WHERE p.permission = :permission")
				.setParameter("permission", permission).uniqueResult();
		return mPermission;
	}
	
	
	@Override
	@Transactional
	public int telephoneTest() throws HibernateException {
	
//		List<Article> availableArticleList = sessionFactory.getCurrentSession().createQuery("Select a From AvailArticleInDepot aInDep Join aInDept.article as a").list();
//		
//		for (Article article : availableArticleList)
//		{
//			System.out.println("Article: "+article.getDescription());
//		}
		
		
//		int id = 36;
//		Person mPerson = getPersonById(id);
//		mPerson.setActive(3);
//		
//		mPerson.setPlatformuser(null);
//		
//		Permission adminPermission = new Permission(1, 1, "ADMIN");
//		sessionFactory.getCurrentSession().saveOrUpdate(adminPermission);
//		
//		Platformuser platformuser = new Platformuser();
//		platformuser.setPerson(mPerson);
//		platformuser.setLoginEmail("mschnoell@gmx.net");
//		platformuser.setPassword("hallo");
//		platformuser.setPermission(adminPermission);
//		
//		sessionFactory.getCurrentSession().saveOrUpdate(platformuser);
		

//		
//		
//		
//		System.out.println("step 1");
//		
//		
//		platformuser.setPermission(adminPermission);
//		
//		System.out.println("step 2");
//		platformuser.setPerson(mPerson);
//		sessionFactory.getCurrentSession().saveOrUpdate(platformuser);
//		// mPerson.setPlatformuser(platformuser);
//		
//		System.out.println("step 3");
		// sessionFactory.getCurrentSession().saveOrUpdate(mPerson);
		
		
		
		
		
		
//		Person mPerson = getPersonById(36);
//		
//		mPerson.setActive(1);
//		
//		Telephone tel = new Telephone(0, getTypeById(Type.PRIVAT), "00 43 5678");
//		sessionFactory.getCurrentSession().saveOrUpdate(tel);
//		
//		Email email = new Email(0, getTypeById(Type.GESCHÄFTLICH), "mschnoell@hotmail.com");
//		sessionFactory.getCurrentSession().saveOrUpdate(email);
//		
//		mPerson.getTelephones().add(tel);
//		mPerson.getEmails().add(email);
		
		// mPerson.getTelephones().remove(mPerson.getTelephones().toArray()[0]);
		
		// tel.setPerson(mPerson);
		
//		Set<Telephone> telephones = (Set<Telephone>) mPerson.getTelephones();
//		telephones.add(tel);
//		mPerson.setTelephones(telephones);
		
		// sessionFactory.getCurrentSession().saveOrUpdate(mPerson);
		
		return 0;
		
	}
	
	
	
	@Override
	public int setPersons(List<Person> persons) throws HibernateException {
		
		return 0;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public Organisation getOrganisationById(int id) throws HibernateException {
		Query query = sessionFactory.getCurrentSession().createQuery("FROM Organisation o WHERE o.organisationId = :id");
		query.setParameter("id", id);
		Organisation organisation = (Organisation)query.uniqueResult();
		return organisation;
	}

	@Override
	public List<Organisation> getOrganisationsByCategory(Category category) throws HibernateException {
		
		return null;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public List<Organisation> getAllOrganisations() throws HibernateException {
		
		@SuppressWarnings("unchecked")
		List<Organisation> organisations = sessionFactory.getCurrentSession().createQuery("FROM Organisation o WHERE o.active=1 ORDER BY o.name").list();
		return organisations;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public int setOrganisation(Organisation organisation) throws HibernateException {


		// update Address
		// if address == null => delete FK
		if (organisation.getAddress() != null)
		{
			// check if Address is not ""
			if (organisation.getAddress().getAddress().isEmpty())
			{
				organisation.setAddress(null);
			}	
			else
			{
				// checks if Address already exists in database (if yes => retrieve existing one, if no => create new one and get it)
				Address Address = getAddressByAddress(organisation.getAddress().getAddress());
				organisation.setAddress(Address);
			}
		}

		// update Country
		if (organisation.getCountry() != null)
		{
			// check if Country is not ""
			if (organisation.getCountry().getCountry().isEmpty())
			{
				organisation.setCountry(null);
			}	
			else
			{
				// checks if Country already exists in database (if yes => retrieve existing one, if no => create new one and get it)
				Country country = getCountryByCountry(organisation.getCountry().getCountry());
				organisation.setCountry(country);
			}
		}

		// update City
		if (organisation.getCity() != null)
		{
			if (organisation.getCity().getCity().isEmpty() && organisation.getCity().getZip().isEmpty())
			{
				organisation.setCity(null);
			}
			else
			{
				// checks if City and Zip already exist in database (if yes => retrieve existing one, if no => create new one and get it)
				City city = getCityByCityAndZip(organisation.getCity().getCity(), organisation.getCity().getZip());
				organisation.setCity(city);
			}
		}


		Set<Type> types = new HashSet<Type>();
		for (Type type : organisation.getTypes())
		{
			types.add(this.getTypeByType(type.getName()));
		}
		organisation.setTypes(types);


		Set<Category> categories = new HashSet<Category>();
		for (Category category : organisation.getCategories())
		{
			categories.add(this.getCategoryByCategory(category.getCategory()));
		}
		organisation.setCategories(categories);


		// Ansprechpersonen
		Set<Person> contactPersons = new HashSet<Person>();
		for (Person p : organisation.getContactPersons())
		{
			contactPersons.add(this.getPersonById(p.getPersonId()));
		}
		organisation.setContactPersons(contactPersons);


		// final update all in DB
		sessionFactory.getCurrentSession().saveOrUpdate(organisation);

		return organisation.getOrganisationId();

	}
	
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public int deleteOrganisationById(int id) throws HibernateException {
		Organisation o = this.getOrganisationById(id);
		o.setActive(0);	// set active flag to inactive (0)
		return 0;
	}
	

	@Override
	public int setOrganisations(List<Organisation> organisations) throws HibernateException {
		
		return 0;
	}

	
	/***** Start der Warenverwaltung *****/
	
	
	/* check validity of the incoming articles vs the outgoing articles */
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public boolean checkInAndOutArticlePUs()
	{
		// get a list of all availArticleInDepot where the availablenumber is negativ (outgoingPUs > incomingPUs)
		@SuppressWarnings("unchecked")
		List<AvailArticleInDepot> list = sessionFactory.getCurrentSession().createQuery("From AvailArticleInDepot a Where a.availNumberOfPUs < 0").list();
		
		// check is false if at least one article is in the list
		if (list.size()>0)
			return false;
		else
			return true;	
	}
	
	
	/***** [START] incoming deliveries *****/
	
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public int setNewIncomingDelivery(IncomingDelivery incomingDelivery) throws Exception {
		
		// set the incoming delivery for each incoming article to be sure they are updated
		for (IncomingArticle incomingArticle : incomingDelivery.getIncomingArticles())
		{
			incomingArticle.setIncomingDelivery(incomingDelivery);
		}
		
		// add all new articles to the table articles
		// set the incomingArticleIds to 0 so they are created
		// set articleIds to 0 so they are created
		for (IncomingArticle incomingArticle : incomingDelivery.getIncomingArticles())
		{
			incomingArticle.setIncomingArticleId(0);
			incomingArticle.getArticle().setArticleId(0);
			sessionFactory.getCurrentSession().saveOrUpdate(incomingArticle.getArticle());
		}
		
		// set incomingDelivery Id to 0 so it is created new
		incomingDelivery.setIncomingDeliveryId(0);
		
		sessionFactory.getCurrentSession().saveOrUpdate(incomingDelivery);
		
		return incomingDelivery.getIncomingDeliveryId();
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public IncomingDelivery getIncomingDeliveryById(int id) throws HibernateException {
		
		IncomingDelivery incomingDelivery = (IncomingDelivery) sessionFactory.getCurrentSession().
				createQuery("From IncomingDelivery i Where i.incomingDeliveryId = :id").setParameter("id", id).uniqueResult();
		
		return incomingDelivery;
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public boolean deleteIncomingDeliveryById(int id) throws Exception {
		
		IncomingDelivery incomingDelivery = this.getIncomingDeliveryById(id);
		
		sessionFactory.getCurrentSession().delete(incomingDelivery);
		
		// call flush to execute pending SQL Statements and synchronize Context to DB 
		sessionFactory.getCurrentSession().flush();
		
		// check incoming and outgoing articles for validity
		if (this.checkInAndOutArticlePUs()==false)
			throw new Exception("Number of PUs for incoming and outgoing articles are not valid.");
		
		return true;
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public List<IncomingDelivery> getAllIncomingDeliveries() throws HibernateException {
		@SuppressWarnings("unchecked")
		List<IncomingDelivery> incomingDeliveries = sessionFactory.getCurrentSession().createQuery("From IncomingDelivery i order by i.date DESC").list();
		return incomingDeliveries;
	}
	
	/***** [END] incoming deliveries *****/
	
	
	/***** [START] articles *****/
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public List<AvailArticleInDepot> getAvailableArticlesInDepot()
	{
		// Test VIEW AvailArticleInDepot
	
		@SuppressWarnings("unchecked")
		List<AvailArticleInDepot> availArticleInDepots = sessionFactory.getCurrentSession().createQuery("From AvailArticleInDepot Where availNumberOfPUs != 0").list();
	
		return availArticleInDepots;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public Article getArticleById(int id) throws HibernateException {
		Article article = (Article)sessionFactory.getCurrentSession().createQuery("From Article a Where a.articleId = :id").setParameter("id", id).uniqueResult();
		return article;
	}
	
	/***** [END] articles *****/
	
	
	
	/***** [START] outgoing deliveries *****/
	@Override
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public int setNewOutgoingDelivery(OutgoingDelivery outgoingDelivery) throws Exception {
		
		// set Id to 0 => so the outgoingDelivery is created
		outgoingDelivery.setOutgoingDeliveryId(0);
		
		for (OutgoingArticle outgoingArticle : outgoingDelivery.getOutgoingArticles())
		{
			// set id to 0 so it is created
			outgoingArticle.setOutgoingArticleId(0);
			
			// get existingArticleId from OutgoingArticle
			int existingId = outgoingArticle.getArticle().getArticleId();
			// get existing Article by existingId
			Article existingArticle = this.getArticleById(existingId);
			// set Article for outgoingArtilce
			outgoingArticle.setArticle(existingArticle);

			// set Delivery for outgoingArticle so they are bidirectional linked (cascade update and owning side/null constraint) 
			outgoingArticle.setOutgoingDelivery(outgoingDelivery);
		}
				
		sessionFactory.getCurrentSession().saveOrUpdate(outgoingDelivery);

		// call flush to execute pending SQL Statements and synchronize Context to DB
		// !!! IMPORTANT !!!
		sessionFactory.getCurrentSession().flush();
		
		// check if the PUs of the outgoing articles and the available articles are correct
		// do the check by checking the consistency of the availArticleInDepot VIEW after persisting the outgoingDelivery
		
		// check incoming and outgoing articles for validity
		boolean validity = this.checkInAndOutArticlePUs();
		if (validity == false)
			throw new HibernateException("Number of PUs for incoming and outgoing articles are not valid.");
		
		return outgoingDelivery.getOutgoingDeliveryId();
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public boolean deleteOutgoingDeliveryById(int id) throws Exception {
		
		OutgoingDelivery outgoingDelivery = this.getOutgoingDeliveryById(id);
		
		sessionFactory.getCurrentSession().delete(outgoingDelivery);
		
		// call flush to execute pending SQL Statements and synchronize Context to DB 
		sessionFactory.getCurrentSession().flush();
		
		// check incoming and outgoing articles for validity
		if (this.checkInAndOutArticlePUs()==false)
			throw new HibernateException("Number of PUs for incoming and outgoing articles are not valid.");
		
		return true;
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public OutgoingDelivery getOutgoingDeliveryById(int id) throws HibernateException {
		
		OutgoingDelivery outgoingDelivery = (OutgoingDelivery)sessionFactory.getCurrentSession().createQuery("From OutgoingDelivery o Where o.outgoingDeliveryId = :id")
				.setParameter("id", id).uniqueResult();
		
		return outgoingDelivery;
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public List<OutgoingDelivery> getAllOutgoingDeliveries() throws HibernateException {
		@SuppressWarnings("unchecked")
		List<OutgoingDelivery> outgoingDeliveries = sessionFactory.getCurrentSession().createQuery("From OutgoingDelivery o order by o.date DESC").list();
		return outgoingDeliveries;
	}

	/***** [END] outgoing deliveries *****/
	
	
	/***** [START] DeliveryLists *****/
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public DeliveryList getDeliveryListById(int id) throws HibernateException {
		
		DeliveryList deliveryList = (DeliveryList)sessionFactory.getCurrentSession().createQuery("From DeliveryList dl Where dl.deliveryListId = :id")
				.setParameter("id", id).uniqueResult();
		return deliveryList;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public List<DeliveryList> getAllDeliveryLists() throws HibernateException {
		
		List<DeliveryList> deliveryLists = sessionFactory.getCurrentSession().createQuery("From DeliveryList").list();
		return deliveryLists;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public int setDeliveryList(DeliveryList deliveryList) throws HibernateException {
		
		sessionFactory.getCurrentSession().saveOrUpdate(deliveryList);
		
		return deliveryList.getDeliveryListId();
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public boolean deleteDeliveryListById(int id) throws HibernateException {
		
		DeliveryList deliveryList = this.getDeliveryListById(id);
		
		sessionFactory.getCurrentSession().delete(deliveryList);
		
		return true;
	}
	
	@Override
	public int setDeliveryLists(List<DeliveryList> deliveryLists) throws HibernateException {
		
		return 0;
	}
	
	/***** [END] DeliveryLists *****/
	
	
	
	
	
	
	

	@Override
	public int setIncomingDeliveries(List<IncomingDelivery> incomingDeliveries) throws HibernateException {
		
		return 0;
	}
	
	/***** [START] IncomingArticles *****/

	@Override
	public IncomingArticle getIncomingArticleById(int id) throws HibernateException {
		
		return null;
	}

	@Override
	public List<IncomingArticle> getAllIncomingArticles() throws HibernateException {
		
		return null;
	}

	@Override
	public int setIncomingArticle(IncomingArticle incomingArticle) throws HibernateException {
		
		return 0;
	}

	@Override
	public int setIncomingArticles(List<IncomingArticle> incomingArticles) throws HibernateException {
		
		return 0;
	}
	
	/***** [END] IncomingArticles *****/


	/***** [START] Articles *****/

	@Override
	public List<Article> getAllArticles() throws HibernateException {
		
		return null;
	}

	@Override
	public int setArticle(Article article) throws HibernateException {
		
		return 0;
	}

	@Override
	public int setArticles(List<Article> articles) throws HibernateException {
		
		return 0;
	}
	
	/***** [END] Articles *****/
	
	
	
	/***** [START] OutgoingArticles *****/

	@Override
	public OutgoingArticle getOutgoingArticleById(int id) throws HibernateException {
		
		return null;
	}

	@Override
	public List<OutgoingArticle> getAllOutgoingArticles() throws HibernateException {
		
		return null;
	}

	@Override
	public int setOutgoingArticle(OutgoingArticle outgoingArticle) throws HibernateException {
		
		return 0;
	}

	@Override
	public int setOutgoingArticles(List<OutgoingArticle> outgoingArticles) throws HibernateException {
		
		return 0;
	}

	/***** [END] OutgoingArticles *****/
	
	
	
	
	
	
	
	@Override
	public int setOutgoingDeliveries(List<OutgoingDelivery> outgoingDeliveries) throws HibernateException {
		
		return 0;
	}

	

	/***** [START] Categories *****/

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public Category getCategoryById(int id) throws HibernateException {

		Category mCategory = (Category)sessionFactory.getCurrentSession().createQuery("FROM Category c WHERE c.categoryId = :id").setParameter("id", id).uniqueResult();
		return mCategory;
	}


	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public Category getCategoryByCategory(String category) throws HibernateException {

		Category mCategory = (Category)sessionFactory.getCurrentSession().createQuery("FROM Category c WHERE c.category = :category").setParameter("category", category).uniqueResult();
		
		/* if (mCategory == null)
		{
			mCategory = new Category(0, category, "");
			this.setCategory(mCategory);
		} */
		
		return mCategory;
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public Category setCategory(Category category) throws HibernateException
	{
		sessionFactory.getCurrentSession().saveOrUpdate(category);
		return category;
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public List<Category> getAllCategories() throws HibernateException
	{
		@SuppressWarnings("unchecked")
		List<Category> categories = sessionFactory.getCurrentSession().createQuery("FROM Category c ORDER BY c.category").list();
		return categories;
	}


	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public boolean deleteCategoryById(int id) throws HibernateException {
		
		Category category = this.getCategoryById(id);
		
		if (category == null)
			return false;
		
		sessionFactory.getCurrentSession().delete(category);
		
		return true;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public List<Organisation> getOrganisationsByCategoryId(int id) throws HibernateException {
		
		
		// important: add 'SELECT DISTINC o' to get only Objects and use JOIN to restrict the categoryIds
		@SuppressWarnings("unchecked")
		List<Organisation> organisations = (List<Organisation>) sessionFactory.getCurrentSession().
					createQuery("SELECT DISTINCT o FROM Organisation o JOIN o.categories c WHERE o.active=1 AND c.categoryId = :id ORDER BY o.name").setParameter("id", id).list();
		
//		System.out.println("size: "+organisations.size());
//		System.out.println(organisations.get(0).getName());
//		System.out.println(organisations.get(0).getComment());
		
		return organisations;
	}
	
	/***** [END] Categories *****/
	
}
