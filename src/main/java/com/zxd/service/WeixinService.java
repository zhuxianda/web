package com.zxd.service;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;

import org.codehaus.jettison.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.google.gson.Gson;
import com.zxd.dao.MysqlDao;
import com.zxd.dao.RedisDao;
import com.zxd.utils.CryptHelper;

import redis.clients.jedis.ShardedJedis;

import sun.misc.BASE64Encoder;

@Component
public class WeixinService {

	@Autowired
	private MysqlDao mysqlDao;
	
	@Autowired
	private RedisDao redisDao;
	
	@Autowired
	private AccountDao accountDao;
	
	// AppID
	@Value("${wx_appid}")
	public String AppID = "";
	
	// 密钥
	@Value("${wx_appsecret}")
	public String AppSecret = "";
	
	// 获取微信用户 OpenId 接口
	public static String getOpenidUrl = "https://api.weixin.qq.com/sns/jscode2session";
	
	private final static String SYNC_KEY_ONE = "o91y0oj!P";
	
	private final static String SYNC_KEY_TWO = "K901F9zjZF";
	
//
//	public static String oauthGetUidURL = "https://ptlogin.4399.com/oauth2/token.do";
//	public static String grant_type = "password";
	
	public Map<String, Object> getOpenIdMapByCode(String js_code) {
//appid=wx249251cf167b1573&secret=e1a74b1d4bbb6acab6ef5c188713aa1b&js_code=081xtnhP1zBKo31a69kP1ghlhP1xtnh8&grant_type=authorization_code		
		String grant_type = "authorization_code";
		//String js_code = "";
		String param = "";
		try {
			
			param = "appid=" + AppID + "&secret=" + AppSecret + "&js_code=" + js_code + "&grant_type=" + grant_type ;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println(getOpenidUrl + "?" + param);
		
		Map<String, Object> result = new HashMap<String, Object>();
		//System.out.println(result);
		
		Gson gson = new Gson();
		
		try {
			result = (Map) gson.fromJson(CommonService.sendPost(getOpenidUrl, param), Map.class);
		} catch (Exception e) {
			e.printStackTrace();
			return new HashMap<String, Object>();
		}
		
		return result;
	}
	
	public Long getWXUserUidBYOpenid(String openId) {
		return accountDao.getWXUid(openId);
	}
	
	
	public Integer bindWXInfo(String openId) {
		return accountDao.bindWXInfo(openId);
	}
	
	public static void main(String[] args) throws JSONException {
		ApplicationContext ac = new ClassPathXmlApplicationContext("spring/adgame_planquery.xml");
		WeixinService weixinService = ac.getBean(WeixinService.class);
		Map<String, Object> res = weixinService.getOpenIdMapByCode("061ydtaZ1jhYtZ0zGU9Z1t7CaZ1ydtaQ");
		String session_key = (String) res.get("session_key");
		String openid = (String) res.get("openid");
//		List<Account> list = accountService.getAllAccount();
//		int count = 0;
//		for (Account account : list) {
//			Long uid = account.getUid();
//			Map<String, Object> connectMethod = OauthService.getTestUserInfo(uid);
//			if (connectMethod.isEmpty())
//				System.out.println(account);
//			else
//				count++; // 有账户信息的计数
//		}
		//查询是否绑定了 uid 
		
		// 在redis 中生成 3rd
		System.out.println(session_key + "|||||" + openid);
	}

	public String set3rdInfo(Long uid) {
		return set3rdInfoRedis(uid);
	}
	
	// 获取 缓存的信息
	public Map<String, Object> getWX3RD(String openid) {
		ShardedJedis jds = null;
		String MD5key = getWXMD5Key(openid);
		String value = "";
		try {
			jds = redisDao.getPool().getResource();
			value = jds.get(MD5key);
		} catch (Exception e) {
			e.printStackTrace();
			if (null != jds) {
				redisDao.getPool().returnBrokenResource(jds);
			}
		} finally {
			if (null != jds) {
				redisDao.getPool().returnResource(jds);
				jds = null;
			}
		}
		
		boolean wxUserInfoExit = StringUtils.hasText(value);
		if (wxUserInfoExit) {
			Map<String, Object> res = new HashMap<String, Object>();
			if(value.equals("pass")) {
				res.put("type", "openid");
			} else {
				res.put("type", "uid");
				res.put("uid", value);
			}
			return res;
		} else {
			return null;
		}
		
	}

	public String getWXMD5Key(String uid) {
		long start = System.currentTimeMillis();
		String key = SYNC_KEY_ONE + "wx3rd" + uid + start;
		String MD5key = CryptHelper.md5(key);
		return MD5key;
	}
	
	public String getWXCodeMD5Key(String uid) {
		long start = System.currentTimeMillis();
		String key = SYNC_KEY_TWO + start + uid ;
		String MD5key = CryptHelper.md5(key);
		return MD5key;
	}
	
	// 写入 3rd
	public String set3rdInfoRedis(Long uid) {
		
		ShardedJedis jds = null;
		String md5Key = getWXMD5Key(uid.toString());
		
		try {
			jds = redisDao.getPool().getResource();
			jds.set(md5Key, uid+"");
			jds.expire(md5Key, 3600 * 24);
			//value = jds.get(md5Key);
		} catch (Exception e) {
			e.printStackTrace();
			if (null != jds) {
				redisDao.getPool().returnBrokenResource(jds);
			}
			return null;
		} finally {
			if (null != jds) {
				redisDao.getPool().returnResource(jds);
				jds = null;
			}
		}
		return md5Key;
	}
	
	// 读取 3rd
	public String get3rdInfoRedis(String info3rd) {
		ShardedJedis jds = null;
		String value = null;
		try {
			jds = redisDao.getPool().getResource();
			value = jds.get(info3rd);
			//value = jds.get(md5Key);
		} catch (Exception e) {
			e.printStackTrace();
			if (null != jds) {
				redisDao.getPool().returnBrokenResource(jds);
			}
			return null;
		} finally {
			if (null != jds) {
				redisDao.getPool().returnResource(jds);
				jds = null;
			}
		}
		return value;
	}
	
	// 获取 二维码的随机串
	public String getQRCode(Long uid) {
		
		String md5Key = getWXCodeMD5Key(uid.toString());
		setRedis(md5Key,uid.toString());
		return md5Key;
	}
	
	//通过code 获得uid值
	
	// 写入 set 方法 单个 Value
	public String setRedis(String key,String value) {
		
		ShardedJedis jds = null;
		try {
			jds = redisDao.getPool().getResource();
			jds.set(key, value);
			jds.expire(key, 60 * 10);//有效时间 60s
			//value = jds.get(md5Key);
		} catch (Exception e) {
			e.printStackTrace();
			if (null != jds) {
				redisDao.getPool().returnBrokenResource(jds);
			}
			return null;
		} finally {
			if (null != jds) {
				redisDao.getPool().returnResource(jds);
				jds = null;
			}
		}
		return key;
	}
	
	// 获取 单个value
	public String getRedis(String key) {
		
		ShardedJedis jds = null;
		String value = null;
		try {
			jds = redisDao.getPool().getResource();
			value = jds.get(key);
		} catch (Exception e) {
			e.printStackTrace();
			if (null != jds) {
				redisDao.getPool().returnBrokenResource(jds);
			}
			return null;
		} finally {
			if (null != jds) {
				redisDao.getPool().returnResource(jds);
				jds = null;
			}
		}
		return value;
	}

	public Map<String, String> getInfo3rdMap(String info3rd) {
		String uid = get3rdInfoRedis(info3rd);
		Map<String, String> res = new HashMap<String, String>();

		res.put("value", uid);

		return res;
	}

	public Long getUidBy3rd(String info3rd) {
		Map<String, String> info3rdMap = getInfo3rdMap(info3rd);
		if(info3rdMap!= null) {
			return  Long.parseLong(info3rdMap.get("value"));
		}
		return -1l;
	}
	
	public static String GetImageBase64(byte[] data) {
		// 对字节数组Base64编码
		BASE64Encoder encoder = new BASE64Encoder();
		return encoder.encode(data);// 返回Base64编码过的字节数组字符串
	}
	
	

//	public static void sendMessage(Long uid, String topic, String body, int type) {
//		String cur = String.valueOf(System.currentTimeMillis() / 1000);
//		String sign = "";
//		String param = "";
//		try {
//			sign = StringUtil.Md5(StringUtil.Md5(key + cur + String.valueOf(uid)));
//			param = "sign=" + sign + "&time=" + cur + "&userId=" + String.valueOf(uid) + "&topic="
//					+ URLEncoder.encode(topic, "utf-8") + "&body=" + URLEncoder.encode(body, "utf-8") + "&type="
//					+ String.valueOf(type);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		String result = CommonService.sendPost(messageUrl, param);
//		System.out.println(messageUrl + "?" + param);
//		System.out.println(result);
//	}


//	@SuppressWarnings({ "unchecked", "rawtypes" })
//	public static Map<String, Object> getUserConnect(Long uid) {
//		Gson gson = new Gson();
//		String cur = String.valueOf(System.currentTimeMillis() / 1000);
//		String sign = "";
//		try {
//			sign = StringUtil.Md5(StringUtil.Md5(key + cur + String.valueOf(uid)));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		String param = "sign=" + sign + "&time=" + cur + "&userId=" + String.valueOf(uid);
//		String url = userInfoUrl;
//		Map<String, Map<String, Object>> userInfo = new HashMap<String, Map<String, Object>>();
//		try {
//			userInfo = (Map) gson.fromJson(CommonService.sendGet(url, param), Map.class);
//		} catch (Exception e) {
//			e.printStackTrace();
//			return new HashMap<String, Object>();
//		}
//		if (userInfo == null || "userId_not_exists".equals(userInfo.get("message".toString())))
//			return new HashMap<String, Object>();
//		if ("false".equals(userInfo.get("data").get("status").toString())) {
//			return new HashMap<String, Object>();
//		}
//		Map<String, Object> result = userInfo.get("data");
//		return result == null ? new HashMap<String, Object>() : result;
//	}
//
//	@SuppressWarnings({ "unchecked", "rawtypes" })
//	public static Map<String, Object> getTestUserConnect(Long uid) {
//		Gson gson = new Gson();
//		String cur = String.valueOf(System.currentTimeMillis() / 1000);
//		String sign = "";
//		try {
//			sign = StringUtil.Md5(StringUtil.Md5(key + cur + String.valueOf(uid)));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		String param = "sign=" + sign + "&time=" + cur + "&userId=" + String.valueOf(uid);
//		String url = testUserInfoUrl;
//		Map<String, Map<String, Object>> userInfo = new HashMap<String, Map<String, Object>>();
//		try {
//			userInfo = (Map) gson.fromJson(CommonService.sendGet(url, param), Map.class);
//		} catch (Exception e) {
//			e.printStackTrace();
//			return new HashMap<String, Object>();
//		}
//		if (userInfo == null || "userId_not_exists".equals(userInfo.get("message".toString())))
//			return new HashMap<String, Object>();
//		Map<String, Object> result = userInfo.get("data");
//		return result == null ? new HashMap<String, Object>() : result;
//	}
//
//	@SuppressWarnings({ "unchecked", "rawtypes" })
//	public static Map<String, Object> getUserInfo(Long uid) {
//		Gson gson = new Gson();
//		String cur = String.valueOf(System.currentTimeMillis() / 1000);
//		String sign = "";
//		try {
//			sign = StringUtil.Md5(StringUtil.Md5(key + cur + String.valueOf(uid)));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		String param = "sign=" + sign + "&time=" + cur + "&userId=" + String.valueOf(uid);
//		String url = userConnectionUrl;
//		Map<String, Map<String, Object>> userInfo = new HashMap<String, Map<String, Object>>();
//		try {
//			userInfo = (Map) gson.fromJson(CommonService.sendGet(url, param), Map.class);
//		} catch (Exception e) {
//			e.printStackTrace();
//			return new HashMap<String, Object>();
//		}
//		if (userInfo == null || "userId_not_exists".equals(userInfo.get("message".toString())))
//			return new HashMap<String, Object>();
//		Map<String, Object> result = userInfo.get("data");
//		return result == null ? new HashMap<String, Object>() : result;
//	}
//
//	@SuppressWarnings({ "unchecked", "rawtypes" })
//	public static Map<String, Object> getTestUserInfo(Long uid) {
//		Gson gson = new Gson();
//		String cur = String.valueOf(System.currentTimeMillis() / 1000);
//		String sign = "";
//		try {
//			sign = StringUtil.Md5(StringUtil.Md5(key + cur + String.valueOf(uid)));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		String param = "sign=" + sign + "&time=" + cur + "&userId=" + String.valueOf(uid);
//		String url = testUserConnectionUrl;
//		Map<String, Map<String, Object>> userInfo = new HashMap<String, Map<String, Object>>();
//		try {
//			userInfo = (Map) gson.fromJson(CommonService.sendGet(url, param), Map.class);
//		} catch (Exception e) {
//			e.printStackTrace();
//			return new HashMap<String, Object>();
//		}
//		if (userInfo == null || "userId_not_exists".equals(userInfo.get("message".toString())))
//			return new HashMap<String, Object>();
//		Map<String, Object> result = userInfo.get("data");
//		return result == null ? new HashMap<String, Object>() : result;
//	}
//
//	public static void logout(Long uid, Cookie cookie) {
//		String cur = String.valueOf(System.currentTimeMillis() / 1000);
//		String sign = "";
//		try {
//			sign = StringUtil.Md5(StringUtil.Md5(key + cur + String.valueOf(uid)));
//			String param = "sign=" + sign + "&time=" + cur + "&userId=" + String.valueOf(uid);
//			CommonService.sendGetWithCookie(logoutUrl, param, cookie);
//			// CommonService.sendGetWithCookie(testLogoutUrl2, param, cookie);
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw new InvalidDataException("退出失败");
//		}
//	}
//
//	@SuppressWarnings({ "unchecked", "rawtypes" })
//	public static List<Map<String, Object>> getTestSDKGames(boolean initializeOrUpdate) {
//		Gson gson = new Gson();
//		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
//		Map<String, Object> sdkgames = initializeOrUpdate ? (Map) gson.fromJson(
//				CommonService.sendPost(updateTestSdkGamesUrl, null), Map.class) : (Map) gson.fromJson(
//				CommonService.sendPost(initializeTestSdkGamesUrl, null), Map.class);
//		Integer code = ((Double) sdkgames.get("code")).intValue();
//		if (sdkgames == null || code != 10000)
//			return result;
//		result = (List<Map<String, Object>>) sdkgames.get("list");
//		return result == null ? new ArrayList<Map<String, Object>>() : result;
//	}
//
//	@SuppressWarnings({ "unchecked", "rawtypes" })
//	public static List<Map<String, Object>> getSDKGames() {
//		Gson gson = new Gson();
//		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
//		Map<String, Object> sdkgames = (Map) gson.fromJson(CommonService.sendPost(initializeSdkGamesUrl, null),
//				Map.class);
//		Integer code = ((Double) sdkgames.get("code")).intValue();
//		if (sdkgames == null || code != 10000)
//			return result;
//		result = (List<Map<String, Object>>) sdkgames.get("list");
//		return result == null ? new ArrayList<Map<String, Object>>() : result;
//	}
//
//	// 通知 推送代理包
//	@SuppressWarnings({ "unchecked", "rawtypes" })
//	public static boolean pushProxyPackage(Long uid, String packageName, String fileMd5, String action, String sourceUrl) {
//		boolean flag = true;
//		String param = "";
//		Map<String, Object> game = new HashMap<String, Object>();
//		game.put("packageName", packageName);
//		game.put("fileMd5", fileMd5);
//		Gson gson = new Gson();
//		String info = gson.toJson(game);
//		try {
//			// 对sourceUrl这个参数进行一次编码，以区别于参数
//			param = "uid=" + String.valueOf(uid) + "&source_url=" + URLEncoder.encode(sourceUrl, "UTF-8") + "&info="
//					+ info + "&action=" + action;
//			System.out.println("fileupload:" + param);
//			String result = CommonService.sendPost(ackPackageisReadyUrl, param);
//			System.out.println("fileupload:send request to " + ackPackageisReadyUrl + " at "
//					+ System.currentTimeMillis());
//			Map<String, Object> map = (Map) gson.fromJson(result, Map.class);
//			System.out.println("fileupload:code :" + map.get("code"));
//			System.out.println("fileupload:" + ackPackageisReadyUrl + "?" + param);
//			System.out.println("fileupload:" + result);
//		} catch (Exception e) {
//			e.printStackTrace();
//			flag = false;
//		}
//		return flag;
//	}
//
//	// 通知 上下架代理包
//	@SuppressWarnings({ "unchecked", "rawtypes" })
//	public static String operateProxyPackage(Long uid, String packageName, String action) {
//		String param = "";
//		String result = "";
//		Map<String, Object> game = new HashMap<String, Object>();
//		game.put("packageName", packageName);
//		Gson gson = new Gson();
//		String info = gson.toJson(game);
//
//		try {
//			param = "uid=" + String.valueOf(uid) + "&info=" + info + "&action=" + action;
//			System.out.println("param:" + param);
//			result = CommonService.sendPost(ackPackageisReadyUrl, param);
//			System.out.println("shelves : proxy package at " + System.currentTimeMillis());
//			Map<String, Object> map = (Map) gson.fromJson(result, Map.class);
//			System.out.println("shelves : code :" + map.get("code"));
//			System.out.println("shelves : " + ackPackageisReadyUrl + "?" + param);
//			System.out.println("shelves : " + result);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return result;
//	}


}
