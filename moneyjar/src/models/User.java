package models;

public class User {

	private int userSn;
	private String userId;
	private String password;
	private String mail;

	public void setUserSn(int userSn) {
		this.userSn = userSn;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}

	public int getUserSn() {
		return userSn;
	}

	public String getUserId() {
		return userId;
	}

	public String getPassword() {
		return password;
	}
	public String getMail() {
		return mail;
	}

}
