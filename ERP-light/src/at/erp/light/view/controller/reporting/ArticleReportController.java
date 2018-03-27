package at.erp.light.view.controller.reporting;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.supercsv.cellprocessor.FmtNumber;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import at.erp.light.view.dto.InOutArticleExtendedPUDTO;
import at.erp.light.view.dto.ReportDataDTO;
import at.erp.light.view.model.IncomingArticle;
import at.erp.light.view.model.IncomingDelivery;
import at.erp.light.view.model.Organisation;
import at.erp.light.view.services.IDataBase;

/**
 * This class is a RestController.<br/>
 * It manages the generation of reports concerning article management.
 * @author Matthias Schnöll
 *
 */
@RestController
public class ArticleReportController {
	private static final Logger log = Logger.getLogger(ArticleReportController.class
			.getName());

	@Autowired
	private IDataBase dataBaseService;

	DecimalFormat csvDecimalFormat = (DecimalFormat)NumberFormat.getNumberInstance(new Locale("de", "AT"));

	/**
	 * Requests a report for a single element which can then be displayed in the frontend.
	 * @param reportCommand tells which report should be carried out
	 * @return a signle report data object
	 * @throws IOException
	 */
	@RequestMapping(value = "secure/reports/articles/getSingleData")
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
	
	
	/**
	 * Generates a list with multiple elements
	 * @param reportCommand tells which report should be carried out
	 * @return a list with multiple report data elements
	 * @throws IOException
	 * @throws ParseException
	 */
	@RequestMapping(value = "secure/reports/articles/getListData")
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
	
	
	/**
	 * Generates a csv report file with multiple report data objects
	 * @param request contains the given report parameters
	 * @param response is used to write the csv file to the frontend
	 * @throws IOException
	 * @throws ParseException
	 */
	@RequestMapping(value = "secure/reports/articles/generateCSVReport")
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
		
		String reportTitle = "Report";
		
		if(reportCommand.isIncomingReportByOrganisationId())
		{
			reportTitle = "Report: Eingehende Waren für Organisation";
		}
		else if(reportCommand.isOutgoingReportByOrganisationId())
		{
			reportTitle = "Report: Ausgehende Waren für Organisation";
		}
		else if(reportCommand.isTotalSumOfAllIncomingDeliveries())
		{
			reportTitle = "Report: Summe über alle eingehenden Waren";
		}
		else if(reportCommand.isTotalSumOfAllOutgoingDeliveries())
		{
			reportTitle = "Report: Summe über alle ausgehenden Waren";
		}
		else if(reportCommand.isIncomingReportForAllOrganisations())
		{
			reportTitle = "Report: Eingehende Waren für alle Organisationen";
		}
		else if(reportCommand.isOutgoingReportForAllOrganisations())
		{
			reportTitle = "Report: Ausgehende Waren für alle Organisationen";
		}
		
		csvWriter.writeHeader(reportTitle);
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
				writeSingleData(csvWriter, reportDataDTO);
			}catch(Exception e){
				log.severe(e.getMessage());
			}
			
		}
		
		else if(reportCommand.isOutgoingReportByOrganisationId())
		{
			csvWriter.writeHeader("Für Kunde");
			try{
				reportDataDTO =  dataBaseService.getOutgoingReportByOrganisationId(id, dateFrom, dateTo);
				writeSingleData(csvWriter, reportDataDTO);
			}catch(Exception e){
				log.severe(e.getMessage());
			}
			
		}
		
		else if(reportCommand.isTotalSumOfAllIncomingDeliveries())
		{
			csvWriter.writeHeader("Summe aller eingehenden Lieferungen");
			try{
				reportDataDTO =  dataBaseService.getTotalSumOfAllIncomingDeliveries(dateFrom, dateTo);
				writeSingleData(csvWriter, reportDataDTO);
			}catch(Exception e){
				log.severe(e.getMessage());
			}
			
		}
		
		else if(reportCommand.isTotalSumOfAllOutgoingDeliveries())
		{
			csvWriter.writeHeader("Summe aller ausgehenden Lieferungen");
			try{
				reportDataDTO =  dataBaseService.getTotalSumOfAllOutgoingDeliveries(dateFrom, dateTo);
				writeSingleData(csvWriter, reportDataDTO);
			} catch(Exception e){
				log.severe(e.getMessage());
			}
			
		}
		
		List<ReportDataDTO> reportDataDTOList = null;

		if(reportCommand.isIncomingReportForAllOrganisations())
		{
			csvWriter.writeHeader("Alle Lieferanten");
			try{
				reportDataDTOList =  dataBaseService.getIncomingReportForAllOrganisations(dateFrom, dateTo);
				writeListData(csvWriter, reportDataDTOList);
			}catch(Exception e){
				log.severe(e.getMessage());
			}
			
		}

		else if(reportCommand.isOutgoingReportForAllOrganisations())
		{
			csvWriter.writeHeader("Alle Kunden");
			try{
				reportDataDTOList =  dataBaseService.getOutgoingReportForAllOrganisations(dateFrom, dateTo);
				writeListData(csvWriter, reportDataDTOList);
			}catch(Exception e){
				log.severe(e.getMessage());
			}
			
		}
		
		csvWriter.close();
	}


	/**
	 * Writes a single report data object to the given CsvBeanWriter
	 * @param csvWriter where the data should be written to
	 * @param reportDataDTO data which should be written
	 * @throws IOException
	 */
	private void writeSingleData(CsvBeanWriter csvWriter,
			ReportDataDTO reportDataDTO) throws IOException {
		String[] reportHeader = getReportHeader(reportDataDTO);

		String[] objectHeader = getReportObjectHeader(reportDataDTO);

		csvWriter.writeHeader(reportHeader);

		List<CellProcessor> cellProcessorsList = new ArrayList<CellProcessor>();
		// use cell Processor to format number
		for (String header : objectHeader)
		{
			if (header.equals("totalPrice")) {
				cellProcessorsList.add(new FmtNumber(csvDecimalFormat));
			} else if (header.equals("totalWeight")) {
				cellProcessorsList.add(new FmtNumber(csvDecimalFormat));
			} else {
				cellProcessorsList.add(null);
			}
		}
		
		CellProcessor[] cellProcessors = new CellProcessor[cellProcessorsList.size()];
		cellProcessors = cellProcessorsList.toArray(cellProcessors);
		
		csvWriter.write(reportDataDTO, objectHeader, cellProcessors);
		csvWriter.writeHeader("");
	}


	/**
	 * Write multiple Report data objects to the given CsvBeanWriter
	 * @param csvWriter where the list of data should be written to
	 * @param reportDataDTOList which should be written
	 * @throws IOException
	 */
	private void writeListData(CsvBeanWriter csvWriter,
			List<ReportDataDTO> reportDataDTOList) throws IOException {
		
		// if no data is available
		if (reportDataDTOList.size() == 0)
		{
			csvWriter.writeHeader("Keine Daten für diesen Zeitraum verfügbar.");
			return;
		}
		
		String[] reportHeader = getReportHeader(reportDataDTOList.get(0));
		
		String[] objectHeader = getReportObjectHeader(reportDataDTOList.get(0));
		
		csvWriter.writeHeader(reportHeader);
		
		List<CellProcessor> cellProcessorsList = new ArrayList<CellProcessor>();
		// use cell Processor to format number
		for (String header : objectHeader)
		{
			if (header.equals("totalPrice")) {
				cellProcessorsList.add(new FmtNumber(csvDecimalFormat));
			} else if (header.equals("totalWeight")) {
				cellProcessorsList.add(new FmtNumber(csvDecimalFormat));
			} else {
				cellProcessorsList.add(null);
			}
		}
		
		CellProcessor[] cellProcessors = new CellProcessor[cellProcessorsList.size()];
		cellProcessors = cellProcessorsList.toArray(cellProcessors);
		
		for(ReportDataDTO reportDataDTO2 : reportDataDTOList)
		{
			csvWriter.write(reportDataDTO2, objectHeader, cellProcessors);
		}
		csvWriter.writeHeader("");
	}


	/**
	 * Extracts the headers for the given reportDataDTO
	 * @param reportDataDTO
	 * @return String array including the headers
	 */
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
	
	
	/**
	 * Extracts the names of the object fields as string array
	 * @param reportDataDTO
	 * @return string array with the names of the object fields
	 */
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
	
	
	
	/***** ArticleDistributionReport *****/
	
	/**
	 * Generates a distribution report for the given delivery Id<br/>
	 * The csv file is directly written two the response stream.
	 * @param id Id of the requested delivery list
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ParseException
	 */
	@RequestMapping(value = "secure/reports/articles/generateDistributionReportByIncomingDeliveryId/{id}")
	public void generateDistributionReport(@PathVariable int id,
			HttpServletRequest request,	HttpServletResponse response) throws IOException, ParseException {

		// get incomingDelivery with the given id
		try {
			
			IncomingDelivery incomingDelivery = dataBaseService.getIncomingDeliveryById(id);
			
			log.info("Generate DistributionReport for IncomingDelivery "+id);
			
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");

			String csvFileName = "WarenVerteilungs-Report-ID" + incomingDelivery.getIncomingDeliveryId() + "_" 
									+ simpleDateFormat.format(incomingDelivery.getDate()) + ".csv";
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
			
			// start writing the content
			csvWriter.writeHeader("Warenverteilungsreport");
			csvWriter.writeHeader("Lieferungs-ID:","" + incomingDelivery.getIncomingDeliveryId());
			csvWriter.writeHeader("Datum:", simpleDateFormat.format(incomingDelivery.getDate()));
			csvWriter.writeHeader("Beschreibung:",incomingDelivery.getComment());
			csvWriter.writeHeader("");
			csvWriter.writeHeader("Erstelldatum des Reports:",simpleDateFormat.format(new Date()));
			csvWriter.writeHeader("");
			
			// get all Article IDs for this incomingDelivery
			List<Integer> articleIds = new ArrayList<Integer>();
			for (IncomingArticle iA : incomingDelivery.getIncomingArticles())
			{
				articleIds.add(iA.getArticle().getArticleId());
			}
			
			// get DistributionDTO Objects for all articleIds
			List<InOutArticleExtendedPUDTO> distributionList = new ArrayList<InOutArticleExtendedPUDTO>();
			
			for (Integer articleId : articleIds)
			{
				List<InOutArticleExtendedPUDTO> tempList = 
						dataBaseService.getArticleDistributionExtendedByArticleId(articleId);
				distributionList.addAll(tempList);	
				
			}
			
			
			// group by incoming and outgoing
			List<InOutArticleExtendedPUDTO> incomingDistribution = new ArrayList<InOutArticleExtendedPUDTO>();
			List<InOutArticleExtendedPUDTO> outgoingDistribution = new ArrayList<InOutArticleExtendedPUDTO>();
			
			for (InOutArticleExtendedPUDTO inOutArticle : distributionList)
			{
				switch (inOutArticle.getType())
				{
				case 0:	// incoming
					incomingDistribution.add(inOutArticle);
					break;
				case 1:	// outgoing
					outgoingDistribution.add(inOutArticle);					
					break;
				case 2:	// depot
					outgoingDistribution.add(inOutArticle);
					break;
				}
			}
			
			// group by organisation
			// maps for associating a organisation Id with a list
			HashMap<Integer, List<InOutArticleExtendedPUDTO>> incomingOrganisationDistributionMap = 
					new HashMap<Integer, List<InOutArticleExtendedPUDTO>>();
			HashMap<Integer, List<InOutArticleExtendedPUDTO>> outgoingOrganisationDistributionMap = 
					new HashMap<Integer, List<InOutArticleExtendedPUDTO>>();
			
			// group incomingArticles by orgId
			for (InOutArticleExtendedPUDTO elem : incomingDistribution)
			{
				int orgId = elem.getOrganisationId();
				// if orgId is already in incomingMap
				if (incomingOrganisationDistributionMap.containsKey(orgId))
				{
					incomingOrganisationDistributionMap.get(orgId).add(elem);
				}
				else	// if orgId is not already in incomingMap
				{
					List<InOutArticleExtendedPUDTO> tempList = new ArrayList<InOutArticleExtendedPUDTO>();
					tempList.add(elem);
					incomingOrganisationDistributionMap.put(orgId, tempList);
				}
			}
			
			// group outgoingArticles by orgId
			for (InOutArticleExtendedPUDTO elem : outgoingDistribution)
			{
				int orgId = elem.getOrganisationId();
				// if orgId is already in outgoingMap
				if (outgoingOrganisationDistributionMap.containsKey(orgId))
				{
					outgoingOrganisationDistributionMap.get(orgId).add(elem);
				}
				else	// if orgId is not already in outgoingMap
				{
					List<InOutArticleExtendedPUDTO> tempList = new ArrayList<InOutArticleExtendedPUDTO>();
					tempList.add(elem);
					outgoingOrganisationDistributionMap.put(orgId, tempList);
				}
			}
			
			
			// write incoming to CSV
			csvWriter.writeHeader("Wareneingang:");
			csvWriter.writeHeader("");
			
			for (Entry<Integer, List<InOutArticleExtendedPUDTO>> entry : incomingOrganisationDistributionMap.entrySet())
			{
				int orgId = entry.getKey();
				List<InOutArticleExtendedPUDTO> list = entry.getValue();
				
				// get Organisation by Key
				Organisation org = dataBaseService.getOrganisationById(orgId);
				
				csvWriter.writeHeader("Organisation:", org.getName());				
				
				// write Objects
				String[] header = {"Artikel", "Anzahl VE", "VE", "Gewicht", "Preis"};
				csvWriter.writeHeader(header);
				
				for (InOutArticleExtendedPUDTO article : list)
				{
					csvWriter.writeHeader(article.getArticleDTO().getDescription(),
							""+article.getNumberPUs(), article.getArticleDTO().getPackagingUnit(),
							""+article.getArticleDTO().getWeightpu(), ""+article.getArticleDTO().getPricepu());
				}
				
				csvWriter.writeHeader("");				
			}			
			
			// write outgoing to CSV
			csvWriter.writeHeader("");
			csvWriter.writeHeader("");
			csvWriter.writeHeader("Warenausgang:");
			csvWriter.writeHeader("");
			
			for (Entry<Integer, List<InOutArticleExtendedPUDTO>> entry : outgoingOrganisationDistributionMap.entrySet())
			{
				int orgId = entry.getKey();
				List<InOutArticleExtendedPUDTO> list = entry.getValue();
				
				// get Organisation by Key
				String organisationName = "";
				if (orgId > 0)
				{
					Organisation org = dataBaseService.getOrganisationById(orgId);
					organisationName = org.getName();
					csvWriter.writeHeader("Organisation:", organisationName);
					csvWriter.writeHeader("Warenausgansdatum:", list.get(0).getOutgoingDate());
				}
				else if (orgId == -1)
				{
					csvWriter.writeHeader("im Depot");
				}
				else
				{
					csvWriter.writeHeader("keine Organisation gefunden für Id "+orgId);
				}
				
				
				// write Objects
				String[] header = {"Artikel", "Anzahl VE", "VE", "Gewicht", "Preis"};
				csvWriter.writeHeader(header);
				
				for (InOutArticleExtendedPUDTO article : list)
				{
					csvWriter.writeHeader(article.getArticleDTO().getDescription(),
							""+article.getNumberPUs(), article.getArticleDTO().getPackagingUnit(),
							""+article.getArticleDTO().getWeightpu(), ""+article.getArticleDTO().getPricepu());
				}				
				csvWriter.writeHeader("");				
			}
		
			csvWriter.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
}
