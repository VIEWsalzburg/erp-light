package at.erp.light.view.services;

import java.util.List;
import java.util.Set;

import org.hibernate.HibernateException;

import at.erp.light.view.model.Article;
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
	 */
	public int deleteOrganisationById(int id) throws HibernateException;
	
	// Incoming deliveries
	public int setNewIncomingDelivery(IncomingDelivery incomingDelivery) throws HibernateException;
	public boolean removeIncomingDeliverById(int id) throws HibernateException;
	
	
	
	
	
	
	
	
	
	
	public IncomingDelivery getIncomingDeliveryById(int id) throws HibernateException;
	public List<IncomingDelivery> getAllIncomingDeliveries() throws HibernateException;
	
	
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
	
	// Outgoing deliveries
	public OutgoingDelivery getOutgoingDeliveryById(int id) throws HibernateException;
	public List<OutgoingDelivery> getAllOutgoingDeliveries() throws HibernateException;
	
	public int setOutgoingDelivery(OutgoingDelivery outgoingDelivery);
	public int setOutgoingDeliveries(List<OutgoingDelivery> outgoingDeliveries) throws HibernateException;
	
	// Delivery list
	public DeliveryList getDeliveryListById(int id) throws HibernateException;
	public List<DeliveryList> getAllDeliveryLists() throws HibernateException;
	
	public int setDeliveryList(DeliveryList deliveryList) throws HibernateException;
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
	
}
