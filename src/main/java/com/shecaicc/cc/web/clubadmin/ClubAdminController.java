package com.shecaicc.cc.web.clubadmin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "clubadmin", method = { RequestMethod.GET })
public class ClubAdminController {
	@RequestMapping(value = "/cluboperation")
	public String clubOperation() {
		return "club/cluboperation";
	}

	@RequestMapping(value = "/clublist")
	public String clubList() {
		return "club/clublist";
	}

	@RequestMapping(value = "/clubmanagement")
	public String clubManagement() {
		return "club/clubmanagement";
	}
}
