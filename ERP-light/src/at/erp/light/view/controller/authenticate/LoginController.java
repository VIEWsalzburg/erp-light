package at.erp.light.view.controller.authenticate;

import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import at.erp.light.view.authenticate.Authentication;
import at.erp.light.view.model.Platformuser;
import at.erp.light.view.services.IDataBase;
import at.erp.light.view.state.ControllerMessage;

/**
 * @author Steve Controller for authentication of a user.
 */
@RestController
public class LoginController {
	private static final Logger log = Logger.getLogger(LoginController.class
			.getName());

	@Autowired
	private IDataBase dataBaseService;

	/**
	 * Listens for login request
	 * 
	 * @param request
	 *            of login action
	 * @param response
	 *            of login action
	 * @param authentication
	 *            object witch contains login info (username/password)
	 * @return Authentication authenticated user, if possible
	 */
	@RequestMapping(value = "/authenticate")
	public ControllerMessage loginUser(HttpServletRequest request,
			HttpServletResponse response, Authentication authentication) {
		log.info("Authenticating user: " + authentication.getLoginEmail()
				+ " with Password: " + authentication.getPassword());
		// If submitted password valid:

		// TODO should be replaced by service call
		Platformuser existingUser = dataBaseService
				.getPlatformuserbyLoginEmail(authentication.getLoginEmail());

		if (existingUser == null) {
			return new ControllerMessage(false, "Anmeldung fehlgeschlagen!");
		}

		if (existingUser.getPassword().equals(authentication.getPassword())) {
			request.getSession().setAttribute("username",
					authentication.getLoginEmail());
			request.getSession().setAttribute("authenticated", true);
			return new ControllerMessage(true, "everything went well");
		}
		else
		{
			return new ControllerMessage(false, "Falsche Anmeldeinformationen");
		}

	}

}