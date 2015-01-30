package at.erp.light.view.controller.article;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import at.erp.light.view.dto.ReportDataDTO;
import at.erp.light.view.services.IDataBase;

@RestController
public class ReportController {
	private static final Logger log = Logger.getLogger(DeliveryController.class
			.getName());

	@Autowired
	private IDataBase dataBaseService;

	


	/***** [END] Delivery list 
	 * @throws ParseException *****/
	
	@RequestMapping(value = "secure/reports/getSingleData")
	public ReportDataDTO getSingleData(@RequestBody ReportCommand reportCommand) throws IOException, ParseException 
	{
			log.info("Returning report data");
		
			Integer id = reportCommand.getId();
			String dateFrom = reportCommand.getDateFrom();
			String dateTo = reportCommand.getDateTo();
		
			if(reportCommand.isIncomingReportByOrganisationId())
			{
				return dataBaseService.getIncomingReportByOrganisationId(id, dateFrom, dateTo);
			}
			
			if(reportCommand.isOutgoingReportByOrganisationId())
			{
				return dataBaseService.getOutgoingReportByOrganisationId(id, dateFrom, dateTo);
			}
			
			if(reportCommand.isTotalSumOfAllIncomingDeliveries())
			{
				return dataBaseService.getTotalSumOfAllIncomingDeliveries(dateFrom, dateTo);
			}
			
			if(reportCommand.isTotalSumOfAllOutgoingDeliveries())
			{
				return dataBaseService.getTotalSumOfAllOutgoingDeliveries(dateFrom, dateTo);
			}
			
			return null;
	}
	
	
	@RequestMapping(value = "secure/reports/getListData")
	public List<ReportDataDTO> getListData(@RequestBody ReportCommand reportCommand) throws IOException, ParseException 
	{
			log.info("Returning report data");

			String dateFrom = reportCommand.getDateFrom();
			String dateTo = reportCommand.getDateTo();
		
			if(reportCommand.isIncomingReportForAllOrganisations())
			{
				return dataBaseService.getIncomingReportForAllOrganisations(dateFrom, dateTo);
			}

			if(reportCommand.isOutgoingReportForAllOrganisation())
			{
				return dataBaseService.getOutgoingReportForAllOrganisations(dateFrom, dateTo);
			}
			
			return null;
	}
	
	
	@RequestMapping(value = "secure/reports/generateCSVReport")
	public void downloadCSV(@RequestBody ReportCommand reportCommand,
			HttpServletResponse response) throws IOException, ParseException {

		log.info("Generate CSV Report");
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");

		String csvFileName = "Report_" + simpleDateFormat.format(new Date())
				+ ".csv";
		response.setContentType("text/csv");
		// creates mock data
		String headerKey = "Content-Disposition";
		String headerValue = String.format("attachment; filename=\"%s\"",
				csvFileName);

		NumberFormat nf_out = NumberFormat.getNumberInstance(Locale.GERMANY);
		nf_out.setMaximumFractionDigits(2);

		response.setHeader(headerKey, headerValue);

		
		CsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(),
				CsvPreference.EXCEL_NORTH_EUROPE_PREFERENCE);

		csvWriter.writeHeader("Report");
		csvWriter.writeHeader("");
		
		csvWriter.writeHeader("Erstelldatum: ",simpleDateFormat.format(new Date()));
		
		String dateFrom = reportCommand.getDateFrom();
		String dateTo = reportCommand.getDateTo();
		
		csvWriter.writeHeader("Zeitraum: ",
				simpleDateFormat.format(dateFrom), " bis ",
				simpleDateFormat.format(dateTo));

		Integer id = reportCommand.getId();
		ReportDataDTO reportDataDTO = null;
		
		csvWriter.writeHeader("");

		if(reportCommand.isIncomingReportByOrganisationId())
		{
			csvWriter.writeHeader("F�r Lieferant");
			reportDataDTO = dataBaseService.getIncomingReportByOrganisationId(id, dateFrom, dateTo);
			writeSingleData(csvWriter, reportDataDTO);
		}
		
		else if(reportCommand.isOutgoingReportByOrganisationId())
		{
			csvWriter.writeHeader("F�r Kunde");
			reportDataDTO =  dataBaseService.getOutgoingReportByOrganisationId(id, dateFrom, dateTo);
			writeSingleData(csvWriter, reportDataDTO);
		}
		
		else if(reportCommand.isTotalSumOfAllIncomingDeliveries())
		{
			csvWriter.writeHeader("Summe aller eingehenden Lieferungen");
			reportDataDTO =  dataBaseService.getTotalSumOfAllIncomingDeliveries(dateFrom, dateTo);
			writeSingleData(csvWriter, reportDataDTO);
		}
		
		else if(reportCommand.isTotalSumOfAllOutgoingDeliveries())
		{
			csvWriter.writeHeader("Summe aller ausgehenden Lieferungen");
			reportDataDTO =  dataBaseService.getTotalSumOfAllOutgoingDeliveries(dateFrom, dateTo);
			writeSingleData(csvWriter, reportDataDTO);
		}
		
		List<ReportDataDTO> reportDataDTOList = null;

		if(reportCommand.isIncomingReportForAllOrganisations())
		{
			csvWriter.writeHeader("Alle Lieferanten");
			reportDataDTOList =  dataBaseService.getIncomingReportForAllOrganisations(dateFrom, dateTo);
			writeListData(csvWriter, reportDataDTOList);
		}

		else if(reportCommand.isOutgoingReportForAllOrganisation())
		{
			csvWriter.writeHeader("Alle Kunden");
			reportDataDTOList =  dataBaseService.getOutgoingReportForAllOrganisations(dateFrom, dateTo);
			writeListData(csvWriter, reportDataDTOList);
		}
		
		csvWriter.close();
	}


	private void writeSingleData(CsvBeanWriter csvWriter,
			ReportDataDTO reportDataDTO) throws IOException {
		String[] reportHeader = getReportHeader(reportDataDTO);

		String[] objectHeader = getReportObjectHeader(reportDataDTO);

		csvWriter.writeHeader(reportHeader);

		csvWriter.write(reportDataDTO, objectHeader);
		csvWriter.writeHeader("");
	}


	private void writeListData(CsvBeanWriter csvWriter,
			List<ReportDataDTO> reportDataDTOList) throws IOException {
		String[] reportHeader = getReportHeader(reportDataDTOList.get(0));
		
		String[] objectHeader = getReportObjectHeader(reportDataDTOList.get(0));
		
		csvWriter.writeHeader(reportHeader);
		
		for(ReportDataDTO reportDataDTO2 : reportDataDTOList)
		{
			csvWriter.write(reportDataDTO2, objectHeader);
		}
		csvWriter.writeHeader("");
	}


	private String[] getReportHeader(ReportDataDTO reportDataDTO) {
		List<String> header = new ArrayList<String>();
		
		if(reportDataDTO.getOrganisationId()>0)
		{
			header.add("Organisations-ID");
		}
		if(reportDataDTO.getOrganisationName()!=null)
		{
			header.add("Organisations-Name");
		}
		if(reportDataDTO.getTotalPrice()>0)
		{
			header.add("Gesamtpreis");
		}
		if(reportDataDTO.getTotalWeight()>0)
		{
			header.add("Gesamtgewicht");
		}
		
		return (String[]) header.toArray();
	}
	
	private String[] getReportObjectHeader(ReportDataDTO reportDataDTO) {
		List<String> header = new ArrayList<String>();
		
		if(reportDataDTO.getOrganisationId()>0)
		{
			header.add("organisationId");
		}
		if(reportDataDTO.getOrganisationName()!=null)
		{
			header.add("organisationName");
		}
		if(reportDataDTO.getTotalPrice()>0)
		{
			header.add("totalPrice");
		}
		if(reportDataDTO.getTotalWeight()>0)
		{
			header.add("totalWeight");
		}
		
		return (String[]) header.toArray();
	}
}
