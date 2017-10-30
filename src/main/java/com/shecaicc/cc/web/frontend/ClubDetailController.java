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

import com.shecaicc.cc.dto.EventExecution;
import com.shecaicc.cc.entity.Club;
import com.shecaicc.cc.entity.Event;
import com.shecaicc.cc.entity.EventCategory;
import com.shecaicc.cc.service.ClubService;
import com.shecaicc.cc.service.EventCategoryService;
import com.shecaicc.cc.service.EventService;
import com.shecaicc.cc.util.HttpServletRequestUtil;

@Controller
@RequestMapping("/frontend")
public class ClubDetailController {
	@Autowired
	private ClubService clubService;
	@Autowired
	private EventService eventService;
	@Autowired
	private EventCategoryService eventCategoryService;

	/**
	 * 获取社团信息以及该社团下面的活动类别列表
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/listclubdetailpageinfo", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> listClubDetailPageInfo(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		// 获取前台传过来的clubId
		long clubId = HttpServletRequestUtil.getLong(request, "clubId");
		Club club = null;
		List<EventCategory> eventCategoryList = null;
		if (clubId != -1) {
			// 获取社团Id为clubId的社团信息
			club = clubService.getByClubId(clubId);
			// 获取社团下面的活动类别列表
			eventCategoryList = eventCategoryService.getEventCategoryList(clubId);
			modelMap.put("club", club);
			modelMap.put("eventCategoryList", eventCategoryList);
			modelMap.put("success", true);
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "empty clubId");
		}
		return modelMap;
	}

	/**
	 * 依据查询条件分页列出该社团下面的所有活动
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/listeventsbyclub", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> listEventsByClub(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		// 获取页码
		int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
		// 获取一页需要显示的条数
		int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
		// 获取社团Id
		long clubId = HttpServletRequestUtil.getLong(request, "clubId");
		// 空值判断
		if ((pageIndex > -1) && (pageSize > -1) && (clubId > -1)) {
			// 尝试获取活动类别Id
			long eventCategoryId = HttpServletRequestUtil.getLong(request, "eventCategoryId");
			// 尝试获取模糊查找的活动名
			String eventName = HttpServletRequestUtil.getString(request, "eventName");
			// 组合查询条件
			Event eventCondition = compactEventCondition4Search(clubId, eventCategoryId, eventName);
			// 按照传入的查询条件以及分页信息返回相应活动列表以及总数
			EventExecution pe = eventService.getEventList(eventCondition, pageIndex, pageSize);
			modelMap.put("eventList", pe.getEventList());
			modelMap.put("count", pe.getCount());
			modelMap.put("success", true);
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "empty pageSize or pageIndex or clubId");
		}
		return modelMap;
	}

	/**
	 * 组合查询条件，并将条件封装到EventCondition对象里返回
	 *
	 * @param clubId
	 * @param eventCategoryId
	 * @param eventName
	 * @return
	 */
	private Event compactEventCondition4Search(long clubId, long eventCategoryId, String eventName) {
		Event eventCondition = new Event();
		Club club = new Club();
		club.setClubId(clubId);
		eventCondition.setClub(club);
		if (eventCategoryId != -1L) {
			// 查询某个活动类别下面的活动列表
			EventCategory eventCategory = new EventCategory();
			eventCategory.setEventCategoryId(eventCategoryId);
			eventCondition.setEventCategory(eventCategory);
		}
		if (eventName != null) {
			// 查询名字里包含eventName的社团列表
			eventCondition.setEventName(eventName);
		}
		// 只允许选出状态为上架的活动
		eventCondition.setEnableStatus(1);
		return eventCondition;
	}
}
