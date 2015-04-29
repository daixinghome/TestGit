package com.stohelper.network;

public class Communication {
	private NetWorker netWorker;
	private static Communication instance;
	
	//�����캯��˽�л���ʹ�䲻�����ɶ��ʵ������ֹ����������ӷ�����
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
	
	//�˳����Ӻ������Դ
	public void clear(){
		netWorker.setOnWork(false);
		instance=null;
	}
	
	//��¼
	public void login(){
		netWorker.login();
	}

	//ע��
	public void register(){
		netWorker.register();
	}

	//�õ�������Ϣ
	public void getInformation() {
		netWorker.getInformation();
	}

	//����ת��Ա
	public void applyforwarder() {
		netWorker.applyforwarder();
	}

    
	public void updateProfileImage() {
		netWorker.updateProfileImage();
	}

	public void updateName(String changedName) {
		netWorker.updateName(changedName);
		
	}

	public void updateAddress(String changedAddress) {
		netWorker.updateAddress(changedAddress);
		
	}

	public void updateGender(String changedGender) {
		netWorker.updateGender(changedGender);
		
	}
}
