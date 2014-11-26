package at.erp.light.view.controller.authenticate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserDataController {
	@RequestMapping(value="secure/userdata")
	public String getUserData(HttpServletRequest request, 
	        HttpServletResponse response)
   {
		return request.getSession().getAttribute("username").toString();
   }
}
