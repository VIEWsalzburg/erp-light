package at.erp.light.view.generation;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.UnderlinePatterns;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import at.erp.light.view.model.Article;
import at.erp.light.view.model.DeliveryList;
import at.erp.light.view.model.Organisation;
import at.erp.light.view.model.OutgoingArticle;
import at.erp.light.view.model.OutgoingDelivery;
import at.erp.light.view.model.Person;
import at.erp.light.view.services.IDataBase;

public class DeliveryWordGenerator {
	
	/**
	 * Generates a word file for the given deliveryList.<br/>
	 * @param deliveryList Delivery list which should be exported
	 * @param doneBy name of the lastEditor of the deliveryList
	 * @param dataBase dataBaseService class for getting data from the DB
	 * @return File which contains the generated word file
	 * @throws Exception
	 */
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
	        
	        // insert image on the right side
	        XWPFParagraph p1_1 = doc.createParagraph();
	        p1_1.setAlignment(ParagraphAlignment.RIGHT);	        
	        XWPFRun r1_1 = p1_1.createRun();
	        r1_1.addPicture(DeliveryWordGenerator.class.getResourceAsStream("VIEW_Logo_4c.png"), XWPFDocument.PICTURE_TYPE_PNG, "VIEW_Logo_4c.png", Units.toEMU(90), Units.toEMU(55)); // 200x200 pixels	        
//	        Disponert von
	        
	        XWPFParagraph p2 = doc.createParagraph();
	        XWPFRun r4 = p2.createRun();
	        r4.addBreak();
	        r4.setText("Disponiert von:");
	        setBoldUnderlined(r4);
	        r4.setFontSize(14);
	        
	        XWPFRun r6 = p2.createRun();
	        r6.setText(" "+doneBy);
	        setArial12(r6);
	        r6.setFontSize(12);
	        
	        
//	        Fahrerteam
	        
	        XWPFParagraph p3 = doc.createParagraph();
	        XWPFRun r5 = p3.createRun();
	        r5.addBreak();
	        r5.setText("Fahrteam:");
	        setBoldUnderlined(r5);
	        r5.setFontSize(14);

	        XWPFRun r7 = p3.createRun();
	        r7.setText(" "+deliveryList.getDriver() + ", " +deliveryList.getPassenger());
	        setArial12(r7);
	        r7.setFontSize(12);
	        
//	        Beschreibung
	        
	        XWPFParagraph commentParagraph = doc.createParagraph();
	        XWPFRun descriptionRun = commentParagraph.createRun();
	        descriptionRun.addBreak();
	        descriptionRun.setText("Notiz / Kontakt:");
	        setBoldUnderlined(descriptionRun);
	        descriptionRun.setFontSize(14);

	        XWPFRun descriptionRun2 = commentParagraph.createRun();
	        descriptionRun2.setText(" "+deliveryList.getComment());
	        setArial12(descriptionRun2);
	        descriptionRun2.setFontSize(12);
	        
//	        Abholen [START]
	        
	        XWPFParagraph p4 = doc.createParagraph();
	        XWPFRun r8 = p4.createRun();
	        r8.addBreak();
	        r8.setText("Abholen:");
	        setBoldUnderlined(r8);
	        r8.setFontSize(16);
	        
	        
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
//	        	String contactString = "";
//	        	for (Person p : organisation.getContactPersons())
//	        	{
//	        		contactString += p.getLastName() + " " + p.getFirstName() + ", ";
//	        	}
//	        	// remove comma after appending all Names, if names have been added
//	        	if (contactString.length() > 2)
//	        		contactString = contactString.substring(0, contactString.length()-2);
	        	
	        	XWPFRun orgRun = delivererParagraph.createRun();
	 	        setArial12(orgRun);
	 	        orgRun.setBold(true);
	 	        orgRun.addBreak();
	 	        
	 	        // print address
		        StringBuilder address = new StringBuilder();
		        
		        if (organisation.getAddress() != null)
		        {
		        	address.append(organisation.getAddress().getAddress());
		        }
		        
		        if (organisation.getCity() != null)
		        {
		        	if (address.length() > 0)
		        		address.append(", ");
		        	address.append(organisation.getCity().getCity());
		        }
		        
		        if (organisation.getCountry() != null)
		        {
		        	if (address.length() > 0)
		        		address.append(", ");
		        	address.append(organisation.getCountry().getCountry());
		        }
	 	        
	 	        orgRun.setText(organisation.getName() + ", " + address.toString());
	 	        
	 	        
	 	        
	 	        orgRun.addBreak();
	 	        
//	 	        XWPFRun contactRun = delivererParagraph.createRun();
//	 	        contactRun.setFontFamily("Arial");
//	 	        contactRun.setText("Ansprechperson(en): "+contactString);
//	 	        contactRun.addBreak();
	        
	 	        
	 	        XWPFRun articleRun = delivererParagraph.createRun();
	 	        articleRun.setFontFamily("Arial");
	 	        
	 	        // write Articles to paragraph
	 	        for(Integer articleId : currentEntry.keySet())
	 	        {
	 	        	Article article = dataBase.getArticleById(articleId);
	 	        	int numberPU = currentEntry.get(articleId);
	 	        	
	 	        	articleRun.setText(String.valueOf(numberPU));
	 	        	articleRun.addTab();
	 	        	articleRun.setText(article.getDescription() +" ("+ article.getPackagingUnit() +")");
	 	        	articleRun.addBreak();
	 	        }
	        
	        }
	        
//	        Abholen [END]
	        

//			Lieferstationen
	        XWPFParagraph p6 = doc.createParagraph();
	        XWPFRun r10 = p6.createRun();
	        r10.setText("Lieferstationen:");
	        setBoldUnderlined(r10);
	        r10.setFontSize(16);
	        r10.addBreak();
	        
	        
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
	        	Organisation org = delivery.getOrganisation();
	        	
	        	i++;
		        XWPFParagraph p = doc.createParagraph();
		        XWPFRun orgRun = p.createRun();
		        
		        // print Address
		        StringBuilder address = new StringBuilder();
		        
		        if (org.getAddress() != null)
		        {
		        	address.append(org.getAddress().getAddress());
		        }
		        
		        if (org.getCity() != null)
		        {
		        	if (address.length() > 0)
		        		address.append(", ");
		        	address.append(org.getCity().getCity());
		        }
		        
		        if (org.getCountry() != null)
		        {
		        	if (address.length() > 0)
		        		address.append(", ");
		        	address.append(org.getCountry().getCountry());
		        }
		        
		        orgRun.setText(i +". Lieferstation: " + org.getName() + ", " + address.toString());
		        
		        setArial12(orgRun);
		        orgRun.setBold(true);
		        orgRun.addBreak();
		        
		        // get contactPersons for current Organisation
//		        String contactString = "";
//	        	for (Person person : org.getContactPersons())
//	        	{
//	        		contactString += person.getLastName() + " " + person.getFirstName() + ", ";
//	        	}
//	        	// remove comma after appending all Names, if names have been added
//	        	if (contactString.length() > 2)
//	        		contactString = contactString.substring(0, contactString.length()-2);
		        
//		        XWPFRun contactPersonRun = p.createRun();
//		        contactPersonRun.setFontFamily("Arial");
//		        contactPersonRun.setText("Ansprechperson(en): "+contactString);
//		        contactPersonRun.addBreak();
		        
		        XWPFRun commentRun = p.createRun();
		        commentRun.setFontFamily("Arial");
		        commentRun.setText("Bemerkung: "+delivery.getComment());
		        commentRun.addBreak();
		        
		        
		        List<OutgoingArticle> articleList = new ArrayList<OutgoingArticle>();
		        articleList.addAll(delivery.getOutgoingArticles());
		        
		        // sort outgoingArticles by articleNr
		        Collections.sort(articleList, new Comparator<OutgoingArticle>() {
					@Override
					public int compare(OutgoingArticle o1, OutgoingArticle o2) {
						return o1.getArticleNr() - o2.getArticleNr();
					}
				});
		        
		        
		        // write each outgoingArticle to this paragraph
	        	for(OutgoingArticle outgoingArticle : articleList)
	        	{
			        XWPFRun innerRun = p.createRun();
			        setArial12(innerRun);
			        innerRun.setText(String.valueOf(outgoingArticle.getNumberpu()));
			        innerRun.addTab();
			        innerRun.setText(outgoingArticle.getArticle().getDescription() + " (" + outgoingArticle.getArticle().getPackagingUnit() + ")");
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

	/**
	 * set the given run bold and underlined
	 * @param r4 XWPFRun which should be formatted
	 */
	private static void setBoldUnderlined(XWPFRun r4) {
		setArial12(r4);
		r4.setBold(true);
		r4.setUnderline(UnderlinePatterns.SINGLE);
	}

	/**
	 * set the given run Arial12
	 * @param r4 XWPFRun which should be formatted
	 */
	private static void setArial12(XWPFRun r4) {
		r4.setFontFamily("Arial");
		r4.setFontSize(12);
	}
}
