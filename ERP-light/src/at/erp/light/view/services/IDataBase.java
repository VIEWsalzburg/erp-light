package at.erp.light.view.services;

import java.util.List;

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

public interface IDataBase {
	
	// Persons
	public Person getPersonById(int id);
	Person getPersonById(int id, int FetchFlags);
	public List<Person> getPersonsByType(Type type);
	public List<Person> getAllPersons();
	public Person getPersonByLoginEmail(String loginEmail);
	
	public int setPerson(Person person);
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
	public Article getAllArticles();
	
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
	public Type getTypeById(int id);
	public Type getTypeByType(String type);
	
	// Permissions
	public Permission getPermissionById(int id);
	public Permission getPermissionByPermission(String permission);
	
	// Platformuser
	public Platformuser getPlatformuserById(int id);
	public Platformuser setPlatformuser(Platformuser platformuser);
	public void removePlatformuserById(int id);

}
