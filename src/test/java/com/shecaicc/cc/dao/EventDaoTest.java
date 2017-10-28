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
import com.shecaicc.cc.entity.Club;
import com.shecaicc.cc.entity.Event;
import com.shecaicc.cc.entity.EventCategory;
import com.shecaicc.cc.entity.EventImg;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class EventDaoTest extends BaseTest {
	@Autowired
	private EventDao eventDao;

	@Autowired
	private EventImgDao eventImgDao;

	@Test
	public void testAInsertEvent() throws Exception {
		Club club1 = new Club();
		club1.setClubId(28L);
		EventCategory ec1 = new EventCategory();
		ec1.setEventCategoryId(6L);

		Event event1 = new Event();
		event1.setEventName("测试1");
		event1.setEventDesc("测试desc1");
		event1.setImgAddr("测试addr1");
		event1.setPriority(1);
		event1.setEnableStatus(1);
		event1.setCreateTime(new Date());
		event1.setLastEditTime(new Date());
		event1.setClub(club1);
		event1.setEventCategory(ec1);
		event1.setCapacity(10);
		event1.setNumPerson(0);
		Event event2 = new Event();
		event2.setEventName("测试2");
		event2.setEventDesc("测试desc2");
		event2.setImgAddr("测试addr2");
		event2.setPriority(2);
		event2.setEnableStatus(0);
		event2.setCreateTime(new Date());
		event2.setLastEditTime(new Date());
		event2.setClub(club1);
		event2.setEventCategory(ec1);
		event2.setCapacity(10);
		event2.setNumPerson(0);
		Event event3 = new Event();
		event3.setEventName("测试3");
		event3.setEventDesc("测试desc3");
		event3.setImgAddr("测试addr3");
		event3.setPriority(3);
		event3.setEnableStatus(1);
		event3.setCreateTime(new Date());
		event3.setLastEditTime(new Date());
		event3.setClub(club1);
		event3.setEventCategory(ec1);
		event3.setCapacity(10);
		event3.setNumPerson(0);
		int effectedNum = eventDao.insertEvent(event1);
		assertEquals(1, effectedNum);
		effectedNum = eventDao.insertEvent(event2);
		assertEquals(1, effectedNum);
		effectedNum = eventDao.insertEvent(event3);
		assertEquals(1, effectedNum);
	}

	@Test
	public void testBQueryEventList() throws Exception {
		Event eventCondition = new Event();
		List<Event> eventList = eventDao.queryEventList(eventCondition, 0, 3);
		assertEquals(3, eventList.size());
		int count = eventDao.queryEventCount(eventCondition);
		assertEquals(3, count);
		eventCondition.setEventName("测试");
		eventList = eventDao.queryEventList(eventCondition, 0, 3);
		assertEquals(3, eventList.size());
	}

	@Test
	public void testCQueryEventByEventId() throws Exception {
		long eventId = 9;

		EventImg eventImg1 = new EventImg();
		eventImg1.setImgAddr("图片1");
		eventImg1.setImgDesc("测试图片1");
		eventImg1.setPriority(1);
		eventImg1.setCreateTime(new Date());
		eventImg1.setEventId(eventId);
		EventImg eventImg2 = new EventImg();
		eventImg2.setImgAddr("图片2");
		eventImg2.setImgDesc("测试图片2");
		eventImg2.setPriority(2);
		eventImg2.setCreateTime(new Date());
		eventImg2.setEventId(eventId);
		List<EventImg> eventImgList = new ArrayList<EventImg>();
		eventImgList.add(eventImg1);
		eventImgList.add(eventImg2);
		int effectedNum = eventImgDao.batchInsertEventImg(eventImgList);
		assertEquals(2, effectedNum);

		Event event = eventDao.queryEventById(eventId);
		assertEquals(2, event.getEventImgList().size());
		effectedNum = eventImgDao.deleteEventImgByEventId(eventId);
		assertEquals(2, effectedNum);
	}

	@Test
	public void testDUpdateEvent() throws Exception {
		Event event = new Event();
		EventCategory ec = new EventCategory();
		Club club = new Club();
		club.setClubId(28L);
		ec.setEventCategoryId(6L);
		event.setEventId(28L);
		event.setClub(club);
		event.setEventName("第二个活动");
		event.setEventCategory(ec);

		int effectedNum = eventDao.updateEvent(event);
		assertEquals(1, effectedNum);
	}

	@Test
	public void testEUpdateEventCategoryToNull() {
		// 将eventCategoryId为6的活动类别下面的活动的活动类别置为空
		int effectedNum = eventDao.updateEventCategoryToNull(6L);
		assertEquals(0, effectedNum);
	}

}
