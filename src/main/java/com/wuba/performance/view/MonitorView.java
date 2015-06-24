/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wuba.performance.view;

import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.DatasetUtilities;

import com.android.tradefed.device.DeviceNotAvailableException;
import com.android.tradefed.device.ITestDevice;
import com.wuba.performance.control.FlowGetter;
import com.wuba.performance.model.FlowData;

import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

/**
 *
 * @author wuxian
 */
public class MonitorView extends JPanel {

	public ITestDevice getmDevice() {
		return mDevice;
	}

	public void setmDevice(ITestDevice mDevice) {
		this.mDevice = mDevice;
	}

	private JPanel flowChat;

	private ITestDevice mDevice;
	private FlowGetter flowGetter;
	private static TimeSeries timeSeries;
	private List<FlowData> datas = new ArrayList<FlowData>();
	private CategoryDataset dataset;

	private JFreeChart chart;

	public void addData(FlowData data) {
		datas.add(data);
		System.out.println("Data : " + data.toString());
		if (datas.size() >= 10) {
			parseDataSet();
		}
	}

	/**
	 * Creates new form MonitorPanel
	 * 
	 * @param controlView
	 *            TODO
	 */
	public MonitorView() {
		initComponents();

	}

	@SuppressWarnings("unchecked")
	// <editor-fold defaultstate="collapsed" desc="Generated Code">
	private void initComponents() {
		// this.setBackground(Color.cyan);
		makeLineAndShapeChart();
		this.setLayout(new GridLayout(1, 1));
		this.add(flowChat);

	}// </editor-fold>

	// 柱状图,折线图数据集
	public CategoryDataset getBarData(double[][] data, String[] rowKeys,
			String[] columnKeys) {
		return DatasetUtilities
				.createCategoryDataset(rowKeys, columnKeys, data);

	}

	// 组装数据集

	private void parseDataSet() {
		// 坐标点
		
		double[][] ten = new double[3][10];
		//次数
		int n = 0;
		for (int i = datas.size() - 1; i > datas.size() - 11; i--) {
			n++;
			FlowData flowData = datas.get(i);
			ten[0][10-n] = flowData.getUp();
			ten[1][10-n] = flowData.getDown();
			ten[2][10-n] = flowData.getTotal();
		}
		double[][] datas = new double[][] { { 0, 0, 30,0, 0, 0,0, 0, 0,10 }, { 0,0, 0,0, 0, 0,0, 2, 0,1 },
				{ 4, 5, 6 ,0, 2, 2,2, 2, 1,0} };
		// 折线说明
		String[] rowKeys = { "up", "down", "total" };
		// 横坐标
		String[] columnKeys = { "1", "2", "3","4","5","6","7","8","9","10" };
		dataset = getBarData(ten, rowKeys, columnKeys);
		CategoryPlot plot = (CategoryPlot)chart.getPlot(); 
		plot.setDataset(dataset); 
//		updateData(rowKeys,columnKeys,ten);
//		flowChat = createTimeXYChar("流量使用趋势图", "时间", "流量(B)", dataset);
	}
	
	private void updateData(String[] rowKeys, String[] columnKeys, double[][] updates){
		CategoryDataset dataset = DatasetUtilities.createCategoryDataset(rowKeys, columnKeys, updates);
		JFreeChart chart = ChartFactory.createLineChart("流量使用趋势图", "时间", "流量(B)",
				dataset, PlotOrientation.VERTICAL, true, true, false);
		CategoryPlot plot = (CategoryPlot)chart.getPlot(); 
		plot.setDataset(dataset); 
	}
	/**
	 * 生成折线图
	 */
	@SuppressWarnings("deprecation")
	public void makeLineAndShapeChart() {
		timeSeries = new TimeSeries("time",
				org.jfree.data.time.Millisecond.class);
		TimeSeriesCollection timeseriescollection = new TimeSeriesCollection();
		timeseriescollection.addSeries(timeSeries);
		System.out.println(timeseriescollection);
		// FlowData flowData = flowGetter.getFlowData();
		// 坐标点
		double[][] data = new double[][] { { 1, 2, 3,1, 2, 3,1, 2, 3,1 }, { 3, 4, 5,1, 2, 3,1, 2, 3,1 },
				{ 4, 5, 6 ,1, 2, 3,1, 2, 3,1} };
		// 折线说明
		String[] rowKeys = { "up", "down", "total" };
		// 横坐标
		String[] columnKeys = { "1", "2", "3","4","5","6","7","8","9","10" };
		dataset = getBarData(data, rowKeys, columnKeys);

		flowChat = createTimeXYChar("流量使用趋势图", "时间", "流量(B)", dataset);
		// timeChart = ChartFactory.createTimeSeriesChart("流量使用趋势图", "时间",
		// "流量(B)", timeseriescollection, true, true, false);
	}

	/**
	 * 折线图
	 * 
	 * @param chartTitle
	 * @param x
	 * @param y
	 * @param xyDataset
	 * @param charName
	 * @return
	 */
	public JPanel createTimeXYChar(String chartTitle, String x, String y,
			CategoryDataset xyDataset) {
		chart = ChartFactory.createLineChart(chartTitle, x, y,
				xyDataset, PlotOrientation.VERTICAL, true, true, false);
		
		return new ChartPanel(chart);
	}
}
