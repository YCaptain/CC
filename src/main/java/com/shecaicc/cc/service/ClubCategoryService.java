package com.shecaicc.cc.service;

import java.util.List;

import com.shecaicc.cc.entity.ClubCategory;

public interface ClubCategoryService {
	/**
	 * 根据查询条件获取社团类别列表
	 * @param clubCategoryCondition
	 * @return
	 */
	List<ClubCategory> getClubCategoryList(ClubCategory clubCategoryCondition);
}
