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

	@RequestMapping(value="/getclubinitinfo", method=RequestMethod.GET)
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
		if (!CodeUtil.chekcVerifyCode(request)) {
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
			PersonInfo captain = new PersonInfo();
			// Session TODO
			captain.setUserId(1L);
			club.setCaptain(captain);
			ClubExecution clubExecution;
			try {
				clubExecution = clubService.addClub(club, clubImg.getInputStream(), clubImg.getOriginalFilename());
				if (clubExecution.getState() == ClubStateEnum.CHECK.getState()) {
					modelMap.put("success", true);
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
		// 3.返回结果
	}

	/*	private static void inputStreamToFile(InputStream ins, File file) {
		FileOutputStream os = null;
		try {
			os = new FileOutputStream(file);
			int bytesRead = 0;
			byte[] buffer = new byte[1024];
			while ((bytesRead = ins.read(buffer)) != -1) {
				os.write(buffer, 0, bytesRead);
			}
		} catch (Exception e) {
			throw new RuntimeException("调用inputStreamToFile产生异常: " + e.getMessage());
		} finally {
			try {
				if (os != null) {
					os.close();
				}
				if (ins != null) {
					ins.close();
				}
			} catch (IOException e) {
				throw new RuntimeException("inputStreamToFile关闭io产生异常: " + e.getMessage());
			}
		}
	}*/
}
