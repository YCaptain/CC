package com.shecaicc.cc.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 用户授权token
 *
 * @author YCaptain
 *
 */
public class UserAccessToken {
	// 获取到的凭证
	@JsonProperty("access_token")
	private String accessToekn;
	// 凭证有效时间，单位：秒
	@JsonProperty("expires_in")
	private String expiresIn;
	// 表示更新令牌，用来获取下一次的访问令牌
	@JsonProperty("refresh_token")
	private String refresh_token;
	// 该用户在此公总号下的身份标识，对此微信号具有唯一性
	@JsonProperty("openid")
	private String openId;
	// 表示权限范围
	@JsonProperty("scope")
	private String scope;

	public String getAccessToekn() {
		return accessToekn;
	}

	public void setAccessToekn(String accessToekn) {
		this.accessToekn = accessToekn;
	}

	public String getExpiresIn() {
		return expiresIn;
	}

	public void setExpiresIn(String expiresIn) {
		this.expiresIn = expiresIn;
	}

	public String getRefresh_token() {
		return refresh_token;
	}

	public void setRefresh_token(String refresh_token) {
		this.refresh_token = refresh_token;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}
}
