package at.erp.light.view.controller.adressmanagement;

/**
 * This class represents a password object, which is used to transfer 
 * password data from the frontend to the backend.
 * @author Matthias Schnöll
 *
 */
public class ChangePasswordObject {
	private String oldPassword;
	private String newPassword;
	
	public ChangePasswordObject(){}
	
	public ChangePasswordObject(String oldPassword, String newPassword) {
		super();
		this.oldPassword = oldPassword;
		this.newPassword = newPassword;
	}
	
	public String getOldPassword() {
		return oldPassword;
	}
	
	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}
	
	public String getNewPassword() {
		return newPassword;
	}
	
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

}
