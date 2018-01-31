package com.xin.tools;

import java.text.SimpleDateFormat;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;
import com.github.sarxos.webcam.WebcamUtils;
import com.github.sarxos.webcam.util.ImageUtils;

public class CaptureThread extends Thread {
	
	private static String currentTime;
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HH-mm-ss-S");
	private static final String url = "D://";
	private static String fileName;
	private Webcam webcam = Webcam.getDefault();
	private WebcamPanel panel = new WebcamPanel(webcam);
	
	
	
	public CaptureThread(){
		panel = new WebcamPanel(webcam);
		panel.setImageSizeDisplayed(true);
		panel.setMirrored(true);
	}

	/*
	 * 启动拍照
	 * */
	public void run(){	
		//调用线程时启动拍照，直到线程受到干预而结束
		while(true){
			currentTime = dateFormat.format(System.currentTimeMillis());//获取当前系统时间并格式化
			fileName = url+currentTime;//存放图片
			WebcamUtils.capture(webcam, fileName, ImageUtils.FORMAT_PNG);
		}
	}
	
	public Webcam getWebcam(){
		return this.webcam;
	}
	public WebcamPanel getWebcamPanel(){
		return this.panel;
	}
}
