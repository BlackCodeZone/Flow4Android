package com.wuba.performance;

import java.util.List;

import junit.framework.TestCase;

import com.android.tradefed.config.ConfigurationException;
import com.android.tradefed.config.GlobalConfiguration;
import com.android.tradefed.device.DeviceManager;
import com.android.tradefed.device.DeviceNotAvailableException;
import com.wuba.performance.control.FlowGetter;
import com.wuba.performance.model.FlowData;

public class FlowGetterTest extends TestCase {
	private FlowGetter flowGetter = new FlowGetter();

	

	public void test_getFlowData() throws InterruptedException,
			DeviceNotAvailableException {
		try {
			String[] str = new String[] { "aaa", "bbb", "ccc" };
			List<String> nonGlobalArgs = GlobalConfiguration
					.createGlobalConfiguration(str);
		} catch (ConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DeviceManager deviceManager = new DeviceManager();
		deviceManager.init();
		Thread.sleep(3000);
		FlowGetter flowGetter = new FlowGetter(deviceManager.allocateDevice());
		
		flowGetter.setUserId("com.wuba");
		FlowData flowdata = flowGetter.getFlowData();
		System.out.println(flowdata.toString());
	}

}
