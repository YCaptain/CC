package com.shecaicc.cc.web.frontend;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.shecaicc.cc.dto.ClubExecution;
import com.shecaicc.cc.entity.Area;
import com.shecaicc.cc.entity.Club;
import com.shecaicc.cc.entity.ClubCategory;
import com.shecaicc.cc.service.AreaService;
import com.shecaicc.cc.service.ClubCategoryService;
import com.shecaicc.cc.service.ClubService;
import com.shecaicc.cc.util.HttpServletRequestUtil;

@Controller
@RequestMapping("/frontend")
public class ClubListController {
	@Autowired
	private AreaService areaService;
	@Autowired
	private ClubCategoryService clubCategoryService;
	@Autowired
	private ClubService clubService;

	/**
	 * 返回活动列表页里的ClubCategory列表(二级或者一级)，以及区域信息列表
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/listclubspageinfo", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> listClubsPageInfo(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		// 试着从前端请求中获取parentId
		long parentId = HttpServletRequestUtil.getLong(request, "parentId");
		List<ClubCategory> clubCategoryList = null;
		if (parentId != -1) {
			// 如果parentId存在，则取出该一级ClubCategory下的二级ClubCategory列表
			try {
				ClubCategory clubCategoryCondition = new ClubCategory();
				ClubCategory parent = new ClubCategory();
				parent.setClubCategoryId(parentId);
				clubCategoryCondition.setParent(parent);
				clubCategoryList = clubCategoryService.getClubCategoryList(clubCategoryCondition);
			} catch (Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
			}
		} else {
			try {
				// 如果parentId不存在，则取出所有一级ClubCategory(用户在首页选择的是全部社团列表)
				clubCategoryList = clubCategoryService.getClubCategoryList(null);
			} catch (Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
			}
		}
		modelMap.put("clubCategoryList", clubCategoryList);
		List<Area> areaList = null;
		try {
			// 获取区域列表信息
			areaList = areaService.getAreaList();
			modelMap.put("areaList", areaList);
			modelMap.put("success", true);
			return modelMap;
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
		}
		return modelMap;
	}

	/**
	 * 获取指定查询条件下的社团列表
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/listclubs", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> listClubs(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		// 获取页码
		int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
		// 获取一页需要显示的数据条数
		int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
		// 非空判断
		if ((pageIndex > -1) && (pageSize > -1)) {
			// 试着获取一级类别Id
			long parentId = HttpServletRequestUtil.getLong(request, "parentId");
			// 试着获取特定二级类别Id
			long clubCategoryId = HttpServletRequestUtil.getLong(request, "clubCategoryId");
			// 试着获取区域Id
			int areaId = HttpServletRequestUtil.getInt(request, "areaId");
			// 试着获取模糊查询的名字
			String clubName = HttpServletRequestUtil.getString(request, "clubName");
			// 获取组合之后的查询条件
			Club clubCondition = compactClubCondition4Search(parentId, clubCategoryId, areaId, clubName);
			// 根据查询条件和分页信息获取社团列表，并返回总数
			ClubExecution se = clubService.getClubList(clubCondition, pageIndex, pageSize);
			modelMap.put("clubList", se.getClubList());
			modelMap.put("count", se.getCount());
			modelMap.put("success", true);
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "empty pageSize or pageIndex");
		}

		return modelMap;
	}

	/**
	 * 组合查询条件，并将条件封装到ClubCondition对象里返回
	 *
	 * @param parentId
	 * @param clubCategoryId
	 * @param areaId
	 * @param clubName
	 * @return
	 */
	private Club compactClubCondition4Search(long parentId, long clubCategoryId, int areaId, String clubName) {
		Club clubCondition = new Club();
		if (parentId != -1L) {
			// 查询某个一级ClubCategory下面的所有二级ClubCategory里面的社团列表
			ClubCategory childCategory = new ClubCategory();
			ClubCategory parentCategory = new ClubCategory();
			parentCategory.setClubCategoryId(parentId);
			childCategory.setParent(parentCategory);
			clubCondition.setClubCategory(childCategory);
		}
		if (clubCategoryId != -1L) {
			// 查询某个二级ClubCategory下面的社团列表
			ClubCategory clubCategory = new ClubCategory();
			clubCategory.setClubCategoryId(clubCategoryId);
			clubCondition.setClubCategory(clubCategory);
		}
		if (areaId != -1L) {
			// 查询位于某个区域Id下的社团列表
			Area area = new Area();
			area.setAreaId(areaId);
			clubCondition.setArea(area);
		}

		if (clubName != null) {
			// 查询名字里包含clubName的社团列表
			clubCondition.setClubName(clubName);
		}
		// 前端展示的社团都是审核成功的社团
		clubCondition.setEnableStatus(1);
		return clubCondition;
	}
}
