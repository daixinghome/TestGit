package com.stohelper.network;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
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
import android.graphics.BitmapFactory;
import android.os.Message;
import android.util.Base64;
import android.util.Log;


public class NetWorker extends Thread{
	private static final String IP = "121.194.63.66";
	private static final int PORT = 30050;
	
	private Socket socket = null;
	private InputStream inIS = null;
	private PrintStream outPS = null;
//	private PrintWriter outPW = null;
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
			handLoginAndRegister();
		}
		//注册
		else if (requestType == Config.REQUEST_REGISTER) {
			handLoginAndRegister();
		}
		//获得个人信息
		else if (requestType == Config.REQUEST_GET_USER_INFO) {
			handGetInformation();
		}
		else if (requestType == Config.REQUEST_FORWARDER_APPLY) {
			handApplyForwarder();
		}
		else if (requestType == Config.REQUEST_UPDATE_NAME) {
			handUpdateName();
		}
		else if (requestType == Config.REQUEST_UPDATE_GENDER) {
			handUpdateGender();
		}
		else if (requestType == Config.REQUEST_UPDATE_ADDRESS) {
			handUpdateAddress();
		}
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

	public void handLoginAndRegister() {
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
	
	public void getInformation() {
		JSONObject jo = new JSONObject();
		try {
			jo.put("what", Config.REQUEST_GET_USER_INFO);
			jo.put("phoneNum", Constant.user.getUserPhoneNum());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.i(tag, "发送的请求为：" + jo.toString());
		
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

	public void applyforwarder() {
		JSONObject jo = new JSONObject();
		try {
			jo.put("what", Config.REQUEST_FORWARDER_APPLY);
			jo.put("phoneNum", Constant.user.getUserPhoneNum());
			jo.put("name", Constant.user.getUserName());
			if(Constant.user.getUserGender().equals("男")){
				jo.put("gender", 1);
			}else{
				jo.put("gender", 0);
			}
			jo.put("idcard", Constant.user.getUserIDNum());
			//jo.put("address", Constant.user.getUserAddress());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.i(tag, "发送的请求为：" + jo.toString());
		
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

	

	/*public void changeInformation() {
		JSONObject jo = new JSONObject();
		try {
			jo.put("what", Config.REQUEST_UPDATE_USER_INFO);
			jo.put("phoneNum", Constant.user.getUserPhoneNum());
			jo.put("name", Constant.user.getUserName());
			if(Constant.user.getUserGender().equals("男")){
				jo.put("gender", 1);
			}else{
				jo.put("gender", 0);
			}
			
			jo.put("address", Constant.user.getUserAddress());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.i(tag, "发送的请求为：" + jo.toString());
		
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
	*/
	private void handChangeInformation() {
		Log.i(tag, "传递从服务器端返回的修改个人信息请求");
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
	
	public String getFromBASE64(String s){
		if(s == null){
			return null;
		}
		
		try{
			byte b[] = Base64.decode(s,Base64.DEFAULT);
			return new String(b);
		}catch(Exception e){
			return null;
		}
	}
	
	
	

	public void updateProfileImage() {
		JSONObject jo = new JSONObject();
		try {
			jo.put("what", Config.REQUEST_UPDATE_PROFILE_IMG);
			jo.put("phoneNum", Constant.user.getUserPhoneNum());
			JSONObject image = new JSONObject();
//			Map<String, Object> image = new HashMap<String, Object>();
			image.put("name", Constant.user.getProfile_img_name());
			image.put("data", Constant.user.getProfile_img_data());
			jo.put("profile_img", image);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.i(tag, "发送的请求为：" + jo.toString());
		
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
		Log.i(tag, "发送的请求为：" + jo.toString());
		
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
	
	private void handUpdateName() {
		Log.i(tag, "传递从服务器端返回的修改个人信息请求");
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
		Log.i(tag, "发送的请求为：" + jo.toString());
		
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

	public void updateGender(String changedGender) {
		JSONObject jo = new JSONObject();
		try {
			jo.put("what", Config.REQUEST_UPDATE_NAME);
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
		Log.i(tag, "发送的请求为：" + jo.toString());
		
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

	private void handUpdateAddress() {
		Log.i(tag, "传递从服务器端返回的修改个人信息请求");
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

	private void handUpdateGender() {
		Log.i(tag, "传递从服务器端返回的修改个人信息请求");
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
	
	private void handUpdateProfileImage() {
		Log.i(tag, "传递从服务器端返回的修改个人信息请求");
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