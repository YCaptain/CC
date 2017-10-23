package com.shecaicc.cc.enums;

public enum ClubStateEnum {
	CHECK(0, "审核中"), OFFLINE(-1, "非法社团"), SUCCESS(1, "操作成功"), PASS(2, "通过认证"), INNER_ERROR(-1001,
			"内部系统错误"), NULL_CLUBID(-1002, "ClubId为空"), NULL_CLUB(-1003, "club信息为空");
	private int state;
	private String stateInfo;

	private ClubStateEnum(int state, String stateInfo) {
		this.state = state;
		this.stateInfo = stateInfo;
	}

	public static ClubStateEnum stateOf(int state) {
		for (ClubStateEnum stateEnum : values()) {
			if (stateEnum.getState() == state) {
				return stateEnum;
			}
		}
		return null;
	}

	public int getState() {
		return state;
	}

	public String getStateInfo() {
		return stateInfo;
	}

}
