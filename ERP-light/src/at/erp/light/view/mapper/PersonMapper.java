package at.erp.light.view.mapper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import at.erp.light.view.dto.PersonDTO;
import at.erp.light.view.model.Email;
import at.erp.light.view.model.Person;
import at.erp.light.view.model.Telephone;
import at.erp.light.view.model.Type;

public class PersonMapper {

	private static DateFormat df = new SimpleDateFormat("dd.MM.yyyy");

	public static PersonDTO mapToDTO(Person person)
	{
		List<String> emails = new ArrayList<String>();
		List<String> telephones = new ArrayList<String>();
		List<String> types = new ArrayList<String>();
		
		for(Email mail : person.getEmails())
		{
			emails.add(mail.getEmail());
		}

		for(Telephone telephone: person.getTelephones())
		{
			telephones.add(telephone.getTelephone());
		}

		for(Type type : person.getTypes())
		{
			types.add(type.getName());
		}
		
		PersonDTO mPerson = new PersonDTO(person.getPersonId(), 
				 person.getSalutation(), 
				 person.getTitle(), 
				 person.getFirstName(), 
				 person.getLastName(), 
				 person.getComment(), 
				 df.format(person.getUpdateTimestamp()), 
				 person.getActive(), 
				 person.getAddress().getAddress(), 
				 person.getCity().getCity(), 
				 person.getCity().getZip(), 
				 person.getCountry().getCountry(), 
				 "test", "test", "admin", types, emails, telephones);
		
		
		
		
		return mPerson;
	}
}
