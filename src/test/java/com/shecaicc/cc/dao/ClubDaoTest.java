package com.shecaicc.cc.dao;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.shecaicc.cc.BaseTest;
import com.shecaicc.cc.entity.Area;
import com.shecaicc.cc.entity.Club;
import com.shecaicc.cc.entity.ClubCategory;
import com.shecaicc.cc.entity.PersonInfo;

public class ClubDaoTest extends BaseTest {
	@Autowired
	private ClubDao clubDao;

	@Test
	public void testQueryClubListAndCount() {
		Club clubCondition = new Club();
		ClubCategory childCategory = new ClubCategory();
		ClubCategory parentCategory = new ClubCategory();
		parentCategory.setClubCategoryId(1L);
		childCategory.setParent(parentCategory);
		clubCondition.setClubCategory(childCategory);
		List<Club> clubList = clubDao.queryClubList(clubCondition, 0, 5);
		int count = clubDao.queryClubCount(clubCondition);
		System.out.println("社团列表的大小: " + clubList.size());
		System.out.println("社团总数: " + count);
		ClubCategory cc = new ClubCategory();
		cc.setClubCategoryId(2L);
		clubList = clubDao.queryClubList(clubCondition, 0, 2);
		count = clubDao.queryClubCount(clubCondition);
		System.out.println("X社团列表的大小: " + clubList.size());
		System.out.println("X社团总数: " + count);
	}

	@Test
	public void testQueryByClubId() {
		long clubId = 12;
		Club club = clubDao.queryByClubId(clubId);
		System.out.println("areaId: " + club.getArea().getAreaId());
		System.out.println("areaName: " + club.getArea().getAreaName());
	}

	@Test
	public void testInsertClub() {
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
		club.setClubName("LF Pakour");
		club.setClubDesc("test");
		club.setClubAddr("test");
		club.setPhone("test");
		club.setClubImg("test");
		club.setCreateTime(new Date());
		club.setEnableStatus(1);
		club.setAdvice("审核中");
		int effectedNum = clubDao.insertClub(club);
		assertEquals(1, effectedNum);
	}

	@Test
	public void testUpdateClub() {
		Club club = new Club();
		club.setClubId(2L);
		club.setClubDesc("测试描述");
		club.setClubAddr("测试地址");
		club.setLastEditTime(new Date());
		int effectedNum = clubDao.updateClub(club);
		assertEquals(1, effectedNum);
	}
}
