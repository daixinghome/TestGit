package com.stohelper.view;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.stohelper.network.NetWorker;
import com.stohelper.util.BaseActivity;
import com.stohelper.util.Config;
import com.stohelper.util.Constant;
import com.stohelper.util.Tools;

public class InformationActivity extends BaseActivity implements OnClickListener{

	//private ImageButton btn_applyforwarder, btn_changeinformation, btn_back;
	private TextView tv_phone,tv_name,tv_gender,tv_adress;
	//private ImageView iv_picture;
	private RoundImageView iv_picture;
	private ListView listview1,listview2;
	String changedPhone,changedName,changedSex,changedAddress;
	private Drawable drawable = null;
	
	private String[] items = new String[] { "ѡ�񱾵�ͼƬ", "����" };
	/* ������ */
	private static final int IMAGE_REQUEST_CODE = 0;
	private static final int CAMERA_REQUEST_CODE = 1;
	private static final int RESULT_REQUEST_CODE = 2;
	/* ͷ������ */
	private static final String IMAGE_FILE_NAME = "faceImage.jpg";
	private ImageView faceImage;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_information);
		
		listview1 = (ListView)findViewById(R.id.listview1);
		//listview1.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1,getData()));

		final SimpleAdapter adapter = new SimpleAdapter(this, getData(),R.layout.listview,
				new String[] {"name"}, new int[] {R.id.title});
		adapter.notifyDataSetChanged();
		listview1.setAdapter(adapter);
		
		listview1.setOnItemClickListener(new OnItemClickListener(){			
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg){
				int row = arg2;       
				
				if(row==1){//�޸�����
					changeNameDialog();
				}
				else if(row==2){//�޸��Ա�
					changeSexDialog();
				}
				else if(row==3){//�޸ĵ�ַ
					changeAddressDialog();
				}
			}			
		});
		
		listview2 = (ListView)findViewById(R.id.listview2);
		//listview2.setAdapter(new ArrayAdapter<String>(this, R.layout.listview,getData2()));
		final SimpleAdapter adapter2 = new SimpleAdapter(this, getData2(),R.layout.listview,
				new String[] {"name"}, new int[] {R.id.title});
		adapter2.notifyDataSetChanged();
		listview2.setAdapter(adapter2);

		listview2.setOnItemClickListener(new OnItemClickListener(){			
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg){
				int row = arg2;       
				if(row==0){//����ת��Ա
					Intent intent=new Intent(InformationActivity.this,ApplyForwarderActivity.class);
			        startActivity(intent);	
				}
				else if(row==1){//�޸�����
					
				}
			}			
		});
		
		//btn_applyforwarder = (ImageButton)findViewById(R.id.information_btn_applyforwarder);
		//btn_changeinformation = (ImageButton)findViewById(R.id.information_btn_changeinformation);
		//btn_back = (ImageButton)findViewById(R.id.information_btn_back);
		//btn_applyforwarder.setOnClickListener(this);
		//btn_changeinformation.setOnClickListener(this);
		//btn_back.setOnClickListener(this);
		
		//tv_phone = (TextView)findViewById(R.id.information_tv_phone);
		//tv_name = (TextView)findViewById(R.id.information_tv_name);
		//tv_gender = (TextView)findViewById(R.id.information_tv_gender);
		//tv_adress = (TextView)findViewById(R.id.information_tv_adress);
		//iv_picture = (ImageView)findViewById(R.id.information_iv_picture);
		iv_picture = (RoundImageView) findViewById(R.id.information_iv_picture);
		iv_picture.setImageResource(R.drawable.default_picture);
		iv_picture.setOnClickListener(this);
		
		//tv_phone.setText("�ֻ��ţ�"+Constant.user.getUserPhoneNum());
		//tv_name.setText("��    ����"+Constant.user.getUserName());
		//tv_gender.setText("��    ��"+Constant.user.getUserGender());
		//tv_adress.setText("��    ַ��"+Constant.user.getUserAddress());
		
		if(Constant.user.getProfile_img()!=null){
			iv_picture.setImageBitmap(Constant.user.getProfile_img());
		}
	
		
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		/*case R.id.information_btn_applyforwarder:
			
			Intent intent=new Intent(InformationActivity.this,ApplyForwarderActivity.class);
	        startActivity(intent);	
			break;
        case R.id.information_btn_changeinformation:
			
			Intent intent2=new Intent(InformationActivity.this,ChangeInformationActivity.class);
	        startActivity(intent2);	
			break;
        case R.id.information_btn_changepassword:
			finish();
			break;
        case R.id.information_btn_back:
			finish();
			break;*/
		case R.id.information_iv_picture:
			showPictureSelectDialog();
			break;
		default:
			break;
		}
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public void processMessage(Message message) {
		switch(message.what){
		
		case Config.REQUEST_UPDATE_PROFILE_IMG:
			int result = message.arg1;
			
			if(result == Config.SUCCESS){
				 Toast toast = Toast.makeText(InformationActivity.this,"ͷ���޸ĳɹ�", 1500);
				 toast.show();
			}else{
				 Toast.makeText(this,"����ת��Աʧ��", 1500).show();
			}
			break;
		//Bitmap bitmap = convertStringToIcon(Constant.user.getProfile_img_data());

		//Bitmap bm=n.getHttpBitmap("http://121.194.63.66/default.bmp");
		
		 
		//iv_picture.s.setImageURI(new URI("http://121.194.63.66/default.bmp"));
		}
	}

	public static Map<String, String> objectToMap(Object obj) throws Exception {    
        if(obj == null){    
            return null;    
        }   
   
        Map<String, String> map = new HashMap<String, String>();    
   
        Field[] declaredFields = obj.getClass().getDeclaredFields();    
        for (Field field : declaredFields) {    
            field.setAccessible(true);  
            map.put(field.getName(), (String) field.get(obj));  
        }    
   
        return map;  
    } 
	
	public static Bitmap convertStringToIcon(String st)  
    {  
        // OutputStream out;  
        Bitmap bitmap = null;  
        try  
        {  
            // out = new FileOutputStream("/sdcard/aa.jpg");  
            
        	byte[] bitmapArray;
          
            ByteArrayInputStream bais = new ByteArrayInputStream(st.getBytes());
            
        
            
            
            //YuvImage yuvimage=new YuvImage(bitmapArray,ImageFormat.NV21,IMG_WIDTH,100,null);
            //ByteArrayOutputStream baos = new ByteArrayOutputStream();
            //yuvimage.compressToJpeg(new Rect(0,0,20,20), 100, baos);
            //bitmapArray=baos.toByteArray();
            //bitmapArray = Base64.decode(st, Base64.DEFAULT);  
            bitmap =  
                    BitmapFactory.decodeStream(bais);
            // bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);  
            return bitmap;  
        }  
        catch (Exception e)  
        {  
            return null;  
        }  
    }  
	
	private List<Map<String, Object>> getData(){   
        final List<Map<String, Object>> listItem = new ArrayList<Map<String, Object>>(); // ����һ��list����
		Map<String, Object> map1 = new HashMap<String, Object>(); 
		map1.put("name", "�ֻ��ţ�"+Constant.user.getUserPhoneNum());	
		listItem.add(map1); 
		Map<String, Object> map2 = new HashMap<String, Object>(); 
		map2.put("name", "��    ����"+Constant.user.getUserName());	
		listItem.add(map2); 
		Map<String, Object> map3 = new HashMap<String, Object>(); 
		map3.put("name", "��    ��"+Constant.user.getUserGender());	
		listItem.add(map3); 
		Map<String, Object> map4 = new HashMap<String, Object>(); 
		map4.put("name", "��    ַ��"+Constant.user.getUserAddress());	
		listItem.add(map4);
    
        return listItem;
    }
	
	private List<Map<String, Object>> getData2(){		
		final List<Map<String, Object>> listItem = new ArrayList<Map<String, Object>>(); // ����һ��list����
		Map<String, Object> map1 = new HashMap<String, Object>(); // ʵ����Map����
		map1.put("name", "����ת��Ա");	
		listItem.add(map1); 
		Map<String, Object> map2 = new HashMap<String, Object>(); // ʵ����Map����
		map2.put("name", "�޸�����");	
		listItem.add(map2);
    
        return listItem;
    }
	
	private void changeNameDialog() {
		final EditText text = new EditText(this);
		text.setText(Constant.user.getUserName());
		new AlertDialog.Builder(this)
				.setTitle("�������޸ĵ�����")
				.setView(text)
				.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				})
				.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {  
	               @Override  
	               public void onClick(DialogInterface dialog, int which) {		
	            	   changedName = text.getText().toString();
	            	   Constant.user.setUserName(changedName);
	            	   BaseActivity.con.updateName(changedName);
	            	   Toast.makeText(getApplicationContext(), "HHHHH"+changedName,
	  					     Toast.LENGTH_LONG).show();
	            	   
	            	   View view =  listview1.getChildAt(1- listview1.getFirstVisiblePosition()); 
						TextView tv = (TextView) view.findViewById(R.id.title);
						tv.setText("��    ����"+ Constant.user.getUserName());
	    		  }  
	        }) 
			.show(); 
	}
	private void changeSexDialog() {
		int choice = 0;
		if(Constant.user.getUserGender() == "��"){
			choice = 0;
		}else{
			choice = 1;
		}
		new AlertDialog.Builder(this)
				.setTitle("�������޸ĵ��Ա�")
				.setIcon(android.R.drawable.ic_dialog_info)                
 	            .setSingleChoiceItems(new String[] {"��","Ů"}, choice,
 	            		new DialogInterface.OnClickListener() { 	            	 	            	                
 	       	        public void onClick(DialogInterface dialog, int which) {
 	       	    	    switch (which) {
 	       	    	    case 0: 	       	    	    	
 	       	    	        changedSex = "��"; 	       	    		
 	       			        break;
 	       		        case 1:
 	       		            changedSex = "Ů";  	       			    
 	       			        break; 	       	        
 	       	    	    }
 	       	    	
 	       	     } 	       	    	
 	            })
				.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {						
						dialog.dismiss();
					}
				}
				)
				.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {  
	               @Override  
	               public void onClick(DialogInterface dialog, int which) {		
	            	   Toast.makeText(getApplicationContext(), "HHHHH"+changedSex,
		  					     Toast.LENGTH_LONG).show();
	 	       	    Constant.user.setUserGender(changedSex);	
	 	       	    BaseActivity.con.updateGender(changedSex);
	 	       	    View view =  listview1.getChildAt(2- listview1.getFirstVisiblePosition()); 
					TextView tv = (TextView) view.findViewById(R.id.title);
					tv.setText("��   ��"+ Constant.user.getUserGender());
	    		  }  
	        }) 
				

			.show(); 
	}
	
	private void changeAddressDialog() {
		final EditText text = new EditText(this);
		text.setText(Constant.user.getUserAddress());
		new AlertDialog.Builder(this)
				.setTitle("�������޸ĵĵ�ַ")
				.setView(text)
				.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				})
				.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {  
	               @Override  
	               public void onClick(DialogInterface dialog, int which) {		
	            	   changedAddress = text.getText().toString();
	            	   Toast.makeText(getApplicationContext(), "HHHHH"+changedAddress,
	  					     Toast.LENGTH_LONG).show();
	            	   Constant.user.setUserAddress(changedAddress);
	            	   BaseActivity.con.updateAddress(changedAddress);
	            	   View view =  listview1.getChildAt(3- listview1.getFirstVisiblePosition()); 
						TextView tv = (TextView) view.findViewById(R.id.title);
						tv.setText("��   ַ��"+ Constant.user.getUserAddress());
	    		  }  
	        }) 
			.show(); 
	}
	
	/**
	 * ��ʾѡ��Ի���
	 */
	private void showPictureSelectDialog() {

		new AlertDialog.Builder(this)
				.setTitle("�޸�ͷ��")
				.setItems(items, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case 0:
							Intent intentFromGallery = new Intent();
							intentFromGallery.setType("image/*"); // �����ļ�����
							intentFromGallery
									.setAction(Intent.ACTION_GET_CONTENT);
							startActivityForResult(intentFromGallery,
									IMAGE_REQUEST_CODE);
							break;
						case 1:

							Intent intentFromCapture = new Intent(
									MediaStore.ACTION_IMAGE_CAPTURE);
							// �жϴ洢���Ƿ�����ã����ý��д洢
							if (Tools.hasSdcard()) {

								intentFromCapture.putExtra(
										MediaStore.EXTRA_OUTPUT,
										Uri.fromFile(new File(Environment
												.getExternalStorageDirectory(),
												IMAGE_FILE_NAME)));
							}

							startActivityForResult(intentFromCapture,
									CAMERA_REQUEST_CODE);
							break;
						}
					}
				})
				.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				}).show();

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		//����벻����ȡ��ʱ��
		if (resultCode != RESULT_CANCELED) {

			switch (requestCode) {
			case IMAGE_REQUEST_CODE:
				startPhotoZoom(data.getData());
				break;
			case CAMERA_REQUEST_CODE:
				if (Tools.hasSdcard()) {
					File tempFile = new File(
							Environment.getExternalStorageDirectory()
									+ IMAGE_FILE_NAME);
					startPhotoZoom(Uri.fromFile(tempFile));
				} else {
					Toast.makeText(InformationActivity.this, "δ�ҵ��洢�����޷��洢��Ƭ��",
							Toast.LENGTH_LONG).show();
				}

				break;
			case RESULT_REQUEST_CODE:
				if (data != null) {
					getImageToView(data);
				}
				break;
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * �ü�ͼƬ����ʵ��
	 * 
	 * @param uri
	 */
	public void startPhotoZoom(Uri uri) {

		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// ���òü�
		intent.putExtra("crop", "true");
		// aspectX aspectY �ǿ�ߵı���
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY �ǲü�ͼƬ���
		intent.putExtra("outputX", 320);
		intent.putExtra("outputY", 320);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, 2);
	}

	/**
	 * ����ü�֮���ͼƬ����
	 * 
	 * @param picdata
	 */
	private void getImageToView(Intent data) {
		Bundle extras = data.getExtras();
		if (extras != null) {
			Bitmap photo = extras.getParcelable("data");
			/*ByteBuffer dst = ByteBuffer.allocate(photo.getByteCount());
			photo.copyPixelsToBuffer(dst);
			byte []a = dst.array();
			try {
				String b = new String(a, "UTF-8");
				System.out.println(b);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String s = photo.toString();*/
			String s = bitmapToBase64(photo);
			Constant.user.setProfile_img_data(s);
			Constant.user.setProfile_img_name("profile_"+Constant.user.getUserPhoneNum()+".jpg");
			BaseActivity.con.updateProfileImage();
			drawable = new BitmapDrawable(photo);
			iv_picture.setImageDrawable(drawable);
		}
	}
	
	public static String bitmapToBase64(Bitmap bitmap) {

		String result = null;
		ByteArrayOutputStream baos = null;
		try {
			if (bitmap != null) {
				baos = new ByteArrayOutputStream();
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

				baos.flush();
				baos.close();

				byte[] bitmapBytes = baos.toByteArray();
				result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (baos != null) {
					baos.flush();
					baos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}


}
