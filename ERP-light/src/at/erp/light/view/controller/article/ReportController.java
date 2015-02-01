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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
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
	public ReportDataDTO getSingleData(@RequestBody ReportCommand reportCommand) throws IOException 
	{
		
		try {
			log.info("Returning report data");
		
			Integer id = reportCommand.getOrganisationId();
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
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
			
		// return Element with 0 values if no Data is found
		return new ReportDataDTO("","","",0,0,0);
	}
	
	
	@RequestMapping(value = "secure/reports/getListData")
	public List<ReportDataDTO> getListData(@RequestBody ReportCommand reportCommand) throws IOException, ParseException 
	{
			log.info("Returning report data");

			String dateFrom = reportCommand.getDateFrom();
			String dateTo = reportCommand.getDateTo();
		
			if(reportCommand.isIncomingReportForAllOrganisations())
			{
				try{
					return dataBaseService.getIncomingReportForAllOrganisations(dateFrom, dateTo);
				}
				catch(Exception e){
					log.severe(e.getMessage());
				}
			}

			if(reportCommand.isOutgoingReportForAllOrganisations())
			{
				try{
					return dataBaseService.getOutgoingReportForAllOrganisations(dateFrom, dateTo);
				}
				catch(Exception e){
					log.severe(e.getMessage());
				}
			}
			
			// return empty List
			return new ArrayList<ReportDataDTO>();
	}
	
	
	@RequestMapping(value = "secure/reports/generateCSVReport")
	public void downloadCSV(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ParseException {

		String organisationId = request.getParameter("organisationId");
		String dateFromParameter = request.getParameter("dateFrom");
		String dateToParameter = request.getParameter("dateTo");
		String incomingReportByOrganisationIdParameter = request.getParameter("incomingReportByOrganisationId");
		String incomingReportForAllOrganisationsParameter = request.getParameter("incomingReportForAllOrganisations");
		String outgoingReportByOrganisationIdParameter = request.getParameter("outgoingReportByOrganisationId");
		String outgoingReportForAllOrganisationsParameter = request.getParameter("outgoingReportForAllOrganisations");
		String totalSumOfAllIncomingDeliveriesParameter = request.getParameter("totalSumOfAllIncomingDeliveries");
		String totalSumOfAllOutgoingDeliveriesParameter = request.getParameter("totalSumOfAllOutgoingDeliveries");

		
		Assert.notNull(organisationId);
		Assert.notNull(dateFromParameter);
		Assert.notNull(dateToParameter);
		Assert.notNull(incomingReportByOrganisationIdParameter);
		Assert.notNull(incomingReportForAllOrganisationsParameter);
		Assert.notNull(outgoingReportByOrganisationIdParameter);
		Assert.notNull(outgoingReportForAllOrganisationsParameter);
		Assert.notNull(totalSumOfAllIncomingDeliveriesParameter);
		Assert.notNull(totalSumOfAllOutgoingDeliveriesParameter);

		
		ReportCommand reportCommand = new ReportCommand();
		
		
		reportCommand.setOrganisationId(Integer.valueOf(organisationId));
		reportCommand.setDateFrom(dateFromParameter);
		reportCommand.setDateTo(dateToParameter);
		reportCommand.setIncomingReportByOrganisationId(Boolean.parseBoolean(incomingReportByOrganisationIdParameter));
		reportCommand.setIncomingReportForAllOrganisations(Boolean.parseBoolean(incomingReportForAllOrganisationsParameter));
		reportCommand.setOutgoingReportByOrganisationId(Boolean.parseBoolean(outgoingReportByOrganisationIdParameter));
		reportCommand.setOutgoingReportForAllOrganisations(Boolean.parseBoolean(outgoingReportForAllOrganisationsParameter));
		reportCommand.setTotalSumOfAllIncomingDeliveries(Boolean.parseBoolean(totalSumOfAllIncomingDeliveriesParameter));
		reportCommand.setTotalSumOfAllOutgoingDeliveries(Boolean.parseBoolean(totalSumOfAllOutgoingDeliveriesParameter));
		
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
				dateFrom, " bis ",
				dateTo);

		Integer id = reportCommand.getOrganisationId();
		ReportDataDTO reportDataDTO = null;
		
		csvWriter.writeHeader("");

		if(reportCommand.isIncomingReportByOrganisationId())
		{
			csvWriter.writeHeader("Für Lieferant");
			try{
				reportDataDTO = dataBaseService.getIncomingReportByOrganisationId(id, dateFrom, dateTo);
			}catch(Exception e){
				log.severe(e.getMessage());
			}
			writeSingleData(csvWriter, reportDataDTO);
		}
		
		else if(reportCommand.isOutgoingReportByOrganisationId())
		{
			csvWriter.writeHeader("Für Kunde");
			try{
			reportDataDTO =  dataBaseService.getOutgoingReportByOrganisationId(id, dateFrom, dateTo);
			}catch(Exception e){
				log.severe(e.getMessage());
			}
			writeSingleData(csvWriter, reportDataDTO);
		}
		
		else if(reportCommand.isTotalSumOfAllIncomingDeliveries())
		{
			csvWriter.writeHeader("Summe aller eingehenden Lieferungen");
			try{
				reportDataDTO =  dataBaseService.getTotalSumOfAllIncomingDeliveries(dateFrom, dateTo);
			}catch(Exception e){
				log.severe(e.getMessage());
			}
			writeSingleData(csvWriter, reportDataDTO);
		}
		
		else if(reportCommand.isTotalSumOfAllOutgoingDeliveries())
		{
			csvWriter.writeHeader("Summe aller ausgehenden Lieferungen");
			try{
				reportDataDTO =  dataBaseService.getTotalSumOfAllOutgoingDeliveries(dateFrom, dateTo);
			} catch(Exception e){
				log.severe(e.getMessage());
			}
			writeSingleData(csvWriter, reportDataDTO);
		}
		
		List<ReportDataDTO> reportDataDTOList = null;

		if(reportCommand.isIncomingReportForAllOrganisations())
		{
			csvWriter.writeHeader("Alle Lieferanten");
			try{
				reportDataDTOList =  dataBaseService.getIncomingReportForAllOrganisations(dateFrom, dateTo);
			}catch(Exception e){
				log.severe(e.getMessage());
			}
			writeListData(csvWriter, reportDataDTOList);
		}

		else if(reportCommand.isOutgoingReportForAllOrganisations())
		{
			csvWriter.writeHeader("Alle Kunden");
			try{
				reportDataDTOList =  dataBaseService.getOutgoingReportForAllOrganisations(dateFrom, dateTo);
			}catch(Exception e){
				log.severe(e.getMessage());
			}
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
			header.add("Gesamtpreis[Euro]");
		}
		if(reportDataDTO.getTotalWeight()>0)
		{
			header.add("Gesamtgewicht[kg]");
		}
		
		String[] stockArr = new String[header.size()];
		stockArr = header.toArray(stockArr);
		
		return stockArr;
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
		String[] stockArr = new String[header.size()];
		stockArr = header.toArray(stockArr);
		return stockArr;
	}
}
