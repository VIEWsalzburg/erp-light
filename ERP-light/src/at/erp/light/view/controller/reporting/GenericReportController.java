package at.erp.light.view.controller.reporting;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import at.erp.light.view.services.IDataBase;

/**
 * This class is a RestController.<br/>
 * It is used for executing custom Reports and return the data as csv-file.
 * @author Matthias Schnöll
 *
 */
@RestController
public class GenericReportController {

	@Autowired
	private IDataBase dataBaseService;
	
	private GenericReportController()
	{
		super();
	}
	
	
	/**
	 * This function returns the result set from the SQL Query as csv file
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "secure/reporting/runSQLQuery")
	public void exportCustomQuery(HttpServletRequest request,	HttpServletResponse response) {
		try {
			
			String sqlQuery = "Select * from person;";
			
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");

			String csvFileName = "Report_" + simpleDateFormat.format(new Date())
					+ ".csv";
			response.setContentType("text/csv");
			// creates mock data
			String headerKey = "Content-Disposition";
			String headerValue = String.format("attachment; filename=\"%s\"",
					csvFileName);
			response.setHeader(headerKey, headerValue);
			
			CsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(),
					CsvPreference.EXCEL_NORTH_EUROPE_PREFERENCE);

			csvWriter.writeHeader("Report");
			csvWriter.writeHeader("");
			
			csvWriter.writeHeader("Erstelldatum: ",simpleDateFormat.format(new Date()));
			csvWriter.writeHeader("");
			
			csvWriter.writeHeader("Query: "+sqlQuery);
			csvWriter.writeHeader("");
			
			csvWriter.writeHeader("Ergebnis");
			try{
				
				List<Object[]> list = dataBaseService.runSQLQuery(sqlQuery);
				
				for (Object[] row : list)
				{
					
					List<String> rowData = new ArrayList<String>();
					
					for (int i=0; i<row.length; i++)
					{
						String text = "";
						if (row[i] != null)
							text = row[i].toString();
						
						rowData.add(text);
					}
					
					String[] array = rowData.toArray(new String[rowData.size()]);
					csvWriter.writeHeader(array);
				}
				
				csvWriter.writeHeader("");
				
			}catch(Exception e){
				e.printStackTrace();
			}
			
			csvWriter.close();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
}
