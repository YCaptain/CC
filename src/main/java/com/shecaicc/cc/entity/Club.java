package com.shecaicc.cc.entity;

import java.util.Date;

public class Club {
	private Long clubId;
	private String clubName;
	private String clubDesc;
	private String clubAddr;
	private String phone;
	private String clubImg;
	private Integer priority;
	private Date createTime;
	private Date lastEditTime;
	// -1.不可用 0.审核中 1.可用
	private Integer enableStatus;
	// 超级管理员给社团的提醒
	private String advice;
	public Long getClubId() {
		return clubId;
	}
	public void setClubId(Long clubId) {
		this.clubId = clubId;
	}
	public String getClubName() {
		return clubName;
	}
	public void setClubName(String clubName) {
		this.clubName = clubName;
	}
	public String getClubDesc() {
		return clubDesc;
	}
	public void setClubDesc(String clubDesc) {
		this.clubDesc = clubDesc;
	}
	public String getClubAddr() {
		return clubAddr;
	}
	public void setClubAddr(String clubAddr) {
		this.clubAddr = clubAddr;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getClubImg() {
		return clubImg;
	}
	public void setClubImg(String clubImg) {
		this.clubImg = clubImg;
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
	public Integer getEnableStatus() {
		return enableStatus;
	}
	public void setEnableStatus(Integer enableStatus) {
		this.enableStatus = enableStatus;
	}
	public String getAdvice() {
		return advice;
	}
	public void setAdvice(String advice) {
		this.advice = advice;
	}
	public Area getArea() {
		return area;
	}
	public void setArea(Area area) {
		this.area = area;
	}
	public PersonInfo getCaptain() {
		return captain;
	}
	public void setCaptain(PersonInfo captain) {
		this.captain = captain;
	}
	public ClubCategory getClubCategory() {
		return clubCategory;
	}
	public void setClubCategory(ClubCategory clubCategory) {
		this.clubCategory = clubCategory;
	}
	private Area area;
	private PersonInfo captain;
	private ClubCategory clubCategory;

}
