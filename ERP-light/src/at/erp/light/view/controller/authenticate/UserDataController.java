package at.erp.light.view.controller.authenticate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * This class is a RestController.<br/>
 * It returns the name of the current user.
 * @author Matthias Schnöll
 *
 */
@RestController
public class UserDataController {
	
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
}
