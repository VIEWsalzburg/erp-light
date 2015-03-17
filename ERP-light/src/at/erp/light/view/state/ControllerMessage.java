package at.erp.light.view.state;

/**
 * This class is used for sending messages form the Controllers to the frontend.
 * @author Matthias Schnöll
 *
 */
public class ControllerMessage {
	private boolean success;
	private String message;
	
	/**
	 * Constructor
	 */
	public ControllerMessage(){}
	
	/**
	 * Constructor
	 * @param state Success of the performed action
	 * @param message Message
	 */
	public ControllerMessage(boolean state, String message) {
		super();
		this.setSuccess(state);
		this.setMessage(message);
	}

	/**
	 * get success of the performed action
	 * @return success
	 */
	public boolean isSuccess() {
		return success;
	}

	/**
	 * set success for the control message
	 * @param success success
	 */
	public void setSuccess(boolean success) {
		this.success = success;
	}

	/**
	 * get the message
	 * @return message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * set the message
	 * @param message message
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	
}
