package com.stohelper.network;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.stohelper.util.BaseActivity;
import com.stohelper.util.Config;
import com.stohelper.util.Constant;
import com.stohelper.util.Tools;

import android.graphics.Bitmap;
import android.os.Message;
import android.util.Log;


public class NetWorker extends Thread{
	private static final String IP = "121.194.63.66";
	private static final int PORT = 30050;
	
	private Socket socket = null;
	private InputStream inIS = null;
	private PrintStream outPS = null;
	private byte[] lengthData = new byte[4];
	
	private Boolean onWork = true;
	protected final byte connect = 1;
	protected final byte running = 2;
	protected byte state = connect;

	private JSONObject jsonObject;
	private JSONArray jsonArray;
	
	private String tag = "后台：";
	private int requestType;
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		
		while (onWork) {
			if(state == connect) {
				connect();
			}else if(state == running){
				receiveMsg();
			}
		}
	}

	private void connect() {
		try{
			socket = new Socket(IP, PORT);
			state = running;
			inIS = new BufferedInputStream(socket.getInputStream());
			outPS = new PrintStream(socket.getOutputStream(), false);

			if(socket != null){
				Log.i(tag, "已连接到服务器");
			}
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void receiveMsg() {
		Log.i(tag, "一直在等待接受服务器返回的信息！");
		try {
			inIS.read(lengthData, 0, 4);
			Log.i(tag, "服务器已返回信息！");
			int length = Tools.getJSONLength(lengthData);
			byte[] data = new byte[length];
			int recvlen=0;
			
			while(recvlen<length){
				recvlen+=inIS.read(data,recvlen,length-recvlen);
			}
			
			Log.i(tag, new Integer(recvlen).toString());
			Log.i(tag, ( new String(data)));
			
			jsonObject = Tools.getJASONObject(length, data);
			requestType = jsonObject.getInt("what");
			int t = jsonObject.getInt("type");
			Log.i(tag,  new Integer(t).toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//登录
		if (requestType == Config.REQUEST_LOGIN) {
			handLogin();
		}
		//注册
		else if (requestType == Config.REQUEST_REGISTER) {
			handRegister();
		}
		//获得个人信息
		else if (requestType == Config.REQUEST_GET_USER_INFO) {
			handGetInformation();
		}
		//申请转寄员
		else if (requestType == Config.REQUEST_FORWARDER_APPLY) {
			handApplyForwarder();
		}
		//修改姓名
		else if (requestType == Config.REQUEST_UPDATE_NAME) {
			handUpdateName();
		}
		//修改性别
		else if (requestType == Config.REQUEST_UPDATE_GENDER) {
			handUpdateGender();
		}
		//修改地址
		else if (requestType == Config.REQUEST_UPDATE_ADDRESS) {
			handUpdateAddress();
		}
		//修改头像
		else if (requestType == Config.REQUEST_UPDATE_PROFILE_IMG) {
			handUpdateProfileImage();
		}
		
	}

	
	public boolean checkConnect() {
		if(state == running)
			return true;
		else
			return false;
	}

	public void setOnWork(Boolean onWork) {
		this.onWork = onWork;
	}
	
	//登录
	public void login() {
		JSONObject jo = new JSONObject();
		try {
			jo.put("what", Config.REQUEST_LOGIN);
			jo.put("phoneNum", Constant.user.getUserPhoneNum());
			jo.put("password", Constant.user.getUserPassword());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.i(tag, "发送登录的请求为：" + jo.toString());
		
		try {
			outPS.write(Tools.getJASONLength(jo));
			outPS.write(jo.toString().getBytes("UTF-8"));
			outPS.flush();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//传递从服务器端返回的登录请求
	private void handLogin() {
		Log.i(tag, "传递从服务器端返回的登录的请求");
		try {
			Message msg = new Message();
			msg.what = jsonObject.getInt("what");
			msg.arg1 = jsonObject.getInt("status");
			BaseActivity.sendMessage(msg);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	}
	
	//注册
	public void register() {
		JSONObject jo = new JSONObject();
		try {
			jo.put("what", Config.REQUEST_REGISTER);
			jo.put("phoneNum", Constant.user.getUserPhoneNum());
			jo.put("password", Constant.user.getUserPassword());
			jo.put("name", Constant.user.getUserName());
			if(Constant.user.getUserGender().equals("男")){
				jo.put("gender", 1);
			}else{
				jo.put("gender", 0);
			}
			jo.put("idcard", Constant.user.getUserIDNum());
			jo.put("address", Constant.user.getUserAddress());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.i(tag, "发送注册的请求为：" + jo.toString());
		
		try {
			outPS.write(Tools.getJASONLength(jo));
			outPS.write(jo.toString().getBytes("UTF-8"));
			outPS.flush();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//传递从服务器端返回的注册请求
	private void handRegister() {
		Log.i(tag, "传递从服务器端返回的注册的请求");
		try {
			Message msg = new Message();
			msg.what = jsonObject.getInt("what");
			msg.arg1 = jsonObject.getInt("status");
			BaseActivity.sendMessage(msg);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	}

	//获得个人信息
	public void getInformation() {
		JSONObject jo = new JSONObject();
		try {
			jo.put("what", Config.REQUEST_GET_USER_INFO);
			jo.put("phoneNum", Constant.user.getUserPhoneNum());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.i(tag, "发送获得个人信息的请求为：" + jo.toString());
		
		try {
			outPS.write(Tools.getJASONLength(jo));
			outPS.write(jo.toString().getBytes("UTF-8"));
			outPS.flush();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//传递从服务器端返回的获得个人信息请求
	private void handGetInformation() {
		// TODO Auto-generated method stub
		Log.i(tag, "传递从服务器端返回的获得个人信息的请求");
		try {
			Message msg = new Message();
			msg.what = jsonObject.getInt("what");
			msg.arg1 = jsonObject.getInt("status");
			Map<String, Object> information = new HashMap<String, Object>();
			information.put("name", jsonObject.getString("name"));
			information.put("type", jsonObject.getInt("type"));
			information.put("gender", jsonObject.getInt("gender"));
			information.put("address", jsonObject.getString("address"));
			Bitmap bm = Tools.getHttpBitmap(jsonObject.getString("profile_img"));
			information.put("profile_img", bm);
			msg.obj = information;
			BaseActivity.sendMessage(msg);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//申请转寄员
	public void applyforwarder() {
		JSONObject jo = new JSONObject();
		try {
			jo.put("what", Config.REQUEST_FORWARDER_APPLY);
			jo.put("phoneNum", Constant.user.getUserPhoneNum());
			jo.put("idcard", Constant.user.getUserIDNum());
			Map<String, Object> image = new HashMap<String, Object>();
			image.put("name", Constant.user.getIdcard_img_name());
			image.put("data", Constant.user.getIdcard_img_data());
			jo.put("idcard_img", image);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.i(tag, "发送的申请转寄员请求为：" + jo.toString());
		
		try {
			outPS.write(Tools.getJASONLength(jo));
			outPS.write(jo.toString().getBytes("UTF-8"));
			outPS.flush();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//传递从服务器端返回的申请转寄员请求
	private void handApplyForwarder() {
		Log.i(tag, "传递从服务器端返回的申请转寄员请求");
		try {
			Message msg = new Message();
			msg.what = jsonObject.getInt("what");
			msg.arg1 = jsonObject.getInt("status");
			BaseActivity.sendMessage(msg);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//修改头像
	public void updateProfileImage() {
		JSONObject jo = new JSONObject();
		try {
			jo.put("what", Config.REQUEST_UPDATE_PROFILE_IMG);
			jo.put("phoneNum", Constant.user.getUserPhoneNum());
			JSONObject image = new JSONObject();
			image.put("name", Constant.user.getProfile_img_name());
			image.put("data", Constant.user.getProfile_img_data());
			jo.put("profile_img", image);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.i(tag, "发送的修改头像请求为：" + jo.toString());
		
		try {
			outPS.write(Tools.getJASONLength(jo));
			outPS.write(jo.toString().getBytes("UTF-8"));
			outPS.flush();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	//传递从服务器端返回的修改头像请求
	private void handUpdateProfileImage() {
		Log.i(tag, "传递从服务器端返回的修改头像请求");
		try {
			Message msg = new Message();
			msg.what = jsonObject.getInt("what");
			msg.arg1 = jsonObject.getInt("status");
			BaseActivity.sendMessage(msg);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	//修改姓名
	public void updateName(String changedName) {
		JSONObject jo = new JSONObject();
		try {
			jo.put("what", Config.REQUEST_UPDATE_NAME);
			jo.put("phoneNum", Constant.user.getUserPhoneNum());
			jo.put("name", Constant.user.getUserName());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.i(tag, "发送的修改姓名请求为：" + jo.toString());
		
		try {
			outPS.write(Tools.getJASONLength(jo));
			outPS.write(jo.toString().getBytes("UTF-8"));
			outPS.flush();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	//传递从服务器端返回的修改姓名请求
	private void handUpdateName() {
		Log.i(tag, "传递从服务器端返回的修改姓名请求");
		try {
			Message msg = new Message();
			msg.what = jsonObject.getInt("what");
			msg.arg1 = jsonObject.getInt("status");
			BaseActivity.sendMessage(msg);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	//修改地址
	public void updateAddress(String changedAddress) {
		JSONObject jo = new JSONObject();
		try {
			jo.put("what", Config.REQUEST_UPDATE_ADDRESS);
			jo.put("phoneNum", Constant.user.getUserPhoneNum());
			jo.put("address", Constant.user.getUserAddress());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.i(tag, "发送的修改地址请求为：" + jo.toString());
		
		try {
			outPS.write(Tools.getJASONLength(jo));
			outPS.write(jo.toString().getBytes("UTF-8"));
			outPS.flush();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//传递从服务器端返回的修改地址请求
	private void handUpdateAddress() {
		Log.i(tag, "传递从服务器端返回的修改地址请求");
		try {
			Message msg = new Message();
			msg.what = jsonObject.getInt("what");
			msg.arg1 = jsonObject.getInt("status");
			BaseActivity.sendMessage(msg);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	//修改性别
	public void updateGender(String changedGender) {
		JSONObject jo = new JSONObject();
		try {
			jo.put("what", Config.REQUEST_UPDATE_GENDER);
			jo.put("phoneNum", Constant.user.getUserPhoneNum());
			if(Constant.user.getUserGender().equals("男")){
				jo.put("gender", 1);
			}else{
				jo.put("gender", 0);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.i(tag, "发送的修改性别请求为：" + jo.toString());
		
		try {
			outPS.write(Tools.getJASONLength(jo));
			outPS.write(jo.toString().getBytes("UTF-8"));
			outPS.flush();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	//传递从服务器端返回的修改性别请求
	private void handUpdateGender() {
		Log.i(tag, "传递从服务器端返回的修改性别请求");
		try {
			Message msg = new Message();
			msg.what = jsonObject.getInt("what");
			msg.arg1 = jsonObject.getInt("status");
			BaseActivity.sendMessage(msg);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
}