package com.zxd.exception;


import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.zxd.utils.JsonHelper;

public class ExceptionHandler implements HandlerExceptionResolver {
	
	//文件2 再来一个测试 再来 很好 很好 现在使用的是git 方式上传 666

	//private static Logger logger = LoggerFactory.getLogger(ExceptionHandler.class);
	private static final Gson gson = new Gson();

	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {

		String callback = request.getParameter("callback");
		String front = null;
		
		ex.printStackTrace();		
		
		if(ex instanceof LoginException){
			ex.printStackTrace();
			response.setContentType("application/json; charset=UTF-8");
			response.setCharacterEncoding("utf-8");
			try {
				PrintWriter out = response.getWriter();
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("code", 799);
				map.put("msg", "请登陆后再进行操作");

				String output = gson.toJson(map);
				if(front != null) {
					response.setContentType("text/html; charset=UTF-8");
					output = front + "(" + output + ");</script>";
				}else{
					if (callback != null)
						output = callback + "(" + output + ")";
				}

				out.println(JsonHelper.encode(output));
				out.flush();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		} else if (ex instanceof InvalidDataException) {
			response.setContentType("application/json; charset=UTF-8");
			response.setCharacterEncoding("utf-8");
			try {
				PrintWriter out = response.getWriter();
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("code", 99);
				map.put("msg", ex.getMessage());

				String output = gson.toJson(map);
				if(front != null) {
					response.setContentType("text/html; charset=UTF-8");
					output = front + "(" + output + ");</script>";
				}else{
					if (callback != null)
						output = callback + "(" + output + ")";
				}

				out.println(JsonHelper.encode(output));
				out.flush();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		} else {
			try {
				PrintWriter out = response.getWriter();
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("code", 99);
				map.put("msg", "工程师正在修复问题！");

				String output = gson.toJson(map);
				if(front != null) {
					response.setContentType("text/html; charset=UTF-8");
					output = front + "(" + output + ");</script>";
				}else{
					if (callback != null)
						output = callback + "(" + output + ")";
				}

				out.println(JsonHelper.encode(output));
				out.flush();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
		
		
		

		
		
/*		String callback = request.getParameter("callback");
		String front = null;
		if(request.getRequestURL().toString().contains("upload.do")){
			front = "<script>document.domain = \"4399.cn\";window.parent.rechargeCallbackJsonp";
		}

		if (ex instanceof InvalidDataException) {
			Report.WARN(GuardBase.ADGAME_PROMOTION, "InvalidDataException", ex.getMessage());
			ex.printStackTrace();
			response.setContentType("application/json; charset=UTF-8");
			response.setCharacterEncoding("utf-8");
			try {
				PrintWriter out = response.getWriter();
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("code", 99);
				map.put("msg", ex.getMessage());

				String output = gson.toJson(map);
				if(front != null) {
					response.setContentType("text/html; charset=UTF-8");
					output = front + "(" + output + ");</script>";
				}else{
					if (callback != null)
						output = callback + "(" + output + ")";
				}

				out.println(JsonHelper.encode(output));
				out.flush();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		} else if (ex instanceof UnDefinedException) {
			if(StringUtils.hasText(ex.getMessage())){
				Report.WARN(GuardBase.ADGAME_PROMOTION, ex.getMessage(), ex.getMessage());
			}else {
				Report.WARN(GuardBase.ADGAME_PROMOTION, "UnDefinedException", ex.getMessage());
			}
			ex.printStackTrace();
			response.setContentType("application/json; charset=UTF-8");
			response.setCharacterEncoding("utf-8");
			try {
				PrintWriter out = response.getWriter();
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("code", 97);
				map.put("msg", ex.getMessage());

				String output = gson.toJson(map);
				if(front != null) {
					response.setContentType("text/html; charset=UTF-8");
					output = front + "(" + output + ");</script>";
				}else{
					if (callback != null)
						output = callback + "(" + output + ")";
				}

				out.println(JsonHelper.encode(output));
				out.flush();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		} else if(ex instanceof LoginException){
			Report.WARN(GuardBase.ADGAME_PROMOTION, "LoginException", ex.getMessage());
			ex.printStackTrace();
			response.setContentType("application/json; charset=UTF-8");
			response.setCharacterEncoding("utf-8");
			try {
				PrintWriter out = response.getWriter();
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("code", 799);
				map.put("msg", "请登陆后再进行操作");

				String output = gson.toJson(map);
				if(front != null) {
					response.setContentType("text/html; charset=UTF-8");
					output = front + "(" + output + ");</script>";
				}else{
					if (callback != null)
						output = callback + "(" + output + ")";
				}

				out.println(JsonHelper.encode(output));
				out.flush();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		} else if(ex instanceof UserNotExistException){
			Report.WARN(GuardBase.ADGAME_PROMOTION, "UserNotExistException", ex.getMessage());
			ex.printStackTrace();
			response.setContentType("application/json; charset=UTF-8");
			response.setCharacterEncoding("utf-8");
			try {
				PrintWriter out = response.getWriter();
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("code", 800);
				map.put("msg", "请确认协议后再进行操作");

				String output = gson.toJson(map);
				if(front != null) {
					response.setContentType("text/html; charset=UTF-8");
					output = front + "(" + output + ");</script>";
				}else{
					if (callback != null)
						output = callback + "(" + output + ")";
				}

				out.println(JsonHelper.encode(output));
				out.flush();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}else {
			if (StringUtils.hasText(ex.getMessage())) {
				Report.ERROR(GuardBase.ADGAME_PROMOTION, "UnCatchException", ex.getMessage());
			} else {
				Report.ERROR(GuardBase.ADGAME_PROMOTION, "UnCatchException", "UnCatchException");
			}
			ex.printStackTrace();
			response.setContentType("application/json; charset=UTF-8");
			response.setCharacterEncoding("utf-8");
			try {
				PrintWriter out = response.getWriter();
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("code", 98);
				map.put("msg", "工程师正在焦头烂额的解决bug");

				String output = gson.toJson(map);
				if(front != null) {
					response.setContentType("text/html; charset=UTF-8");
					output = front + "(" + output + ");</script>";
				}else{
					if (callback != null)
						output = callback + "(" + output + ")";
				}
				
				out.println(JsonHelper.encode(output));
				out.flush();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}*/
			//return null;
	}
}
