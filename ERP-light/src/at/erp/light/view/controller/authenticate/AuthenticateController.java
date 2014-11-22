package at.erp.light.view.controller.authenticate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import at.erp.light.view.authenticate.Authentication;

@RestController
public class AuthenticateController{
   
   @RequestMapping(value="/authenticate")
   public Authentication postListener(HttpServletRequest request, 
	        HttpServletResponse response)
   {	   
	   //If submitted password valid: 
	   request.getSession().setAttribute("authenticated", true);
	   return new Authentication(11,"Testuser", "skdfj33ljl");
   }

}