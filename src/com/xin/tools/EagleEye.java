package com.xin.tools;

import java.awt.Container;
import java.awt.Dimension;
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
 * @description 界面框架，进行前端交互
 * @author zhangxiaolong
 * @date 2018-4
 * */
public class EagleEye extends JFrame {

	protected static CaptureRunnable capRunnable;
	protected static Thread thread;
	public JPanel componentsPanel = new JPanel();
	public JButton btnStartCapture,btnStopCapture,btnSelectFirst,btnSelectLast;
	public JLabel jlbSelectFirst,jlbSelectLast,jlbFilePath_1,jlbFilePath_2;
	public JFileChooser fileChooser; 
	
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
		this.setSize(1300, 2048);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setBackground(Color.WHITE);
		
		btnStartCapture = new JButton("开始");
		btnStopCapture = new JButton("停止");
		btnSelectFirst = new JButton("选择图片");
		btnSelectLast = new JButton("选择图片");
		jlbSelectFirst = new JLabel("图片 1");
		jlbSelectLast = new JLabel("图片 2");
		jlbFilePath_1 = new JLabel();//显示选择的文件名称
		jlbFilePath_2 = new JLabel();
		
		capRunnable = new CaptureRunnable();// 线程类CaptureRunnable
		thread = new Thread(capRunnable);
		
		capRunnable.getWebcamPanel().setLayout(null);//清空WebcamPanel
		this.add(capRunnable.getWebcamPanel(),BorderLayout.WEST);
		
		this.add(btnStartCapture).setBounds(700, 100, 100, 50);
		this.add(btnStopCapture).setBounds(850, 100, 100, 50);
		
		this.add(btnSelectFirst).setBounds(700, 300, 100, 50);
		this.add(jlbSelectFirst).setBounds(850, 300, 50, 50);
		this.add(jlbFilePath_1).setBounds(900, 300, 200, 50);
		
		this.add(btnSelectLast).setBounds(700, 400, 100, 50);
		this.add(jlbSelectLast).setBounds(850, 400, 50, 50);
		this.add(jlbFilePath_2).setBounds(900, 400, 200, 50);
		
		
		
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
		btnSelectFirst.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fileChooser = new JFileChooser();
				fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				fileChooser.showDialog(new JLabel(), "选择图片");
				File file = fileChooser.getSelectedFile();
				String fileName = file.getName();
				jlbFilePath_1.setText(fileName.toString());	
			}		
		});
		//建立btnSelectLast按钮的监听
		btnSelectLast.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				fileChooser = new JFileChooser();
				fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				fileChooser.showDialog(new JLabel(), "选择图片");
				File file = fileChooser.getSelectedFile();
				String fileName = file.getName();
				jlbFilePath_2.setText(fileName);
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
		
		
		jlbSelectFirst = new JLabel("第一帧");	
		componentsPanel.add(jlbSelectFirst);
		btnSelectFirst = new JButton("选择");
		componentsPanel.add(btnSelectFirst);
		btnSelectFirst.setBounds(700,600,150,30);

		jlbSelectLast = new JLabel("最后帧");
		componentsPanel.add(jlbSelectLast);
		jlbSelectFirst.setBounds(500,600,150,30);
		btnSelectLast = new JButton("选择");
		componentsPanel.add(btnSelectLast);
		
		JLabel jlbFile_1 = new JLabel("图片1：");
		componentsPanel.add(jlbFile_1);
		jlbFilePath_1 = new JLabel();//显示选择的文件路径名称			
		componentsPanel.add(jlbFilePath_1);
		jlbFilePath_1.setBounds(400,400,200,50);
		

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
		btnSelectFirst.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fileChooser = new JFileChooser();
				fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				fileChooser.showDialog(new JLabel(), "选择图片");
				File file = fileChooser.getSelectedFile();
				String fileName = file.getName();
				jlbFilePath_1.setText(fileName.toString());	
			}		
		});
	}
	
	
	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws IOException {
		new EagleEye().show();
	}
}
