package at.erp.light.view.authenticate;

/**
 * The class represents an authentication object for 
 * transferring authentication data from the frontend to the backend
 * @author Matthias Schnöll
 *
 */

public class Authentication {
	private int id;
	private String loginEmail;
	private String password;

	public Authentication() {
		super();
	}

	public Authentication(int id, String username, String password) {
		super();
		this.id = id;
		this.loginEmail = username;
		this.password = password;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLoginEmail() {
		return loginEmail;
	}

	public void setUsername(String loginEmail) {
		this.loginEmail = loginEmail;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
