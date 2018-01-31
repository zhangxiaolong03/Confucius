package com.xin.tools;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.text.SimpleDateFormat;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.omg.CORBA.PUBLIC_MEMBER;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;
import com.github.sarxos.webcam.WebcamUtils;
import com.github.sarxos.webcam.util.ImageUtils;

public class EagleEye extends JFrame{
	
	protected CaptureThread capThread = null;
	/*
	 * 构造
	 * */
	public EagleEye(){	
		try {
			createFrame();//调用Frame
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

    
    /*
     * 绘制frame窗口
     * 对窗口中的按钮进行监听
     * */
    public void createFrame(){
    	
    	final JFrame window = new JFrame("连续抓拍");
    	capThread = new CaptureThread();//实例化线程类
    	
        window.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e)
            {
                capThread.getWebcam().close();
                window.dispose();
            }
        });
        
        // window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        final JButton startCapture = new JButton("开始拍照");
        JButton stopCapture = new JButton("停止拍照");
        window.add(capThread.getWebcamPanel(), BorderLayout.CENTER);
        window.add(startCapture, BorderLayout.WEST);
        window.add(stopCapture,BorderLayout.EAST);
        window.setResizable(true);
        window.pack();
        window.setVisible(true);
        
        //建立startCapture按钮的监听
        startCapture.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                //判断线程对象是否为空，如果为空则创建新的线程对象并启动
            	if(capThread != null){            	
            		capThread.start();//点击【开始】按钮启动拍照的thread                
                }else{         	
                	new Thread(capThread).start();
                	//capThread = new CaptureThread(); 
                }
                
//                SwingUtilities.invokeLater(new Runnable() {
//
//                    public void run()
//                    {
//                        //JOptionPane.showMessageDialog(null, "截图成功");
//                    	startCapture.setText("停止");
//                        startCapture.setEnabled(true);
//                        return;
//                    }
//                });
            }
        });
        
        //建立stopCapture按钮的监听
        stopCapture.addActionListener(new ActionListener() {
			
        	@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent e) {
        		//判断线程对象是否为空，非空则中断线程
				if(capThread != null){
					capThread.interrupt();
				}	
				capThread = null;
			}
		});
    }
    
    public static void main(String[] args) throws IOException
    {
    	new EagleEye().show();
    	
    }


}
