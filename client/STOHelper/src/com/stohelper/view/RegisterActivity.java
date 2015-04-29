package com.stohelper.view;

import static cn.smssdk.framework.utils.R.getStringRes;

import com.stohelper.util.BaseActivity;
import com.stohelper.util.Constant;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class RegisterActivity extends BaseActivity implements OnClickListener{
	// 填写从短信SDK应用后台mob注册得到的APPKEY 
	private static String APPKEY = "6e32d54ce50c";//DX注册所得
	// 填写从短信SDK应用后台mob注册得到的APPSECRET
	private static String APPSECRET = "1c4e982c105c49bcdf28014388346b44";//DX注册所得
	
	private ImageButton btn_getVcode, btn_checkVcode, btn_submit;
	private EditText et_phoneNum, et_vCode, et_password, et_name, et_IDNum, et_address;
	private Spinner spin_gender;
	private String phString;
	private static final String[] genders={"男","女"};
	private ArrayAdapter<String> adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		
		btn_getVcode = (ImageButton) findViewById(R.id.register_btn_getVCode);
		btn_checkVcode = (ImageButton) findViewById(R.id.register_btn_checkVCode);
		btn_submit = (ImageButton) findViewById(R.id.register_btn_submit);
		
		et_phoneNum = (EditText) findViewById(R.id.register_et_phoneNum);
		et_vCode = (EditText) findViewById(R.id.register_et_vCode);
		et_password = (EditText) findViewById(R.id.register_et_password);
		et_name = (EditText) findViewById(R.id.register_et_name);
		et_IDNum = (EditText) findViewById(R.id.register_et_ID);
		et_address = (EditText) findViewById(R.id.register_et_address);
		
		spin_gender = (Spinner) findViewById(R.id.register_spinner_gender);
		
		btn_getVcode.setOnClickListener(this);
		btn_checkVcode.setOnClickListener(this);
		btn_submit.setOnClickListener(this);
		
		btn_submit.setEnabled(false);
		
		spin_gender = (Spinner) findViewById(R.id.register_spinner_gender);
        //将可选内容与ArrayAdapter连接起来
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,genders);
         
        //设置下拉列表的风格
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
         
        //将adapter 添加到spinner中
        spin_gender.setAdapter(adapter);
         
        //设置默认值
        spin_gender.setVisibility(View.VISIBLE);
        
		SMSSDK.initSDK(this,APPKEY,APPSECRET);
		EventHandler eh=new EventHandler(){
			@Override
			public void afterEvent(int event, int result, Object data) {
				Message msg = new Message();
				msg.arg1 = event;
				msg.arg2 = result;
				msg.obj = data;
				handler.sendMessage(msg);
				
				if(event == 3){
					if(result == -1){//验证成功
						et_phoneNum.setEnabled(false);
						btn_getVcode.setEnabled(false);
						btn_checkVcode.setEnabled(false);
						btn_submit.setEnabled(true);
					}
				}
			}
			
		};
		SMSSDK.registerEventHandler(eh);
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.register_btn_getVCode:
			if(!TextUtils.isEmpty(et_phoneNum.getText().toString())){
				SMSSDK.getVerificationCode("86",et_phoneNum.getText().toString());
				phString=et_phoneNum.getText().toString();
			}else {
				Toast.makeText(this, "电话不能为空", 1).show();
			}
			break;
		case R.id.register_btn_submit:
			if(!TextUtils.isEmpty(et_vCode.getText().toString())){
				SMSSDK.submitVerificationCode("86", phString, et_vCode.getText().toString());
			}else {
				Toast.makeText(this, "验证码不能为空", 1).show();
			}
			break;
		case R.id.register_btn_checkVCode:
			Constant.user.setUserPhoneNum(et_phoneNum.getText().toString());
			Constant.user.setUserPassword(et_password.getText().toString());
			Constant.user.setUserName(et_name.getText().toString());
			Constant.user.setUserGender(spin_gender.getSelectedItem().toString());
			Constant.user.setUserIDNum(et_IDNum.getText().toString());
			Constant.user.setUserAddress(et_address.getText().toString());
			
			BaseActivity.con.register();
			break;
		default:
			break;
		}
	}
	
	Handler handler=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			int event = msg.arg1;
			int result = msg.arg2;
			Object data = msg.obj;
			Log.e("event", "event="+event);
			if (result == SMSSDK.RESULT_COMPLETE) {
				if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {//提交验证码成功
					Toast.makeText(getApplicationContext(), "提交验证码成功", Toast.LENGTH_SHORT).show();
				} else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){
					Toast.makeText(getApplicationContext(), "验证码已经发送", Toast.LENGTH_SHORT).show();
				}
			} else {
				((Throwable) data).printStackTrace();
				int resId = getStringRes(RegisterActivity.this, "smssdk_network_error");
				Toast.makeText(RegisterActivity.this, "验证码错误", Toast.LENGTH_SHORT).show();
				if (resId > 0) {
					Toast.makeText(RegisterActivity.this, resId, Toast.LENGTH_SHORT).show();
				}
			}
		}
	};
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		SMSSDK.unregisterAllEventHandler();
	}

	@Override
	public void processMessage(Message message) {
		
	}
}
