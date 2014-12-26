package at.erp.light.view.state;

public class ControllerMessage {
	private boolean success;
	private String message;
	
	public ControllerMessage(){}
	
	public ControllerMessage(boolean state, String message) {
		super();
		this.setSuccess(state);
		this.setMessage(message);
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	
	
}
