package com.xin.tools;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.junit.Test;

/*
 * @description 基础工具类
 * @author zhangxiaolong
 * @date 2018-4
 * */
public class Base {
	
	//最终计算结果/错误码
	private int calculateResult;
	
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
			System.out.println("文件不存在，俺给你新建了一个。");
			return true;
		}else {
			System.out.println("文件已经存在："+path);
			System.out.println("当前工程路径:"+System.getProperty("user.dir"));
			return true;
		}
	}

	/* @param 两个String类型文件名称
	 * @return calculateResult，int 类型计算结果/错误状态码
	 * 过滤出文件名中的所有数字
	 * 将时、分、秒、毫秒 按照存储的文件名格式分别截取出来
	 * 计算时分秒的时间差并以 毫秒 输出
	 * 
	 * 考虑到实际使用场景所以没有对 年、月、日 进行判断
	 * 更改CaptureRunnable类中的文件名称格式会导致此方法异常！！！！
	 * */
	public int calculateDispose(String strFirstPic,String strLastPic){
		
		//接收string，转换为char[]
		char[] charFirstPic = null;
		char[] charLastPic = null;
		
		//判断文件是否已经选择
		if(strFirstPic == null || strLastPic == null || strFirstPic.length()<=0 || strLastPic.length()<=0){
			
			//未选择文件，直接返回：0
			calculateResult = 0;
			return calculateResult;
		}else{
			
			//将字符串转换成字符数组
			charFirstPic = strFirstPic.toCharArray();
			charLastPic = strLastPic.toCharArray();
		}

		StringBuffer strBufferFirstPic = new StringBuffer();
		StringBuffer strBufferLastPic = new StringBuffer();
		
		//遍历第一个文件名，剔除非数字
		for(int i=0;i<charFirstPic.length;i++){ 
			if(Character.isDigit(charFirstPic[i])){
				strBufferFirstPic.append(charFirstPic[i]);
			} 
		}
		
		//遍历第二个文件名，剔除非数字
		for(int j=0;j<charLastPic.length;j++){  
			if(Character.isDigit(charLastPic[j])){
				strBufferLastPic.append(charLastPic[j]);
			}
		}
		
		//格式化文件名，删除YYYYMMDD，保留hhmmssS
		strBufferFirstPic = strBufferFirstPic.delete(0,8);
		strBufferLastPic = strBufferLastPic.delete(0, 8);
		
		//截取第一张图片文件名称中的：小时、分钟、秒、毫秒
		int hhFirstPic = Integer.parseInt(strBufferFirstPic.substring(0,2));
		int mmFirstPic = Integer.parseInt(strBufferFirstPic.substring(2,4));
		int ssFirstPic = Integer.parseInt(strBufferFirstPic.substring(4,6));
		int msFirstPic = Integer.parseInt(strBufferFirstPic.substring(6,strBufferFirstPic.length()));
		
		//截取第二张图片文件名称中的：小时、分钟、秒、毫秒
		int hhLastPic = Integer.parseInt(strBufferLastPic.substring(0,2));
		int mmLastPic = Integer.parseInt(strBufferLastPic.substring(2,4));
		int ssLastPic = Integer.parseInt(strBufferLastPic.substring(4,6));
		int msLastPic = Integer.parseInt(strBufferLastPic.substring(6,strBufferLastPic.length()));

		//判断两个文件的合法性：-1，文件先后顺序错误;-2，两个文件相同
		if(hhLastPic<hhFirstPic || mmLastPic<mmFirstPic&&hhLastPic==hhFirstPic || ssLastPic<ssFirstPic&&mmLastPic==mmFirstPic){ //文件先后顺序错误
			
			calculateResult = -1;
		}else if (hhLastPic==hhFirstPic && mmLastPic==mmFirstPic && ssLastPic==ssFirstPic && msLastPic==msFirstPic) {  //文件名相同
			
			calculateResult = -2;	
		}else{
			//如果两个文件合法，计算两张图片的时间差
			calculateResult = (hhLastPic-hhFirstPic)*60*60*1000  //小时 换算 毫秒
							+ (mmLastPic-mmFirstPic)*60*1000  //分钟 换算 毫秒
							+ ((ssLastPic*1000)+msLastPic)-((ssFirstPic*1000)+msFirstPic);//将 秒&&毫秒 合并计算	
		}
		return calculateResult;
	}
	
	/*
	 * @return String[] 文件属性
	 * 
	 * 文件选择器实现文件选择
	 * 过滤文件格式
	 * 处理异常
	 * */
	public String[] fileChooser(){
		
		//默认打开图片保存路径
		JFileChooser chooser = new JFileChooser(new CaptureRunnable().PICTURE_URL);
		FileNameExtensionFilter filter = new FileNameExtensionFilter("just PNG format", "png");
		
		//过滤，只显示PNG格式文件
		chooser.setFileFilter(filter);
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		
		//数组保存文件path、name
		String[] fileAttribute = new String[2];
		
		//文件选择的状态
		int returnState = chooser.showOpenDialog(null);
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
