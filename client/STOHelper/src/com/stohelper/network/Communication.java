package com.stohelper.network;

public class Communication {
	private NetWorker netWorker;
	private static Communication instance;
	
	//将构造函数私有化，使其不能生成多个实例，防止多次连接连接服务器
	private Communication(){
		netWorker = new NetWorker();
		netWorker.start();
	}
	
	public static Communication getInstance(){
		if(instance == null){
			instance = new Communication();
		}
		return instance;
	}

	public boolean checkConnect(){
		return netWorker.checkConnect();
	}
	
	//退出连接后，清空资源
	public void clear(){
		netWorker.setOnWork(false);
		instance=null;
	}
	
	//登录
	public void login(){
		netWorker.login();
	}

	//注册
	public void register(){
		netWorker.register();
	}

	//得到个人信息
	public void getInformation() {
		netWorker.getInformation();
	}

	//申请转寄员
	public void applyforwarder() {
		netWorker.applyforwarder();
	}

    
	//修改姓名
	public void updateName(String changedName) {
		netWorker.updateName(changedName);
		
	}

	//修改地址
	public void updateAddress(String changedAddress) {
		netWorker.updateAddress(changedAddress);
		
	}

	//修改性别
	public void updateGender(String changedGender) {
		netWorker.updateGender(changedGender);
		
	}
	
	//修改头像
	public void updateProfileImage() {
		netWorker.updateProfileImage();
	}
}
