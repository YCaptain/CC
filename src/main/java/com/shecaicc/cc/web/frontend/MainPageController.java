package com.shecaicc.cc.web.frontend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.shecaicc.cc.entity.ClubCategory;
import com.shecaicc.cc.entity.HeadLine;
import com.shecaicc.cc.service.ClubCategoryService;
import com.shecaicc.cc.service.HeadLineService;

@Controller
@RequestMapping("/frontend")
public class MainPageController {
	@Autowired
	private ClubCategoryService clubCategoryService;
	@Autowired
	private HeadLineService headLineService;

	/**
	 * 初始化前端展示系统的主页信息，包括获取一级社团类别列表以及头条列表
	 *
	 * @return
	 */
	@RequestMapping(value = "/listmainpageinfo", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> listMainPageInfo() {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		List<ClubCategory> clubCategoryList = new ArrayList<ClubCategory>();
		try {
			// 获取一级社团类别列表(即parentId为空的ClubCategory)
			clubCategoryList = clubCategoryService.getClubCategoryList(null);
			modelMap.put("clubCategoryList", clubCategoryList);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
			return modelMap;
		}
		List<HeadLine> headLineList = new ArrayList<HeadLine>();
		try {
			// 获取状态为可用(1)的头条列表
			HeadLine headLineCondition = new HeadLine();
			headLineCondition.setEnableStatus(1);
			headLineList = headLineService.getHeadLineList(headLineCondition);
			modelMap.put("headLineList", headLineList);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
			return modelMap;
		}
		modelMap.put("success", true);
		return modelMap;
	}

}
