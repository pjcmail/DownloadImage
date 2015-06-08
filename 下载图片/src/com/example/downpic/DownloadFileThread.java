package com.example.downpic;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.RandomAccessFile;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import android.R.integer;

public class DownloadFileThread extends Thread{
	private static final int BUFFER_SIZE=1024;
	private URL mURL;
	private File mFile;
	private  boolean mIsFinished;
	private  int mNowDownloadSize = 0;
	public DownloadFileThread(String p_URL,String path){
		try {
			mURL = new URL(p_URL);
		} catch (MalformedURLException e) {}
		mFile = new File(path);
		mIsFinished = false;
	}
	@Override
	public void run(){
		BufferedInputStream inputStream = null;
		RandomAccessFile accessFile = null;
		byte buf[] = new byte[BUFFER_SIZE];
		URLConnection con = null;
		try {
			con = mURL.openConnection();
			con.setAllowUserInteraction(true);
			accessFile = new RandomAccessFile(mFile, "rwd");
			accessFile.setLength(con.getContentLength());
			inputStream = new BufferedInputStream(con.getInputStream());
			int len = 0;
			while((len = inputStream.read(buf))!=-1){
				mNowDownloadSize += len;
				accessFile.write(buf, 0, len);
			}
			mIsFinished = true;
			inputStream.close();
			accessFile.close();
		} catch (Exception e){}
	}
	
	public  boolean ssFinished(){
		return mIsFinished;
	}
	public  int getNowDownloadSize(){
		return mNowDownloadSize;
	}
}
