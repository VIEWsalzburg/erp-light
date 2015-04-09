package at.erp.light.view.services;

import java.io.File;
import java.util.List;

import org.hibernate.HibernateException;

import at.erp.light.view.dto.InOutArticlePUDTO;
import at.erp.light.view.dto.LoggingDTO;
import at.erp.light.view.dto.PersonAddressReportDataDTO;
import at.erp.light.view.dto.PersonDTO;
import at.erp.light.view.dto.PersonEmailReportDataDTO;
import at.erp.light.view.dto.ReportDataDTO;
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
	 * @param id Id
	 * @return Person object / null, if Person does not exist
	 */
	public Person getPersonById(int id) throws HibernateException;
	
	/**
	 * Returns the number of active persons in the system. Used for pagewise loading.
	 * @return number of active persons
	 */
	public int getCountActivePersons() throws Exception;
	
	/**
	 * This function returns all Persons from the DB.
	 * @return a Set with all Persons. 
	 */
	public List<Person> getAllPersons() throws HibernateException;
	
	/**
	 * This function returns all active (non deleted) Persons from the DB.
	 * @return a Set with all Persons. 
	 */
	public List<Person> getAllActivePersons() throws HibernateException;
	
	/**
	 * This function returns a defined number of active (non deleted) Persons from the DB.
	 * @param count amount of Persons which should be loaded
	 * @param offset offset for loading pagewise
	 * @return List of Persons
	 * @throws HibernateException
	 */
	public List<Person> getActivePersons(int count, int offset) throws HibernateException;
	
	/**
	 * This function returns a minified set of data for all persons.<br/>
	 * It only contains the personId, salutation, firstName and lastName.<br/>
	 * This data can be used for faster loading in the frontend.
	 * @return list of all active persons
	 * @throws HibernateException
	 */
	public List<PersonDTO> getAllActivePersonsReducedData() throws HibernateException;
	
	/**
	 * Saves or updates the given Person in the DB. All fields are parsed and reassigned depending on the saved entities.<br/>
	 * New Objects are automatically created and assigned (Address, City, Country, Telephones, Emails)
	 * @param person
	 * @return Id of the saved person
	 */
	public int setPerson(Person person) throws HibernateException;
	

	
	/**
	 * Deletes a person by setting the active flag to 0<br/>
	 * Removes contact person entries for this person.
	 * @param id Id of the person
	 * @return 0 on success
	 */
	public int deletePersonById(int id) throws HibernateException;
	
	
	/**
	 * Get the organisation with the given Id.
	 * @param id Id
	 * @return Organisation entity
	 * @throws HibernateException
	 */
	public Organisation getOrganisationById(int id) throws HibernateException;
	
	/**
	 * Get all Organisations in the system.
	 * @param fetchParam This parameter defines if contactPersons are also fetched or not (for faster loading).
	 * @return list of all Organisations
	 * @throws HibernateException
	 */
	public List<Organisation> getAllOrganisations(int fetchParam) throws HibernateException;
	
	/**
	 * Returns all active organisations in the system.
	 * @param fetchParam This parameter defines if contactPersons are also fetched or not (for faster loading).
	 * @return list of all active organisations, which have not been deleted
	 * @throws HibernateException
	 */
	public List<Organisation> getAllActiveOrganisations(int fetchParam) throws HibernateException;
	
	
	/**
	 * Saves or updates an organisation in the system.
	 * @param organisation Organisation entity
	 * @return Id of the saved organisation
	 * @throws HibernateException
	 */
	public int setOrganisation(Organisation organisation) throws HibernateException;
	
	
	/**
	 * deletes an organisation by setting the active flag to 0<br/>
	 * Removes all contactPerson entries for the organisation
	 * @param id Id
	 * @return 0 on success
	 * @throws HibernateException
	 */
	public int deleteOrganisationById(int id) throws HibernateException;
	
	
	// Incoming deliveries
	/**
	 * persists a new incoming delivery with the given incoming articles to the DB
	 * @param incomingDelivery IncomingDelivery entity
	 * @return id of the persisted entity
	 * @throws Exception
	 */
	public int setNewIncomingDelivery(IncomingDelivery incomingDelivery) throws Exception;
	
	/**
	 * updates an existing incomingDelivery in the DB<br/>
	 * checks if the delivery is booked and updates it accordingly<br/>
	 * checks are performed to ensure article consistency
	 * @param incomingDelivery IncomingDelivery entity
	 * @return Id of the updated entity
	 * @throws Exception
	 */
	public int updateIncomingDelivery(IncomingDelivery incomingDelivery) throws Exception;
	
	/**
	 * deletes an incoming delivery with the associated incoming articles;<br/>
	 * only works if articles haven't been added to outgoing delivery (delivery is not booked)
	 * @param id Id
	 * @return success of operation
	 * @throws Exception
	 */
	public boolean deleteIncomingDeliveryById(int id) throws Exception;
	
	/**
	 * archive an incoming delivery to hide it in the frontend
	 * @param id Id of the incoming delivery
	 * @param status which should be set (0=unarchived, 1=archived)
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
	 * @param id Id
	 * @return incoming delivery
	 * @throws HibernateException
	 */
	public IncomingDelivery getIncomingDeliveryById(int id) throws HibernateException;
	
	
	/**
	 * returns the IncomingArticle with the specified Id
	 * @param id Id
	 * @return IncomingArticle Object
	 * @throws HibernateException
	 */
	public IncomingArticle getIncomingArticleById(int id) throws HibernateException;
	
	/**
	 * returns the IncomingArticle with the specified Id of the linked Article
	 * @param id Id
	 * @return list of IncomingArticle Objects which have linked the Article with the given Id
	 * @throws HibernateException
	 */
	public List<IncomingArticle> getIncomingArticlesByArticleId(int id) throws HibernateException;
	
	
	/**
	 * returns the OutgoingArticle with the specified Id
	 * @param id Id
	 * @return OutgoingArticle Object
	 * @throws HibernateException
	 */
	public OutgoingArticle getOutgoingArticleById(int id) throws HibernateException;
	
	/**
	 * returns the OutgoingArticle with the specified Id of the linked Article
	 * @param id Id
	 * @return list of OutgoingArticle Objects which have linked the Article with the given Id
	 * @throws HibernateException
	 */
	public List<OutgoingArticle> getOutgoingArticlesByArticleId(int id) throws HibernateException;
	
	
	/**
	 * persists a new outgoing delivery with the given outgoing articles to the DB;<br/>
	 * checks if PUs of outgoing articles are allowed and the given articleIds exist in the DB;<br/>
	 * if outgoing delivery is not valid, an Exception is thrown and a DB rollback is performed
	 * @param outgoingDelivery OutgoingDelivery entity
	 * @return id of the persisted delivery
	 * @throws Exception
	 */
	public int setNewOutgoingDelivery(OutgoingDelivery outgoingDelivery) throws Exception;
	
	
	/**
	 * updates an existing outgoingDelivery with the given outgoing articles in the DB;<br/>
	 * checks if PUs of outgoing articles are allowed and the given articleIds exist in the DB;<br/>
	 * if outgoing delivery is not valid, an Exception is thrown and a DB rollback is performed
	 * @param outgoingDelivery OutgoingDelivery entity
	 * @return id of the updated delivery
	 * @throws Exception
	 */
	int updateOutgoingDelivery(OutgoingDelivery outgoingDelivery) throws Exception;
	
	/**
	 * delete an outgoing delivery with the associated outgoing articles;
	 * @param id Id
	 * @return success of operation
	 * @throws Exception
	 */
	public boolean deleteOutgoingDeliveryById(int id) throws Exception;
	
	/**
	 * archive an outgoing delivery to hide it in the frontend
	 * @param id Id of the outgoing delivery
	 * @param status which should be set
	 * @return success of operation
	 * @throws Exception
	 */
	public boolean archiveOutgoingDeliveryById(int id, int status) throws Exception;
	
	
	/**
	 * returns an outgoingDelivery with the given id
	 * @param id Id
	 * @return OutgoingDelivery entity
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
	public List<OutgoingDelivery> getAllOutgoingDeliveries(int archivedStatus) throws HibernateException;
	
	/**
	 * returns all available OutgoingDeliveries
	 * @return list with all outgoingDeliveries which are not booked
	 * @throws HibernateException
	 */
	public List<OutgoingDelivery> getAvailableOutgoingDeliveries() throws HibernateException;
	
	
	/**
	 * returns a List with all Incoming and Outgoing Articles for a specific ArticleId
	 * this function is used for comparing and updating the PUs of the Incoming, Outgoing and Depot Articles
	 * @param articleId of the Article which the PUs should be updated for
	 * @return a List with the requested PU-distribution
	 * @throws Exception
	 */
	public List<InOutArticlePUDTO> getArticleDistributionByArticleId(int articleId) throws Exception;
	
	/**
	 * updates the new article distribution in the DB
	 * @param list of the new distribution
	 * @return 0 on success
	 * @throws Exception
	 */
	public int updateArticleDistribution(List<InOutArticlePUDTO> list) throws Exception;
	
	/**
	 * deletes an Article with the given ID from all incomingDeliveries and all outgoingDeliveries
	 * @param articleId ArticleId of the Article which should be deleted
	 * @return 0 on success
	 * @throws Exception
	 */
	public int deleteArticleWithDistributionByArticleId(int articleId) throws Exception;
	
	
	// Articles
	/**
	 * Returns the Article entity with the given Id
	 * @param id Id
	 * @return Article entity
	 * @throws HibernateException
	 */
	public Article getArticleById(int id) throws HibernateException;
	

	/**
	 * returns the delivery list with the given id
	 * @param id Id
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
	 * @param archivedStatus archivedStatus for the delivery lists
	 * @return list with all delivery lists
	 * @throws HibernateException
	 */
	public List<DeliveryList> getAllDeliveryLists(int archivedStatus) throws HibernateException;
	
	/**
	 * persists or updates a new deliveryList to the DB
	 * @param deliveryList DeliverList entity
	 * @return the id of the persisted deliveryList
	 * @throws HibernateException
	 */
	public int setDeliveryList(DeliveryList deliveryList) throws HibernateException;
	
	/**
	 * deletes the deliveryList with the given ID
	 * @param id Id
	 * @return the success of the operation
	 * @throws HibernateException
	 */
	boolean deleteDeliveryListById(int id) throws HibernateException;
	
	/**
	 * archives the deliveryList with the given ID
	 * @param id Id
	 * @param status which should be assigned
	 * @return the success of the operation
	 * @throws HibernateException
	 */
	boolean archiveDeliveryListById(int id, int status) throws HibernateException;
	
	
	
	// Types
	/**
	 * Gets the Type object with the given ID from the DB.
	 * @param id Id
	 * @return Type object / null, if given ID does not exist in DB
	 */
	public Type getTypeById(int id) throws HibernateException;
	
	/**
	 * Gets the Type object with the given Name from the DB.
	 * @param type Name of the type
	 * @return Type object / null, if given Name does not exist in DB
	 */
	public Type getTypeByType(String type) throws HibernateException;
	
	/**
	 * Gets a Set with all Type objects from the DB.
	 * @return list with all types
	 */
	public List<Type> getAllTypes() throws HibernateException;
	
	// Permissions
	/**
	 * Gets the Permission object with the given ID from the DB.
	 * @param id Id
	 * @return Permission object / null, if given ID does not exist in DB
	 */
	public Permission getPermissionById(int id) throws HibernateException;
	
	/**
	 * Gets the Permission object with the given Name from the DB.
	 * @param permission name of the permission
	 * @return Permission object / null, if given Name does not exist in DB
	 */
	public Permission getPermissionByPermission(String permission) throws HibernateException;
	
	// Platformuser
	
	/**
	 * Returns the platform user with the given Id
	 * @param id Id
	 * @return Platformuser entity
	 * @throws HibernateException
	 */
	public Platformuser getPlatformuserById(int id) throws HibernateException;
	
	/**
	 * Returns the first platform user with the given loginEmail<br/>
	 * if two platform users have the same email address, this can lead to a problem
	 * @param loginEmail
	 * @return Platformuser entity
	 * @throws HibernateException
	 */
	public Platformuser getPlatformuserbyLoginEmail(String loginEmail) throws HibernateException;
	
	/**
	 * Saves or updates the platform user in the DB
	 * @param platformuser Platformuser entity
	 * @return Platformuser entity which has been saved
	 * @throws HibernateException
	 */
	public Platformuser setPlatformuser(Platformuser platformuser) throws HibernateException;
	
	/**
	 * Removes the platform user with the given Id
	 * @param id Id
	 * @throws HibernateException
	 */
	public void removePlatformuserById(int id) throws HibernateException;
	
	
	// Categories
	/**
	 * Returns the category with the given Id
	 * @param id Id
	 * @return Category entity
	 * @throws HibernateException
	 */
	public Category getCategoryById(int id) throws HibernateException;
	
	/**
	 * Returns the category with the given name
	 * @param category Category entity
	 * @return Category entity, or null if it does not exist
	 * @throws HibernateException
	 */
	public Category getCategoryByCategory(String category) throws HibernateException;
	
	/**
	 * Saves or updates a category in the DB
	 * @param category Category entity
	 * @return Category entity
	 * @throws HibernateException
	 */
	public Category setCategory(Category category) throws HibernateException;
	
	/**
	 * Deletes the category with the given Id
	 * @param id Id
	 * @return success of operation
	 * @throws HibernateException
	 */
	public boolean deleteCategoryById(int id) throws HibernateException;
	
	/**
	 * Returns a list with all categories in the system.
	 * @return list with Category entities
	 * @throws HibernateException
	 */
	public List<Category> getAllCategories() throws HibernateException;

	/**
	 * Returns a list with all Organisations, which use the given category
	 * @param id Id of the category
	 * @return list with Organisation entities
	 */
	public List<Organisation> getOrganisationsByCategoryId(int id);

	
	/**
	 * returns a list of all available articles, which can be added to outgoing deliveries<br/>
	 * returns only those articles where the numberofPUs != 0<br/>
	 * The list bases on the DB view availarticleindepot
	 * @return list with available articles
	 */
	public List<AvailArticleInDepot> getAvailableArticlesInDepot();
	
	/**
	 * Compares the incoming and outgoing articles and check if the number of the PUs are valid<br/>
	 * If any of the available articles has a numberPU < 0, the check fails
	 * @return validity of the incoming and outgoing article PUs
	 */
	public boolean checkInAndOutArticlePUs();
	
	
	/***** Reports *****/
	
	/**
	 * Returns reporting data of incoming deliveries for the given organisation within the given time span
	 * @param id Id of the organisation
	 * @param dateFrom Start time span
	 * @param dateTo End time span
	 * @return ReportingDataDTO object containing the reporting data
	 * @throws Exception
	 */
	public ReportDataDTO getIncomingReportByOrganisationId(int id, String dateFrom, String dateTo) throws Exception;
	
	/**
	 * Returns a list with reporting data entries of incoming deliveries for all organisations within the given time span
	 * @param dateFrom Start time span
	 * @param dateTo End time span
	 * @return list with ReportingDataDTO objects
	 * @throws Exception
	 */
	public List<ReportDataDTO> getIncomingReportForAllOrganisations(String dateFrom, String dateTo) throws Exception;
	
	/**
	 * Returns reporting data of outgoing deliveries for the given organisation within the given time span
	 * @param id Id of the organisation
	 * @param dateFrom Start time span
	 * @param dateTo End time span
	 * @return ReportingDataDTO object containing the reporting data
	 * @throws Exception
	 */
	public ReportDataDTO getOutgoingReportByOrganisationId(int id, String dateFrom, String dateTo) throws Exception;
	
	/**
	 * Returns a list with reporting data entries of outgoing deliveries for all organisations within the given time span
	 * @param dateFrom Start time span
	 * @param dateTo End time span
	 * @return list with ReportingDataDTO objects
	 * @throws Exception
	 */
	public List<ReportDataDTO> getOutgoingReportForAllOrganisations(String dateFrom, String dateTo) throws Exception;
	
	/**
	 * Returns reporting data of all incoming deliveries within the given time span
	 * @param dateFrom Start time span
	 * @param dateTo End time span
	 * @return ReportingDataDTO object containing the reporting data
	 * @throws Exception
	 */
	public ReportDataDTO getTotalSumOfAllIncomingDeliveries(String dateFrom, String dateTo) throws Exception;
	
	/**
	 * Returns reporting data of all outgoing deliveries within the given time span
	 * @param dateFrom Start time span
	 * @param dateTo End time span
	 * @return ReportingDataDTO object containing the reporting data
	 * @throws Exception
	 */
	public ReportDataDTO getTotalSumOfAllOutgoingDeliveries(String dateFrom, String dateTo) throws Exception;

	/**
	 * Returns a File object containing the generated word file for the given DeliveryList 
	 * @param id Id of the delivery list
	 * @param lastEditor LastEditor of the delivery list
	 * @return File object
	 * @throws Exception
	 */
	public File generateDeliveryExport(int id, Person lastEditor) throws Exception;
	
	/***** StickerReport *****/
	/**
	 * Returns a list with the address report entries
	 * @return list with address report entries
	 * @throws Exception
	 */
	public List<PersonAddressReportDataDTO> getPersonAddressReport() throws Exception;
	
	/***** EmailReport *****/
	/**
	 * Returns a list with email report entries
	 * @return list with email report entries
	 * @throws Exception
	 */
	public List<PersonEmailReportDataDTO> getPersonEmailReport() throws Exception;
	
	
	/**
	 * Returns the size of the database in Bytes.
	 * @return database size in String
	 */
	public String getDatabaseSize() throws Exception;
	
	/***** Logging *****/
	/**
	 * saves a log message in the DB
	 * @param text Log message
	 * @param personId Id of the person, which carried out the specific action
	 * @return Id of the saved log message
	 */
	public int insertLogging(String text, int personId);

	/**
	 * Returns the latest logs from the DB
	 * @param count Number of logs, which should be returned
	 * @return list with log messages
	 */
	public List<LoggingDTO> getLatestLoggings(int count);

	/**
	 * Returns all logs from the DB
	 * @return list with log messages
	 */
	public List<LoggingDTO> getAllLoggings();
	
}
