package com.shecaicc.cc.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.shecaicc.cc.entity.EventCategory;

public interface EventCategoryDao {
	/**
	 * 通过club id查询社团活动类别
	 * @param clubId
	 * @return
	 */
	List<EventCategory> queryEventCategoryList(long clubId);

	/**
	 * 批量新增活动类别
	 * @param eventCategoryList
	 * @return
	 */
	int batchInsertEventCategory(List<EventCategory> eventCategoryList);

	/**
	 * 删除指定活动类别
	 * @param eventCategoryId
	 * @param clubId
	 * @return
	 */
	int deleteEventCategory(@Param("eventCategoryId") long eventCategoryId, @Param("clubId") long clubId);
}
