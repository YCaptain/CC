package com.shecaicc.cc.entity;

import java.util.Date;

public class EventImg {
	private Long EventImgId;
	private String imgAddr;
	private String imgDesc;
	private Integer priority;
	private Date createTime;
	private Long eventId;

	public Long getEventImgId() {
		return EventImgId;
	}

	public void setEventImgId(Long eventImgId) {
		EventImgId = eventImgId;
	}

	public String getImgAddr() {
		return imgAddr;
	}

	public void setImgAddr(String imgAddr) {
		this.imgAddr = imgAddr;
	}

	public String getImgDesc() {
		return imgDesc;
	}

	public void setImgDesc(String imgDesc) {
		this.imgDesc = imgDesc;
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

	public Long getEventId() {
		return eventId;
	}

	public void setEventId(Long eventId) {
		this.eventId = eventId;
	}
}
