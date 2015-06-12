/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wuba.performance.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.DatasetUtilities;

/**
 *
 * @author wuxian
 */
public class MonitorView extends JPanel {
	private javax.swing.JPanel flowChat;

	/**
	 * Creates new form MonitorPanel
	 */
	public MonitorView() {
		initComponents();

	}

	@SuppressWarnings("unchecked")
	// <editor-fold defaultstate="collapsed" desc="Generated Code">
	private void initComponents() {
		// this.setBackground(Color.cyan);
		makeLineAndShapeChart();
		flowChat.setBackground(Color.RED);
		this.setLayout(new GridLayout(1, 1));
		this.add(flowChat);

	}// </editor-fold>

	// 柱状图,折线图数据集
	public CategoryDataset getBarData(double[][] data, String[] rowKeys,
			String[] columnKeys) {
		return DatasetUtilities
				.createCategoryDataset(rowKeys, columnKeys, data);

	}

	/**
	 * 生成折线图
	 */
	public void makeLineAndShapeChart() {
		//坐标点
		double[][] data = new double[][] { { 672, 766, 223, 540, 126 },
				{ 325, 521, 210, 340, 106 }, { 332, 256, 523, 240, 526 } };
		//折线说明
		String[] rowKeys = { "up", "down", "total" };
		//横坐标
		String[] columnKeys = { "北京", "上海", "广州", "成都", "深圳" };
		CategoryDataset dataset = getBarData(data, rowKeys, columnKeys);

		flowChat = createTimeXYChar("流量使用趋势图", "时间", "流量(B)", dataset);
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

		JFreeChart chart = ChartFactory.createLineChart(chartTitle, x, y,
				xyDataset, PlotOrientation.VERTICAL, true, true, false);

		return new ChartPanel(chart);

	}
}
