package com.shecaicc.cc.dao;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.shecaicc.cc.BaseTest;
import com.shecaicc.cc.entity.ClubCategory;

public class ClubCategoryDaoTest extends BaseTest {
	@Autowired
	private ClubCategoryDao clubCategoryDao;

	@Test
	public void testQueryClubCategory() {
		List<ClubCategory> clubCategoryList = clubCategoryDao.queryClubCategory(null);
		//		assertEquals(2, clubCategoryList.size());
		//		ClubCategory testCategory = new ClubCategory();
		//		ClubCategory parentCategory = new ClubCategory();
		//		parentCategory.setClubCategoryId(1L);
		//		testCategory.setParent(parentCategory);
		//		clubCategoryList = clubCategoryDao.queryClubCategory(testCategory);
		//		assertEquals(1, clubCategoryList.size());
		//		System.out.println(clubCategoryList.get(0).getClubCategoryName());
		assertEquals(1, clubCategoryList.size());
	}
}
