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

import at.erplight.model.Adresse;
import at.erplight.model.Land;
import at.erplight.model.Ort;
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
			
			Adresse mAdresse = new Adresse();
			mAdresse.setAdresse("Alte Landstraße 10A");
			
			Ort mOrt = new Ort();
			mOrt.setPlz("5112");
			mOrt.setOrt("Lamprechtshausen");
			
			Land mLand = new Land();
			mLand.setLand("Österreich");
			
			Person mPerson = new Person();
			mPerson.setAnrede("Herr");
			mPerson.setTitel("");
			mPerson.setVorname("Matthias");
			mPerson.setNachname("Schnöll");
			mPerson.setBemerkung("programmiert die Datenbank");
			mPerson.setAktualisierungsdatum(new Date(System.currentTimeMillis()));
			mPerson.setAdresse(mAdresse);
			mPerson.setOrt(mOrt);
			mPerson.setLand(mLand);
			
			dbTest.addPerson(mPerson);
			
			dbTest.listPersons();
			
			
		return;
		

	}
	
	public int addLand(Land mLand) {
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
	
	public int addOrt(Ort mOrt) {
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
	
	public int addAdresse(Adresse mAdresse) {
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
			addAdresse(mPerson.getAdresse());
			
			// save Ort
			addOrt(mPerson.getOrt());
			
			// save Land
			addLand(mPerson.getLand());
			
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
				System.out.println(person.getPersonId()+" "+person.getAnrede()+" "+person.getTitel()+" "+
						person.getVorname()+" "+person.getNachname()+" "+person.getBemerkung()+" "+person.getAdresse().getAdresse()+" "+
						person.getOrt().getPlz()+" "+person.getOrt().getOrt()+" "+person.getLand().getLand());
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
