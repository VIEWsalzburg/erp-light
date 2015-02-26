package at.erp.light.view.controller.adressmanagement;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import at.erp.light.view.dto.PersonAddressReportDataDTO;
import at.erp.light.view.dto.PersonEmailReportDataDTO;
import at.erp.light.view.services.IDataBase;

@RestController
public class AddressReportController {
	private static final Logger log = Logger.getLogger(AddressReportController.class
			.getName());

	@Autowired
	private IDataBase dataBaseService;
	
	
	
	@RequestMapping(value = "secure/reports/address/generateAddressReport")
	public void generateAddressReport(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ParseException {

		
		log.info("Generate Address Report for all Persons and Organisations");
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");

		String csvFileName = "AddressReport_" + simpleDateFormat.format(new Date())
				+ ".csv";
		response.setContentType("text/csv");
		// creates mock data
		String headerKey = "Content-Disposition";
		String headerValue = String.format("attachment; filename=\"%s\"",
				csvFileName);
		response.setHeader(headerKey, headerValue);
		
		NumberFormat nf_out = NumberFormat.getNumberInstance(Locale.GERMANY);
		nf_out.setMaximumFractionDigits(2);

		CsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(),
				CsvPreference.EXCEL_NORTH_EUROPE_PREFERENCE);

		csvWriter.writeHeader("Address-Report");
		csvWriter.writeHeader("");
		
		csvWriter.writeHeader("Erstelldatum: ",simpleDateFormat.format(new Date()));
		
		csvWriter.writeHeader("");

		
		List<PersonAddressReportDataDTO> reportDataList = null;

		
		csvWriter.writeHeader("Alle Personen und Organisationen");
		try{
			reportDataList =  dataBaseService.getPersonAddressReport();
			writeListData(csvWriter, reportDataList);
		}catch(Exception e){
			log.severe(e.getMessage());
			e.printStackTrace();
		}
		
		csvWriter.close();
	}

	private void writeListData(CsvBeanWriter csvWriter,
			List<PersonAddressReportDataDTO> reportDataDTOList) throws IOException {
		
		String[] reportHeader = {"Titel","Anrede","Nachname","Vorname","Straße (privat)",
				"PLZ (privat)", "Stadt (privat)", "Land (privat)", "Kontaktperson für", "Organisationstyp",
				"Straße (gesch.)", "PLZ (gesch.)", "Stadt (gesch.)", "Land (gesch.)"};
				
		csvWriter.writeHeader(reportHeader);
		
		String[] objectHeader = {"salutation", "title", "lastName", "firstName", "privateAddress",
				"privateZip", "privateCity", "privateCountry", "orgName", "orgType", "orgAddress", "orgZip", "orgCity", "orgCountry"};
		
		
		for(PersonAddressReportDataDTO dataDTO : reportDataDTOList)
		{
			csvWriter.write(dataDTO, objectHeader);
		}
		csvWriter.writeHeader("");
	}

	
	
	@RequestMapping(value = "secure/reports/address/generateEmailReport")
	public void generateEmailReport(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ParseException {

		
		log.info("Generate Email Report for all Persons");
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");

		String csvFileName = "EmailReport_" + simpleDateFormat.format(new Date())
				+ ".csv";
		response.setContentType("text/csv");
		// creates mock data
		String headerKey = "Content-Disposition";
		String headerValue = String.format("attachment; filename=\"%s\"",
				csvFileName);
		response.setHeader(headerKey, headerValue);
		
		NumberFormat nf_out = NumberFormat.getNumberInstance(Locale.GERMANY);
		nf_out.setMaximumFractionDigits(2);

		CsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(),
				CsvPreference.EXCEL_NORTH_EUROPE_PREFERENCE);

		csvWriter.writeHeader("Email-Report");
		csvWriter.writeHeader("");
		
		csvWriter.writeHeader("Erstelldatum: ",simpleDateFormat.format(new Date()));
		
		csvWriter.writeHeader("");

		
		List<PersonEmailReportDataDTO> reportDataList = null;

		
		csvWriter.writeHeader("Alle Personen");
		try{
			reportDataList =  dataBaseService.getPersonEmailReport();
			
			// write into file
			String[] reportHeader = {"Id", "Titel", "Anrede", "Nachname", "Vorname", "Kommentar", "Email",
					"Email-Typ", "Kontaktperson für"};
					
			csvWriter.writeHeader(reportHeader);
			
			String[] objectHeader = {"personId", "salutation", "title", "lastName", "firstName", "comment", "email",
					"emailType", "organisationName"};
			
			
			for(PersonEmailReportDataDTO dataDTO : reportDataList)
			{
				csvWriter.write(dataDTO, objectHeader);
			}
			csvWriter.writeHeader("");
		}catch(Exception e){
			log.severe(e.getMessage());
			e.printStackTrace();
		}
		
		csvWriter.close();
	}

	
}
