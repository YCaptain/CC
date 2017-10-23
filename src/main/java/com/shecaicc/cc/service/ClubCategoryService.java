package com.shecaicc.cc.service;

import java.util.List;

import com.shecaicc.cc.entity.ClubCategory;

public interface ClubCategoryService {
	List<ClubCategory> getClubCategoryList(ClubCategory clubCategoryCondition);
}
