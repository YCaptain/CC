package com.shecaicc.cc.util;

import javax.servlet.http.HttpServletRequest;

public class CodeUtil {
	public static boolean chekcVerifyCode(HttpServletRequest request) {
		String verifyExcepted = (String) request.getSession().getAttribute(com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);
		String verifyCodeActual = HttpServletRequestUtil.getString(request, "verifyCodeActual");
		if (verifyCodeActual == null || !verifyCodeActual.equals(verifyExcepted)) {
			return false;
		}
		return true;
	}
}
