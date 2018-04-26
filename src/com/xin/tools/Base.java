package com.xin.tools;

import java.io.File;

/*
 * @description 基础工具类
 * @author zhangxiaolong
 * @date 2018-4
 * */
public class Base {

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
}
