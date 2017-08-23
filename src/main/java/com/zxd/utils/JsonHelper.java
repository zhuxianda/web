package com.zxd.utils;

import com.google.gson.Gson;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.nio.charset.Charset;

public class JsonHelper {

	private static ThreadLocal<Boolean> filter = new ThreadLocal<Boolean>() {
		@Override
		protected Boolean initialValue() {
			return true;
		}
	};

	public static ResponseEntity<String> jsonpEntity(Object obj, String callback) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
		String output;
		Gson gson = new Gson();
		if ("utf8".equals(filter.get())) {
			output = gson.toJson(obj);
		} else {
			output = encode(gson.toJson(obj));
		}
		if (callback != null) {
			callback = callback.replaceAll("\\?|>|<|/", "");
			output = callback + "(" + output + ")";
		}
		return new ResponseEntity<String>(output, headers, HttpStatus.OK);
	}
	
	public static ResponseEntity<String> jsonpEntity(Object obj, String front, String tail) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(new MediaType("text", "html", Charset.forName("UTF-8")));
		String output;
		Gson gson = new Gson();
		if ("utf8".equals(filter.get())) {
			output = gson.toJson(obj);
		} else {
			output = encode(gson.toJson(obj));
		}
		if (front != null)
			output = front + "(" + output + ")";
		if (tail != null)
			output = output + tail;

		return new ResponseEntity<String>(output, headers, HttpStatus.OK);
	}

	public static String encode(String s) {
		StringBuilder sb = new StringBuilder(s.length() * 3);
		for (char c : s.toCharArray()) {
			if (c < 256) {
				sb.append(c);
			} else {
				sb.append("\\u");
				sb.append(Character.forDigit((c >>> 12) & 0xf, 16));
				sb.append(Character.forDigit((c >>> 8) & 0xf, 16));
				sb.append(Character.forDigit((c >>> 4) & 0xf, 16));
				sb.append(Character.forDigit((c) & 0xf, 16));
			}
		}
		return sb.toString();
	}
}
