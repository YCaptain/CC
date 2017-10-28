package com.shecaicc.cc.dto;

import java.util.List;

import com.shecaicc.cc.entity.EventCategory;
import com.shecaicc.cc.enums.EventCategoryStateEnum;

public class EventCategoryExecution {
	// 结果状态
	private int state;
	// 状态标识
	private String stateInfo;

	private List<EventCategory> eventCategoryList;

	public EventCategoryExecution() {
	}

	// 操作失败时使用的构造器
	public EventCategoryExecution(EventCategoryStateEnum stateEnum) {
		this.state = stateEnum.getState();
		this.stateInfo = stateEnum.getStateInfo();
	}

	// 操作成功时使用的构造器
	public EventCategoryExecution(EventCategoryStateEnum stateEnum, List<EventCategory> eventCategoryList) {
		this.state = stateEnum.getState();
		this.stateInfo = stateEnum.getStateInfo();
		this.eventCategoryList = eventCategoryList;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getStateInfo() {
		return stateInfo;
	}

	public void setStateInfo(String stateInfo) {
		this.stateInfo = stateInfo;
	}

	public List<EventCategory> getEventCategoryList() {
		return eventCategoryList;
	}

	public void setEventCategoryList(List<EventCategory> eventCategoryList) {
		this.eventCategoryList = eventCategoryList;
	}
}
