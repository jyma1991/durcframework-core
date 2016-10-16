package org.durcframework.core.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.durcframework.core.DefaultMessageResult;
import org.durcframework.core.MessageResult;
import org.durcframework.core.WebContext;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 提供基础能力的Controller,如果一个Controller具备简单功能可以继承这个类
 * @author hc.tang
 * 2015-2-28
 */
public abstract class BaseController {
	
	private static DateFormat defaultDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	private static CustomDateEditor customDateEditor = new CustomDateEditor(defaultDateFormat, true);
	
	public HttpServletRequest getRequest() {
		return WebContext.getInstance().getRequest();
	}
	
	public HttpSession getSession() {
		return WebContext.getInstance().getSession();
	}
	
	public HttpServletResponse getResponse() {
		return WebContext.getInstance().getResponse();
	}

	@InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Date.class, customDateEditor);
    }
	
	@ExceptionHandler
	protected 
	@ResponseBody 
	MessageResult exceptionHandler(HttpServletRequest request,
			HttpServletResponse response,
			Exception e) {
		return error(e.getMessage());
    }
	
	/**
	 * 返回成功的视图
	 * @return 默认返回DefaultMessageResult对象,可以重写getMessageResult()方法返回自定义的MessageResult
	 */
	public MessageResult success() {
		MessageResult msgResult = getMessageResult();
		msgResult.setSuccess(true);
		return msgResult;
	}
	
	/**
	 * 返回成功
	 * @param message
	 * @return 默认返回DefaultMessageResult对象,可以重写getMessageResult()方法返回自定义的MessageResult
	 */
	public MessageResult success(String message) {
		MessageResult msgResult = getMessageResult();
		msgResult.setSuccess(true);
		msgResult.setMessage(message);
		return msgResult;
	}
	
	/**
	 * 返回错误的视图
	 * @param errorMsg 错误信息
	 * @return 默认返回DefaultMessageResult对象,可以重写getMessageResult()方法返回自定义的MessageResult
	 */
	public MessageResult error(String errorMsg) {
		MessageResult msgResult = getMessageResult();
		msgResult.setSuccess(false);
		msgResult.setMessage(errorMsg);
		return msgResult;
	}
	
	/**
	 * 返回错误信息
	 * @param errorMsg 错误信息
	 * @param errorMsgs 更多错误信息
	 * @return 默认返回DefaultMessageResult对象,可以重写getMessageResult()方法返回自定义的MessageResult
	 */
	public MessageResult error(String errorMsg,List<String> errorMsgs) {
		MessageResult msgResult = error(errorMsg);
		msgResult.setMessages(errorMsgs);
		return msgResult;
	}
	
	/**
	 * 返回默认的消息实现类,可覆盖此方法返回自定义的消息实现类
	 * @return
	 */
	protected MessageResult getMessageResult() {
		return new DefaultMessageResult();
	}
}
