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


public class DataBaseService implements IDataBase {

	private SessionFactory sessionFactory;
		
	public DataBaseService(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

		
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public Person getPersonById(int id) {				
		Query query = sessionFactory.getCurrentSession().createQuery("FROM Person p left join fetch p.person WHERE p.personId = :id");
		query.setParameter("id", id);
		Person person = (Person)query.uniqueResult();
		return person;
	}
	
	@Override
	public int setIncomingDelivery(IncomingDelivery incomingDelivery) {
		
		return 0;
	}


	

	@Override
	public List<Person> getPersonsByType(Type type) {
		
		return null;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public List<Person> getAllPersons() {
		@SuppressWarnings("unchecked")
		List<Person> persons = sessionFactory.getCurrentSession().createQuery("FROM Person p left join fetch p.person ORDER BY p.lastName").list();
		return persons;
	}

	@Override
	public Person getPersonByLoginEmail(String loginEmail) {
		
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
	public Type getTypeById(int id) {
		Query query = sessionFactory.getCurrentSession().createQuery("FROM Type t WHERE t.typeId = :id");
		query.setParameter("id", id);
		Type type = (Type)query.uniqueResult();
		return type;
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public Type getTypeByType(String type)
	{
		Query query = sessionFactory.getCurrentSession().createQuery("FROM Type t WHERE t.name = :type");
		query.setParameter("type", type);
		Type mType = (Type)query.uniqueResult();
		return mType;
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public List<Type> getAllTypes() {
		Query query = sessionFactory.getCurrentSession().createQuery("FROM Type t ORDER BY t.name");
		List<Type> types = (List<Type>)query.list();
		return types;
	}
	
	
	@Transactional(propagation=Propagation.REQUIRED)
	public Telephone setTelephone(Telephone telephone)
	{
		sessionFactory.getCurrentSession().saveOrUpdate(telephone);
		return telephone;
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	public Telephone getTelephoneByTelephone(String telephone, String type)
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
	public Email setEmail(Email email)
	{
		sessionFactory.getCurrentSession().saveOrUpdate(email);
		return email;
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	public Email getEmailByEmail(String email, String type)
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
	public Platformuser getPlatformuserById(int id)
	{
		Platformuser platformuser = (Platformuser) sessionFactory.getCurrentSession().createQuery("FROM Platformuser p WHERE p.personId = :id")
				.setParameter("id", id).uniqueResult();
		return platformuser;
	}
	
	@Override
	@Transactional
	public Platformuser getPlatformuserbyLoginEmail(String loginEmail)
	{
		Platformuser platformuser = (Platformuser) sessionFactory.getCurrentSession().createQuery("FROM Platformuser p WHERE p.loginEmail = :loginEmail")
				.setParameter("loginEmail", loginEmail).uniqueResult();
		return platformuser;
	}
	
	@Override
	@Transactional
	public Platformuser setPlatformuser(Platformuser platformuser)
	{
		sessionFactory.getCurrentSession().saveOrUpdate(platformuser);
		return platformuser;
	}
	
	@Override
	@Transactional
	public void removePlatformuserById(int id)
	{
		Platformuser platformuser = this.getPlatformuserById(id);
		if (platformuser != null)
			sessionFactory.getCurrentSession().delete(platformuser);
	}
	
	@Override
	@Transactional
	public Permission getPermissionById(int id)
	{
		Permission permission = (Permission) sessionFactory.getCurrentSession().createQuery("From Permission p WHERE p.permissionId = :id")
				.setParameter("id", id).uniqueResult();
		return permission;
	}
	
	@Override
	@Transactional
	public Permission getPermissionByPermission(String permission)
	{
		Permission mPermission = (Permission) sessionFactory.getCurrentSession().createQuery("From Permission p WHERE p.permission = :permission")
				.setParameter("permission", permission).uniqueResult();
		return mPermission;
	}
	
	
	@Override
	@Transactional
	public int telephoneTest() {
	
		
		Person mPerson = getPersonById(36);
		for (Type type : mPerson.getTypes())
		{
			System.out.println(type.getName());
		}
		
		System.out.println("Seas");
		
		
		
		
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
	public int setPersons(List<Person> persons) {
		
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
		
		return null;
	}

	@Override
	public List<Organisation> getAllOrganisations() {
		
		return null;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public int setOrganisation(Organisation organisation) {


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
		for (Person p : organisation.getPersons())
		{
			contactPersons.add(this.getPersonById(p.getPersonId()));
		}
		organisation.setPersons(contactPersons);


		// final update all in DB
		sessionFactory.getCurrentSession().saveOrUpdate(organisation);

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
	@Transactional(propagation=Propagation.REQUIRED)
	public Category getCategoryById(int id) {

		Category mCategory = (Category)sessionFactory.getCurrentSession().createQuery("FROM Category c WHERE c.categoryId = :id").setParameter("id", id).uniqueResult();
		return mCategory;
	}


	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public Category getCategoryByCategory(String category) {

		Category mCategory = (Category)sessionFactory.getCurrentSession().createQuery("FROM Category c WHERE c.category = :category").setParameter("category", category).uniqueResult();
		
		if (mCategory == null)
		{
			mCategory = new Category(0, category, "");
			this.setCategory(mCategory);
		}
		
		return mCategory;
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public Category setCategory(Category category)
	{
		sessionFactory.getCurrentSession().saveOrUpdate(category);
		return category;
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public List<Category> getAllCategories()
	{
		List<Category> categories = sessionFactory.getCurrentSession().createQuery("FROM Category c ORDER BY c.category").list();
		return categories;
	}

}
