package com.shecaicc.cc.web.wechat;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.shecaicc.cc.dto.UserAccessToken;
import com.shecaicc.cc.dto.WechatUser;
import com.shecaicc.cc.util.wechat.WechatUtil;

/**
 * 获取关注公众号之后的微信用户信息的接口，在微信浏览器里访问
 * https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx71e1235459ffc1ee&redirect_uri=http://m.shecaicc.com/CC/wechatlogin/logincheck&role_type=1&response_type=code&scope=snsapi_userinfo&state=1#wechat_redirect
 * 则这里将会获取到code,之后再可以通过code获取到access_token 进而获取到用户信息
 *
 */
@Controller
@RequestMapping("wechatlogin")
public class WechatLoginController {
	private static Logger log = LoggerFactory.getLogger(WechatLoginController.class);

	public String doGet(HttpServletRequest request, HttpServletResponse response) {
		log.debug("weixin login get...");
		// 获取微信公总号传输过来的code，通过code获取access_token，获取用户信息
		String code = request.getParameter("state");
		// state传自定义信息以程序调用
		String roleType = request.getParameter("state");
		log.debug("weixin login code: " + code);
		WechatUser user = null;
		String openId = null;
		if (null != code) {
			UserAccessToken token;
			try {
				// 通过code获取access_token
				token = WechatUtil.getUserAccessToken(code);
				log.debug("weixin login token: " + user.toString());
				request.getSession().setAttribute("openId", openId);
			} catch (IOException e) {
				log.error("error in getUserAccessToken or getUserInfo or findByOpenId: " + e.toString());
				e.printStackTrace();
			}
		}
		// TODO 注册账号
		if (user != null)
			// 获取到微信验证的信息后返回到指定的路由
			return "frontend/index";
		else
			return "club/clublist";
	}
}
