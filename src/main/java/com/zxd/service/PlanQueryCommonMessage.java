package com.zxd.service;

import org.springframework.stereotype.Component;

@Component
public class PlanQueryCommonMessage {

	private static ThreadLocal<Long> uid = new ThreadLocal<Long>() {
		@Override
		protected Long initialValue() {
			return 0l;
		}
	};

	private static ThreadLocal<String> username = new ThreadLocal<String>() {
		@Override
		protected String initialValue() {
			return "";
		}
	};

	public static void setUid(Long uid) {
		PlanQueryCommonMessage.uid.set(uid);
	}

	public static Long getUid() {
		return PlanQueryCommonMessage.uid.get();
	}

	public static void setUsername(String username) {
		PlanQueryCommonMessage.username.set(username);
	}

	public static String getUsername() {
		return PlanQueryCommonMessage.username.get();
	}


	public static void clear() {
		PlanQueryCommonMessage.uid.remove();
		PlanQueryCommonMessage.username.remove();
	}
}
