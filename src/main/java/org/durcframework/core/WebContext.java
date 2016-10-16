package org.durcframework.core;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 使用这个类时,必须要建一个Filter每次请求都要设置下HttpServletRequest<br>
 * <pre>
public void doFilter(ServletRequest arg0, ServletResponse arg1,
			FilterChain arg2) throws IOException, ServletException {
	HttpServletRequest request = (HttpServletRequest) arg0;
	WebContext.getInstance().setRequest(request);
	arg2.doFilter(arg0, arg1);
}
	</pre>
 *
 */
public enum WebContext {
	INSTANCE;
	
	private ThreadLocal<HttpServletRequest> request = new ThreadLocal<HttpServletRequest>();
	private ThreadLocal<HttpServletResponse> response = new ThreadLocal<HttpServletResponse>();
	
	public static WebContext getInstance() {
		return INSTANCE;
	}
	
	public void setRequest(HttpServletRequest req) {
		request.set(req);
	}
	
	public HttpServletRequest getRequest() {
		return request.get();
	}
	
	public HttpServletResponse getResponse() {
		return response.get();
	}

	public void setResponse(HttpServletResponse resp) {
		response.set(resp);
	}

	public void resetRquest() {
		request.remove();
	}
	
	public void setAttr(String key,Object value){
		this.getSession().setAttribute(key, value);
	}
	
	public Object getAttr(String key){
		return this.getSession().getAttribute(key);
	}
	
	public HttpSession getSession() {
		if(getRequest() != null) {
			return getRequest().getSession();
		}
		return null;
	}
	
}
