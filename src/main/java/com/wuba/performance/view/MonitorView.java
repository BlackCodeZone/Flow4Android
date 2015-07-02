/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wuba.performance.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.DateTickUnit;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.axis.Timeline;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardXYItemLabelGenerator;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.TextAnchor;

import com.android.tradefed.device.DeviceNotAvailableException;
import com.android.tradefed.device.ITestDevice;
import com.wuba.performance.model.FlowData;
import com.wuba.performance.view.ControlView;

import java.io.File;

/**
 *
 * @author wuxian
 */
@SuppressWarnings("serial")
public class MonitorView extends JPanel {

	public ITestDevice getmDevice() {
		return mDevice;
	}

	public void setmDevice(ITestDevice mDevice) {
		this.mDevice = mDevice;
	}

	private JPanel flowChat;
	private ITestDevice mDevice;
	private List<FlowData> datas = new ArrayList<FlowData>();
	private TimeSeriesCollection timeDataset;

	private static JFreeChart chart;
	private TimeSeries up;
	private TimeSeries down;
	private TimeSeries total;
	private static XYPlot plot;
	public int recordCount;
	public static int[] upList;
	public static int[] downList;
	public static int[] totalList;
	public static int[] incrementList;
	public static String FILE;
	private static NumberAxis numberaxis;
	private Timeline timeLine;
	public static DateAxis dateaxis;
	private static Date minDate;
	private static DateAxis pauseAxis;

	public void addData(FlowData data) {
		datas.add(data);
		datas.remove(0);
		// System.out.println("Data : " + data.toString());
		addUpObservation(data);
		addDownObservation(data);
		addTotalObservation(data);
		timeDataset = createXYDataset(data);
		 update();
//		netFlow();
	}

	private void addUpObservation(FlowData flowData) {
		if (up.getItemCount() > 10) {
			up.delete(0, 0);
		}
		up.add(new Millisecond(), flowData.getUp());
	}

	private void addDownObservation(FlowData flowData) {
		if (down.getItemCount() > 10) {
			down.delete(0, 0);
		}
		down.add(new Millisecond(), flowData.getDown());
	}

	private void addTotalObservation(FlowData flowData) {
		if (total.getItemCount() > 10) {
			total.delete(0, 0);
		}
		total.add(new Millisecond(), flowData.getTotal());
	}

	/**
	 * Creates new form MonitorPanel
	 * 
	 * @param controlView
	 *            TODO
	 * @throws DeviceNotAvailableException
	 */
	public MonitorView() throws DeviceNotAvailableException {
		initComponents();
	}

	@SuppressWarnings("deprecation")
	private void initComponents() throws DeviceNotAvailableException {
		up = new TimeSeries("up", Millisecond.class);
		down = new TimeSeries("down", Millisecond.class);
		total = new TimeSeries("total", Millisecond.class);
		makeLineAndShapeChart();
		this.setLayout(new GridLayout(1, 1));
		this.add(flowChat);
		recordCount = 0;
		upList = new int[100];
		downList = new int[100];
		totalList = new int[100];
		incrementList = new int[100];

	}

	// 柱状图,折线图数据集
	public CategoryDataset getBarData(double[][] data, String[] rowKeys,
			String[] columnKeys) {
		return DatasetUtilities
				.createCategoryDataset(rowKeys, columnKeys, data);

	}

	private TimeSeriesCollection createXYDataset(FlowData flowData) {
		return timeDataset;
	}

	/**
	 * 设置plot
	 */
	public JPanel chartSetting() {	
		dateaxis = new DateAxis("Time");
		numberaxis = new NumberAxis("NetFlow(Bytes)");
		dateaxis.setTickLabelFont(new Font("SansSerif", 0, 9));
		numberaxis.setTickLabelFont(new Font("SansSerif", 0, 9));
		dateaxis.setLabelFont(new Font("SansSerif", 0, 10));
		dateaxis.setAutoTickUnitSelection(true);
		numberaxis.setLabelFont(new Font("SansSerif", 0, 10));
		numberaxis.setAutoRange(true);
		numberaxis.setAutoRangeIncludesZero(false);
		numberaxis.setAutoTickUnitSelection(true);
		if(recordCount>2){
			int k = totalList[recordCount-1];
			numberaxis.setTickUnit(new NumberTickUnit(k*5));
		}

//		System.out.println(totalList[recordCount-1]);

//		numberaxis.setRangeAboutValue(k, k);
//		dateaxis.setRange(10);
//		 SimpleDateFormat format = new SimpleDateFormat("hh:mm:ss");
//		 dateaxis.setTickUnit(new DateTickUnit(DateTickUnit.MILLISECOND, 1,
//		 format));
//		dateaxis.setDateFormatOverride(format);
//		XYMultipleSeriesRenderer xyRenderer = new XYMultipleSeriesRenderer();
		
		XYLineAndShapeRenderer xylineandshaperenderer = new XYLineAndShapeRenderer(
				true, false);
		xylineandshaperenderer.setSeriesPaint(0, Color.red);
		xylineandshaperenderer.setSeriesPaint(1, Color.green);
		xylineandshaperenderer.setSeriesStroke(0, new BasicStroke(3.0F, 0, 2));
		xylineandshaperenderer.setSeriesStroke(1, new BasicStroke(3.0F, 0, 2));
		xylineandshaperenderer.setBaseShapesVisible(true);
		
		plot = new XYPlot(timeDataset, dateaxis, numberaxis,
				xylineandshaperenderer);
		plot.setBackgroundPaint(Color.lightGray);
		plot.setDomainGridlinePaint(Color.white);
		plot.setRangeGridlinePaint(Color.white);

		plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));
		dateaxis.setAutoRange(true);
		dateaxis.setLowerMargin(0.0);
		dateaxis.setUpperMargin(0.0);
		dateaxis.setTickLabelsVisible(true);
		
		numberaxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

		XYItemRenderer xyitem = plot.getRenderer();
//		xyitem.setBaseItemLabelsVisible(true);
		xyitem.setBaseItemLabelGenerator(new StandardXYItemLabelGenerator());
//		xyitem.setBaseItemLabelFont(new Font("Dialog", 1, 8));
		xyitem.setBaseSeriesVisibleInLegend(true);

		JFreeChart jfreechart = new JFreeChart("Net Flow", new Font(
				"SansSerif", 1, 24), plot, true);
		jfreechart.setBackgroundPaint(Color.white);
		ChartPanel chartpanel = new ChartPanel(jfreechart, true);
		chartpanel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createEmptyBorder(4, 4, 4, 4),
				BorderFactory.createLineBorder(Color.black)));
		add(chartpanel);
		return chartpanel;
	}

	public void update() {
		if(recordCount>2){

			int k = totalList[recordCount-1];
			System.out.println(k);
			numberaxis = new NumberAxis("NetFlow(Bytes)");
			numberaxis.setTickLabelFont(new Font("SansSerif", 0, 9));
			numberaxis.setLabelFont(new Font("SansSerif", 0, 10));
			numberaxis.setAutoRange(true);
			numberaxis.setAutoRangeIncludesZero(false);
			numberaxis.setAutoTickUnitSelection(true);
			numberaxis.setTickUnit(new NumberTickUnit(k*4));
		}
		plot.setDataset(timeDataset);
	}

	/**
	 * 更新data
	 */
	@SuppressWarnings("deprecation")
	public void netFlow() {
		XYPlot plot = chart.getXYPlot();
		DateAxis xAxis = (DateAxis) plot.getDomainAxis();
		// 设置每隔1秒钟一个间距
		xAxis.setTickUnit(new DateTickUnit(DateTickUnit.SECOND, 1,
				new SimpleDateFormat("HH:mm:ss")));

		XYLineAndShapeRenderer xylineandshaperenderer = (XYLineAndShapeRenderer) plot
				.getRenderer();
		// 设置网格背景颜色
		plot.setBackgroundPaint(Color.white);
		// 设置网格竖线颜色
		plot.setDomainGridlinePaint(Color.pink);
		// 设置网格横线颜色
		plot.setRangeGridlinePaint(Color.pink);
		// 设置曲线图与xy轴的距离
		plot.setAxisOffset(new RectangleInsets(0D, 0D, 0D, 10D));
		// 设置曲线是否显示数据点
		xylineandshaperenderer.setBaseShapesVisible(true);
		// 设置曲线显示各数据点的值
		XYItemRenderer xyitem = plot.getRenderer();
		xyitem.setBaseItemLabelsVisible(true);
		xyitem.setBasePositiveItemLabelPosition(new ItemLabelPosition(
				ItemLabelAnchor.OUTSIDE12, TextAnchor.BASELINE_LEFT));

		xyitem.setBaseItemLabelGenerator(new StandardXYItemLabelGenerator());
		xyitem.setBaseItemLabelFont(new Font("Dialog", 1, 9));
		plot.setRenderer(xyitem);
		plot.setDataset(timeDataset);
	}

	/**
	 * 生成折线图
	 * 
	 * @throws DeviceNotAvailableException
	 */
	public void makeLineAndShapeChart() {
		timeDataset = new TimeSeriesCollection();
		timeDataset.addSeries(up);
		timeDataset.addSeries(down);
		timeDataset.addSeries(total);
//		flowChat = createTimeXYChar("流量使用趋势图", "时间", "流量(B)", timeDataset);
		flowChat = chartSetting();
		// update();
	}

	/**
	 * 折线图
	 * 
	 * @param chartTitle
	 * @param x
	 * @param y
	 * @param timeDataset
	 * @param charName
	 * @return
	 */
	public JPanel createTimeXYChar(String chartTitle, String x, String y,
			TimeSeriesCollection timeDataset) {
		chart = ChartFactory.createTimeSeriesChart(chartTitle, x, y,
				timeDataset, true, true, false);
		return new ChartPanel(chart);
	}

	public void saveCurrentPoint(FlowData flowData) {
		int currentotal = flowData.getTotal();
		int currentup = flowData.getUp();
		int currentdown = flowData.getDown();

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
		// System.out.println(df.format(new Date(System.currentTimeMillis())));
		// System.out.println(df.format(new
		// Date(System.currentTimeMillis())).getClass());
		String currenttime = df.format(new Date(System.currentTimeMillis()));
		keepRecord(currentup, currentdown, currentotal, currenttime, 0, null);
	}

	public void keepRecord(int up, int down, int total, String current,
			int increment, String comment) {
		upList[recordCount] = up;
		downList[recordCount] = down;
		totalList[recordCount] = total;
		if (recordCount > 2) {
			incrementList[recordCount] = totalList[recordCount]
					- totalList[recordCount - 1];
		}
		ControlView.tableModel.addRow(new Object[] { recordCount, current, up,
				down, total, incrementList[recordCount], "" });
		recordCount++;
		if (upList[99] != 0) {
			saveAsFile(current, ControlView.flagTable);
			upList = new int[100];
			downList = new int[100];
			totalList = new int[100];
			incrementList = new int[100];
			recordCount = 0;
			ControlView.tableModel.getDataVector().removeAllElements();
		}
	}

	public static boolean saveAsFile(String current, JTable table) {
		File file = new File(current + ".txt");
		if (table == null) {
			return false;
		}
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		StringBuffer fileBuf = new StringBuffer("");
		int rowCount = model.getRowCount();
		int columnCount = model.getColumnCount();
		for (int col = 0; col < columnCount; col++) {
			fileBuf.append(model.getColumnName(col));
			fileBuf.append(",");
		}
		fileBuf.append("\n");
		for (int row = 0; row < rowCount; row++) {
			for (int col = 0; col < columnCount; col++) {
				System.out.println(model.getValueAt(row, col).toString());
				fileBuf.append(model.getValueAt(row, col).toString());

				if (col != columnCount - 1) {
					fileBuf.append(",");
				}
			}
			fileBuf.append("\n");
		}
		try {
			FileWriter writer = new FileWriter(file);
			writer.write(fileBuf.toString());
			writer.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace(System.err);
			return false;
		}
	}

	public void openFile() {
		FileNameExtensionFilter filter=new FileNameExtensionFilter("*.txt","txt");
		JFileChooser fc=new JFileChooser(".");
		fc.setFileFilter(filter);
		fc.setMultiSelectionEnabled(false);
		int result=fc.showSaveDialog(null);
		if (result==JFileChooser.APPROVE_OPTION) {
			File file=fc.getSelectedFile();
			if (!file.getPath().endsWith(".txt")) {
				saveAsFile(file.getName(), ControlView.flagTable);
//				file=new File(file.getPath());
				System.out.println("1234");
			}
		}
	}
	
	public static void stopPaint(){
		if(!ControlView.isRun){
			System.out.println("wer");
			Calendar cal = Calendar.getInstance();
			minDate = new Date(cal.getTimeInMillis());
			System.out.println(cal.getTimeInMillis());
//			pauseAxis = new DateAxis("Stop");
//			pauseAxis.setMinimumDate(minDate);
		}
	}
}