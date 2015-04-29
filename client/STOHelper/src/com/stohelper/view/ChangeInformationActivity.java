package com.stohelper.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.stohelper.util.BaseActivity;
import com.stohelper.util.Config;
import com.stohelper.util.Constant;

public class ChangeInformationActivity extends BaseActivity implements OnClickListener{

	private ImageButton btn_cancel, btn_ok;
	private EditText et_phone,et_name,et_address;
	private Spinner spinner_gender;
	private static final String[] genders={"男","女"};
	private ArrayAdapter<String> adapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_changeinfo);
		
		btn_cancel = (ImageButton)findViewById(R.id.changeinfo_btn_cancel);
		btn_ok = (ImageButton)findViewById(R.id.changeinfo_btn_ok);
		et_phone = (EditText)findViewById(R.id.changeinfo_et_phone);
		et_name = (EditText)findViewById(R.id.changeinfo_et_name);
		et_address = (EditText)findViewById(R.id.changeinfo_et_address);
		spinner_gender = (Spinner)findViewById(R.id.changeinfo_spinner_gender);
		
        //将可选内容与ArrayAdapter连接起来
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,genders);
         
        //设置下拉列表的风格
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
         
        //将adapter 添加到spinner中
        spinner_gender.setAdapter(adapter);
         
        //设置默认值
        spinner_gender.setVisibility(View.VISIBLE);
        
		et_phone.setText(Constant.user.getUserPhoneNum());
		et_name.setText(Constant.user.getUserName());
		et_address.setText(Constant.user.getUserAddress());
		
		if(Constant.user.getUserGender().equals("男")){
			spinner_gender.setSelection(0);
		}else{
			spinner_gender.setSelection(1);
		}
		
		btn_cancel.setOnClickListener(this);
		btn_ok.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.changeinfo_btn_cancel:
			finish();
			break;
		case R.id.changeinfo_btn_ok:
			if(!TextUtils.isEmpty(et_name.getText().toString()) &&
			   !TextUtils.isEmpty(et_address.getText().toString())
			){
				Constant.user.setUserName(et_name.getText().toString());
				Constant.user.setUserGender(spinner_gender.getSelectedItem().toString());
				Constant.user.setUserAddress(et_address.getText().toString());
				//BaseActivity.con.changeInformation();
			}else {
				Toast.makeText(this, "姓名和地址不能为空", 1).show();
			}
			break;
		default:
			break;
		}
		
	}

	@Override
	public void processMessage(Message message) {
		switch(message.what){
		/*case Config.REQUEST_UPDATE_USER_INFO:
			int result = message.arg1;
			
			if(result == Config.SUCCESS){
				 Toast toast = Toast.makeText(ChangeInformationActivity.this,"信息修改成功", 1500);
				 toast.show();
				 Intent intent=new Intent(ChangeInformationActivity.this,InformationActivity.class);
			     startActivity(intent);	
			}else{
				 Toast.makeText(this,"信息修改失败", 1500).show();
			}
			break;*/
		}
		
	}

}