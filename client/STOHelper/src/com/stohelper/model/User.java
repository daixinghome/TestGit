package com.stohelper.model;

import android.graphics.Bitmap;

//�û���
public class User { 
	
	private String userPhoneNum = ""; //�绰����
	private String userPassword = ""; //����
	private String userName = ""; //����
	private String userGender = ""; //�Ա�
	private String userIDNum = ""; //���֤����
	private String userAddress = ""; //��ַ
	private int userType = 1; //�û�����
	private String profile_img_name = ""; //ͷ��ͼƬ��
	private String profile_img_data = ""; //ͷ��ͼƬ����
	private Bitmap profile_img = null; //ͷ��ͼƬ
	private String idcard_img_name = ""; //���֤ͼƬ��
	private String idcard_img_data = ""; //���֤ͼƬ����
	private Bitmap idcard_img = null; //���֤ͼƬ
	
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

	public String getIdcard_img_name() {
		return idcard_img_name;
	}

	public void setIdcard_img_name(String idcard_img_name) {
		this.idcard_img_name = idcard_img_name;
	}

	public String getIdcard_img_data() {
		return idcard_img_data;
	}

	public void setIdcard_img_data(String idcard_img_data) {
		this.idcard_img_data = idcard_img_data;
	}

	public Bitmap getIdcard_img() {
		return idcard_img;
	}

	public void setIdcard_img(Bitmap idcard_img) {
		this.idcard_img = idcard_img;
	}
}
