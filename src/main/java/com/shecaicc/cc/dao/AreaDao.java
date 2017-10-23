package com.shecaicc.cc.dao;

import java.util.List;

import com.shecaicc.cc.entity.Area;

public interface AreaDao {
	/**
	 * 列出区域列表
	 * @return areaList
	 */
	List<Area> queryArea();
}
