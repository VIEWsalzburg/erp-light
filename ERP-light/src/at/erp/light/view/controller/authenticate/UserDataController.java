package at.erp.light.view.controller.authenticate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import at.erp.light.view.listener.HttpSessionCollector;
import at.erp.light.view.model.Person;
import at.erp.light.view.services.IDataBase;

/**
 * This class is a RestController.<br/>
 * It returns the name of the current user.
 * @author Matthias Schnöll
 *
 */
@RestController
public class UserDataController {
	
	@Autowired
	private IDataBase dataBaseService;
	
	/**
	 * Returns the name of the current user.
	 * @param request
	 * @param response
	 * @return name of the current user
	 */
	@RequestMapping(value="secure/userdata")
	public String getUserData(HttpServletRequest request, 
	        HttpServletResponse response)
	{
			return request.getSession().getAttribute("username").toString();
	}
	
	
	/**
	 * This function returns a list of all persons logged in to the system
	 * @return string in JSON representation
	 */
	@RequestMapping(value="secure/getCurrentUsers")
	public String getCurrentUsers() {
		
		Map<Integer, String> personMap = new HashMap<Integer, String>();
		
		Map<String, HttpSession> sessions = HttpSessionCollector.getSessions();
		
		for (Entry<String, HttpSession> entry : sessions.entrySet())
		{
			HttpSession session = entry.getValue();
			
			if (session.getAttribute("id") != null)
			{
				int personId = (int) session.getAttribute("id");
				Person person = dataBaseService.getPersonById(personId);
				personMap.put(personId, person.getLastName()+" "+person.getFirstName());
			}	
			
		}
		
		StringBuilder sb = new StringBuilder();
		sb.append("{\"persons\":[");

		for (Entry<Integer, String> entry : personMap.entrySet())
		{
			sb.append("\"").append(entry.getValue()).append("\"");
			sb.append(",");
		}
		
		// remove last comma
		sb.deleteCharAt(sb.length()-1);
		
		sb.append("]}");
		
		return sb.toString();
		
	}
	
	
}
