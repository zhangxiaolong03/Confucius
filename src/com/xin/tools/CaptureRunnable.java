package com.xin.tools;

import java.text.SimpleDateFormat;
import java.io.File;

import org.omg.CORBA.Request;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamUtils;
import com.github.sarxos.webcam.util.ImageUtils;

public class CaptureRunnable implements Runnable {

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HH-mm-ss-S");
	private static String currentTime;
	private static final String PICTURE_URL = "../../PICTURES";//使用相对路径存储抓取的图片
	private static String fileName;
	private Webcam webcam = Webcam.getDefault();
	private WebcamPanel panel = new WebcamPanel(webcam);
	
	public CaptureRunnable(){
		panel = new WebcamPanel(webcam);
		panel.setImageSizeDisplayed(true);
		panel.setMirrored(true);
		fileIsExists(PICTURE_URL);//构造时调用，保证存储路径可写
	}

	/*
	 * 启动拍照
	 * */
	public void run(){	
		//调用线程时启动拍照，直到线程停止
		while(true){
			currentTime = dateFormat.format(System.currentTimeMillis());//获取当前系统时间并格式化
			fileName = PICTURE_URL + currentTime;//存放图片
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
	
	//判断文件路径||文件是否存在，不存在时创建文件路径
	public static boolean fileIsExists(String path) {
		File file = new File(path);
		if(!file.exists()) {
			file.mkdirs();
			System.out.println("文件不存在，新建一个。");
			return true;
		}else {
			System.out.println("文件已存在："+path);
			System.out.println("当前工程路径:"+System.getProperty("user.dir"));
			return true;
		}
	}
}
