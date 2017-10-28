package com.shecaicc.cc.service;

import java.util.List;

import com.shecaicc.cc.dto.EventExecution;
import com.shecaicc.cc.dto.ImageHolder;
import com.shecaicc.cc.entity.Event;
import com.shecaicc.cc.exceptions.EventOperationException;

public interface EventService {
	/**
	 * 分页查询活动列表
	 *
	 * @param eventCondition
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	EventExecution getEventList(Event eventCondition, int pageIndex, int pageSize);

	/**
	 * 通过活动Id查询唯一的活动信息
	 *
	 * @param eventId
	 * @return
	 */
	Event getEventById(long eventId);

	/**
	 * 添加活动信息以及图片处理
	 *
	 * @return
	 * @throws EventOperationException
	 */
	EventExecution addEvent(Event event, ImageHolder thumbnail, List<ImageHolder> eventImgHolderList)
			throws EventOperationException;

	/**
	 * 修改活动信息以及图片处理
	 *
	 * @param event
	 * @param thumbnail
	 * @param eventImgHolderList
	 * @return
	 * @throws EventOperationException
	 */
	EventExecution modifyEvent(Event event, ImageHolder thumbnail, List<ImageHolder> eventImgHolderList)
			throws EventOperationException;
}
