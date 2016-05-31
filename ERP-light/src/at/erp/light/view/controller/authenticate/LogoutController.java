package at.erp.light.view.controller.authenticate;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import at.erp.light.view.model.Platformuser;
import at.erp.light.view.services.IDataBase;

/**
 * This class is a RestController.<br/>
 * It manages the logout process for the system.
 * @author Matthias Schnöll
 *
 */
@RestController
public class LogoutController {

	private static final Logger log = Logger
			.getLogger(LogoutController.class.getName());

	@Autowired
	private IDataBase dataBaseService;
	
	/**
	 * Performs the logout for the current user.<br/>
	 * This function redirects the user to the login page and destroys the session.
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/logout")
	public void postListener(HttpServletRequest request,
			HttpServletResponse response) {
		
		try {
			int id = (int) request.getSession().getAttribute("id");
			Platformuser platformuser = dataBaseService.getPlatformuserById(id);
			
			log.info("Performing logout for user with Id: "+id);
			dataBaseService.insertLogging("[INFO] "+platformuser.getLoginEmail()+" hat sich abgemeldet", platformuser.getPersonId());
		} catch (Exception e) {
			log.info("Didn't find session for user");
		}
		
		try {
			response.sendRedirect("/");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		request.getSession().invalidate();
		
	}

}