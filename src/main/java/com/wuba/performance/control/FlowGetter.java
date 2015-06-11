package com.wuba.performance.control;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.android.tradefed.device.DeviceNotAvailableException;
import com.android.tradefed.device.ITestDevice;
import com.wuba.performance.model.FlowData;

public class FlowGetter {
	private static final String PID_COMMAND_PREFIX = "dumpsys package %s | grep userId";
	private static final String FLOW_COMMAND = "cd /proc/uid_stat/%s/ && cat tcp_rcv && cat tcp_snd";
	private static final String USERID_PREFIX = "userId";
	private static final String USERID_REGEX = "^[\\s]*userId=([\\d]+)\\s*gids.*";
	private ITestDevice device;
	private String appPackage;
	private String userId = null;

	public FlowGetter() {
	}

	public FlowGetter(ITestDevice device) {
		this.device = device;

	}

	public void setUserId(String appPackage) throws DeviceNotAvailableException {
		// 获得com.package对应的pid
		String userIdStr = device.executeShellCommand(String.format(
				PID_COMMAND_PREFIX, appPackage));
		userId = getUserId(userIdStr);

	}

	public FlowData getFlowData() throws DeviceNotAvailableException {
		// 得到上行和下行流量
		if (userId == null)
			return null;
		String flowCommand = String.format(FLOW_COMMAND, userId);
		String flow = device.executeShellCommand(flowCommand);
		String[] args = flow.trim().replaceAll("\\r", "").split("\\n");
		FlowData flowData = null;
		if (args != null && args.length > 1) {
			int up = Integer.parseInt(args[0]);
			int down = Integer.parseInt(args[1]);
			int total = up + down;
			flowData = new FlowData();
			flowData.setUp(up);
			flowData.setDown(down);
			flowData.setTotal(total);
		}

		return flowData;
	}

	private String getUserId(String userIdStr) {
		if (userIdStr == null || userIdStr.length() <= 0)
			return null;
		Pattern p = Pattern.compile(USERID_REGEX);
		Matcher m = p.matcher(userIdStr);
		String userId = null;
		while (m.find()) {
			userId = m.group(1);
		}
		return userId;
	}

}
