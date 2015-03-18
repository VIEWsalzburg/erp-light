package at.erp.light.view.services;

/**
 * This exception is used for system errors.
 * @author Matthias Schnöll
 *
 */
public class ERPLightException extends Exception {

	/**
	 * Constructor
	 */
	public ERPLightException() {

	}
	
	/**
	 * Constructor
	 * @param message
	 */
	public ERPLightException(String message) {
		super(message);
	}
	
}
