package com.shecaicc.cc.dto;

import java.util.List;

import com.shecaicc.cc.entity.Club;
import com.shecaicc.cc.enums.ClubStateEnum;

public class ClubExecution {

	// 结果状态
	private int state;
	// 状态标识
	private String stateInfo;
	// 社团数量
	private int count;
	// 操作的club
	private Club club;
	// club列表
	private List<Club> clubList;

	public ClubExecution() {
	}

	// 社团操作失败时的使用的构造器
	public ClubExecution(ClubStateEnum stateEnum) {
		this.state = stateEnum.getState();
		this.stateInfo = stateEnum.getStateInfo();
	}

	// 社团操作成功时的使用的构造器
	public ClubExecution(ClubStateEnum stateEnum, Club club) {
		this.state = stateEnum.getState();
		this.stateInfo = stateEnum.getStateInfo();
		this.club = club;
	}

	// 社团操作成功时的使用的构造器
	public ClubExecution(ClubStateEnum stateEnum, List<Club> clubList) {
		this.state = stateEnum.getState();
		this.stateInfo = stateEnum.getStateInfo();
		this.clubList = clubList;
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

	public Club getClub() {
		return club;
	}

	public void setClub(Club club) {
		this.club = club;
	}

	public List<Club> getClubList() {
		return clubList;
	}

	public void setClubList(List<Club> clubList) {
		this.clubList = clubList;
	}

}
