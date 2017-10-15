package com.shecaicc.cc.entity;

import java.util.Date;
import java.util.List;

public class Event {
	private Long eventId;
	private String eventName;
	private String eventDesc;
	private String imgAddr;
	// 活动容量
	private Integer capacity;
	private Integer numPerson;
	private Integer priority;
	private Date createTime;
	private Date endTime;
	private Date lastEditTime;
	// -1.不显示 0.不可参与 1.可参与
	private Integer enableStatus;
	private List<EventImg> eventImgList;
	private Club club;

	public Long getEventId() {
		return eventId;
	}

	public void setEventId(Long eventId) {
		this.eventId = eventId;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public String getEventDesc() {
		return eventDesc;
	}

	public void setEventDesc(String eventDesc) {
		this.eventDesc = eventDesc;
	}

	public String getImgAddr() {
		return imgAddr;
	}

	public void setImgAddr(String imgAddr) {
		this.imgAddr = imgAddr;
	}

	public Integer getCapacity() {
		return capacity;
	}

	public void setCapacity(Integer capacity) {
		this.capacity = capacity;
	}

	public Integer getNumPerson() {
		return numPerson;
	}

	public void setNumPerson(Integer numPerson) {
		this.numPerson = numPerson;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Date getLastEditTime() {
		return lastEditTime;
	}

	public void setLastEditTime(Date lastEditTime) {
		this.lastEditTime = lastEditTime;
	}

	public Integer getEnableStatus() {
		return enableStatus;
	}

	public void setEnableStatus(Integer enableStatus) {
		this.enableStatus = enableStatus;
	}

	public List<EventImg> getEventImgList() {
		return eventImgList;
	}

	public void setEventImgList(List<EventImg> eventImgList) {
		this.eventImgList = eventImgList;
	}

	public Club getClub() {
		return club;
	}

	public void setClub(Club club) {
		this.club = club;
	}
}
