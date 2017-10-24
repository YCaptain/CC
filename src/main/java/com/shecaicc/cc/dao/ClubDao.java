package com.shecaicc.cc.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.shecaicc.cc.entity.Club;

public interface ClubDao {
	/**
	 * 分页查询社团：店铺名(模糊), 店铺状态, 店铺类别, 区域Id, captain
	 * @param clubCondition
	 * @param rowIndex 从rowIndex行开始获取数据
	 * @param pageSize 返回条数
	 * @return
	 */
	List<Club> queryClubList(@Param("clubCondition") Club clubCondition, @Param("rowIndex") int rowIndex, @Param("pageSize") int pageSize);

	/**
	 * 返回queryClubList总数
	 * @param clubCondition
	 * @return
	 */
	int queryClubCount(@Param("clubCondition") Club clubCondition);

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
