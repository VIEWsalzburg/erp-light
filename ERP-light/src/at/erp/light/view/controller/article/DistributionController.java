package at.erp.light.view.controller.article;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import at.erp.light.view.dto.InOutArticlePUDTO;
import at.erp.light.view.services.IDataBase;
import at.erp.light.view.state.ControllerMessage;

/**
 * This class is a RestController.<br/>
 * It manages calls concerning the article distribution for incoming and outgoing deliveries.
 * @author Matthias Schnöll
 *
 */
@RestController
public class DistributionController {
	private static final Logger log = Logger.getLogger(DistributionController.class
			.getName());

	@Autowired
	private IDataBase dataBaseService;
	
	/***** [START] updateNumberOfPUs for Incoming and OutgoingArticles "Buchhalterfunktion" *****/
	
	
	/**
	 * Returns a list of objects representing the distribution for the given article Id.<br/>
	 * The list contains objects contianing the number of articles PUs for 
	 * incomingArticles, outgoingArticles and the depot.
	 * @param articleId
	 * @return list with the distribution objects
	 */
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
	
	
	/**
	 * Updates the article distribution in the DB with the given distribution.<br/>
	 * @param distributionList new distribution for the article; the elements contain the 
	 * Id of the incomingArticle, outgoingArticle or the depot and the number of PUs
	 * @param request
	 * @return ControllerMessage showing the success of the call
	 */
	@RequestMapping(value = "secure/articlePUDistribution/updateDistributionList")
	public ControllerMessage updateArticlePUDistributionList(@RequestBody List<InOutArticlePUDTO> distributionList,
			HttpServletRequest request)
	{
		try {
			int lastEditorId = (int) request.getSession().getAttribute("id");
			String currentUserPermission = dataBaseService.getPlatformuserById(lastEditorId).getPermission().getPermission();
			if (currentUserPermission.equals("Admin") == false && currentUserPermission.equals("ReadWrite") == false)
			{
				return new ControllerMessage(false, "Keine Berechtigung für diese Aktion!");
			}
			
			dataBaseService.updateArticleDistribution(distributionList);
			
			if (distributionList.size()>0){
				int articleId = 0;

				// get incomingArticleId for first Entry => most times the incomingArticle
				if (distributionList.get(0).getType() == 0)
					articleId = dataBaseService.getIncomingArticleById(distributionList.get(0).getInOutArticleId()).getArticle().getArticleId();

				log.info("articleDistribution updated for Article "+articleId);
				dataBaseService.insertLogging("[INFO] Artikelverteilung für Artikel mit der Id "+articleId+" geändert", lastEditorId);
			}
			
			return new ControllerMessage(true, "Speichern erfolgreich");
		} catch (Exception e) {
			e.printStackTrace();
			return new ControllerMessage(false, "Speichern nicht erfolgreich: "+e.getMessage());
		}
		
	}
	
	
	/**
	 * Deletes all incomingArticle, outoingArticle and depot entries with the given ArticleId.<br/>
	 * This function can be used to completely remove an Article from the DB.<br/>
	 * For example, if the article was not delivered by the deliverer.
	 * @param articleId of the Article which should be removed form all incomingArticles, outoingArticles and depot
	 * @param request
	 * @return ControllerMessage showing the success of the call
	 */
	@RequestMapping(value = "secure/articlePUDistribution/deleteArticleById/{articleId}")
	public ControllerMessage deleteArticleAndDistribution(@PathVariable int articleId, HttpServletRequest request)
	{
		try {
			int lastEditorId = (int) request.getSession().getAttribute("id");
			String currentUserPermission = dataBaseService.getPlatformuserById(lastEditorId).getPermission().getPermission();
			if (currentUserPermission.equals("Admin") == false && currentUserPermission.equals("ReadWrite") == false)
			{
				return new ControllerMessage(false, "Keine Berechtigung für diese Aktion!");
			}
			
			dataBaseService.deleteArticleWithDistributionByArticleId(articleId);
			log.info("deleted all articleDistributions for article "+articleId);
			dataBaseService.insertLogging("[INFO] Artikel mit der Id "+articleId+" für alle Wareneingänge und Warenausgänge gelöscht", lastEditorId);
			return new ControllerMessage(true, "Löschen erfolgreich!");
		} catch (Exception e) {
			e.printStackTrace();
			return new ControllerMessage(false, "Löschen nicht erfolgreich: "+e.getMessage());
		}
	}
	
}
