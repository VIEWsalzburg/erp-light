package at.erp.light.view.controller.administration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import at.erp.light.view.services.IDataBase;

/**
 * This class is a RestController.<br/>
 * It is used for performing administrative tasks on the system.
 * @author Matthias Schnöll
 *
 */
@RestController
public class AdminController {

	@Autowired
	private IDataBase dataBaseService;
	
	private AdminController()
	{
		super();
	}
	
	@RequestMapping(value = "secure/admin/getDatabaseSize")
	public String getDatabaseSize() {
		try {
			return ""+dataBaseService.getDatabaseSize();
		} catch(Exception e) {
			e.printStackTrace();
			return "Error";
		}
	}
	
}
