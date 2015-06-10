package com.wuba.performance;

import java.util.List;

import com.android.tradefed.config.ConfigurationException;
import com.android.tradefed.config.GlobalConfiguration;
import com.android.tradefed.device.DeviceManager;
import com.android.tradefed.log.LogRegistry;
import com.android.tradefed.log.LogUtil.CLog;
import com.android.tradefed.log.StdoutLogger;

public class HomePage {
	private DeviceManager deviceManager;

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		HomePage page = new HomePage();
		page.initDeviceManager(args);
		CLog.i("Helloworld");
		
		
		
		
		
		
		
		page.stopDeviceManager();
	}
	
	
	
	
	
	
	private void stopDeviceManager() {
		if (deviceManager != null) {
			deviceManager.terminate();
		}
	}

	/**
	 * device manager start to work
	 * 
	 * @param args
	 */
	private void initDeviceManager(String[] args) {
		try {
			List<String> nonGlobalArgs = GlobalConfiguration
					.createGlobalConfiguration(args);
		} catch (ConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		initLog();
		DeviceManager deviceManager = new DeviceManager();
		deviceManager.init();

	}

	private void initLog() {
		LogRegistry.getLogRegistry().registerLogger(new StdoutLogger());
	}
}
