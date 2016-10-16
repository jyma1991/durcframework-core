package org.durcframework.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UserFilter implements Filter {

	protected static List<String> NEED_NOT_LOGGIN = new ArrayList<String>();

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain arg2)
			throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) arg0;
		HttpServletResponse response = (HttpServletResponse) arg1;
		WebContext.getInstance().setRequest(request);

		if (isLogin(request)) {
			arg2.doFilter(arg0, arg1);
		} else {
			redirect(request, response);
		}
	}

	/**
	 * 跳转
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	protected void redirect(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// 如果是ajax请求
		/*
		 * 前台页面可以这样处理 if (typeof(jQuery) != 'undefined') {
		 * $(document).ajaxError(function (event, request, settings) { if
		 * (request.getResponseHeader("X-timeout") && request.status == 401) {
		 * // 跳转到首页 top.location.href = ctx; }else{ alert("系统异常"); } }); }
		 * 这段代码放在页面底部即可
		 */
		String servletPath = request.getServletPath();
		if (isAjaxRequest(request)) {
			response.setHeader("X-timeout", "1");
			response.setStatus(401);
			response.getWriter().close();
			return;
		}
		response.sendRedirect(request.getContextPath() + "/" + getNeedLoginPage(servletPath));
	}

	/**
	 * 判断请求是否为Ajax请求. <br/>
	 * 
	 * @param request
	 *            请求对象
	 * @return boolean
	 */
	public boolean isAjaxRequest(HttpServletRequest request) {
		String header = request.getHeader("X-Requested-With");
		return "XMLHttpRequest".equals(header);
	}

	/**
	 * 用户登陆页面
	 * 
	 * @return
	 */
	protected String getNeedLoginPage(String servletPath) {
		if (servletPath != null && servletPath.indexOf("/chat/") > -1) {
			return "chat/login.html";
		}
		return "needLogin.html";
	}

	/**
	 * 用户是否登录,如果没有登录则被拦截,跳转到getNeedLoginPage()页面
	 * 
	 * @param request
	 * @return true,已登录
	 */
	protected boolean isLogin(HttpServletRequest request) {
		String uri = request.getRequestURI();

		uri = uri.substring(uri.lastIndexOf("/") + 1);

		boolean isNeedNotLoginUrl = NEED_NOT_LOGGIN.contains(uri);

		if (isNeedNotLoginUrl) {
			return true;
		}

		return UserContext.getInstance().getUser() != null;
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		String paramValue = filterConfig.getInitParameter("excludeUrl");
		String[] urlArr = paramValue.split(",");

		NEED_NOT_LOGGIN.add("/");

		for (String excludeUrl : urlArr) {
			if (excludeUrl != null) {
				NEED_NOT_LOGGIN.add(excludeUrl.trim());
			}
		}

		NEED_NOT_LOGGIN.add(getNeedLoginPage(null));
	}
}
