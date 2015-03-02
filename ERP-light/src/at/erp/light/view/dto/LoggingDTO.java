package at.erp.light.view.dto;

public class LoggingDTO {

	String loggingText;
	String timestamp;
	String personName;
	
	public LoggingDTO() {
		
	}

	public LoggingDTO(String loggingText, String timestamp, String personName) {
		
		this.loggingText = loggingText;
		this.timestamp = timestamp;
		this.personName = personName;
	}

	public String getLoggingText() {
		return loggingText;
	}

	public void setLoggingText(String loggingText) {
		this.loggingText = loggingText;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timeStamp) {
		this.timestamp = timeStamp;
	}

	public String getPersonName() {
		return personName;
	}

	public void setPersonName(String personName) {
		this.personName = personName;
	}
	
}
