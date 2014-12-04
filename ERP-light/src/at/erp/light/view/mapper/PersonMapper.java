package at.erp.light.view.mapper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.util.Assert;

import at.erp.light.view.dto.PersonDTO;
import at.erp.light.view.model.Address;
import at.erp.light.view.model.City;
import at.erp.light.view.model.Country;
import at.erp.light.view.model.Email;
import at.erp.light.view.model.Person;
import at.erp.light.view.model.Platformuser;
import at.erp.light.view.model.Telephone;
import at.erp.light.view.model.Type;

public class PersonMapper {

	private static DateFormat df = new SimpleDateFormat("dd.MM.yyyy");

	public static PersonDTO mapToDTO(Person person) {
		Assert.notNull(person);
		List<String> emails = new ArrayList<String>();
		List<String> telephones = new ArrayList<String>();
		List<String> types = new ArrayList<String>();

		for (Email mail : person.getEmails()) {
			emails.add(mail.getEmail());
		}

		for (Telephone telephone : person.getTelephones()) {
			telephones.add(telephone.getTelephone());
		}

		for (Type type : person.getTypes()) {
			types.add(type.getName());
		}

		Address address = person.getAddress();
		City city = person.getCity();
		Country country = person.getCountry();
		String cityString, zipString, countryString, addressString;

		if (city == null) {
			cityString = "";
			zipString = "";
		} else {
			cityString = city.getCity();
			zipString = city.getZip();
		}

		if (country == null) {
			countryString = "";
		} else {
			countryString = country.getCountry();
		}
		
		if (address == null) {
			addressString = "";
		} else {
			addressString = address.getAddress();
		}

		// TODO change platform user things to real
		PersonDTO mPerson = new PersonDTO(person.getPersonId(),
				person.getSalutation(), person.getTitle(),
				person.getFirstName(), person.getLastName(),
				person.getComment(), df.format(person.getUpdateTimestamp()),
				person.getActive(), addressString,
				cityString, zipString, countryString, "test", "test", "admin",
				types, emails, telephones);

		return mPerson;
	}
	
	public static Person mapToEntity(PersonDTO dto)
	{
		Person entity = new Person();
		
		entity.setPersonId(dto.getPersonId());
		entity.setSalutation(dto.getSalutation());
		entity.setTitle(dto.getTitle());
		entity.setFirstName(dto.getFirstName());
		entity.setLastName(dto.getLastName());
		entity.setComment(dto.getComment());
		entity.setUpdateTimestamp(new Date(System.currentTimeMillis()));
		entity.setActive(dto.getActive());
		
		entity.setAddress(new Address(0,dto.getAddress()));
		entity.setCity(new City(0, dto.getCity(), dto.getZip()));
		entity.setCountry(new Country(0, dto.getCountry()));
		
		Set<Type> types = new HashSet<Type>();
		for (String typeStr : dto.getTypes())
		{
			types.add(new Type(0, typeStr));
		}
		
		Set<Email> emails = new HashSet<Email>();
		for (String emailStr : dto.getEmails())
		{
			emails.add(new Email(0, new Type(0,"PRIVAT"), emailStr));
		}
		
		Set<Telephone> telephones = new HashSet<Telephone>();
		for (String telephoneStr : dto.getTelephones())
		{
			telephones.add(new Telephone(0, new Type(0, "PRIVAT"), telephoneStr));
		}
		
		entity.setTypes(types);
		entity.setTelephones(telephones);
		entity.setEmails(emails);
		
		//Platformuser platformuser = new Platformuser(dto.getPermission(), person, dto.getPassword(), dto.getLoginEmail());
		
		//entity.setPlatformuser(platformuser);
		
		// TODO Password must be removed from dto because it isn't necessary 
		// TODO get Platformuser from DB and set it
		// TODO get current Modifier from DB and set it
		// TODO refactor to set
		
		
		return entity;
	}
}
