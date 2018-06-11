package com.xin.tools;

import java.text.SimpleDateFormat;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamUtils;

/*
 * 线程类，控制拍照
 * 
 * 将 Webcam、WebcamPanel的实例化放到了frame中，方便对 Webcam、WebcamPanel的一些其他操作
 * */
public class CaptureRunnableOptimize implements Runnable{

	private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HH-mm-ss-S");
	
	//使用相对路径存储抓取的图片
	public final String PICTURE_URL = "../../PICTURES";
	private Webcam webcam;
	private WebcamPanel panel;
	private static String fileName;
	
	//控制线程循环的开关
	private boolean flag;
	public static Base base = new Base();
	
	public CaptureRunnableOptimize(Webcam webcam, WebcamPanel panel){
		
		//判断写的文件路径是否存在
		base.fileIsExists(PICTURE_URL);
		this.webcam = webcam;
		this.panel = panel;
	}
	/*
	 * 调用线程时启动拍照，直到线程停止
	 * 
	 * */
	public void run(){	
		while(flag){
			
			//存储的文件名称是格式化后的当前系统时间
			fileName = dateFormat.format(System.currentTimeMillis());
			WebcamUtils.capture(webcam,base.filePath(PICTURE_URL,fileName + ".png"));			
		}
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
