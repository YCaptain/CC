package com.shecaicc.cc.service;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.shecaicc.cc.BaseTest;
import com.shecaicc.cc.dto.EventExecution;
import com.shecaicc.cc.dto.ImageHolder;
import com.shecaicc.cc.entity.Club;
import com.shecaicc.cc.entity.Event;
import com.shecaicc.cc.entity.EventCategory;
import com.shecaicc.cc.enums.EventStateEnum;
import com.shecaicc.cc.exceptions.ClubOperationException;
import com.shecaicc.cc.exceptions.EventOperationException;

public class EventServiceTest extends BaseTest {
	@Autowired
	private EventService eventService;

	@Test
	public void testAddEvent() throws ClubOperationException, FileNotFoundException {
		// 创建clubId为28且eventCategoryId为6的活动实例
		Event event = new Event();
		Club club = new Club();
		club.setClubId(28L);
		EventCategory ec = new EventCategory();
		ec.setEventCategoryId(6L);
		event.setClub(club);
		event.setEventCategory(ec);
		event.setEventName("测试活动1");
		event.setEventDesc("测试活动1");
		event.setPriority(20);
		event.setCreateTime(new Date());
		event.setEnableStatus(EventStateEnum.SUCCESS.getState());
		event.setCapacity(20);
		event.setNumPerson(3);
		// 创建缩略图文件流
		File thumbnailFile = new File("D:\\学习\\素材\\rpg素材\\普通\\background\\assassin.jpg");
		InputStream is = new FileInputStream(thumbnailFile);
		ImageHolder thumbnail = new ImageHolder(thumbnailFile.getName(), is);
		// 创建两个活动详情文件流
		File eventImg1 = new File("D:\\学习\\素材\\rpg素材\\普通\\background\\tree.jpg");
		File eventImg2 = new File("D:\\学习\\素材\\rpg素材\\普通\\background\\demon.jpg");
		InputStream is1 = new FileInputStream(eventImg1);
		InputStream is2 = new FileInputStream(eventImg2);
		List<ImageHolder> eventImgList = new ArrayList<ImageHolder>();
		eventImgList.add(new ImageHolder(eventImg1.getName(), is1));
		eventImgList.add(new ImageHolder(eventImg2.getName(), is2));
		// 添加活动并验证
		EventExecution ee = eventService.addEvent(event, thumbnail, eventImgList);
		assertEquals(EventStateEnum.SUCCESS.getState(), ee.getState());
	}

	@Test
	public void testModifyEvent() throws EventOperationException, FileNotFoundException {
		// 创建活动实例
		Event event = new Event();
		Club club = new Club();
		club.setClubId(28L);
		EventCategory ec = new EventCategory();
		ec.setEventCategoryId(6L);
		event.setEventId(4L);
		event.setClub(club);
		event.setEventCategory(ec);
		event.setEventName("正式的活动");
		event.setEventDesc("正式的活动");
		// 创建缩略图文件流
		File thumbnailFile = new File("D:\\学习\\素材\\rpg素材\\普通\\background\\assassin.jpg");
		InputStream is = new FileInputStream(thumbnailFile);
		ImageHolder thumbnail = new ImageHolder(thumbnailFile.getName(), is);
		// 创建两个活动详情文件流
		File eventImg1 = new File("D:\\学习\\素材\\rpg素材\\普通\\background\\tree.jpg");
		File eventImg2 = new File("D:\\学习\\素材\\rpg素材\\普通\\background\\demon.jpg");
		InputStream is1 = new FileInputStream(eventImg1);
		InputStream is2 = new FileInputStream(eventImg2);
		List<ImageHolder> eventImgList = new ArrayList<ImageHolder>();
		eventImgList.add(new ImageHolder(eventImg1.getName(), is1));
		eventImgList.add(new ImageHolder(eventImg2.getName(), is2));
		// 添加活动并验证
		EventExecution ee = eventService.modifyEvent(event, thumbnail, eventImgList);
		assertEquals(EventStateEnum.SUCCESS.getState(), ee.getState());
	}
}
