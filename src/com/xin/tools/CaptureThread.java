package com.xin.tools;

import java.text.SimpleDateFormat;
import java.io.File;
import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamUtils;
import com.github.sarxos.webcam.util.ImageUtils;

public class CaptureThread extends Thread {

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HH-mm-ss-S");
	private static String currentTime = dateFormat.format(System.currentTimeMillis());
	private static final String url = "../../logs";
	private static String fileName;
	private Webcam webcam = Webcam.getDefault();
	private WebcamPanel panel = new WebcamPanel(webcam);
	
	
	
	public CaptureThread(){
		panel = new WebcamPanel(webcam);
		panel.setImageSizeDisplayed(true);
		panel.setMirrored(true);
		fileIsExists(url);//构造时调用，保证存储路径正确
	}

	/*
	 * 启动拍照
	 * */
	public void run(){	
		//调用线程时启动拍照，直到线程受到干预而结束
		while(true){
			//currentTime = dateFormat.format(System.currentTimeMillis());//获取当前系统时间并格式化
			fileName = url+currentTime;//存放图片
			WebcamUtils.capture(webcam, fileName, ImageUtils.FORMAT_PNG);
		}
	}
	//获取实例对象引用
	public Webcam getWebcam(){
		return this.webcam;
	}
	public WebcamPanel getWebcamPanel(){
		return this.panel;
	}
	
	//判断文件路径/文件是否存在
	public static boolean fileIsExists(String path) {
		File file = new File(path);
		if(!file.exists()) {
			file.mkdirs();
			return true;
		}else {
			return true;
		}
	}
}
