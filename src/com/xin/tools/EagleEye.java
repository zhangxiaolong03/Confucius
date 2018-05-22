package com.xin.tools;

import java.awt.Font;
import java.awt.Image;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JFileChooser;//读取文件
import javax.swing.ImageIcon;//显示图片

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;

/*
 * @description UI界面，进行前端交互
 * @author zhangxiaolong
 * @date 2018-4
 * */
public class EagleEye extends JFrame {

	protected static CaptureRunnable capRunnable;
	protected static Thread thread;
	public JPanel componentsPanel = new JPanel();
	public JButton btnStartCapture,btnStopCapture,btnPicture_1,btnPicture_2,btnCalculate;
	public JLabel jlbPictureSelect_1,jlbPictureSelect_2,jlbPicturePath_1,jlbPicturePath_2,jlbCalculateResult
		,jlbPicture_1,jlbPicture_2,jlbUseless;
	public JFileChooser fileChooser;
	public String firstPicName,lastPicName;
	public static Base base = new Base();
	
	public EagleEye() {
		//启动窗体
		try {
			createFrame();
			//initFrame();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/*
	 * 初始化窗体
	 * 1.绘制界面及控件
	 * 2.对控件进行监听
	 * 
	 * */
	
	public void initFrame(){
		this.setTitle("高速相机 v1.0");
		this.setResizable(false);
		this.setSize(1100, 1800);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		btnStartCapture = new JButton("开始抓拍");
		btnStopCapture = new JButton("停止抓拍");
		btnPicture_1 = new JButton("首张图片");
		btnPicture_2 = new JButton("末尾图片");
		jlbPictureSelect_1 = new JLabel("图片-1",JLabel.CENTER);
		jlbPictureSelect_2 = new JLabel("图片-2",JLabel.CENTER);
		jlbPicturePath_1 = new JLabel();//显示选择的文件名称，默认空白
		jlbPicturePath_2 = new JLabel();//显示选择的文件名称，默认空白
		jlbCalculateResult = new JLabel("计算结果展示区",JLabel.CENTER);//显示计算结果，文本设置 居中
		btnCalculate = new JButton("<html>计<br>算</html>");
		jlbPicture_1 = new JLabel();//显示图片预览
		jlbPicture_2 = new JLabel();
		
		jlbUseless = new JLabel();//该标签作用是占据元素列表的最后一个位置，解决最后一个元素无法按设定的bounds展示问题
		
		
		capRunnable = new CaptureRunnable();// 线程类CaptureRunnable
		thread = new Thread(capRunnable);
		
		capRunnable.getWebcamPanel().setLayout(null);//清空WebcamPanel
		this.add(capRunnable.getWebcamPanel(),BorderLayout.WEST);
		
		this.add(btnCalculate).setBounds(1040,110,50,150);
		
		this.add(btnStartCapture).setBounds(700,40,100,50);
		this.add(btnStopCapture).setBounds(850,40,100,50);
		
		this.add(btnPicture_1).setBounds(700,110,100,50);
		this.add(jlbPictureSelect_1).setBounds(810,110,45,50);
		this.add(jlbPicturePath_1).setBounds(860,110,170,50);
		jlbPictureSelect_1.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		jlbPicturePath_1.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		
		this.add(btnPicture_2).setBounds(700,210,100,50);
		this.add(jlbPictureSelect_2).setBounds(810,210,45,50);
		this.add(jlbPicturePath_2).setBounds(860,210,170,50);
		jlbPictureSelect_2.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		jlbPicturePath_2.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		
		this.add(jlbCalculateResult).setBounds(700,265,390,100);
		jlbCalculateResult.setFont(new Font("DIALOG",1,30));//设置label中字号
		jlbCalculateResult.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		
		this.add(jlbPicture_1).setBounds(700,380,195,200);
		jlbPicture_1.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		this.add(jlbPicture_2).setBounds(898,380,195,200);
		jlbPicture_2.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		
		this.add(jlbUseless).setBounds(860,210,200,50);

		/*
		 * @description 以下方法处理组件操作事件
		 * */
		// 建立startCapture按钮的监听
		btnStartCapture.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// 判断线程对象是否为空，如果为空则创建新的线程对象并启动
				capRunnable.setFlag(true);// 设置线程循环：true
				if (thread.isAlive()) {
					thread.start();
				} else {
					new Thread(capRunnable).start();
				}
			}
		});
		// 建立stopCapture按钮的监听
		btnStopCapture.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				capRunnable.setFlag(false);// 设置线程循环：false
				
			}
		});		
		//建立btnSelectFirst按钮的监听
		btnPicture_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String[] fileAttribute = base.fileChooser();//接收fileChooser()回传的文件path、name
				if(fileAttribute[0] != null && fileAttribute[1] != null){ //文件name、path都不能为空
					jlbPicturePath_1.setText(fileAttribute[0]);	
					ImageIcon imageIcon = new ImageIcon(fileAttribute[1]);
					imageIcon = new ImageIcon(imageIcon.getImage().getScaledInstance(195, 200, Image.SCALE_DEFAULT));
					jlbPicture_1.setIcon(imageIcon);
					firstPicName = fileAttribute[0];//calculateDispose()计算时需要传入的文件名
				}else { 
					jlbPicturePath_1.updateUI();
					jlbPicture_1.updateUI();
				}
			}		
		});
		//建立btnSelectLast按钮的监听
		btnPicture_2.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				String[] fileAttribute = base.fileChooser();//接收fileChooser()回传的文件path、name
				if(fileAttribute[0]!=null && fileAttribute[1]!=null){
					jlbPicturePath_2.setText(fileAttribute[0]);
					ImageIcon imageIcon = new ImageIcon(fileAttribute[1]);
					imageIcon = new ImageIcon(imageIcon.getImage().getScaledInstance(195, 200, Image.SCALE_DEFAULT));
					jlbPicture_2.setIcon(imageIcon);
					lastPicName = fileAttribute[0];//calculateDispose()计算时需要传入的文件名
				}else {
					jlbPicturePath_2.updateUI();
					jlbPicture_2.updateUI();
				}
			}
		});
		//建立btnCalculate按钮的监听
		btnCalculate.addActionListener(new ActionListener() {	
			public void actionPerformed(ActionEvent e) {
				// 处理逻辑：判断两张图片先后顺序是否正确、是否是同一张照片
				int calculateResult = base.calculateDispose(firstPicName, lastPicName);
				if(calculateResult == 0){
					jlbCalculateResult.setText("文件不能为空，请选择文件");
				}else if(calculateResult == -1){
					jlbCalculateResult.setText("文件顺序错误，请按先后顺序选择");
				}else if(calculateResult == -2){
					jlbCalculateResult.setText("两个文件不能相同，请重新选择");
				}else{
					jlbCalculateResult.setBackground(Color.GREEN);
					jlbCalculateResult.setText("用时："+calculateResult+"毫秒");
				}
			}
		});
	}
//------------------------------↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑----------------------------↑	
	
	/*
	 * 另一个窗体，主要对UI进行优化
	 * 
	 * */
	public void createFrame() {	
		this.setTitle("高速相机 v2.0");
		
		//禁用frame的【最大化】
		this.setResizable(false);
		
		//设置frame展示时的默认坐标轴
		this.setLocation(150, 0);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		Webcam webcam =Webcam.getDefault();
		WebcamPanel panel = new WebcamPanel(webcam);
		
		//字体 设置
		Font font = new Font("宋体",Font.BOLD,15);
		
		btnStartCapture = new JButton("开始抓拍");
		btnStopCapture = new JButton("停止抓拍");
		btnPicture_1 = new JButton("选择首图");
		btnPicture_2 = new JButton("选择末图");
//		jlbPictureSelect_1 = new JLabel("图 1",JLabel.CENTER);
//		jlbPictureSelect_2 = new JLabel("图 2",JLabel.CENTER);
		jlbPicturePath_1 = new JLabel();//显示选择的文件名称，默认空白
		jlbPicturePath_2 = new JLabel();//显示选择的文件名称，默认空白
		jlbCalculateResult = new JLabel("计算结果展示区",JLabel.CENTER);//显示计算结果，文本设置 居中
		btnCalculate = new JButton("<html>计<br>算</html>");
		
		//用label显示图片预览
		jlbPicture_1 = new JLabel();
		jlbPicture_2 = new JLabel();
		
		final CaptureRunnableOptimize capRunnableOptimize = new CaptureRunnableOptimize(webcam, panel);// 线程类CaptureRunnable
		thread = new Thread(capRunnableOptimize);
		
		this.setLayout(null);
		this.setSize(1040,800);
		this.setVisible(true);
		this.add(panel).setBounds(0,0,550,550);
		panel.setMirrored(true);
		panel.setFPSDisplayed(true);
		
		this.add(btnCalculate).setBounds(940,90,50,150);
		btnCalculate.setFont(font);
		
		this.add(btnStartCapture).setBounds(600,20,100,50);
		btnStartCapture.setFont(font);
		this.add(btnStopCapture).setBounds(750,20,100,50);
		btnStopCapture.setFont(font);
		
		this.add(btnPicture_1).setBounds(600,90,100,50);
		btnPicture_1.setFont(font);
//		this.add(jlbPictureSelect_1).setBounds(810,110,45,50);
		this.add(jlbPicturePath_1).setBounds(710,90,220,50);
		jlbPicturePath_1.setFont(font);
//		jlbPictureSelect_1.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		jlbPicturePath_1.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		
		this.add(btnPicture_2).setBounds(600,190,100,50);
		btnPicture_2.setFont(font);
//		this.add(jlbPictureSelect_2).setBounds(810,210,45,50);
		this.add(jlbPicturePath_2).setBounds(710,190,220,50);
		jlbPicturePath_2.setFont(font);
//		jlbPictureSelect_2.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		jlbPicturePath_2.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		
		this.add(jlbCalculateResult).setBounds(600,245,390,60);
		
		//设置label中字体、字号
		jlbCalculateResult.setFont(new Font("宋体",Font.ITALIC,26));
		jlbCalculateResult.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		
		this.add(jlbPicture_1).setBounds(570,330,220,220);
		jlbPicture_1.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		this.add(jlbPicture_2).setBounds(800,330,220,220);
		jlbPicture_2.setBorder(BorderFactory.createLineBorder(Color.GRAY));

		/*
		 * @description 以下方法处理组件操作事件
		 * */
		// 建立startCapture按钮的监听
		btnStartCapture.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// 判断线程对象是否为空，如果为空则创建新的线程对象并启动
				capRunnableOptimize.setFlag(true);// 设置线程循环：true
				if (thread.isAlive()) {
					thread.start();
				} else {
					new Thread(capRunnableOptimize).start();
				}
			}
		});
		// 建立stopCapture按钮的监听
		btnStopCapture.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				capRunnableOptimize.setFlag(false);// 设置线程循环：false
				
			}
		});		
		//建立btnSelectFirst按钮的监听
		btnPicture_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				//接收fileChooser()回传的文件path、name
				String[] fileAttribute = base.fileChooser();
				
				//文件name、path都不能为空
				if(fileAttribute[0] != null && fileAttribute[1] != null){ 
					jlbPicturePath_1.setText(fileAttribute[0]);	
					ImageIcon imageIcon = new ImageIcon(fileAttribute[1]);
					imageIcon = new ImageIcon(imageIcon.getImage().getScaledInstance(220, 220, Image.SCALE_DEFAULT));
					jlbPicture_1.setIcon(imageIcon);
					firstPicName = fileAttribute[0];//calculateDispose()计算时需要传入的文件名
				}else { 
					jlbPicturePath_1.updateUI();
					jlbPicture_1.updateUI();
				}
			}		
		});
		//建立btnSelectLast按钮的监听
		btnPicture_2.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				
				//接收fileChooser()回传的文件path、name
				String[] fileAttribute = base.fileChooser();
				if(fileAttribute[0]!=null && fileAttribute[1]!=null){
					jlbPicturePath_2.setText(fileAttribute[0]);
					ImageIcon imageIcon = new ImageIcon(fileAttribute[1]);
					imageIcon = new ImageIcon(imageIcon.getImage().getScaledInstance(220, 220, Image.SCALE_DEFAULT));
					jlbPicture_2.setIcon(imageIcon);
					
					//calculateDispose()计算时需要传入的文件名
					lastPicName = fileAttribute[0];
				}else {
					jlbPicturePath_2.updateUI();
					jlbPicture_2.updateUI();
				}
			}
		});
		//建立btnCalculate按钮的监听
		btnCalculate.addActionListener(new ActionListener() {	
			public void actionPerformed(ActionEvent e) {
				// 处理逻辑：判断两张图片先后顺序是否正确、是否是同一张照片
				int calculateResult = base.calculateDispose(firstPicName, lastPicName);
				if(calculateResult == 0){
					jlbCalculateResult.setText("文件不能为空，请选择文件");
				}else if(calculateResult == -1){
					jlbCalculateResult.setText("文件顺序错误，请按先后顺序选择");
				}else if(calculateResult == -2){
					jlbCalculateResult.setText("两个文件不能相同，请重新选择");
				}else{
					jlbCalculateResult.setBackground(Color.GREEN);
					jlbCalculateResult.setText("用时："+calculateResult+"毫秒");
				}
			}
		});
	}
	
	public static void main(String[] args) throws IOException {
		new EagleEye().setVisible(true);
	}
}
