package com.shecaicc.cc.enums;

public enum EventStateEnum {
	OFFLINE(-1, "非法活动"), DOWN(0, "下架"), SUCCESS(1, "操作成功"), INNER_ERROR(-1001, "操作失败"), EMPTY(-1002, "活动为空");

	private int state;
	private String stateInfo;

	private EventStateEnum(int state, String stateInfo) {
		this.state = state;
		this.stateInfo = stateInfo;
	}

	public int getState() {
		return state;
	}

	public String getStateInfo() {
		return stateInfo;
	}

	public static EventStateEnum stateOf(int index) {
		for (EventStateEnum state: values()) {
			if (state.getState() == index) {
				return state;
			}
		}
		return null;
	}
}
