package com.wuba.performance;

import java.util.List;

import junit.framework.TestCase;

import com.android.tradefed.config.ConfigurationException;
import com.android.tradefed.config.GlobalConfiguration;
import com.android.tradefed.device.DeviceManager;
import com.android.tradefed.device.DeviceNotAvailableException;
import com.wuba.performance.control.FlowGetter;

public class FlowGetterTest extends TestCase {
	private FlowGetter flowGetter = new FlowGetter();

	public void test_getUserId() {
		String userIdStr = "    userId=10262 gids=[1028, 1015, 3003]";
		flowGetter.getUserId(userIdStr);
	}

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
		FlowGetter flowGetter = new FlowGetter(deviceManager.allocateDevice(),
				"com.wuba");
		flowGetter.setUserId();
		while (true) {
			flowGetter.getFlowData();
		}
	}

}
