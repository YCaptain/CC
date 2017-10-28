package com.shecaicc.cc.dao;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

import com.shecaicc.cc.BaseTest;
import com.shecaicc.cc.entity.EventCategory;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class EventCategoryDaoTest extends BaseTest {
	@Autowired
	private EventCategoryDao eventCategoryDao;

	@Test
	public void testBQueryById() throws Exception {
		long clubId = 28L;
		List<EventCategory> eventCategoryList = eventCategoryDao.queryEventCategoryList(clubId);
		System.out.println("该社团自定义类别数为: " + eventCategoryList.size());
	}

	@Test
	public void testABatchInsertEventCategory() {
		EventCategory eventCategory = new EventCategory();
		eventCategory.setEventCategoryName("活动类别4");
		eventCategory.setPriority(1);
		eventCategory.setCreateTime(new Date());
		eventCategory.setClubId(28L);
		EventCategory eventCategory2 = new EventCategory();
		eventCategory2.setEventCategoryName("活动类别5");
		eventCategory2.setPriority(2);
		eventCategory2.setCreateTime(new Date());
		eventCategory2.setClubId(28L);
		List<EventCategory> eventCategoryList = new ArrayList<EventCategory>();
		eventCategoryList.add(eventCategory);
		eventCategoryList.add(eventCategory2);
		int effectedNum = eventCategoryDao.batchInsertEventCategory(eventCategoryList);
		assertEquals(2, effectedNum);
	}

	@Test
	public void deleteCEventCategory() {
		long clubId = 28;
		List<EventCategory> eventCategoryList = eventCategoryDao.queryEventCategoryList(clubId);
		for (EventCategory ec: eventCategoryList) {
			if ("活动类别4".equals(ec.getEventCategoryName()) || "活动类别5".equals(ec.getEventCategoryName())) {
				int effectedNum = eventCategoryDao.deleteEventCategory(ec.getEventCategoryId(), clubId);
				assertEquals(1, effectedNum);
			}
		}
	}

}
