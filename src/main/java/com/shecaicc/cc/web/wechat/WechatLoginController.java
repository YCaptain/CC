package com.shecaicc.cc.web.wechat;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.shecaicc.cc.dto.UserAccessToken;
import com.shecaicc.cc.dto.WechatAuthExecution;
import com.shecaicc.cc.dto.WechatUser;
import com.shecaicc.cc.entity.PersonInfo;
import com.shecaicc.cc.entity.WechatAuth;
import com.shecaicc.cc.enums.WechatAuthStateEnum;
import com.shecaicc.cc.service.PersonInfoService;
import com.shecaicc.cc.service.WechatAuthService;
import com.shecaicc.cc.util.wechat.WechatUtil;

/**
 * 获取关注公众号之后的微信用户信息的接口，在微信浏览器里访问
 * https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx71e1235459ffc1ee&redirect_uri=http://m.shecaicc.com/CC/wechatlogin/logincheck&role_type=1&response_type=code&scope=snsapi_userinfo&state=1#wechat_redirect
 * 获取到code, 之后再通过code获取到access_token 进而获取到用户信息
 *
 */
@Controller
@RequestMapping("wechatlogin")
public class WechatLoginController {
	private static Logger log = LoggerFactory.getLogger(WechatLoginController.class);
	private static final String FRONTEND = "1";
	private static final String CLUBEND = "2";
	@Autowired
	private PersonInfoService personInfoService;
	@Autowired
	private WechatAuthService wechatAuthService;

	@RequestMapping(value = "/logincheck", method = { RequestMethod.GET })
	public String doGet(HttpServletRequest request, HttpServletResponse response) {
		log.debug("weixin login get...");
		// 获取微信公众号传输过来的code，通过code获取access_token，获取用户信息
		String code = request.getParameter("code");
		// state传自定义信息以程序调用
		String roleType = request.getParameter("state");
		log.debug("weixin login code: " + code);
		WechatUser user = null;
		String openId = null;
		WechatAuth auth = null;
		if (null != code) {
			UserAccessToken token;
			try {
				// 通过code获取access_token
				token = WechatUtil.getUserAccessToken(code);
				log.debug("weixin login token: " + token.toString());
				// 通过token获取accessToken
				String accessToken = token.getAccessToekn();
				// 通过token获取openId
				openId = token.getOpenId();
				// 通过access_token和openId获取用户昵称等信息
				user = WechatUtil.getUserInfo(accessToken, openId);
				log.debug("weixin login user: " + user.toString());
				request.getSession().setAttribute("openId", openId);
				auth = wechatAuthService.getWechatAuthByOpenId(openId);
			} catch (IOException e) {
				log.error("error in getUserAccessToken or getUserInfo or findByOpenId: " + e.toString());
				e.printStackTrace();
			}
		}
		// 若微信帐号为空则需要注册微信帐号，同时注册用户信息
		if (auth == null) {
			PersonInfo personInfo = WechatUtil.getPersonInfoFromRequest(user);
			auth = new WechatAuth();
			auth.setOpenId(openId);
			if (FRONTEND.equals(roleType)) {
				personInfo.setUserType(1);
			} else {
				personInfo.setUserType(2);
			}
			auth.setPersonInfo(personInfo);
			WechatAuthExecution we = wechatAuthService.register(auth);
			if (we.getState() != WechatAuthStateEnum.SUCCESS.getState()) {
				return null;
			} else {
				personInfo = personInfoService.getPersonInfoById(auth.getPersonInfo().getUserId());
				request.getSession().setAttribute("user", personInfo);
			}
		}
		if (user != null)
			// 获取到微信验证的信息后返回到指定的路由
			return "frontend/index";
		else
			return "club/clublist";
	}
}
