package com.shecaicc.cc.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.shecaicc.cc.entity.Event;

public interface EventDao {
	/**
	 * 分页查询活动：活动名(模糊), 活动状态, 活动类别, 社团Id
	 *
	 * @param eventCondition
	 * @param rowIndex
	 * @param pageSize
	 * @return
	 */
	List<Event> queryEventList(@Param("eventCondition") Event eventCondition, @Param("rowIndex") int rowIndex,
			@Param("pageSize") int pageSize);

	/**
	 * 返回queryEventList总数
	 *
	 * @param eventCondition
	 * @return
	 */
	int queryEventCount(@Param("eventCondition") Event eventCondition);

	/**
	 * 通过event id查询活动
	 *
	 * @param eventId
	 * @return
	 */
	Event queryEventById(long eventId);

	/**
	 * 插入活动
	 *
	 * @param event
	 * @return
	 */
	int insertEvent(Event event);

	/**
	 * 更新活动信息
	 *
	 * @param event
	 * @return
	 */
	int updateEvent(Event event);

	/**
	 * 删除活动类别前，将活动类别Id置为空
	 * @param eventCategoryId
	 * @return
	 */
	int updateEventCategoryToNull(long eventCategoryId);

	/**
	 * 删除活动
	 *
	 * @param eventId
	 * @param clubId
	 * @return
	 */
	int deleteEvent(@Param("eventId") long eventId, @Param("clubId") long clubId);
}
