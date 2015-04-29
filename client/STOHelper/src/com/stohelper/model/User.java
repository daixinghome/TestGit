package com.stohelper.model;

import android.graphics.Bitmap;

public class User {
	private String userPhoneNum = "";
	private String userPassword = "";
	private String userName = "";
	private String userGender = "";
	private String userIDNum = "";
	private String userAddress = "";
	private int userType = 1;
	private String profile_img_name = "";
	private String profile_img_data = "";
	private Bitmap profile_img = null;
	
	public User(){}
	
	public User(String userPhoneNum,
				String userPassword,
				String userName,
				String userGender,
				String userIDNum,
				String userAddress){
		
		this.userPhoneNum = userPhoneNum;
		this.userPassword = userPassword;
		this.userName = userName;
		this.userGender = userGender;
		this.userIDNum = userIDNum;
		this.userAddress = userAddress;
	}

	public String getUserPhoneNum() {
		return userPhoneNum;
	}

	public void setUserPhoneNum(String userPhoneNum) {
		this.userPhoneNum = userPhoneNum;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserGender() {
		return userGender;
	}

	public void setUserGender(String userGender) {
		this.userGender = userGender;
	}

	public String getUserIDNum() {
		return userIDNum;
	}

	public void setUserIDNum(String userIDNum) {
		this.userIDNum = userIDNum;
	}

	public String getUserAddress() {
		return userAddress;
	}

	public void setUserAddress(String userAddress) {
		this.userAddress = userAddress;
	}

	public int getUserType() {
		return userType;
	}
	
	public void setUserType(int userType) {
		this.userType = userType;
	}
	
	public String getProfile_img_name() {
		return profile_img_name;
	}
	
	public void setProfile_img_name(String profile_img_name) {
		this.profile_img_name = profile_img_name;
	}
	
	public String getProfile_img_data() {
		return profile_img_data;
	}
	
	public void setProfile_img_data(String profile_img_data) {
		this.profile_img_data = profile_img_data;
	}
	
	public Bitmap getProfile_img() {
		return profile_img;
	}
	
	public void setProfile_img(Bitmap profile_img) {
		this.profile_img = profile_img;
	}
}
