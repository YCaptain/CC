package com.shecaicc.cc.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shecaicc.cc.dao.ClubCategoryDao;
import com.shecaicc.cc.entity.ClubCategory;
import com.shecaicc.cc.service.ClubCategoryService;

@Service
public class ClubCategoryServiceImpl implements ClubCategoryService {
	@Autowired
	private ClubCategoryDao clubCaegoryDao;

	@Override
	public List<ClubCategory> getClubCategoryList(ClubCategory clubCategoryCondition) {
		return clubCaegoryDao.queryClubCategory(clubCategoryCondition);
	}

}
