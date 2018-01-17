package com.zxd.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class CryptHelper {
    private static final char hexDigitsTab[] = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    
    static public String md5(String plainText){
        try {
            MessageDigest md5Md = MessageDigest.getInstance("MD5");
            md5Md.update(plainText.getBytes("utf-8"));

            byte tmp[] = md5Md.digest();
            char resBuf[] = new char[16 * 2];

            for (int i = 0, k = 0; i < 16; i++) {
                byte byte0 = tmp[i];
                resBuf[k++] = hexDigitsTab[byte0 >>> 4 & 0xf];
                resBuf[k++] = hexDigitsTab[byte0 & 0xf];
            }

            return new String(resBuf);
        } catch (NoSuchAlgorithmException e) {
            return "";
        } catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return "";
		}
    }
    
    static public String genSig(Map<?, ?> params, String secret) throws Exception {
        StringBuffer res = new StringBuffer("");
        List<String> list = new ArrayList<String>();
        for (Object object : params.keySet()) {
            list.add((String) object);
        }
        Collections.sort(list);
        for (String key : list) {
            if (!"sig".equals(key)) {
                String val = (String)params.get(key);
                res.append(key).append("=").append(val);
            }
        }
        res.append(secret);
//        System.out.println(res);
//        System.out.println(res.length());
//        System.out.println(md5(res.toString()));
//        System.out.println("Default Charset=" + Charset.defaultCharset());
        return md5(res.toString());
    }
}
