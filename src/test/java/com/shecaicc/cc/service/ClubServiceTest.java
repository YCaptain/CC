package com.shecaicc.cc.service;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.shecaicc.cc.BaseTest;
import com.shecaicc.cc.dto.ClubExecution;
import com.shecaicc.cc.dto.ImageHolder;
import com.shecaicc.cc.entity.Area;
import com.shecaicc.cc.entity.Club;
import com.shecaicc.cc.entity.ClubCategory;
import com.shecaicc.cc.entity.PersonInfo;
import com.shecaicc.cc.enums.ClubStateEnum;
import com.shecaicc.cc.exceptions.ClubOperationException;

public class ClubServiceTest extends BaseTest {
	@Autowired
	private ClubService clubService;

	@Test
	public void testGetClubList() {
		Club clubCondition = new Club();
		ClubCategory cc = new ClubCategory();
		cc.setClubCategoryId(2L);
		ClubExecution ce = clubService.getClubList(clubCondition, 6, 3);
		System.out.println("社团列表数为: " + ce.getClubList().size());
		System.out.println("社团总数为: " + ce.getCount());
	}

	@Test
	public void testModifyClub() throws FileNotFoundException {
		Club club = new Club();
		club.setClubId(28L);
		club.setClubName("修改后的社团名称");
		File clubImg = new File("D:\\学习\\素材\\跑酷社.jpg");
		InputStream is = new FileInputStream(clubImg);
		ImageHolder imageHolder = new ImageHolder("跑酷社.jpg", is);
		ClubExecution clubExecution = clubService.modifyClub(club, imageHolder);
		System.out.println("新的图片地址为: " + clubExecution.getClub().getClubImg());
	}

	@Test
	public void testAddClub() throws ClubOperationException, FileNotFoundException {
		Club club = new Club();
		PersonInfo captain = new PersonInfo();
		Area area = new Area();
		ClubCategory clubCategory = new ClubCategory();
		captain.setUserId(1L);
		area.setAreaId(2);
		clubCategory.setClubCategoryId(1L);
		club.setCaptain(captain);
		club.setArea(area);
		club.setClubCategory(clubCategory);
		club.setClubName("篮球社");
		club.setClubDesc("灌篮高手是篮板王");
		club.setClubAddr("本校区");
		club.setPhone("18812344321");
		club.setCreateTime(new Date());
		club.setEnableStatus(ClubStateEnum.CHECK.getState());
		club.setAdvice("审核中");
		File clubImg = new File("D:/学习/素材/basketball.jpg");
		InputStream is = new FileInputStream(clubImg);
		ImageHolder imageHolder = new ImageHolder("basketball.jpg", is);
		ClubExecution clubExecution = clubService.addClub(club, imageHolder);
		assertEquals(ClubStateEnum.CHECK.getState(), clubExecution.getState());
	}

}
