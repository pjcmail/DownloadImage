package com.example.downpic;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import com.lidroid.xutils.BitmapUtils;

public class ThirdActivity extends Activity{
	ImageView view;
	View bgView;
	private Button mButton;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_third);
		view  = (ImageView) findViewById(R.id.third_image);
		bgView = findViewById(R.id.RelativeLayout3);
		mButton = (Button) findViewById(R.id.btn_3);
		String url = getIntent().getStringExtra("singer");
		BitmapUtils bitmapUtils = new BitmapUtils(this);
		bitmapUtils.display(view, url,Util.getConfig(this, R.drawable.bibi));
		mButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Bitmap bitmap = ((BitmapDrawable)view.getDrawable()).getBitmap();
				bgView.setBackground(new BitmapDrawable(bitmap));
				view.setVisibility(View.GONE);
			}
		});
		
	}
	
	
	
	
	
	private void test() {
//		TransmitBitmapUtil bitmapUtil = new TransmitBitmapUtil(this);
//		Bitmap bitmap = bitmapUtil.getBitmap("singer");
		Bitmap bitmap = getIntent().getParcelableExtra("bitmap");
		Matrix matrix = new Matrix();
		matrix.postScale(2.0f,2.0f);
		Bitmap bit = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
		if(bit!=null)
			view.setImageBitmap(bit);
	}

}
