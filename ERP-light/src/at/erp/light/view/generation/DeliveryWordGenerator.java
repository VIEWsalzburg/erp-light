package at.erp.light.view.generation;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.UnderlinePatterns;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import at.erp.light.view.dto.DeliveryListDTO;
import at.erp.light.view.dto.OutgoingArticleDTO;
import at.erp.light.view.dto.OutgoingDeliveryDTO;
import at.erp.light.view.model.Article;
import at.erp.light.view.model.DeliveryList;
import at.erp.light.view.model.Organisation;
import at.erp.light.view.model.OutgoingArticle;
import at.erp.light.view.model.OutgoingDelivery;
import at.erp.light.view.model.Person;
import at.erp.light.view.services.IDataBase;

public class DeliveryWordGenerator {
	 
	public static File generateDeliveryExport(DeliveryList deliveryList, String doneBy, IDataBase dataBase) throws Exception {
	        XWPFDocument doc = new XWPFDocument();

	        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
	        
//	        Header
	        
	        XWPFParagraph p1 = doc.createParagraph();
	        XWPFRun r1 = p1.createRun();
	        r1.setBold(true);
	        r1.setText("Lieferliste für " + sdf.format(deliveryList.getDate()));
	        r1.setColor("458B00");
	        r1.setFontFamily("Arial");
	        r1.setFontSize(18);
	        r1.setBold(true);
	        r1.addTab();
	        r1.addTab();
	        r1.addTab();
	        r1.addTab();
	        r1.addTab();
	        r1.addPicture(DeliveryWordGenerator.class.getResourceAsStream("VIEW_Logo_4c.png"), XWPFDocument.PICTURE_TYPE_PNG, "VIEW_Logo_4c.png", Units.toEMU(80), Units.toEMU(55)); // 200x200 pixels	        
//	        Disponert von
	        
	        XWPFParagraph p2 = doc.createParagraph();
	        XWPFRun r4 = p2.createRun();
	        r4.setText("Disponiert von: ");
	        setBoldUnderlined(r4);
	        
	        XWPFRun r6 = p2.createRun();
	        r6.setText(doneBy);
	        setArial12(r6);
	        
	        
//	        Fahrerteam
	        
	        XWPFParagraph p3 = doc.createParagraph();
	        XWPFRun r5 = p3.createRun();
	        r5.setText("Fahrteam: ");
	        setBoldUnderlined(r5);

	        XWPFRun r7 = p3.createRun();
	        r7.setText(deliveryList.getDriver() + ", " +deliveryList.getPassenger());
	        setArial12(r7);
	        
//	        Abholen [START]
	        
	        XWPFParagraph p4 = doc.createParagraph();
	        XWPFRun r8 = p4.createRun();
	        r8.setText("Abholen: ");
	        setBoldUnderlined(r8);
	        
	        
	        // Overview of the following process:
	        // get all OutgoingDeliveries
	        // get all OutgoingArticles and combine them according to their ArticleId and calculate the Sum of total articles in this deliveryList
	        // get Organisation according to the ArticleId and write them combined into the incoming Section
	        
	        
	        
	        // get all OutgoingDeliveries
	        List<OutgoingDelivery> outgoingDeliveries = new ArrayList<OutgoingDelivery>(deliveryList.getOutgoingDeliveries());
	        
	        List<OutgoingArticle> allOutgoingArticles = new ArrayList<OutgoingArticle>();
	        for (OutgoingDelivery oD : outgoingDeliveries)
	        {
	        	allOutgoingArticles.addAll(oD.getOutgoingArticles());
	        }
	        
	        // create Map for summing up all outgoing ArticlePUs by ArticleId
	        Map<Integer, Integer> articleSumMap = new HashMap<Integer, Integer>();
	        for (OutgoingArticle oA : allOutgoingArticles)
	        {
	        	int articleId = oA.getArticle().getArticleId();
	        	// if articleId is already in the Map
	        	if (articleSumMap.containsKey(articleId))
	        	{
	        		// sum up the value
	        		Integer sum = articleSumMap.get(articleId);		// get current sum for the articleId
	        		sum = sum + oA.getNumberpu();					// add sum to current sum
	        		articleSumMap.put(articleId, sum);				// update sum in Map
	        	}
	        	else
	        	{
	        		articleSumMap.put(articleId, oA.getNumberpu());	// insert value into Map
	        	}
	        	
	        }
	        
	        
	        
	        // combine Articles by delivererId
	        Map<Integer, Map<Integer, Integer> > organisationArticleSumMap = new HashMap<Integer, Map<Integer,Integer>>();
	        
	        // iterate over all ArticleIds in the Set
	        for (Integer id : articleSumMap.keySet())
	        {
	        	Article article = dataBase.getArticleById(id);
	        	int organisationId = article.getDelivererId();
	        	
	        	// is organisationId is already in the map
	        	if (organisationArticleSumMap.containsKey(organisationId))
	        	{
	        		Map<Integer, Integer> existingMap = organisationArticleSumMap.get(organisationId);
	        		// insert new entry with the ArticleId and the number of PUs
	        		existingMap.put(id, articleSumMap.get(id));
	        	}
	        	else	// else, if organisation does not exist in the map
	        	{
	        		Map<Integer, Integer> newMap = new HashMap<Integer, Integer>();	// create new hashMap for the organisation
	        		newMap.put(id, articleSumMap.get(id));							// insert article mapping into new hashMap
	        		organisationArticleSumMap.put(organisationId, newMap);			// add new map for the organisation
	        	}
	        }
	        // this results in a map combining organisations with their delivered amount of articles
	        
	        
	        
	        // write the infos to the file
	        XWPFParagraph delivererParagraph = doc.createParagraph();
	        
	        for (Integer orgId : organisationArticleSumMap.keySet())
	        {
	        	Map<Integer, Integer> currentEntry = organisationArticleSumMap.get(orgId);
	        	
	        	// get organisation from DB
	        	Organisation organisation = dataBase.getOrganisationById(orgId);
	        	
	        	// get all ContactPersons for the Organisation
	        	// iterate over all ContactPersons
	        	String contactString = "";
	        	for (Person p : organisation.getContactPersons())
	        	{
	        		contactString += p.getLastName() + " " + p.getFirstName() + ", ";
	        	}
	        	// remove comma after appending all Names, if names have been added
	        	if (contactString.length() > 2)
	        		contactString = contactString.substring(0, contactString.length()-2);
	        	
	        	XWPFRun innerRun = delivererParagraph.createRun();
	 	        setArial12(innerRun);
	 	        innerRun.setText(organisation.getName() + " Ansprechpartner: " + contactString);
	 	        innerRun.addBreak();
	        
	 	        
	 	        // write Articles to paragraph
	 	        for(Integer articleId : currentEntry.keySet())
	 	        {
	 	        	Article article = dataBase.getArticleById(articleId);
	 	        	int numberPU = currentEntry.get(articleId);
	 	        	
			 	    innerRun.setText(String.valueOf(numberPU));
			 	    innerRun.addTab();
	 	        	innerRun.setText("Einheit: "+ article.getPackagingUnit() +" Beschreibung: "+article.getDescription());
		 	        innerRun.addBreak();
	 	        }
	 	        innerRun.addBreak();
	        
	        }
	        
	        
//	        Abholen [END]
	        
	        
	        

//			Lieferstationen
	        
	        XWPFParagraph p6 = doc.createParagraph();
	        XWPFRun r10 = p6.createRun();
	        r10.setText("Lieferstationen: ");
	        setBoldUnderlined(r10);
	        
	        XWPFParagraph p7 = doc.createParagraph();
	        XWPFRun r11 = p7.createRun();
	        setArial12(r11);
	        
	        
	        
	        // sort OutgoingDeliveries
	        // variable outgoingDeliveries is still valid (initialized and used above)
	        Collections.sort(outgoingDeliveries, new Comparator<OutgoingDelivery>() {
	        	@Override
	        	public int compare(OutgoingDelivery o1, OutgoingDelivery o2)
	        	{
	        		return (o1.getDeliveryNr() - o2.getDeliveryNr());
	        	}
			});
	        
	        
	        // counter for the deliveryStations
	        int i=0;
	        
	        for(OutgoingDelivery delivery : outgoingDeliveries)
	        {
	        	i++;
		        XWPFParagraph p = doc.createParagraph();
		        XWPFRun run = p.createRun();
		        run.setText(i +" " + delivery.getOrganisation().getName() + " Anmerkung: "+delivery.getComment());
		        setArial12(run);
		        run.setBold(true);
		        run.addBreak();
		        
		        // write each outgoingArticle to this paragraph
	        	for(OutgoingArticle outgoingArticle : delivery.getOutgoingArticles())
	        	{
			        XWPFRun innerRun = p.createRun();
			        setArial12(innerRun);
			        innerRun.setText(String.valueOf(outgoingArticle.getNumberpu()));
			        innerRun.addTab();
			        innerRun.setText(" Einheit: "+outgoingArticle.getArticle().getPackagingUnit() + " Beschreibung: " + outgoingArticle.getArticle().getDescription());
			        innerRun.addBreak();
	        	}
	        }
	        
	        XWPFParagraph end = doc.createParagraph();
	        XWPFRun endRun = end.createRun();
	        setArial12(endRun);
	        endRun.setBold(true);
	        endRun.setText("Gute Fahrt!");	        
	        
	        
	        File file = new File("generated.docx");
	        FileOutputStream out = new FileOutputStream(file);
	        doc.write(out);
	        out.close();
	        return file;
	}

	private static void setBoldUnderlined(XWPFRun r4) {
		setArial12(r4);
		r4.setBold(true);
		r4.setUnderline(UnderlinePatterns.SINGLE);
	}

	private static void setArial12(XWPFRun r4) {
		r4.setFontFamily("Arial");
		r4.setFontSize(12);
	}
}
