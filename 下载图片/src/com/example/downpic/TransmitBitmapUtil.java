package com.example.downpic;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.util.Base64;

public class TransmitBitmapUtil {
	SharedPreferences sPreferences;
	String mImageName;
	Bitmap mBitmap;
	private static final String SHAREDPREFERENCES_NAME =  "share_bitmap";
	public TransmitBitmapUtil(Context p_Context){
		sPreferences = p_Context.getSharedPreferences(SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
	}
	
	public void saveBitmap(Bitmap bitmap,String imageName){
		Editor editor = sPreferences.edit();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(CompressFormat.PNG, 50,baos);
		String imageBase64 = new String (Base64.encode(baos.toByteArray(), 0));
		editor.putString(imageName, imageBase64);
		editor.commit();
	}
	
	public Bitmap getBitmap(String imageName){
		String imageBase64 = sPreferences.getString(imageName, "");
		byte[] byte64 = Base64.decode(imageBase64, 0);
		ByteArrayInputStream bais = new ByteArrayInputStream(byte64);
		Bitmap bitmap = BitmapFactory.decodeStream(bais);
		return bitmap;
	}
}
