package com.shecaicc.cc.web.frontend;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.shecaicc.cc.entity.Event;
import com.shecaicc.cc.service.EventService;
import com.shecaicc.cc.util.HttpServletRequestUtil;

@Controller
@RequestMapping("/frontend")
public class EventDetailController {
	@Autowired
	private EventService eventService;

	/**
	 * 根据活动Id获取活动详情
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/listeventdetailpageinfo", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> listEventDetailPageInfo(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		// 获取前台传递过来的eventId
		long eventId = HttpServletRequestUtil.getLong(request, "eventId");
		Event event = null;
		// 空值判断
		if (eventId != -1) {
			// 根据eventId获取活动信息，包含活动详情图列表
			event = eventService.getEventById(eventId);
			modelMap.put("event", event);
			modelMap.put("success", true);
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "empty eventId");
		}
		return modelMap;
	}

}
