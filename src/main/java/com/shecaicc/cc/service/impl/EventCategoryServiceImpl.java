package com.shecaicc.cc.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shecaicc.cc.dao.EventCategoryDao;
import com.shecaicc.cc.dao.EventDao;
import com.shecaicc.cc.dto.EventCategoryExecution;
import com.shecaicc.cc.entity.EventCategory;
import com.shecaicc.cc.enums.EventCategoryStateEnum;
import com.shecaicc.cc.exceptions.EventCategoryOperationException;
import com.shecaicc.cc.service.EventCategoryService;

@Service
public class EventCategoryServiceImpl implements EventCategoryService {
	@Autowired
	private EventDao eventDao;
	@Autowired
	private EventCategoryDao eventCategoryDao;

	@Override
	public List<EventCategory> getEventCategoryList(long clubId) {
		return eventCategoryDao.queryEventCategoryList(clubId);
	}

	@Override
	@Transactional
	public EventCategoryExecution batchAddEventCategory(List<EventCategory> eventCategoryList)
			throws EventCategoryOperationException {
		if (eventCategoryList != null && eventCategoryList.size() > 0) {
			try {
				int effectedNum = eventCategoryDao.batchInsertEventCategory(eventCategoryList);
				if (effectedNum <= 0) {
					throw new EventCategoryOperationException("活动类别创建失败");
				} else {
					return new EventCategoryExecution(EventCategoryStateEnum.SUCCESS, eventCategoryList);
				}
			} catch (Exception e) {
				throw new EventCategoryOperationException("batchAddEventCategory error: " + e.getMessage());
			}
		} else {
			return new EventCategoryExecution(EventCategoryStateEnum.EMPTY_LIST);
		}
	}

	@Override
	@Transactional
	public EventCategoryExecution deleteEventCategory(long eventCategoryId, long clubId)
			throws EventCategoryOperationException {
		// 解除tb_event里的活动与该eventcategoryId的关联
		try {
			int effectedNum = eventDao.updateEventCategoryToNull(eventCategoryId);
			if (effectedNum < 0) {
				throw new EventCategoryOperationException("活动类别更新失败");
			}
		} catch (Exception e) {
			throw new EventCategoryOperationException("deleteEventCategory error: " + e.getMessage());
		}
		// 删除该eventCategoty
		try {
			int effectedNum = eventCategoryDao.deleteEventCategory(eventCategoryId, clubId);
			if (effectedNum <= 0) {
				throw new EventCategoryOperationException("活动类别删除失败");
			} else {
				return new EventCategoryExecution(EventCategoryStateEnum.SUCCESS);
			}
		} catch (Exception e) {
			throw new EventCategoryOperationException("deleteEventCategory error: " + e.getMessage());
		}
	}

}
