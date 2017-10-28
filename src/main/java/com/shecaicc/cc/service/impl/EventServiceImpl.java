package com.shecaicc.cc.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shecaicc.cc.dao.EventDao;
import com.shecaicc.cc.dao.EventImgDao;
import com.shecaicc.cc.dto.EventExecution;
import com.shecaicc.cc.dto.ImageHolder;
import com.shecaicc.cc.entity.Event;
import com.shecaicc.cc.entity.EventImg;
import com.shecaicc.cc.enums.EventStateEnum;
import com.shecaicc.cc.exceptions.EventOperationException;
import com.shecaicc.cc.service.EventService;
import com.shecaicc.cc.util.ImageUtil;
import com.shecaicc.cc.util.PageCalculator;
import com.shecaicc.cc.util.PathUtil;

@Service
public class EventServiceImpl implements EventService {
	@Autowired
	private EventDao eventDao;

	@Autowired
	private EventImgDao eventImgDao;

	@Override
	@Transactional
	// 1. 处理缩略图
	// 2. 活动信息写入数据库
	// 3. 批量处理活动图片
	// 4. 活动图片写入数据库
	public EventExecution addEvent(Event event, ImageHolder thumbnail, List<ImageHolder> eventImgHolderList)
			throws EventOperationException {
		if (event != null && event.getClub() != null && event.getClub().getClubId() != null) {
			// 设置默认属性
			event.setCreateTime(new Date());
			event.setLastEditTime(new Date());
			event.setEnableStatus(1);
			// 若活动缩略图不为空则添加
			if (thumbnail != null) {
				addThumbnail(event, thumbnail);
			}
			try {
				// 创建活动信息
				int effectedNum = eventDao.insertEvent(event);
				if (effectedNum <= 0) {
					throw new EventOperationException("创建活动失败");
				}
			} catch (Exception e) {
				throw new EventOperationException("创建活动失败: " + e.toString());
			}
			// 若活动详情图不为空则添加
			if (eventImgHolderList != null && eventImgHolderList.size() > 0) {
				addEventImgList(event, eventImgHolderList);
			}
			return new EventExecution(EventStateEnum.SUCCESS, event);
		} else {
			return new EventExecution(EventStateEnum.EMPTY);
		}
	}

	/**
	 * 添加缩略图
	 *
	 * @param event
	 * @param thumbnail
	 */
	private void addThumbnail(Event event, ImageHolder thumbnail) {
		String dest = PathUtil.getClubImagePath(event.getClub().getClubId());
		String thumbnailAddr = ImageUtil.generateThumbnail(thumbnail, dest);
		event.setImgAddr(thumbnailAddr);
	}

	private void addEventImgList(Event event, List<ImageHolder> eventImgHolderList) {
		String dest = PathUtil.getClubImagePath(event.getClub().getClubId());
		List<EventImg> eventImgList = new ArrayList<EventImg>();
		for (ImageHolder eventImgHolder : eventImgHolderList) {
			String imgAddr = ImageUtil.generateNormalImg(eventImgHolder, dest);
			EventImg EventImg = new EventImg();
			EventImg.setImgAddr(imgAddr);
			EventImg.setEventId(event.getEventId());
			EventImg.setCreateTime(new Date());
			eventImgList.add(EventImg);
		}
		if (eventImgList.size() > 0) {
			try {
				int effectedNum = eventImgDao.batchInsertEventImg(eventImgList);
				if (effectedNum <= 0) {
					throw new EventOperationException("创建活动详情图片失败");
				}
			} catch (Exception e) {
				throw new EventOperationException("创建活动详情图片失败: " + e.toString());
			}
		}
	}

	@Override
	public EventExecution getEventList(Event eventCondition, int pageIndex, int pageSize) {
		int rowIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
		List<Event> eventList = eventDao.queryEventList(eventCondition, rowIndex, pageSize);
		int count = eventDao.queryEventCount(eventCondition);
		EventExecution ee = new EventExecution();
		ee.setEventList(eventList);
		ee.setCount(count);
		return ee;
	}

	@Override
	public Event getEventById(long eventId) {
		return eventDao.queryEventById(eventId);
	}

	@Override
	@Transactional
	// 1.若缩略图有值，先处理
	// 若已存在缩略图，先删除再添加
	// 2.若详情图有值，处理
	// 3.清除原详情图
	// 4.更新信息
	public EventExecution modifyEvent(Event event, ImageHolder thumbnail, List<ImageHolder> eventImgHolderList)
			throws EventOperationException {
		// 空值判断
		if (event != null && event.getClub() != null && event.getClub().getClubId() != null) {
			// 设置默认属性
			event.setLastEditTime(new Date());
			// 处理缩略图
			if (thumbnail != null) {
				Event tempEvent = eventDao.queryEventById(event.getEventId());
				if (tempEvent.getImgAddr() != null) {
					ImageUtil.deleteFileOrPath(tempEvent.getImgAddr());
				}
				addThumbnail(event, thumbnail);
			}
			// 处理详情图
			if (eventImgHolderList != null && eventImgHolderList.size() > 0) {
				deleteEventImgList(event.getEventId());
				addEventImgList(event, eventImgHolderList);
			}
			try {
				int effectedNum = eventDao.updateEvent(event);
				if (effectedNum <= 0) {
					throw new EventOperationException("更新活动信息失败");
				}
				return new EventExecution(EventStateEnum.SUCCESS, event);
			} catch (Exception e) {
				throw new EventOperationException("更新活动信息失败: " + e.toString());
			}
		} else {
			return new EventExecution(EventStateEnum.EMPTY);
		}
	}

	private void deleteEventImgList(long eventId) {
		List<EventImg> eventImgList = eventImgDao.queryEventImgList(eventId);
		for (EventImg eventImg : eventImgList) {
			ImageUtil.deleteFileOrPath(eventImg.getImgAddr());
		}
		eventImgDao.deleteEventImgByEventId(eventId);
	}

}
