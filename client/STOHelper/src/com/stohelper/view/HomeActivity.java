package com.stohelper.view;

import java.util.List;
import java.util.Map;

import com.stohelper.util.BaseActivity;
import com.stohelper.util.Config;
import com.stohelper.util.Constant;
import com.stohelper.view.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class HomeActivity extends BaseActivity implements OnClickListener{
	private SlidingMenu mMenu;
	
	private ImageButton btn_picture, btn_traderecord,btn_mypackage;
	private TextView tv_name,tv_phone;
	private ImageView slideicon_personalinfo;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home2);
		mMenu = (SlidingMenu) findViewById(R.id.id_menu);
		
		slideicon_personalinfo = (ImageView)findViewById(R.id.home_slideicon_personalinfo);
		btn_picture = (ImageButton)findViewById(R.id.home2_btn_picture);
		slideicon_personalinfo.setOnClickListener(this);
		btn_picture.setOnClickListener(this);
	}

	public void toggleMenu(View view)
	{
		mMenu.toggle();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.home_slideicon_personalinfo:
			Intent intent=new Intent(HomeActivity.this,InformationActivity.class);
	        startActivity(intent);	
			break;
			
        case R.id.home2_btn_picture:
        	BaseActivity.con.getInformation();
			Intent intent2=new Intent(HomeActivity.this,InformationActivity.class);
	        startActivity(intent2);	
			break;
			
		default:
			break;
		}
	}

	@Override
	public void processMessage(Message message) {
		
	}
}
