package at.erp.light.view.mapper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.util.Assert;

import at.erp.light.view.dto.EmailDTO;
import at.erp.light.view.dto.PersonDTO;
import at.erp.light.view.dto.TelephoneDTO;
import at.erp.light.view.model.Address;
import at.erp.light.view.model.City;
import at.erp.light.view.model.Country;
import at.erp.light.view.model.Email;
import at.erp.light.view.model.Permission;
import at.erp.light.view.model.Person;
import at.erp.light.view.model.Platformuser;
import at.erp.light.view.model.Telephone;
import at.erp.light.view.model.Type;

public class PersonMapper {

	private static DateFormat df = new SimpleDateFormat("dd.MM.yyyy");

	public static PersonDTO mapToDTO(Person person) {
		Assert.notNull(person);
		List<EmailDTO> emails = new ArrayList<EmailDTO>();
		List<TelephoneDTO> telephones = new ArrayList<TelephoneDTO>();
		List<String> types = new ArrayList<String>();

		for (Email mail : person.getEmails()) {
			emails.add(new EmailDTO(mail.getEmail(), mail.getType().getName()));
		}

		for (Telephone telephone : person.getTelephones()) {
			telephones.add(new TelephoneDTO(telephone.getTelephone(), telephone.getType().getName()));
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

		String loginMail = "";
		String permission = "";

		Platformuser pUser = person.getPlatformuser();
		
		if (pUser != null) {
			loginMail = pUser.getLoginEmail();
			Permission perm = pUser.getPermission();
			if(perm !=null)
			{
				permission = pUser.getPermission().getPermission();
			}
			else
			{
				permission="User";
			}
		}

		String lastEditor = "kA";
		if (person.getLastEditor() != null)
			lastEditor = person.getLastEditor().getFirstName() + " " + person.getLastEditor().getLastName();
		
		// TODO change platform user things to real
		PersonDTO mPerson = new PersonDTO(person.getPersonId(),
				person.getSalutation(), person.getTitle(),
				person.getFirstName(), person.getLastName(),
				person.getComment(), df.format(person.getUpdateTimestamp()),
				person.getActive(), addressString, cityString, zipString,
				countryString, loginMail, permission, lastEditor, types, emails, telephones);

		return mPerson;
	}

	public static Person mapToEntity(PersonDTO dto) {
		Person entity = new Person();
		entity.setPersonId(dto.getPersonId());
		entity.setSalutation(dto.getSalutation());
		entity.setTitle(dto.getTitle());
		entity.setFirstName(dto.getFirstName());
		entity.setLastName(dto.getLastName());
		entity.setComment(dto.getComment());
		entity.setUpdateTimestamp(new Date(System.currentTimeMillis()));
		entity.setActive(dto.getActive());

		entity.setAddress(new Address(0, dto.getAddress()));
		entity.setCity(new City(0, dto.getCity(), dto.getZip()));
		entity.setCountry(new Country(0, dto.getCountry()));

		Set<Type> types = new HashSet<Type>();
		for (String typeStr : dto.getTypes()) {
			types.add(new Type(0, typeStr));
		}
		
		// TODO gather EMail types
		Set<Email> emails = new HashSet<Email>();
		for (EmailDTO emailDTO : dto.getEmails()) {
			emails.add(new Email(0, new Type(0, emailDTO.getType()), emailDTO.getMail()));
		}

		Set<Telephone> telephones = new HashSet<Telephone>();
		for (TelephoneDTO telephoneDTO : dto.getTelephones()) {
			telephones.add(new Telephone(0, new Type(0, telephoneDTO.getType()), telephoneDTO.getTelephone()));
		}
		
		entity.setTypes(types);
		entity.setTelephones(telephones);
		entity.setEmails(emails);

		return entity;
	}
}
