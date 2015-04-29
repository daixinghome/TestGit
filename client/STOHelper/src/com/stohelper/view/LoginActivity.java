package com.stohelper.view;

import java.util.Map;

import com.stohelper.network.Communication;
import com.stohelper.util.BaseActivity;
import com.stohelper.util.Config;
import com.stohelper.util.Constant;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;



public class LoginActivity extends BaseActivity implements OnClickListener{
//	public Communication con;
	private ImageButton btn_cancel, btn_ok;
	private EditText et_userPhoneNum;
	private EditText et_userPassword;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        con = Communication.getInstance();
        
        btn_cancel = (ImageButton) findViewById(R.id.login_btn_cancel);
        btn_ok = (ImageButton) findViewById(R.id.login_btn_ok);
        btn_cancel.setOnClickListener(this);
        btn_ok.setOnClickListener(this);
        
        et_userPhoneNum = (EditText) findViewById(R.id.login_et_userPhoneNum);
        et_userPassword = (EditText) findViewById(R.id.login_et_password);
    }

    @Override
    public void onClick(View v) {
    	// TODO Auto-generated method stub
    	switch (v.getId()) {
		case R.id.login_btn_cancel:
			//ÆÁÄ»×ª»»
            finish();
			break;
		case R.id.login_btn_ok:
			Constant.user.setUserPhoneNum(et_userPhoneNum.getText().toString());
			Constant.user.setUserPassword(et_userPassword.getText().toString());
			BaseActivity.con.getInformation();
			BaseActivity.con.login();
			//
			//Intent intent=new Intent(LoginActivity.this,HomeActivity.class);
	        //startActivity(intent);	
			//
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
				 Toast toast = Toast.makeText(LoginActivity.this,"µÇÂ¼³É¹¦", 1500);
				 toast.show();
				 Intent intent=new Intent(LoginActivity.this,HomeActivity.class);
			     startActivity(intent);	
			}else{
				 Toast.makeText(this,"µÇÂ¼Ê§°Ü", 1500).show();
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
					Constant.user.setUserGender("Å®");
				}else{
					Constant.user.setUserGender("ÄÐ");
				}
				Constant.user.setProfile_img((Bitmap)map.get("profile_img"));
				
			}
			break;
		}
	}
}
