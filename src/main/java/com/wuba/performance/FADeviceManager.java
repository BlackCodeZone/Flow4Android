package com.wuba.performance;

import java.util.List;

import com.android.tradefed.config.ConfigurationException;
import com.android.tradefed.config.GlobalConfiguration;
import com.android.tradefed.device.DeviceManager;
import com.android.tradefed.device.IDeviceManager.FreeDeviceState;
import com.android.tradefed.device.ITestDevice;
import com.android.tradefed.log.LogRegistry;
import com.android.tradefed.log.StdoutLogger;

public class FADeviceManager {
	private DeviceManager deviceManager;
	private static FADeviceManager mFaDeviceManager = null;
	

	public FADeviceManager() {
	}

	public static FADeviceManager getInstance() {
		if (mFaDeviceManager == null) {
			mFaDeviceManager = new FADeviceManager();
		}

		return mFaDeviceManager;
	}

	public void stopDeviceManager() {
		if (deviceManager != null) {
			deviceManager.terminate();
		}
	}

	/**
	 * device manager start to work
	 * 
	 * @param args
	 */
	public void startDeviceManager() {
		try {
			List<String> nonGlobalArgs = GlobalConfiguration
					.createGlobalConfiguration(new String[] {});
		} catch (ConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		initLog();
		deviceManager = new DeviceManager();
		deviceManager.init();

	}

	private void initLog() {
		LogRegistry.getLogRegistry().registerLogger(new StdoutLogger());
	}

	public boolean hasDevice() {
		if (deviceManager == null) {
			System.out.println("deviceManager not init");
			return false;
		}

		if (deviceManager.getAvailableDevices().size() > 0) {
			return true;
		}

		return false;
	}

	public ITestDevice getDevice() {
		return deviceManager.allocateDevice();
	}
	public void freeDevice(ITestDevice device){
		deviceManager.freeDevice(device, FreeDeviceState.AVAILABLE);
	}
}
