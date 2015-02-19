package at.erp.light.view.generation;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
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
import at.erp.light.view.model.Organisation;
import at.erp.light.view.model.Person;
import at.erp.light.view.services.IDataBase;

public class DeliveryWordGenerator {
	 public static File generate(DeliveryListDTO deliveryListDTO, String doneBy, IDataBase dataBase) throws Exception {
	        XWPFDocument doc = new XWPFDocument();

//	        Header
	        
	        XWPFParagraph p1 = doc.createParagraph();
	        XWPFRun r1 = p1.createRun();
	        r1.setBold(true);
	        r1.setText("Lieferliste für " + deliveryListDTO.getDate());
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
	        r7.setText(deliveryListDTO.getDriver() + ", " +deliveryListDTO.getPassenger());
	        setArial12(r7);
	        
//	        Abholen
	        
	        
	        Set<OutgoingDeliveryDTO> deliveryDTOs = deliveryListDTO.getOutgoingDeliveryDTOs();
	        
	        XWPFParagraph p4 = doc.createParagraph();
	        XWPFRun r8 = p4.createRun();
	        r8.setText("Abholen: ");
	        setBoldUnderlined(r8);
	        
//	        XWPFParagraph p5 = doc.createParagraph();
//	        XWPFRun r9 = p5.createRun();
	        // create hashmap with a List of outgoingArticles for every deliverer
	        Map<Integer, List<OutgoingArticleDTO>> allDeliverers = new HashMap<Integer, List<OutgoingArticleDTO>>();
	        for(OutgoingDeliveryDTO deliveryDTO:deliveryDTOs)
	        {
	        	for(OutgoingArticleDTO outgoingArticleDTO:deliveryDTO.getOutgoingArticleDTOs())
	        	{
	        		// get deliverer of current outgoingArticle
	        		Integer delivererId = outgoingArticleDTO.getArticleDTO().getDelivererId();
	        		// if deliverer has already a list
	        		if(allDeliverers.containsKey(delivererId))
	        		{
	        			// add outgoingArticle to existing list
	        			allDeliverers.get(delivererId).add(outgoingArticleDTO);
	        		}
	        		else
	        		{
	        			// add new outgoingArticle list for the deliverer
	        			List<OutgoingArticleDTO> newList = new ArrayList<OutgoingArticleDTO>();
	        			// add outgoingArticle to the new list
	        			newList.add(outgoingArticleDTO);
	        			// put list into hashmap
	        			allDeliverers.put(delivererId, newList);
	        		}
	        	}
	        }
	        
	        XWPFParagraph delivererParagraph = doc.createParagraph();

	        for(Entry<Integer, List<OutgoingArticleDTO>> entry :allDeliverers.entrySet())
	        {
	        	Organisation o = dataBase.getOrganisationById(entry.getKey());
	        	
	        	// get all ContactPersons for the Organisation
	        	// iterate over all ContactPersons
	        	String contactString = "";
	        	for (Person p : o.getContactPersons())
	        	{
	        		contactString += p.getLastName() + " " + p.getFirstName() + ", ";
	        	}
	        	// remove comma after appending all Names, if names have been added
	        	if (contactString.length() > 2)
	        		contactString = contactString.substring(0, contactString.length()-2);
	        	
	        	XWPFRun innerRun = delivererParagraph.createRun();
	 	        setArial12(innerRun);
	 	        innerRun.setText(o.getName() + " Ansprechpartner: " + contactString);
	 	        innerRun.addBreak();

	 	        for(OutgoingArticleDTO articleDTO : entry.getValue())
	 	        {
			 	    innerRun.setText(String.valueOf(articleDTO.getNumberpu()));
			 	    innerRun.addTab();
	 	        	innerRun.setText("Einheit: "+ articleDTO.getArticleDTO().getPackagingUnit() +" Beschreibung: "+articleDTO.getArticleDTO().getDescription());
		 	        innerRun.addBreak();
	 	        }
	 	        innerRun.addBreak();
	        }
	        
	        // Lieferstationen
	        
	        XWPFParagraph p6 = doc.createParagraph();
	        XWPFRun r10 = p6.createRun();
	        r10.setText("Lieferstationen: ");
	        setBoldUnderlined(r10);
	        
	        XWPFParagraph p7 = doc.createParagraph();
	        XWPFRun r11 = p7.createRun();
	        setArial12(r11);
	        int i=0;
	        for(OutgoingDeliveryDTO deliveryDTO:deliveryDTOs)
	        {
        		i++;
		        XWPFParagraph p = doc.createParagraph();
		        XWPFRun run = p.createRun();
		        run.setText(i +" " + dataBase.getOrganisationById(deliveryDTO.getOrganisationId()).getName() + " Anmerkung: "+deliveryDTO.getComment());
		        setArial12(run);
		        run.setBold(true);
		        run.addBreak();
		        
	        	for(OutgoingArticleDTO outgoingArticleDTO:deliveryDTO.getOutgoingArticleDTOs())
	        	{
			        XWPFRun innerRun = p.createRun();
			        setArial12(innerRun);
			        innerRun.setText(String.valueOf(outgoingArticleDTO.getNumberpu()));
			        innerRun.addTab();
			        innerRun.setText(" Einheit: "+outgoingArticleDTO.getArticleDTO().getPackagingUnit() + " Beschreibung: " + outgoingArticleDTO.getArticleDTO().getDescription());
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
