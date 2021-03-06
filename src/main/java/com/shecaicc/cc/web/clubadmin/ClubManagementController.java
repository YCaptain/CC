package com.shecaicc.cc.web.clubadmin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shecaicc.cc.dto.ClubExecution;
import com.shecaicc.cc.dto.ImageHolder;
import com.shecaicc.cc.entity.Area;
import com.shecaicc.cc.entity.Club;
import com.shecaicc.cc.entity.ClubCategory;
import com.shecaicc.cc.entity.PersonInfo;
import com.shecaicc.cc.enums.ClubStateEnum;
import com.shecaicc.cc.exceptions.ClubOperationException;
import com.shecaicc.cc.service.AreaService;
import com.shecaicc.cc.service.ClubCategoryService;
import com.shecaicc.cc.service.ClubService;
import com.shecaicc.cc.util.CodeUtil;
import com.shecaicc.cc.util.HttpServletRequestUtil;

@Controller
@RequestMapping("/clubadmin")
public class ClubManagementController {
	@Autowired
	private ClubService clubService;
	@Autowired
	private ClubCategoryService clubCategoryService;
	@Autowired
	private AreaService areaService;

	@RequestMapping(value = "/getclubmanagementinfo", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> getClubManagementInfo(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		long clubId = HttpServletRequestUtil.getLong(request, "clubId");
		if (clubId <= 0) {
			Object currentClubObj = request.getSession().getAttribute("currentClub");
			if (currentClubObj == null) {
				modelMap.put("redirect", true);
				modelMap.put("url", "/CC/clubadmin/clublist");
			} else {
				Club currentClub = (Club) currentClubObj;
				modelMap.put("redirect", false);
				modelMap.put("clubId", currentClub.getClubId());
			}
		} else {
			Club currentClub = new Club();
			currentClub.setClubId(clubId);
			request.getSession().setAttribute("currentClub", currentClub);
			modelMap.put("redirect", false);
		}
		return modelMap;
	}

	@RequestMapping(value = "/getclublist", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> getClubList(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		PersonInfo user = new PersonInfo();
		user.setUserId(1L);
		user.setName("testName");
		request.getSession().setAttribute("user", user);
		user = (PersonInfo) request.getSession().getAttribute("user");
		try {
			Club clubCondition = new Club();
			clubCondition.setCaptain(user);
			ClubExecution ce = clubService.getClubList(clubCondition, 0, 100);
			modelMap.put("clubList", ce.getClubList());
			modelMap.put("user", user);
			modelMap.put("success", true);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
		}
		return modelMap;
	}

	@RequestMapping(value = "/getclubbyid", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> getClubById(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		Long clubId = HttpServletRequestUtil.getLong(request, "clubId");
		if (clubId > -1) {
			try {
				Club club = clubService.getByClubId(clubId);
				List<Area> areaList = areaService.getAreaList();
				modelMap.put("club", club);
				modelMap.put("areaList", areaList);
				modelMap.put("success", true);
			} catch (Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
			}
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "empty clubId");
		}
		return modelMap;
	}

	@RequestMapping(value = "/getclubinitinfo", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> getClubInitInfo() {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		List<ClubCategory> clubCategoryList = new ArrayList<ClubCategory>();
		List<Area> areaList = new ArrayList<Area>();
		try {
			clubCategoryList = clubCategoryService.getClubCategoryList(new ClubCategory());
			areaList = areaService.getAreaList();
			modelMap.put("clubCategoryList", clubCategoryList);
			modelMap.put("areaList", areaList);
			modelMap.put("success", true);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
		}
		return modelMap;
	}

	@RequestMapping(value = "/registerclub", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> registerClub(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		if (!CodeUtil.checkVerifyCode(request)) {
			modelMap.put("success", false);
			modelMap.put("errMsg", "验证码错误");
			return modelMap;
		}
		// 1.接收并转化相应的额参数，包括社团信息以及图片信息
		String clubStr = HttpServletRequestUtil.getString(request, "clubStr");
		ObjectMapper mapper = new ObjectMapper();
		Club club = null;
		try {
			club = mapper.readValue(clubStr, Club.class);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
			return modelMap;
		}
		CommonsMultipartFile clubImg = null;
		CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		if (commonsMultipartResolver.isMultipart(request)) {
			MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
			clubImg = (CommonsMultipartFile) multipartHttpServletRequest.getFile("clubImg");
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "上传图片不能为空");
			return modelMap;
		}
		// 2.注册社团
		if (club != null && clubImg != null) {
			PersonInfo captain = (PersonInfo) request.getSession().getAttribute("user");
			club.setCaptain(captain);
			ClubExecution clubExecution;
			try {
				ImageHolder imageHolder = new ImageHolder(clubImg.getOriginalFilename(), clubImg.getInputStream());
				clubExecution = clubService.addClub(club, imageHolder);
				if (clubExecution.getState() == ClubStateEnum.CHECK.getState()) {
					modelMap.put("success", true);
					// 该用户可操作的社团列表
					@SuppressWarnings("unchecked")
					List<Club> clubList = (List<Club>) request.getSession().getAttribute("clubList");
					if (clubList == null || clubList.size() == 0) {
						clubList = new ArrayList<Club>();
					}
					clubList.add(clubExecution.getClub());
					request.getSession().setAttribute("clubList", clubList);
				} else {
					modelMap.put("success", false);
					modelMap.put("errMsg", clubExecution.getStateInfo());
				}
			} catch (ClubOperationException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
				return modelMap;
			} catch (IOException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
				return modelMap;
			}
			return modelMap;
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "请输入社团信息");
			return modelMap;
		}
	}

	@RequestMapping(value = "/modifyclub", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> modifyClub(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		if (!CodeUtil.checkVerifyCode(request)) {
			modelMap.put("success", false);
			modelMap.put("errMsg", "验证码错误");
			return modelMap;
		}
		// 1.接收并转化相应的参数，包括社团信息以及图片信息
		String clubStr = HttpServletRequestUtil.getString(request, "clubStr");
		ObjectMapper mapper = new ObjectMapper();
		Club club = null;
		try {
			club = mapper.readValue(clubStr, Club.class);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
			return modelMap;
		}
		CommonsMultipartFile clubImg = null;
		CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		if (commonsMultipartResolver.isMultipart(request)) {
			MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
			clubImg = (CommonsMultipartFile) multipartHttpServletRequest.getFile("clubImg");
		}
		// 2.修改社团信息
		if (club != null && club.getClubId() != null) {
			ClubExecution clubExecution;
			try {
				if (clubImg == null) {
					clubExecution = clubService.modifyClub(club, null);
				} else {
					ImageHolder imageHolder = new ImageHolder(clubImg.getOriginalFilename(), clubImg.getInputStream());
					clubExecution = clubService.modifyClub(club, imageHolder);
				}
				if (clubExecution.getState() == ClubStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
				} else {
					modelMap.put("success", false);
					modelMap.put("errMsg", clubExecution.getStateInfo());
				}
			} catch (ClubOperationException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
			} catch (IOException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
			}
			return modelMap;
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "请输入社团Id");
			return modelMap;
		}
	}
}
