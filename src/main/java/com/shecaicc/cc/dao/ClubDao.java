package com.shecaicc.cc.dao;

import com.shecaicc.cc.entity.Club;

public interface ClubDao {

	/**
	 * 新增社团
	 * @param club
	 * @return
	 */
	int insertClub(Club club);

	/**
	 * 更新社团信息
	 * @param club
	 * @return
	 */
	int updateClub(Club club);
}
