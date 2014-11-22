package at.erp.light.view.controller.authenticate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LogoutController{
   
   @RequestMapping(value="/logout")
   public boolean postListener(HttpServletRequest request, 
	        HttpServletResponse response)
   {	   
	   //If submitted password valid: 
	   request.getSession().invalidate();
	   return true;
   }

}