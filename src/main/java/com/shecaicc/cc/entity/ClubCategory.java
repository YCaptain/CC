package com.shecaicc.cc.entity;

import java.util.Date;

public class ClubCategory {
	private Long clubCategoryId;
	private String clubCategoryName;
	private String clubCategoryDesc;
	private String clubCategoryImg;
	private Integer priority;
	private Date createTime;
	private Date lastEditTime;
	private ClubCategory parent;

	public Long getClubCategoryId() {
		return clubCategoryId;
	}

	public void setClubCategoryId(Long clubCategoryId) {
		this.clubCategoryId = clubCategoryId;
	}

	public String getClubCategoryName() {
		return clubCategoryName;
	}

	public void setClubCategoryName(String clubCategoryName) {
		this.clubCategoryName = clubCategoryName;
	}

	public String getClubCategoryDesc() {
		return clubCategoryDesc;
	}

	public void setClubCategoryDesc(String clubCategoryDesc) {
		this.clubCategoryDesc = clubCategoryDesc;
	}

	public String getClubCategoryImg() {
		return clubCategoryImg;
	}

	public void setClubCategoryImg(String clubCategoryImg) {
		this.clubCategoryImg = clubCategoryImg;
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

	public Date getLastEditTime() {
		return lastEditTime;
	}

	public void setLastEditTime(Date lastEditTime) {
		this.lastEditTime = lastEditTime;
	}

	public ClubCategory getParent() {
		return parent;
	}

	public void setParent(ClubCategory parent) {
		this.parent = parent;
	}
}
