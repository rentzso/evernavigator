package com.classes.users;

import java.sql.Date;

public class User {
	private int userid=-1;
	private String username;
	private String hashkey;
	private String salts;
	private String accesstoken;
	private Date expirationdate;
	private String userauthority="ROLE_USER";
	private String email;
	
	public int getUserid() {
		return userid;
	}
	public void setUserid(int userID) {
		this.userid = userID;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String userName) {
		this.username = userName;
	}
	public String getHashkey() {
		return hashkey;
	}
	public void setHashkey(String hashkey) {
		this.hashkey = hashkey;
	}
	public String getSalts() {
		return salts;
	}
	public void setSalts(String salts) {
		this.salts = salts;
	}
	public String getAccesstoken() {
		return accesstoken;
	}
	public void setAccesstoken(String accessToken) {
		this.accesstoken = accessToken;
	}
	public Date getExpirationdate() {
		return expirationdate;
	}
	public void setExpirationdate(Date expirationDate) {
		this.expirationdate = expirationDate;
	}
	public String getUserauthority() {
		return userauthority;
	}
	public void setUserauthority(String userAuthority) {
		this.userauthority = userAuthority;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

}
