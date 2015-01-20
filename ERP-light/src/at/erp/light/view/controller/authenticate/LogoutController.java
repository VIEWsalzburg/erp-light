package at.erp.light.view.controller.authenticate;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LogoutController {

	private static final Logger log = Logger
			.getLogger(LogoutController.class.getName());

	@RequestMapping(value = "/logout")
	public boolean postListener(HttpServletRequest request,
			HttpServletResponse response) {
		
		log.info("Performing logout");
		
		// If submitted password valid:
		request.getSession().invalidate();
		try {
			response.sendRedirect("http://"+request.getServerName()+":"+request.getServerPort()+"/ERP-light");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}

}