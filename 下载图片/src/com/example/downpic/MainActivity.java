package com.example.downpic;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class MainActivity extends Activity implements OnClickListener{

	Button btn,btn_next;
	ImageView mView;
	EditText text ;
	String picURL = "http://image.baidu.com/i?tn=baiduimagejson&ie=utf-8&ic=0&rn=20&pn=1&word=";
	JSONArray mArray;
	String rootPath = Environment.getExternalStorageDirectory().getAbsolutePath();
	private int index = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		btn = (Button) findViewById(R.id.btn);
		btn_next = (Button) findViewById(R.id.btn_next);
		text = (EditText) findViewById(R.id.name);
		mView  = (ImageView) findViewById(R.id.imageView);
		btn.setOnClickListener(this);
		btn_next.setOnClickListener(this);
		File file = new File(rootPath+"/pic/");
		if(!file.exists()){
			file.mkdirs();
		}
	}

	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.btn){
			downFirst();
		}else if (v.getId()==R.id.btn_next) {
			downNext();
		}
	}
	private void downNext() {
		index++;
		if(index<mArray.length()){
			try {
				JSONObject object = mArray.getJSONObject(index);
				String url = object.getString("objURL");
				String type = object.getString("type");
				showPic(url);
				downPicToSDCard(url, type);
			} catch (JSONException e) {}
		}
	}
	private void showPic(String url) {
		BitmapUtils bitmapUtils = new BitmapUtils(MainActivity.this);
		bitmapUtils.display(mView, url);
	}
	
	private void downPicToSDCard(String url,String pic_type){
		String path=rootPath+"/pic/"+text.getText().toString()+index+"."+pic_type;
			DownloadFileThread thread = new DownloadFileThread(url,path);
			thread.start();	
	}
	private void downFirst() {
		String name = text.getText().toString();
		try{
			picURL+=URLEncoder.encode(name, "utf-8");
			HttpUtils httpUtils = new HttpUtils();
			httpUtils.configCurrentHttpCacheExpiry(1000*5);
			httpUtils.send(HttpMethod.GET, picURL, new RequestCallBack<String>(){

				@Override
				public void onFailure(HttpException arg0, String arg1){
					
				}
				@Override
				public void onSuccess(ResponseInfo<String> arg0){
					try {
						JSONObject object = new JSONObject(arg0.result);
						mArray = object.getJSONArray("data");
						if(mArray.length()>1){
							index = 0;
							JSONObject oneObject = mArray.getJSONObject(index);
							String url = oneObject.getString("objURL");
							String type = oneObject.getString("type");
							showPic(url);
							downPicToSDCard(url, type);
						}
					} catch (JSONException e) {}
				}
			});
		} catch (UnsupportedEncodingException e) {}
	}
}
