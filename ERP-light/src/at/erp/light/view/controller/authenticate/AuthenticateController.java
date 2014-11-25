package at.erp.light.view.controller.authenticate;

import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import at.erp.light.view.authenticate.Authentication;

@RestController
public class AuthenticateController{
   private static final Logger log = Logger.getLogger( AuthenticateController.class.getName() );
	
	
   @RequestMapping(value="/authenticate")
   public Authentication authenticationListener(HttpServletRequest request, 
	        HttpServletResponse response, Authentication authentication)
   {   
	   log.info("Authenticating user: " + authentication.getUsername() + " with Password: " + authentication.getPassword());
	   //If submitted password valid: 
	   request.getSession().setAttribute("authenticated", true);
	   return new Authentication(11,authentication.getUsername(), authentication.getPassword());
   }

}