package com.xin.tools;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.junit.Test;

/*
 * @description 基础工具类
 * @author zhangxiaolong
 * @date 2018-4
 * */
public class Base {
	private int calculateResult;//最终计算结果/错误码
	
	//处理文件存放路径
	public File filePath(String path,String fileName){
		File file = new File(path,fileName);
		return file;
	}
	
	//判断文件路径||文件是否存在，不存在时创建文件路径
	public boolean fileIsExists(String path) {
		File file = new File(path);
		if(!file.exists()) {
			file.mkdirs();
			System.out.println("文件不存在，新建了一个。");
			return true;
		}else {
			System.out.println("文件已存在："+path);
			System.out.println("当前工程路径:"+System.getProperty("user.dir"));
			return true;
		}
	}

	//************更改CaptureRunnable类中的文件名称格式会导致此方法异常！！！！*************
	/* @input 两个String类型文件名称
	 * @return calculateResult，int 类型计算结果/错误状态码
	 * 过滤出文件名中的所有数字
	 * 将时、分、秒、毫秒 按照存储的文件名格式分别截取出来
	 * 计算时分秒的时间差并以 毫秒 输出
	 * */
	public int calculateDispose(String strFirstPic,String strLastPic){
		char[] charFirstPic = null;//接收传入的string，转换为char[]
		char[] charLastPic = null;
		//判断文件是否已经选择
		if(strFirstPic == null || strLastPic == null || strFirstPic.length()<=0 || strLastPic.length()<=0){
			calculateResult = 0;//0作为状态码返回，供前端判断
			return calculateResult;
		}else{
			charFirstPic = strFirstPic.toCharArray();//将字符串转换成字符数组
			charLastPic = strLastPic.toCharArray();
		}

		StringBuffer strBufferFirstPic = new StringBuffer();
		StringBuffer strBufferLastPic = new StringBuffer();
		for(int i=0;i<charFirstPic.length;i++){ //遍历第一个文件名，剔除非数字
			if(Character.isDigit(charFirstPic[i])){
				strBufferFirstPic.append(charFirstPic[i]);
			} 
		}
		for(int j=0;j<charLastPic.length;j++){  //遍历第二个文件名，剔除非数字
			if(Character.isDigit(charLastPic[j])){
				strBufferLastPic.append(charLastPic[j]);
			}
		}
		strBufferFirstPic = strBufferFirstPic.delete(0,8);//删除string串中的YYYYMMDD，保留hhmmss
		strBufferLastPic = strBufferLastPic.delete(0, 8);
		int hhFirstPic = Integer.parseInt(strBufferFirstPic.substring(0,2));//截取第一个图片文件名称中的：小时
		int mmFirstPic = Integer.parseInt(strBufferFirstPic.substring(2,4));//截取第一个图片文件名称中的：分钟
		int ssFirstPic = Integer.parseInt(strBufferFirstPic.substring(4,6));//截取第一个图片文件名称中的：秒
		int msFirstPic = Integer.parseInt(strBufferFirstPic.substring(6,strBufferFirstPic.length()));//截取第一个图片文件名称中的：毫秒。strBuffer.length()保证能取到字符串的最后一位
		
		int hhLastPic = Integer.parseInt(strBufferLastPic.substring(0,2));//截取第二个图片文件名称中的：小时
		int mmLastPic = Integer.parseInt(strBufferLastPic.substring(2,4));//截取第二个图片文件名称中的：分钟
		int ssLastPic = Integer.parseInt(strBufferLastPic.substring(4,6));//截取第二个图片文件名称中的：秒
		int msLastPic = Integer.parseInt(strBufferLastPic.substring(6,strBufferLastPic.length()));//截取第二个图片文件名称中的：毫秒

		//判断两个文件的合法性：1.先后顺序是否正确;2.是否是同一个文件
		
		
		if(hhLastPic<hhFirstPic || mmLastPic<mmFirstPic || ssLastPic<ssFirstPic){
			System.out.println("文件顺序错误，请按先后顺序选择。");
			calculateResult = -1;//返回状态码，前端根据状态码进行错误提示
			
		}else if (hhLastPic==hhFirstPic && mmLastPic==mmFirstPic && ssLastPic==ssFirstPic && msLastPic==msFirstPic) {
			System.out.println("两个文件不能相同，请重新选择。");
			calculateResult = -2;//返回状态码，前端根据状态码进行错误提示
			
		}else{
			//如果是两个合法文件，计算两张图片的时间差
			calculateResult = (hhLastPic-hhFirstPic)*60*60*1000  //小时 换算 毫秒
							+ (mmLastPic-mmFirstPic)*60*1000  //分钟 换算 毫秒
							+ ((ssLastPic*1000)+msLastPic)-((ssFirstPic*1000)+msFirstPic);//将 秒&&毫秒 合并计算
			System.out.println("总共用时："+calculateResult+"毫秒");		
		}
		return calculateResult;
	}
	
	/*
	 * 文件选择器
	 * 实现文件选择功能
	 * 过滤文件格式
	 * 处理异常
	 * @return String 文件名
	 * */
	public String[] fileChooser(){
		JFileChooser chooser = new JFileChooser(new CaptureRunnable().PICTURE_URL);//默认打开图片保存路径
		FileNameExtensionFilter filter = new FileNameExtensionFilter("PNG格式图片", "png");
		chooser.setFileFilter(filter);//过滤，只显示PNG格式文件
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		String[] fileAttribute = new String[2];//数组保存文件path、name
		int returnState = chooser.showOpenDialog(null);//文件选择的状态
		System.out.println("returnState == "+returnState);
		if(returnState == JFileChooser.APPROVE_OPTION){ 
			File file = chooser.getSelectedFile();
			String name = file.getName().toString();
			String path = file.getPath().toString();
			fileAttribute[0] = name;
			fileAttribute[1] = path;
		}else {
			chooser.cancelSelection();
			fileAttribute[0] = null;
			fileAttribute[1] = null;
		}
		
		return fileAttribute;
	}
}
