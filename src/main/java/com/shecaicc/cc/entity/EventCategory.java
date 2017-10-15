package com.shecaicc.cc.entity;

import java.util.Date;

public class EventCategory {
	private Long eventCategoryId;
	private Long clubId;
	private String eventCategoryName;
	private Integer priority;
	private Date createTime;

	public Long getEventCategoryId() {
		return eventCategoryId;
	}

	public void setEventCategoryId(Long eventCategoryId) {
		this.eventCategoryId = eventCategoryId;
	}

	public Long getClubId() {
		return clubId;
	}

	public void setClubId(Long clubId) {
		this.clubId = clubId;
	}

	public String getEventCategoryName() {
		return eventCategoryName;
	}

	public void setEventCategoryName(String eventCategoryName) {
		this.eventCategoryName = eventCategoryName;
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

}
