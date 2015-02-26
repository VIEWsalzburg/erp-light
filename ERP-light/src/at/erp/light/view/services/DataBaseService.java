package at.erp.light.view.services;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.cglib.core.EmitUtils;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import at.erp.light.view.dto.ArticleDTO;
import at.erp.light.view.dto.InOutArticlePUDTO;
import at.erp.light.view.dto.PersonAddressReportDataDTO;
import at.erp.light.view.dto.PersonEmailReportDataDTO;
import at.erp.light.view.dto.ReportDataDTO;
import at.erp.light.view.mapper.ArticleMapper;
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
import at.erp.light.view.model.Logging;
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
	@Transactional(propagation=Propagation.REQUIRED)
	public List<Person> getAllPersons() throws HibernateException {
		@SuppressWarnings("unchecked")
		List<Person> persons = sessionFactory.getCurrentSession().createQuery("FROM Person p left join fetch p.lastEditor ORDER BY p.lastName").list();
		return persons;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public List<Person> getAllActivePersons() throws HibernateException {
		@SuppressWarnings("unchecked")
		List<Person> persons = sessionFactory.getCurrentSession().createQuery("FROM Person p left join fetch p.lastEditor WHERE p.active=1 ORDER BY p.lastName").list();
		return persons;
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
		
		// create new Person
		Person existingPerson = new Person();
		
		// if person exists in DB
		if (person.getPersonId() > 0)
		{
			// 	get current Person
			existingPerson = this.getPersonById(person.getPersonId());
		}
		// else use the new Object
		
		// update general information
		existingPerson.setSalutation(person.getSalutation());
		existingPerson.setTitle(person.getTitle());
		existingPerson.setFirstName(person.getFirstName());
		existingPerson.setLastName(person.getLastName());
		existingPerson.setComment(person.getComment());
		existingPerson.setUpdateTimestamp(person.getUpdateTimestamp());
		existingPerson.setActive(person.getActive());
		existingPerson.setLastEditor(person.getLastEditor());
		
		// update Address
		// if address == null => delete FK
		if (person.getAddress() != null)
		{
			// check if Address is not ""
			if (person.getAddress().getAddress().isEmpty())
			{
				// update existingPerson
				existingPerson.setAddress(null);
			}	
			else
			{
				// checks if Address already exists in database (if yes => retrieve existing one, if no => create new one and get it)
				Address Address = getAddressByAddress(person.getAddress().getAddress());
				// update existingPerson
				existingPerson.setAddress(Address);
			}
		}
		
		// update Country
		if (person.getCountry() != null)
		{
			// check if Country is not ""
			if (person.getCountry().getCountry().isEmpty())
			{
				// update existingPerson
				existingPerson.setCountry(null);
			}	
			else
			{
				// checks if Country already exists in database (if yes => retrieve existing one, if no => create new one and get it)
				Country country = getCountryByCountry(person.getCountry().getCountry());
				// update existingPerson
				existingPerson.setCountry(country);
			}
		}
		
		// update City
		if (person.getCity() != null)
		{
			if (person.getCity().getCity().isEmpty() && person.getCity().getZip().isEmpty())
			{
				// update existingPerson
				existingPerson.setCity(null);
			}
			else
			{
				// checks if City and Zip already exist in database (if yes => retrieve existing one, if no => create new one and get it)
				City city = getCityByCityAndZip(person.getCity().getCity(), person.getCity().getZip());
				// update existingPerson
				existingPerson.setCity(city);
			}
		}
		
		// update Telephones
		// 1. remove all telephones from Person
		// 2. add all telephones to Set
		existingPerson.getTelephones().clear();
		
		for (Telephone telephone : person.getTelephones())
		{
			// create new Telephone object and add it to existingPerson
			existingPerson.getTelephones().add(new Telephone(0, this.getTypeByType(telephone.getType().getName()), telephone.getTelephone()));
		}
		// types of telephones should be set in Controller
		
		// update Emails
		// 1. remove all emails from Person
		existingPerson.getEmails().clear();
		for (Email email : person.getEmails())
		{
			// create new email object and add it to existingPerson
			existingPerson.getEmails().add(new Email(0, this.getTypeByType(email.getType().getName()), email.getEmail()));
		}
		// types of emails should be set in Controller
		
		existingPerson.getTypes().clear();
		Set<Type> types = new HashSet<Type>();
		for (Type type : person.getTypes())
		{
			types.add(this.getTypeByType(type.getName()));
		}
		existingPerson.setTypes(types);
		
		// Set Types should be set in Controller because Types are "static"
		
		// final update all in DB
		sessionFactory.getCurrentSession().saveOrUpdate(existingPerson);
		
		return existingPerson.getPersonId();
		
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
	@Transactional(propagation=Propagation.REQUIRED)
	public Organisation getOrganisationById(int id) throws HibernateException {
		Query query = sessionFactory.getCurrentSession().createQuery("FROM Organisation o WHERE o.organisationId = :id");
		query.setParameter("id", id);
		Organisation organisation = (Organisation)query.uniqueResult();
		return organisation;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public List<Organisation> getAllOrganisations() throws HibernateException {
		
		@SuppressWarnings("unchecked")
		List<Organisation> organisations = sessionFactory.getCurrentSession().createQuery("FROM Organisation o ORDER BY o.name").list();
		return organisations;
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public List<Organisation> getAllActiveOrganisations() throws HibernateException {
		
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
	
	// TODO: this function needs accurate testing
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public int setNewIncomingDelivery(IncomingDelivery incomingDelivery) throws Exception {
		
		// set the incoming delivery for each incoming article to be sure they are updated
		for (IncomingArticle incomingArticle : incomingDelivery.getIncomingArticles())
		{
			incomingArticle.setIncomingDelivery(incomingDelivery);
		}
		
		// check incomingDeliveryId: if 0 => create all incomingArticles and Articles new by setting their Ids to 0
		// create completely new incomingDelivery
		if (incomingDelivery.getIncomingDeliveryId()==0)
		{
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
		else
		{
			// if Id != 0 => throw Exception
			throw new Exception("Id of new IncomingDelivery is not 0");
		}
		
		
	}
	
	// TODO: this function needs accurate testing
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public int updateIncomingDelivery(IncomingDelivery incomingDelivery) throws Exception {
		
		// update existing incomingDelivery if Id != 0
		
		// set the incoming delivery for each incoming article to be sure they are updated
		for (IncomingArticle incomingArticle : incomingDelivery.getIncomingArticles())
		{
			incomingArticle.setIncomingDelivery(incomingDelivery);
		}
		
		// check incomingDeliveryId: if not 0 => get existing incomingDelivery from DB and update fields
		// update the incomingArticles according to the updated IncomingArticles
		// and update the existing Articles according to the updated Articles
		if (incomingDelivery.getIncomingDeliveryId() <= 0)
		{
			throw new Exception("Id of IncomingDelivery is "+incomingDelivery.getIncomingDeliveryId()+", entity does not exist.");
		}
		
		// get existingEntity from DB, which should be updated
		IncomingDelivery existingEntity = this.getIncomingDeliveryById(incomingDelivery.getIncomingDeliveryId());
		
		// first option: update infos of booked delivery
		if (existingEntity.getBooked() == 1)
		{
			// first update all Article infos
			// check if number of IncomingArticles is the same for existing and updated
			if (existingEntity.getIncomingArticles().size() != incomingDelivery.getIncomingArticles().size())
			{
				throw new Exception("Numbers of Incoming Articles of existing and updated IncomingDelivery are not the same!");
			}
			
			// update infos for all IncomingArticles (ArticleNr - order) including Articles (pricePU), except for packagingUnits
			for (IncomingArticle existingIA : existingEntity.getIncomingArticles())
			{
				// find the correct Article in the updatedDelivery
				for (IncomingArticle updatedIA : incomingDelivery.getIncomingArticles())
				{
					// found correct IncomingArticle
					if (existingIA.getIncomingArticleId() == updatedIA.getIncomingArticleId())
					{
						// update Infos for corresponding Article
						existingIA.getArticle().setPricepu(updatedIA.getArticle().getPricepu());	// update Price
						existingIA.getArticle().setMdd(updatedIA.getArticle().getMdd());			// update Mhd
						this.sessionFactory.getCurrentSession().update(existingIA.getArticle());	// persist change
						
						// update infos for corresponding IncomingArticle
						existingIA.setArticleNr(updatedIA.getArticleNr());				// only update order of IncomingArticles in IncomingDelivery
						this.sessionFactory.getCurrentSession().update(existingIA);		// persist change
					}	
				}
			}
			
			// update infos for IncomingDelivery - only update Comment and Date
			existingEntity.setComment(incomingDelivery.getComment());
			existingEntity.setDate(incomingDelivery.getDate());
			existingEntity.setLastEditor(incomingDelivery.getLastEditor());
			existingEntity.setUpdateTimestamp(incomingDelivery.getUpdateTimestamp());
			this.sessionFactory.getCurrentSession().update(existingEntity);		// persist change
			
			// call flush to execute pending SQL Statements and synchronize Context to DB 
			sessionFactory.getCurrentSession().flush();
			
			// check incoming and outgoing articles for validity
			if (this.checkInAndOutArticlePUs()==false)
				throw new ERPLightException("Number of PUs for incoming and outgoing articles are not valid.");
			
			return existingEntity.getIncomingDeliveryId();
			
		}	// end of update booked Delivery
		
		// update existing Delivery which is not booked
		else if (existingEntity.getBooked() == 0)		
		{
			// remove all old incomingArticles and set the updatedIncomingArticles
			// old incomingArticles are automatically deleted by removing them from the incomingDelivery
			
			// delete all IncomingArticles from the DB
			for (IncomingArticle existingIA : existingEntity.getIncomingArticles())
			{
				this.sessionFactory.getCurrentSession().delete(existingIA);
			}
			existingEntity.getIncomingArticles().clear();	// remove IncomingArticles in the memory
			
			Set<IncomingArticle> updatedIncomingArticles = incomingDelivery.getIncomingArticles();	// get incomingArticles from the updated Delivery
			existingEntity.setIncomingArticles(updatedIncomingArticles);	// set new updatedList
			
			// link IncomingArticles and existingEntity together
			// and set Ids for incomingArticles and Articles so they are created new in the DB
			for (IncomingArticle ia : updatedIncomingArticles)
			{
				// persist all Articles new by setting Id to 0
				ia.setIncomingDelivery(existingEntity);
				ia.setIncomingArticleId(0);
				ia.getArticle().setArticleId(0);				// set id to 0
				this.sessionFactory.getCurrentSession().saveOrUpdate(ia.getArticle());		// persist new Article to DB
			}
			
			// update infos for incomingDelivery
			existingEntity.setComment(incomingDelivery.getComment());
			existingEntity.setDate(incomingDelivery.getDate());
			existingEntity.setOrganisation(incomingDelivery.getOrganisation());
			existingEntity.setLastEditor(incomingDelivery.getLastEditor());
			existingEntity.setUpdateTimestamp(incomingDelivery.getUpdateTimestamp());
			// incomingDeliveryId stays the same
			this.sessionFactory.getCurrentSession().update(existingEntity);
			
			// call flush to execute pending SQL Statements and synchronize Context to DB 
			sessionFactory.getCurrentSession().flush();
			
			// check incoming and outgoing articles for validity
			if (this.checkInAndOutArticlePUs()==false)
				throw new ERPLightException("Number of PUs for incoming and outgoing articles are not valid.");
			
			return existingEntity.getIncomingDeliveryId();
			
		}
		
		return -1;	// should not happen
		
	}
	
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public int deleteArticleWithDistributionByArticleId(int articleId) throws Exception {
		
		// get all incomingArticles and all outgoingArticles which are linked to the Article with the given ArticleId
		
		List<IncomingArticle> incomingArticles = this.getIncomingArticlesByArticleId(articleId);
		List<OutgoingArticle> outgoingArticles = this.getOutgoingArticlesByArticleId(articleId);
		
		// perform for every IncomingArticle
		for (IncomingArticle iA : incomingArticles)
		{
			// get IncomingDelivery for IncomingArticle
			IncomingDelivery incomingDelivery = iA.getIncomingDelivery();
			// remove IncomingArticle from IncomingDelivery
			incomingDelivery.getIncomingArticles().remove(iA);
			// update modified IncomingDelivery
			this.sessionFactory.getCurrentSession().update(incomingDelivery);
			// delete IncomingArticle
			this.sessionFactory.getCurrentSession().delete(iA);
		}
		
		// perform for every OutgoingArticle
		for (OutgoingArticle oA : outgoingArticles)
		{
			// get OutgoingDelivery for OutgoingArticle
			OutgoingDelivery outgoingDelivery = oA.getOutgoingDelivery();
			// remove OutgoingArticle from OutgoingDelivery
			outgoingDelivery.getOutgoingArticles().remove(oA);
			// update modified OutgoingDelivery
			this.sessionFactory.getCurrentSession().update(outgoingDelivery);
			// delete OutgoingArticle
			this.sessionFactory.getCurrentSession().delete(oA);
		}
		
		// update DB changes
		this.sessionFactory.getCurrentSession().flush();
		
		// delete Article from DB
		this.sessionFactory.getCurrentSession().delete(this.getArticleById(articleId));
		
		// perform consistency check
		if (checkInAndOutArticlePUs() == false)
			throw new ERPLightException("Number of PUs for incoming and outgoing articles are not valid.");
		
		return 0;
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
		
		// check if incomingDelivery is booked => throw exception and rollback
		if (incomingDelivery.getBooked()!=0)
		{
			throw new ERPLightException("Lieferung ist verbucht!");
		}
		
		sessionFactory.getCurrentSession().delete(incomingDelivery);
		
		// call flush to execute pending SQL Statements and synchronize Context to DB 
		sessionFactory.getCurrentSession().flush();
		
		// check incoming and outgoing articles for validity
		if (this.checkInAndOutArticlePUs()==false)
			throw new Exception("Number of PUs for incoming and outgoing articles are not valid.");
		
		return true;
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public boolean archiveIncomingDeliveryById(int id, int status) throws Exception {
		IncomingDelivery incomingDelivery = this.getIncomingDeliveryById(id);
		incomingDelivery.setArchived(status);
		return true;
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public List<IncomingDelivery> getAllIncomingDeliveries() throws HibernateException {
		@SuppressWarnings("unchecked")
		List<IncomingDelivery> incomingDeliveries = sessionFactory.getCurrentSession().createQuery("From IncomingDelivery i order by i.date DESC").list();
		return incomingDeliveries;
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public List<IncomingDelivery> getAllIncomingDeliveries(int archivedStatus) throws HibernateException {
		@SuppressWarnings("unchecked")
		List<IncomingDelivery> incomingDeliveries = sessionFactory.getCurrentSession()
			.createQuery("From IncomingDelivery i Where i.archived = :archivedStatus order by i.date DESC")
			.setParameter("archivedStatus", archivedStatus)
			.list();
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
		
		// check if outgoingDelivery is booked => throw exception and rollback
		if (outgoingDelivery.getBooked()!=0)
		{
			throw new ERPLightException("Lieferung ist verbucht!");
		}
		
		sessionFactory.getCurrentSession().delete(outgoingDelivery);
		
		// call flush to execute pending SQL Statements and synchronize Context to DB 
		sessionFactory.getCurrentSession().flush();
		
		// check incoming and outgoing articles for validity
		if (this.checkInAndOutArticlePUs()==false)
			throw new HibernateException("Number of PUs for incoming and outgoing articles are not valid.");
		
		return true;
	}
	
	
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public int updateOutgoingDelivery(OutgoingDelivery outgoingDelivery) throws Exception
	{
		// update outgoingDelivery if id > 0
		
		// link outgoingArticles and OutgoingDelivery to be sure they are updated
		for (OutgoingArticle outgoingArticle : outgoingDelivery.getOutgoingArticles())
		{
			outgoingArticle.setOutgoingDelivery(outgoingDelivery);
		}
		
		// check outgoingDeliveryId: if not 0 => get existing outgoingDelivery from DB and update fields
		// update the outgoingArticles according to the updated outogingArticles
		if (outgoingDelivery.getOutgoingDeliveryId() == 0)
		{
			throw new Exception("Update of OutgoingDelivery with Id 0 not possible.");
		}
		
		// get existing entity from DB, which should be updated
		OutgoingDelivery existingEntity = this.getOutgoingDeliveryById(outgoingDelivery.getOutgoingDeliveryId());
		
		// if an outgoingDelivery is already booked: nevertheless update articles and outgoingArticles
		// => updatedOutgoingDelivery should appear in new DeliveryList
		
		// update OutgoingDelivery Infos from updatedOutgoingDelivery
		existingEntity.setComment(outgoingDelivery.getComment());
		existingEntity.setDate(outgoingDelivery.getDate());
		existingEntity.setOrganisation(outgoingDelivery.getOrganisation());
		existingEntity.setLastEditor(outgoingDelivery.getLastEditor());
		existingEntity.setUpdateTimestamp(outgoingDelivery.getUpdateTimestamp());
		

		// remove all old outgoingArticles and assign new ones
		
		// delete all current OutgoingArticles from the DB
		for (OutgoingArticle existingOA : existingEntity.getOutgoingArticles())
		{
			this.sessionFactory.getCurrentSession().delete(existingOA);
		}
		existingEntity.getOutgoingArticles().clear();	// clear assigned articles in list
		
		this.sessionFactory.getCurrentSession().flush();
		
		Set<OutgoingArticle> updatedOutgoingArticles = outgoingDelivery.getOutgoingArticles();
		existingEntity.setOutgoingArticles(updatedOutgoingArticles);	// already creates new outgoingArticles in DB
		
		// link outgoingDelivery and outgoingArticles bi-directional
		// check if articles exist in DB and assign them
		for (OutgoingArticle oA : existingEntity.getOutgoingArticles())
		{
			// set id to 0 so it is created
			// oA.setOutgoingArticleId(0);	// entity is being persisted by assigning the new Set to the existingEntity
			// exception "identifier of an instance of at.erp.light.view.modal.OutgoingArticle was altered form 118 to 0"
			
			// assign article from DB
			int existingArticleId = oA.getArticle().getArticleId();
			oA.setArticle(this.getArticleById(existingArticleId));
			
			// link to existingEntity, to ensure they are updated
			oA.setOutgoingDelivery(existingEntity);
		}
		
		// save updated Entity in DB
		sessionFactory.getCurrentSession().saveOrUpdate(existingEntity);
		
		// call flush to execute pending SQL Statements and synchronize Context to DB 
		sessionFactory.getCurrentSession().flush();
				
		// check incoming and outgoing articles for validity
		if (this.checkInAndOutArticlePUs()==false)
			throw new ERPLightException("Anzahl der ausgehenden und eingehenden Artikel stimmt nicht überein.");
		
		return existingEntity.getOutgoingDeliveryId();
	}
	
	
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public boolean archiveOutgoingDeliveryById(int id, int status) throws Exception {
		OutgoingDelivery outgoingDelivery = this.getOutgoingDeliveryById(id);
		outgoingDelivery.setArchived(status);
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
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public List<OutgoingDelivery> getAllOutgoingDeliveries(int archivedStatus) throws HibernateException {
		@SuppressWarnings("unchecked")
		List<OutgoingDelivery> outgoingDeliveries = sessionFactory.getCurrentSession()
			.createQuery("From OutgoingDelivery o Where o.archived = :archivedStatus order by o.date DESC")
			.setParameter("archivedStatus", archivedStatus)
			.list();
		return outgoingDeliveries;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public List<OutgoingDelivery> getAvailableOutgoingDeliveries()
			throws HibernateException {
		@SuppressWarnings("unchecked")
		List<OutgoingDelivery> outgoingDeliveries = sessionFactory.getCurrentSession().createQuery("From OutgoingDelivery o where o.booked = 0 order by o.date DESC").list();
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
		
		@SuppressWarnings("unchecked")
		List<DeliveryList> deliveryLists = sessionFactory.getCurrentSession().createQuery("From DeliveryList dl order by dl.date DESC").list();
		return deliveryLists;
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public List<DeliveryList> getAllDeliveryLists(int archivedStatus) throws HibernateException {
		
		@SuppressWarnings("unchecked")
		List<DeliveryList> deliveryLists = sessionFactory.getCurrentSession()
				.createQuery("From DeliveryList dl Where dl.archived = :archivedStatus order by dl.date DESC")
				.setParameter("archivedStatus", archivedStatus)
				.list();
		return deliveryLists;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public int setDeliveryList(DeliveryList deliveryList) throws HibernateException {
		
		// update deliveryNr in the corresponding outgoingDeliveries
		for (OutgoingDelivery outgoingDelivery : deliveryList.getOutgoingDeliveries())
		{
			// get DB entity and update deliveryNr for this entity
			this.getOutgoingDeliveryById(outgoingDelivery.getOutgoingDeliveryId()).setDeliveryNr(outgoingDelivery.getDeliveryNr());
		}
		
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
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public boolean archiveDeliveryListById(int id, int status)
			throws HibernateException {
		DeliveryList deliveryList = this.getDeliveryListById(id);
		deliveryList.setArchived(status);
		return true;
	}
	
	/***** [END] DeliveryLists *****/
	
	
	/***** [START] Incoming and Outgoing Articles *****/
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public IncomingArticle getIncomingArticleById(int id) throws HibernateException
	{
		IncomingArticle incomingArticle = (IncomingArticle)this.sessionFactory.getCurrentSession()
				.createQuery("From IncomingArticle ia Where ia.incomingArticleId = :id")
				.setParameter("id", id).uniqueResult();
		return incomingArticle;
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public List<IncomingArticle> getIncomingArticlesByArticleId(int id) throws HibernateException
	{
		@SuppressWarnings("unchecked")
		List<IncomingArticle> list = (List<IncomingArticle>)this.sessionFactory.getCurrentSession()
				.createQuery("Select ia From IncomingArticle ia Join ia.article a Where a.articleId = :id")
				.setParameter("id", id).list();
		return list;
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public OutgoingArticle getOutgoingArticleById(int id) throws HibernateException
	{
		OutgoingArticle outgoingArticle = (OutgoingArticle)this.sessionFactory.getCurrentSession()
				.createQuery("From OutgoingArticle oa Where oa.outgoingArticleId = :id")
				.setParameter("id", id).uniqueResult();
		return outgoingArticle;
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public List<OutgoingArticle> getOutgoingArticlesByArticleId(int id) throws HibernateException
	{
		@SuppressWarnings("unchecked")
		List<OutgoingArticle> list = (List<OutgoingArticle>)this.sessionFactory.getCurrentSession()
				.createQuery("Select oa From OutgoingArticle oa Join oa.article a Where a.articleId = :id")
				.setParameter("id", id).list();
		return list;
	}
	
	
	/***** [END] Incoming and Outgoing Articles *****/
	
	
	
	/***** [START] "Buchhalterfunktion" *****/
	
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public List<InOutArticlePUDTO> getArticleDistributionByArticleId(int articleId) throws Exception
	{
		// get articleInfo from DB
		Article article = this.getArticleById(articleId);
		if (article == null)
		{
			throw new ERPLightException("An article with the given id "+articleId+" does not exist in the DB");
		}
		
		// create articleDTO for all InOutArticlePUDTOs
		ArticleDTO articleDTO = ArticleMapper.mapToDTO(article);
		
		// list for all InOutArticlePUDTOs
		List<InOutArticlePUDTO> distributionList = new ArrayList<InOutArticlePUDTO>();  
		
		
		/***** IncomingArticles *****/
		List<IncomingArticle> incomingArticles = this.getIncomingArticlesByArticleId(articleId);
		int numberPUsIncoming = 0;
		if (incomingArticles.size()==0)
		{
			throw new ERPLightException("no IncomingArticle exists for articleId "+articleId);
		}
		// add objects to distributionList
		for (IncomingArticle ia : incomingArticles)
		{
			distributionList.add(new InOutArticlePUDTO(ia.getIncomingDelivery().getOrganisation().getOrganisationId(),
					ia.getIncomingArticleId(), ia.getNumberpu(), articleDTO, 0));
			// get organisationId via incomingDelivery
			// tpye 0 for IncomingArticle
			
			// calc total incomingPUs
			numberPUsIncoming += ia.getNumberpu();
		}
		
		
		/***** OutgoingArticles *****/
		List<OutgoingArticle> outgoingArticles = this.getOutgoingArticlesByArticleId(articleId);
		int numberPUsOutgoing = 0;
		if (outgoingArticles.size()==0)
		{
			// an keine Organisation verteilt
		}
		// add objects to distributionList
		for (OutgoingArticle oa : outgoingArticles)
		{
			distributionList.add(new InOutArticlePUDTO(oa.getOutgoingDelivery().getOrganisation().getOrganisationId(),
					oa.getOutgoingArticleId(), oa.getNumberpu(), articleDTO, 1));
			// get organisationId via IncomingDelivery
			// tpye 1 for OutgoingArticle
			
			// calc total outgoingPUs
			numberPUsOutgoing += oa.getNumberpu();
		}
		
		
		
		/***** Articles in depot *****/
		List<AvailArticleInDepot> availArticleInDepots = this.getAvailableArticlesInDepot();
		// find AvailArticleInDepot for current ArticleId
		AvailArticleInDepot availArticleInDepot = null;
		int numberPUsDepot = 0;
		for (AvailArticleInDepot depotArticle : availArticleInDepots)
		{
			if (depotArticle.getArticleId() == articleId)
			{
				availArticleInDepot = depotArticle;
			}
		}
		// if not found => create InOutArticlePUDTO for depot with number PU 0
		if (availArticleInDepot == null)
		{
			distributionList.add(new InOutArticlePUDTO(-1, -1, 0, articleDTO, 2));
			// organisationId ... -1 for depot
			// InOutArticleId ... -1 for depot
			// numberPU ... 0 (nothing in the depot)
			// type ... 2 for depot
			
			numberPUsDepot = 0;
		}
		else
		{
			distributionList.add(new InOutArticlePUDTO(-1, -1, availArticleInDepot.getAvailNumberOfPUs(), articleDTO, 2));
			// organisationId ... -1 for depot
			// InOutArticleId ... -1 for depot
			// numberPU ... availableNumberPUs in depot
			// type ... 2 for depot
			
			numberPUsDepot = availArticleInDepot.getAvailNumberOfPUs();
		}
		
		
		// check number of PUs for consistency
		
		if ( (numberPUsOutgoing+numberPUsDepot) != numberPUsIncoming)
		{
			throw new ERPLightException("number of IncomingArticles compared to Outgoing and DepotArticle does not match");
		}
		
		
		return distributionList;
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public int updateArticleDistribution(List<InOutArticlePUDTO> list) throws Exception
	{
		// check numberPUs is the same for incoming and outgoing articles
		int numberPUsIncoming = 0;
		int numberPUsOutgoing = 0;
		
		for (InOutArticlePUDTO elem : list)
		{
			switch(elem.getType()){
			// incoming
			case 0:	
				if (elem.getNumberPUs()<=0)
					throw new ERPLightException("numberPU must not be smaller or euqal 0");
				numberPUsIncoming += elem.getNumberPUs();
				break;
			// outgoing
			case 1:
				if (elem.getNumberPUs()<=0)
					throw new ERPLightException("numberPU must not be smaller or euqal 0");
				numberPUsOutgoing += elem.getNumberPUs();
				break;
			// depot
			case 2:
				numberPUsOutgoing += elem.getNumberPUs();
				break;
			default:
				throw new ERPLightException("Unknown type for distribution element");
			}
		}
		
		if (numberPUsIncoming != numberPUsOutgoing)
		{
			throw new ERPLightException("Anzahl der eingehenden und ausgehenden Waren stimmt nicht überein!");
		}
		
		
		// get every Incoming/OutgoingArticle and update the numberPUs
		int articleId = 0;	// use for determining the assigned articleId for checking the depot numberPUs
		int announcedNumberPUDepot = 0;
		
		for (InOutArticlePUDTO elem : list)
		{
			switch(elem.getType())
			{
			// incoming
			case 0:	
				IncomingArticle incomingArticle = this.getIncomingArticleById(elem.getInOutArticleId());
				if (incomingArticle==null)
					throw new ERPLightException("did not find IncomingArticle with Id "+elem.getInOutArticleId());
				incomingArticle.setNumberpu(elem.getNumberPUs());
				this.sessionFactory.getCurrentSession().update(incomingArticle);
				articleId = incomingArticle.getArticle().getArticleId();
				break;
			// outgoing
			case 1:
				OutgoingArticle outgoingArticle = this.getOutgoingArticleById(elem.getInOutArticleId());
				if (outgoingArticle==null)
					throw new ERPLightException("did not find OutgoingArticle with Id "+elem.getInOutArticleId());
				outgoingArticle.setNumberpu(elem.getNumberPUs());
				this.sessionFactory.getCurrentSession().update(outgoingArticle);
				break;
			// depot
			case 2:
				// don't save in database
				announcedNumberPUDepot = elem.getNumberPUs();
				break;
			default:
				throw new ERPLightException("Unknown type for distribution element");
			}
			
		}
		
		// flush to update changes
		this.sessionFactory.getCurrentSession().flush();
		
		// check resulting depot numberPUs with determined numberPUs
		
		// find number
		List<AvailArticleInDepot> depotList = this.getAvailableArticlesInDepot();
		AvailArticleInDepot availArticleInDepot = null;
		for (AvailArticleInDepot a : depotList)
		{
			if (a.getArticleId() == articleId)
				availArticleInDepot = a;
		}
		
		// if not found => 0 PUs in depot
		if (availArticleInDepot == null)
		{
			if (announcedNumberPUDepot != 0)
				throw new ERPLightException("announced numberPUsDepot does not match the real numberPUs in depot "+announcedNumberPUDepot+" - 0");
		}
		else
		{
			if (announcedNumberPUDepot != availArticleInDepot.getAvailNumberOfPUs())
				throw new ERPLightException("announced numberPUsDepot does not match the real numberPUs in depot "+announcedNumberPUDepot+" - "+availArticleInDepot.getAvailNumberOfPUs());
		}
		
		if(this.checkInAndOutArticlePUs()==false)
		{
			throw new ERPLightException("Number of PUs for incoming and outgoing articles are not valid.");
		}
		
		
		return 0;
	}
	
	
	
	/***** [END] "Buchhalterfunktion" *****/
	
	

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

	
	
	/***** [START] Reports *****/
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public ReportDataDTO getIncomingReportByOrganisationId(int id, String dateFromStr, String dateToStr) throws Exception
	{
		
		SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
		Date dateFrom = df.parse(dateFromStr);
		Date dateTo = df.parse(dateToStr);
		
		String sqlString = " select name as \"organisationName\", organisation_id as \"organisationId\",  sum(numberpu * pricepu)  as \"totalPrice\", sum(numberpu * weightpu) as \"totalWeight\"	"+
					"from organisation join incoming_delivery id using (organisation_id) "+
					"join incoming_article using (incoming_delivery_id) "+
					" join article using (article_id) "+
					"where organisation_id = :id "+
					"AND id.date between :dateFrom and :dateTo "+
					"group by name, organisation_id;";
		
		ReportDataDTO reportDataDTO = (ReportDataDTO) this.sessionFactory.getCurrentSession().createSQLQuery(sqlString)
			.addScalar("organisationName", StandardBasicTypes.STRING)
			.addScalar("organisationId", StandardBasicTypes.INTEGER)
			.addScalar("totalPrice", StandardBasicTypes.DOUBLE)
			.addScalar("totalWeight", StandardBasicTypes.DOUBLE)
			.setParameter("id", id)
			.setParameter("dateFrom", dateFrom)
			.setParameter("dateTo", dateTo)
			.setResultTransformer(Transformers.aliasToBean(ReportDataDTO.class))
			.uniqueResult();
		
		// round price
		reportDataDTO.setTotalPrice( (double)Math.round(reportDataDTO.getTotalPrice()*100)/100 );
		reportDataDTO.setTotalWeight( (double)Math.round(reportDataDTO.getTotalWeight()*100)/100 );
		reportDataDTO.setDateFrom(df.format(dateFrom));
		reportDataDTO.setDateTo(df.format(dateTo));
		
		return reportDataDTO;
	}
	
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public List<ReportDataDTO> getIncomingReportForAllOrganisations(String dateFromStr, String dateToStr) throws Exception
	{
		
		SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
		Date dateFrom = df.parse(dateFromStr);
		Date dateTo = df.parse(dateToStr);
		
		String sqlString = " select name as \"organisationName\", organisation_id as \"organisationId\",  sum(numberpu * pricepu)  as \"totalPrice\", sum(numberpu * weightpu) as \"totalWeight\"	"+
					"from organisation join incoming_delivery id using (organisation_id) "+
					"join incoming_article using (incoming_delivery_id) "+
					"join article using (article_id) "+
					"where id.date between :dateFrom and :dateTo "+
					"group by name, organisation_id;";
		
		@SuppressWarnings("unchecked")
		List<ReportDataDTO> reportDataDTOs = (List<ReportDataDTO>) this.sessionFactory.getCurrentSession().createSQLQuery(sqlString)
			.addScalar("organisationName", StandardBasicTypes.STRING)
			.addScalar("organisationId", StandardBasicTypes.INTEGER)
			.addScalar("totalPrice", StandardBasicTypes.DOUBLE)
			.addScalar("totalWeight", StandardBasicTypes.DOUBLE)
			.setParameter("dateFrom", dateFrom)
			.setParameter("dateTo", dateTo)
			.setResultTransformer(Transformers.aliasToBean(ReportDataDTO.class))
			.list();
		
		for (ReportDataDTO rd : reportDataDTOs)
		{
			// round price
			rd.setTotalPrice( (double)Math.round(rd.getTotalPrice()*100)/100 );
			rd.setTotalWeight( (double)Math.round(rd.getTotalWeight()*100)/100 );
			rd.setDateFrom(df.format(dateFrom));
			rd.setDateTo(df.format(dateTo));
		}
		
		return reportDataDTOs;
	}
	
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public ReportDataDTO getOutgoingReportByOrganisationId(int id, String dateFromStr, String dateToStr) throws Exception
	{
		
		SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
		Date dateFrom = df.parse(dateFromStr);
		Date dateTo = df.parse(dateToStr);
		
		String sqlString = " select name as \"organisationName\", organisation_id as \"organisationId\",  sum(numberpu * pricepu)  as \"totalPrice\", sum(numberpu * weightpu) as \"totalWeight\"	"+
					"from organisation join outgoing_delivery od using (organisation_id) "+
					"join outgoing_article using (outgoing_delivery_id) "+
					" join article using (article_id) "+
					"where organisation_id = :id "+
					"AND od.date between :dateFrom and :dateTo "+
					"group by name, organisation_id;";
		
		ReportDataDTO reportDataDTO = (ReportDataDTO) this.sessionFactory.getCurrentSession().createSQLQuery(sqlString)
			.addScalar("organisationName", StandardBasicTypes.STRING)
			.addScalar("organisationId", StandardBasicTypes.INTEGER)
			.addScalar("totalPrice", StandardBasicTypes.DOUBLE)
			.addScalar("totalWeight", StandardBasicTypes.DOUBLE)
			.setParameter("id", id)
			.setParameter("dateFrom", dateFrom)
			.setParameter("dateTo", dateTo)
			.setResultTransformer(Transformers.aliasToBean(ReportDataDTO.class))
			.uniqueResult();
		
		// round price
		reportDataDTO.setTotalPrice( (double)Math.round(reportDataDTO.getTotalPrice()*100)/100 );
		reportDataDTO.setTotalWeight( (double)Math.round(reportDataDTO.getTotalWeight()*100)/100 );
		reportDataDTO.setDateFrom(df.format(dateFrom));
		reportDataDTO.setDateTo(df.format(dateTo));
		
		return reportDataDTO;
	}
	
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public List<ReportDataDTO> getOutgoingReportForAllOrganisations(String dateFromStr, String dateToStr) throws Exception
	{
		
		SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
		Date dateFrom = df.parse(dateFromStr);
		Date dateTo = df.parse(dateToStr);
		
		String sqlString = " select name as \"organisationName\", organisation_id as \"organisationId\",  sum(numberpu * pricepu)  as \"totalPrice\", sum(numberpu * weightpu) as \"totalWeight\"	"+
					"from organisation join outgoing_delivery id using (organisation_id) "+
					"join outgoing_article using (outgoing_delivery_id) "+
					"join article using (article_id) "+
					"where id.date between :dateFrom and :dateTo "+
					"group by name, organisation_id;";
		
		@SuppressWarnings("unchecked")
		List<ReportDataDTO> reportDataDTOs = (List<ReportDataDTO>) this.sessionFactory.getCurrentSession().createSQLQuery(sqlString)
			.addScalar("organisationName", StandardBasicTypes.STRING)
			.addScalar("organisationId", StandardBasicTypes.INTEGER)
			.addScalar("totalPrice", StandardBasicTypes.DOUBLE)
			.addScalar("totalWeight", StandardBasicTypes.DOUBLE)
			.setParameter("dateFrom", dateFrom)
			.setParameter("dateTo", dateTo)
			.setResultTransformer(Transformers.aliasToBean(ReportDataDTO.class))
			.list();
		
		for (ReportDataDTO rd : reportDataDTOs)
		{
			// round price
			rd.setTotalPrice( (double)Math.round(rd.getTotalPrice()*100)/100 );
			rd.setTotalWeight( (double)Math.round(rd.getTotalWeight()*100)/100 );
			rd.setDateFrom(df.format(dateFrom));
			rd.setDateTo(df.format(dateTo));
		}
		
		return reportDataDTOs;
	}
	
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public ReportDataDTO getTotalSumOfAllIncomingDeliveries(String dateFromStr, String dateToStr) throws Exception
	{
		SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
		Date dateFrom = df.parse(dateFromStr);
		Date dateTo = df.parse(dateToStr);
		
		String sqlString = "select sum(numberpu * pricepu)  as \"totalPrice\", sum(numberpu * weightpu) as \"totalWeight\" "+
				"from incoming_delivery id join incoming_article using (incoming_delivery_id) "+
				"join article using (article_id) "+
				"where id.date between :dateFrom and :dateTo ;";
		
		ReportDataDTO reportDataDTO = (ReportDataDTO) this.sessionFactory.getCurrentSession().createSQLQuery(sqlString)
			.addScalar("totalPrice", StandardBasicTypes.DOUBLE)
			.addScalar("totalWeight", StandardBasicTypes.DOUBLE)
			.setParameter("dateFrom", dateFrom)
			.setParameter("dateTo", dateTo)
			.setResultTransformer(Transformers.aliasToBean(ReportDataDTO.class))
			.uniqueResult();
		
		// round price
		reportDataDTO.setTotalPrice( (double)Math.round(reportDataDTO.getTotalPrice()*100)/100 );
		reportDataDTO.setTotalWeight( (double)Math.round(reportDataDTO.getTotalWeight()*100)/100 );
		reportDataDTO.setDateFrom(df.format(dateFrom));
		reportDataDTO.setDateTo(df.format(dateTo));
		
		return reportDataDTO;
	}
	
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public ReportDataDTO getTotalSumOfAllOutgoingDeliveries(String dateFromStr, String dateToStr) throws Exception
	{
		SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
		Date dateFrom = df.parse(dateFromStr);
		Date dateTo = df.parse(dateToStr);
		
		String sqlString = "select sum(numberpu * pricepu)  as \"totalPrice\", sum(numberpu * weightpu) as \"totalWeight\" "+
				"from outgoing_delivery od join outgoing_article using (outgoing_delivery_id) "+
				"join article using (article_id) "+
				"where od.date between :dateFrom and :dateTo ;";
		
		ReportDataDTO reportDataDTO = (ReportDataDTO) this.sessionFactory.getCurrentSession().createSQLQuery(sqlString)
			.addScalar("totalPrice", StandardBasicTypes.DOUBLE)
			.addScalar("totalWeight", StandardBasicTypes.DOUBLE)
			.setParameter("dateFrom", dateFrom)
			.setParameter("dateTo", dateTo)
			.setResultTransformer(Transformers.aliasToBean(ReportDataDTO.class))
			.uniqueResult();
		
		// round price
		reportDataDTO.setTotalPrice( (double)Math.round(reportDataDTO.getTotalPrice()*100)/100 );
		reportDataDTO.setTotalWeight( (double)Math.round(reportDataDTO.getTotalWeight()*100)/100 );
		reportDataDTO.setDateFrom(df.format(dateFrom));
		reportDataDTO.setDateTo(df.format(dateTo));
		
		return reportDataDTO;
	}
	
	
	/***** [END] *****/
	
	/***** PersonAddressReport *****/
	public List<PersonAddressReportDataDTO> getPersonAddressReport() throws Exception
	{
		String sqlString = "select * from personAddressReportView";
		
		@SuppressWarnings("unchecked")
		List<PersonAddressReportDataDTO> reportDataList = (List<PersonAddressReportDataDTO>) this.sessionFactory.getCurrentSession().createSQLQuery(sqlString)
			.addScalar("salutation", StandardBasicTypes.STRING)
			.addScalar("title", StandardBasicTypes.STRING)
			.addScalar("lastName", StandardBasicTypes.STRING)
			.addScalar("firstName", StandardBasicTypes.STRING)
			.addScalar("privateAddress", StandardBasicTypes.STRING)
			.addScalar("privateZip", StandardBasicTypes.STRING)
			.addScalar("privateCity", StandardBasicTypes.STRING)
			.addScalar("privateCountry", StandardBasicTypes.STRING)
			.addScalar("orgName", StandardBasicTypes.STRING)
			.addScalar("orgType", StandardBasicTypes.STRING)
			.addScalar("orgAddress", StandardBasicTypes.STRING)
			.addScalar("orgZip", StandardBasicTypes.STRING)
			.addScalar("orgCity", StandardBasicTypes.STRING)
			.addScalar("orgCountry", StandardBasicTypes.STRING)
			.setResultTransformer(Transformers.aliasToBean(PersonAddressReportDataDTO.class))
			.list();

		return reportDataList;
	}
	
	/***** EmailReport *****/
	@Override
	public List<PersonEmailReportDataDTO> getPersonEmailReport() throws Exception
	{
		String sqlString = "select * from personEmailReportView";
		
		@SuppressWarnings("unchecked")
		List<PersonEmailReportDataDTO> reportDataList = (List<PersonEmailReportDataDTO>) this.sessionFactory.getCurrentSession().createSQLQuery(sqlString)
			.addScalar("personId", StandardBasicTypes.INTEGER)
			.addScalar("salutation", StandardBasicTypes.STRING)
			.addScalar("title", StandardBasicTypes.STRING)
			.addScalar("lastName", StandardBasicTypes.STRING)
			.addScalar("firstName", StandardBasicTypes.STRING)
			.addScalar("comment", StandardBasicTypes.STRING)
			.addScalar("email", StandardBasicTypes.STRING)
			.addScalar("emailType", StandardBasicTypes.STRING)
			.addScalar("organisationName", StandardBasicTypes.STRING)
			.setResultTransformer(Transformers.aliasToBean(PersonEmailReportDataDTO.class))
			.list();

		return reportDataList;
	}
	
	/***** [START Logging] *****/
	
	@Override
	@Transactional
	public int insertLogging(String text, int personId)
	{
		Logging logging = new Logging(0, new Timestamp(System.currentTimeMillis()), text, personId);
		this.sessionFactory.getCurrentSession().save(logging);
		return logging.getLoggingId();
	}
	
	/***** [END logging] *****/
	
}
