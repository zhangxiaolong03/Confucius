package com.xin.tools;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Label;
import java.awt.Rectangle;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.dnd.DropTarget;//窗体拖动
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JFileChooser;//读取文件
import javax.swing.ImageIcon;//显示图片
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.plaf.LabelUI;

/*
 * @description 界面框架，进行前端交互
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
		try {
			//createFrame();// 调用Frame
			initFrame();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
//------------------------使用GridBagConstraints绘制窗口以及布局---------------------------↓
	
	public void initFrame(){
		this.setTitle("高速相机 v2.0");
		this.setResizable(false);
		this.setSize(1100, 2048);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setBackground(Color.WHITE);
		
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
					firstPicName = fileAttribute[0];//calculateDispose()计算需要传入 文件名
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
					lastPicName = fileAttribute[0];//calculateDispose()计算需要传入 文件名
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
	
	// 绘制窗口,监听按钮
	public void createFrame() {	
		this.setTitle("高速相机 v1.0");
		this.setResizable(true);
		this.setVisible(true);
		this.setSize(1024, 1024);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		componentsPanel.setLayout(null);
		btnStartCapture = new JButton("\u5f00\u59cb");//"开始"
		componentsPanel.add(btnStartCapture);
		btnStartCapture.setBounds(500,500, 150, 30);
		
		btnStopCapture = new JButton("\u505c\u6b62");//"停止"
		componentsPanel.add(btnStopCapture);
		btnStopCapture.setBounds(800,500, 300, 30);
		
		
		jlbPictureSelect_1 = new JLabel("第一帧");	
		componentsPanel.add(jlbPictureSelect_1);
		btnPicture_1 = new JButton("选择");
		componentsPanel.add(btnPicture_1);
		btnPicture_1.setBounds(700,600,150,30);

		jlbPictureSelect_2 = new JLabel("最后帧");
		componentsPanel.add(jlbPictureSelect_2);
		jlbPictureSelect_1.setBounds(500,600,150,30);
		btnPicture_2 = new JButton("选择");
		componentsPanel.add(btnPicture_2);
		
		JLabel jlbFile_1 = new JLabel("图片1：");
		componentsPanel.add(jlbFile_1);
		jlbPicturePath_1 = new JLabel();//显示选择的文件路径名称			
		componentsPanel.add(jlbPicturePath_1);
		jlbPicturePath_1.setBounds(400,400,200,50);
		

		capRunnable = new CaptureRunnable();// 线程类CaptureRunnable
		thread = new Thread(capRunnable);
		
		capRunnable.getWebcamPanel().setLayout(null);
		this.add(capRunnable.getWebcamPanel(),BorderLayout.WEST);
		this.add(componentsPanel,BorderLayout.EAST);

		//this.getContentPane().setBackground(Color.WHITE);

		
		/*
		 * @description -----以下方法处理组件操作事件-----
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
				fileChooser = new JFileChooser();
				fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				fileChooser.showDialog(new JLabel(), "选择图片");
				File file = fileChooser.getSelectedFile();
				String fileName = file.getName();
				jlbPicturePath_1.setText(fileName.toString());	
			}		
		});
	}
	
	
	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws IOException {
		new EagleEye().show();
	}
}
