package com.shecaicc.cc.web.frontend;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/frontend")
public class FrontendController {

	/**
	 * 首页路由
	 *
	 * @return
	 */
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	private String index() {
		return "frontend/index";
	}

	/**
	 * 活动列表页路由
	 *
	 * @return
	 */
	@RequestMapping(value = "/clublist", method = RequestMethod.GET)
	private String showClubList() {
		return "frontend/clublist";
	}

	/**
	 * 社团详情页路由
	 *
	 * @return
	 */
	@RequestMapping(value = "/clubdetail", method = RequestMethod.GET)
	private String showClubDetail() {
		return "frontend/clubdetail";
	}

	/**
	 * 活动详情页路由
	 *
	 * @return
	 */
	@RequestMapping(value = "/eventdetail", method = RequestMethod.GET)
	private String showEventDetail() {
		return "frontend/eventdetail";
	}
}
