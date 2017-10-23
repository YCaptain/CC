package com.shecaicc.cc.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.shecaicc.cc.entity.ClubCategory;

public interface ClubCategoryDao {
	List<ClubCategory> queryClubCategory(@Param("clubCategoryCondition") ClubCategory clubCategoryCondition);
}
