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
import at.erp.light.view.services.IDataBase;
import at.erp.light.view.state.ControllerMessage;

@RestController
public class ReportController {
	private static final Logger log = Logger.getLogger(DeliveryController.class
			.getName());

	@Autowired
	private IDataBase dataBaseService;

	


	/***** [END] Delivery list 
	 * @throws ParseException *****/
	@RequestMapping(value = "secure/reports/generateReport")
	public void downloadCSV(@RequestBody ReportCommand reportCommand,
			HttpServletResponse response) throws IOException, ParseException {

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

		
		CsvListWriter csvWriter = new CsvListWriter(response.getWriter(),
				CsvPreference.EXCEL_NORTH_EUROPE_PREFERENCE);

		List<IncomingDelivery> allIncomingDeliveries = dataBaseService
				.getAllIncomingDeliveries();
		List<OutgoingDelivery> allOutgoingDeliveries = dataBaseService
				.getAllOutgoingDeliveries();

		csvWriter.writeHeader("Zeitraum: ",
				simpleDateFormat.format(reportCommand.getFrom()), " bis ",
				simpleDateFormat.format(reportCommand.getTo()));

		if (reportCommand.isWeightPerDeliveringCompany()||reportCommand.isCompletePriceDeliveringCompany()||reportCommand.isCompleteWeightDeliveringCompany()) {

		csvWriter.writeHeader("Eingang");
		csvWriter.writeHeader("");
		}

		if (reportCommand.isWeightPerDeliveringCompany()) {

			String[] header = { "Lieferant", "Gewicht" };
			csvWriter.writeHeader(header);
			HashMap<Integer, Double> companyWeight = new HashMap<Integer, Double>();

			for (IncomingDelivery incomingDelivery : allIncomingDeliveries) {
				if (simpleDateFormat.parse(reportCommand.getFrom()).compareTo(
						incomingDelivery.getDate()) < 0
						&& simpleDateFormat.parse(reportCommand.getTo()).compareTo(
								incomingDelivery.getDate()) > 0) {
					double weigthPerDelivery = 0;
					IncomingDeliveryDTO incomingDeliveryDTO = IncomingDeliveryMapper
							.mapToDTO(incomingDelivery);
					for (IncomingArticleDTO incomingArticleDTO : incomingDeliveryDTO
							.getIncomingArticleDTOs()) {
						weigthPerDelivery += incomingArticleDTO.getArticleDTO()
								.getWeightpu()*incomingArticleDTO.getNumberpu();
					}

					Double existingValue = putIfAbsent(companyWeight,
							incomingDeliveryDTO.getOrganisationId(),
							weigthPerDelivery);
					if (existingValue != null) {
						companyWeight.put(
								incomingDeliveryDTO.getOrganisationId(),
								existingValue + weigthPerDelivery);
					}
				}

			}
			Set<Integer> organisationIds = companyWeight.keySet();
			for (int id : organisationIds) {
				csvWriter.write(new String[] {
						dataBaseService.getOrganisationById(id).getName(),
						nf_out.format(companyWeight.get(id)) });
			}
		}
		csvWriter.writeHeader("");

		// Gesamtgewicht

		if (reportCommand.isCompleteWeightDeliveringCompany()) {
			double weight = 0;

			for (IncomingDelivery incomingDelivery : allIncomingDeliveries) {
				if (simpleDateFormat.parse(reportCommand.getFrom()).compareTo(
						incomingDelivery.getDate()) < 0
						&& simpleDateFormat.parse(reportCommand.getTo()).compareTo(
								incomingDelivery.getDate()) > 0) {
					IncomingDeliveryDTO incomingDeliveryDTO = IncomingDeliveryMapper
							.mapToDTO(incomingDelivery);
					for (IncomingArticleDTO incomingArticleDTO : incomingDeliveryDTO
							.getIncomingArticleDTOs()) {
						weight += incomingArticleDTO.getArticleDTO()
								.getWeightpu()*incomingArticleDTO.getNumberpu();
					}
				}
			}
			csvWriter.writeHeader("Gesamtgewicht aller Firmen");
			csvWriter.write(new String[] { nf_out.format(weight), "kg" });
		}

		if (reportCommand.isCompletePriceDeliveringCompany()) {

			// Gesamtpreis
			double price = 0;

			for (IncomingDelivery incomingDelivery : allIncomingDeliveries) {
				if (simpleDateFormat.parse(reportCommand.getFrom()).compareTo(
						incomingDelivery.getDate()) < 0
						&& simpleDateFormat.parse(reportCommand.getTo()).compareTo(
								incomingDelivery.getDate()) > 0) {
					IncomingDeliveryDTO incomingDeliveryDTO = IncomingDeliveryMapper
							.mapToDTO(incomingDelivery);
					for (IncomingArticleDTO incomingArticleDTO : incomingDeliveryDTO
							.getIncomingArticleDTOs()) {
						price += incomingArticleDTO.getArticleDTO()
								.getPricepu()
								* incomingArticleDTO.getNumberpu();
					}
				}
			}
			csvWriter.writeHeader("");
			csvWriter.writeHeader("Gesamtpreis aller Firmen");
			csvWriter.write(new String[] { nf_out.format(price), "Euro" });
			csvWriter.writeHeader("");
		}
		
		if (reportCommand.isWeightPerReceivingCompany()||reportCommand.isPricePerReceivingCompany()) {

			csvWriter.writeHeader("Ausgang");
			csvWriter.writeHeader("");
		}
		
		// Gewicht je Kunde

		if (reportCommand.isWeightPerReceivingCompany()) {

			String[] header = { "Kunde", "Gewicht" };
			csvWriter.writeHeader(header);
			HashMap<Integer, Double> companyOutgoingWeight = new HashMap<Integer, Double>();

			for (OutgoingDelivery outgoingDelivery : allOutgoingDeliveries) {
				if (simpleDateFormat.parse(reportCommand.getFrom()).compareTo(
						outgoingDelivery.getDate()) < 0
						&& simpleDateFormat.parse(reportCommand.getTo()).compareTo(
								outgoingDelivery.getDate()) > 0) {
					
					double weightPerDelivery = 0;
					OutgoingDeliveryDTO outgoingDeliveryDTO = OutgoingDeliveryMapper
							.mapToDTO(outgoingDelivery);
					for (OutgoingArticleDTO outgoingArticleDTO : outgoingDeliveryDTO
							.getOutgoingArticleDTOs()) {
						weightPerDelivery += outgoingArticleDTO.getArticleDTO()
								.getWeightpu()*outgoingArticleDTO.getNumberpu();
					}

					Double existingValue = putIfAbsent(companyOutgoingWeight,
							outgoingDeliveryDTO.getOrganisationId(),
							weightPerDelivery);
					if (existingValue != null) {
						companyOutgoingWeight.put(
								outgoingDeliveryDTO.getOrganisationId(),
								existingValue + weightPerDelivery);
					}
				}
			}
			Set<Integer> organisationIds = companyOutgoingWeight.keySet();
			for (int id : organisationIds) {
				csvWriter.write(new String[] {
						dataBaseService.getOrganisationById(id).getName(),
						nf_out.format(companyOutgoingWeight.get(id)) });
			}
		}

		// Preis je Kunde

		if (reportCommand.isPricePerReceivingCompany()) {
			String[] header = { "Kunde", "Preis" };
			csvWriter.writeHeader(header);
			HashMap<Integer, Double> companyPrice = new HashMap<Integer, Double>();

			for (OutgoingDelivery outgoingDelivery : allOutgoingDeliveries) {
				if (simpleDateFormat.parse(reportCommand.getFrom()).compareTo(
						outgoingDelivery.getDate()) < 0
						&& simpleDateFormat.parse(reportCommand.getTo()).compareTo(
								outgoingDelivery.getDate()) > 0) {
					
					
					double pricePerDelivery = 0;
					OutgoingDeliveryDTO outgoingDeliveryDTO = OutgoingDeliveryMapper
							.mapToDTO(outgoingDelivery);
					for (OutgoingArticleDTO outgoingArticleDTO : outgoingDeliveryDTO
							.getOutgoingArticleDTOs()) {
						pricePerDelivery += outgoingArticleDTO.getArticleDTO()
								.getPricepu()*outgoingArticleDTO.getNumberpu();
					}

					Double existingValue = putIfAbsent(companyPrice,
							outgoingDeliveryDTO.getOrganisationId(),
							pricePerDelivery);
					if (existingValue != null) {
						companyPrice.put(
								outgoingDeliveryDTO.getOrganisationId(),
								existingValue + pricePerDelivery);
					}
				}
			}

			Set<Integer> organisationIds = companyPrice.keySet();
			for (int id : organisationIds) {
				csvWriter.write(new String[] {
						dataBaseService.getOrganisationById(id).getName(),
						nf_out.format(companyPrice.get(id)) });
			}
		}
		csvWriter.close();
	}
	
	private double putIfAbsent(HashMap<Integer,Double> hashMap,int key, double value)
	{
		Double v = hashMap.get(key);
		 if (v == null)
		     v = hashMap.put(key, value);

		 return v;
	}
	
}
