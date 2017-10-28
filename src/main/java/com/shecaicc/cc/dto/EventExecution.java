package com.shecaicc.cc.dto;

import java.util.List;

import com.shecaicc.cc.entity.Event;
import com.shecaicc.cc.enums.EventStateEnum;

public class EventExecution {
	// 结果状态
	private int state;
	// 状态标识
	private String stateInfo;
	// 活动数量
	private int count;
	// 操作的event
	private Event event;
	// 获取的event列表
	private List<Event> eventList;

	public EventExecution() {
	}

	// 失败时用的构造器
	public EventExecution(EventStateEnum stateEnum) {
		this.state = stateEnum.getState();
		this.stateInfo = stateEnum.getStateInfo();
	}

	// 成功时用的构造器
	public EventExecution(EventStateEnum stateEnum, Event event) {
		this.state = stateEnum.getState();
		this.stateInfo = stateEnum.getStateInfo();
		this.event = event;
	}

	// 成功时用的构造器
	public EventExecution(EventStateEnum stateEnum, List<Event> eventList) {
		this.state = stateEnum.getState();
		this.stateInfo = stateEnum.getStateInfo();
		this.eventList = eventList;
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

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	public List<Event> getEventList() {
		return eventList;
	}

	public void setEventList(List<Event> eventList) {
		this.eventList = eventList;
	}
}
