package com.xin.tools;

import java.awt.Color;
import java.text.SimpleDateFormat;

import javax.swing.BorderFactory;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamUtils;

/*
 * 线程类，控制拍照
 * 
 * */
public class CaptureRunnable implements Runnable {

	private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HH-mm-ss-S");
	public final String PICTURE_URL = "../../PICTURES";//使用相对路径存储抓取的图片
	private static String fileName;
	
	//控制线程循环的开关
	private boolean flag;
	private final Webcam webcam = Webcam.getDefault();
	private final WebcamPanel panel = new WebcamPanel(webcam);
	public static Base base = new Base();//Base工具类实例化
	
	public CaptureRunnable(){
		panel.setMirrored(true);
		panel.setFPSDisplayed(true);
		base.fileIsExists(PICTURE_URL);//保证存储路径可写
	}
	/*
	 * 调用线程时启动拍照，直到线程停止
	 * 
	 * */
	public void run(){	
		while(flag){
			fileName = dateFormat.format(System.currentTimeMillis());//存储的文件名称是格式化后的当前系统时间
			WebcamUtils.capture(webcam,base.filePath(PICTURE_URL,fileName+".png"));			
		}
	}
	/*
	 * 获取实例对象引用
	 * */
	public Webcam getWebcam(){
		return this.webcam;
	}
	/*
	 * 获取Panel对象
	 * */
	public WebcamPanel getWebcamPanel(){
		return this.panel;
	}
	/*
	 * 设置线程执行标记，控制线程启/停
	 * 
	 * */
	public boolean setFlag(boolean flag){
		this.flag = flag;
		return flag;
	}
}
