package com.zxd.interceptor;

import com.google.gson.Gson;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

@Component
public class AuthInterceptor extends HandlerInterceptorAdapter {

	//private static Logger logger = LoggerFactory.getLogger(AuthInterceptor.class);
	private static Gson gson = new Gson();


	/*
	 * html页面 静态资源 接口 已登录 通过 通过 通过 未登录 重定向index 通过 799
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

		request.setAttribute("requestTime", System.currentTimeMillis());

		String url = request.getRequestURL().toString();
		// 设置缓存
		if (url.endsWith(".html")) {
			response.setHeader("Cache-Control", "no-cache");
			response.setHeader("Pragma", "no-cache");
			response.setDateHeader("Expires", 0);
		} else {
			response.setDateHeader("Last-Modified", new Date().getTime()); // Last-Modified:页面的最后生成时间
			response.setHeader("Cache-Control", "public"); // Cache-Control来控制页面的缓存与否,public:浏览器和缓存服务器都可以缓存页面信息；
			response.setHeader("Pragma", "Pragma");
			response.setHeader("Cache-Control", "max-age=36000");
		}

		// if (url.equals("http://e.4399.cn:8112/"))
		// url += "ads/index.html";
		//String contextPath = request.getContextPath().replaceAll("/adgame_promotion", "");
		// 将登录信息保存到cookie
		/*CookieHelper.getAllAuthed(request);

		if (PlanQueryCommonMessage.getUid() == 0) { // 未登录
			if (url.endsWith(".html")) {
				return redirecToIndexIfNeed(url, contextPath, response);
			}
			if (!judgeIsStaticResource(request)) {
				if (url.indexOf("getUserInfo.do") == -1 && url.indexOf("AccountByOpen.do") == -1) // 获取用户名，未登录提供给判断index的显示方式
					throw new LoginException("请登录后操作");// 接口拦截
			}
		} else { // 已登录
			if (url.endsWith(".html")) {
				String redirectUrl = "";
				if (!isDeveloper() && !url.contains("index")) {
					// 不是开发者，除了index页之外的所有页面都往open跳转
					redirectUrl = "http://open.4399.cn";
				}
				if (StringUtils.hasText(redirectUrl)) {
					response.sendRedirect(redirectUrl);
					return false;
				}
				if(url.indexOf("app") > -1) {
					response.sendRedirect(contextPath + "/plan.html");
					return false;
				}
				if(url.indexOf("ads/plan.html") > -1) {
					response.sendRedirect(contextPath + "/plan.html");
					return false;
				}
				if(url.indexOf("ads/index.html") > -1) {
					response.sendRedirect(contextPath + "/index.html");
					return false;
				}
				if(url.indexOf("ads/stream.html") > -1) {
					response.sendRedirect(contextPath + "/plan.html#/stream");
					return false;
				}
				if(url.indexOf("ads/recharge.html") > -1) {
					response.sendRedirect(contextPath + "/plan.html#/recharge");
					return false;
				}
				if(url.indexOf("ads/recharge_fill.html") > -1) {
					response.sendRedirect(contextPath + "/plan.html#/recharge/fill");
					return false;
				}
				if(url.indexOf("ads/protocol.html") > -1) {
					response.sendRedirect(contextPath + "/protocol.html");
					return false;
				}
			}
		}*/
		return true;
	}

	public boolean redirecToProtocolIfNeed(String url, String contextPath, HttpServletResponse response)
			throws IOException {
		if (!url.contains("protocol")) {
			response.sendRedirect(contextPath + "/ads/protocol.html");
			return false;
		}
		return true;
	}

	public boolean redirecToIndexIfNeed(String url, String contextPath, HttpServletResponse response)
			throws IOException {
		if (url.contains("incompatible")) {
			// 前端用于判断是否为 ie8浏览器用来跳转的一个页面，无需判断是否登录
			return true;
		}
		if (!url.contains("index")) {
			response.sendRedirect(contextPath + "/index.html");
			return false;
		}
		return true;
	}

//	public boolean isDeveloper() {
//		Long uid = PlanQueryCommonMessage.getUid();
//		if (this.accountService.isOperator(uid)) {
//			return true;
//		}
//		if (this.accountService.isDeveloper(uid)) {
//			return true;
//		}
//
//		Map<String, Object> user = OpenMailService.getUserConnect(uid);
//		if (user.size() > 0) {
//			try {
//				this.accountService.markDeveloper(uid);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//			return true;
//		}
//		return false;
//	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		/*if (!this.judgeIsStaticResource(request)) {
			//this.writeLog(request);
		}*/
		//PlanQueryCommonMessage.clear();
	}

	private boolean judgeIsStaticResource(HttpServletRequest request) {
		if (request.getRequestURL().indexOf(request.getContextPath() + "/static") > -1
				|| request.getRequestURL().indexOf(request.getContextPath() + "/ads/resource") > -1
				|| request.getRequestURL().indexOf(request.getContextPath() + "/resource") > -1
				|| request.getRequestURL().indexOf(request.getContextPath() + "/jetty-dir.css") > -1
				// 新增
				|| request.getRequestURL().indexOf(".css") > -1 || request.getRequestURL().indexOf(".js") > -1
				|| request.getRequestURL().indexOf("/dist") > -1 || request.getRequestURL().indexOf("/src") > -1) {
			return true;
		} else {
			return false;
		}
	}

	public String getRemoteHost(javax.servlet.http.HttpServletRequest request) {
		//equalsIgnoreCase,忽略大小的equals
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip.equals("0:0:0:0:0:0:0:1") ? "127.0.0.1" : ip;
	}

//	private void writeLog(HttpServletRequest request) {
//		try {
//			Map<String, Object> result = new HashMap<String, Object>();
//			Map<String, String[]> map = request.getParameterMap();
//			Set<Map.Entry<String, String[]>> set = map.entrySet();
//			Iterator<Map.Entry<String, String[]>> it = set.iterator();
//			String params = "?";
//			while (it.hasNext()) {
//				Map.Entry<String, String[]> entry = it.next();
//				params += entry.getKey() + "=";
//				for (String i : entry.getValue()) {
//					params += i;
//				}
//				params += "&";
//			}
//			if ("?".equals(params)) {
//				params = "";
//			} else {
//				params = params.substring(0, params.length() - 1);
//			}
//			long responseTime = System.currentTimeMillis();
//			long requestTime = Long.parseLong(request.getAttribute("requestTime").toString());
//			long costTime = responseTime - requestTime;
//			result.put("requestTime", requestTime);
//			result.put("uid", PlanQueryCommonMessage.getUid());
//			result.put("userName", PlanQueryCommonMessage.getUsername());
//			result.put("ip", getRemoteHost(request));
//			result.put("costTime", costTime);
//			result.put("requestURL", request.getRequestURL() + params);
//			result.put("brower", request.getHeader("User-Agent"));
//			logger.info(gson.toJson(result));
//			// System.out.println(gson.toJson(result));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

}
