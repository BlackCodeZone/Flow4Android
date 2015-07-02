package com.wuba.performance.model;

public class FlowData {
	@Override
	public String toString() {
		return "FlowData [up=" + up + ", down=" + down + ", total=" + total
				+ "]";
	}

	public int getUp() {
		return up;
	}

	public void setUp(int up) {
		this.up = up;
	}

	public int getDown() {
		return down;
	}

	public void setDown(int down) {
		this.down = down;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	// 上行流量
	private int up;
	// 下行流量
	private int down;
	// 总流量
	private int total;
	//

	public FlowData() {
	}
	
}
