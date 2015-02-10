package at.erp.light.view.controller.article;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.supercsv.io.CsvListWriter;
import org.supercsv.prefs.CsvPreference;

import at.erp.light.view.dto.AvailableArticleDTO;
import at.erp.light.view.dto.DeliveryListDTO;
import at.erp.light.view.dto.InOutArticlePUDTO;
import at.erp.light.view.dto.IncomingArticleDTO;
import at.erp.light.view.dto.IncomingDeliveryDTO;
import at.erp.light.view.dto.OutgoingArticleDTO;
import at.erp.light.view.dto.OutgoingDeliveryDTO;
import at.erp.light.view.generation.WordGenerator;
import at.erp.light.view.mapper.AvailableArticleMapper;
import at.erp.light.view.mapper.DeliveryListMapper;
import at.erp.light.view.mapper.IncomingDeliveryMapper;
import at.erp.light.view.mapper.OutgoingDeliveryMapper;
import at.erp.light.view.model.AvailArticleInDepot;
import at.erp.light.view.model.DeliveryList;
import at.erp.light.view.model.IncomingDelivery;
import at.erp.light.view.model.OutgoingDelivery;
import at.erp.light.view.model.Person;
import at.erp.light.view.services.IDataBase;
import at.erp.light.view.state.ControllerMessage;

@RestController
public class DistributionController {
	private static final Logger log = Logger.getLogger(DistributionController.class
			.getName());

	@Autowired
	private IDataBase dataBaseService;
	
	/***** [START] updateNumberOfPUs for Incoming and OutgoingArticles "Buchhalterfunktion" *****/
	
	@RequestMapping(value = "secure/articlePUDistribution/getListByArticleId/{articleId}")
	public List<InOutArticlePUDTO> getArticlePUDistributionListByArticleId(@PathVariable int articleId)
	{
		List<InOutArticlePUDTO> distributionList = new ArrayList<InOutArticlePUDTO>();
		
		try {
			distributionList = dataBaseService.getArticleDistributionByArticleId(articleId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return distributionList;
	}
	
	@RequestMapping(value = "secure/articlePUDistribution/updateDistributionList")
	public ControllerMessage updateArticlePUDistributionList(@RequestBody List<InOutArticlePUDTO> distributionList)
	{
		try {
			dataBaseService.updateArticleDistribution(distributionList);
			return new ControllerMessage(true, "Speichern erfolgreich");
		} catch (Exception e) {
			e.printStackTrace();
			return new ControllerMessage(false, "Speichern nicht erfolgreich: "+e.getMessage());
		}
		
	}
	
	
	
	@RequestMapping(value = "secure/articlePUDistribution/deleteArticleById/{articleId}")
	public ControllerMessage deleteArticleAndDistribution(@PathVariable int articleId)
	{
		try {
			dataBaseService.deleteArticleWithDistributionByArticleId(articleId);
			return new ControllerMessage(true, "Löschen erfolgreich!");
		} catch (Exception e) {
			e.printStackTrace();
			return new ControllerMessage(false, "Löschen nicht erfolgreich: "+e.getMessage());
		}
	}
	
	
}
