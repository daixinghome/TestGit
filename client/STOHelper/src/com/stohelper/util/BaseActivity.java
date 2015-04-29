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
		
		//判断该Activity是否在LinkedList中，没有在的话就添加上
		if(!queue.contains(this)){
			queue.add(this);
			System.out.println("将"+queue.getLast()+"添加到list中去");
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
					Log.i("后台：","执行请求："+msg.what+"; 执行状态："+msg.arg1);
					queue.getLast().processMessage(msg);
				}
				break;
			}
		};
		
	};
	
	//处理接收到的message
	public abstract void processMessage(Message message);
	//传递接收到的消息给Activity
	public static void sendMessage(Message msg){
		handler.sendMessage(msg);
	}
}
