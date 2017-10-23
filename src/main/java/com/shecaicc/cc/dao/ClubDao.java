package com.shecaicc.cc.dao;

import com.shecaicc.cc.entity.Club;

public interface ClubDao {
	/**
	 * 通过club id查询社团
	 * @param clubId
	 * @return
	 */
	Club queryByClubId(long clubId);

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