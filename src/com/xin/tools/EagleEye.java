package com.xin.tools;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.BorderLayout;
import java.awt.dnd.DropTarget;//窗体拖动
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JFileChooser;//读取文件
import javax.swing.ImageIcon;//显示图片
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.omg.CORBA.PUBLIC_MEMBER;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;
import com.github.sarxos.webcam.WebcamUtils;
import com.github.sarxos.webcam.util.ImageUtils;

/*
 * @description 界面框架，进行前端操作
 * @author zhangxiaolong
 * @date 2018-4
 * */
public class EagleEye extends JFrame {

	protected static CaptureRunnable capRunnable;
	protected static Thread thread;
	private JPanel componentsPanel = new JPanel();
	private JButton startCapture,stopCapture,btnSelectFirst,btnSelectLast;
	private JLabel jlbSelectFirst,jlbSelectLast;
	
	public EagleEye() {
		try {
			createFrame();// 调用Frame
			//initFrame();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
//------------------------使用GridBagConstraints绘制窗口以及布局---------------------------↓
	JButton buttonStart,buttonStop,buttonCalc;
	JLabel labelPicture01,labelPicture02;
	
	public void initFrame(){
		buttonStart = new JButton("开始");
		buttonStop = new JButton("停止");
		labelPicture01 = new JLabel();
		
		capRunnable = new CaptureRunnable();// 线程类CaptureRunnable
		thread = new Thread(capRunnable);
		
		this.setTitle("高速相机 v2.0");
		this.setResizable(true);
		this.setSize(1024, 2048);
		this.setLocationRelativeTo(null);
//		this.add(buttonStart);
//		this.add(buttonStop);
//		this.add(labelPicture01);
		this.add(labelPicture02);
		labelPicture01.setIcon(new ImageIcon("D:/PICTURES/20180424_14-46-54-216.png"));
		
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
	}
//--------------------------------------↑↑↑↑↑↑↑↑↑↑---------------------------------------↑	
	
	// 绘制窗口,监听按钮
	public void createFrame() {	
		startCapture = new JButton("\u5f00\u59cb");//"开始"
		//startCapture.setBounds(new Rectangle(500,500, 150, 30));
		stopCapture = new JButton("\u505c\u6b62");//"停止"
		//stopCapture.setBounds(700,500, 150, 30);
		btnSelectFirst = new JButton("选择");
		btnSelectFirst.setBounds(700,600,150,30);
		jlbSelectFirst = new JLabel("第一帧");	
		jlbSelectFirst.setBounds(500,600,150,30);
		btnSelectLast = new JButton("选择");
		jlbSelectLast = new JLabel("最后帧");
		

		capRunnable = new CaptureRunnable();// 线程类CaptureRunnable
		thread = new Thread(capRunnable);
		
		
		this.setTitle("高速相机 v1.0");
		this.setResizable(true);
		this.setSize(1024, 1024);
		this.setLocationRelativeTo(null);
		this.add(componentsPanel,BorderLayout.EAST);
		this.add(capRunnable.getWebcamPanel(),BorderLayout.WEST);
		componentsPanel.add(startCapture);
		componentsPanel.add(stopCapture);
		componentsPanel.add(jlbSelectFirst);
		componentsPanel.add(jlbSelectLast);
		componentsPanel.add(btnSelectFirst);
		componentsPanel.add(btnSelectLast);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		//setUpUIComponent();
		//setUpEventListener();

		
		/*
		 * @description -----以下方法处理组件操作事件-----
		 * */
		// 建立startCapture按钮的监听
		startCapture.addActionListener(new ActionListener() {
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
		stopCapture.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				capRunnable.setFlag(false);// 设置线程循环：false
			}
		});
	}
	
	
	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws IOException {
		new EagleEye().show();
	}
}
