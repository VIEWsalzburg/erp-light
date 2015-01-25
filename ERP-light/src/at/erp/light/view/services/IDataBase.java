package at.erp.light.view.services;

import java.util.List;
import java.util.Set;

import org.hibernate.HibernateException;

import at.erp.light.view.model.Article;
import at.erp.light.view.model.AvailArticleInDepot;
import at.erp.light.view.model.Category;
import at.erp.light.view.model.DeliveryList;
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


/**
 * Interface IDataBase, used for communication with the database.
 * @author Matthias
 *
 */
public interface IDataBase {
	
	// Persons
	/**
	 * This function gets a Person element with the given ID from the DB.
	 * @param id
	 * @return Person object / null, if Person does not exist
	 */
	public Person getPersonById(int id) throws HibernateException;
	

	
	/**
	 * unimplemented
	 * @param type
	 * @return
	 */
	public List<Person> getPersonsByType(Type type) throws HibernateException;
	
	/**
	 * This function returns all Persons from the DB.
	 * @return a Set with all Persons. 
	 */
	public List<Person> getAllPersons() throws HibernateException;
	
	/**
	 * unimplemented
	 * @param loginEmail
	 * @return
	 */
	public Person getPersonByLoginEmail(String loginEmail) throws HibernateException;
	
	/**
	 * Saves or updates the given Person in the DB. All fields are parsed and reassign depending on the saved entities.
	 * New Objects are automatically created and assigned (Address, City, Country, Telephones, Emails)
	 * @param person
	 * @return
	 */
	public int setPerson(Person person) throws HibernateException;
	
	/**
	 * unimplemented
	 * @param persons
	 * @return
	 */
	public int setPersons(List<Person> persons) throws HibernateException;
	
	/**
	 * deletes a person by setting the active flag to 0
	 * @param id
	 * @return 0 on success
	 */
	public int deletePersonById(int id) throws HibernateException;
	
	// Organisations
	public Organisation getOrganisationById(int id) throws HibernateException;
	public List<Organisation> getOrganisationsByCategory(Category category) throws HibernateException;
	public List<Organisation> getAllOrganisations() throws HibernateException;
	
	public int setOrganisation(Organisation organisation) throws HibernateException;
	public int setOrganisations(List<Organisation> organisations) throws HibernateException;
	
	/**
	 * deletes an organisation by setting the active flag to 0
	 * @param id
	 * @return 0 on success
	 * @throws HibernateException
	 */
	public int deleteOrganisationById(int id) throws HibernateException;
	
	
	// Incoming deliveries
	/**
	 * persists a new incoming delivery with the given incoming articles to the DB
	 * @param incomingDelivery
	 * @return id of the persisted entity
	 * @throws Exception
	 */
	public int setNewIncomingDelivery(IncomingDelivery incomingDelivery) throws Exception;
	
	public int updateIncomingDelivery(IncomingDelivery incomingDelivery) throws Exception;
	
	/**
	 * deletes an incoming delivery with the associated incoming articles; only works if articles haven't been added to outgoing delivery
	 * @param id
	 * @return success of operation
	 * @throws Exception
	 */
	public boolean deleteIncomingDeliveryById(int id) throws Exception;
	
	/**
	 * archive an incoming delivery to hide it in the frontend
	 * @param id of the incoming delivery
	 * @param status which should be set
	 * @return success of operation
	 * @throws Exception
	 */
	public boolean archiveIncomingDeliveryById(int id, int status) throws Exception;
	
	/**
	 * return a list of all incoming deliveries
	 * @return list of all incoming deliveries
	 * @throws HibernateException
	 */
	public List<IncomingDelivery> getAllIncomingDeliveries() throws HibernateException;
	
	/**
	 * return a list of all incomingDeliveries with the given archivedStatus
	 * @param archivedStatus of the deliveries, which should be returned
	 * @return list of all incomingDeliveries with the given archivedStatus
	 * @throws HibernateException
	 */
	public List<IncomingDelivery> getAllIncomingDeliveries(int archivedStatus) throws HibernateException;
	
	/**
	 * returns the incoming delivery with the given id
	 * @param id
	 * @return incoming delivery
	 * @throws HibernateException
	 */
	public IncomingDelivery getIncomingDeliveryById(int id) throws HibernateException;
	
	/**
	 * persists a new outgoing delivery with the given outgoing articles to the DB;
	 * checks if PUs of outgoing articles are allowed and the given articleIds exist in the DB;
	 * if outgoing delivery is not valid, a Exception is thrown and a DB rollback is performed
	 * @param outgoingDelivery
	 * @return id of the persisted delivery
	 * @throws Exception
	 */
	public int setNewOutgoingDelivery(OutgoingDelivery outgoingDelivery) throws Exception;
	
	/**
	 * delete an outgoing delivery with the associated outgoing articles;
	 * @param id
	 * @return success of operation
	 * @throws Exception
	 */
	public boolean deleteOutgoingDeliveryById(int id) throws Exception;
	
	/**
	 * archive an outgoing delivery to hide it in the frontend
	 * @param id of the outgoing delivery
	 * @param status which should be set
	 * @return success of operation
	 * @throws Exception
	 */
	public boolean archiveOutgoingDeliveryById(int id, int status) throws Exception;
	
	
	/**
	 * returns an outgoingDelivery with the given id
	 * @param id
	 * @return outgoingDelivery
	 * @throws HibernateException
	 */
	public OutgoingDelivery getOutgoingDeliveryById(int id) throws HibernateException;
	
	/**
	 * returns all outgoingDeliveries
	 * @return list with all outgoingDeliveries
	 * @throws HibernateException
	 */
	public List<OutgoingDelivery> getAllOutgoingDeliveries() throws HibernateException;
	
	/**
	 * return a list of all outgoingDeliveries with the given archivedStatus
	 * @param archivedStatus of the deliveries, which should be returned
	 * @return list of all outgoingDeliveries with the given archivedStatus
	 * @throws HibernateException
	 */
	public List<OutgoingDelivery> getAllOutgoingDeliveries(int archviedStatus) throws HibernateException;
	
	/**
	 * returns all available OutgoingDeliveries
	 * @return list with all outgoingDeliveries which are not booked
	 * @throws HibernateException
	 */
	public List<OutgoingDelivery> getAvailableOutgoingDeliveries() throws HibernateException;
	
	
	public int setOutgoingDeliveries(List<OutgoingDelivery> outgoingDeliveries) throws HibernateException;
	
	
	public int setIncomingDeliveries(List<IncomingDelivery> incomingDeliveries) throws HibernateException;
	
	// Incoming articles
	public IncomingArticle getIncomingArticleById(int id) throws HibernateException;
	public List<IncomingArticle> getAllIncomingArticles() throws HibernateException;
	
	public int setIncomingArticle(IncomingArticle incomingArticle) throws HibernateException;
	public int setIncomingArticles(List<IncomingArticle> incomingArticles) throws HibernateException;

	// Articles
	public Article getArticleById(int id) throws HibernateException;
	public List<Article> getAllArticles() throws HibernateException;
	
	public int setArticle(Article article) throws HibernateException;
	public int setArticles(List<Article> articles) throws HibernateException;

	// Outgoing articles
	public OutgoingArticle getOutgoingArticleById(int id) throws HibernateException;
	public List<OutgoingArticle> getAllOutgoingArticles() throws HibernateException;
	
	public int setOutgoingArticle(OutgoingArticle outgoingArticle) throws HibernateException;
	public int setOutgoingArticles(List<OutgoingArticle> outgoingArticles) throws HibernateException;
	
	
	
	// Delivery list
	/**
	 * returns the delivery list with the given id
	 * @param id
	 * @return delivery list object
	 * @throws HibernateException
	 */
	public DeliveryList getDeliveryListById(int id) throws HibernateException;
	
	/**
	 * returns all delivery lists
	 * @return list with all delivery lists
	 * @throws HibernateException
	 */
	public List<DeliveryList> getAllDeliveryLists() throws HibernateException;
	
	/**
	 * returns all delivery lists with the given archivedStatus
	 * @param archivedStatus
	 * @return list with all delivery lists
	 * @throws HibernateException
	 */
	public List<DeliveryList> getAllDeliveryLists(int archivedStatus) throws HibernateException;
	
	/**
	 * persist or update a new deliveryList to the DB
	 * @param deliveryList
	 * @return the id of the persisted deliveryList
	 * @throws HibernateException
	 */
	public int setDeliveryList(DeliveryList deliveryList) throws HibernateException;
	
	/**
	 * deletes the deliveryList with the given ID
	 * @param id
	 * @return the success of the operation
	 * @throws HibernateException
	 */
	boolean deleteDeliveryListById(int id) throws HibernateException;
	
	/**
	 * archives the deliveryList with the given ID
	 * @param id
	 * @param status which should be assigned
	 * @return the success of the operation
	 * @throws HibernateException
	 */
	boolean archiveDeliveryListById(int id, int status) throws HibernateException;
	
	public int setDeliveryLists(List<DeliveryList> deliveryLists) throws HibernateException;
	
	public int telephoneTest() throws HibernateException;
	
	
	
	
	
	
	// Types
	/**
	 * Gets the Type object with the given ID from the DB.
	 * @param id
	 * @return Type object / null, if given ID does not exist in DB
	 */
	public Type getTypeById(int id) throws HibernateException;
	
	/**
	 * Gets the Type object with the given Name from the DB.
	 * @param type
	 * @return Type object / null, if given Name does not exist in DB
	 */
	public Type getTypeByType(String type) throws HibernateException;
	
	/**
	 * Gets a Set with all Type objects from the DB.
	 * @return
	 */
	public List<Type> getAllTypes() throws HibernateException;
	
	// Permissions
	/**
	 * Gets the Permission object with the given ID from the DB.
	 * @param id
	 * @return Permission object / null, if given ID does not exist in DB
	 */
	public Permission getPermissionById(int id) throws HibernateException;
	
	/**
	 * Gets the Permission object with the given Name from the DB.
	 * @param permission
	 * @return Permission object / null, if given Name does not exist in DB
	 */
	public Permission getPermissionByPermission(String permission) throws HibernateException;
	
	// Platformuser
	public Platformuser getPlatformuserById(int id) throws HibernateException;
	public Platformuser getPlatformuserbyLoginEmail(String loginEmail) throws HibernateException;
	public Platformuser setPlatformuser(Platformuser platformuser) throws HibernateException;
	public void removePlatformuserById(int id) throws HibernateException;
	
	
	// Categories
	public Category getCategoryById(int id) throws HibernateException;
	public Category getCategoryByCategory(String category) throws HibernateException;
	public Category setCategory(Category category) throws HibernateException;
	public boolean deleteCategoryById(int id) throws HibernateException;
	public List<Category> getAllCategories() throws HibernateException;

	public List<Organisation> getOrganisationsByCategoryId(int id);

	
	
	// Returns a list of all available Articles in the Depot including the Number of PUs
	// The list bases on the VIEW AvailArticleInDepot
	/**
	 * returns a list of all available articles, which can be added to outgoing deliveries
	 * returns only those articles where the numberofPUs != 0
	 * @return
	 */
	public List<AvailArticleInDepot> getAvailableArticlesInDepot();
	
	/**
	 * Compares the incoming and outgoing articles and check if the number of the PUs are valid
	 * the numbers must be equal or incoming article PUs must be greater
	 * @return validity of the incoming and outgoing article PUs
	 */
	public boolean checkInAndOutArticlePUs();
	
	
}
