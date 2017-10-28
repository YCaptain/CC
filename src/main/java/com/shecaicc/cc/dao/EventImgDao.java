package com.shecaicc.cc.dao;

import java.util.List;

import com.shecaicc.cc.entity.EventImg;

public interface EventImgDao {

	List<EventImg> queryEventImgList(long eventId);

	/**
	 * 批量添加活动详情图片
	 *
	 * @param eventImgList
	 * @return
	 */
	int batchInsertEventImg(List<EventImg> eventImgList);

	/**
	 * 删除指定活动下的所有详情图
	 *
	 * @param eventId
	 * @return
	 */
	int deleteEventImgByEventId(long eventId);
}
