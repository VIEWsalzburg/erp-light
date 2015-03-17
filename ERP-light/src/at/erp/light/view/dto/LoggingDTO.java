package at.erp.light.view.dto;

/**
 * This class is a DTO representation of the entity Logging.<br/>
 * It is mainly used to transmit data between the frontend and backend.
 * @author Matthias Schnöll
 *
 */
public class LoggingDTO {

	String loggingText;
	String timestamp;
	String personName;
	
	/**
	 * Constructor
	 */
	public LoggingDTO() {
		
	}

	/**
	 * Constructor
	 * @param loggingText Log message
	 * @param timestamp Timestamp of the log message
	 * @param personName Name of the person, which carried out the action mentioned in the log message
	 */
	public LoggingDTO(String loggingText, String timestamp, String personName) {
		
		this.loggingText = loggingText;
		this.timestamp = timestamp;
		this.personName = personName;
	}

	/**
	 * get the log message
	 * @return log message
	 */
	public String getLoggingText() {
		return loggingText;
	}

	/**
	 * set the log message
	 * @param loggingText log message
	 */
	public void setLoggingText(String loggingText) {
		this.loggingText = loggingText;
	}

	/**
	 * get the timestamp of the log message
	 * @return timestamp
	 */
	public String getTimestamp() {
		return timestamp;
	}

	/**
	 * set the timestamp for the log message
	 * @param timeStamp Timestamp
	 */
	public void setTimestamp(String timeStamp) {
		this.timestamp = timeStamp;
	}

	/**
	 * get the name of the person, which carried out the action mentioned in the log message
	 * @return name of the person
	 */
	public String getPersonName() {
		return personName;
	}

	/**
	 * set the name of the person
	 * @param personName name of the person
	 */
	public void setPersonName(String personName) {
		this.personName = personName;
	}
	
}
