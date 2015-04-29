package com.stohelper.util;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Vector;

import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.text.InputFilter.LengthFilter;

public class Tools {
	public static byte[] getJASONLength(JSONObject jo){
		byte[] data = null;
		
		try {
			data = jo.toString().getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		byte[] length = new byte[] {  
		        (byte) ((data.length >> 24) & 0xFF),  
		        (byte) ((data.length >> 16) & 0xFF),     
		        (byte) ((data.length >> 8) & 0xFF),     
		        (byte) (data.length & 0xFF)  
		    };
		
		return length;
	}
	
	public static int getJSONLength(byte[] lengthData){
		int length = lengthData[3] & 0xFF |  
	            (lengthData[2] & 0xFF) << 8 |  
	            (lengthData[1] & 0xFF) << 16 |  
	            (lengthData[0] & 0xFF) << 24;
		
		return length;
	}
	
	public static JSONObject getJASONObject(int length, byte[] data){
		JSONObject jo = null;
		try {
			jo = new JSONObject(new String(data, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return jo;
	}
	
	public static boolean hasSdcard(){
		String state = Environment.getExternalStorageState();
		if(state.equals(Environment.MEDIA_MOUNTED)){
			return true;
		}else{
			return false;
		}
	}
	
	//获取网络图片资源
    public static Bitmap getHttpBitmap(String url) {
        URL myFileURL;
        Bitmap bitmap = null;
        try {
                myFileURL = new URL(url);
                // 获得连接
                HttpURLConnection conn = (HttpURLConnection) myFileURL
                                .openConnection();
                // 设置超时时间为6000毫秒，conn.setConnectionTiem(0);表示没有时间限制
                conn.setConnectTimeout(6000);
                // 连接设置获得数据流
                conn.setDoInput(true);
                // 不使用缓存
                conn.setUseCaches(false);
                // 这句可有可无，没有影响
                 conn.connect();
                // 得到数据流
                InputStream is = conn.getInputStream();
                // 解析得到图片
                bitmap = BitmapFactory.decodeStream(is);
                // 关闭数据流
                is.close();
        } catch (Exception e) {
                e.printStackTrace();
        }

        return bitmap;

    }
}