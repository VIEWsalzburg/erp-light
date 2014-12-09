package at.erp.light.view.controller.adressmanagement;

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
