package at.erp.light.view.services;

import java.util.List;
import java.util.Set;

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
	public Person getPersonById(int id);
	

	
	/**
	 * unimplemented
	 * @param type
	 * @return
	 */
	public List<Person> getPersonsByType(Type type);
	
	/**
	 * This function returns all Persons from the DB.
	 * @return a Set with all Persons. 
	 */
	public List<Person> getAllPersons();
	
	/**
	 * unimplemented
	 * @param loginEmail
	 * @return
	 */
	public Person getPersonByLoginEmail(String loginEmail);
	
	/**
	 * Saves or updates the given Person in the DB. All fields are parsed and reassign depending on the saved entities.
	 * New Objects are automatically created and assigned (Address, City, Country, Telephones, Emails)
	 * @param person
	 * @return
	 */
	public int setPerson(Person person);
	
	/**
	 * unimplemented
	 * @param persons
	 * @return
	 */
	public int setPersons(List<Person> persons);
	
	// Organisations
	public Organisation getOrganisationById(int id);
	public List<Organisation> getOrganisationsByCategory(Category category);
	public List<Organisation> getAllOrganisations();
	
	public int setOrganisation(Organisation organisation);
	public int setOrganisations(List<Organisation> organisations);
	
	// Incoming deliveries
	public IncomingDelivery getIncomingDeliveryById(int id);
	public List<IncomingDelivery> getAllIncomingDeliveries();
	
	public int setIncomingDelivery(IncomingDelivery incomingDelivery);
	public int setIncomingDeliveries(List<IncomingDelivery> incomingDeliveries);
	
	// Incoming articles
	public IncomingArticle getIncomingArticleById(int id);
	public List<IncomingArticle> getAllIncomingArticles();
	
	public int setIncomingArticle(IncomingArticle incomingArticle);
	public int setIncomingArticles(List<IncomingArticle> incomingArticles);

	// Articles
	public Article getArticleById(int id);
	public List<Article> getAllArticles();
	
	public int setArticle(Article article);
	public int setArticles(List<Article> articles);

	// Outgoing articles
	public OutgoingArticle getOutgoingArticleById(int id);
	public List<OutgoingArticle> getAllOutgoingArticles();
	
	public int setOutgoingArticle(OutgoingArticle outgoingArticle);
	public int setOutgoingArticles(List<OutgoingArticle> outgoingArticles);
	
	// Outgoing deliveries
	public OutgoingDelivery getOutgoingDeliveryById(int id);
	public List<OutgoingDelivery> getAllOutgoingDeliveries();
	
	public int setOutgoingDelivery(OutgoingDelivery outgoingDelivery);
	public int setOutgoingDeliveries(List<OutgoingDelivery> outgoingDeliveries);
	
	// Delivery list
	public DeliveryList getDeliveryListById(int id);
	public List<DeliveryList> getAllDeliveryLists();
	
	public int setDeliveryList(DeliveryList deliveryList);
	public int setDeliveryLists(List<DeliveryList> deliveryLists);
	public int telephoneTest();
	
	
	// Types
	/**
	 * Gets the Type object with the given ID from the DB.
	 * @param id
	 * @return Type object / null, if given ID does not exist in DB
	 */
	public Type getTypeById(int id);
	
	/**
	 * Gets the Type object with the given Name from the DB.
	 * @param type
	 * @return Type object / null, if given Name does not exist in DB
	 */
	public Type getTypeByType(String type);
	
	/**
	 * Gets a Set with all Type objects from the DB.
	 * @return
	 */
	public List<Type> getAllTypes();
	
	// Permissions
	/**
	 * Gets the Permission object with the given ID from the DB.
	 * @param id
	 * @return Permission object / null, if given ID does not exist in DB
	 */
	public Permission getPermissionById(int id);
	
	/**
	 * Gets the Permission object with the given Name from the DB.
	 * @param permission
	 * @return Permission object / null, if given Name does not exist in DB
	 */
	public Permission getPermissionByPermission(String permission);
	
	// Platformuser
	public Platformuser getPlatformuserById(int id);
	public Platformuser getPlatformuserbyLoginEmail(String loginEmail);
	public Platformuser setPlatformuser(Platformuser platformuser);
	public void removePlatformuserById(int id);
	
	
	// Categories
	public Category getCategoryById(int id);
	public Category getCategoryByCategory(String category);
	public Category setCategory(Category category);
	public List<Category> getAllCategories();
	
}
