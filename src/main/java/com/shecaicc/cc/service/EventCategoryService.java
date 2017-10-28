package com.shecaicc.cc.service;

import java.util.List;

import com.shecaicc.cc.dto.EventCategoryExecution;
import com.shecaicc.cc.entity.EventCategory;
import com.shecaicc.cc.exceptions.EventCategoryOperationException;

public interface EventCategoryService {
	/**
	 * 查询某个指定社团下的所有活动类别信息
	 *
	 * @param clubId
	 * @return
	 */
	List<EventCategory> getEventCategoryList(long clubId);

	/**
	 * 批量添加活动类别
	 *
	 * @param eventCategoryList
	 * @return
	 * @throws EventCategoryOperationException
	 */
	EventCategoryExecution batchAddEventCategory(List<EventCategory> eventCategoryList)
			throws EventCategoryOperationException;

	/**
	 * 将此类别下活动的类别id置为空，再删除该活动类别
	 *
	 * @param eventCategoryId
	 * @param clubId
	 * @return
	 * @throws EventCategoryOperationException
	 */
	EventCategoryExecution deleteEventCategory(long eventCategoryId, long clubId)
			throws EventCategoryOperationException;
}
