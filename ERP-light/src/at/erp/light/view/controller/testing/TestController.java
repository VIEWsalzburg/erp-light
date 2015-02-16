package at.erp.light.view.controller.testing;


import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import at.erp.light.view.authenticate.HashGenerator;
import at.erp.light.view.dto.InOutArticlePUDTO;
import at.erp.light.view.dto.ReportDataDTO;
import at.erp.light.view.model.Address;
import at.erp.light.view.model.Article;
import at.erp.light.view.model.AvailArticleInDepot;
import at.erp.light.view.model.Category;
import at.erp.light.view.model.City;
import at.erp.light.view.model.Country;
import at.erp.light.view.model.DeliveryList;
import at.erp.light.view.model.Email;
import at.erp.light.view.model.IncomingArticle;
import at.erp.light.view.model.IncomingDelivery;
import at.erp.light.view.model.Organisation;
import at.erp.light.view.model.OutgoingArticle;
import at.erp.light.view.model.OutgoingDelivery;
import at.erp.light.view.model.Permission;
import at.erp.light.view.model.Person;
import at.erp.light.view.model.Platformuser;
import at.erp.light.view.model.Telephone;
import at.erp.light.view.model.Type;
import at.erp.light.view.services.IDataBase;

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
	
	
	@RequestMapping(value = "IncomingReportByOrganisationId/{id}")
	public ReportDataDTO reportTest1(@PathVariable int id) throws Exception {	
		ReportDataDTO reportDataDTO = dataBaseService.getIncomingReportByOrganisationId(id, "10.01.2015", "31.05.2015");
		return reportDataDTO;
	}
	
	@RequestMapping(value = "OutgoingReportByOrganisationId/{id}")
	public ReportDataDTO reportTest2(@PathVariable int id) throws Exception {	
		ReportDataDTO reportDataDTO = dataBaseService.getOutgoingReportByOrganisationId(id, "10.01.2015", "31.05.2015");
		return reportDataDTO;
	}
	
	@RequestMapping(value = "IncomingReportForAllOrganisations")
	public List<ReportDataDTO> reportTest3() throws Exception {	
		List<ReportDataDTO> reportDataDTOs = dataBaseService.getIncomingReportForAllOrganisations("10.01.2015", "31.05.2015");
		return reportDataDTOs;
	}
	
	@RequestMapping(value = "OutgoingReportForAllOrganisations")
	public List<ReportDataDTO> reportTest4() throws Exception {	
		List<ReportDataDTO> reportDataDTOs = dataBaseService.getOutgoingReportForAllOrganisations("10.01.2015", "31.05.2015");
		return reportDataDTOs;
	}
	
	@RequestMapping(value = "TotalSumOfAllIncomingDeliveries")
	public ReportDataDTO reportTest5() throws Exception {	
		ReportDataDTO reportDataDTO = dataBaseService.getTotalSumOfAllIncomingDeliveries("10.01.2015", "31.05.2015");
		return reportDataDTO;
	}
	
	@RequestMapping(value = "TotalSumOfAllOutgoingDeliveries")
	public ReportDataDTO reportTest6() throws Exception {	
		ReportDataDTO reportDataDTO = dataBaseService.getTotalSumOfAllOutgoingDeliveries("10.01.2015", "31.05.2015");
		return reportDataDTO;
	}
	
}
