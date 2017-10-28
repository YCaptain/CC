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
import com.shecaicc.cc.entity.EventImg;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class EventImgDaoTest extends BaseTest {
	@Autowired
	private EventImgDao eventImgDao;

	@Test
	public void testABatchInsertEventImg() throws Exception {
		EventImg eventImg1 = new EventImg();
		eventImg1.setImgAddr("图片1");
		eventImg1.setImgDesc("测试图片1");
		eventImg1.setPriority(1);
		eventImg1.setCreateTime(new Date());
		eventImg1.setEventId(9L);
		EventImg eventImg2 = new EventImg();
		eventImg2.setImgAddr("图片2");
		eventImg2.setImgDesc("测试图片2");
		eventImg2.setPriority(2);
		eventImg2.setCreateTime(new Date());
		eventImg2.setEventId(9L);
		List<EventImg> eventImgList = new ArrayList<EventImg>();
		eventImgList.add(eventImg1);
		eventImgList.add(eventImg2);
		int effectedNum = eventImgDao.batchInsertEventImg(eventImgList);
		assertEquals(2, effectedNum);
	}

	@Test
	public void testBQueryEventImgList() {
		List<EventImg> eventImgList = eventImgDao.queryEventImgList(9L);
		assertEquals(2, eventImgList.size());
	}

	@Test
	public void testCDeleteEventImgByEventId() throws Exception {
		long eventId = 6;
		int effectedNum = eventImgDao.deleteEventImgByEventId(eventId);
		assertEquals(2, effectedNum);
	}

}
