package com.stohelper.view;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.stohelper.util.BaseActivity;
import com.stohelper.util.Config;
import com.stohelper.util.Constant;
import com.stohelper.util.Tools;

public class ApplyForwarderActivity extends BaseActivity implements OnClickListener{

	private ImageView iv_idcard;
	private ImageButton btn_cancel, btn_submit;
	private EditText et_phone,et_ID;
	private Drawable drawable = null;
	
	private String[] items = new String[] { "ѡ�񱾵�ͼƬ", "����" };
	
	//������
	private static final int IMAGE_REQUEST_CODE = 0;
	private static final int CAMERA_REQUEST_CODE = 1;
	private static final int RESULT_REQUEST_CODE = 2;
	
	//ͷ������
	private static final String IMAGE_FILE_NAME = "faceImage.jpg";
	private ImageView faceImage;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_applyforwarder);
		
		btn_cancel = (ImageButton)findViewById(R.id.applyforwarder_btn_cancel);
		btn_submit = (ImageButton)findViewById(R.id.applyforwarder_btn_submit);
		et_phone = (EditText)findViewById(R.id.applyforwarder_et_phone);
		et_ID = (EditText)findViewById(R.id.applyforwarder_et_ID);
		iv_idcard = (ImageView)findViewById(R.id.applyforwarder_iv_idcard);
		
		et_phone.setText(Constant.user.getUserPhoneNum());
		et_ID.setText(Constant.user.getUserIDNum());
		
		btn_cancel.setOnClickListener(this);
		btn_submit.setOnClickListener(this);
		iv_idcard.setOnClickListener(this);
		
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.applyforwarder_btn_submit:
			if(!TextUtils.isEmpty(et_ID.getText().toString())){
				Constant.user.setUserIDNum(et_ID.getText().toString());
				BaseActivity.con.applyforwarder();
			}else {
				Toast.makeText(this, "���֤���벻��Ϊ��", 1).show();
    	}
			break;
		case R.id.applyforwarder_iv_idcard:
			showPictureSelectDialog();
			break;
		default:
			break;
		}
	}

	// ��ʾѡ��Ի���
	private void showPictureSelectDialog() {

		new AlertDialog.Builder(this)
				.setTitle("�ϴ���Ƭ")
				.setItems(items, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case 0:
							Intent intentFromGallery = new Intent();
							intentFromGallery.setType("image/*"); //�����ļ�����
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
					Toast.makeText(ApplyForwarderActivity.this, "δ�ҵ��洢�����޷��洢��Ƭ��",
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

	
	//�ü�ͼƬ����ʵ��
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

	//����ü�֮���ͼƬ����
	private void getImageToView(Intent data) {
		Bundle extras = data.getExtras();
		if (extras != null) {
			Bitmap photo = extras.getParcelable("data");
			String s = bitmapToBase64(photo);
			Constant.user.setIdcard_img_data(s);
			Constant.user.setIdcard_img_name(Constant.user.getUserPhoneNum()+"_idcard.bmp");
			BaseActivity.con.updateProfileImage();
			drawable = new BitmapDrawable(photo);
			
		    iv_idcard.setImageDrawable(drawable);
			
		}
	}
	
	@Override
	public void processMessage(Message message) {
		switch(message.what){
		case Config.REQUEST_FORWARDER_APPLY:
			int result = message.arg1;
			
			if(result == Config.SUCCESS){
				 Toast toast = Toast.makeText(ApplyForwarderActivity.this,"ת��Ա�����ѷ���", 1500);
				 toast.show();
				 Intent intent=new Intent(ApplyForwarderActivity.this,InformationActivity.class);
			     startActivity(intent);	
			}else{
				 Toast.makeText(this,"ת��Ա���뷢��ʧ��", 1500).show();
			}
			break;
		}
		
	}
	
	//bitmapת��ΪBase64��ʽ
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
