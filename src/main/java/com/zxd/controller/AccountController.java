package com.zxd.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.zxd.dao.MysqlDao;
import com.zxd.model.QueryData;
import com.zxd.service.DatabaseService;
import com.zxd.service.PlanQueryCommonMessage;
import com.zxd.service.WeixinService;
import com.alibaba.fastjson.JSON;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import static com.zxd.utils.JsonHelper.jsonpEntity;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.imageio.ImageIO;;

@Controller
@RequestMapping(value = "/account")
public class AccountController {

	@Autowired
	private DatabaseService databaseService;

	@Autowired
	private WeixinService weixinService;

	/**
	 * 测试连通
	 * 
	 * @param callback
	 * @return
	 */
	@RequestMapping(value = "ping.do")
	public ResponseEntity<String> ping(String callback) {
		return jsonpEntity("pong", callback);
	}

	@RequestMapping(value = "getQRimg.do")
	public ResponseEntity<String> getQRimg(String callback) {

		Long uid = PlanQueryCommonMessage.getUid();

		// String code = this.weixinService.getQRCode(uid);

		String code = "zhuxianda|朱贤达！";
		String reString = "";
		Hashtable hints = new Hashtable();
		hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

		if (code != null && !"".equals(code)) {
			// ServletOutputStream stream = null;
			int width = 200;// 图片的宽度
			int height = 200;// 高度
			// stream = resp.getOutputStream();
			QRCodeWriter writer = new QRCodeWriter();
			BitMatrix m;
			try {
				m = writer.encode(code, BarcodeFormat.QR_CODE, height, width, hints);
				// MatrixToImageWriter.writeToStream(m, "png", stream);
				// 将矩阵转为Image
				BufferedImage image = MatrixToImageWriter.toBufferedImage(m);
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				ImageIO.write(image, "png", out);// 将BufferedImage转成out输出流
				byte[] bytes = out.toByteArray();
				reString = this.weixinService.GetImageBase64(bytes);

			} catch (WriterException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("code", 100);
			result.put("msg", "查询成功");
			result.put("result", reString);
			return jsonpEntity(result, callback);
		}
		return null;
	}

	// 绑定微信账号
//	@RequestMapping(value = "bindWXInfo.do")
//	public ResponseEntity<String> bindWXInfo(String code, String callback, String info3rd) {
//		Map<String, Object> map = new HashMap<String, Object>();
//		String uid = this.weixinService.getRedis(code);
//		// uid="111";
//		Map<String, String> info3rdMap = this.weixinService.getInfo3rdMap(info3rd);
//		if (info3rdMap.get("type").equals("openid")) {
//			String openId = info3rdMap.get("value");
//			if (uid != null) {
//				if (this.weixinService.getWXUserUidBYOpenid(openId) == -1l) {
//					if (this.weixinService.bindWXInfo(uid, openId) == 1) {
//						map.put("code", 500);
//						map.put("info", "绑定成功！");
//						// 更新redis 中的 3rd 数据
//						String key = weixinService.set3rdInfo(openId, Long.parseLong(uid), true);
//						map.put("info3rd", key);
//					} else {
//						map.put("code", 400);
//						map.put("info", "数据写入失败！");
//					}
//				} else {
//					map.put("code", 400);
//					map.put("info", "该微信账号已经绑定!");
//				}
//			}
//		} else if (info3rdMap.get("type").equals("uid")) {
//			map.put("code", 400);
//			map.put("info", "该微信账号已经绑定!");
//		}
//
//		return jsonpEntity(map, callback);
//
//	}

	@RequestMapping(value = "getWeixinUserInfo.do")
	public ResponseEntity<String> getWeixinUserInfo(String code, String callback) {
		Map<String, Object> map = new HashMap<String, Object>();
		// String username = PlanQueryCommonMessage.getUsername();
		// map.put("code", StringUtils.hasText(username) ? 100 : 99);
		// map.put("username", username);
		//
		// // 返回告知是否为子账号
		// map.put("subAccount",
		// this.accountService.isAuthorizedAccount(PlanQueryCommonMessage.getUid())
		// ? 1 : 0);
		Map<String, Object> openIdMap = weixinService.getOpenIdMapByCode(code);
		String openid = (String) (openIdMap.get("openid"));

		if (openid == null) {
			map.put("code", 400);
			map.put("info", "该Code已经被使用了");
			return jsonpEntity(map, callback);
		}
		Long uid = weixinService.getWXUserUidBYOpenid(openid);
		if(uid == -1) {
			map.put("code", 400);
			map.put("info", "Mysql 写入数据失败");
			return jsonpEntity(map, callback);
		}
		String key = weixinService.set3rdInfo(uid);
		if(key == null) {
			map.put("code", 400);
			map.put("info", "Redis 写入数据失败");
			return jsonpEntity(map, callback);
		}
		
		map.put("code", 500);
		map.put("info", "执行成功");
		map.put("info3rd", key);
		
		return jsonpEntity(map, callback);
	}
}
