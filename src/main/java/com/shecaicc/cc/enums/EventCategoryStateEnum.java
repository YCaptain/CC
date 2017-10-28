package com.shecaicc.cc.enums;

public enum EventCategoryStateEnum {
	SUCCESS(1, "创建成功"), INNER_ERROR(-1001, "操作失败"), EMPTY_LIST(-1002, "添加数少于1");

	private int state;
	private String stateInfo;

	private EventCategoryStateEnum(int state, String stateInfo) {
		this.state = state;
		this.stateInfo = stateInfo;
	}

	public int getState() {
		return state;
	}

	public String getStateInfo() {
		return stateInfo;
	}

	public static EventCategoryStateEnum stateOf(int index) {
		for (EventCategoryStateEnum state: values()) {
			if (state.getState() == index) {
				return state;
			}
		}
		return null;
	}

}
