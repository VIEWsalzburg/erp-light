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

import at.erplight.model.Address;
import at.erplight.model.City;
import at.erplight.model.Country;
import at.erplight.model.Person;


public class DB_Test {
	
	private static SessionFactory factory;
	
	public static void main(String[] args) {
		
			Configuration configuration;
		
			try {
				// http://stackoverflow.com/questions/8621906/is-buildsessionfactory-deprecated-in-hibernate-4 - 13.11.2014 22:29
				// http://www.tutorialspoint.com/hibernate/hibernate_examples.htm - 13.11.2014 22:30
				configuration = new Configuration();
				configuration.configure("hibernate.cfg.xml");
				ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
				factory = configuration.buildSessionFactory(serviceRegistry);
				
			} catch (Throwable ex) {
				System.err.println("Failed to create sessionFactory object."+ex);
			}
		
			DB_Test dbTest = new DB_Test();
			
			dbTest.listPersons();
			
			Address mAdresse = new Address();
			mAdresse.setAddress("Alte Landstraße 10A");
			
			City mOrt = new City();
			mOrt.setZip("5112");
			mOrt.setCity("Lamprechtshausen");
			
			Country mLand = new Country();
			mLand.setCountry("Österreich");
			
			Person mPerson = new Person();
			mPerson.setSalutation("Herr");
			mPerson.setTitle("");
			mPerson.setFirstName("Matthias");
			mPerson.setLastName("Schnöll");
			mPerson.setComment("programmiert die Datenbank");
			mPerson.setUpdateTimestamp(new Date(System.currentTimeMillis()));
			mPerson.setAddress(mAdresse);
			mPerson.setCity(mOrt);
			mPerson.setCountry(mLand);
			
			dbTest.addPerson(mPerson);
			
			dbTest.listPersons();
			
			factory.close();
			
		return;
		

	}
	
	public int addLand(Country mLand) {
		Session session = factory.openSession();
		Transaction tx = null;
		int landId = 0;
		
		try {
			tx = session.beginTransaction();
			
			landId = (Integer) session.save(mLand);
			System.out.println("adresseId "+landId);
			tx.commit();
			
		} catch (HibernateException e) {
			if (tx!=null) tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return landId;
	}
	
	public int addOrt(City mOrt) {
		Session session = factory.openSession();
		Transaction tx = null;
		int ortId = 0;
		
		try {
			tx = session.beginTransaction();
			
			ortId = (Integer) session.save(mOrt);
			System.out.println("ortId "+ortId);
			tx.commit();
			
		} catch (HibernateException e) {
			if (tx!=null) tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return ortId;
	}
	
	public int addAdresse(Address mAdresse) {
		Session session = factory.openSession();
		Transaction tx = null;
		int adresseId = 0;
		
		try {
			tx = session.beginTransaction();
			
			adresseId = (Integer) session.save(mAdresse);
			System.out.println("adresseId "+adresseId);
			tx.commit();
			
		} catch (HibernateException e) {
			if (tx!=null) tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return adresseId;
	}
	
	public int addPerson(Person mPerson) {
		Session session = factory.openSession();
		Transaction tx = null;
		int personId = 0;
		
		try {
			tx = session.beginTransaction();
			
			// save Adresse
			addAdresse(mPerson.getAddress());
			
			// save Ort
			addOrt(mPerson.getCity());
			
			// save Land
			addLand(mPerson.getCountry());
			
			personId = (Integer) session.save(mPerson);
			System.out.println("personId "+personId);
			tx.commit();
			
		} catch (HibernateException e) {
			if (tx!=null) tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return personId;
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
				System.out.println(person.getPersonId()+" "+person.getSalutation()+" "+person.getTitle()+" "+
						person.getFirstName()+" "+person.getLastName()+" "+person.getComment()+" "+person.getAddress().getAddress()+" "+
						person.getCity().getZip()+" "+person.getCity().getCity()+" "+person.getCountry().getCountry());
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
