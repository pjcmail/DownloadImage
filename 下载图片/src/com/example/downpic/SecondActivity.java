package com.example.downpic;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class SecondActivity extends Activity implements OnClickListener ,Callback{
	
	private GridView mGridView;
	private MyGridViewAdapter adapter;
	private EditText text;
	private Button btn;
	private Handler mHandler;
	String picURL = "http://image.baidu.com/i?tn=baiduimagejson&ie=utf-8&ic=0&rn=20&pn=1&word=";
	private List<String> list;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_second);
		mGridView = (GridView) findViewById(R.id.gridview);
		text = (EditText) findViewById(R.id.second_edit);
		btn  =(Button) findViewById(R.id.second_btn);
		btn.setOnClickListener(this);
		mHandler = new Handler(this);
		mGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String url = (String)mGridView.getItemAtPosition(position);
				Intent intent = new Intent(SecondActivity.this,ThirdActivity.class);
				intent.putExtra("singer", url);
				startActivity(intent);
			}
		});
		
	}
	
	class MyGridViewAdapter extends BaseAdapter{
		
		private List<String> imageURLs;
		private Context mContext;
		
		public MyGridViewAdapter(Context context,List<String> list){
			imageURLs = list;
			mContext = context;
		}

		@Override
		public int getCount() {
			return imageURLs.size();
		}

		@Override
		public Object getItem(int position){
			return imageURLs.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_gridview,null);
			final ImageView view = (ImageView) convertView.findViewById(R.id.iv_imageview);
			BitmapUtils bitmapUtils = new BitmapUtils(SecondActivity.this);
			bitmapUtils.display(view, imageURLs.get(position),Util.getConfig(mContext, R.drawable.bibi));
			return convertView;
		}	
	}

	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.second_btn){
			downURL();
		}
	}

	private void downURL() {
		String name = text.getText().toString();
		try{
			picURL+=URLEncoder.encode(name, "utf-8");
			HttpUtils httpUtils = new HttpUtils();
			httpUtils.configCurrentHttpCacheExpiry(1000*5);
			httpUtils.send(HttpMethod.GET, picURL, new RequestCallBack<String>(){

				@Override
				public void onFailure(HttpException arg0, String arg1){
					Log.e("Second", "Failure");
				}
				@Override
				public void onSuccess(ResponseInfo<String> arg0){
					Log.e("Second", "Success");
					try {
						JSONObject object = new JSONObject(arg0.result);
						JSONArray mArray = object.getJSONArray("data");
						if(mArray.length()>1){
							list = new ArrayList<String>();
							for(int i=0;i<mArray.length()-1;i++){
								JSONObject oneObject = mArray.getJSONObject(i);
								String url = oneObject.getString("objURL");
								list.add(url);
								if(i==mArray.length()-2){
									mHandler.sendEmptyMessage(0);
									break;
								}
							}
						}
					} catch (JSONException e) {}
				}
			});
		} catch (UnsupportedEncodingException e) {}
	}

	@Override
	public boolean handleMessage(Message msg) {
		adapter = new MyGridViewAdapter(this, list);
		mGridView.setAdapter(adapter);
		return false;
	}

}
