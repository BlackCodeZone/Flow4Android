/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wuba.performance.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.LayoutStyle;
import javax.swing.table.DefaultTableModel;

import com.android.tradefed.device.DeviceNotAvailableException;
import com.android.tradefed.device.ITestDevice;
import com.wuba.performance.FADeviceManager;
import com.wuba.performance.control.FlowGetter;
/**
 *
 * @author wuxian
 */
@SuppressWarnings("serial")
public class ControlView extends JPanel implements Runnable {
	
	public boolean isRun() {
		return isRun;
	}

	public void setRun(boolean isRun) {
		this.isRun = isRun;
	}

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private JButton endButton;
	private JButton flagButton;
	public static JTable flagTable;
	private JScrollPane buttonMenu;
	private JButton startButton;
	private JButton saveButton;
	// 间隔时间
	private float mInterval = 1000;

	private ITestDevice device;
	private FlowGetter flowGetter;

	static boolean isRun;
	private MonitorView mMonitorView;
	public static DefaultTableModel tableModel;
	/**
	 * Creates new form MonitorView
	 * @param monitorView TODO
	 */
	public ControlView(MonitorView monitorView) {
		initComponents();
		this.mMonitorView = monitorView;
	}
	

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings({ "unchecked", "serial" })
	// <editor-fold defaultstate="collapsed"
	// desc="Generated Code">//GEN-BEGIN:initComponents
	private void initComponents() {

		buttonMenu = new JScrollPane();
		flagTable = new JTable();
		startButton = new JButton();
		flagButton = new JButton();
		endButton = new JButton();
		saveButton = new JButton();

		startButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					startButtonPressed();
				} catch (DeviceNotAvailableException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
		});
		flagButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				flagButtonPressed();

			}

		});
		endButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				endButtonPressed();

			}

		});
		saveButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				saveButtonPressed();

			}
		});
		setBackground(new java.awt.Color(255, 255, 255));
		
		flagTable.setModel(new DefaultTableModel(null, new String[] {
				"No", "Time", "Up", "Down", "Total", "Increment", "Comment" }) {
			boolean[] canEdit = new boolean[] { false, false, false, false,
					false, false, true };

			public boolean isCellEditable(int rowIndex, int columnIndex) {
				return canEdit[columnIndex];
			}
		});
		tableModel = (DefaultTableModel) flagTable.getModel();
		
		buttonMenu.setViewportView(flagTable);

		startButton.setText("Start");

		flagButton.setText("Flag");

		endButton.setText("End");
		
		saveButton.setText("Save");

		GroupLayout layout = new GroupLayout(this);
		this.setLayout(layout);
		layout.setHorizontalGroup(layout
				.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(
						layout.createSequentialGroup()
								.addComponent(buttonMenu,
										GroupLayout.DEFAULT_SIZE, 422,
										Short.MAX_VALUE)
								.addPreferredGap(
										LayoutStyle.ComponentPlacement.RELATED)
								.addGroup(
										layout.createParallelGroup(
												GroupLayout.Alignment.LEADING)
												.addComponent(startButton)
												.addComponent(saveButton)
												.addComponent(
														endButton,
														GroupLayout.Alignment.TRAILING)
												.addComponent(flagButton))
								.addContainerGap()));
		layout.setVerticalGroup(layout
				.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addComponent(buttonMenu, GroupLayout.PREFERRED_SIZE, 0,
						Short.MAX_VALUE)
				.addGroup(
						layout.createSequentialGroup()
								.addComponent(startButton)
								.addPreferredGap(
										LayoutStyle.ComponentPlacement.RELATED,
										40, Short.MAX_VALUE)
								.addComponent(flagButton)
								.addPreferredGap(
										LayoutStyle.ComponentPlacement.RELATED,
										40, Short.MAX_VALUE)
								.addComponent(saveButton)
								.addPreferredGap(
										LayoutStyle.ComponentPlacement.RELATED,
										40, Short.MAX_VALUE)
								.addComponent(endButton)));

		startButton.setEnabled(true);
		flagButton.setEnabled(false);
		endButton.setEnabled(false);
		saveButton.setEnabled(false);
	}// </editor-fold>//GEN-END:initComponents

	private void startButtonPressed() throws DeviceNotAvailableException {
		System.out.println("start");
//		try
//        {	
//			String command1 = "adb shell cd proc/uid_stat/10083 && cat tcp_rcv && cat tcp_snd";
//            Process process1 = Runtime.getRuntime().exec (command1);
//            InputStreamReader ir=new InputStreamReader(process1.getInputStream());
//            BufferedReader input = new BufferedReader (ir);
//            String line;
//            while ((line = input.readLine ()) != null){
//                float i = Float.parseFloat(line)/1024;
//                System.out.println(i+"kbytes");
//            }
//            float rcv = Float.parseFloat(input.readLine ())/1024;
//            float snd = Float.parseFloat(input.readLine ())/1024;
//            System.out.println("rcv:" + rcv + "KBytes");
//            System.out.println("snd:" + snd + "KBytes");
//        }
//        catch (java.io.IOException e){
//            System.err.println ("IOException " + e.getMessage());
//        }
		if (!FADeviceManager.getInstance().hasDevice()) {
			// 弹出警告框，提示信息是未发现设备
			JOptionPane.showMessageDialog(null, "There is no device right now", "alert", JOptionPane.ERROR_MESSAGE);
			return;
		}
		device = FADeviceManager.getInstance().getDevice();
		
		flowGetter = new FlowGetter(device);
		flowGetter.setUserId("com.wuba");
		// 启动线程
		setRun(true);
		new Thread(this).start();
		startButton.setEnabled(false);
		flagButton.setEnabled(true);
		endButton.setEnabled(true);
		saveButton.setEnabled(true);
		
	}

	private void flagButtonPressed() {
		System.out.println("flag");
		if (startButton.isEnabled()) {
			System.out.println("还未开始,无反应");
			return;
		}
		try {
			mMonitorView.saveCurrentPoint(flowGetter.getFlowData());
		} catch (DeviceNotAvailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private void endButtonPressed() {
		System.out.println("end");
		setRun(false);
		FADeviceManager.getInstance().freeDevice(device);
		startButton.setEnabled(true);
		flagButton.setEnabled(true);
		endButton.setEnabled(false);
//		MonitorView.stopPaint();
	}

	private void saveButtonPressed() {
		System.out.println("save");
		setRun(false);
		FADeviceManager.getInstance().freeDevice(device);
		startButton.setEnabled(true);
		flagButton.setEnabled(true);
		endButton.setEnabled(false);
		mMonitorView.openFile();
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("数据获取已经开始");

		while (isRun) {
			try {
//				tableModel.setRowCount(0);
				mMonitorView.addData(flowGetter.getFlowData());
				
				Thread.sleep(1000);
			} catch (DeviceNotAvailableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}
