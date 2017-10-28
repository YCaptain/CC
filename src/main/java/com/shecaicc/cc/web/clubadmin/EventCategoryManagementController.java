package com.shecaicc.cc.web.clubadmin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.shecaicc.cc.dto.EventCategoryExecution;
import com.shecaicc.cc.dto.Result;
import com.shecaicc.cc.entity.Club;
import com.shecaicc.cc.entity.EventCategory;
import com.shecaicc.cc.enums.EventCategoryStateEnum;
import com.shecaicc.cc.exceptions.EventCategoryOperationException;
import com.shecaicc.cc.service.EventCategoryService;

@Controller
@RequestMapping("/clubadmin")
public class EventCategoryManagementController {
	@Autowired
	private EventCategoryService eventCategoryService;

	@RequestMapping(value = "/geteventcategorylist", method = RequestMethod.GET)
	@ResponseBody
	private Result<List<EventCategory>> getEventCategoryList(HttpServletRequest request) {
		Club currentClub = (Club) request.getSession().getAttribute("currentClub");
		List<EventCategory> list = null;
		if (currentClub != null && currentClub.getClubId() > 0) {
			list = eventCategoryService.getEventCategoryList(currentClub.getClubId());
			return new Result<List<EventCategory>>(true, list);
		} else {
			EventCategoryStateEnum es = EventCategoryStateEnum.INNER_ERROR;
			return new Result<List<EventCategory>>(false, es.getState(), es.getStateInfo());
		}
	}

	@RequestMapping(value = "/addeventcategory", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> addEventCategory(@RequestBody List<EventCategory> eventCategoryList,
			HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		Club currentClub = (Club) request.getSession().getAttribute("currentClub");
		for (EventCategory ec : eventCategoryList) {
			ec.setClubId(currentClub.getClubId());
		}
		if (eventCategoryList != null && eventCategoryList.size() > 0) {
			try {
				EventCategoryExecution ee = eventCategoryService.batchAddEventCategory(eventCategoryList);
				if (ee.getState() == EventCategoryStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
				} else {
					modelMap.put("success", false);
					modelMap.put("errMsg", ee.getStateInfo());
				}
			} catch (RuntimeException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
				return modelMap;
			}
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "请至少输入一个活动类别");
		}
		return modelMap;
	}

	@RequestMapping(value = "/removeeventcategory", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> removeEventCategory(Long eventCategoryId,
			HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		Club currentClub = (Club) request.getSession().getAttribute("currentClub");
		if (eventCategoryId != null && eventCategoryId > 0) {
			try {
				EventCategoryExecution ee = eventCategoryService.deleteEventCategory(eventCategoryId, currentClub.getClubId());
				if (ee.getState() == EventCategoryStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
				} else {
					modelMap.put("success", false);
					modelMap.put("errMsg", ee.getStateInfo());
				}
			} catch (EventCategoryOperationException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
				return modelMap;
			}
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "请至少输入一个活动类别");
		}
		return modelMap;
	}
}
