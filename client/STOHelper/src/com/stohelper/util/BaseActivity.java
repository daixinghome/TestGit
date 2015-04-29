package com.stohelper.util;

import java.util.LinkedList;

import com.stohelper.network.Communication;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public abstract class BaseActivity extends Activity{
	protected static LinkedList<BaseActivity> queue= new LinkedList<BaseActivity>();
	public static Communication con = Communication.getInstance();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		//�жϸ�Activity�Ƿ���LinkedList�У�û���ڵĻ��������
		if(!queue.contains(this)){
			queue.add(this);
			System.out.println("��"+queue.getLast()+"��ӵ�list��ȥ");
		}
	}
	
	private static Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			
			case 9999:	
				
				break;
			default:
				if(!queue.isEmpty()){
					Log.i("��̨��","ִ������"+msg.what+"; ִ��״̬��"+msg.arg1);
					queue.getLast().processMessage(msg);
				}
				break;
			}
		};
		
	};
	
	//������յ���message
	public abstract void processMessage(Message message);
	//���ݽ��յ�����Ϣ��Activity
	public static void sendMessage(Message msg){
		handler.sendMessage(msg);
	}
}
