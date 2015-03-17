package at.erp.light.view.controller.testing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import at.erp.light.view.authenticate.HashGenerator;
import at.erp.light.view.services.IDataBase;

/**
 * This class is a RestController.<br/>
 * It is used for testing purposes of single functionalities.
 * @author Matthias Schnöll
 *
 */
@RestController
public class TestController {

	@Autowired
	private IDataBase dataBaseService;
	
	private TestController()
	{
		super();
	}
	
	@RequestMapping(value = "HashPassword-default")
	public String hashPassword() {
		return HashGenerator.hashPasswordWithSalt("default");
	}
	
}
