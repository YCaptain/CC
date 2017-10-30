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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shecaicc.cc.dto.EventExecution;
import com.shecaicc.cc.dto.ImageHolder;
import com.shecaicc.cc.entity.Club;
import com.shecaicc.cc.entity.Event;
import com.shecaicc.cc.entity.EventCategory;
import com.shecaicc.cc.enums.EventStateEnum;
import com.shecaicc.cc.exceptions.EventOperationException;
import com.shecaicc.cc.service.EventCategoryService;
import com.shecaicc.cc.service.EventService;
import com.shecaicc.cc.util.CodeUtil;
import com.shecaicc.cc.util.HttpServletRequestUtil;

@Controller
@RequestMapping("/clubadmin")
public class EventManagementController {
	@Autowired
	private EventService eventService;
	@Autowired
	private EventCategoryService eventCategoryService;

	// 支持上传活动详情图的最大数量
	private static final int IMAGEMAXCOUNT = 6;

	/**
	 * 通过社团id获取该社团下的活动列表
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/geteventlistbyclub", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> getEventListByClub(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
		int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
		Club currentClub = (Club) request.getSession().getAttribute("currentClub");
		if ((pageIndex > -1) && (pageSize > -1) && (currentClub != null) && (currentClub.getClubId() != null)) {
			long eventCategoryId = HttpServletRequestUtil.getLong(request, "eventCategoryId");
			String eventName = HttpServletRequestUtil.getString(request, "eventName");
			Event eventCondition = compactEventCondition(currentClub.getClubId(), eventCategoryId, eventName);
			// 传入查询条件以及分页信息进行查询，返回相应活动列表以及总数
			EventExecution ee = eventService.getEventList(eventCondition, pageIndex, pageSize);
			modelMap.put("eventList", ee.getEventList());
			modelMap.put("count", ee.getCount());
			modelMap.put("success", true);
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "empty pageSize or pageIndex or clubId");
		}
		return modelMap;
	}

	@RequestMapping(value = "/addevent", method = RequestMethod.POST)
	private Map<String, Object> addEvent(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		// 验证码校验
		if (!CodeUtil.checkVerifyCode(request)) {
			modelMap.put("success", false);
			modelMap.put("errMsg", "验证码错误");
			return modelMap;
		}
		// 接收前端参数的变量的初始化
		ObjectMapper mapper = new ObjectMapper();
		Event event = null;
		ImageHolder thumbnail = null;
		List<ImageHolder> eventImgList = new ArrayList<ImageHolder>();
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		try {
			// 若请求中存在文件流，则取出相关文件
			if (multipartResolver.isMultipart(request)) {
				thumbnail = handleImage(request, thumbnail, eventImgList);
			} else {
				modelMap.put("success", false);
				modelMap.put("errMsg", "上传图片不能为空");
				return modelMap;
			}
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.toString());
			return modelMap;
		}
		try {
			String eventStr = HttpServletRequestUtil.getString(request, "eventStr");
			// 获取前端表单string并转换成Event实体类
			event = mapper.readValue(eventStr, Event.class);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.toString());
			return modelMap;
		}
		// 若Event信息，缩略图以及详情图非空，则进行活动添加操作
		if (event != null && thumbnail != null && eventImgList.size() > 0) {
			try {
				Club currentClub = (Club) request.getSession().getAttribute("currentClub");
				event.setClub(currentClub);
				// 执行添加操作
				EventExecution ee = eventService.addEvent(event, thumbnail, eventImgList);
				if (ee.getState() == EventStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
				} else {
					modelMap.put("success", false);
					modelMap.put("errMsg", ee.getStateInfo());
				}
			} catch (EventOperationException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
				return modelMap;
			}
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "请输入活动信息");
		}
		return modelMap;
	}

	/**
	 * 通过活动id获取活动信息
	 *
	 * @param eventId
	 * @return
	 */
	@RequestMapping(value = "/geteventbyid", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> getEventById(@RequestParam Long eventId) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		// 非空判断
		if (eventId > -1) {
			// 获取活动信息
			Event event = eventService.getEventById(eventId);
			List<EventCategory> eventCategoryList = eventCategoryService
					.getEventCategoryList(event.getClub().getClubId());
			modelMap.put("event", event);
			modelMap.put("eventCategoryList", eventCategoryList);
			modelMap.put("success", true);
		} else {
			modelMap.put("success", true);
			modelMap.put("errMsg", "empty eventId");
		}
		return modelMap;
	}

	private ImageHolder handleImage(HttpServletRequest request, ImageHolder thumbnail, List<ImageHolder> eventImgList)
			throws IOException {
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		// 取出缩略图
		CommonsMultipartFile thumbnailFile = (CommonsMultipartFile) multipartRequest.getFile("thumbnail");
		if (thumbnailFile != null) {
			thumbnail = new ImageHolder(thumbnailFile.getOriginalFilename(), thumbnailFile.getInputStream());
		}
		// 取出详情图
		for (int i = 0; i < IMAGEMAXCOUNT; i++) {
			CommonsMultipartFile eventImgFile = (CommonsMultipartFile) multipartRequest.getFile("eventImg" + i);
			if (eventImgFile != null) {
				ImageHolder eventImg = new ImageHolder(eventImgFile.getOriginalFilename(),
						eventImgFile.getInputStream());
				eventImgList.add(eventImg);
			} else {
				break;
			}
		}
		return thumbnail;
	}

	/**
	 * 活动编辑
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/modifyevent", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> modifyEvent(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		// 判断操作为活动编辑还是活动发布下架
		// 后者跳过验证码判断
		boolean statusChange = HttpServletRequestUtil.getBoolean(request, "statusChange");
		if (!statusChange && !CodeUtil.checkVerifyCode(request)) {
			modelMap.put("success", false);
			modelMap.put("errMsg", "验证码错误");
			return modelMap;
		}
		// 接收前端参数
		ObjectMapper mapper = new ObjectMapper();
		Event event = null;
		ImageHolder thumbnail = null;
		List<ImageHolder> eventImgList = new ArrayList<ImageHolder>();
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		// 若请求中存在文件流，取出相关文件
		try {
			if (multipartResolver.isMultipart(request)) {
				thumbnail = handleImage(request, thumbnail, eventImgList);
			}
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.toString());
			return modelMap;
		}
		try {
			String eventStr = HttpServletRequestUtil.getString(request, "eventStr");
			event = mapper.readValue(eventStr, Event.class);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.toString());
			return modelMap;
		}
		if (event != null) {
			try {
				Club currentClub = (Club) request.getSession().getAttribute("currentClub");
				event.setClub(currentClub);
				EventExecution ee = eventService.modifyEvent(event, thumbnail, eventImgList);
				if (ee.getState() == EventStateEnum.SUCCESS.getState()) {
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
			modelMap.put("errMsg", "请输入活动信息");
		}
		return modelMap;
	}

	/**
	 * 封装活动查询条件到Event实例中
	 *
	 * @param clubId
	 * @param eventCategoryId
	 * @param eventName
	 * @return
	 */
	private Event compactEventCondition(long clubId, long eventCategoryId, String eventName) {
		Event eventCondition = new Event();
		Club club = new Club();
		club.setClubId(clubId);
		eventCondition.setClub(club);
		// 添加指定类别要求
		if (eventCategoryId != -1L) {
			EventCategory eventCategory = new EventCategory();
			eventCategory.setEventCategoryId(eventCategoryId);
			eventCondition.setEventCategory(eventCategory);
		}
		// 添加活动名模糊查询
		if (eventName != null) {
			eventCondition.setEventName(eventName);
		}
		return eventCondition;

	}
}
