package com.zxd.service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zxd.exception.InvalidDataException;

public class CommonService {

	public static final char[] HEX_DIGITS_TAB = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
			'e', 'f' };

	public static String sendPost(String url, String param) {
		String result = "";
		BufferedReader reader = null;
		try {

			URL postUrl = new URL(url);
			// 打开连接
			HttpURLConnection connection = (HttpURLConnection) postUrl.openConnection();

			// 设置是否向connection输出，因为这个是post请求，参数要放在 http正文内，因此需要设为true
			connection.setDoOutput(true);
			connection.setDoInput(true);

			// 默认是 GET方式
			connection.setRequestMethod("POST");

			// Post 请求不能使用缓存
			connection.setUseCaches(false);
			connection.setInstanceFollowRedirects(true);
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			// 连接，从postUrl.openConnection()至此的配置必须要在connect之前完成，
			connection.connect();
			DataOutputStream out = new DataOutputStream(connection.getOutputStream());
			// DataOutputStream.writeBytes将字符串中的16位的unicode字符以8位的字符形式写到流里面
			if (param != null)
				out.writeBytes(param);
			out.flush();
			out.close();
			reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line;
			while ((line = reader.readLine()) != null) {
				result += line;
			}

			reader.close();
			connection.disconnect();
		} catch (Exception e) {
			System.out.println("发送POST请求出现异常！" + e);
			e.printStackTrace();
		}
		// 使用finally块来关闭输入流
		finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}

	public static String sendGetWithCookie(String url, String param, Cookie cookie) {
		String result = "";
		BufferedReader in = null;
		try {
			String urlName = url + "?" + param;
			// System.out.println(urlName);
			URL realUrl = new URL(urlName);
			// 打开和URL之间的连接
			HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
			// URLConnection conn = realUrl.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
			conn.setRequestProperty("Cookie", cookie.getName() + "=" + cookie.getValue());
			conn.setConnectTimeout(1200);
			// 建立实际的连接
			conn.connect();
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			System.out.println("发送GET请求出现异常！" + e);
			e.printStackTrace();
			throw new InvalidDataException(e.getMessage());
		}
		// 使用finally块来关闭输入流
		finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}

	public static String sendGet(String url, String param) {
		String result = "";
		BufferedReader in = null;
		try {
			String urlName = url + "?" + param;
			// System.out.println(urlName);
			URL realUrl = new URL(urlName);
			// 打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
			conn.setConnectTimeout(1200);// TODO 后续设置回1200 
			// 建立实际的连接
			conn.connect();
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			System.out.println("发送GET请求出现异常！" + e);
			e.printStackTrace();
			throw new InvalidDataException(e.getMessage());
		}
		// 使用finally块来关闭输入流
		finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}

	public static boolean isHostReachable(String ip) {
		Runtime runtime = Runtime.getRuntime();
		Process process = null;
		String line = null;
		InputStream is = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		boolean res = false;
		try {
			process = runtime.exec("ping " + ip + " -w 1");
			is = process.getInputStream();
			isr = new InputStreamReader(is);
			br = new BufferedReader(isr);
			while ((line = br.readLine()) != null) {
				if (line.contains("ttl") || line.contains("TTL")) {
					res = true;
					break;
				}
			}
			is.close();
			isr.close();
			br.close();
		} catch (Exception e) {
			System.out.println(e);
			runtime.exit(1);
		}
		return res;
	}

	public static String genSignature(Map<String, ?> params, String secret) {
		List<String> keys = new ArrayList<String>();
		for (String key : params.keySet()) {
			keys.add(key);
		}
		Collections.sort(keys);

		StringBuffer stringBuffer = new StringBuffer();
		for (String key : keys) {
			if (!"sig".equals(key)) {
				stringBuffer.append(key).append("=").append((String) params.get(key));
			}
		}

		stringBuffer.append(secret);

		return genMd5(stringBuffer.toString());
	}

	public static String genMd5(String data) {
		MessageDigest messageDigest;

		try {
			messageDigest = MessageDigest.getInstance("MD5");
			messageDigest.update(data.getBytes("UTF-8"));
		} catch (NoSuchAlgorithmException e) {
			return "";
		} catch (UnsupportedEncodingException e) {
			return "";
		}

		byte[] tmp = messageDigest.digest();
		char[] charBuffer = new char[16 * 2];

		for (int i = 0, k = 0; i < 16; i++) {
			byte byte0 = tmp[i];
			charBuffer[k++] = HEX_DIGITS_TAB[byte0 >>> 4 & 0xf];
			charBuffer[k++] = HEX_DIGITS_TAB[byte0 & 0xf];
		}
		return new String(charBuffer);
	}

//	public static void deleteAll(HttpServletRequest request, HttpServletResponse response) {
//		response.addCookie(CommonService.delete4399Cookie(request, Constant.COOKIE_PAUTH));
//		response.addCookie(CommonService.delete4399Cookie(request, Constant.COOKIE_XAUTH));
//		response.addCookie(CommonService.delete4399Cookie(request, Constant.COOKIE_UAUTH));
//		response.addCookie(CommonService.delete4399Cookie(request, Constant.COOKIE_PHPSESSID));
//	}
//
//	public static Cookie delete4399Cookie(HttpServletRequest request, String name) {
//		Cookie cookie = new Cookie(name, null);
//		String[] split = request.getServerName().split("\\.");
//		cookie.setDomain(split[split.length - 2] + "." + split[split.length - 1]);
//		cookie.setPath("/");
//		cookie.setMaxAge(0);
//		return cookie;
//	}

	public static String formatBankAccount(String account, String insertChar, Integer interval) {
		StringBuilder sb = new StringBuilder(account);
		int length = account.length() / interval + account.length();
		for (int i = 0; i < length; i++) {
			if (i % 5 == 0) {
				sb.insert(i, insertChar);
			}
		}
		sb.deleteCharAt(0);
		return sb.toString();
	}

	public static String formatMoney(String money) {
		DecimalFormat df = new DecimalFormat("#,###");
		if (money.contains(".")) {
			Double num = Double.parseDouble(money);
			return df.format(num.longValue());
		} else
			return df.format(Long.parseLong(money));
	}

	public static String formatTraceDisplay(Double display) { // 将展示量处理成百分比字符串的分段函数
		Double percent = 0d;
		if (display >= 200000)
			percent = 0.98d;
		else if (display >= 100000 && display < 200000) {
			percent = (display - 100000) * 0.08d / 100000 + 0.90;
		} else if (display >= 50000 && display < 100000) {
			percent = (display - 50000) * 0.10 / 50000 + 0.80;
		} else if (display >= 10000 && display < 50000) {
			percent = (display - 10000) * 0.20 / 40000 + 0.60;
		} else if (display >= 1000 && display < 10000) {
			percent = (display - 1000) * 0.20 / 9000 + 0.40;
		} else if (display >= 100 && display < 1000) {
			percent = (display - 100) * 0.15 / 900 + 0.25;
		} else if (display >= 10 && display < 100) {
			percent = (display - 10) * 0.15 / 90 + 0.10;
		} else if (display >= 1 && display < 10) {
			percent = (display - 1) * 0.05 / 9 + 0.05;
		} else {
			percent = 0.03d;
		}
		return (Math.floor(percent * 100) + "%");
	}

	public static String formatSearchDisplay(Double display) { // 将展示量处理成百分比字符串的分段函数
		Double percent = 0d;
		if (display >= 50000)
			percent = 0.95d;
		else if (display >= 25000 && display < 50000) {
			percent = (display - 25000) * 0.05d / 25000 + 0.90;
		} else if (display >= 5000 && display < 25000) {
			percent = (display - 5000) * 0.15 / 20000 + 0.75;
		} else if (display >= 1000 && display < 5000) {
			percent = (display - 1000) * 0.20 / 4000 + 0.55;
		} else if (display >= 100 && display < 1000) {
			percent = (display - 100) * 0.15 / 900 + 0.40;
		} else if (display >= 10 && display < 100) {
			percent = (display - 10) * 0.20 / 900 + 0.20;
		} else if (display >= 1 && display < 10) {
			percent = (display - 1) * 0.15 / 9 + 0.05;
		} else {
			percent = 0.03d;
		}
		return (Math.floor(percent * 100) + "%");
	}

	public static String formatQuality(/* Double display, */Double sumCTR, Integer size) {
		Double percent = 0.0d;
		if (sumCTR == null || size == 0)
			percent = 0.35d;
		else {
			Double ctr = sumCTR / size;
			if (ctr == null || size == 0)
				percent = 0.15d;
			if (ctr < 0.00001)
				percent = 0.20d;
			else if (ctr >= 0.0001 && ctr < 0.001) {
				percent = (ctr - 0.0001) * 0.30 / 0.0009 + 0.10;
				if (percent < 0.125)
					percent *= 2;
			} else if (ctr >= 0.001 && ctr < 0.005) {
				percent = (ctr - 0.001) * 0.20 / 0.004 + 0.40;
			} else if (ctr >= 0.005 && ctr < 0.01) {
				percent = (ctr - 0.005) * 0.15 / 0.005 + 0.60;
			} else if (ctr >= 0.01 && ctr < 0.05) {
				percent = (ctr - 0.01) * 0.10 / 0.04 + 0.75;
			} else if (ctr >= 0.05 && ctr < 0.075) {
				percent = (ctr - 0.05) * 0.05 / 0.025 + 0.85;
			} else if (ctr >= 0.075 && ctr < 0.1) {
				percent = (ctr - 0.075) * 0.05 / 0.025 + 0.90;
			} else if (ctr >= 0.1 && ctr < 0.5) {
				percent = (ctr - 0.1) * 0.03 / 0.4 + 0.95;
			} else {
				percent = 0.98d;
			}
		}
		return (Math.floor(percent * 100) + "%");
	}

	public static void main(String[] args) throws ParseException, InterruptedException {
		// ApplicationContext ac = new
		// ClassPathXmlApplicationContext("spring/adgame_planquery.xml");
		String sql = formatBankAccount("111111111111111", "a", 4);
		System.out.println(sql);
		System.out.println(formatMoney("2345374856"));
	}

}
