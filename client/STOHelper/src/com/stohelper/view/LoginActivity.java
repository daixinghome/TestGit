package com.stohelper.view;

import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.stohelper.network.Communication;
import com.stohelper.util.BaseActivity;
import com.stohelper.util.Config;
import com.stohelper.util.Constant;



public class LoginActivity extends BaseActivity implements OnClickListener{
	private ImageButton btn_cancel, btn_ok;
	private EditText et_userPhoneNum;
	private EditText et_userPassword;
	private CheckBox cb_ifRemember;
	private CheckBox cb_ifAutoLogin;
	
	private SharedPreferences sp;
	private Editor ed;
	private boolean ifReadyRem = false;
	private boolean ifReadyAuto = false;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        con = Communication.getInstance();
        
        sp = getSharedPreferences("users", Context.MODE_PRIVATE);
        ed = sp.edit();
        
        btn_cancel = (ImageButton) findViewById(R.id.login_btn_cancel);
        btn_ok = (ImageButton) findViewById(R.id.login_btn_ok);
        btn_cancel.setOnClickListener(this);
        btn_ok.setOnClickListener(this);
        
        cb_ifRemember = (CheckBox) findViewById(R.id.login_cb_ifRemember);
        cb_ifAutoLogin = (CheckBox) findViewById(R.id.login_cb_ifAutoLogin);
        cb_ifRemember.setOnClickListener(this);
        cb_ifAutoLogin.setOnClickListener(this);
        
        et_userPhoneNum = (EditText) findViewById(R.id.login_et_userPhoneNum);
        et_userPassword = (EditText) findViewById(R.id.login_et_password);
        
        et_userPhoneNum.setText(sp.getString("UserPhone", ""));
        boolean ifRem = sp.getBoolean("IfRemember", false);
        boolean ifAuto = sp.getBoolean("IfAutoLogin", false);
        
        if(ifRem){
        	cb_ifRemember.setChecked(true);
        	et_userPassword.setText(sp.getString("UserPassword", ""));
        }else{
        	cb_ifRemember.setChecked(false);
        }
        
        if(ifAuto){
        	cb_ifAutoLogin.setChecked(true);
        }else{
        	cb_ifAutoLogin.setChecked(false);
        }
        
        if(ifRem && ifAuto){
        	Constant.user.setUserPhoneNum(et_userPhoneNum.getText().toString());
			Constant.user.setUserPassword(et_userPassword.getText().toString());
			BaseActivity.con.getInformation();
			BaseActivity.con.login();
        }
    }

    @Override
    public void onClick(View v) {
    	// TODO Auto-generated method stub
    	switch (v.getId()) {
		case R.id.login_btn_cancel:
			//屏幕转换
            finish();
			break;
		case R.id.login_btn_ok:
			Constant.user.setUserPhoneNum(et_userPhoneNum.getText().toString());
			Constant.user.setUserPassword(et_userPassword.getText().toString());
			BaseActivity.con.getInformation();
			BaseActivity.con.login();
			break;
		case R.id.login_cb_ifRemember:
			if(!et_userPhoneNum.getText().toString().equalsIgnoreCase("")){
				ifReadyRem = cb_ifRemember.isChecked();
				if(!ifReadyRem)
					ed.putString("UserPassword", "");
			}else{
				Toast.makeText(this,"请输入用户名", 1500).show();
				cb_ifRemember.setChecked(false);
			}
			break;
		case R.id.login_cb_ifAutoLogin:
			ifReadyAuto = cb_ifAutoLogin.isChecked();
			break;
		default:
			break;
		}
    }    

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

	@Override
	public void processMessage(Message message) {
		switch(message.what){
		case Config.REQUEST_LOGIN:
			int result = message.arg1;
			
			if(result == Config.SUCCESS){
				 Toast toast = Toast.makeText(LoginActivity.this,"登录成功", 1500);
				 toast.show();
				 
				 ed.putString("UserPhone", et_userPhoneNum.getText().toString());
				 if(ifReadyRem){
					 ed.putBoolean("IfRemember", ifReadyRem);
					 ed.putString("UserPassword", et_userPassword.getText().toString());
					 ed.commit();
				 }
				 
				 if(ifReadyAuto){
					 ed.putBoolean("IfAutoLogin", ifReadyAuto);
					 ed.commit();
				 }
				 
				 Intent intent=new Intent(LoginActivity.this,HomeActivity.class);
			     startActivity(intent);	
			}else{
				 Toast.makeText(this,"登录失败", 1500).show();
				 et_userPassword.setText("");
			}
			break;
		case Config.REQUEST_GET_USER_INFO:
			int result1 = message.arg1;
			
			if(result1 == Config.SUCCESS){
				Map<String, Object> map = (Map<String, Object>)message.obj;
				Constant.user.setUserAddress((String) map.get("address"));
				Constant.user.setUserName((String) map.get("name"));
				Constant.user.setUserType((Integer) map.get("type"));
				int gender = (Integer) map.get("gender");
				if(gender == 0){
					Constant.user.setUserGender("Ů");
				}else{
					Constant.user.setUserGender("��");
				}
				Constant.user.setProfile_img((Bitmap)map.get("profile_img"));
				
			}
			break;
		}
	}
}
