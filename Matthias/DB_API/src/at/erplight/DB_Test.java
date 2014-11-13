package at.erplight;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.Iterator;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import at.erplight.model.Person;


public class DB_Test {
	
	private static SessionFactory factory;
	
	public static void main(String[] args) {
		
			try {
				// http://stackoverflow.com/questions/8621906/is-buildsessionfactory-deprecated-in-hibernate-4 - 13.11.2014 22:29
				// http://www.tutorialspoint.com/hibernate/hibernate_examples.htm - 13.11.2014 22:30
				Configuration configuration = new Configuration();
				configuration.configure("hibernate.cfg.xml");
				ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
				factory = configuration.buildSessionFactory(serviceRegistry);
				
			} catch (Throwable ex) {
				System.err.println("Failed to create sessionFactory object."+ex);
			}
		
			DB_Test dbTest = new DB_Test();
			
			dbTest.listPersons();
			
			Person mPerson = new Person();
			mPerson.setAnrede("Herr");
			mPerson.setTitel("");
			mPerson.setVorname("Christof");
			mPerson.setNachname("Haslauer");
			mPerson.setBemerkung("Copadata");
			mPerson.setAktualisierungsdatum(new Date(System.currentTimeMillis()));
			
			dbTest.addPerson(mPerson);
			
			dbTest.listPersons();
			
		return;
		

	}
	
	public void addPerson(Person mPerson) {
		Session session = factory.openSession();
		Transaction tx = null;
		
		try {
			tx = session.beginTransaction();
			
			int personId = (Integer) session.save(mPerson);
			System.out.println("personId "+personId);
			tx.commit();
			
		} catch (HibernateException e) {
			if (tx!=null) tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
	}
	
	
	public void listPersons() {
		Session session = factory.openSession();
		Transaction tx = null;
		
		try {
			tx = session.beginTransaction();
			List persons = session.createQuery("FROM Person").list();
			for (Iterator iterator = persons.iterator(); iterator.hasNext();)
			{
				Person person = (Person) iterator.next();
				System.out.println(person.getPersonId()+" "+person.getAnrede()+" "+person.getTitel()+" "+
						person.getVorname()+" "+person.getNachname()+" "+person.getBemerkung());
			}
			tx.commit();
		} catch (HibernateException e) {
			if (tx!=null) tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		
		
		
	}
	

}
